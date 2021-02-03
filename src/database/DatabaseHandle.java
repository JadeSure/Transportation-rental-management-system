package database;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.Alert;
import model.Car;
import model.RentalRecord;
import model.Status;
import model.Van;
import model.Vehicle;
import util.DateTime;
import view.AlertDialog;

public class DatabaseHandle {

	private final String DB_NAME = "ThrifySystem";
	private final String VEHICLE_TABLE_NAME = "VEHICLE";
	private final String RECORD_TABLE_NAME = "RECORDER";
	private AlertDialog alert = null;
	private String customerId;
	private int rentDay;
	private int rentMonth;
	private int rentYear;
	private int estimatedReturnDay;
	private int estimatedReturnMonth;
	private int estimatedReturnYear;
	private int actualReturnDay;
	private int actualReturnMonth;
	private int actualReturnYear;
	private static DatabaseHandle dataBaseHandle = null;

	private DatabaseHandle() {
	}

	public static DatabaseHandle getDataBaseHandle() {
		if (dataBaseHandle == null) {
			dataBaseHandle = new DatabaseHandle();
		}
		return dataBaseHandle;
	}

	// insert a car into vehicle table
	public void addCarToDataBase(Car car) {
		try {

			Connection con = getConnection(DB_NAME);
			Statement stmt = con.createStatement();

			String vehicleID = car.getID();
			int year = car.getYear();
			String make = car.getMake();
			String model = car.getModel();
			int numOfSeats = car.getNumOfSeats();
			String status = car.getStatus().toString();
			String image = car.getImageName() == null ? "no image.png" : car.getImageName();

			String insertString = "INSERT INTO " + VEHICLE_TABLE_NAME + " VALUES ('" + vehicleID + "', " + year + ", '"
					+ make + "', '" + model + "', " + numOfSeats + ", '" + status + "', '', '" + image + "')";

			stmt.executeUpdate(insertString);
		} catch (Exception e) {
			alert.showWarning("a mistake happened in add Car to database", e);
		}
	}

	// check whether this ID has been registered
	public boolean checkIdRepeat(String vehicleID) {
		try {
			Connection con = getConnection(DB_NAME);
			Statement stmt = con.createStatement();
			String checkString = "Select * from " + VEHICLE_TABLE_NAME + " where vehicleID = '" + vehicleID + "';";
			ResultSet result = stmt.executeQuery(checkString);
			return result.next();
		} catch (Exception e) {
			e.getMessage();
			return false;
		}
	}

	// insert a van to database
	public void addVanToDataBase(Van van) {
		try {
			Connection con = getConnection(DB_NAME);
			Statement stmt = con.createStatement();

			String vehichleID = van.getID();
			int year = van.getYear();
			String make = van.getMake();
			String model = van.getModel();
			int numOfSeats = van.getNumOfSeats();
			String status = van.getStatus().toString();
			String lastMaintenanceDate = van.getlastMaintenanceDate() == null ? null
					: van.getlastMaintenanceDate().getFormattedDate();
			String image = van.getImageName() == null ? "no image.png" : van.getImageName();
			String insertString = "INSERT INTO " + VEHICLE_TABLE_NAME + " VALUES ('" + vehichleID + "', " + year + ", '"
					+ make + "', '" + model + "', " + numOfSeats + ", '" + status + "', '" + lastMaintenanceDate
					+ "', '" + image + "')";

			stmt.executeUpdate(insertString);
		} catch (Exception e) {
			alert.showWarning("a mistake happened in add Van to database", e);
		}
	}

