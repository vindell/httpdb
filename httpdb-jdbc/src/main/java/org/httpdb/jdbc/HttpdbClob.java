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

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.sql.Clob;
import java.sql.SQLException;

public class HttpdbClob implements Clob {

	public long length() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getSubString(long pos, int length) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Reader getCharacterStream() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public InputStream getAsciiStream() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public long position(String searchstr, long start) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public long position(Clob searchstr, long start) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public int setString(long pos, String str) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public int setString(long pos, String str, int offset, int len)
			throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public OutputStream setAsciiStream(long pos) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Writer setCharacterStream(long pos) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public void truncate(long len) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void free() throws SQLException {
		// TODO Auto-generated method stub

	}

	public Reader getCharacterStream(long pos, long length) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}
