package model;

import java.util.*;


import javax.xml.transform.Templates;

import Exception.MaintenanceException;
import database.DatabaseHandle;
import util.DateTime;
import Exception.MaintenanceException;
import Exception.RentException;
import Exception.ReturnException;

public class Van extends AbstractVehicle {

	// instance variable
	public static final int minimalDay = 1;
	public static final double rentalRates = 235;
	public static final double lateFee = 299;
	private DateTime lastMaintenanceDate;
	public static final int maintenanceInterval = 12;
	private static final int NUMBEROFSEATS = 15;
	private DatabaseHandle databaseHandle = DatabaseHandle.getDataBaseHandle();

	// constructor
	public Van(String ID, int year, String make, String model, int numOfSeats, Status status,
			DateTime lastMaintenanceDate, String imageName) {
		super(ID, year, make, model, numOfSeats, status, imageName);
		this.lastMaintenanceDate = lastMaintenanceDate;
	}

	// constructor overloading
	public Van(String ID, int year, String make, String model, DateTime lastMaintenanceDate, String imageName) {
		this(ID, year, make, model, NUMBEROFSEATS, Status.AVAILABLE, lastMaintenanceDate, imageName);
	}
	
	public Van(String ID, int year, String make, String model, DateTime lastMaintenanceDate) {
		this(ID, year, make, model, NUMBEROFSEATS, Status.AVAILABLE, lastMaintenanceDate, "no image.png");
	}

	// accessor method
	public String getID() {
		return ID;
	}

	public int getYear() {
		return year;
	}

	public Status getStatus() {
		return status;
	}

	public String getMake() {
		return make;
	}

	public String getModel() {
		return model;
	}

	public static int getMinimalday() {
		return minimalDay;
	}
	
	

	public static double getRentalrates() {
		return rentalRates;
	}

	public static double getLatefee() {
		return lateFee;
	}
	
	public double getLateRates() {
		return lateFee;
	}

	public double getRentalRates() {
		return rentalRates;
	}
	
	public String getImageName() {
		return imageName;
	}
	
	public DateTime getlastMaintenanceDate() {
		return lastMaintenanceDate;
	}

	// mutator method
	void setAvailable(Status status) {
		status = Status.AVAILABLE;
	}

	public void setMaintenance(Status status) {
		status = Status.MAINTENANCE;
	}

