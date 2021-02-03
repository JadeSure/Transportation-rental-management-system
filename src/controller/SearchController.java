package controller;

import view.MainView;

import java.util.List;

import database.DatabaseHandle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Car;
import model.Vehicle;
import view.AlertDialog;

public class SearchController implements EventHandler<ActionEvent> {
	private Stage primaryStage;
	private AlertDialog alert;

	public SearchController(Stage primaryStage) {
		this.primaryStage = primaryStage;

	}

	@Override
	public void handle(ActionEvent event) {
		Scene scene = primaryStage.getScene();
		VBox vBox = (VBox) scene.lookup("#vBox");
		ScrollPane scrollPane = (ScrollPane) vBox.lookup("#scrollPane");
		VBox functionunit = (VBox) vBox.getChildren().get(0);
		HBox hBox = (HBox) functionunit.getChildren().get(1);
//		hBox.getChildren().addAll(numOfSeats, type, status, make, search);
		ComboBox _numberOfSeats = (ComboBox) hBox.getChildren().get(0);
		ComboBox _type = (ComboBox) hBox.getChildren().get(1);
		ComboBox _status = (ComboBox) hBox.getChildren().get(2);
		ComboBox _make = (ComboBox) hBox.getChildren().get(3);
		try {
			int numberOfSeats = 0;
			if (_numberOfSeats.getValue() != null) {
				numberOfSeats = Integer.valueOf(_numberOfSeats.getValue().toString());
			}

			String type = _type.getValue() == null ? "" : _type.getValue().toString();
			String status = _status.getValue() == null ? "" : _status.getValue().toString();
			String make = _make.getValue() == null ? "" : _make.getValue().toString();
			char subType = 'a';
			if ("".equals(type)) {
				subType = 'a';
			} else if ("car".equals(type.toLowerCase())) {
				subType = 'c';
			} else {
				subType = 'v';
			}

			DatabaseHandle databaseHandle = DatabaseHandle.getDataBaseHandle();
			List<Vehicle> vehicles = databaseHandle.getAllVehicles();
			int count = 0;
			VBox tempVbox = new VBox();
			
			// for vehicle, if attributes = 0 or attributes = selected, pass
			for (Vehicle vehicle : vehicles) {
				if (subType != 'a') {
					if (vehicle.getId().charAt(0) != subType && vehicle.getId().toLowerCase().charAt(0) != subType) {
						continue;
					}
				}
				
				if (!"".equals(status) && !vehicle.getStatus().toString().toLowerCase().equals(status.toLowerCase())) {
					continue;
				}
				if (numberOfSeats!=0 && vehicle.getNumOfSeats() != numberOfSeats) {
					continue;
				}
				if (!"".equals(make) &&!make.toLowerCase().equals(vehicle.getMake().toLowerCase())) {
					continue;
				}

				tempVbox.getChildren().add(MainView.getOneVehicleBox(vehicle, primaryStage));
				count++;
				if (count > 49) {
					break;
				}
			}

			scrollPane.setContent(tempVbox);

		} catch (Exception e) {
			alert = new AlertDialog();
			alert.showWarning("Search did not process successful", e);
			;
		}
	}
}
