package org.httpdb.jdbc;


import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

import com.hg.httpdb.HttpPreparedStatement;
import com.hg.httpdb.HttpResultSet;
import com.hg.httpdb.HttpStatement;
/**
 * 
 * *******************************************************************
 * @className	： HttpdbConnection
 * @description	： 与特定数据库的连接（会话）。在连接上下文中执行 SQL 语句并返回结果。

		Connection 对象的数据库能够提供描述其表、所支持的 SQL 语法、存储过程、此连接功能等等的信息。此信息是使用 getMetaData 方法获得的。
		注：在配置 Connection 时，JDBC 应用程序应该使用适当的 Connection 方法，比如 setAutoCommit 或 setTransactionIsolation。在有可用的 JDBC 方法时，应用程序不能直接调用 SQL 命令更改连接的配置。默认情况下，Connection 对象处于自动提交模式下，这意味着它在执行每个语句后都会自动提交更改。如果禁用了自动提交模式，那么要提交更改就必须显式调用 commit 方法；否则无法保存数据库更改。
		使用 JDBC 2.1 核心 API 创建的新 Connection 对象有一个与之关联的最初为空的类型映射。用户可以为此类型映射中的 UDT 输入一个自定义映射关系。在使用 ResultSet.getObject 方法从数据源中获取 UDT 时，getObject 方法将检查该连接的类型映射是否有对应该 UDT 的条目。如果有，那么 getObject 方法将该 UDT 映射到所指示的类。如果没有条目，则使用标准映射关系映射该 UDT。
		用户可以创建一个新的类型映射，该映射是一个 java.util.Map 对象，可在其中创建一个条目，并将该条目传递给可以执行自定义映射关系的 java.sql 方法。在这种情况下，该方法将使用给定的类型映射，而不是与连接关联的映射。
		例如，以下代码片段指定 SQL 类型 ATHLETES 将被映射到 Java 编程语言中的 Athletes 类。该代码片段为 Connection 对象 con 获取类型映射，并在其中插入条目，然后使用新的条目将该类型映射设置为连接的类型映射。
	      java.util.Map map = con.getTypeMap();
	      map.put("mySchemaName.ATHLETES", Class.forName("Athletes"));
	      con.setTypeMap(map);
 * @author 		： <a href="mailto:hnxyhcwdl1003@163.com">wandalong</a>
 * @date		： Dec 17, 2016 10:40:27 PM
 * @version 	V1.0 
 * *******************************************************************
 */
public class HttpdbConnection implements Connection {
	
	protected String url;
	protected String user;
	protected String password;
	protected boolean autoCommit = true;
	protected boolean closeed = false;
	protected int transactionIsolation = Connection.TRANSACTION_SERIALIZABLE;
	protected Map typeMap = new HashMap();
	protected DatabaseMetaData metaData = null;
	protected static final String NOT_IMPLEMENT = "Not implement!";
	protected final AtomicInteger savePoint = new AtomicInteger(0);

	public HttpdbConnection(String url,Properties prop) throws SQLException {
		this(url, prop.getProperty("user", ""), prop.getProperty("password", ""));
    }
	
	public HttpdbConnection(String url, String user, String password) throws SQLException {
		this.url = url;
		this.user = user;
		this.password = password;
		if (!"ok".equals(((HttpResultSet) new HttpStatement(this).executeQuery("$hi$")).getRowSet().value(0, 0))) {
			throw new SQLException("服务无效或用户名口令错误");
		}
	}

	/**
     * Whether an SQLite library interface to the database has been established.
     * @throws SQLException.
     */
    protected void checkOpen() throws SQLException {
       throw new SQLException("database connection closed");
    }
    
    /**
     * 清除为此 Connection 对象报告的所有警告
     */
    public void clearWarnings() throws SQLException {
    	
    }

    /**
     * 立即释放此 Connection 对象的数据库和 JDBC 资源，而不是等待它们被自动释放
     */
    public void close() throws SQLException {
		
	}
    
    /**
     * 使所有上一次提交/回滚后进行的更改成为持久更改，并释放此 Connection 对象当前持有的所有数据库锁
     */
    public void commit() throws SQLException {
        checkOpen();
        if (autoCommit){
            throw new SQLException("database in auto-commit mode");
        }
    }
    