	// insert a record to database
	public void addVanToDataBase(RentalRecord rentalRecord) {
		try {
			Connection con = getConnection(DB_NAME);
			Statement stmt = con.createStatement();

			String vehicleID = rentalRecord.getVehicleId();
			String recordID = rentalRecord.getId();
			DateTime rentDate = rentalRecord.getRentDate();
			DateTime estReturnDate = rentalRecord.getEstimatedReturnDate();
			DateTime actReturnDate = rentalRecord.getActualReturnDate();
			double rentalFee = rentalRecord.getRentalFee();
			double lateFee = rentalRecord.getLateFee();

			if (actReturnDate == null) {
				String insertString = "INSERT INTO " + VEHICLE_TABLE_NAME + " VALUES ('" + recordID + "', " + vehicleID
						+ ", '" + rentDate.getFormattedDate() + "', '" + estReturnDate.getFormattedDate() + "', " + ""
						+ ", '" + rentalFee + "', '" + lateFee + "')";
				stmt.executeUpdate(insertString);
			} else {
				String insertString = "INSERT INTO " + VEHICLE_TABLE_NAME + " VALUES ('" + recordID + "', " + vehicleID
						+ ", '" + rentDate.getFormattedDate() + "', '" + estReturnDate.getFormattedDate() + "', "
						+ actReturnDate.getFormattedDate() + ", '" + rentalFee + "', '" + lateFee + "')";
				stmt.executeUpdate(insertString);
			}

		} catch (Exception e) {
			alert.showWarning("a mistake happened in add Van to database", e);
		}
	}

	// update return vehicle for vehicle table and insert a new recorder table
	public void rentVehicle(Vehicle vehicle, String customerId, String bookTime, String returnTime) {
		this.customerId = customerId;

		try {
			Connection con = getConnection(DB_NAME);
			Statement stmt = con.createStatement();
			String vehicleId = vehicle.getId();
			String updateSql = "UPDATE " + VEHICLE_TABLE_NAME + " SET STATUS='RENTED' WHERE vehicleID='" + vehicleId
					+ "';";

			stmt.executeUpdate(updateSql);

			rentDay = Integer.valueOf(bookTime.substring(8, 10));
			rentMonth = Integer.valueOf(bookTime.substring(5, 7));
			rentYear = Integer.valueOf(bookTime.substring(0, 4));

			estimatedReturnDay = Integer.valueOf(returnTime.substring(8, 10));
			estimatedReturnMonth = Integer.valueOf(returnTime.substring(5, 7));
			estimatedReturnYear = Integer.valueOf(returnTime.substring(0, 4));

			DateTime rentDate = new DateTime(rentDay, rentMonth, rentYear);
			DateTime estimateReturnDate = new DateTime(estimatedReturnDay, estimatedReturnMonth, estimatedReturnYear);

			RentalRecord rentalRecord = new RentalRecord(vehicleId, customerId, rentDate, estimateReturnDate,
					vehicle.getRentalRates(), vehicle.getLateRates());

			String recordId = rentalRecord.getId();

			String insertSql = "INSERT INTO " + RECORD_TABLE_NAME + " VALUES('" + recordId + "','" + vehicleId + "','"
					+ rentalRecord.getRentDate().getFormattedDate() + "','"
					+ rentalRecord.getEstimatedReturnDate().getFormattedDate() + "','',0,0);";

			stmt.executeUpdate(insertSql);
		} catch (Exception e) {
			alert.showWarning("a mistake happened in rent vehicle to database", e);
		}
	}

	// update return maintenance for vehicle table and insert a new recorder table
	public void returnVehicle(Vehicle vehicle, String returnTime, RentalRecord rentalRecord) {
		try {
			Connection con = getConnection(DB_NAME);
			Statement stmt = con.createStatement();
			String vehicleId = vehicle.getId();

			String updateSql = "UPDATE " + VEHICLE_TABLE_NAME + " SET STATUS='AVAILABLE' WHERE vehicleID='" + vehicleId
					+ "';";
			stmt.executeUpdate(updateSql);

			actualReturnDay = Integer.valueOf(returnTime.substring(8, 10));
			actualReturnMonth = Integer.valueOf(returnTime.substring(5, 7));
			actualReturnYear = Integer.valueOf(returnTime.substring(0, 4));

			String recordId = rentalRecord.getId();
			String insertSql = "INSERT INTO " + RECORD_TABLE_NAME + " VALUES('" + recordId + "','" + vehicleId + "','"
					+ rentalRecord.getRentDate().getFormattedDate() + "','"
					+ rentalRecord.getEstimatedReturnDate().getFormattedDate() + "','"
					+ rentalRecord.getActualReturnDate().getFormattedDate() + "','" + rentalRecord.getRentalFee() + "','"
					+ rentalRecord.getLateFee() + "');";
			stmt.executeUpdate(insertSql);

		} catch (Exception e) {
			alert.showWarning("a mistake happened in return vehicle to database", e);
		}
	}

