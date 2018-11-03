package org.httpdb.exception;

import org.httpdb.error.ErrorCode;
import org.httpdb.result.Result;

@SuppressWarnings("serial")
public class HttpdbException extends RuntimeException {

	public static final HttpdbException[] emptyArray = new HttpdbException[] {};
	public static final HttpdbException noDataCondition = org.httpdb.error.Error.error(ErrorCode.N_02000);

	//
	private String message;
	private String state;
	private int code;
	private int level;
	private int statementGroup;
	private int statementCode;
	public Object info;

	/**
	 * @param message String
	 * @param state XOPEN / SQL code for exception
	 * @param code number code in HSQLDB
	 */
	public HttpdbException(Throwable t, String message, String state, int code) {

		super(t);

		this.message = message;
		this.state = state;
		this.code = code;
	}

	/**
	 * @param r containing the members
	 */
	public HttpdbException(Result r) {
		this.message = r.getMainString();
		this.state = r.getSubString();
		this.code = r.getErrorCode();
	}

	public HttpdbException(Throwable t, String errorState, int errorCode) {

		super(t);

		this.message = t.toString();
		this.state = errorState;
		this.code = errorCode;
	}

	/**
	 * @return message
	 */
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return SQL State
	 */
	public String getSQLState() {
		return state;
	}

	/**
	 * @return vendor specific error code
	 */
	public int getErrorCode() {
		return code;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getLevel() {
		return level;
	}

	public int getStatementCode() {
		return statementCode;
	}

	public void setStatementType(int group, int code) {
		statementGroup = group;
		statementCode = code;
	}

	public static class HttpdbRuntimeMemoryError extends OutOfMemoryError {
		HttpdbRuntimeMemoryError() {
		}
	}

	public int hashCode() {
		return code;
	}

	public boolean equals(Object other) {

		if (other instanceof HttpdbException) {
			HttpdbException o = (HttpdbException) other;
			return code == o.code && equals(state, o.state)
					&& equals(message, o.message);
		}

		return false;
	}

	private static boolean equals(Object a, Object b) {

		if (a == b) {
			return true;
		}

		if (a == null || b == null) {
			return false;
		}

		return a.equals(b);
	}
}