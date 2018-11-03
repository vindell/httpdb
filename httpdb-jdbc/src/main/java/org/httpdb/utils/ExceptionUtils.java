package org.httpdb.utils;

import java.sql.SQLDataException;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLInvalidAuthorizationSpecException;
import java.sql.SQLNonTransientConnectionException;
import java.sql.SQLSyntaxErrorException;
import java.sql.SQLTransactionRollbackException;
import java.sql.SQLTransientConnectionException;
import java.sql.SQLWarning;

import org.httpdb.error.ErrorCode;
import org.httpdb.exception.HttpdbException;
import org.httpdb.result.Result;


public final class ExceptionUtils {

	static void throwError(HttpdbException e) throws SQLException {

		// #ifdef JAVA6
		throw sqlException(e.getMessage(), e.getSQLState(), e.getErrorCode(), e);

		// #else
		/*
		 * SQLException se = new SQLException(e.getMessage(), e.getSQLState(),
		 * e.getErrorCode());
		 * 
		 * se.initCause(e);
		 * 
		 * throw se;
		 */

		// #endif JAVA6
	}

	static void throwError(Result r) throws SQLException {

		// #ifdef JAVA6
		throw sqlException(r.getMainString(), r.getSubString(), r.getErrorCode(), r.getException());

		// #else
		/*
		 * SQLException se = new SQLException(r.getMainString(),
		 * r.getSubString(), r.getErrorCode());
		 * 
		 * if (r.getException() != null) { se.initCause(r.getException()); }
		 * 
		 * throw se;
		 */

		// #endif JAVA6
	}

	public static SQLException sqlException(HttpdbException e) {

		// #ifdef JAVA6
		return sqlException(e.getMessage(), e.getSQLState(), e.getErrorCode(),
				e);

		// #else
		/*
		 * SQLException se = new SQLException(e.getMessage(), e.getSQLState(),
		 * e.getErrorCode());
		 * 
		 * se.initCause(e);
		 * 
		 * return se;
		 */

		// #endif JAVA6
	}

	public static SQLException sqlException(HttpdbException e, Throwable cause) {

		// #ifdef JAVA6
		return sqlException(e.getMessage(), e.getSQLState(), e.getErrorCode(), cause);

		// #else
		/*
		 * SQLException se = new SQLException(e.getMessage(), e.getSQLState(),
		 * e.getErrorCode());
		 * 
		 * if (cause != null) { se.initCause(cause); }
		 * 
		 * return se;
		 */

		// #endif JAVA6
	}

	public static SQLException sqlException(int id) {
		return sqlException(org.httpdb.error.Error.error(id));
	}

	public static SQLException sqlExceptionSQL(int id) {
		return sqlException(org.httpdb.error.Error.error(id));
	}

	public static SQLException sqlException(int id, String message) {
		return sqlException(org.httpdb.error.Error.error(id, message));
	}

	public static SQLException sqlException(int id, String message,
			Throwable cause) {
		return sqlException(org.httpdb.error.Error.error(id, message), cause);
	}

	public static SQLException sqlException(int id, int add) {
		return sqlException(org.httpdb.error.Error.error(id, add));
	}

	static SQLException sqlException(int id, int subId, Object[] add) {
		return sqlException(org.httpdb.error.Error.error(null, id, subId, add));
	}

	static SQLException notSupported() {

		// #ifdef JAVA6
		HttpdbException e = org.httpdb.error.Error.error(ErrorCode.X_0A000);

		return new SQLFeatureNotSupportedException(e.getMessage(),
				e.getSQLState(), -ErrorCode.X_0A000);

		// #else
		/*
		 * return sqlException(Error.error(ErrorCode.X_0A000));
		 */

		// #endif JAVA6
	}

	static SQLException notUpdatableColumn() {
		return sqlException(ErrorCode.X_0U000);
	}

	public static SQLException nullArgument() {
		return sqlException(ErrorCode.JDBC_INVALID_ARGUMENT);
	}

