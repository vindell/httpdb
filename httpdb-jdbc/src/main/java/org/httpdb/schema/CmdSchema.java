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

public class CmdSchema {

	protected String cmd;
	protected String db;
	protected String user;
	protected String pwd;
	protected String sql;

	public CmdSchema() {
	}
	
	public CmdSchema(String cmd, String db, String user, String pwd, String sql) {
		this.cmd = cmd;
		this.db = db;
		this.user = user;
		this.pwd = pwd;
		this.sql = sql;
	}

	public boolean certified() {
		return db != null && sql != null && sql.length() > 0;
	}
		
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Cmd Schema [");
			builder.append("cmd:").append(cmd).append(",");
			builder.append("db:").append(db).append(",");
			builder.append("user:").append(user).append(",");
			builder.append("pwd:").append("******").append(",");
			builder.append("sql:").append(sql);
		builder.append("]");
		return builder.toString();
	}
	
	public String getDb() {
		return db;
	}

	public void setDb(String db) {
		this.db = db;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

}
