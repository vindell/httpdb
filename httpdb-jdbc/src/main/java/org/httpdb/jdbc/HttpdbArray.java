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
package org.httpdb.jdbc;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * *******************************************************************
 * @className	： HttpdbArray
 * @description	： TODO(描述这个类的作用)
 * @author 		： <a href="mailto:hnxyhcwdl1003@163.com">wandalong</a>
 * @date		： Dec 17, 2016 11:23:56 PM
 * @version 	V1.0 
 * *******************************************************************
 */
public class HttpdbArray implements Array {

	 //-------------
    volatile boolean closed;
    Object[]         data;
    
	public String getBaseTypeName() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public int getBaseType() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public Object getArray() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getArray(Map<String, Class<?>> map) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getArray(long index, int count) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getArray(long index, int count, Map<String, Class<?>> map)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public ResultSet getResultSet() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public ResultSet getResultSet(Map<String, Class<?>> map) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public ResultSet getResultSet(long index, int count) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public ResultSet getResultSet(long index, int count, Map<String, Class<?>> map) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public void free() throws SQLException {
		// TODO Auto-generated method stub
		
	}

}
