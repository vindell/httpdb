package org.httpdb.error;

import java.lang.reflect.Field;

import org.httpdb.exception.HttpdbException;
import org.httpdb.resources.ResourceBundleHandler;
import org.httpdb.result.Result;
import org.httpdb.utils.StringUtils;

public class Error {
	 //
    public static boolean TRACE          = false;
    public static boolean TRACESYSTEMOUT = false;

    //
    private static final String defaultMessage = "S1000 General error";
    private static final String errPropsName   = "sql-state-messages";
    private static final int bundleHandle = ResourceBundleHandler.getBundleHandle(errPropsName, null);
    private static final String MESSAGE_TAG      = "$$";
    private static final int    SQL_STATE_DIGITS = 5;
    private static final int    SQL_CODE_DIGITS  = 4;
    private static final int    ERROR_CODE_BASE  = 11;

    public static RuntimeException runtimeError(int code, String add) {

        HttpdbException e = error(code, add);

        return new RuntimeException(e.getMessage());
    }

    public static HttpdbException error(int code, String add) {
        return error((Throwable) null, code, add);
    }

    public static HttpdbException error(Throwable t, int code, String add) {

        String s = getMessage(code);

        if (add != null) {
            s += ": " + add;
        }

        return new HttpdbException(t, s.substring(SQL_STATE_DIGITS + 1),
                                 s.substring(0, SQL_STATE_DIGITS), -code);
    }

    public static HttpdbException parseError(int code, String add,
                                           int lineNumber) {

        String s = getMessage(code);

        if (add != null) {
            s = s + ": " + add;
        }

        if (lineNumber > 1) {
            add = getMessage(ErrorCode.M_parse_line);
            s   = s + " :" + add + String.valueOf(lineNumber);
        }

        return new HttpdbException(null, s.substring(SQL_STATE_DIGITS + 1),
                                 s.substring(0, SQL_STATE_DIGITS), -code);
    }

    public static HttpdbException error(int code) {
        return error(null, code, 0, null);
    }

    public static HttpdbException error(int code, Throwable t) {

        String message = getMessage(code, 0, null);

        return new HttpdbException(t, message.substring(0, SQL_STATE_DIGITS),
                                 -code);
    }

    /**
     * Compose error message by inserting the strings in the add parameters
     * in placeholders within the error message. The message string contains
     * $$ markers for each context variable. Context variables are supplied in
     * the add parameters.
     *
     * @param code      main error code
     * @param subCode   sub error code (if 0 => no subMessage!)
     * @param   add     optional parameters
     *
     * @return an <code>HttpdbException</code>
     */
    public static HttpdbException error(Throwable t, int code, int subCode,
                                      final Object[] add) {

        String message = getMessage(code, subCode, add);
        int    sqlCode = subCode < ERROR_CODE_BASE ? code
                                                   : subCode;

        return new HttpdbException(t, message.substring(SQL_STATE_DIGITS + 1),
                                 message.substring(0, SQL_STATE_DIGITS),
                                 -sqlCode);
    }

    public static HttpdbException parseError(int code, int subCode,
                                           int lineNumber,
                                           final Object[] add) {

        String message = getMessage(code, subCode, add);

        if (lineNumber > 1) {
            String sub = getMessage(ErrorCode.M_parse_line);

            message = message + " :" + sub + String.valueOf(lineNumber);
        }

        int sqlCode = subCode < ERROR_CODE_BASE ? code
                                                : subCode;

        return new HttpdbException(null,
                                 message.substring(SQL_STATE_DIGITS + 1),
                                 message.substring(0, SQL_STATE_DIGITS),
                                 -sqlCode);
    }

    public static HttpdbException error(int code, int code2) {
        return error(code, getMessage(code2));
    }

    /**
     * For SIGNAL and RESIGNAL
     * @see HttpdbException#HttpdbException(Throwable,String, String, int)
     * @return an <code>HttpdbException</code>
     */
    public static HttpdbException error(String message, String sqlState) {

        int code = getCode(sqlState);

        if (code < 1000) {
            code = ErrorCode.X_45000;
        }

        if (message == null) {
            message = getMessage(code);
        }

        return new HttpdbException(null, message, sqlState, code);
    }

    /**
     * Compose error message by inserting the strings in the add variables
     * in placeholders within the error message. The message string contains
     * $$ markers for each context variable. Context variables are supplied in
     * the add parameter. (by Loic Lefevre)
     *
     * @param message  message string
     * @param add      optional parameters
     *
     * @return an <code>HttpdbException</code>
     */
    private static String insertStrings(String message, Object[] add) {

        StringBuffer sb        = new StringBuffer(message.length() + 32);
        int          lastIndex = 0;
        int          escIndex  = message.length();

        // removed test: i < add.length
        // because if mainErrorMessage is equal to "blabla $$"
        // then the statement escIndex = mainErrorMessage.length();
        // is never reached!  ???
        for (int i = 0; i < add.length; i++) {
            escIndex = message.indexOf(MESSAGE_TAG, lastIndex);

            if (escIndex == -1) {
                break;
            }

            sb.append(message.substring(lastIndex, escIndex));
            sb.append(add[i] == null ? "null exception message"
                                     : add[i].toString());

            lastIndex = escIndex + MESSAGE_TAG.length();
        }

        escIndex = message.length();

        sb.append(message.substring(lastIndex, escIndex));

        return sb.toString();
    }

    /**
     * Returns the error message given the error code.<br/>
     * This method is be used when throwing exception other
     * than <code>HttpdbException</code>.
     *
     * @param errorCode    the error code associated to the error message
     * @return  the error message associated with the error code
     */
    public static String getMessage(final int errorCode) {
        return getMessage(errorCode, 0, null);
    }

    /**
     * Returns the error SQL STATE sting given the error code.<br/>
     * This method is be used when throwing exception based on other exceptions.
     *
     * @param errorCode    the error code associated to the error message
     * @return  the error message associated with the error code
     */
    public static String getStateString(final int errorCode) {
        return getMessage(errorCode, 0, null).substring(0, SQL_STATE_DIGITS);
    }

    /**
     * Returns the error message given the error code.<br/> This method is used
     * when throwing exception other than <code>HttpdbException</code>.
     *
     * @param code the code for the error message
     * @param subCode the code for the addon message
     * @param add value(s) to use to replace the placeholer(s)
     * @return the error message associated with the error code
     */
    public static String getMessage(final int code, int subCode,
                                    final Object[] add) {

        String message = getResourceString(code);

        if (subCode != 0) {
            message += getResourceString(subCode);
        }

        if (add != null) {
            message = insertStrings(message, add);
        }

        return message;
    }

    private static String getResourceString(int code) {

        String key = StringUtils.toZeroPaddedString(code, SQL_CODE_DIGITS, SQL_CODE_DIGITS);
        String string = ResourceBundleHandler.getString(bundleHandle, key);

        if (string == null) {
            string = defaultMessage;
        }

        return string;
    }

    public static HttpdbException error(final Result result) {
        return new HttpdbException(result);
    }

    /**
     * Used to print messages to System.out
     *
     *
     * @param message message to print
     */
    public static void printSystemOut(String message) {

        if (TRACESYSTEMOUT) {
            System.out.println(message);
        }
    }

    public static int getCode(String sqlState) {

        try {
            Field[] fields = ErrorCode.class.getDeclaredFields();

            for (int i = 0; i < fields.length; i++) {
                String name = fields[i].getName();

                if (name.length() == 7 && name.endsWith(sqlState)) {
                    return fields[i].getInt(ErrorCode.class);
                }
            }
        } catch (IllegalAccessException e) {}

        return -1;
    }
}