	public void performMaintenance(Vehicle vehicle) {
		try {
			Connection con = getConnection(DB_NAME);
			Statement stmt = con.createStatement();

			String vehicleId = vehicle.getId();
			String updateSql = "UPDATE " + VEHICLE_TABLE_NAME + " SET STATUS='" + Status.MAINTENANCE.toString()
					+ "' WHERE vehicleID='" + vehicleId + "';";
			stmt.executeUpdate(updateSql);
		} catch (Exception e) {
			alert.showWarning("a mistake happened in perform vehicle Maintenance to database", e);
		}
	}

	// update complete maintenance for vehicle table
	public void completeMaintenance(Vehicle vehicle, String completeTime) {
		try {
			Connection con = getConnection(DB_NAME);
			Statement stmt = con.createStatement();

			int completeDay = Integer.valueOf(completeTime.substring(8, 10));
			int completeMonth = Integer.valueOf(completeTime.substring(5, 7));
			int completeYear = Integer.valueOf(completeTime.substring(0, 4));
			DateTime lastMaintenanceDate = new DateTime(completeDay, completeMonth, completeYear);

			String vehicleId = vehicle.getId();

			String updateSql = "UPDATE " + VEHICLE_TABLE_NAME + " SET STATUS='AVAILABLE', lastMaintenanceDate = '"
					+ lastMaintenanceDate.getFormattedDate() + "' WHERE vehicleID='" + vehicleId + "';";
			stmt.executeUpdate(updateSql);

		} catch (Exception e) {
			alert.showWarning("a mistake happened in complete vehicle to maintenance to database", e);
		}
	}

	// delete all tables
	public void deleteAllTables() {
		DeleteTable.deleteTables();
	}

	
	public List<Vehicle> getAllVehicles() {
		List<Vehicle> vehicles = new ArrayList<Vehicle>();
		try {
			Connection con = getConnection(DB_NAME);
			Statement stmt = con.createStatement();

			ResultSet resultSet = stmt.executeQuery("SELECT * FROM " + VEHICLE_TABLE_NAME + ";");
			while (resultSet.next()) {
				String vehicleID = resultSet.getString("vehicleID");
				int year = resultSet.getInt("year");
				String make = resultSet.getString("make");
				String model = resultSet.getString("model");
				int numOfSeats = resultSet.getInt("numOfSeats");
				String lastMaintenanceDate = resultSet.getString("lastMaintenanceDate");

				Status status = Status.valueOf(resultSet.getString("status").toUpperCase());

				String image = resultSet.getString("image");
				if (vehicleID.toLowerCase().substring(0,1).equals("c")) {
					vehicles.add(new Car(vehicleID, year, make, model, numOfSeats, status, image));
				} else {
					int day = Integer.valueOf(lastMaintenanceDate.substring(0, 2));
					int month = Integer.valueOf(lastMaintenanceDate.substring(3, 5));
					int year2 = Integer.valueOf(lastMaintenanceDate.substring(6, 10));
					vehicles.add(new Van(vehicleID, year, make, model, numOfSeats, status,
							new DateTime(day, month, year2), image));
				}
			}
		} catch (Exception e) {
			alert.showWarning("a mistake happened in get all Vehicles ", e);
		}

		return vehicles;
	}

	public Vehicle getOneVehicle(String vehicleID) {
		List<Vehicle> vehicles = new ArrayList<Vehicle>();
		try {
			Connection con = getConnection(DB_NAME);
			Statement stmt = con.createStatement();

			ResultSet resultSet = stmt
					.executeQuery("SELECT * FROM " + VEHICLE_TABLE_NAME + " WHERE vehicleID = '" + vehicleID + "';");
			while (resultSet.next()) {
				int year = resultSet.getInt("year");
				String make = resultSet.getString("make");
				String model = resultSet.getString("model");
				int numOfSeats = resultSet.getInt("numOfSeats");
				String lastMaintenanceDate = resultSet.getString("lastMaintenanceDate");

				Status status = Status.valueOf(resultSet.getString("status").toUpperCase());

				String image = resultSet.getString("image");
				if (vehicleID.toLowerCase().substring(0,1).equals("c")) {
					vehicles.add(new Car(vehicleID, year, make, model, numOfSeats, status, image));
				} else {
					int day = Integer.valueOf(lastMaintenanceDate.substring(0, 2));
					int month = Integer.valueOf(lastMaintenanceDate.substring(3, 5));
					int year2 = Integer.valueOf(lastMaintenanceDate.substring(6, 10));
					vehicles.add(new Van(vehicleID, year, make, model, numOfSeats, status,
							new DateTime(day, month, year2), image));
				}
			}
		} catch (Exception e) {
			alert.showWarning("a mistake happened in get all Vehicles ", e);
		}

		return vehicles.get(0);
	}

