package org.httpdb.http;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.httpdb.HttpdbConstants;
import org.httpdb.http.async.AsyncRequestProcessorThread;
import org.httpdb.schema.ColumnType;
import org.httpdb.utils.Crypt;
import org.httpdb.utils.DataIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hg.httpdb.DSMgr;
import com.hg.httpdb.HttpResultSet;
import com.hg.httpdb.RowSet;
import com.hg.httpdb.To;

/**
 * 
 * *******************************************************************
 * @className	： HttpdbServerAsyncServlet
 * @description	： Httpdb 异步Web服务接口
 * @author 		： <a href="mailto:hnxyhcwdl1003@163.com">wandalong</a>
 * @date		： Dec 16, 2016 1:32:47 PM
 * @version 	V1.0 
 * *******************************************************************
 */
@SuppressWarnings("serial")
public class HttpdbServerAsyncServlet extends HttpdbServerServlet {
	
	protected static Logger LOG = LoggerFactory.getLogger(HttpdbServerAsyncServlet.class);
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		try {
			
			// 初始化webapp参数取值对象
			Parameters.initialize(config);
			// 检查是否启用异步请求处理
			boolean  pool_disable = Parameters.getBoolean(Parameter.HTTPDB_HTTP_ASYNC_POOL_DISABLED, "true");
			if(!pool_disable){
				int corePoolSize 	= Parameters.getInt(Parameter.HTTPDB_HTTP_ASYNC_POOL_MIN_SIZE , "100");
				int maximumPoolSize = Parameters.getInt(Parameter.HTTPDB_HTTP_ASYNC_POOL_MAX_SIZE , "200");
				long keepAliveTime 	= Parameters.getLong(Parameter.HTTPDB_HTTP_ASYNC_POOL_ALIVE_TIME , "50000L");
				int workQueueSize 	= Parameters.getInt(Parameter.HTTPDB_HTTP_ASYNC_POOL_QUEUE_SIZE , "100");
				// create the thread pool
				ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.MILLISECONDS, new ArrayBlockingQueue(workQueueSize));
				config.getServletContext().setAttribute(HttpdbConstants.HTTPDB_EXECUTOR_STRING, executor);
			}
			
