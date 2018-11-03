package org.httpdb.http;

import javax.servlet.FilterConfig;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import org.httpdb.utils.StringUtils;

/**
 * 
 * *******************************************************************
 * @className	： Parameters
 * @description	： TODO(描述这个类的作用)
 * @author 		： <a href="mailto:hnxyhcwdl1003@163.com">wandalong</a>
 * @date		： Sep 17, 2016 9:21:34 PM
 * @version 	V1.0 
 * *******************************************************************
 */
public abstract class Parameters {
	
	static final String PARAMETER_SYSTEM_PREFIX = "httpdb.";

	private static ServletConfig servletConfig;
	private static FilterConfig filterConfig;
	private static ServletContext servletContext;

	private Parameters() {
		super();
	}

	public static void initialize(ServletConfig config) {
		servletConfig = config;
		if (config != null) {
			final ServletContext context = config.getServletContext();
			initialize(context);
		}
	}
	
	public static void initialize(FilterConfig config) {
		filterConfig = config;
		if (config != null) {
			final ServletContext context = config.getServletContext();
			initialize(context);
		}
	}

	public static void initialize(ServletContext context) {
		servletContext = context;
	}

	public static ServletContext getServletContext() {
		assert servletContext != null;
		return servletContext;
	}

	/**
	 * 单个Boolean值解析
	 */
	public static boolean getBoolean(Parameter parameter,String def) {
		assert parameter != null;
		final String name = parameter.getCode();
		String para = getParameterByName(name);
		return Boolean.parseBoolean( para == null ? def : para);
	}
	
	/**
	 * 单个Int值解析
	 */
	public static int getInt(Parameter parameter,String def) {
		assert parameter != null;
		final String name = parameter.getCode();
		String para = getParameterByName(name);
		return Integer.parseInt(para == null ? def : para);
	}
	
	/**
	 * 单个Long值解析
	 */
	public static long getLong(Parameter parameter,String def) {
		assert parameter != null;
		final String name = parameter.getCode();
		String para = getParameterByName(name);
		return Long.parseLong(para == null ? def : para);
	}
	
	/**
	 * 单个String值解析
	 */
	public static String getString(Parameter parameter,String def) {
		assert parameter != null;
		final String name = parameter.getCode();
		String para = getParameterByName(name);
		return para == null ? def : para;
	}
	
	/**
	 * 单个String值解析
	 */
	public static String getString(Parameter parameter) {
		assert parameter != null;
		final String name = parameter.getCode();
		return getParameterByName(name);
	}

	/**
	 * 多个String值解析 ;多个配置可以用",; \t\n"中任意字符分割
	 */
	public static String[] getStringArray(Parameter parameter){
		assert parameter != null;
		final String name = parameter.getCode();
		String para = getParameterByName(name);
		return para == null ? new String[]{} : StringUtils.tokenizeToStringArray(para);
	}

	public static String getParameterByName(String parameterName) {
		assert parameterName != null;
		final String globalName = PARAMETER_SYSTEM_PREFIX + parameterName;
		String result = System.getProperty(globalName);
		if (result != null) {
			return result;
		}
		if (servletContext != null) {
			result = servletContext.getInitParameter(globalName);
			if (result != null) {
				return result;
			}
			// In a ServletContextListener, it's also possible to call servletContext.setAttribute("http.xxx", "true"); for example
			final Object attribute = servletContext.getAttribute(globalName);
			if (attribute instanceof String) {
				return (String) attribute;
			}
		}
		if (filterConfig != null) {
			result = filterConfig.getInitParameter(parameterName);
			if (result != null) {
				return result;
			}
		}
		if (servletConfig != null) {
			result = servletConfig.getInitParameter(parameterName);
			if (result != null) {
				return result;
			}
		}
		return null;
	}
}
