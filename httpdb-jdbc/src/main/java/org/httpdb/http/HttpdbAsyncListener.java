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
package org.httpdb.http;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletResponse;

public class HttpdbAsyncListener implements AsyncListener {

	public void onComplete(AsyncEvent asyncEvent) throws IOException {
		System.out.println("AppAsyncListener onComplete");
		// we can do resource cleanup activity here
	}
	
	public void onError(AsyncEvent asyncEvent) throws IOException {
		System.out.println("AppAsyncListener onError");
		//we can return error response to client
	}

	
	public void onStartAsync(AsyncEvent asyncEvent) throws IOException {
		System.out.println("AppAsyncListener onStartAsync");
		//we can log the event here
	}
	
	public void onTimeout(AsyncEvent asyncEvent) throws IOException {
		System.out.println("AppAsyncListener onTimeout");
		//we can send appropriate response to client
		ServletResponse response = asyncEvent.getAsyncContext().getResponse();
		PrintWriter out = response.getWriter();
		out.write("TimeOut Error in Processing");
	}
	
}