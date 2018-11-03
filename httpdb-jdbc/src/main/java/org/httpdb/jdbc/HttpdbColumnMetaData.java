package org.httpdb.jdbc;

import java.lang.reflect.Field;

public class HttpdbColumnMetaData {
	
	/** The column's table's catalog name. */
    public String catalogName;

    /**
     * The fully-qualified name of the Java class whose instances are
     * manufactured if the method ResultSet.getObject is called to retrieve
     * a value from the column.
     */
    public String columnClassName;

    /** The column's normal max width in chars. */
    public int columnDisplaySize;

    /** The suggested column title for use in printouts and displays. */
    public String columnLabel;

    /** The column's name. */
    public String columnName;

    /** The column's SQL type. */
    public int columnType;

    /** The column's value's number of decimal digits. */
    public int precision;

    /** The column's value's number of digits to right of the decimal point. */
    public int scale;

    /** The column's table's schema. */
    public String schemaName;

    /** The column's table's name. */
    public String tableName;

    /** Whether the value of the column are automatically numbered. */
    public boolean isAutoIncrement;

    /** Whether the column's value's case matters. */
    public boolean isCaseSensitive;

    /** Whether the values in the column are cash values. */
    public boolean isCurrency;

    /** Whether a write on the column will definitely succeed. */
    public boolean isDefinitelyWritable;

    /** The nullability of values in the column. */
    public int isNullable;

    /** Whether the column's values are definitely not writable. */
    public boolean isReadOnly;

    /** Whether the column's values can be used in a where clause. */
    public boolean isSearchable;

    /** Whether values in the column are signed numbers. */
    public boolean isSigned;

    /** Whether it is possible for a write on the column to succeed. */
    public boolean isWritable;

    /**
     * Retrieves a String representation of this object.
     *
     * @return a String representation of this object
     */
    public String toString() {

        try {
            return toStringImpl();
        } catch (Exception e) {
            return super.toString() + "[" + e + "]";
        }
    }

    /**
     * Provides the implementation of the toString() method.
     *
     * @return a String representation of this object
     */
    private String toStringImpl() throws Exception {

        StringBuffer sb;
        Field[]      fields;
        Field        field;

        sb = new StringBuffer();

        sb.append('[');

        fields = getClass().getFields();

        int len = fields.length;

        for (int i = 0; i < len; i++) {
            field = fields[i];

            sb.append(field.getName());
            sb.append('=');
            sb.append(field.get(this));

            if (i + 1 < len) {
                sb.append(',');
                sb.append(' ');
            }
        }
        sb.append(']');

        return sb.toString();
    }
    
}
