package org.httpdb.jdbc;


import java.sql.SQLException;
import java.sql.Savepoint;

import org.httpdb.utils.ExceptionUtils;

public class HttpdbSavepoint implements Savepoint {

	final int id;

	final String name;

	HttpdbSavepoint(int id) {
		this.id = id;
		this.name = null;
	}

	HttpdbSavepoint(int id, String name) {
		this.id = id;
		this.name = name;
	}

	HttpdbSavepoint(HttpdbConnection conn) throws SQLException {
        if (conn == null) {
            throw ExceptionUtils.nullArgument("conn");
        }
        this.id         = conn.getSavepointID();
        this.name = null;
    }
    

	public int getSavepointId() throws SQLException {
		return id;
	}

	public String getSavepointName() throws SQLException {
		return name == null ? String.format("HTTPDB_SAVEPOINT_%s", id) : name;
	}
	
}