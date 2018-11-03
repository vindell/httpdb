package org.httpdb.jdbc;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

import org.httpdb.error.ErrorCode;
import org.httpdb.result.ResultMetaData;
import org.httpdb.schema.ColumnSchema;
import org.httpdb.utils.ExceptionUtils;

/**
 * 记录集元数据
 */
public class HttpdbResultSetMetaData implements ResultSetMetaData {
	
	protected ResultMetaData resultMetaData;
	protected int columnCount;
	protected List<ColumnSchema> fields;

	public HttpdbResultSetMetaData(List<ColumnSchema> fields) {
		this.fields = fields;
	}
	
	/**
     * Constructs a new HttpdbResultSetMetaData object from the specified
     * HttpdbResultSet and HsqlProprties objects.
     *
     * @param meta the ResultMetaData object from which to construct a new HttpdbResultSetMetaData object
     * @throws SQLException if a database access error occurs
     */
	public HttpdbResultSetMetaData(ResultMetaData meta, HttpdbConnection conn) throws SQLException {
        init(meta, conn);
    }

    /**
     *  Initializes this HttpdbResultSetMetaData object from the specified
     *  Result and HsqlProperties objects.
     *
     *  @param meta the ResultMetaData object from which to initialize this
     *         HttpdbResultSetMetaData object
     *  @param conn the HttpdbConnection
     *  @throws SQLException if a database access error occurs
     */
    void init(ResultMetaData meta, HttpdbConnection conn) throws SQLException {
        resultMetaData = meta;
        columnCount    = resultMetaData.getColumnCount();
    }
    
   

	public boolean isAutoIncrement(int column) throws SQLException {
		return true;
	}

	public boolean isCaseSensitive(int column) throws SQLException {
		return false;
	}

	public boolean isSearchable(int column) throws SQLException {
		return true;
	}

	public boolean isCurrency(int column) throws SQLException {
		return false;
	}

	public int isNullable(int column) throws SQLException {
		return ResultSetMetaData.columnNullable;
	}

	public boolean isSigned(int column) throws SQLException {
		return true;
	}

	public int getColumnDisplaySize(int column) throws SQLException {
		return 20;
	}

	private Field getField(int column) throws SQLException {
		if (column <= this.fields.size()) {
			return (Field) this.fields.get(column - 1);
		} else {
			throw new SQLException(new IndexOutOfBoundsException("Index: "
					+ column + ", Size: " + this.fields.size()));
		}
	}

	public String getColumnLabel(int column) throws SQLException {
		return this.getField(column).getName();
	}

	public String getColumnName(int column) throws SQLException {
		return this.getField(column).getName();
	}

	public String getSchemaName(int column) throws SQLException {
		return "";
	}

	public int getPrecision(int column) throws SQLException {
		return 0;
	}

	public int getScale(int column) throws SQLException {
		return 0;
	}

	public String getTableName(int column) throws SQLException {
		return "";
	}

	public String getCatalogName(int column) throws SQLException {
		return "";
	}

	public int getColumnType(int column) throws SQLException {
		return this.getField(column).getIntType();
	}

	/**
     * <!-- start generic documentation -->
     * Retrieves the designated column's database-specific type name.
     * <!-- end generic documentation -->
     *
     * @param column the first column is 1, the second is 2, ...
     * @return type name used by the database. If the column type is
     * a user-defined type, then a fully-qualified type name is returned.
     * @exception SQLException if a database access error occurs
     */
	public String getColumnTypeName(int column) throws SQLException {
		checkColumn(column);
        return resultMetaData.columnTypes[--column].getName();
	}

	 /**
     * <!-- start generic documentation -->
     * Indicates whether the designated column is definitely not writable.
     * <!-- end generic documentation -->
     *
     * @param column the first column is 1, the second is 2, ...
     * @return <code>true</code> if so; <code>false</code> otherwise
     * @exception SQLException if a database access error occurs
     */
	public boolean isReadOnly(int column) throws SQLException {
        checkColumn(column);
        return !resultMetaData.columns[--column].isWriteable();
	}

	  /**
     * <!-- start generic documentation -->
     * Indicates whether it is possible for a write on the designated column to succeed.
     * <!-- end generic documentation -->
     *
     * @param column the first column is 1, the second is 2, ...
     * @return <code>true</code> if so; <code>false</code> otherwise
     * @exception SQLException if a database access error occurs
     */
	public boolean isWritable(int column) throws SQLException {
		 checkColumn(column);
	     return resultMetaData.colIndexes != null && resultMetaData.colIndexes[--column] > -1;
	}

	/**
     * <!-- start generic documentation -->
     * Indicates whether a write on the designated column will definitely succeed.
     * <!-- end generic documentation -->
     *
     * @param column the first column is 1, the second is 2, ...
     * @return <code>true</code> if so; <code>false</code> otherwise
     * @exception SQLException if a database access error occurs
     */
	public boolean isDefinitelyWritable(int column) throws SQLException {
		//return false;
		checkColumn(column);
	    return resultMetaData.colIndexes != null  && resultMetaData.colIndexes[--column] > -1;
	}

	//--------------------------JDBC 2.0-----------------------------------

