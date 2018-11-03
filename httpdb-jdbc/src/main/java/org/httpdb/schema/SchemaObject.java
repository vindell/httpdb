package org.httpdb.schema;

/**
 * SQL schema object interface
 */
public interface SchemaObject {

    int DATABASE         = 0;
    int CATALOG          = 1;
    int SCHEMA           = 2;
    int TABLE            = 3;
    int VIEW             = 4;
    int CONSTRAINT       = 5;
    int ASSERTION        = 6;
    int SEQUENCE         = 7;
    int TRIGGER          = 8;
    int COLUMN           = 9;
    int TRANSITION       = 10;
    int GRANTEE          = 11;
    int TYPE             = 12;
    int DOMAIN           = 13;
    int CHARSET          = 14;
    int COLLATION        = 15;
    int FUNCTION         = 16;
    int PROCEDURE        = 17;
    int ROUTINE          = 18;
    int CURSOR           = 19;
    int INDEX            = 20;
    int LABEL            = 21;
    int VARIABLE         = 22;
    int PARAMETER        = 23;
    int SPECIFIC_ROUTINE = 24;
    int WRAPPER          = 25;
    int SERVER           = 26;
    int SUBQUERY         = 27;
    int SEARCH           = 28;
    int REFERENCE        = 29;

    SchemaObject[] emptyArray = new SchemaObject[]{};

    int getType();

    String getSQL();

    long getChangeTimestamp();

    interface ConstraintTypes {

        int FOREIGN_KEY = 0;
        int MAIN        = 1;
        int UNIQUE      = 2;
        int CHECK       = 3;
        int PRIMARY_KEY = 4;
        int TEMP        = 5;
    }

    /*
     SQL CLI codes

     Referential Constraint 0 CASCADE
     Referential Constraint 1 RESTRICT
     Referential Constraint 2 SET NULL
     Referential Constraint 3 NO ACTION
     Referential Constraint 4 SET DEFAULT
     */
    interface ReferentialAction {

        int CASCADE     = 0;
        int RESTRICT    = 1;
        int SET_NULL    = 2;
        int NO_ACTION   = 3;
        int SET_DEFAULT = 4;
    }

    interface Deferable {

        int INIT_DEFERRED  = 5;
        int INIT_IMMEDIATE = 6;
        int NOT_DEFERRABLE = 7;
    }

    interface ViewCheckModes {

        int CHECK_NONE    = 0;
        int CHECK_LOCAL   = 1;
        int CHECK_CASCADE = 2;
    }

    interface ParameterModes {

        byte PARAM_UNKNOWN = 0;    // java.sql.ParameterMetaData.parameterModeUnknown
        byte PARAM_IN    = 1;      // java.sql.ParameterMetaData.parameterModeIn
        byte PARAM_OUT   = 4;      // java.sql.ParameterMetaData.parameterModeInOut
        byte PARAM_INOUT = 2;      // java.sql.ParameterMetaData.parameterModeOut
    }

    interface Nullability {

        byte NO_NULLS         = 0;    // java.sql.ResultSetMetaData.columnNoNulls
        byte NULLABLE         = 1;    // java.sql.ResultSetMetaData.columnNullable
        byte NULLABLE_UNKNOWN = 2;    // java.sql.ResultSetMetaData.columnNullableUnknown
    }
}
