package org.httpdb.jdbc;

import java.io.IOException;
import java.sql.RowId;
import java.sql.SQLException;
import java.util.Arrays;

import org.httpdb.error.ErrorCode;
import org.httpdb.utils.ExceptionUtils;
import org.httpdb.utils.StringUtils;
/**
 * 
 * *******************************************************************
 * @className	： HttpdbRowId
 * @description	： SQL ROWID 值在 Java 编程语言中的表示形式（映射）。
 * <pre>
 * SQL ROWID 是一种内置类型，其值可视为它标识的行在数据库表中的一个地址。该地址是逻辑的还是物理的（在某些方面）取决于它的原始数据源。
 * 接口 ResultSet、CallableStatement 和 PreparedStatement 中的方法（如 getRowId 和 setRowId）允许程序员访问 SQL ROWID 值。
 * RowId 接口提供以 byte 数组或 String 形式表示 ROWID 值的方法。
 * 接口 DatabaseMetaData 中的方法 getRowIdLifetime 可用于确定 RowId 对象在创建它的事务及会话处理期间是否仍然有效，
 * 实际上，也就是用于确定其标识的行未被删除之前，RowId 是否仍然有效。除指定其原始数据源外的有效生存期之外，getRowIdLifetime 还指定其原始数据源内的 ROWID 值的有效生存期。
 * 在这一点上，它不同于大对象，因为原始数据源内大对象的有效生存期没有限制。
 * 如果 JDBC 驱动程序支持该数据类型，则必须完全实现 RowId 接口中的所有方法。
 * </pre>
 * @author 		： <a href="mailto:hnxyhcwdl1003@163.com">wandalong</a>
 * @date		： Dec 17, 2016 11:19:13 PM
 * @version 	V1.0 
 * *******************************************************************
 */
public class HttpdbRowId implements RowId {

	private int hash;

    // ------------------------- Internal Implementation -----------------------
    private final byte[] id;

    /**
     * Constructs a new HttpdbRowId instance wrapping the given octet sequence. <p>
     *
     * This constructor may be used internally to retrieve result set values as
     * RowId objects, yet it also may need to be public to allow access from
     * other packages. As such (in the interest of efficiency) this object
     * maintains a reference to the given octet sequence rather than making a
     * copy; special care should be taken by external clients never to use this
     * constructor with a byte array object that may later be modified
     * externally.
     *
     * @param id the octet sequence representing the Rowid value
     * @throws SQLException if the argument is null
     */
    public HttpdbRowId(final byte[] id) throws SQLException {

        if (id == null) {
            throw ExceptionUtils.nullArgument("id");
        }
        this.id = id;
    }

    /**
     * Constructs a new HttpdbRowId instance whose internal octet sequence is
     * is a copy of the octet sequence of the given RowId object. <p>
     *
     * @param id the octet sequence representing the Rowid value
     * @throws SQLException if the argument is null
     */
    public HttpdbRowId(RowId id) throws SQLException {
        this(id.getBytes());
    }

    /**
     * Constructs a new HttpdbRowId instance whose internal octet sequence is
     * is that represented by the given hexadecimal character sequence. <p>
     * @param hex the hexadecimal character sequence from which to derive
     *        the internal octet sequence
     * @throws java.sql.SQLException if the argument is null or is not a valid
     *         hexadecimal character sequence
     */
    public HttpdbRowId(final String hex) throws SQLException {

        if (hex == null) {
            throw ExceptionUtils.nullArgument("hex");
        }

        try {
            this.id = StringUtils.hexStringToByteArray(hex);
        } catch (IOException e) {
            throw ExceptionUtils.sqlException(ErrorCode.JDBC_INVALID_ARGUMENT, "hex: " + e);

            // .illegalHexadecimalCharacterSequenceArgumentException("hex", e);
        }
    }

    /**
     * Compares this <code>RowId</code> to the specified object. The result is
     * <code>true</code> if and only if the argument is not null and is a RowId
     * object that represents the same ROWID as  this object.
     * <p>
     * It is important
     * to consider both the origin and the valid lifetime of a <code>RowId</code>
     * when comparing it to another <code>RowId</code>. If both are valid, and
     * both are from the same table on the same data source, then if they are equal
     * they identify
     * the same row; if one or more is no longer guaranteed to be valid, or if
     * they originate from different data sources, or different tables on the
     * same data source, they  may be equal but still
     * not identify the same row.
     *
     * @param obj the <code>Object</code> to compare this <code>RowId</code> object against.
     * @return true if the <code>RowId</code>s are equal; false otherwise
     * @since JDK 1.6, Httpdb 1.0
     */
    public boolean equals(Object obj) {
        return (obj instanceof HttpdbRowId) && Arrays.equals(this.id, ((HttpdbRowId) obj).id);
    }

    /**
     * Returns an array of bytes representing the value of the SQL <code>ROWID</code>
     * designated by this <code>java.sql.RowId</code> object.
     *
     * @return an array of bytes, whose length is determined by the driver supplying
     *     the connection, representing the value of the ROWID designated by this
     *     java.sql.RowId object.
     */
    public byte[] getBytes() {
        return id.clone();
    }

    /**
     * Returns a String representing the value of the SQL ROWID designated by this
     * <code>java.sql.RowId</code> object.
     * <p>
     * Like <code>java.sql.Date.toString()</code>
     * returns the contents of its DATE as the <code>String</code> "2004-03-17"
     * rather than as  DATE literal in SQL (which would have been the <code>String</code>
     * DATE "2004-03-17"), toString()
     * returns the contents of its ROWID in a form specific to the driver supplying
     * the connection, and possibly not as a <code>ROWID</code> literal.
     *
     * @return a String whose format is determined by the driver supplying the
     *     connection, representing the value of the <code>ROWID</code> designated
     *     by this <code>java.sql.RowId</code>  object.
     */
    public String toString() {
        return StringUtils.byteArrayToHexString(id);
    }

    /**
     * Returns a hash code value of this <code>RowId</code> object.
     *
     * @return a hash code for the <code>RowId</code>
     */
    public int hashCode() {

        if (hash == 0) {
            hash = Arrays.hashCode(id);
        }

        return hash;
    }

    /**
     * Direct access to id bytes for subclassing.
     *
     * @return direct reference to id bytes.
     */
    Object id() {
        return id;
    }

}
