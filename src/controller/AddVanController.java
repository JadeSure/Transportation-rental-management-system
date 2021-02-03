
package controller;

import java.util.HashMap;

import Exception.InvalidException;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.converter.LocalDateStringConverter;
import model.Car;
import database.DatabaseHandle;
import model.FileHandle;
import model.Van;
import util.DateTime;
import view.AlertDialog;

public class AddVanController implements EventHandler<ActionEvent> {

	private Stage dialogBox;
	private AlertDialog alert;
	private DatabaseHandle databaseHandle = DatabaseHandle.getDataBaseHandle();

	public AddVanController(Stage dialogBox) {
		this.dialogBox = dialogBox;
	}

	// process van information
	@Override
	public void handle(ActionEvent event) {
		Scene scene = dialogBox.getScene();

		try {
			TextField vehicleID = (TextField) scene.lookup("#VehicleID");
			TextField yearField = (TextField) scene.lookup("#year");
			TextField modelField = (TextField) scene.lookup("#model");
			TextField makeField = (TextField) scene.lookup("#make");
			DatePicker lastMaintenanceDate = (DatePicker) scene.lookup("#lastMaintenanceDate");
			int year = Integer.parseInt(yearField.getText());
			LocalDate value = lastMaintenanceDate.getValue();
			String stringDate = value.toString();
			// format local data to string
			int day = Integer.parseInt(stringDate.substring(8, 10));
			int month = Integer.parseInt(stringDate.substring(5, 7));
			int dateYear = Integer.parseInt(stringDate.substring(0, 4));
			DateTime dateTimeDate = new DateTime(day, month, dateYear);
			if (!vehicleID.getText().startsWith("V_")) {
				throw new InvalidException("Wrong ID format");
			}
			if (databaseHandle.checkIdRepeat(vehicleID.getText())) {
				throw new InvalidException("This ID has been registered");
			}
			// save values into the constructor of van
			Van newCar = new Van(vehicleID.getText(), year, makeField.getText(), modelField.getText(), dateTimeDate);
			DatabaseHandle databaseHandle = DatabaseHandle.getDataBaseHandle();
			databaseHandle.addVanToDataBase(newCar);
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
