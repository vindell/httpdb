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

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.servlet.ServletConfig;

import org.httpdb.cmd.Command;
import org.httpdb.exception.BeanInstantiationException;
import org.httpdb.http.handler.CommandHandler;
import org.httpdb.http.handler.RequestHandler;
import org.httpdb.http.handler.ResponseHandler;
import org.httpdb.utils.BeanUtils;
import org.httpdb.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class HttpdbHandlerFactory {
	
	protected static Logger LOG = LoggerFactory.getLogger(HttpdbHandlerFactory.class);
	protected static ConcurrentMap<String, RequestHandler> COMPLIED_REQUEST_HANDLER = new ConcurrentHashMap<String, RequestHandler>();
	protected static ConcurrentMap<String, ResponseHandler> COMPLIED_RESPONSE_HANDLER = new ConcurrentHashMap<String, ResponseHandler>();
	protected static ConcurrentMap<String, CommandHandler> COMPLIED_COMMAND_HANDLER = new ConcurrentHashMap<String, CommandHandler>();
	
	private static class SingletonHolder {  
		private static final HttpdbHandlerFactory INSTANCE = new HttpdbHandlerFactory();  
    }  
	
    private HttpdbHandlerFactory (){}  
    
    public static final HttpdbHandlerFactory getInstance() {  
    	return SingletonHolder.INSTANCE;  
    }  

	public static HttpdbHandlerFactory initialize(ServletConfig config) {
		//初始化Request请求处理Handler
		init_request_handlers();
		//初始化Response请求处理Handler
		init_response_handlers();
		//初始化Command请求处理Handler
		init_command_handlers();
		//返回工厂单例对象
		return getInstance();
	}

    private static void init_request_handlers() {
    	String[] request_handlers = Parameters.getStringArray(Parameter.HTTPDB_REQUEST_HANDLERS);
		for (String className : request_handlers) {
			if (StringUtils.isNotEmpty(className)) {
	 			try {
	 				RequestHandler ret = BeanUtils.instantiateClass(Class.forName(className), RequestHandler.class);
	 				//此段逻辑是为了方便调用者扩展默认的Handler实现
	 				if(Command.CMD_DEF.equals(ret.command())){
	 					COMPLIED_REQUEST_HANDLER.remove(ret.command());
	 				}
	 				COMPLIED_REQUEST_HANDLER.putIfAbsent(ret.command(), ret);
				} catch (BeanInstantiationException e) {
					LOG.error(e.getMessage());
				} catch (ClassNotFoundException e) {
					LOG.error(e.getMessage());
				}
	 		}
		}
	}
    
    private static void init_response_handlers() {
    	String[] response_handlers = Parameters.getStringArray(Parameter.HTTPDB_RESPONSE_HANDLERS);
		for (String className : response_handlers) {
			
			if (StringUtils.isNotEmpty(className)) {
	 			try {
	 				ResponseHandler ret = BeanUtils.instantiateClass(Class.forName(className), ResponseHandler.class);
	 				//此段逻辑是为了方便调用者扩展默认的Handler实现
	 				if(Command.CMD_DEF.equals(ret.command())){
	 					COMPLIED_RESPONSE_HANDLER.remove(ret.command());
	 				}
	 				COMPLIED_RESPONSE_HANDLER.putIfAbsent(ret.command(), ret);
				} catch (BeanInstantiationException e) {
					LOG.error(e.getMessage());
				} catch (ClassNotFoundException e) {
					LOG.error(e.getMessage());
				}
	 		}
		}
    }
    
    private static void init_command_handlers() {
    	String[] command_handlers = Parameters.getStringArray(Parameter.HTTPDB_COMMAND_HANDLERS);
		for (String className : command_handlers) {
			if (StringUtils.isNotEmpty(className)) {
	 			try {
	 				CommandHandler ret = BeanUtils.instantiateClass(Class.forName(className), CommandHandler.class);
	 				//此段逻辑是为了方便调用者扩展默认的Handler实现
	 				if(Command.CMD_DEF.equals(ret.command())){
	 					COMPLIED_COMMAND_HANDLER.remove(ret.command());
	 				}
	 				COMPLIED_COMMAND_HANDLER.putIfAbsent(ret.command(), ret);
				} catch (BeanInstantiationException e) {
					LOG.error(e.getMessage());
				} catch (ClassNotFoundException e) {
					LOG.error(e.getMessage());
				}
	 		}
		}
    }
	
	public static RequestHandler getRequestHandler(String command) {
 		if (StringUtils.isNotEmpty(command)) {
 			RequestHandler ret = COMPLIED_REQUEST_HANDLER.get(command);
 			if (ret != null) {
 				return ret;
 			}
 		}
 		return COMPLIED_REQUEST_HANDLER.get(Command.CMD_DEF);
 	}
	
	public static ResponseHandler getResponseHandler(String command) {
 		if (StringUtils.isNotEmpty(command)) {
 			ResponseHandler ret = COMPLIED_RESPONSE_HANDLER.get(command);
 			if (ret != null) {
 				return ret;
 			}
 		}
 		return COMPLIED_RESPONSE_HANDLER.get(Command.CMD_DEF);
 	}
	
	public static CommandHandler getCommandHandler(String command) {
		if (StringUtils.isNotEmpty(command)) {
			CommandHandler ret = COMPLIED_COMMAND_HANDLER.get(command);
 			if (ret != null) {
 				return ret;
 			}
 		}
 		return COMPLIED_COMMAND_HANDLER.get(Command.CMD_DEF);
 	}
	
	
}
