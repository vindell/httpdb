package org.httpdb.schema;

public class ColumnSchema {

	private String    name;
    private String    table;
    private String    schema;
    private String    catalog;
    boolean           isWriteable;
    private boolean   isSearchable;
    protected byte    parameterMode;
    protected boolean isIdentity;
    protected ColumnType  dataType;

    ColumnSchema() {}

    public ColumnSchema(String catalog, String schema, String table, String name) {
        this.catalog = catalog;
        this.schema  = schema;
        this.table   = table;
        this.name    = name;
    }

    public ColumnSchema(String catalog, ColumnSchema other) {

        this.catalog      = catalog;
        this.schema       = other.getSchemaNameString();
        this.table        = other.getTableNameString();
        this.name         = other.getNameString();
        this.isIdentity   = other.isIdentity();
        this.isSearchable = other.isSearchable();
        this.isWriteable  = other.isWriteable();
    }

    public String getNameString() {
        return name;
    }

    public String getTableNameString() {
        return table;
    }

    public String getSchemaNameString() {
        return schema;
    }

    public String getCatalogNameString() {
        return catalog;
    }

    public void setIdentity(boolean value) {
        isIdentity = value;
    }

    public boolean isIdentity() {
        return isIdentity;
    }

    protected void setType(ColumnSchema other) {
        dataType    = other.dataType;
    }

    public void setType(ColumnType type) {
        this.dataType = type;
    }

    public boolean isNullable() {
        return !isIdentity;
    }

    public boolean isWriteable() {
        return isWriteable;
    }

    public void setWriteable(boolean value) {
        isWriteable = value;
    }

    public boolean isSearchable() {
        return isSearchable;
    }

    public void setSearchable(boolean value) {
        isSearchable = value;
    }

    public ColumnType getDataType() {
        return dataType;
    }

    public byte getParameterMode() {
        return parameterMode;
    }

    public void setParameterMode(byte mode) {
        this.parameterMode = mode;
    }
    
}
