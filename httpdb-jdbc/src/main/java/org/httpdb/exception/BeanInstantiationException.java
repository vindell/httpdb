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
package org.httpdb.exception;

/**
 * 
 * *******************************************************************
 * @className	： BeanInstantiationException
 * @description	： Bean实例化异常
 * @author 		： <a href="mailto:hnxyhcwdl1003@163.com">wandalong</a>
 * @date		： Feb 26, 2016 11:03:21 PM
 * @version 	V1.0 
 * *******************************************************************
 */
@SuppressWarnings("serial")
public class BeanInstantiationException  extends RuntimeException {
	
	private Class<?> beanClass;
	/**
	 * Create a new BeanInstantiationException.
	 * @param beanClass the offending bean class
	 * @param msg the detail message
	 */
	public BeanInstantiationException(Class<?> beanClass, String msg) {
		this(beanClass, msg, null);
	}

	/**
	 * Create a new BeanInstantiationException.
	 * @param beanClass the offending bean class
	 * @param msg the detail message
	 * @param cause the root cause
	 */
	public BeanInstantiationException(Class<?> beanClass, String msg, Throwable cause) {
		super("Failed to instantiate [" + beanClass.getName() + "]: " + msg, cause);
		this.beanClass = beanClass;
	}


	/**
	 * Return the offending bean class.
	 */
	public Class<?> getBeanClass() {
		return this.beanClass;
	}

}

 