    /**
	 *  创建 Array 对象的工厂方法
	 */
	public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 构造实现 Blob 接口的对象
	 */
	public Blob createBlob() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 *  构造实现 Clob 接口的对象
	 */
	public Clob createClob() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 *  构造实现 NClob 接口的对象
	 */
	public NClob createNClob() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 *  构造实现 SQLXML 接口的对象
	 */
	public SQLXML createSQLXML() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
     *   创建一个 Statement 对象来将 SQL 语句发送到数据库
     */
    public Statement createStatement() throws SQLException {
        return createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY,ResultSet.CLOSE_CURSORS_AT_COMMIT);
    }

    /**
     *  创建一个 Statement 对象，该对象将生成具有给定类型和并发性的 ResultSet 对象。
     */
    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        return createStatement(resultSetType, resultSetConcurrency, ResultSet.CLOSE_CURSORS_AT_COMMIT);
    }

    /**
     * 创建一个 Statement 对象，该对象将生成具有给定类型、并发性和可保存性的 ResultSet 对象
     */
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException{
    	return null;
    }

    /**
     *  创建 Struct 对象的工厂方法
     */
    public Struct createStruct(String t, Object[] attr) throws SQLException {
        throw new SQLException("unsupported by httpdb.");
    }
    
    /**
     * 获取此 Connection 对象的当前自动提交模式
     */
    public boolean getAutoCommit() throws SQLException {
        checkOpen();
        return autoCommit;
    }
    

	/**
	 * 获取此 Connection 对象的当前目录名称
	 */
	public String getCatalog() throws SQLException {
		return null;
	}
	
	/**
	 * 返回一个列表，它包含驱动程序支持的每个客户端信息属性的名称和当前值
	 */
	public Properties getClientInfo() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 返回通过名称指定的客户端信息属性的值
	 */
	public String getClientInfo(String name) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

    /**
     * 获取使用此 Connection 对象创建的 ResultSet 对象的当前可保存性
     */
    public int getHoldability() throws SQLException {
        checkOpen();
        return ResultSet.CLOSE_CURSORS_AT_COMMIT;
    }
    

	/**
	 * 获取一个 DatabaseMetaData 对象，该对象包含关于此 Connection 对象所连接的数据库的元数据
	 */
	public DatabaseMetaData getMetaData() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
     * 获取此 Connection 对象的当前事务隔离级别
     */
    public int getTransactionIsolation() {
        return transactionIsolation;
    }
    
    /**
     * 获取与此 Connection 对象关联的 Map 对象
     */
    public Map<String,Class<?>> getTypeMap() throws SQLException {
        throw new SQLException("not yet implemented");
    }


    /**
     * 获取此 Connection 对象上的调用报告的第一个警告
     */
    public SQLWarning getWarnings() throws SQLException {
        return null;
    }
    
    /**
	 * 查询此 Connection 对象是否已经被关闭
	 */
	public boolean isClosed() throws SQLException {
		return false;
	}

    /**
     * 查询此 Connection 对象是否处于只读模式
     */
    public boolean isReadOnly() throws SQLException {
    	return true;
    }
    
	/**
	 * 如果连接尚未关闭并且仍然有效，则返回 true
	 */
	public boolean isValid(int timeout) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return false;
	}
	
	/**
     *  将给定的 SQL 语句转换成系统本机 SQL 语法
     */
    public String nativeSQL(String sql) {
        return sql;
    }
    

    /**
     *  创建一个 CallableStatement 对象来调用数据库存储过程
     */
    public CallableStatement prepareCall(String sql) throws SQLException {
        return prepareCall(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.CLOSE_CURSORS_AT_COMMIT);
    }

    /**
     *  创建一个 CallableStatement 对象，该对象将生成具有给定类型和并发性的 ResultSet 对象
     */
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return prepareCall(sql, resultSetType, resultSetConcurrency, ResultSet.CLOSE_CURSORS_AT_COMMIT);
    }

    /**
     *  创建一个 CallableStatement 对象，该对象将生成具有给定类型和并发性的 ResultSet 对象
     */
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException{
    	
    	return null;
    }

    /**
     *  创建一个 PreparedStatement 对象来将参数化的 SQL 语句发送到数据库
     */
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
    }

    /**
     *  创建一个默认 PreparedStatement 对象，该对象能获取自动生成的键
     */
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        return prepareStatement(sql);
    }

    /**
     *  创建一个能返回由给定数组指定的自动生成键的默认 PreparedStatement 对象
     */
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        return prepareStatement(sql);
    }

    /**
     *  创建一个能返回由给定数组指定的自动生成键的默认 PreparedStatement 对象
     */
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        return prepareStatement(sql);
    }

    /**
     *  创建一个 PreparedStatement 对象，该对象将生成具有给定类型和并发性的 ResultSet 对象
     */
    public PreparedStatement prepareStatement(String sql,int resultSetType, int resultSetConcurrency) throws SQLException {
        return prepareStatement(sql, resultSetType, resultSetConcurrency, ResultSet.CLOSE_CURSORS_AT_COMMIT);
    }

    /**
     *  创建一个 PreparedStatement 对象，该对象将生成具有给定类型、并发性和可保存性的 ResultSet 对象
     */
    public PreparedStatement prepareStatement(String sql,  int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException{
    	return new HttpPreparedStatement(this);
    }
    
    /**
     *  从当前事务中移除指定的 Savepoint 和后续 Savepoint 对象
     */
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
    	checkOpen();
    	if (autoCommit){
    		throw new SQLException("database in auto-commit mode");
    	}
    	//TODO
    }

    /**
     *  取消在当前事务中进行的所有更改，并释放此 Connection 对象当前持有的所有数据库锁
     */
    public void rollback() throws SQLException {
        checkOpen();
        if (autoCommit){
        	throw new SQLException("database in auto-commit mode");
        }
    }

    /**
     *  取消所有设置给定 Savepoint 对象之后进行的更改
     */
    public void rollback(Savepoint savepoint) throws SQLException {
    	checkOpen();
    	if (autoCommit){
    		throw new SQLException("database in auto-commit mode");
    	}
    	//db.exec(String.format("ROLLBACK TO SAVEPOINT %s", savepoint.getSavepointName()));
    	//TODO 远程回滚的逻辑代码
    }
    

    /**
     * 将此连接的自动提交模式设置为给定状态
     */
    public void setAutoCommit(boolean ac) throws SQLException {
        checkOpen();
        autoCommit = ac;
    }
    
    /**
     * 设置给定目录名称，以便选择要在其中进行工作的此 Connection 对象数据库的子空间
	 */
	public void setCatalog(String catalog) throws SQLException {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 设置连接的客户端信息属性的值
	 */
	public void setClientInfo(String name, String value) throws SQLClientInfoException {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 将 name 指定的客户端信息属性的值设置为 value 指定的值
	 */
	public void setClientInfo(Properties properties) throws SQLClientInfoException {
		// TODO Auto-generated method stub
		
	}
    
    /**
     * 将使用此 Connection 对象创建的 ResultSet 对象的默认可保存性 (holdability) 更改为给定可保存性
     */
    public void setHoldability(int h) throws SQLException {
        checkOpen();
        if (h != ResultSet.CLOSE_CURSORS_AT_COMMIT){
            throw new SQLException("Httpdb only supports CLOSE_CURSORS_AT_COMMIT");
        }
    }
    
    /**
     * 将此连接设置为只读模式，作为驱动程序启用数据库优化的提示。
     */
    public void setReadOnly(boolean ro) throws SQLException {
        // trying to change read-only flag
        if (ro != isReadOnly()) {
            throw new SQLException("Cannot change read-only flag after establishing a connection.");
        }
    }
    
    /**
     *    在当前事务中创建一个未命名的保存点 (savepoint)，并返回表示它的新 Savepoint 对象
     */
    public Savepoint setSavepoint() throws SQLException {
    	checkOpen();
    	if (autoCommit){
    		// when a SAVEPOINT is the outermost savepoint and not
    		// with a BEGIN...COMMIT then the behavior is the same as BEGIN DEFERRED TRANSACTION
    		autoCommit = false;
    	}
    	Savepoint sp = new HttpdbSavepoint(savePoint.incrementAndGet());
    	return sp;
    }

    /**
     * 在当前事务中创建一个具有给定名称的保存点，并返回表示它的新 Savepoint 对象
     */
    public Savepoint setSavepoint(String name) throws SQLException {
    	checkOpen();
    	if (autoCommit){
    		// when a SAVEPOINT is the outermost savepoint and not
    		// with a BEGIN...COMMIT then the behavior is the same as BEGIN DEFERRED TRANSACTION
    		autoCommit = false;
    	}
    	Savepoint sp = new HttpdbSavepoint(savePoint.incrementAndGet(), name);
    	return sp;
    }

    /**
     * 试图将此 Connection 对象的事务隔离级别更改为给定的级别
     */
    public void setTransactionIsolation(int level) throws SQLException {
        checkOpen();
        transactionIsolation = level;
    }
    
    /**
     * 将给定的 TypeMap 对象安装为此 Connection 对象的类型映射
     */
    @SuppressWarnings("rawtypes")
    public void setTypeMap(Map map) throws SQLException {
        throw new SQLException("not yet implemented");
    }

    public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	
   

    

    


    


    


    

    

    

	public String getURL() {
		return url;
	}

	public void setURL(String url) {
		this.url = url;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getSavepointID() {
		return 0;
	}

	

}