	/**
     * <!-- start generic documentation -->
     * <p>Returns the fully-qualified name of the Java class whose instances
     * are manufactured if the method <code>ResultSet.getObject</code>
     * is called to retrieve a value
     * from the column.  <code>ResultSet.getObject</code> may return a subclass of the
     * class returned by this method.
     * <!-- end generic documentation -->
     *
     * @param column the first column is 1, the second is 2, ...
     * @return the fully-qualified name of the class in the Java programming language that would be used by the method
     * <code>ResultSet.getObject</code> to retrieve the value in the specified
     * column. This is the class name used for custom mapping.
     * @exception SQLException if a database access error occurs
     * @since JDK 1.2 (JDK 1.1.x developers: read the overview for HttpdbResultSet)
     */
	public String getColumnClassName(int column) throws SQLException {
		checkColumn(column);
		return resultMetaData.columnTypes[--column].getTypeClass().getName();
	}

	//----------------------------- JDBC 4.0 -----------------------------------
    // ------------------- java.sql.Wrapper implementation ---------------------

	/**
     * Returns an object that implements the given interface to allow access to
     * non-standard methods, or standard methods not exposed by the proxy.
     *
     * If the receiver implements the interface then the result is the receiver
     * or a proxy for the receiver. If the receiver is a wrapper
     * and the wrapped object implements the interface then the result is the
     * wrapped object or a proxy for the wrapped object. Otherwise return the
     * the result of calling <code>unwrap</code> recursively on the wrapped object
     * or a proxy for that result. If the receiver is not a
     * wrapper and does not implement the interface, then an <code>SQLException</code> is thrown.
     *
     * @param iface A Class defining an interface that the result must implement.
     * @return an object that implements the interface. May be a proxy for the actual implementing object.
     * @throws java.sql.SQLException If no object found that implements the interface
     * @since JDK 1.6, Httpdb 1.0.x
     */
	public <T>T unwrap(Class<T> iface) throws SQLException {
		if (isWrapperFor(iface)) {
            return (T) this;
        }
		throw ExceptionUtils.invalidArgument("iface: " + iface);
	}
	
	/**
     * Returns true if this either implements the interface argument or is directly or indirectly a wrapper
     * for an object that does. Returns false otherwise. If this implements the interface then return true,
     * else if this is a wrapper then return the result of recursively calling <code>isWrapperFor</code> on the wrapped
     * object. If this does not implement the interface and is not a wrapper, return false.
     * This method should be implemented as a low-cost operation compared to <code>unwrap</code> so that
     * callers can use this method to avoid expensive <code>unwrap</code> calls that may fail. If this method
     * returns true then calling <code>unwrap</code> with the same argument should succeed.
     *
     * @param iface a Class defining an interface.
     * @return true if this implements the interface or directly or indirectly wraps an object that does.
     * @throws java.sql.SQLException  if an error occurs while determining whether this is a wrapper
     * for an object with the given interface.
     * @since JDK 1.6, Httpdb 1.0.x
     */
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return (iface != null && iface.isAssignableFrom(this.getClass()));
	}

	public int getColumnCount() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}
	
	 /**
     * Performs an internal check for column index validity. <p>
     *
     * @param column index of column to check
     * @throws SQLException when this object's parent ResultSet has
     *      no such column
     */
    private void checkColumn(int column) throws SQLException {
        if (column < 1 || column > columnCount) {
            throw ExceptionUtils.sqlException(ErrorCode.JDBC_COLUMN_NOT_FOUND, String.valueOf(column));
        }
    }


    /**
     * Returns a string representation of the object. <p>
     *
     * The string consists of the name of the class of which the
     * object is an instance, the at-sign character `<code>@</code>',
     * the unsigned hexadecimal representation of the hash code of the
     * object and a comma-delimited list of this object's indexed attributes,
     * enclosed in square brackets.
     *
     * @return  a string representation of the object.
     */
    public String toString() {

        StringBuffer sb = new StringBuffer();

        sb.append(super.toString());

        if (columnCount == 0) {
            sb.append("[columnCount=0]");

            return sb.toString();
        }
        sb.append('[');

        for (int i = 0; i < columnCount; i++) {
        	HttpdbColumnMetaData meta = getColumnMetaData(i + 1);

            sb.append('\n');
            sb.append("   column_");
            sb.append(i + 1);
            sb.append('=');
            sb.append(meta);

            if (i + 1 < columnCount) {
                sb.append(',');
                sb.append(' ');
            }
        }
        sb.append('\n');
        sb.append(']');

        return sb.toString();
    }

    HttpdbColumnMetaData getColumnMetaData(int i) {

    	HttpdbColumnMetaData meta = new HttpdbColumnMetaData();

        try {
            meta.catalogName          = getCatalogName(i);
            meta.columnClassName      = getColumnClassName(i);
            meta.columnDisplaySize    = getColumnDisplaySize(i);
            meta.columnLabel          = getColumnLabel(i);
            meta.columnName           = getColumnName(i);
            meta.columnType           = getColumnType(i);
            meta.isAutoIncrement      = isAutoIncrement(i);
            meta.isCaseSensitive      = isCaseSensitive(i);
            meta.isCurrency           = isCurrency(i);
            meta.isDefinitelyWritable = isDefinitelyWritable(i);
            meta.isNullable           = isNullable(i);
            meta.isReadOnly           = isReadOnly(i);
            meta.isSearchable         = isSearchable(i);
            meta.isSigned             = isSigned(i);
            meta.isWritable           = isWritable(i);
            meta.precision            = getPrecision(i);
            meta.scale                = getScale(i);
            meta.schemaName           = getSchemaName(i);
            meta.tableName            = getTableName(i);
        } catch (SQLException e) {
        }

        return meta;
    }
}
