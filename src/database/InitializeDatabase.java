package database;

import java.sql.Connection;
import java.sql.Statement;

public class InitializeDatabase {

	public static void main(String[] args) {
		initDatabase();
	}
	public static void initDatabase(){
		final String DB_NAME = "ThrifySystem";
		final String TABLE_NAME_VEHICLE = "VEHICLE";
		final String TABLE_NAME_RECORDER = "RECORDER";

		
		
		// use try with resources Statement
		// create a table to save the information of vehicles
		try (Connection con = ConnectionTest.getConnection(DB_NAME); Statement stmt = con.createStatement();) {
			int result = stmt.executeUpdate("CREATE TABLE VEHICLE (" 
					+ "vehicleID VARCHAR(64) NOT NULL,"
					+ "year INT NOT NULL," 
					+ "make VARCHAR(64) NOT NULL,"
					+ "model VARCHAR(64) NOT NULL," 
					+ "numOfSeats int NOT NULL," 
					+ "status VARCHAR(64) NOT NULL," 
					+ "lastMaintenanceDate VARCHAR(64),"
					+ "image VARCHAR(64) NOT NULL,"
					+ "PRIMARY KEY (vehicleID))");
			
			// create a recorder table to save generated information
			result = stmt.executeUpdate("CREATE TABLE RECORDER ("
	                    + "recordID VARCHAR(128) NOT NULL,"
	                    + "vehicleID VARCHAR(64) NOT NULL,"
	                    + "rentDate VARCHAR(64) NOT NULL,"
	                    + "estReturnDate VARCHAR(64) NOT NULL,"
	                    + "actReturnDate VARCHAR(64),"
	                    + "rentalFee DECIMAL(10,2),"
	                    + "lateFee DECIMAL(10,2),"
	                    + "CONSTRAINT fk_000 FOREIGN KEY (vehicleID) REFERENCES VEHICLE(vehicleID))");
			} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

}
