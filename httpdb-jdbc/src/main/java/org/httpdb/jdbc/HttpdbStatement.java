package org.httpdb.jdbc;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

import org.httpdb.utils.Crypt;
import org.httpdb.utils.DataIO;

import com.hg.httpdb.HttpConnection;
import com.hg.httpdb.HttpResultSet;
import com.hg.httpdb.HttpStatement;
import com.hg.httpdb.RowSet;

/**
 * 
 * *******************************************************************
 * @className	： HttpdbStatement
 * @description	： TODO(描述这个类的作用)
 * @author 		： <a href="mailto:hnxyhcwdl1003@163.com">wandalong</a>
 * @date		： Dec 15, 2016 10:48:39 PM
 * @version 	V1.0 
 * *******************************************************************
 */
public class HttpdbStatement implements Statement {
	
	protected int maxFieldSize = Integer.MAX_VALUE;
	protected int maxRows = Integer.MAX_VALUE;
	protected int quertTimeout = Integer.MAX_VALUE;
	protected int updateCount = 0;
	protected int curResult = -1;
	protected int fetchDirection = ResultSet.FETCH_UNKNOWN;
	protected StringBuffer batchSql = new StringBuffer();
	protected HttpConnection conn;
	protected ResultSet resultSet;

	public HttpdbStatement(HttpConnection conn) {
		this.conn = conn;
	}

	public ResultSet executeQuery(String sql) throws SQLException {
		HttpURLConnection httpConn;
		
		try {
			String baseURL = this.conn.getUrl();
			
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			
			
			
			//HttpConnectionUtils.httpRequestWithPost(baseURL, paramsMap, charset, handler);
			
			
			httpConn = (HttpURLConnection) new URL(baseURL).openConnection();
			httpConn.setRequestProperty("User-Agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows 2000)");
			httpConn.setDoOutput(true);
			OutputStream out = httpConn.getOutputStream();
			out.write("&db=".getBytes());
			out.write(URLEncoder.encode(this.conn.getUser(), "UTF-8").getBytes());
			out.write("&sql=".getBytes());
			out.write(URLEncoder.encode(
					Crypt.encrypt(sql, this.conn.getPassword()), "UTF-8")
					.getBytes());
			if (this.maxRows != Integer.MAX_VALUE) {
				out.write("&maxrows=".getBytes());
				out.write(String.valueOf(this.maxRows).getBytes());
			}
			out.flush();
			Object res = DataIO.read(httpConn.getInputStream(),
					this.conn.getPassword());
			if (res instanceof RowSet) {
				return new HttpResultSet((RowSet) res);
			} else {
				return new HttpResultSet(new RowSet());
			}
		} catch (Exception e) {
			throw new SQLException(e);
		}
	}

	public int executeUpdate(String sql) throws SQLException {
		return 0;
	}

	public void close() throws SQLException {
		if (this.resultSet != null) {
			this.resultSet.close();
		}
	}

	public int getMaxFieldSize() throws SQLException {
		return this.maxFieldSize;
	}

	public void setMaxFieldSize(int max) throws SQLException {
		this.maxFieldSize = max;
	}

	public int getMaxRows() throws SQLException {
		return this.maxRows;
	}

	public void setMaxRows(int max) throws SQLException {
		this.maxRows = max;
	}

	public void setEscapeProcessing(boolean enable) throws SQLException {
	}

	public int getQueryTimeout() throws SQLException {
		return quertTimeout;
	}

	public void setQueryTimeout(int seconds) throws SQLException {
		this.quertTimeout = seconds;
	}

	public void cancel() throws SQLException {
	}

	public SQLWarning getWarnings() throws SQLException {
		return null;
	}

	public void clearWarnings() throws SQLException {
	}

	public void setCursorName(String name) throws SQLException {
	}

	public boolean execute(String sql) throws SQLException {
		this.resultSet = this.executeQuery(sql);
		return true;
	}

	public ResultSet getResultSet() throws SQLException {
		return this.resultSet;
	}

	public int getUpdateCount() throws SQLException {
		return updateCount;
	}

	public boolean getMoreResults() throws SQLException {
		if (curResult > 1) {
			return false;
		} else {
			curResult++;
			return true;
		}
	}

	public void setFetchDirection(int direction) throws SQLException {
		this.fetchDirection = direction;
	}

	public int getFetchDirection() throws SQLException {
		return this.fetchDirection;
	}

	private int fetchSize = 100;

	public void setFetchSize(int rows) throws SQLException {
		this.fetchSize = rows;
	}

	public int getFetchSize() throws SQLException {
		return this.fetchSize;
	}

	public int getResultSetConcurrency() throws SQLException {
		return ResultSet.CONCUR_READ_ONLY;
	}

	public int getResultSetType() throws SQLException {
		if (this.resultSet != null) {
			return this.resultSet.getType();
		} else {
			throw new SQLException("数据集为空");
		}
	}

	public void addBatch(String sql) throws SQLException {
		batchSql.append(sql).append(";");
	}

	public void clearBatch() throws SQLException {
		batchSql.setLength(0);
	}

	public int[] executeBatch() throws SQLException {
		if (this.execute(this.batchSql.toString())) {
			return new int[] { 1 };
		} else {
			return new int[] { 0 };
		}
	}

	public Connection getConnection() throws SQLException {
		return this.conn;
	}

