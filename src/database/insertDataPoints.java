package database;

import java.sql.Connection;
import java.sql.Statement;


public class insertDataPoints {

	public static void main(String[] args) {
		final String DB_NAME = "ThrifySystem";
		final String TABLE_NAME = "VEHICLE";
		final String TABLE_NAME_RECORDER = "RECORDER";
		
		//use try-with-resources Statement
		try (Connection con = ConnectionTest.getConnection(DB_NAME);
				Statement stmt = con.createStatement();
		) {
			String query = ""
			+"INSERT INTO " + TABLE_NAME + " VALUES ('C_001', 1998, 'Audi', 'sprinter', 4, 'Available', '', 'Car1.jpg')"
			+ "INSERT INTO " + TABLE_NAME +" VALUES ('C_002', 2019, 'Audi', 'sprinter', 4, 'Available', '', 'Car2.jpg')"
			+ "INSERT INTO " + TABLE_NAME +" VALUES ('C_003', 1996, 'Audi', 'sprinter', 4, 'Available', '', 'Car3.jpg')"
			+ "INSERT INTO " + TABLE_NAME +" VALUES ('C_004', 1990, 'Audi', 'Jin', 4, 'Available', '', 'Car4.jpg')"
			+ "INSERT INTO " + TABLE_NAME +" VALUES ('C_005', 1999, 'Audi', 'Lulu', 4, 'Available', '', 'Car5.jpg')"
			+ "INSERT INTO " + TABLE_NAME +" VALUES ('C_006', 1998, 'Audi', 'Zo Yi', 4, 'Available', '', 'Car6.jpg')"
			+ "INSERT INTO " + TABLE_NAME +" VALUES ('C_007', 1948, 'Audi', 'Shuo', 7, 'Available', '', 'Car7.jpg')"
			+ "INSERT INTO " + TABLE_NAME +" VALUES ('C_008', 1998, 'Audi', 'sprinter', 7, 'Available', '', 'Car8.jpg')"
			+ "INSERT INTO " + TABLE_NAME +" VALUES ('C_009', 2008, 'Audi', 'sprinter', 7, 'Available', '', 'Car9.jpg')"
			+ "INSERT INTO " + TABLE_NAME +" VALUES ('C_010', 2010, 'Audi', 'sprinter', 7, 'Available', '', 'Car10.jpg')"
			+ "INSERT INTO " + TABLE_NAME +" VALUES ('V_001', 2010, 'Toyota', 'sprinter', 15, 'Available', '01/06/2019', 'Van1.jpg')"
			+ "INSERT INTO " + TABLE_NAME +" VALUES ('V_002', 2012, 'Toyota', 'sprinter', 15, 'Available', '01/06/2019', 'Van2.jpg')"
			+ "INSERT INTO " + TABLE_NAME +" VALUES ('V_003', 2013, 'Toyota', 'sprinter', 15, 'Available', '01/06/2019', 'Van3.jpg')"
			+ "INSERT INTO " + TABLE_NAME +" VALUES ('V_004', 2015, 'Toyota', 'sprinter', 15, 'Available', '01/06/2019', 'Van4.jpg')"
			+ "INSERT INTO " + TABLE_NAME +" VALUES ('V_005', 2016, 'Toyota', 'sprinter', 15, 'Available', '01/06/2019', 'Van5.jpg')";
			
					
			int result = stmt.executeUpdate(query);
			
			con.commit();
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
