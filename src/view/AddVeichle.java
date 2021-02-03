package view;

import java.time.LocalDate;

import controller.AddCarController;
import controller.AddVanController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AddVeichle {

	private Stage dialogBox; 

	public void addCar() {
		// primary stage
	    dialogBox = new Stage();
		dialogBox.setTitle("Car information entry dialog box");
		
		// dialog components
		TextField IDField = new TextField();
		IDField.setPromptText("begin with C_");
		IDField.setId("VehicleID");
		IDField.setMaxWidth(250);
		Label vehicleID = new Label("Vehicle ID: ");
		
		TextField yearField = new TextField();
		yearField.setPromptText("YYYY");
		yearField.setId("year");
		Label year = new Label("Year: ");
		
		TextField modelField = new TextField();
		modelField.setPromptText("Model");
		modelField.setId("model");
		Label model = new Label("Model: ");

		TextField makeField = new TextField();
		makeField.setPromptText("Make");
		makeField.setId("make");
		Label make = new Label("Make: ");

		TextField numSeatsField = new TextField();
		numSeatsField.setPromptText("Number of seats (4 / 7)");
		numSeatsField.setId("numOfSeats");
		Label numOfSeats = new Label("Number of Seats: ");

		Button dialogOKButton = new Button("Add Car");
		Button dialogCancelButton = new Button("Cancel");
		
		// VBox include all panes
		VBox dialogInput = new VBox();
		
		// save input information
		GridPane textInput = new GridPane();
		textInput.setPadding(new Insets(15,15,15,15));
		
		textInput.add(vehicleID, 0,0);
		textInput.add(IDField, 1,0);
		textInput.add(year, 0, 1);
		textInput.add(yearField, 1, 1);
		textInput.add(model, 0, 2);
		textInput.add(modelField, 1, 2);
		textInput.add(make, 0, 3);
		textInput.add(makeField, 1, 3);
		textInput.add(numOfSeats, 0, 4);
		textInput.add(numSeatsField, 1, 4);

		// HBox will be used to save two buttons
		HBox dialogButtons = new HBox();
		dialogButtons.setSpacing(40);
		
		dialogInput.getChildren().add(textInput);
		dialogInput.getChildren().add(dialogButtons);
		
		Scene scene = new Scene(dialogInput, 350, 200);
		
		dialogButtons.getChildren().add(dialogCancelButton);
		dialogButtons.getChildren().add(dialogOKButton);
		dialogCancelButton.setOnAction(e ->{
			dialogBox.close();
		});
		
		dialogOKButton.setOnAction(new AddCarController(dialogBox));
		dialogButtons.setAlignment(Pos.CENTER);
		
		dialogBox.setTitle("input car information");
		dialogBox.setScene(scene);
		dialogBox.show();
	}
	
	
	public void addVan() {
		dialogBox = new Stage();
		dialogBox.setTitle("Van information entry dialog box");
		
		// dialog components
		TextField IDField = new TextField();
		IDField.setPromptText("begin with V_");
		IDField.setId("VehicleID");
		IDField.setMaxWidth(250);
		Label vehicleID = new Label("Vehicle ID: ");
		
		TextField yearField = new TextField();
		yearField.setPromptText("YYYY");
		yearField.setId("year");
		Label year = new Label("Year: ");
		
		
		TextField modelField = new TextField();
		modelField.setPromptText("Model");
		modelField.setId("model");
		Label model = new Label("Model: ");

		TextField makeField = new TextField();
		makeField.setPromptText("Make");
		makeField.setId("make");
		Label make = new Label("Make: ");

		// collect the date of maintenance
		Label lmd = new Label("Maintenance date: ");
		DatePicker lastMaintenanceDate = new DatePicker();
		lastMaintenanceDate.setPromptText("select date in here-->");
		lastMaintenanceDate.setId("lastMaintenanceDate");
		
		Button dialogOKButton = new Button("Add Van");
		Button dialogCancelButton = new Button("Cancel");
		
		// VBox include all panes
		VBox dialogInput = new VBox();
		
		// save input information
		GridPane textInput = new GridPane();
		textInput.setPadding(new Insets(15,15,15,15));
		
		textInput.add(vehicleID, 0,0);
		textInput.add(IDField, 1,0);
		textInput.add(year, 0, 1);
		textInput.add(yearField, 1, 1);
		textInput.add(model, 0, 2);
		textInput.add(modelField, 1, 2);
		textInput.add(make, 0, 3);
		textInput.add(makeField, 1, 3);
		textInput.add(lmd, 0, 4);
		textInput.add(lastMaintenanceDate, 1, 4);
		
		// HBox will be used to save two buttons
		HBox dialogButtons = new HBox();
		dialogButtons.setSpacing(40);
		
		dialogInput.getChildren().add(textInput);
		dialogInput.getChildren().add(dialogButtons);
		
		Scene scene = new Scene(dialogInput, 350, 200);
		
		dialogButtons.getChildren().add(dialogCancelButton);
		dialogButtons.getChildren().add(dialogOKButton);
		dialogCancelButton.setOnAction(e ->{
			dialogBox.close();
		});
		
		dialogOKButton.setOnAction(new AddVanController(dialogBox));
		
		dialogButtons.setAlignment(Pos.CENTER);
		
		dialogBox.setTitle("Input van information");
		dialogBox.setScene(scene);
		dialogBox.show();
	}
	
}
