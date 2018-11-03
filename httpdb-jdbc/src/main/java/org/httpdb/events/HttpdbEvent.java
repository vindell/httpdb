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


import net.engio.mbassy.bus.MBassador;

public abstract class HttpdbEvent {
	
	private static MBassador<HttpdbEvent> bus;
	public static void setEventNotifier(MBassador<HttpdbEvent> bus) {
		HttpdbEvent.bus = bus;
	}	
	
	private static Boolean publishAsynch = null;
	public static Boolean publishAsynch() {
		if (publishAsynch==null) {
			publishAsynch = false; //Docx4jProperties.getProperty("docx4j.events.HttpdbEvent.PublishAsync", false);
		}
		return publishAsynch;
	}

	public static void setPublishAsynch(Boolean publishAsynch) {
		HttpdbEvent.publishAsynch = publishAsynch;
	}
	
	/* 
	 * The 3 constructors are designed to facilitate the one-liner:
	 * HttpdbEvent.publish(bus, new HttpdbEvent( Job.MERGE, EventType.PKG_STARTED));
	 */
	private PackageIdentifier pkgIdentifier=null; // where null, the event is assumed to be job level
	public PackageIdentifier getPkgIdentifier() {
		return pkgIdentifier;
	}
	
	/**
	 * Use this to signal the start or end of a Job.
	 * 
	 * @param job
	 * @param eventType
	 */
	public HttpdbEvent(JobIdentifier job) {
		this.job = job;
		
		
	}

	/**
	 * Use this to signal the start or end of work on a specific pkg in a job.
	 * 
	 * @param job
	 * @param eventType
	 */
	public HttpdbEvent(JobIdentifier job,  PackageIdentifier pkgIdentifier) {
		this.job = job;
		this.pkgIdentifier = pkgIdentifier;
	}
	
	/**
	 * Use this to signal the start or end of work on a specific pkg, where you didn't define an overall job.
	 * 
	 * @param job
	 * @param eventType
	 */
	public HttpdbEvent(PackageIdentifier pkgIdentifier) { 
		this.job = WellKnownJobTypes.ANONYMOUS;
		this.pkgIdentifier = pkgIdentifier;
	}
	
	/**
	 * Use this to signal the start or end of a process step, where you didn't define an overall job.
	 * @param job
	 * @param eventType
	 */
	public HttpdbEvent(PackageIdentifier pkgIdentifier, ProcessStep processStep) {
		this.job = WellKnownJobTypes.ANONYMOUS;
		this.pkgIdentifier = pkgIdentifier;
		this.processStep = processStep;
	}
	
	public HttpdbEvent(JobIdentifier job,  PackageIdentifier pkgIdentifier, ProcessStep processStep) {
		this.job = job;
		this.pkgIdentifier = pkgIdentifier;
		this.processStep = processStep;
	}

	
	public void publish() {
    	if (bus!=null) {
    		if (publishAsynch()) {
    			bus.publishAsync(this);
    		} else {
    			// predictable order
    			bus.publish(this);    			
    		}
    	}				
	}
	

	private JobIdentifier job; 
	public JobIdentifier getJob() {
		return job;
	}

	private ProcessStep processStep;
	/**
	 * Retrieve the step in the process, if set.  You can create your own set of steps
	 * (for things which happen in your user code ie outside docx4j's source code)
	 * by implementing the ProcessStep interface.  
	 * @return
	 */
	public ProcessStep getProcessStep() {
		return processStep;
	}
	public void setProcessStep(ProcessStep processStep) {
		this.processStep = processStep;
	}
	
//	private OpcPackage pkg;
//	/**
//	 * Retrieve the pkg, for example if you want to get some user data you've set on it.
//	 * @return
//	 */
//	public OpcPackage getPkg() {
//		return pkg;
//	}
//	public void setPkg(OpcPackage pkg) {
//		this.pkg = pkg;
//	}

	
}
