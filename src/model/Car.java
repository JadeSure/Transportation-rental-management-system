package model;

import util.DateTime;

import java.awt.TexturePaint;
import java.util.*;

import javax.naming.InitialContext;

import Exception.MaintenanceException;
import Exception.RentException;
import Exception.ReturnException;
import database.DatabaseHandle;

public class Car extends AbstractVehicle {

	// instance variable

	private int minimalDay;
	private static final int maximumDay = 14;
	private double rentalRates;
	private double lateRates;
	private static final double LATERATE = 1.25;
	private static final double FOURSEATS = 78;
	private static final double SEVENSEATS = 113;
	private DateTime maintenanceDate;
	private DatabaseHandle databaseHandle = DatabaseHandle.getDataBaseHandle();

	// constructor
	public Car(String ID, int year, String make, String model, int numOfSeats, Status status, String imageName) {
		super(ID, year, make, model, numOfSeats, status, imageName);
		// according to the number of seats for car to calculate rental fee
		if (numOfSeats == 4) {
			rentalRates = FOURSEATS;
		} else {
			rentalRates = SEVENSEATS;
		}

	}

	// constructor overloading
	public Car(String vehicleID, int year, String make, String model, int numOfSeats, String imageName) {
		this(vehicleID, year, make, model, numOfSeats, Status.AVAILABLE, imageName);
	}

	public Car(String vehicleID, int year, String make, String model, int numOfSeats) {
		this(vehicleID, year, make, model, numOfSeats, Status.AVAILABLE, "no image.png");
	}

	// accessor method
	public String getID() {
		return ID;
	}

	public int getYear() {
		return year;
	}

	public String getMake() {
		return make;
	}

	public String getModel() {
		return model;
	}

	public Status getStatus() {
		return status;
	}

	public int getMaximunDay() {
		return maximumDay;
	}

	public DateTime getMaintenanceDate() {
		return maintenanceDate;
	}

	public double getLateRates() {
		return lateRates;
	}

	public int getMinimalDay() {
		return minimalDay;
	}

	public int getNumOfSeats() {
		return numOfSeats;
	}

	public double getRentalRates() {
		return rentalRates;
	}

	public String getImageName() {
		return imageName;
	}

	// mutator method
	public void setAvailable(Status status) {
		status = Status.AVAILABLE;
	}

	public void setMaintenance(Status status) {
		status = Status.MAINTENANCE;
	}

	public void setRented(Status status) {
		status = Status.RENTED;
	}

