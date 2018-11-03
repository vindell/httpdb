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
package org.httpdb.http.async;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.AsyncContext;

public class AsyncRequestProcessorThread implements Runnable {

	private AsyncContext asyncContext;
	private int secs;

	public AsyncRequestProcessorThread() {
	}

	public AsyncRequestProcessorThread(AsyncContext asyncCtx, int secs) {
		this.asyncContext = asyncCtx;
		this.secs = secs;
	}

	public void run() {
		System.out.println("Async Supported? " + asyncContext.getRequest().isAsyncSupported());
		longProcessing(secs);
		try {
			PrintWriter out = asyncContext.getResponse().getWriter();
			out.write("Processing done for " + secs + " milliseconds!!");
		} catch (IOException e) {
			e.printStackTrace();
		}
		//complete the processing
		asyncContext.complete();
	}

	private void longProcessing(int secs) {
		// wait for given time before finishing
		try {
			Thread.sleep(secs);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}