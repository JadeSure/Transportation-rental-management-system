
package controller;

import java.util.HashMap;

import javax.activity.InvalidActivityException;

import Exception.InvalidException;

import java.util.*;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Car;
import database.DatabaseHandle;
import model.FileHandle;
import view.AlertDialog;


public class AddCarController implements EventHandler<ActionEvent>{

    private Stage dialogBox;
    private AlertDialog alert;
	private DatabaseHandle databaseHandle = DatabaseHandle.getDataBaseHandle();

    
    public AddCarController(Stage dialogBox) {
    	this.dialogBox = dialogBox;
    }
    
    

	@Override
	public void handle(ActionEvent event) {
		Scene scene = dialogBox.getScene();
		
		try {
			TextField vehicleID = (TextField) scene.lookup("#VehicleID");
			TextField yearField = (TextField) scene.lookup("#year");
			TextField modelField = (TextField) scene.lookup("#model");
			TextField makeField = (TextField) scene.lookup("#make");
			TextField numOfSeatsField = (TextField) scene.lookup("#numOfSeats");
			
			int year = Integer.parseInt(yearField.getText());
			int numOfSeats = Integer.parseInt(numOfSeatsField.getText());
			if (!vehicleID.getText().startsWith("C_")){
				throw new InvalidException("Wrong ID type");
			}
			
			if (databaseHandle.checkIdRepeat(vehicleID.getText())) {
				throw new InvalidException("This ID has been registered");
			}
			
			if (!(numOfSeats == 4) && !(numOfSeats == 7)) {
				throw new InvalidException("Wrong number of Seats");
			}
			
			Car newCar = new Car(vehicleID.getText(), year, makeField.getText(), modelField.getText(), numOfSeats);
			DatabaseHandle databaseHandle = DatabaseHandle.getDataBaseHandle();
			databaseHandle.addCarToDataBase(newCar);
			alert = new AlertDialog();
			alert.showInformation();
		} catch (Exception e) {
			alert = new AlertDialog();

			alert.showWarning("message did not save successful", e);
		} finally {
			dialogBox.close();
		}
	}
	

}
