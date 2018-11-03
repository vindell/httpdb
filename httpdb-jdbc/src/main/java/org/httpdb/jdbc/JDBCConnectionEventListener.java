package org.httpdb.jdbc;


import java.sql.SQLException;

public interface JDBCConnectionEventListener {

    void connectionClosed();

    void connectionErrorOccurred(SQLException e);
}
