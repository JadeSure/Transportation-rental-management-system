package model;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import util.DateTime;

// a class for rental record
public class RentalRecord {
	// instance variable
	private String id;
	private DateTime rentDate;
	private DateTime estimatedReturnDate;
	private DateTime actualReturnDate;
	private double rentalFee;
	private double lateFee;
	private String customerId;
	private String vehicleId;

	// constructor
	public RentalRecord(String vehicleId, String customerId, DateTime rentDate, DateTime estimatedReturnDate, DateTime actualReturnDate,
			double rentalFee, double lateFee) {
		this.id = vehicleId + customerId + rentDate.getEightDigitDate();
		this.rentDate = rentDate;
		this.estimatedReturnDate = estimatedReturnDate;
		this.actualReturnDate = actualReturnDate;
		this.rentalFee = rentalFee;
		this.lateFee = lateFee;
		this.vehicleId = vehicleId;	
	}
	
	
	public RentalRecord(String vehicleId, DateTime rentDate, DateTime estimatedReturnDate, DateTime actualReturnDate,
			double rentalFee, double lateFee,String recordId) {
		this.id = recordId;
		this.rentDate = rentDate;
		this.estimatedReturnDate = estimatedReturnDate;
		this.actualReturnDate = actualReturnDate;
		this.rentalFee = rentalFee;
		this.lateFee = lateFee;
		this.vehicleId = vehicleId;
	}
	
	
	
	public RentalRecord(String vehicleId, String customerId, DateTime rentDate, DateTime estimatedReturnDate,
			double rentalFee, double lateFee) {
		this(vehicleId, customerId, rentDate, estimatedReturnDate, estimatedReturnDate, rentalFee, lateFee);
		actualReturnDate = null;
	}

	// accessor and mutator method
	public DateTime getActualReturnDate() {
		return actualReturnDate;
	}

	public void setActualReturnDate(DateTime actualReturnDate) {
		this.actualReturnDate = actualReturnDate;
	}

	// set rental fee and put it into decimal 2
	public void setRentalFee(double rentalRates, double rentalDays) {
		rentalFee = rentalRates * rentalDays;
		rentalFee = DecimalTwo(rentalFee);
	}

	public void setLateFee(double lateRates, double lateDays) {
		lateFee = lateRates * lateDays;
		lateFee = DecimalTwo(lateFee);
	}
	

	public double getLateFee() {
		return lateFee;
	}

	public double getRentalFee() {
		return rentalFee;
	}

	public String getId() {
		return id;
	}

	public String getCustomerID() {
		return customerId;
	}

	public DateTime getRentDate() {
		return rentDate;
	}

	public DateTime getEstimatedReturnDate() {
		return estimatedReturnDate;
	}

	public String getVehicleId() {
		return vehicleId;
	}

	public String getRecorderID() {
		return id;
	}
	// a method to limit decimal number
	public double DecimalTwo(double num) {
		BigDecimal bigDecimal = new BigDecimal(num);
		num = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		return num;
	}

	// override method to get basic information
	@Override
	public String toString() {
		String returnDateStr = getActualReturnDate() == null ? "none" : "" + getActualReturnDate();
		String rentalFeeStr = getRentalFee() == 0 ? "none" : "" + getRentalFee();
		String lateFeeStr = getLateFee() == 0 ? "none" : "" + getLateFee();
		return getId() + ":" + getRentDate() + ":" + getEstimatedReturnDate() + ":" + returnDateStr + ":" + rentalFeeStr
				+ ":" + lateFeeStr;
	}

	// get the detail for records
	public String getDetails() {

		String detail1;
		String detail2;

		detail1 = "Record ID:			  " + getId() + '\n' + "Rent Date:		      " + getRentDate() + '\n'
				+ "Estimated Return Date: " + getEstimatedReturnDate();

		detail2 = "Actual Return Date:	  " + getActualReturnDate() + '\n' + "Rental Fee:			  " + getRentalFee()
				+ '\n' + "Late Fee:			  " + getLateFee();

		if (getActualReturnDate() == null) {
			return detail1;
		} else {
			return detail1 + detail2;
		}

	}
	

}
