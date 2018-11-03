package org.httpdb.result;

import org.httpdb.exception.HttpdbException;


public class Result {

    // error strings in error results
    private String mainString;
    private String subString;
    private String zoneString;

    // vendor error code
    int errorCode;

    // the exception if this is an error
    private HttpdbException exception;
    
	public String getMainString() {
		return mainString;
	}

	public void setMainString(String mainString) {
		this.mainString = mainString;
	}

	public String getSubString() {
		return subString;
	}

	public void setSubString(String subString) {
		this.subString = subString;
	}

	public String getZoneString() {
		return zoneString;
	}

	public void setZoneString(String zoneString) {
		this.zoneString = zoneString;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public HttpdbException getException() {
		return exception;
	}

	public void setException(HttpdbException exception) {
		this.exception = exception;
	}
    
}
