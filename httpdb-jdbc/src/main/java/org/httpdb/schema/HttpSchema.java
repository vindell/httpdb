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
package org.httpdb.schema;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.httpdb.utils.Crypt;

import com.hg.httpdb.DSMgr;

/**
 * 
 * *******************************************************************
 * @className	： HttpSchema
 * @description	： 当前Http请求的概要描述
 * @author 		： <a href="mailto:hnxyhcwdl1003@163.com">wandalong</a>
 * @date		： Dec 16, 2016 3:00:15 PM
 * @version 	V1.0 
 * *******************************************************************
 */
public class HttpSchema {
	
	protected HttpServletRequest request;
	
	public HttpSchema(HttpServletRequest request){
		this.request = request;
	}

	public Map<String, String[]> getParams() {
		return request.getParameterMap();
	}
	
	public CmdSchema getCmdSchema() throws IOException {
		String cmd	= request.getParameter("cmd");
		String db = request.getParameter("db");
		String user = request.getParameter("user");
		String pwd	= request.getParameter("pwd");
		String sql	= request.getParameter("sql");
		 	   sql = Crypt.decrypt(sql, DSMgr.getInstance().getPassword(db));
		return new CmdSchema(cmd,  db , user, pwd, sql);
	}
	
	public boolean certified() throws IOException {
		return getCmdSchema().certified();
	}
	
	
}
