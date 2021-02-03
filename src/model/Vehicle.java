package model;

import util.DateTime;

public interface Vehicle {

	String getId();
	
	int getYear();
	
	String getMake();
	
	String getModel();
	
	int getNumOfSeats();
	
	Status getStatus();
	
	Status setStatus();
	
	double getLateRates();
	
	void returnVehicle(DateTime returnDate);
	
	void completeMaintenance(DateTime completionDate);
	
	void rent(String customerID, DateTime rentDate, int numOfRentday);
	
	double getRentalRates();
	
}