			DSMgr.getInstance().init(config.getServletContext().getResourceAsStream("/WEB-INF/httpdb.xml"));
		} catch (Exception e) {
			LOG.error(e.getMessage());
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(!request.isAsyncSupported()){
			LOG.warn("Async Request IS Not Supported .");
			super.doPost(request, response);
			return;
		}
		request.setCharacterEncoding("UTF-8");
		
		long startTime = System.currentTimeMillis();
		LOG.info("AsyncLongRunningServlet Start::Name=" + Thread.currentThread().getName() + "::ID=" + Thread.currentThread().getId());
		request.setAttribute("org.apache.catalina.ASYNC_SUPPORTED", true);

		String time = request.getParameter("time");
		int secs = Integer.valueOf(time);
		// max 10 seconds
		if (secs > 10000){
			secs = 10000;
		}

		AsyncContext asyncCtx = request.startAsync();
		asyncCtx.addListener(new HttpdbAsyncListener());
		asyncCtx.setTimeout(9000);

		//获取容器中绑定的线程池对象
		ThreadPoolExecutor executor = (ThreadPoolExecutor) request.getServletContext().getAttribute(HttpdbConstants.HTTPDB_EXECUTOR_STRING);

		executor.execute(new AsyncRequestProcessorThread(asyncCtx, secs));
		long endTime = System.currentTimeMillis();
		System.out.println("AsyncLongRunningServlet End::Name="
				+ Thread.currentThread().getName() + "::ID="
				+ Thread.currentThread().getId() + "::Time Taken="
				+ (endTime - startTime) + " ms.");
		request.getRequestDispatcher("/index.jsp").forward(request, response);
		
		
		
		AsyncContext ac = request.startAsync();   
		ac.setTimeout(10 * 60 * 1000); 
        ac.addListener(new AsyncListener() { 
            public void onComplete(AsyncEvent event) throws IOException { 
               
            } 

            public void onTimeout(AsyncEvent event) throws IOException { 
                
            } 

            public void onError(AsyncEvent event) throws IOException { 
                
            } 

            public void onStartAsync(AsyncEvent event) throws IOException { 
            } 
        }); 
        
        executor.execute(new AsyncRequestProcessorThread(request.startAsync(), secs));
    	endTime = System.currentTimeMillis();
    	System.out.println("AsyncLongRunningServlet End::Name="
    			+ Thread.currentThread().getName() + "::ID="
    			+ Thread.currentThread().getId() + "::Time Taken="
    			+ (endTime - startTime) + " ms.");
    	
    	
		
		String db = request.getParameter("db");
        String sql = request.getParameter("sql");
        String password = DSMgr.getInstance().getPassword(db);
        sql = Crypt.decrypt(sql, DSMgr.getInstance().getPassword(db));
        Connection conn = null;
        if (db != null && sql != null && sql.length() > 0) {
        	Statement stmt = null;
        	ResultSet rs = null;
        	try {
        		conn = DSMgr.getInstance().getConnection(db);
        		LOG.info(db + ":" + sql);
        		if (sql.startsWith("$")) {
        			String strs[] = sql.split(",");
            		if (sql.startsWith("$hi$")) {
            			rs = toResultSet("ok");
            		} else if (sql.startsWith("$meta$")) {
            			DatabaseMetaData meta = conn.getMetaData();
            			Map<String,Object> map = new HashMap<String,Object>();
            		/*	meta.allProceduresAreCallable()
            			meta.allTablesAreSelectable()
            			meta.autoCommitFailureClosesAllResultSets()
            			meta.dataDefinitionCausesTransactionCommit()
            			meta.dataDefinitionIgnoredInTransactions()
            			meta.doesMaxRowSizeIncludeBlobs()
            			meta.getCatalogTerm()
            			meta.supportsUnionAll()
            			meta.storesLowerCaseIdentifiers()
            			meta.storesLowerCaseQuotedIdentifiers()
            			meta.storesMixedCaseIdentifiers()
            			meta.storesMixedCaseQuotedIdentifiers()
            			meta.storesUpperCaseIdentifiers()
            			meta.supportsAlterTableWithAddColumn()
            			meta.supportsAlterTableWithDropColumn()*/
            			
            			
            			
            			
            			map.put("DatabaseProductName", meta.getDatabaseProductName());
            			map.put("DatabaseProductVersion", meta.getDatabaseProductVersion());
            			map.put("DatabaseMajorVersion", String.valueOf(meta.getDatabaseMajorVersion()));
            			map.put("DatabaseMinorVersion", String.valueOf(meta.getDatabaseMinorVersion()));
            			map.put("DriverName", String.valueOf(meta.getDriverName()));
            			map.put("DriverVersion", String.valueOf(meta.getDriverVersion()));
            			map.put("supportsSchemasInTableDefinitions", String.valueOf(meta.supportsSchemasInTableDefinitions()));
                		rs = toResultSet(map);
                	} else if (sql.startsWith("$catalogs$")) {
                		rs = conn.getMetaData().getCatalogs();
                	} else if (sql.startsWith("$schemas$")) {
                		try {
                			rs = conn.getMetaData().getSchemas(metaParam(strs, 0), null);
                		} catch (Throwable e) {
                			rs = conn.getMetaData().getSchemas();
                		}
            		} else if (sql.startsWith("$tables$")) {
            			rs = conn.getMetaData().getTables(metaParam(strs, 0), metaParam(strs, 1), null, null);
            		} else if (sql.startsWith("$columns$")) {
            			rs = conn.getMetaData().getColumns(metaParam(strs, 0), metaParam(strs, 1), metaParam(strs, 2), null);
	        		} else {
	        			throw new Exception("无效元数据查询");
	        		}
        		} else if (sql.trim().toLowerCase().startsWith("select")) {
        			stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        			if (request.getParameter("maxrows") != null) {
        				stmt.setMaxRows(To.toInt(request.getParameter("maxrows"), Integer.MAX_VALUE));
        			}
        			rs = stmt.executeQuery(sql);
        		} else {
        			throw new Exception("无效查询语句");
        		}
        		
        		
        		
        		write(request, response, toRowSet(rs), password);
        	} catch (Exception e) {
        		LOG.error(e.getMessage());
        		write(request, response, e, password);
        	} finally {
        		if (rs != null) {
        			try {
        				rs.close();
        			} catch (Exception e) {
        			}
        		}
        		if (stmt != null) {
        			try {
        				stmt.close();
        			} catch (Exception e) {
        			}
        		}
        		if (conn != null) {
        			try {
        				conn.close();
        			} catch (Exception e) {
        			}
        		}
        	}
        }
	}

	private String metaParam(String[] strs, int inx) {
		if (strs.length > inx + 1 && !strs[inx + 1].equals("null")) {
			return strs[inx + 1];
		} else {
			return null;
		}
	}

	private void write(HttpServletRequest req, HttpServletResponse res, Object obj, String password) {
		try {
			res.setContentType("application/octet-stream");
			ServletOutputStream out = res.getOutputStream();
			DataIO.write(out, obj, password);
			out.flush();
			out.close();
		} catch (Exception e) {
			LOG.error(e.getMessage());
		}
	}
	private Object toRowSet(ResultSet rs) throws SQLException {
        RowSet rowSet = new RowSet();
        ColumnType field;
        ResultSetMetaData rsmd = rs.getMetaData();
        for (int i = 1; i <= rsmd.getColumnCount(); i++) {
        	field = new ColumnType(String.valueOf(rsmd.getColumnName(i)));
        	rowSet.getFields().add(field);
        }
        Object obj;
        Map map;
        while (rs.next()) {
        	map = new HashMap();
        	for (int i = 1; i <= rowSet.getFields().size(); i++) {
        		obj = rs.getObject(i);
        		map.put(rowSet.fieldAt(i - 1).getName(), To.toString(obj));
        	}
        	rowSet.add(map);
        }
        return rowSet;
    }
	
	private ResultSet toResultSet(String str) {
        RowSet rowSet = new RowSet();
        rowSet.getFields().add(new ColumnType("STRING"));
        Map map = new HashMap();
        rowSet.add(map);
        map.put("STRING", str);
        return new HttpResultSet(rowSet);
	}
	
	private ResultSet toResultSet(Map map) {
		RowSet rowSet = new RowSet();
        rowSet.getFields().add(new ColumnType("KEY"));
        rowSet.getFields().add(new ColumnType("VALUE"));
        Iterator it = map.keySet().iterator();
        Map row;
        String key;
        while (it.hasNext()) {
        	key = (String) it.next();
        	row = new HashMap();
        	rowSet.add(row);
        	row.put("KEY", key);
        	row.put("VALUE", To.toString(map.get(key)));
        }
        return new HttpResultSet(rowSet);
	}
	
	@Override
	public void destroy() {
		super.destroy();
		ThreadPoolExecutor executor = (ThreadPoolExecutor) Parameters.getServletContext().getAttribute(HttpdbConstants.HTTPDB_EXECUTOR_STRING);
		if(executor != null){
			executor.shutdown();
		}
	}
}
