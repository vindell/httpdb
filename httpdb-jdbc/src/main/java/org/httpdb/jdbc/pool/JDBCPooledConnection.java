package org.httpdb.jdbc.pool;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.ConnectionEvent;
import javax.sql.ConnectionEventListener;
import javax.sql.PooledConnection;
import javax.sql.StatementEventListener;

import org.httpdb.jdbc.JDBCConnectionEventListener;


public abstract class JDBCPooledConnection implements PooledConnection, JDBCConnectionEventListener {

	protected Connection physicalConn;
	protected volatile Connection handleConn;

	protected List<ConnectionEventListener> listeners = new ArrayList<ConnectionEventListener>();

	/**
	 * Constructor.
	 * 
	 * @param physicalConn
	 *            The physical Connection.
	 */
	protected JDBCPooledConnection(Connection physicalConn) {
		this.physicalConn = physicalConn;
	}

	/**
	 * @see javax.sql.PooledConnection#close()
	 */
	public void close() throws SQLException {
		if (handleConn != null) {
			listeners.clear();
			handleConn.close();
		}

		if (physicalConn != null) {
			try {
				physicalConn.close();
			} finally {
				physicalConn = null;
			}
		}
	}

	/**
	 * @see javax.sql.PooledConnection#getConnection()
	 */
	public Connection getConnection() throws SQLException {
		if (handleConn != null) {
			handleConn.close();
		}
		handleConn = (Connection) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] { Connection.class }, new InvocationHandler() {
			
			boolean isClosed;

			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				try {
					String name = method.getName();
					if ("close".equals(name)) {
						ConnectionEvent event = new ConnectionEvent(JDBCPooledConnection.this);

						for (int i = listeners.size() - 1; i >= 0; i--) {
							listeners.get(i).connectionClosed(event);
						}

						if (!physicalConn.getAutoCommit()) {
							physicalConn.rollback();
						}
						physicalConn.setAutoCommit(true);
						isClosed = true;

						return null; // don't close physical connection
					} else if ("isClosed".equals(name)) {
						if (!isClosed)
							isClosed = ((Boolean) method.invoke(physicalConn, args)).booleanValue();

						return isClosed;
					}

					if (isClosed) {
						throw new SQLException("Connection is closed");
					}

					return method.invoke(physicalConn, args);
				} catch (SQLException e) {
					if ("database connection closed".equals(e.getMessage())) {
						ConnectionEvent event = new ConnectionEvent(JDBCPooledConnection.this, e);
						for (int i = listeners.size() - 1; i >= 0; i--) {
							listeners.get(i).connectionErrorOccurred(event);
						}
					}
					throw e;
				} catch (InvocationTargetException ex) {
					throw ex.getCause();
				}
			}
		});

		return handleConn;
	}

	/**
	 * @see javax.sql.PooledConnection#addConnectionEventListener(javax.sql.ConnectionEventListener)
	 */
	public void addConnectionEventListener(ConnectionEventListener listener) {
		listeners.add(listener);
	}

	/**
	 * @see javax.sql.PooledConnection#removeConnectionEventListener(javax.sql.ConnectionEventListener)
	 */
	public void removeConnectionEventListener(ConnectionEventListener listener) {
		listeners.remove(listener);
	}

	public void addStatementEventListener(StatementEventListener listener) {
		// TODO impl
	}

	public void removeStatementEventListener(StatementEventListener listener) {
		// TODO impl
	}
}
