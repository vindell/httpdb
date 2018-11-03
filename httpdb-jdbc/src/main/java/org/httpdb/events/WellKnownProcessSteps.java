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


public enum WellKnownProcessSteps implements ProcessStep {
	
	NOT_SET,
	PKG_LOAD, // query whether we should bother having PKG_LOAD 
	PKG_SAVE,	
	BIND_INSERT_XML,
	BIND_BIND_XML,
	BIND_REMOVE_SDT,
	BIND_REMOVE_XML,
	PDF,
	OUT_XsltExporterDelegate,
	OUT_AbstractVisitorExporterDelegate,
	//FO_XSLT_NON,
	FO_EXTENTS,
	FOP_RENDER_PASS1,
	FOP_RENDER_PASS2,
	CONVERT_PREPROCESS,
	HTML_OUT,
	XHTML_IMPORT;	

}
