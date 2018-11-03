/*
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
package org.httpdb.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

/**
 * HttpDB测试
 * @author xdoc
 */
public class HttpDBTest {
    public static void main(String[] args) {
    	Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
        	Class.forName("com.hg.httpdb.Driver");
        	conn = DriverManager.getConnection("http://www.xdocin.com/httpdb", "xdoc", "123456");
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select * from emp");
            ResultSetMetaData rsmd = rs.getMetaData();
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
            	if (i > 1) {
            		System.out.print(",");
            	}
            	System.out.print(rsmd.getColumnName(i));
            }
            while (rs.next()) {
            	System.out.println();
            	for (int i = 1; i <= rsmd.getColumnCount(); i++) {
            		if (i > 1) {
            			System.out.print(",");
            		}
            		System.out.print(rs.getObject(i));
            	}
            }
        } catch (Exception e) {
        	e.printStackTrace();
        } finally {
        	try {
	            if (rs != null) rs.close();
	            if (stmt != null) stmt.close();
	            if (conn != null) conn.close();
            } catch (Exception e) {
            }
        }
    }
}