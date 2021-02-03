package model;

import java.util.Scanner;

import util.DateTime;

// a abstract class for vehicle
public abstract class AbstractVehicle implements Vehicle {

	// some instance variables that are common in both Van and Car
	// 1 or more protected constructor that will be used in subclasses
	protected String ID;
	protected int year;
	protected String make;
	protected String model;
	protected int numOfSeats;
	protected Status status;
	protected String imageName;

	
	// constructor
	public AbstractVehicle(String ID, int year, String make, String model, int numOfSeats, Status status, String imageName) {
		this.ID = ID;
		this.year = year;
		this.make = make;
		this.model = model;
		this.numOfSeats = numOfSeats;
		this.status = status;
		this.imageName = imageName;
	}

	// abstract method for Car and Van to perform
	// accessor and mutator method
	@Override
	public String getId() {
		return ID;
	}

	@Override
	public int getYear() {
		return year;
	}

	@Override
	public String getMake() {
		return make;
	}

	@Override
	public String getModel() {
		return make;
	}

	@Override
	public int getNumOfSeats() {
		return numOfSeats;
	}

	@Override
	public Status setStatus() {
		return null;
	}
	
	@Override
	public double getLateRates() {
		return 0;
	}

	@Override
	public double getRentalRates() {
		return 0;
	}

	// the operations required to rent the vehicle
	public abstract void rent(String customerID, DateTime rentDate, int numOfRentday);

	// perform the operations required to return the vehicle in superclass
	public abstract void returnVehicle(DateTime returnDate);

	// perform the maintenance for vehicle
	public abstract void performMaintenance();

	// perform the operations required
	// when the maintenance of that vehicle is finished for vehicle
	public abstract void completeMaintenance(DateTime completionDate);

	// a abstract method for get detail for car or van and at the most of ten
	// records
	public abstract String getDetails();

}