	public static SQLException nullArgument(String name) {
		return sqlException(ErrorCode.JDBC_INVALID_ARGUMENT, name + ": null");
	}

	public static SQLException invalidArgument() {
		return sqlException(ErrorCode.JDBC_INVALID_ARGUMENT);
	}

	public static SQLException invalidArgument(String name) {
		return sqlException(ErrorCode.JDBC_INVALID_ARGUMENT, name);
	}

	public static SQLException outOfRangeArgument() {
		return sqlException(ErrorCode.JDBC_INVALID_ARGUMENT);
	}

	public static SQLException outOfRangeArgument(String name) {
		return sqlException(ErrorCode.JDBC_INVALID_ARGUMENT, name);
	}

	public static SQLException connectionClosedException() {
		return sqlException(ErrorCode.X_08003);
	}

	public static SQLWarning sqlWarning(Result r) {
		return new SQLWarning(r.getMainString(), r.getSubString(), r.getErrorCode());
	}

	public static SQLException sqlException(Throwable t) {

		// #ifdef JAVA6
		return new SQLNonTransientConnectionException(t);

		// #else
		/*
		 * return new SQLException(t.getMessage(),
		 * Error.getStateString(ErrorCode.GENERAL_ERROR),
		 * ErrorCode.GENERAL_ERROR);
		 */

		// #endif JAVA6
	}

	public static SQLException sqlException(Result r) {

		// #ifdef JAVA6
		return sqlException(r.getMainString(), r.getSubString(),
				r.getErrorCode(), r.getException());

		// #else
		/*
		 * SQLException se = new SQLException(r.getMainString(),
		 * r.getSubString(), r.getErrorCode());
		 * 
		 * if (r.getException() != null) { se.initCause(r.getException()); }
		 * 
		 * return se;
		 */

		// #endif JAVA6
	}

