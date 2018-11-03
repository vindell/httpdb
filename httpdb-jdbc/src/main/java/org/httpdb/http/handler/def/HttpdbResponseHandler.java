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
import java.io.ObjectOutputStream;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.httpdb.cmd.Command;
import org.httpdb.http.handler.ResponseHandler;
import org.httpdb.schema.HttpSchema;
import org.httpdb.utils.DataIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpdbResponseHandler implements ResponseHandler {

	protected static Logger LOG = LoggerFactory.getLogger(HttpdbResponseHandler.class);

	public String command() {
		return Command.CMD_DEF;
	}

	public HttpSchema handle(ResultSet rs) throws ServletException, IOException {
		return null;
	}

	public <T> void handle(HttpServletRequest request, HttpServletResponse response, T out) throws ServletException,
			IOException {
		try {
			response.setContentType("application/octet-stream");
			ObjectOutputStream output = new ObjectOutputStream(response.getOutputStream());
			DataIO.write(output, out, "");
			output.flush();
			output.close();
		} catch (Exception e) {
			LOG.error(e.getMessage());
		}
		
	}
	
}
