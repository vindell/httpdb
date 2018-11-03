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
package org.httpdb.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.httpdb.exception.BeanInstantiationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeanUtils {

	protected static Logger LOG = LoggerFactory.getLogger(BeanUtils.class);

	private BeanUtils() {
	}

	/**
	 * Convenience method to instantiate a class using its no-arg constructor.
	 * As this method doesn't try to load classes by name, it should avoid
	 * class-loading issues.
	 * @param clazz class to instantiate
	 * @return the new instance
	 * @throws BeanInstantiationException if the bean cannot be instantiated
	 */
	public static <T> T instantiate(Class<T> clazz) throws BeanInstantiationException {
		Assert.notNull(clazz, "Class must not be null");
		if (clazz.isInterface()) {
			throw new BeanInstantiationException(clazz, "Specified class is an interface");
		}
		try {
			return clazz.newInstance();
		}
		catch (InstantiationException ex) {
			throw new BeanInstantiationException(clazz, "Is it an abstract class?", ex);
		}
		catch (IllegalAccessException ex) {
			throw new BeanInstantiationException(clazz, "Is the constructor accessible?", ex);
		}
	}

	/**
	 * Instantiate a class using its no-arg constructor.
	 * As this method doesn't try to load classes by name, it should avoid
	 * class-loading issues.
	 * <p>Note that this method tries to set the constructor accessible
	 * if given a non-accessible (that is, non-public) constructor.
	 * @param clazz class to instantiate
	 * @return the new instance
	 * @throws BeanInstantiationException if the bean cannot be instantiated
	 */
	public static <T> T instantiateClass(Class<T> clazz) throws BeanInstantiationException {
		Assert.notNull(clazz, "Class must not be null");
		if (clazz.isInterface()) {
			throw new BeanInstantiationException(clazz, "Specified class is an interface");
		}
		try {
			return instantiateClass(clazz.getDeclaredConstructor());
		}
		catch (NoSuchMethodException ex) {
			throw new BeanInstantiationException(clazz, "No default constructor found", ex);
		}
	}

	/**
	 * Instantiate a class using its no-arg constructor and return the new instance
	 * as the the specified assignable type.
	 * <p>Useful in cases where
	 * the type of the class to instantiate (clazz) is not available, but the type
	 * desired (assignableTo) is known.
	 * <p>As this method doesn't try to load classes by name, it should avoid
	 * class-loading issues.
	 * <p>Note that this method tries to set the constructor accessible
	 * if given a non-accessible (that is, non-public) constructor.
	 * @param clazz class to instantiate
	 * @param assignableTo type that clazz must be assignableTo
	 * @return the new instance
	 * @throws BeanInstantiationException if the bean cannot be instantiated
	 */
	@SuppressWarnings("unchecked")
	public static <T> T instantiateClass(Class<?> clazz, Class<T> assignableTo) throws BeanInstantiationException {
		Assert.isAssignable(assignableTo, clazz);
		return (T) instantiateClass(clazz);
	}

	/**
	 * Convenience method to instantiate a class using the given constructor.
	 * As this method doesn't try to load classes by name, it should avoid
	 * class-loading issues.
	 * <p>Note that this method tries to set the constructor accessible
	 * if given a non-accessible (that is, non-public) constructor.
	 * @param ctor the constructor to instantiate
	 * @param args the constructor arguments to apply
	 * @return the new instance
	 * @throws BeanInstantiationException if the bean cannot be instantiated
	 */
	public static <T> T instantiateClass(Constructor<T> ctor, Object... args) throws BeanInstantiationException {
		Assert.notNull(ctor, "Constructor must not be null");
		try {
			if ((!Modifier.isPublic(ctor.getModifiers()) || !Modifier.isPublic(ctor.getDeclaringClass().getModifiers())) && !ctor.isAccessible()) {
				ctor.setAccessible(true);
			}
			return ctor.newInstance(args);
		}
		catch (InstantiationException ex) {
			throw new BeanInstantiationException(ctor.getDeclaringClass(),
					"Is it an abstract class?", ex);
		}
		catch (IllegalAccessException ex) {
			throw new BeanInstantiationException(ctor.getDeclaringClass(),
					"Is the constructor accessible?", ex);
		}
		catch (IllegalArgumentException ex) {
			throw new BeanInstantiationException(ctor.getDeclaringClass(),
					"Illegal arguments for constructor", ex);
		}
		catch (InvocationTargetException ex) {
			throw new BeanInstantiationException(ctor.getDeclaringClass(),
					"Constructor threw exception", ex.getTargetException());
		}
	}

	/**
	 * Find a method with the given method name and the given parameter types,
	 * declared on the given class or one of its superclasses. Prefers public methods,
	 * but will return a protected, package access, or private method too.
	 * <p>Checks {@code Class.getMethod} first, falling back to
	 * {@code findDeclaredMethod}. This allows to find public methods
	 * without issues even in environments with restricted Java security settings.
	 * @param clazz the class to check
	 * @param methodName the name of the method to find
	 * @param paramTypes the parameter types of the method to find
	 * @return the Method object, or {@code null} if not found
	 * @see Class#getMethod
	 * @see #findDeclaredMethod
	 */
	public static Method findMethod(Class<?> clazz, String methodName, Class<?>... paramTypes) {
		try {
			return clazz.getMethod(methodName, paramTypes);
		}
		catch (NoSuchMethodException ex) {
			return findDeclaredMethod(clazz, methodName, paramTypes);
		}
	}

	/**
	 * Find a method with the given method name and the given parameter types,
	 * declared on the given class or one of its superclasses. Will return a public,
	 * protected, package access, or private method.
	 * <p>Checks {@code Class.getDeclaredMethod}, cascading upwards to all superclasses.
	 * @param clazz the class to check
	 * @param methodName the name of the method to find
	 * @param paramTypes the parameter types of the method to find
	 * @return the Method object, or {@code null} if not found
	 * @see Class#getDeclaredMethod
	 */
	public static Method findDeclaredMethod(Class<?> clazz, String methodName, Class<?>... paramTypes) {
		try {
			return clazz.getDeclaredMethod(methodName, paramTypes);
		}
		catch (NoSuchMethodException ex) {
			if (clazz.getSuperclass() != null) {
				return findDeclaredMethod(clazz.getSuperclass(), methodName, paramTypes);
			}
			return null;
		}
	}

	/**
	 * Find a method with the given method name and minimal parameters (best case: none),
	 * declared on the given class or one of its superclasses. Prefers public methods,
	 * but will return a protected, package access, or private method too.
	 * <p>Checks {@code Class.getMethods} first, falling back to
	 * {@code findDeclaredMethodWithMinimalParameters}. This allows for finding public
	 * methods without issues even in environments with restricted Java security settings.
	 * @param clazz the class to check
	 * @param methodName the name of the method to find
	 * @return the Method object, or {@code null} if not found
	 * @throws IllegalArgumentException if methods of the given name were found but
	 * could not be resolved to a unique method with minimal parameters
	 * @see Class#getMethods
	 * @see #findDeclaredMethodWithMinimalParameters
	 */
	public static Method findMethodWithMinimalParameters(Class<?> clazz, String methodName)
			throws IllegalArgumentException {

		Method targetMethod = findMethodWithMinimalParameters(clazz.getMethods(), methodName);
		if (targetMethod == null) {
			targetMethod = findDeclaredMethodWithMinimalParameters(clazz, methodName);
		}
		return targetMethod;
	}

	/**
	 * Find a method with the given method name and minimal parameters (best case: none),
	 * declared on the given class or one of its superclasses. Will return a public,
	 * protected, package access, or private method.
	 * <p>Checks {@code Class.getDeclaredMethods}, cascading upwards to all superclasses.
	 * @param clazz the class to check
	 * @param methodName the name of the method to find
	 * @return the Method object, or {@code null} if not found
	 * @throws IllegalArgumentException if methods of the given name were found but
	 * could not be resolved to a unique method with minimal parameters
	 * @see Class#getDeclaredMethods
	 */
	public static Method findDeclaredMethodWithMinimalParameters(Class<?> clazz, String methodName)
			throws IllegalArgumentException {

		Method targetMethod = findMethodWithMinimalParameters(clazz.getDeclaredMethods(), methodName);
		if (targetMethod == null && clazz.getSuperclass() != null) {
			targetMethod = findDeclaredMethodWithMinimalParameters(clazz.getSuperclass(), methodName);
		}
		return targetMethod;
	}

	/**
	 * Find a method with the given method name and minimal parameters (best case: none)
	 * in the given list of methods.
	 * @param methods the methods to check
	 * @param methodName the name of the method to find
	 * @return the Method object, or {@code null} if not found
	 * @throws IllegalArgumentException if methods of the given name were found but
	 * could not be resolved to a unique method with minimal parameters
	 */
	public static Method findMethodWithMinimalParameters(Method[] methods, String methodName)
			throws IllegalArgumentException {

		Method targetMethod = null;
		int numMethodsFoundWithCurrentMinimumArgs = 0;
		for (Method method : methods) {
			if (method.getName().equals(methodName)) {
				int numParams = method.getParameterTypes().length;
				if (targetMethod == null || numParams < targetMethod.getParameterTypes().length) {
					targetMethod = method;
					numMethodsFoundWithCurrentMinimumArgs = 1;
				}
				else {
					if (targetMethod.getParameterTypes().length == numParams) {
						// Additional candidate with same length
						numMethodsFoundWithCurrentMinimumArgs++;
					}
				}
			}
		}
		if (numMethodsFoundWithCurrentMinimumArgs > 1) {
			throw new IllegalArgumentException("Cannot resolve method '" + methodName +
					"' to a unique method. Attempted to resolve to overloaded method with " +
					"the least number of parameters, but there were " +
					numMethodsFoundWithCurrentMinimumArgs + " candidates.");
		}
		return targetMethod;
	}

	/**
	 * 
	 * @description ： 检测类是否包含属性
	 * @author ： <a href="mailto:hnxyhcwdl1003@163.com">wandalong</a>
	 * @date ：Dec 30, 2015 9:16:49 AM
	 * @param clazz
	 * @param propertyName
	 * @return
	 */
	public static boolean hasProperty(Class<?> clazz, String propertyName) {
		boolean has = false;
		try {
			new PropertyDescriptor(propertyName, clazz);
			has = true;
		} catch (IntrospectionException e) {
			LOG.info(clazz.getName() + ":" + e.getMessage());
			return false;
		}
		return has;
	}

	public static PropertyDescriptor[] getPropertyDescriptors(Class<?> clazz) {
		BeanInfo beanInfo = getBeanInfo(clazz);
		return beanInfo.getPropertyDescriptors();
	}

	/**
	 * 
	 * @description ： 根据bean的类取得bean的信息
	 * @author ： <a href="mailto:hnxyhcwdl1003@163.com">wandalong</a>
	 * @date ：Dec 30, 2015 9:17:40 AM
	 * @param clazz
	 * @return
	 */
	public static BeanInfo getBeanInfo(Class<?> clazz) {
		BeanInfo beanInfo = null;
		try {
			beanInfo = Introspector.getBeanInfo(clazz);
		} catch (IntrospectionException e) {
			LOG.error(e.getMessage(), e);
		}
		return beanInfo;
	}

	public static List<Method> findMethods(Class<?> clazz, String regex) {
		List<Method> methods = new ArrayList<Method>();
		BeanInfo beanInfo = getBeanInfo(clazz);
		String mathodName = null;
		for (MethodDescriptor descriptor : beanInfo.getMethodDescriptors()) {
			mathodName = descriptor.getMethod().getName();
			if (mathodName.matches(regex)) {
				methods.add(descriptor.getMethod());
			}
		}
		return methods;
	}

	public static List<String> findMethodNames(Class<?> clazz, String regex) {
		List<String> methods = new ArrayList<String>();
		BeanInfo beanInfo = getBeanInfo(clazz);
		String mathodName = null;
		for (MethodDescriptor descriptor : beanInfo.getMethodDescriptors()) {
			mathodName = descriptor.getMethod().getName();
			if (mathodName.matches(regex)) {
				methods.add(mathodName);
			}
		}
		return methods;
	}

}