	// get all recorder from database
	public List<RentalRecord> getAllRecords() {
		List<RentalRecord> rentalRecords = new ArrayList<RentalRecord>();
		try {
			Connection con = getConnection(DB_NAME);
			Statement stmt = con.createStatement();

			ResultSet resultSet = stmt.executeQuery("SELECT * FROM " + RECORD_TABLE_NAME + ";");
			while (resultSet.next()) {
				String vehicleID = resultSet.getString("vehicleID");
				String recordID = resultSet.getString("recordID");
				double rentalFee = resultSet.getDouble("rentalFee");
				double lateFee = resultSet.getDouble("lateFee");
				String rentDate = resultSet.getString("rentDate");
				String estReturnDate = resultSet.getString("estReturnDate");
				String actReturnDate = resultSet.getString("actReturnDate");

				if (actReturnDate == null || "".equals(actReturnDate)) {
					RentalRecord rentalRecord = new RentalRecord(vehicleID, new DateTime(rentDate, 0),
							new DateTime(estReturnDate, 0), null, rentalFee, lateFee, recordID);
					rentalRecords.add(rentalRecord);

				} else {
					RentalRecord rentalRecord = new RentalRecord(vehicleID, new DateTime(rentDate, 0),
							new DateTime(estReturnDate, 0), new DateTime(actReturnDate, 0), rentalFee, lateFee,
							recordID);
					rentalRecords.add(rentalRecord);
				}
			}
		} catch (Exception e) {
			alert.showWarning("a mistake happened in get all Records ", e);
		}
		return rentalRecords;
	}

	// get all the vehicle ID recorders from vehicle database
	public List<RentalRecord> getAllRecordsByVehicleID(String vehicleID) {
		List<RentalRecord> rentalRecords = new ArrayList<RentalRecord>();
		try {
			Connection con = getConnection(DB_NAME);
			Statement stmt = con.createStatement();

			ResultSet resultSet = stmt
					.executeQuery("SELECT * FROM " + RECORD_TABLE_NAME + " WHERE vehicleID='" + vehicleID + "';");
			while (resultSet.next()) {
				String recordID = resultSet.getString("recordID");
				double rentalFee = resultSet.getDouble("rentalFee");
				double lateFee = resultSet.getDouble("lateFee");
				String rentDate = resultSet.getString("rentDate");
				String estReturnDate = resultSet.getString("estReturnDate");
				String actReturnDate = resultSet.getString("actReturnDate");

				if (actReturnDate == null || "".equals(actReturnDate)) {
					RentalRecord rentalRecord = new RentalRecord(vehicleID, new DateTime(rentDate, 0),
							new DateTime(estReturnDate, 0), null, rentalFee, lateFee, recordID);
					rentalRecords.add(rentalRecord);

				} else {
					RentalRecord rentalRecord = new RentalRecord(vehicleID, new DateTime(rentDate, 0),
							new DateTime(estReturnDate, 0), new DateTime(actReturnDate, 0), rentalFee, lateFee,
							recordID);
					rentalRecords.add(rentalRecord);
				}
			}
		} catch (Exception e) {
			alert.showWarning("a mistake happened in get all Records ", e);
		}
		return rentalRecords;
	}

	public RentalRecord getNewestRecords(String vehicleID) {
		List<RentalRecord> rentalRecords = getAllRecordsByVehicleID(vehicleID);
		return rentalRecords.get(rentalRecords.size() - 1);

	}

	public void initAllTables() {
		InitializeDatabase.initDatabase();
	}

	// connect to database system
	private static Connection getConnection(String dbName) throws SQLException, ClassNotFoundException {
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
