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
package org.httpdb.events;


public class EventFinished extends HttpdbEvent {

	private StartEvent started;
	public StartEvent getStartEvent() {
		return started;
	}
	
	public EventFinished(StartEvent started) {
		super( started.getJob(),   started.getPkgIdentifier(),  started.getProcessStep());
		this.started = started;
	}
	
	/**
	 * Use this to signal the end of a Job.
	 * 
	 * @param job
	 * @param eventType
	 */
	public EventFinished(JobIdentifier job) {
		super( job);
	}

	/**
	 * Use this to signal the end of work on a specific pkg in a job.
	 * 
	 * @param job
	 * @param eventType
	 */
	public EventFinished(JobIdentifier job,  PackageIdentifier pkgIdentifier) {
		super( job,   pkgIdentifier);
	}
	
	/**
	 * Use this to signal the end of work on a specific pkg,
	 * where you didn't define an overall job.
	 * 
	 * @param job
	 * @param eventType
	 */
	public EventFinished(PackageIdentifier pkgIdentifier) { 
		super(  pkgIdentifier);
	}
	
	/**
	 * Use this to signal the end of a process step,
	 * where you didn't define an overall job.
	 * 
	 * @param job
	 * @param eventType
	 */
	public EventFinished(PackageIdentifier pkgIdentifier, ProcessStep processStep) {
		super( pkgIdentifier,  processStep);
	}
	
	/**
	 * Use this to signal the end of a process step,
	 * on some pkg in some overall job.
	 * 
	 * @param job
	 * @param eventType
	 */
	public EventFinished(JobIdentifier job,  PackageIdentifier pkgIdentifier, ProcessStep processStep) {
		
		super( job,   pkgIdentifier,  processStep);
	}		
	
	// Should the result of a job be available???
	
//	private Object result;
//	public Object getResult() {
//		return result;
//	}
	
}
