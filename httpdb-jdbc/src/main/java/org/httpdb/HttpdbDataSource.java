package org.httpdb;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

/**
 * 
 * @author Administrator
 *数据源实现
 */
public class HttpdbDataSource implements javax.sql.DataSource {
	
	private String driver;
	private String jdbcUrl;
	private String user;
	private String password;
	
	public Connection getConnection() throws SQLException {
		return this.getConnection(this.user, this.password);
	}
	
	public Connection getConnection(String username, String password) throws SQLException {
		try {
			Class.forName(this.driver).newInstance();
			return DriverManager.getConnection(this.jdbcUrl, username, password);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
    }

	public void setDriverClass(String driver) {
		this.driver = driver;
	}

	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public PrintWriter getLogWriter() throws SQLException {
		return null;
	}

	public void setLogWriter(PrintWriter out) throws SQLException {
	}

	public void setLoginTimeout(int seconds) throws SQLException {
	}

	public int getLoginTimeout() throws SQLException {
		return 0;
	}

	public Object unwrap(Class arg0) throws SQLException {
		return null;
	}

	public boolean isWrapperFor(Class arg0) throws SQLException {
		return false;
	}
	
	 /**
     * @see javax.sql.DataSource#getLogWriter()
     */
    public PrintWriter getLogWriter() throws SQLException {
        return logger;
    }

    /**
     * @see javax.sql.DataSource#getLoginTimeout()
     */
    public int getLoginTimeout() throws SQLException {
        return loginTimeout;
    }

    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException("getParentLogger");
    }

    /**
     * @see javax.sql.DataSource#setLogWriter(java.io.PrintWriter)
     */
    public void setLogWriter(PrintWriter out) throws SQLException {
        this.logger = out;
    }

    /**
     * @see javax.sql.DataSource#setLoginTimeout(int)
     */
    public void setLoginTimeout(int seconds) throws SQLException {
        loginTimeout = seconds;
    }

    /**
     * Determines if this object wraps a given class.
     * @param iface The class to check.
     * @return True if it is an instance of the current class; false otherwise.
     * @throws SQLException
     */
    public boolean isWrapperFor(Class< ? > iface) throws SQLException {
        return iface.isInstance(this);
    }

    /**
     * Casts this object to the given class.
     * @param iface The class to cast to.
     * @return The casted class.
     * @throws SQLException
     */
    @SuppressWarnings("unchecked")
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return (T) this;
    }
}
