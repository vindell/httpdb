/*
 * Copyright (c) 2010-2020, wandalong (hnxyhcwdl1003@163.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.httpdb.http.handler.def;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.httpdb.cmd.Command;
import org.httpdb.cmd.HttpdbCommand;
import org.httpdb.http.handler.CommandHandler;
import org.httpdb.schema.CmdSchema;
import org.httpdb.schema.HttpSchema;
import org.httpdb.utils.HttpdbUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hg.httpdb.DSMgr;
import com.hg.httpdb.To;

public class HttpdbCommandHandler implements CommandHandler {

	protected static Logger LOG = LoggerFactory.getLogger(HttpdbCommandHandler.class);
	
	public String command() {
		return Command.CMD_DEF;
	}

	public ResultSet handle(HttpSchema httpSchema) throws SQLException {

		Connection conn = null;
    	try {
			//请求命令概要对象
			CmdSchema cmdSchema = httpSchema.getCmdSchema();
			LOG.info("Connection Info:" + cmdSchema.toString());
			
			conn = DSMgr.getInstance().getConnection(cmdSchema.getDb());
      
			String password = DSMgr.getInstance().getPassword(cmdSchema.getDb());
			
			String sql = cmdSchema.getSql();
			Statement stmt = null;
			ResultSet rs = null;
			try {
				
				conn = connHandler.getConnection(cmdSchema);
				 
				if (sql.startsWith("$")) {
					String strs[] = sql.split(",");
					if (sql.startsWith("$hi$")) {
						rs = toResultSet("ok");
					} else if (sql.startsWith("$meta$")) {
						DatabaseMetaData meta = conn.getMetaData();
						
						
						//MetaDataSchema schema = HttpdbHandlerFactory.getMetaDataHandler(command).handle(meta);
						
						HttpdbCommand.exec(cmdSchema,conn);
						
						
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
		} catch (IOException e) {
			//关闭连接
			HttpdbUtils.closeQuietly(conn);
		}
		return null;
	}
	
}
