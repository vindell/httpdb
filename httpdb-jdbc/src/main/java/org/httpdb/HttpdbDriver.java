package org.httpdb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.Properties;

import org.httpdb.jdbc.HttpdbConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * *******************************************************************
 * @className	： HttpdbDriver
 * @description	： HttpDB JDBC驱动
 * @author 		： <a href="mailto:hnxyhcwdl1003@163.com">wandalong</a>
 * @date		： Dec 17, 2016 10:33:52 PM
 * @version 	V1.0 
 * *******************************************************************
 */
public class HttpdbDriver implements java.sql.Driver {
	
	protected static Logger LOG = LoggerFactory.getLogger(HttpdbDriver.class);
	
	static {
		try {
			DriverManager.registerDriver(new HttpdbDriver());
		} catch (SQLException e) {
			LOG.error(e.getMessage());
		}
	}
	
	/**
     * Creates a new database connection to a given URL.
     * @param url the URL
     * @param prop the properties
     * @return a Connection object that represents a connection to the URL
     * @throws SQLException
     * @see java.sql.Driver#connect(java.lang.String, java.util.Properties)
     */
    public static Connection createConnection(String url, Properties prop) throws SQLException {
        if (!isValidURL(url)){
            return null;
        }
        return new HttpdbConnection(url.trim(), prop);
    }
	
    /**
     * Validates a URL
     * @param url
     * @return true if the URL is valid, false otherwise
     */
    public static boolean isValidURL(String url) {
    	return isValidURL(url);
    }
    
    /**
	 * 试图创建一个到给定 URL 的数据库连接
	 */
	public Connection connect(String url, Properties prop) throws SQLException {
		return createConnection(url, prop);
	}

	/**
	 * 查询驱动程序是否认为它可以打开到给定 URL 的连接
	 */
	public boolean acceptsURL(String url) throws SQLException {
		if (url.startsWith("http://") || url.startsWith("https://")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获得此驱动程序的可能属性信息
	 */
	public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
		return new DriverPropertyInfo[] {};
	}

	/**
	 * 获取此驱动程序的主版本号
	 */
	public int getMajorVersion() {
		return HttpdbVersion.getMajorVersion();
	}

	/**
	 * 获得此驱动程序的次版本号
	 */
	public int getMinorVersion() {
		return HttpdbVersion.getMinorVersion();
	}

	/**
	 * 报告此驱动程序是否是一个真正的 JDBC CompliantTM 驱动程序
	 */
	public boolean jdbcCompliant() {
		return false;
	}
	
}
