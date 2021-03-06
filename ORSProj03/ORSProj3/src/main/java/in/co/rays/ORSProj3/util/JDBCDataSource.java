package in.co.rays.ORSProj3.util;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import in.co.rays.ORSProj3.exception.ApplicationException;

/**
 * JDBC DataSource is a Data Connection Pool
 * 
 * @author Dilip Singh
 * @version 1.0
 * Copyright (c) SunilOS
 * 
 */
public class JDBCDataSource {

	private JDBCDataSource() {

	}

    /**
     * JDBC Database connection pool ( DCP )
     */
	private static JDBCDataSource datasource;

	private ComboPooledDataSource cpds = null;

    /**
     * Create instance of Connection Pool
     * 
     * @return
     */
	public static JDBCDataSource getInstance() {
		if (datasource == null) {
			ResourceBundle rb = ResourceBundle.getBundle("in.co.rays.ORSProj3.bundle.System");

			datasource = new JDBCDataSource();
			datasource.cpds = new ComboPooledDataSource();
			try {

				datasource.cpds.setDriverClass(rb.getString("driver"));
				datasource.cpds.setJdbcUrl(rb.getString("url"));
				datasource.cpds.setUser(rb.getString("username"));
				datasource.cpds.setPassword(rb.getString("password"));
				datasource.cpds.setInitialPoolSize(new Integer((String) rb.getString("initialPoolSize")));
				datasource.cpds.setAcquireIncrement(new Integer((String) rb.getString("acquireIncrement")));
				datasource.cpds.setMaxPoolSize(new Integer((String) rb.getString("maxPoolSize")));
				datasource.cpds.setMaxIdleTime(DataUtility.getInt(rb.getString("timeout")));
				datasource.cpds.setMinPoolSize(new Integer((String) rb.getString("minPoolSize")));

			} catch (PropertyVetoException e) {
				e.printStackTrace();
			}
		}

		return datasource;
	}

    /**
     * Gets the connection from ComboPooledDataSource
     * 
     * @return connection
     */
	public static Connection getConnection() throws Exception {
		return getInstance().cpds.getConnection();
	}

    /**
     * Closes a connection
     * 
     * @param connection
     * 
     */
	public static void closeConnection(Connection connection) {
		if (connection != null) {
			try {
				connection.close();
			} catch (Exception e) {

			}
		}
	}

    /**
     * Roll Back a connection
     * 
     * @param connection
     * 
     */
	public static void trnRollback(Connection connection) throws ApplicationException {
		if (connection != null) {
			try {
				connection.rollback();
			} catch (SQLException ex) {
				throw new ApplicationException(ex.toString());
			}
		}
	}
}
