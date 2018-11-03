package com.hg.httpdb;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

/**
 * 数据集
 * @author wanghg
 */
public class HttpResultSet implements ResultSet {
    protected RowSet rowSet;
    protected boolean closed = false;

    public HttpResultSet() {
    	this(new RowSet());
    }
    public HttpResultSet(RowSet rowSet) {
        this.rowSet = rowSet;
	}
    public void close() throws SQLException {
    }
    public boolean wasNull() throws SQLException {
        return false;
    }
    public String getString(int columnIndex) throws SQLException {
    	return To.toString(this.getObject(columnIndex));
    }
    public boolean getBoolean(int columnIndex) throws SQLException {
    	return To.toBool(this.getObject(columnIndex));
    }
    public byte getByte(int columnIndex) throws SQLException {
        return 0;
    }
    public short getShort(int columnIndex) throws SQLException {
        return (short) this.getInt(columnIndex);
    }
    public int getInt(int columnIndex) throws SQLException {
        return To.toInt(this.getObject(columnIndex));
    }
    public long getLong(int columnIndex) throws SQLException {
        return To.toLong(this.getObject(columnIndex));
    }
    public float getFloat(int columnIndex) throws SQLException {
        return (float) To.toDouble(this.getObject(columnIndex));
    }
    public double getDouble(int columnIndex) throws SQLException {
        return To.toDouble(this.getObject(columnIndex));
    }
    public BigDecimal getBigDecimal(int columnIndex, int scale)
            throws SQLException {
        return new BigDecimal(this.getDouble(columnIndex));
    }
    public byte[] getBytes(int columnIndex) throws SQLException {
        return To.toString(this.getObject(columnIndex)).getBytes();
    }
    public Date getDate(int columnIndex) throws SQLException {
        return new Date(To.toDate(this.getObject(columnIndex)).getTime());
    }
    public Time getTime(int columnIndex) throws SQLException {
        return new Time(To.toDate(this.getObject(columnIndex)).getTime());
    }
    public Timestamp getTimestamp(int columnIndex) throws SQLException {
        return new Timestamp(To.toDate(this.getObject(columnIndex)).getTime());
    }
    public InputStream getAsciiStream(int columnIndex) throws SQLException {
        return null;
    }
    public InputStream getUnicodeStream(int columnIndex) throws SQLException {
        return null;
    }
    public InputStream getBinaryStream(int columnIndex) throws SQLException {
        return null;
    }
    public String getString(String columnName) throws SQLException {
        return To.toString(this.getObject(columnName));
    }
    public boolean getBoolean(String columnName) throws SQLException {
        return To.toBool(this.getObject(columnName));
    }
    public byte getByte(String columnName) throws SQLException {
        return 0;
    }
    public short getShort(String columnName) throws SQLException {
        return 0;
    }
    public int getInt(String columnName) throws SQLException {
        return To.toInt(this.getObject(columnName));
    }
    public long getLong(String columnName) throws SQLException {
        return To.toLong(this.getObject(columnName));
    }
    public float getFloat(String columnName) throws SQLException {
        return (float) To.toDouble(this.getObject(columnName));
    }
    public double getDouble(String columnName) throws SQLException {
        return To.toDouble(this.getObject(columnName));
    }
    public BigDecimal getBigDecimal(String columnName, int scale)
            throws SQLException {
        return null;
    }
    public byte[] getBytes(String columnName) throws SQLException {
        return null;
    }
    public Date getDate(String columnName) throws SQLException {
        return new Date(To.toDate(this.getObject(columnName)).getTime());
    }
    public Time getTime(String columnName) throws SQLException {
        return new Time(To.toDate(this.getObject(columnName)).getTime());
    }
    public Timestamp getTimestamp(String columnName) throws SQLException {
        return new Timestamp(To.toDate(this.getObject(columnName)).getTime());
    }
    public InputStream getAsciiStream(String columnName) throws SQLException {
        return null;
    }
    public InputStream getUnicodeStream(String columnName) throws SQLException {
        return null;
    }
    public InputStream getBinaryStream(String columnName) throws SQLException {
        return null;
    }
    public SQLWarning getWarnings() throws SQLException {
        return null;
    }
    public void clearWarnings() throws SQLException {
    }
    public String getCursorName() throws SQLException {
        return "";
    }
    public ResultSetMetaData getMetaData() throws SQLException {
        return new HttpRsMetaData(rowSet.getFields());
    }
    protected int curRowno = -1;
    public Object getObject(int columnIndex) throws SQLException {
    	if (columnIndex - 1 < rowSet.getFields().size()) {
    		return rowSet.get(this.curRowno).get(rowSet.fieldAt(columnIndex - 1).getName());
    	} else {
    		return "";
    	}
    }
    public Object getObject(String columnName) throws SQLException {
    	return rowSet.get(this.curRowno).get(columnName);
    }
    public int findColumn(String columnName) throws SQLException {
        return this.rowSet.fieldIndex(columnName) + 1;
    }
    public Reader getCharacterStream(int columnIndex) throws SQLException {
        return null;
    }
    public Reader getCharacterStream(String columnName) throws SQLException {
        return null;
    }
    public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
        return null;
    }
    public BigDecimal getBigDecimal(String columnName) throws SQLException {
        return null;
    }
    public int getRowCount() {
       return this.rowSet.size(); 
    }
    public boolean isBeforeFirst() throws SQLException {
        return this.getRow() == 0; 
    }
    public boolean isAfterLast() throws SQLException {
        return this.getRow() == this.getRowCount() + 1;
    }
    public boolean isFirst() throws SQLException {
        return this.getRow() == 1;
    }
    public boolean isLast() throws SQLException {
        return this.getRow() == this.getRowCount();
    }
    public void beforeFirst() throws SQLException {
        this.absolute(0);
    }
    public void afterLast() throws SQLException {
        this.absolute(this.getRowCount() + 1);
    }
    public boolean first() throws SQLException {
        return this.absolute(1);
    }
    public boolean last() throws SQLException {
        return this.absolute(this.getRowCount());
    }
    public boolean relative(int rows) throws SQLException {
        return this.absolute(this.getRow() + rows);
    }
    public boolean next() throws SQLException {
        return this.absolute(this.getRow() + 1);
    }
    public boolean previous() throws SQLException {
        return this.absolute(this.getRow() - 1);
    }
    public int getRow() throws SQLException {
        return this.curRowno + 1;
    }
    public boolean absolute(int row) throws SQLException {
        if (row >= 0 && row <= this.rowSet.size() + 1) {
            this.curRowno = row - 1;
        }
        return row > 0 && row < this.rowSet.size() + 1;
    }
    protected int perFetch = 100;
    public void setFetchSize(int rows) throws SQLException {
        if (rows > 0) {
            this.perFetch = rows;
        }
    }
    public int getFetchSize() throws SQLException {
        return this.perFetch;
    }
    private int type = ResultSet.TYPE_SCROLL_INSENSITIVE;
    public int getType() throws SQLException {
        return this.type;
    }
    public int getConcurrency() throws SQLException {
        return ResultSet.CONCUR_READ_ONLY;
    }
    private int fetchDirection = ResultSet.FETCH_UNKNOWN;
    public void setFetchDirection(int direction) throws SQLException {
        this.fetchDirection = direction;
    }
    public int getFetchDirection() throws SQLException {
        return this.fetchDirection;
    }
    public boolean rowUpdated() throws SQLException {
        return false;
    }
    public boolean rowInserted() throws SQLException {
        return false;
    }
    public boolean rowDeleted() throws SQLException {
        return false;
    }
    public void updateNull(int columnIndex) throws SQLException {
    }
    public void updateBoolean(int columnIndex, boolean x) throws SQLException {
    }
    public void updateByte(int columnIndex, byte x) throws SQLException {
    }
    public void updateShort(int columnIndex, short x) throws SQLException {
    }
    public void updateInt(int columnIndex, int x) throws SQLException {
    }
    public void updateLong(int columnIndex, long x) throws SQLException {
    }
    public void updateFloat(int columnIndex, float x) throws SQLException {
    }
    public void updateDouble(int columnIndex, double x) throws SQLException {
    }
    public void updateBigDecimal(int columnIndex, BigDecimal x)
            throws SQLException {
    }
    public void updateString(int columnIndex, String x) throws SQLException {
    }
    public void updateBytes(int columnIndex, byte[] x) throws SQLException {
    }
    public void updateDate(int columnIndex, Date x) throws SQLException {
    }
    public void updateTime(int columnIndex, Time x) throws SQLException {
    }
    public void updateTimestamp(int columnIndex, Timestamp x)
            throws SQLException {
    }
    public void updateAsciiStream(int columnIndex, InputStream x, int length)
            throws SQLException {
    }
    public void updateBinaryStream(int columnIndex, InputStream x, int length)
            throws SQLException {
    }
    public void updateCharacterStream(int columnIndex, Reader x, int length)
            throws SQLException {
    }
    public void updateObject(int columnIndex, Object x, int scale)
            throws SQLException {
    }
    public void updateObject(int columnIndex, Object x) throws SQLException {
    }
    public void updateNull(String columnName) throws SQLException {
    }
    public void updateBoolean(String columnName, boolean x) throws SQLException {
    }
    public void updateByte(String columnName, byte x) throws SQLException {
    }
    public void updateShort(String columnName, short x) throws SQLException {
    }
    public void updateInt(String columnName, int x) throws SQLException {
    }
    public void updateLong(String columnName, long x) throws SQLException {
    }
    public void updateFloat(String columnName, float x) throws SQLException {
    }
    public void updateDouble(String columnName, double x) throws SQLException {
    }
    public void updateBigDecimal(String columnName, BigDecimal x)
            throws SQLException {
    }
    public void updateString(String columnName, String x) throws SQLException {
    }
    public void updateBytes(String columnName, byte[] x) throws SQLException {
    }
    public void updateDate(String columnName, Date x) throws SQLException {
    }
    public void updateTime(String columnName, Time x) throws SQLException {
    }
    public void updateTimestamp(String columnName, Timestamp x)
            throws SQLException {
    }
    public void updateAsciiStream(String columnName, InputStream x, int length)
            throws SQLException {
    }
    public void updateBinaryStream(String columnName, InputStream x, int length)
            throws SQLException {
    }
    public void updateCharacterStream(String columnName, Reader reader,
            int length) throws SQLException {
    }
    public void updateObject(String columnName, Object x, int scale)
            throws SQLException {
    }
    public void updateObject(String columnName, Object x) throws SQLException {
    }
    public void insertRow() throws SQLException {
    }
    public void updateRow() throws SQLException {
    }
    public void deleteRow() throws SQLException {
    }
    public void refreshRow() throws SQLException {
    }
    public void cancelRowUpdates() throws SQLException {
    }
    public void moveToInsertRow() throws SQLException {
    }
    public void moveToCurrentRow() throws SQLException {
    }
    protected Statement statement;
    public Statement getStatement() throws SQLException {
        return this.statement;
    }
    public Object getObject(int i, Map map) throws SQLException {
        return this.getObject(i);
    }
    public Ref getRef(int i) throws SQLException {
        return null;
    }
    public Blob getBlob(int i) throws SQLException {
        return null;
    }
    public Clob getClob(int i) throws SQLException {
        return null;
    }
    public Array getArray(int i) throws SQLException {
        return null;
    }
    public Object getObject(String colName, Map map) throws SQLException {
        return this.getObject(colName);
    }
    public Ref getRef(String colName) throws SQLException {
        return null;
    }
    public Blob getBlob(String colName) throws SQLException {
        return null;
    }
    public Clob getClob(String colName) throws SQLException {
        return null;
    }
    public Array getArray(String colName) throws SQLException {
        return null;
    }
    public Date getDate(int columnIndex, Calendar cal) throws SQLException {
        return new Date(To.toDate(this.getObject(columnIndex), cal.getTime()).getTime());
    }
    public Date getDate(String columnName, Calendar cal) throws SQLException {
        return new Date(To.toDate(this.getObject(columnName), cal.getTime()).getTime());
    }
    public Time getTime(int columnIndex, Calendar cal) throws SQLException {
        return new Time(To.toDate(this.getObject(columnIndex), cal.getTime()).getTime());
    }
    public Time getTime(String columnName, Calendar cal) throws SQLException {
        return new Time(To.toDate(this.getObject(columnName), cal.getTime()).getTime());
    }
    public Timestamp getTimestamp(int columnIndex, Calendar cal)
            throws SQLException {
        return new Timestamp(To.toDate(this.getObject(columnIndex), cal.getTime()).getTime());
    }
    public Timestamp getTimestamp(String columnName, Calendar cal)
            throws SQLException {
        return new Timestamp(To.toDate(this.getObject(columnName), cal.getTime()).getTime());
    }
    public URL getURL(int columnIndex) throws SQLException {
        return null;
    }
    public URL getURL(String columnName) throws SQLException {
        return null;
    }
    public void updateRef(int columnIndex, Ref x) throws SQLException {
    }
    public void updateRef(String columnName, Ref x) throws SQLException {
    }
    public void updateBlob(int columnIndex, Blob x) throws SQLException {
    }
    public void updateBlob(String columnName, Blob x) throws SQLException {
    }
    public void updateClob(int columnIndex, Clob x) throws SQLException {
    }
    public void updateClob(String columnName, Clob x) throws SQLException {
    }
    public void updateArray(int columnIndex, Array x) throws SQLException {
    }
    public void updateArray(String columnName, Array x) throws SQLException {
    }
    public RowId getRowId(int columnIndex) throws SQLException {

        return null;
    }
    public RowId getRowId(String columnLabel) throws SQLException {

        return null;
    }
    public void updateRowId(int columnIndex, RowId x) throws SQLException {

        
    }
    public void updateRowId(String columnLabel, RowId x) throws SQLException {

        
    }
    public int getHoldability() throws SQLException {

        return 0;
    }
    public boolean isClosed() throws SQLException {

        return false;
    }
    public void updateNString(int columnIndex, String nString) throws SQLException {

        
    }
    public void updateNString(String columnLabel, String nString) throws SQLException {

        
    }
    public void updateNClob(int columnIndex, NClob nClob) throws SQLException {

        
    }
    public void updateNClob(String columnLabel, NClob nClob) throws SQLException {

        
    }
    public NClob getNClob(int columnIndex) throws SQLException {

        return null;
    }
    public NClob getNClob(String columnLabel) throws SQLException {

        return null;
    }
    public SQLXML getSQLXML(int columnIndex) throws SQLException {

        return null;
    }
    public SQLXML getSQLXML(String columnLabel) throws SQLException {

        return null;
    }
    public void updateSQLXML(int columnIndex, SQLXML xmlObject) throws SQLException {

        
    }
    public void updateSQLXML(String columnLabel, SQLXML xmlObject) throws SQLException {

        
    }
    public String getNString(int columnIndex) throws SQLException {

        return null;
    }
    public String getNString(String columnLabel) throws SQLException {

        return null;
    }
    public Reader getNCharacterStream(int columnIndex) throws SQLException {

        return null;
    }
    public Reader getNCharacterStream(String columnLabel) throws SQLException {

        return null;
    }
    public void updateNCharacterStream(int columnIndex, Reader x, long length) throws SQLException {

        
    }
    public void updateNCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {

        
    }
    public void updateAsciiStream(int columnIndex, InputStream x, long length) throws SQLException {

        
    }
    public void updateBinaryStream(int columnIndex, InputStream x, long length) throws SQLException {

        
    }
    public void updateCharacterStream(int columnIndex, Reader x, long length) throws SQLException {

        
    }
    public void updateAsciiStream(String columnLabel, InputStream x, long length) throws SQLException {

        
    }
    public void updateBinaryStream(String columnLabel, InputStream x, long length) throws SQLException {

        
    }
    public void updateCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {

        
    }
    public void updateBlob(int columnIndex, InputStream inputStream, long length) throws SQLException {

        
    }
    public void updateBlob(String columnLabel, InputStream inputStream, long length) throws SQLException {

        
    }
    public void updateClob(int columnIndex, Reader reader, long length) throws SQLException {

        
    }
    public void updateClob(String columnLabel, Reader reader, long length) throws SQLException {

        
    }
    public void updateNClob(int columnIndex, Reader reader, long length) throws SQLException {

        
    }
    public void updateNClob(String columnLabel, Reader reader, long length) throws SQLException {

        
    }
    public void updateNCharacterStream(int columnIndex, Reader x) throws SQLException {

        
    }
    public void updateNCharacterStream(String columnLabel, Reader reader) throws SQLException {

        
    }
    public void updateAsciiStream(int columnIndex, InputStream x) throws SQLException {

        
    }
    public void updateBinaryStream(int columnIndex, InputStream x) throws SQLException {

        
    }
    public void updateCharacterStream(int columnIndex, Reader x) throws SQLException {

        
    }
    public void updateAsciiStream(String columnLabel, InputStream x) throws SQLException {

        
    }
    public void updateBinaryStream(String columnLabel, InputStream x) throws SQLException {

        
    }
    public void updateCharacterStream(String columnLabel, Reader reader) throws SQLException {

        
    }
    public void updateBlob(int columnIndex, InputStream inputStream) throws SQLException {

        
    }
    public void updateBlob(String columnLabel, InputStream inputStream) throws SQLException {

        
    }
    public void updateClob(int columnIndex, Reader reader) throws SQLException {

        
    }
    public void updateClob(String columnLabel, Reader reader) throws SQLException {

        
    }
    public void updateNClob(int columnIndex, Reader reader) throws SQLException {

        
    }
    public void updateNClob(String columnLabel, Reader reader) throws SQLException {

        
    }
    public Object unwrap(Class arg0) throws SQLException {

        return null;
    }
    public boolean isWrapperFor(Class arg0) throws SQLException {

        return false;
    }
	public RowSet getRowSet() {
		return this.rowSet;
	}
}