	public boolean getMoreResults(int current) throws SQLException {
		// 只支持单记录集合
		return false;
	}

	public ResultSet getGeneratedKeys() throws SQLException {
		return null;
	}

	public int executeUpdate(String sql, int autoGeneratedKeys)
			throws SQLException {
		// 忽略key
		return this.executeUpdate(sql);
	}

	public int executeUpdate(String sql, int[] columnIndexes)
			throws SQLException {
		// 忽略key
		return this.executeUpdate(sql);
	}

	public int executeUpdate(String sql, String[] columnNames)
			throws SQLException {
		return this.executeUpdate(sql);
	}

	public boolean execute(String sql, int autoGeneratedKeys)
			throws SQLException {
		return this.execute(sql);
	}

	public boolean execute(String sql, int[] columnIndexes) throws SQLException {
		return this.execute(sql);
	}

	public boolean execute(String sql, String[] columnNames)
			throws SQLException {
		return this.execute(sql);
	}

	public int getResultSetHoldability() throws SQLException {
		return ResultSet.CLOSE_CURSORS_AT_COMMIT;
	}

	public boolean isClosed() throws SQLException {
		return false;
	}

	public void setPoolable(boolean poolable) throws SQLException {
	}

	public boolean isPoolable() throws SQLException {
		return false;
	}

	public Object unwrap(Class arg0) throws SQLException {
		return null;
	}

	public boolean isWrapperFor(Class arg0) throws SQLException {
		return false;
	}
	
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

	public HttpdbConnection(String url, String user, String password) throws SQLException {
		this.url = url;
		this.user = user;
		this.password = password;
		if (!"ok".equals(((HttpResultSet) new HttpStatement(this).executeQuery("$hi$")).getRowSet().value(0, 0))) {
			throw new SQLException("服务无效或用户名口令错误");
		}
	}
	
	protected HttpdbConnection(String url,Properties prop) throws SQLException {
        
    }

	/**
     * Whether an SQLite library interface to the database has been established.
     * @throws SQLException.
     */
    protected void checkOpen() throws SQLException {
       throw new SQLException("database connection closed");
    }
    
    /**
     * 获取此 Connection 对象的当前目录名称
     */
    public String getCatalog() throws SQLException {
        checkOpen();
        return null;
    }

    /**
     * 设置给定目录名称，以便选择要在其中进行工作的此 Connection 对象数据库的子空间。
     */
    public void setCatalog(String catalog) throws SQLException {
        checkOpen();
    }

    /**
     * 获取使用此 Connection 对象创建的 ResultSet 对象的当前可保存性
     */
    public int getHoldability() throws SQLException {
        checkOpen();
        return ResultSet.CLOSE_CURSORS_AT_COMMIT;
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
     * 获取此 Connection 对象的当前事务隔离级别
     */
    public int getTransactionIsolation() {
        return transactionIsolation;
    }

    /**
     * 试图将此 Connection 对象的事务隔离级别更改为给定的级别
     */
    public void setTransactionIsolation(int level) throws SQLException {
        checkOpen();
        transactionIsolation = level;
    }
    
    /**
     * 获取与此 Connection 对象关联的 Map 对象
     */
    public Map<String,Class<?>> getTypeMap() throws SQLException {
        throw new SQLException("not yet implemented");
    }

    /**
     * 将给定的 TypeMap 对象安装为此 Connection 对象的类型映射
     */
    @SuppressWarnings("rawtypes")
    public void setTypeMap(Map map) throws SQLException {
        throw new SQLException("not yet implemented");
    }

    /**
     * 查询此 Connection 对象是否处于只读模式
     */
    public boolean isReadOnly() throws SQLException {
    	return true;
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
     *  获取一个 DatabaseMetaData 对象，该对象包含关于此 Connection 对象所连接的数据库的元数据
     */
    public abstract DatabaseMetaData getMetaData() throws SQLException;

    /**
     *  将给定的 SQL 语句转换成系统本机 SQL 语法
     */
    public String nativeSQL(String sql) {
        return sql;
    }

    /**
     *  清除为此 Connection 对象报告的所有警告
     */
    public void clearWarnings() throws SQLException {}

    /**
     * 获取此 Connection 对象上的调用报告的第一个警告
     */
    public SQLWarning getWarnings() throws SQLException {
        return null;
    }

    /**
     * 获取此 Connection 对象的当前自动提交模式
     */
    public boolean getAutoCommit() throws SQLException {
        checkOpen();
        return autoCommit;
    }

    /**
     * 将此连接的自动提交模式设置为给定状态
     */
    public void setAutoCommit(boolean ac) throws SQLException {
        checkOpen();
        autoCommit = ac;
    }

    /**
     *  使所有上一次提交/回滚后进行的更改成为持久更改，并释放此 Connection 对象当前持有的所有数据库锁。
     */
    public void commit() throws SQLException {
        checkOpen();
        if (autoCommit){
            throw new SQLException("database in auto-commit mode");
        }
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
    public abstract Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException;

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
    public abstract  CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException;

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
    public abstract PreparedStatement prepareStatement(String sql,  int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException;

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

    // UNUSED FUNCTIONS /////////////////////////////////////////////

    /**
     *  创建 Struct 对象的工厂方法
     */
    public Struct createStruct(String t, Object[] attr) throws SQLException {
        throw new SQLException("unsupported by httpdb.");
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
