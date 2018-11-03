package org.httpdb.jdbc;

import java.io.PrintWriter;
import java.io.Serializable;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

import javax.sql.CommonDataSource;

@SuppressWarnings("serial")
public abstract class JDBCCommonDataSource implements CommonDataSource, Serializable {
	
	// ------------------------ internal implementation ------------------------
    protected Properties connectionProps = new Properties();

    /** description of data source - informational */
    protected String description = null;

    /** name of data source - informational */
    protected String dataSourceName = null;

    /** name of server - informational */
    protected String serverName = null;

    /** network protocol - informational */
    protected String networkProtocol = null;

    /** login timeout */
    protected int loginTimeout = 0;

    /** log writer */
    protected transient PrintWriter logWriter;

    /** connection user */
    protected String user = null;

    /** connection password */
    protected String password = null;

    /** database URL */
    protected String url = null;
    
	public PrintWriter getLogWriter() throws SQLException {
		return logWriter;
	}
	
	public void setLogWriter(java.io.PrintWriter out) throws SQLException {
        logWriter = out;
    }
	
	public void setLoginTimeout(int seconds) throws SQLException {
        loginTimeout = seconds;
        connectionProps.setProperty("loginTimeout", Integer.toString(loginTimeout));
    }
	
	public int getLoginTimeout() throws SQLException {
        return loginTimeout;
    }
	
	 // ------------------------ custom public methods ------------------------

    /**
     * Retrieves the description of the data source. <p>
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Retrieves the name of the data source. <p>
     *
     * @return the description
     */
    public String getDataSourceName() {
        return dataSourceName;
    }

    /**
     * Retrieves the network protocol of the data source. <p>
     *
     * @return the network protocol
     */
    public String getNetworkProtocol() {
        return networkProtocol;
    }

    /**
     * Retrieves the server name attribute. <p>
     *
     * @return the server name attribute
     */
    public String getServerName() {
        return serverName;
    }

    /**
     * Synonym for getUrl().
     *
     * @return the jdbc database connection url attribute
     */
    public String getDatabaseName() {
        return url;
    }

    /**
     * Synonym for getUrl().
     *
     * @return the jdbc database connection url attribute
     */
    public String getDatabase() {
        return url;
    }

    /**
     * Retrieves the jdbc database connection url attribute. <p>
     *
     * @return the jdbc database connection url attribute
     */
    public String getUrl() {
        return url;
    }

    /**
     * Retrieves the jdbc database connection url attribute. <p>
     *
     * @return the jdbc database connection url attribute
     */
    public String getURL() {
        return url;
    }

    /**
     * Retrieves the user name for the connection. <p>
     *
     * @return the username for the connection
     */
    public String getUser() {
        return user;
    }

    /**
     * Synonym for setUrl(String). <p>
     *
     * @param databaseName the new value for the attribute
     */
    public void setDatabaseName(String databaseName) {
        this.url = databaseName;
    }

    /**
     * Synonym for setUrl(String). <p>
     *
     * @param database the new value for the attribute
     */
    public void setDatabase(String database) {
        this.url = database;
    }

    /**
     * Sets the jdbc database URL. <p>
     *
     * @param url the new value of this object's jdbc database connection
     *      url attribute
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Sets the jdbc database URL. <p>
     *
     * @param url the new value of this object's jdbc database connection
     *      url attribute
     */
    public void setURL(String url) {
        this.url = url;
    }

    /**
     * Sets the password for the user name.
     *
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
        connectionProps.setProperty("password", password);
    }

    /**
     * Sets the user name.
     *
     * @param user the user id
     */
    public void setUser(String user) {
        this.user = user;
        connectionProps.setProperty("user", user);
    }

    /**
     * Sets connection properties. If user / password / loginTimeout has been
     * set with one of the setXXX() methods it will be added to the Properties
     * object.
     * @param props properties.  If null, then existing properties will be cleared/replaced.
     */
    public void setProperties(Properties props) {

        connectionProps = (props == null) ? new Properties() : (Properties) props.clone();

        if (user != null) {
            connectionProps.setProperty("user", user);
        }

        if (password != null) {
            connectionProps.setProperty("password", password);
        }

        if (loginTimeout != 0) {
            connectionProps.setProperty("loginTimeout", Integer.toString(loginTimeout));
        }
    }

    //------------------------- JDBC 4.1 -----------------------------------
    
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException("getParentLogger");
    }
    
    
    
}
