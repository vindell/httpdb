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
import java.sql.Blob;
import java.sql.SQLException;

public class HttpdbBlob implements Blob {

	public long length() throws SQLException {
		return 0;
	}

	public byte[] getBytes(long pos, int length) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public InputStream getBinaryStream() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public long position(byte[] pattern, long start) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public long position(Blob pattern, long start) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public int setBytes(long pos, byte[] bytes) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public int setBytes(long pos, byte[] bytes, int offset, int len)
			throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public OutputStream setBinaryStream(long pos) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public void truncate(long len) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void free() throws SQLException {
		// TODO Auto-generated method stub

	}

	public InputStream getBinaryStream(long pos, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}