	// TODO: Needs review.
	//
	// Connection exception subclass may be an insufficient discriminator
	// regarding the choice of throwing transient or non-transient
	// connection exception.
	//
	// SQL 2003 Table 32 SQLSTATE class and subclass values
	//
	// connection exception 08 (no subclass) 000
	//
	// SQL-client unable to establish 001
	// SQL-connection
	//
	// connection name in use 002
	//
	// connection does not exist 003
	//
	// SQL-server rejected establishment 004
	// of SQL-connection
	//
	// connection failure 006
	//
	// transaction resolution unknown 007
	// org.hsqldb.Trace - sql-error-messages
	//
	// 080=08000 socket creation error - better 08001 ?
	// 085=08000 Unexpected exception when setting up TLS
	//
	// 001=08001 The database is already in use by another process - better
	// 08002 ?
	//
	// 002=08003 Connection is closed
	// 003=08003 Connection is broken
	// 004=08003 The database is shutdown
	// 094=08003 Database does not exists - better 08001 ?
	//
	// #ifdef JAVA6
	public static SQLException sqlException(String msg, String sqlstate, int code, Throwable cause) {

		if (sqlstate.startsWith("08")) {
			if (!sqlstate.endsWith("3")) {

				// then, e.g. - the database may spuriously cease to be "in use"
				// upon retry
				// - the network configuration, server availability
				// may change spuriously
				// - keystore location/content may change spuriously
				return new SQLTransientConnectionException(msg, sqlstate, code,
						cause);
			} else {

				// the database is (permanently) shut down or the connection is
				// (permanently) closed or broken
				return new SQLNonTransientConnectionException(msg, sqlstate,
						code, cause);
			}
		} else if (sqlstate.startsWith("22")) {
			return new SQLDataException(msg, sqlstate, code, cause);
		} else if (sqlstate.startsWith("23")) {
			return new SQLIntegrityConstraintViolationException(msg, sqlstate,
					code, cause);
		} else if (sqlstate.startsWith("28")) {
			return new SQLInvalidAuthorizationSpecException(msg, sqlstate,
					code, cause);
		} else if (sqlstate.startsWith("42") || sqlstate.startsWith("37")
				|| sqlstate.startsWith("2A")) {

			// TODO:
			//
			// First, the overview section of java.sql.SQLSyntaxErrorException
			//
			// "...thrown when the SQLState class value is '<i>42</i>'"
			//
			// appears to be inaccurate or not in sync with the
			// SQL 2003 standard, 02 Foundation, Table 32, which states:
			//
			// Condition Class SubClass
			// syntax error or access rule violation - 42 (no subclass) 000
			//
			// SQL 2003 describes an Access Rule Violation as referring to
			// the case where, in the course of preparing or executing
			// an SQL statement, an Access Rule section pertaining
			// to one of the elements of the statement is violated.
			//
			// Further, section 13.4 Calls to an <externally-invoked-procedure>
			// lists:
			//
			// SYNTAX_ERROR_OR_ACCESS_RULE_VIOLATION_NO_SUBCLASS:
			// constant SQLSTATE_TYPE :="42000";
			// SYNTAX_ERROR_OR_ACCESS_RULE_VIOLATION_IN_DIRECT_STATEMENT_NO_SUBCLASS:
			// constant SQLSTATE_TYPE :="2A000";
			// SYNTAX_ERROR_OR_ACCESS_RULE_VIOLATION_IN_DYNAMIC_STATEMENT_NO_SUBCLASS:
			// constant SQLSTATE_TYPE :="37000";
			//
			// Strangely, SQLSTATEs "37000" and 2A000" are not mentioned
			// anywhere else in any of the SQL 2003 parts and are
			// conspicuously missing from 02 - Foundation, Table 32.
			//
			// -----------------------------------
			// /
			// Our only Access Violation SQLSTATE so far is:
			//
			// Error.NOT_AUTHORIZED 255=42000 User not authorized for action
			// '$$'
			//
			// Our syntax exceptions are apparently all sqlstate "37000"
			//
			// Clearly, we should differentiate between DIRECT and DYNAMIC
			// SQL forms. And clearly, our current "37000" is "wrong" in
			// that we do not actually support dynamic SQL syntax, but
			// rather implement similar behaviour only through JDBC
			// Prepared and Callable statements.
			return new SQLSyntaxErrorException(msg, sqlstate, code, cause);
		} else if (sqlstate.startsWith("40")) {

			// TODO: our 40xxx exceptions are not currently used (correctly)
			// for transaction rollback exceptions:
			//
			// 018=40001 Serialization failure
			//
			// - currently used to indicate Java object serialization
			// failures, which is just plain wrong.
			//
			// 019=40001 Transfer corrupted
			//
			// - currently used to indicate IOExceptions related to
			// PreparedStatement XXXStreamYYY operations and Result
			// construction using RowInputBinary (e.g. when reading
			// a result transmitted over the network), which is
			// probably also just plain wrong.
			//
			// SQL 2003 02 - Foundation, Table 32 states:
			//
			// 40000 transaction rollback - no subclass
			// 40001 transaction rollback - (transaction) serialization failure
			// 40002 transaction rollback - integrity constraint violation
			// 40003 transaction rollback - statement completion unknown
			// 40004 transaction rollback - triggered action exception
			//
			return new SQLTransactionRollbackException(msg, sqlstate, code, cause);
		} else if (sqlstate.startsWith("0A")) { // JSR 221 2005-12-14 prd
			return new SQLFeatureNotSupportedException(msg, sqlstate, code,
					cause);
		} else {

			// TODO resolved:
			//
			// JSR 221 2005-12-14 prd
			//
			// "Any SQLState class values which are currently not mapped to
			// either a SQLNonTransientException or a SQLTransientException
			// will result in a java.sql.SQLException being thrown."
			//
			return new SQLException(msg, sqlstate, code, cause);
		}
	}
}