	public void setRented(Status status) {
		status = Status.RENTED;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public void setModel(String model) {
		this.model = model;
	}

	// perform the operations required to rent the vehicle for van
	@Override
	public void rent(String customerID, DateTime rentDate, int numOfRentday) throws RentException{

		DateTime nextMaintenanceDate = new DateTime(lastMaintenanceDate, maintenanceInterval);
		DateTime estimatedReturnDay = new DateTime(rentDate, numOfRentday);
		int diff = DateTime.diffDays(nextMaintenanceDate, estimatedReturnDay);

		// according to real situations to judge whether this van can be rented
		// if next maintenance date is smaller than estimated return day, it should be false
		if (diff < 0)
			throw new RentException("Sorry, next maintenance date is smaller than estimated return day");

		// if the situation of van is being maintenance and rented, this van should not
		// be rented
		if (status == Status.MAINTENANCE || status == Status.RENTED) {
			throw new RentException("This Van is being maintenance or rented, so it is not available right now");
		}

		// the rent day should not smaller than minimal days which is one day
		if (numOfRentday < minimalDay)
			throw new RentException("the rent day should not smaller than minimal day --- one day");

		// update record directly
		status = Status.RENTED;
		
		// if the process is successful, save it database
		databaseHandle.rentVehicle(this, customerID, DateTime.formatDateType(rentDate), DateTime.formatDateType(estimatedReturnDay));
	}

	// override toString method to get Car basic information
	@Override
	public String toString() {
		String record;
		record = getID() + ":" + getYear() + ":" + getMake() + ":" + getModel() + ":" + getNumOfSeats()+":"+
				 getStatus()+ ":"+getlastMaintenanceDate() +":"+getImageName();
		return record;
	}
	
	

	// perform the operations required to return the Van
	@Override
	public void returnVehicle(DateTime actualReturnDate) throws ReturnException{
		Van van = (Van) databaseHandle.getOneVehicle(ID);
		
		// check whether this van can be returned
	    // if this van is being available and maintenance, this van should not be returned
		if(van.getStatus()!=Status.RENTED) {
			throw new ReturnException("this van is not in rented and it's in : "+status.toString());
		}
		
		RentalRecord temp = databaseHandle.getNewestRecords(ID);
		double actualRentalDays = DateTime.diffDays(actualReturnDate, temp.getRentDate());
		double overRentalDays = DateTime.diffDays(actualReturnDate, temp.getEstimatedReturnDate());
		double normalFeeDays = actualRentalDays - overRentalDays;

		// return date should not be smaller than rent date
		if (DateTime.diffDays(actualReturnDate, temp.getRentDate()) < 0) {
			throw new ReturnException("return date should not be smaller than rent date");
		}

		// according to real situation to calculate the rental rates and late fee
		if (overRentalDays > 0) {
			temp.setRentalFee(rentalRates, normalFeeDays);
			temp.setLateFee(lateFee, overRentalDays);
		} else {
			temp.setRentalFee(rentalRates, normalFeeDays);
		}
		temp.setActualReturnDate(actualReturnDate);
		status = Status.AVAILABLE;
		databaseHandle.returnVehicle(this, DateTime.formatDateType(actualReturnDate),temp);	
	}

	
	// perform the maintenance for Van
	@Override
	public void performMaintenance() throws MaintenanceException {
		if (status == Status.RENTED) {
			throw new MaintenanceException("this van is being rented, so it is not available");
		}	
		DateTime today = new DateTime();
		lastMaintenanceDate = today;
		status = Status.MAINTENANCE;
		databaseHandle.performMaintenance(this);
	}

	// perform the operations required
	// when the maintenance of that vehicle is finished for Van
	@Override
	public void completeMaintenance(DateTime completionDate) throws MaintenanceException {
		if (status == Status.AVAILABLE || status == Status.RENTED) {
			throw new MaintenanceException("This van is being available or rented, so it is not available");
		}

		
		lastMaintenanceDate = completionDate;
		status = Status.AVAILABLE;
		databaseHandle.completeMaintenance(this, DateTime.formatDateType(completionDate));
	}

	// override getDetails method to get Van detail information
	// get at most 10 records for this van
	@Override
	public String getDetails() {
		String result = "";
		result += "Vehicle ID:   			" + ID + "\n" + "Year:				" + year + "\n"
				+ "Make:				" + make + "\n" + "Model:				" + model + "\n"
				+ "Number of seats:		" + numOfSeats + "\n" + "Status:				" + getStatus() + "\n"
				+ "Last maintenance date: "+ lastMaintenanceDate.toString()+"\n"
				+ "RENTAL RECORD:		" + "\n";

		List<RentalRecord> carRecorder = databaseHandle.getAllRecordsByVehicleID(ID);

		for (int i = 0; i < carRecorder.size() && i < 10; i++) {
			if (i == 0 && status == Status.RENTED) {

				RentalRecord record = carRecorder.get(carRecorder.size() - 1);
				result += "Record ID: 			 " + record.getId() + "\n" + "Rent date:			 "
						+ record.getRentDate() + "\n" + "Estimated Return Date:  "  + record.getEstimatedReturnDate()
						+ "\n";
			} else {
				result += "------------------------------------" + "\n";

				RentalRecord temp = carRecorder.get(carRecorder.size() - 1 - i);
				result += "Record ID: 			 " + temp.getId() + "\n" + "Rent date:	 		 " + temp.getRentDate()
						+ "\n" + "Estimated Return Date:  " + temp.getEstimatedReturnDate() + "\n"
						+ "Actual Return Date:        " + temp.getActualReturnDate() + "\n" + "Rental Fee: 			 "
						+ temp.getRentalFee() + "\n" + "Late Fee:                          " + temp.getLateFee() + "\n";
			}
		}
		result += "====================================" + "\n";
		return result;
	}
}