	public void setLateRates(double rentalRates) {
		lateRates = rentalRates * LATERATE;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	// set minimal day for car in two situations "minimal 2 days and minimal 3 days"
	// normally situation is 1 day
	public void setMinimalDay(DateTime rentDate) {
		String startDay;
		startDay = rentDate.getNameOfDay();

		if (startDay.equals("Sunday") || startDay.equals("Monday") || startDay.equals("Thursday")) {
			minimalDay = 2;
		} else if (startDay.equals("Friday") || startDay.equals("Saturday")) {
			minimalDay = 3;
		} else {
			minimalDay = 1;
		}
	}

	// perform the operations required to rent the vehicle for car
	@Override
	public void rent(String customerID, DateTime rentDate, int numOfRentday) throws RentException {
			
		setMinimalDay(rentDate);
		if (customerID.equals(null) || customerID.equals("")) {
			throw new RentException("customerID should not be null");
		}
		// rent days should not smaller than minimal days
		if (numOfRentday < getMinimalDay()) {
			throw new RentException("Rent days should bigger than "+getMinimalDay()+" days " + "on " + rentDate.getNameOfDay());
		}
		if (numOfRentday > Car.maximumDay)
			throw new RentException("Rent days should smaller than " + Car.maximumDay + " days");

		// if the status of this car is maintenance and rented, this car should not be
		// rented
		if (status == Status.MAINTENANCE || status == Status.RENTED) {
			throw new RentException("This car is under maintenance or rented");
		}

		// according to rent date and how many number of rent days, calculate estimated
		// date
		DateTime estimatedDate = new DateTime(rentDate, numOfRentday);

		// update the recorder after operation
		status = Status.RENTED;

		databaseHandle.rentVehicle(this, customerID, DateTime.formatDateType(rentDate),
				DateTime.formatDateType(estimatedDate));
	}

	// perform the operations required to return the Car
	@Override
	public void returnVehicle(DateTime actualReturnDate) throws ReturnException {

		// pass value from the records (the news one) to the object of rental record
		// calculate the actual rental days, over rental days and normal fee days
		Car car = (Car) databaseHandle.getOneVehicle(ID);
		if (car.getStatus() != Status.RENTED) {
			throw new ReturnException("this car is not in rented and it's in : " + status.toString());
		}

		RentalRecord temp = databaseHandle.getNewestRecords(ID);
		double actualRentalDays = DateTime.diffDays(actualReturnDate, temp.getRentDate());
		double overRentalDays = DateTime.diffDays(actualReturnDate, temp.getEstimatedReturnDate());
		double normalFeeDays = actualRentalDays - overRentalDays;
		temp.setActualReturnDate(actualReturnDate);

		// if actual return date smaller than rent date, it should be return false
		if (DateTime.diffDays(actualReturnDate, temp.getRentDate()) < 0) {
			throw new ReturnException("Actual return date should not smaller than rent date");
		}

		// according to situation to set rental fee and late rates
		if (overRentalDays > 0) {
			temp.setRentalFee(getRentalRates(), normalFeeDays);
			setLateRates(rentalRates);

			temp.setLateFee(getLateRates(), overRentalDays);
		}
		if (overRentalDays < 0) {

			temp.setRentalFee(getRentalRates(), actualRentalDays);
		} else {
			temp.setRentalFee(getRentalRates(), normalFeeDays);
		}

		// set status to available
		this.status = Status.AVAILABLE;
		databaseHandle.returnVehicle(this, DateTime.formatDateType(actualReturnDate), temp);
	}

	// perform the maintenance for Car
	@Override
	public void performMaintenance() throws MaintenanceException {
		// if this car is being rented, this car should not be performed to maintenance
		if (status == Status.RENTED) {
			throw new MaintenanceException("This car is being rented, you cannot perform maintenance right now");
		}
		DateTime today = new DateTime();
		maintenanceDate = today;
		status = Status.MAINTENANCE;
		databaseHandle.performMaintenance(this);
	}

	// perform the operations required when the maintenance of that vehicle is
	// finished for Car
	@Override
	public void completeMaintenance(DateTime completionDate) throws MaintenanceException {
		if (status == Status.AVAILABLE || status == Status.RENTED) {
			throw new MaintenanceException(
					"This car is being available or rented, you cannot complete maintenance right now");
		}
		completionDate = new DateTime();
		status = Status.AVAILABLE;
		databaseHandle.completeMaintenance(this, DateTime.formatDateType(completionDate));
		;
	}

	// override toString method to get Car basic information
	@Override
	public String toString() {
		String record;
		record = getID() + ":" + getYear() + ":" + getMake() + ":" + getModel() + ":" + getNumOfSeats() + ":"
				+ getStatus() + ":" + getImageName();
		return record;
	}

	// override getDetails method to get Car detail information
	// get the most 10 records
	@Override
	public String getDetails() {
		String result = "";
		result += "Vehicle ID:   			" + getID() + "\n" + "Year:				" + getYear() + "\n"
				+ "Make:				" + getMake() + "\n" + "Model:				" + getModel() + "\n"
				+ "Number of seats:		" + getNumOfSeats() + "\n" + "Status:				" + getStatus() + "\n"
				+ "RENTAL RECORD:		" + "\n";

		List<RentalRecord> carRecorder = databaseHandle.getAllRecordsByVehicleID(ID);
		for (int i = 0; i < carRecorder.size() && i < 10; i++) {
			if (i == 0 && status == Status.RENTED) {

				RentalRecord temp = carRecorder.get(carRecorder.size() - 1);
				result += "Record ID: 			 " + temp.getId() + "\n" + "Rent date:			 " + temp.getRentDate()
						+ "\n" + "Estimated Return Date:  " + temp.getEstimatedReturnDate() + "\n";

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