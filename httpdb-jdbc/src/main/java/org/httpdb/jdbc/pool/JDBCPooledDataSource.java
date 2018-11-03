package org.httpdb.jdbc.pool;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Properties;

import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.Referenceable;
import javax.naming.StringRefAddr;
import javax.sql.ConnectionPoolDataSource;
import javax.sql.PooledConnection;

import org.httpdb.HttpdbDataSource;

public class JDBCPooledDataSource extends HttpdbDataSource implements ConnectionPoolDataSource, Serializable, Referenceable {

    /**
     * Default constructor.
     */
    public JDBCPooledDataSource () {
        super();
    }

    /**
     * @see javax.sql.ConnectionPoolDataSource#getPooledConnection()
     */
    public PooledConnection getPooledConnection() throws SQLException {
        return getPooledConnection(null, null);
    }

    /**
     * @see javax.sql.ConnectionPoolDataSource#getPooledConnection(java.lang.String, java.lang.String)
     */
    public PooledConnection getPooledConnection(String user, String password) throws SQLException {
        return new JDBCPooledDataSource(getConnection(user, password));
    }
    
    public PooledConnection getPooledConnection() throws SQLException {

        JDBCConnection connection =
            (JDBCConnection) JDBCDriver.getConnection(url, connectionProps);

        return new JDBCPooledConnection(connection);
    }

    public PooledConnection getPooledConnection(String user,
            String password) throws SQLException {

        Properties props = new Properties();

        props.setProperty("user", user);
        props.setProperty("password", password);

        JDBCConnection connection =
            (JDBCConnection) JDBCDriver.getConnection(url, props);

        return new JDBCPooledConnection(connection);
    }

    /**
     * Retrieves the Reference of this object.
     *
     * @return The non-null javax.naming.Reference of this object.
     * @exception NamingException If a naming exception was encountered
     *          while retrieving the reference.
     */
    public Reference getReference() throws NamingException {

        String    cname = "org.hsqldb.jdbc.JDBCDataSourceFactory";
        Reference ref   = new Reference(getClass().getName(), cname, null);

        ref.add(new StringRefAddr("database", getDatabase()));
        ref.add(new StringRefAddr("user", getUser()));
        ref.add(new StringRefAddr("password", password));
        ref.add(new StringRefAddr("loginTimeout",
                                  Integer.toString(loginTimeout)));

        return ref;
    }
    
}