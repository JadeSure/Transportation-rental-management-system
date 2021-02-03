package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import model.Status;

public class ConnectionTest {
	public static void main(String[] args) {

		final String DB_NAME = "ThrifySystem";

		// use try-with-resources Statement
		try (Connection con = getConnection(DB_NAME)) {
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public static Connection getConnection(String dbName) throws SQLException, ClassNotFoundException {
		// Registering the HSQLDB JDBC driver
		Class.forName("org.hsqldb.jdbc.JDBCDriver");
		/*
		 * Database files will be created in the "database" folder in the project. If no
		 * username or password is specified, the default SA user and an empty password
		 * are used
		 */
		Connection con = DriverManager.getConnection("jdbc:hsqldb:file:database/" + dbName, "SA", "");
		return con;
	}

}
