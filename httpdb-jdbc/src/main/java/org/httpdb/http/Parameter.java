package org.httpdb.http;

import java.util.Locale;

public enum Parameter {
	
	/**
	 */
	HTTPDB_HTTP_ASYNC_POOL_DISABLED("http-async-pool-disable"),
	/**
	 */
	HTTPDB_HTTP_ASYNC_POOL_MIN_SIZE("http-async-pool-minSize"),
	/**
	 */
	HTTPDB_HTTP_ASYNC_POOL_MAX_SIZE("http-async-pool-maxSize"),
	/**
	 */
	HTTPDB_HTTP_ASYNC_POOL_ALIVE_TIME("http-async-pool-aliveTime"),
	/**
	 */
	HTTPDB_HTTP_ASYNC_POOL_QUEUE_SIZE("http-async-pool-queueSize"),
	/**
	 */
	HTTPDB_REQUEST_ENCODING("request-encoding"),
	/**
	 */
	HTTPDB_REQUEST_HANDLERS("request-handlers"),
	/**
	 */
	HTTPDB_COMMAND_HANDLERS("command-handlers"),
	/**
	 */
	HTTPDB_RESPONSE_HANDLERS("response-handlers"),
	
	/**
	 */
	HTTPDB_CONFIGLOCATION("configLocation"),
	
	/**
	 */
	HTTPDB_CMD_RECEIVERS("cmd-receivers");
	
	
	private final String code;

	private Parameter(String code) {
		this.code = code;
	}

	/**
	 * @return code de l'enum tel qu'il doit être paramétré
	 */
	public String getCode() {
		return code;
	}

	static Parameter valueOfIgnoreCase(String parameter) {
		return valueOf(parameter.toUpperCase(Locale.ENGLISH).trim());
	}
}
