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
package org.httpdb.cmd;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.servlet.ServletConfig;

import org.httpdb.cmd.rec.CmdReceiver;
import org.httpdb.exception.BeanInstantiationException;
import org.httpdb.http.Parameter;
import org.httpdb.http.Parameters;
import org.httpdb.utils.BeanUtils;
import org.httpdb.utils.StringUtils;
/**
 * 
 * *******************************************************************
 * @className	： CmdReceiverFactory
 * @description	： 命令接受对象
 * @author 		： <a href="mailto:hnxyhcwdl1003@163.com">wandalong</a>
 * @date		： Dec 16, 2016 5:52:17 PM
 * @version 	V1.0 
 * *******************************************************************
 */
public class CmdReceiverFactory {
	
	protected static ConcurrentMap<String, CmdReceiver> COMPLIED_CMD_RECEIVER = new ConcurrentHashMap<String, CmdReceiver>();
	private static class SingletonHolder {  
		private static final CmdReceiverFactory INSTANCE = new CmdReceiverFactory();  
    }  
	
    private CmdReceiverFactory (){}  
    
    public static final CmdReceiverFactory getInstance() {  
    	return SingletonHolder.INSTANCE;  
    }

	public static CmdReceiverFactory initialize(ServletConfig config) {
		
		String[] cmd_receivers = Parameters.getStringArray(Parameter.HTTPDB_CMD_RECEIVERS);
		for (String className : cmd_receivers) {
			if (StringUtils.isNotEmpty(className)) {
	 			try {
	 				CmdReceiver ret = BeanUtils.instantiateClass(Class.forName(className), CmdReceiver.class);
	 				COMPLIED_CMD_RECEIVER.putIfAbsent(ret.command(), ret);
				} catch (BeanInstantiationException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
	 		}
		}
		return getInstance();
	}
 
	public static CmdReceiver getCmdReceiver(String command) {
 		if (StringUtils.isNotEmpty(command)) {
 			CmdReceiver ret = COMPLIED_CMD_RECEIVER.get(command);
 			if (ret != null) {
 				return ret;
 			}
 		}
 		return null;
 	}
	
}
