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
package org.httpdb.utils;

import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.httpdb.schema.ColumnType;

import com.hg.httpdb.HttpResultSet;
import com.hg.httpdb.RowSet;
import com.hg.httpdb.To;

public final class HttpdbUtils {


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
			ObjectOutputStream output = new ObjectOutputStream(res.getOutputStream());
			DataIO.write(output, obj, password);
			output.flush();
			output.close();
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
	
	public static void closeQuietly(final Connection conn) {
        try {
            if (conn != null) {
            	conn.close();
            }
        } catch (final SQLException ioe) {
            // ignore
		}
    }
	
	public static void closeQuietly(final Statement stmt) {
        try {
            if (stmt != null) {
            	stmt.close();
            }
        } catch (final SQLException ioe) {
            // ignore
		}
    }
	
	public static void closeQuietly(final ResultSet rs) {
        try {
            if (rs != null) {
            	rs.close();
            }
        } catch (final SQLException ioe) {
            // ignore
		}
    }
	
}
