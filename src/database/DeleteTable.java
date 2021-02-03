package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.spi.DirStateFactory.Result;

public class DeleteTable {
	public static void main(String[] args) throws SQLException {
		deleteTables();
	}

	public static void deleteTables() {

		final String DB_NAME = "ThrifySystem";
		final String TABLE_NAME_VEHICLE = "VEHICLE";
		final String TABLE_NAME_RECORDER = "RECORDER";

		// use try-with-resources Statement
		try (Connection con = ConnectionTest.getConnection(DB_NAME); Statement stmt = con.createStatement();) {
			int result = stmt.executeUpdate("ALTER TABLE " + TABLE_NAME_RECORDER + " DROP CONSTRAINT fk_000");

			result = stmt.executeUpdate("DROP TABLE " + TABLE_NAME_VEHICLE);
			result = stmt.executeUpdate("DROP TABLE " + TABLE_NAME_RECORDER);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
