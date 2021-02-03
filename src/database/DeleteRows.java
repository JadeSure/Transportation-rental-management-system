package database;


import java.sql.Connection;
import java.sql.Statement;

public class DeleteRows {
	public static void main(String[] args) {
		final String DB_NAME = "ThrifySystem";
		final String TABLE_NAME = "VEHICLE";
		
		//use try-with-resources Statement
		try (Connection con = ConnectionTest.getConnection(DB_NAME);
				Statement stmt = con.createStatement();
		) {
			String query = "DELETE FROM " + TABLE_NAME + 
					" WHERE vehicleID = 'dfa'";
			
			int result = stmt.executeUpdate(query);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}