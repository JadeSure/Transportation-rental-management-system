package view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.Format;
import java.time.LocalDate;
import java.util.List;

import org.hsqldb.Database;
import org.hsqldb.lib.CountdownInputStream;

import Exception.MaintenanceException;
import controller.ExportFileHandle;
import controller.ImportFileHandle;
import controller.SearchController;
import database.DatabaseHandle;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Car;
import model.Van;
import model.Vehicle;
import util.DateTime;

public class MainView extends Application {
	private static AlertDialog alert = new AlertDialog();
	private ListView<String> ListView = null;

	// start up
	public static void appStart(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {

		
		// create a main pane
		VBox vBox = new VBox();
		vBox.setSpacing(10);
		vBox.setAlignment(Pos.TOP_CENTER);
		vBox.setId("vBox");
		// the second pane which is located in main pane

		// the third one --- scroll pane
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setPannable(true);
		scrollPane.setFitToWidth(true);
		scrollPane.setId("scrollPane");
		
		

		// add function unit in vBox
		
		vBox.getChildren().add(functionUnitBox(primaryStage));
		vBox.getChildren().add(scrollPane);
		
		// test output scroll pane
		VBox hboxtest = getAllVehicles(primaryStage);
		scrollPane.setContent(hboxtest);

		Scene scene = new Scene(vBox,800, 600);
	
		primaryStage.setTitle("ThrifySystem");
		primaryStage.setScene(scene);
		primaryStage.show();	
	}


	public static VBox getAllVehicles(Stage primaryStage) throws FileNotFoundException {
		VBox vbox = new VBox();
		DatabaseHandle databaseHandle = DatabaseHandle.getDataBaseHandle();
		List<Vehicle> vehicles = databaseHandle.getAllVehicles();
		int count = 0;
		for (Vehicle vehicle : vehicles) {
			vbox.getChildren().add(getOneVehicleBox(vehicle,primaryStage));
			count++;
			if (count > 49) {
				break;
			}
		}
		return vbox;
	}

	public static HBox getOneVehicleBox(Vehicle vehicle,Stage primaryStage) throws FileNotFoundException {
		HBox hboxTest = new HBox();
		VBox vboxTest = new VBox();
		HBox hBoxButtons = new HBox();
		if(vehicle instanceof Car) {
			Car car = (Car)vehicle;
			hboxTest.setAlignment(Pos.CENTER);
			hboxTest.setSpacing(20);
			hboxTest.setPadding(new Insets(10, 11, 12, 13));
			vboxTest.setSpacing(20);
			vboxTest.setPadding(new Insets(10, 11, 12, 13));
			hBoxButtons.setSpacing(10);
			hBoxButtons.setAlignment(Pos.BOTTOM_CENTER);

			// created images box for each car
			FileInputStream inputStream = new FileInputStream("images/" + car.getImageName() + "");
			Image image = new Image(inputStream);
			ImageView imageView = new ImageView(image);
			imageView.setX(20);
			imageView.setY(30);
			imageView.setFitHeight(250);
			imageView.setFitWidth(350);

			Group root = new Group(imageView);
			hboxTest.getChildren().add(root);

			Label vehicleType = new Label("Vehicle type: " + DetermineType(car.getID()));
			Label numberOfSeats = new Label("Number of seats: " + car.getNumOfSeats());
			Label price = new Label("Price per day: " + car.getRentalRates());
			Label status = new Label("Status: " + car.getStatus());

			Button details = new Button("Details");
			Button bookNow = new Button("Book Now");
			bookNow.setOnAction(e->{
				rentVehicle(car).show();
			});
			details.setOnAction(e->{
				try {
					vehicleDetail(car,primaryStage).show();
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
			});

			hBoxButtons.getChildren().addAll(details, bookNow);
			vboxTest.getChildren().addAll(vehicleType, numberOfSeats, price, status, hBoxButtons);

			hboxTest.getChildren().add(vboxTest);
			return hboxTest;
		}else {
			Van van = (Van)vehicle;
			hboxTest.setAlignment(Pos.CENTER);
			hboxTest.setSpacing(20);
			hboxTest.setPadding(new Insets(10, 11, 12, 13));
			vboxTest.setSpacing(20);
			vboxTest.setPadding(new Insets(10, 11, 12, 13));
			hBoxButtons.setSpacing(10);
			hBoxButtons.setAlignment(Pos.BOTTOM_CENTER);

			// created images box for each car
			FileInputStream inputStream = new FileInputStream("images/" + van.getImageName() + "");
			Image image = new Image(inputStream);
			ImageView imageView = new ImageView(image);
			imageView.setX(20);
			imageView.setY(30);
			imageView.setFitHeight(250);
			imageView.setFitWidth(350);

			Group root = new Group(imageView);
			hboxTest.getChildren().add(root);

			Label vehicleType = new Label("Vehicle type: " + DetermineType(van.getID()));
			Label numberOfSeats = new Label("Number of seats: " + van.getNumOfSeats());
			Label price = new Label("Price per day: " + van.getRentalrates());
			Label status = new Label("Status: " + van.getStatus());

			Button details = new Button("Details");
			Button bookNow = new Button("Book Now");
			bookNow.setOnAction(e->{
				rentVehicle(van).show();;
			});
			details.setOnAction(e->{
				try {
					vehicleDetail(van,primaryStage).show();
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
			});

			hBoxButtons.getChildren().addAll(details, bookNow);
			vboxTest.getChildren().addAll(vehicleType, numberOfSeats, price, status, hBoxButtons);

			hboxTest.getChildren().add(vboxTest);
			return hboxTest;
		}
		
	}

	public static String DetermineType(String vehicleID) {
		String str1 = vehicleID.substring(0, 1).toLowerCase();
		String strCar = "Car";
		String strVan = "Van";
		if (str1.equals("c")) {
			return strCar;
		} else {
			return strVan;
		}
	}

	// stage show rent a vehicle 
	public static Stage rentVehicle(Vehicle vehicle) {
		Stage dialogBox = new Stage();
		GridPane gridPane = new GridPane();
		gridPane.setHgap(10);
		gridPane.setVgap(20);
		gridPane.setPadding(new Insets(30, 10, 10, 13));
		gridPane.setAlignment(Pos.CENTER);
		
		Label vehicleId = new Label("Vehicle ID:");
		Label customerId = new Label("Customer ID:");
		Label rentDay = new Label("Select a rent day:");
		Label estimatedReturnDay = new Label("Estimated return day:");
		
		Button okButton = new Button("   Ok   ");
		Button cancelButton = new Button(" Cancel ");
		cancelButton.setOnAction(e->{
			dialogBox.close();
		});
			
		TextField inputCustomerId = new TextField();
		DatePicker rentDate = new DatePicker();
		DatePicker estimatedReturnDate = new DatePicker();
		rentDate.setValue(LocalDate.now());
		rentDate.setShowWeekNumbers(true);
		estimatedReturnDate.setShowWeekNumbers(true);
		final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
			@Override
			public DateCell call(final DatePicker datePicker) {
				return new DateCell() {
					@Override
					public void updateItem(LocalDate item, boolean empty) {
						super.updateItem(item, empty);

						if (item.isBefore(rentDate.getValue().plusDays(1))) {
							setDisable(true);
							setStyle("-fx-background-color: #ffc0cb;");
						}
					}
				};
			}
		};
		estimatedReturnDate.setDayCellFactory(dayCellFactory);
		estimatedReturnDate.setValue(rentDate.getValue().plusDays(1));
		
		gridPane.add(vehicleId, 0, 0);
		gridPane.add(customerId, 0, 1);
		gridPane.add(rentDay, 0, 2);
		gridPane.add(estimatedReturnDay, 0, 3);
		gridPane.add(inputCustomerId, 1, 1);
		gridPane.add(rentDate, 1, 2);
		gridPane.add(estimatedReturnDate, 1, 3);
		gridPane.add(okButton, 1, 4);
		gridPane.add(cancelButton, 0, 4);
		Scene scene = new Scene(gridPane, 400, 250);
		dialogBox.setScene(scene);
		
		if(vehicle instanceof Car) {
			Car car = (Car)vehicle;
			Label actualVehicleId = new Label(car.getID());
			okButton.setOnAction(e->{
				DateTime rentDateTime = new DateTime(rentDate.getValue().toString(),1);
				DateTime estimatedReturnDateTime = new DateTime(estimatedReturnDate.getValue().toString(),1);
				int numOfRentday = DateTime.diffDays(estimatedReturnDateTime,rentDateTime);
				try {
					
					car.rent(inputCustomerId.getText(), rentDateTime, numOfRentday);
					alert.showInformation();
				} catch (Exception re) {
					alert.showWarning("Attention", re);
				}
				dialogBox.close();
			});

			gridPane.add(actualVehicleId, 1, 0);
			dialogBox.setTitle("Dialog of rent a var");
			return dialogBox;
			
		}else {
			Van van = (Van)vehicle;
			Label actualVehicleId = new Label(van.getID());
			okButton.setOnAction(e->{
				DateTime rentDateTime = new DateTime(rentDate.getValue().toString(),1);
				DateTime estimatedReturnDateTime = new DateTime(estimatedReturnDate.getValue().toString(),1);
				int numOfRentday = DateTime.diffDays(estimatedReturnDateTime,rentDateTime);
				try {
					van.rent(inputCustomerId.getText(), rentDateTime, numOfRentday);
				} catch (Exception re) {
					alert.showWarning("Attention", re);
				}
				dialogBox.close();
			});
			gridPane.add(actualVehicleId, 1, 0);
			dialogBox.setTitle("Dialog of rent a van");
			return dialogBox;
		}
	}
	
	// a stage show detail vehicle
	public static Stage vehicleDetail(Vehicle vehicle,Stage primaryStage) throws FileNotFoundException {
		
		Stage stage = new Stage();
		VBox mainVBox = new VBox();
		HBox hBox = new HBox();
		VBox descriptionBox = new VBox();
		VBox buttonBox2 = new VBox();
		HBox buttonBox1 = new HBox();
		
		mainVBox.setSpacing(20);
		mainVBox.setAlignment(Pos.TOP_CENTER);
		buttonBox2.setSpacing(20);
		buttonBox2.setPadding(new Insets(40, 20, 20, 20));
		buttonBox2.setAlignment(Pos.CENTER);
		buttonBox1.setSpacing(20);
		buttonBox1.setPadding(new Insets(40, 20, 20, 20));
		buttonBox1.setAlignment(Pos.BOTTOM_CENTER);
		
		hBox.setSpacing(10);
		hBox.setPadding(new Insets(20, 20, 20, 20));
		ListView detail = new ListView();
		Button rent = new Button("Rent Vehicle");
		if(vehicle instanceof Car) {
			Car car = (Car)vehicle;
			FileInputStream inputStream = new FileInputStream("images/" + car.getImageName() + "");
			Image image = new Image(inputStream);
			ImageView imageView = new ImageView(image);
			imageView.setX(20);
			imageView.setY(30);
			imageView.setFitHeight(350);
			imageView.setFitWidth(450);
					
			detail.setPrefHeight(300);
			detail.setPrefWidth(450);
			Group root = new Group(imageView);
			descriptionBox.getChildren().add(root);
			detail.getItems().add(car.getDetails());
			rent.setOnAction(e->{
				rentVehicle(car).show();;
			});
			Button returnVehicle = new Button("return Vehicle");
			Button performMaintenance = new Button("Perform Maintenance");
			Button completeMaintenance = new Button("Complete Maintenance");
			Button goBack = new Button("Go back");
			goBack.setOnAction(e->{
				stage.close();
			});
			
			returnVehicle.setOnAction(e ->{
				returnTimePickers(car).show();;
			});
					
			performMaintenance.setOnAction(e ->{
				try {
					car.performMaintenance();
					alert.showInformation();
				}catch(MaintenanceException me) {
					alert.showWarning("Attension", me);;
				}
			});
			
			completeMaintenance.setOnAction(e->{
				cmTimePickers(car).show();
			});
			
			buttonBox2.getChildren().addAll(detail, performMaintenance, completeMaintenance, goBack);
			buttonBox1.getChildren().addAll(rent, returnVehicle);
			descriptionBox.getChildren().add(buttonBox1);
			hBox.getChildren().addAll(descriptionBox, buttonBox2);
			functionUnitBox(primaryStage).setAlignment(Pos.TOP_CENTER);
			mainVBox.getChildren().addAll(functionUnitBox(primaryStage),hBox);
			Scene scene = new Scene(mainVBox, 950, 570);
			stage.setScene(scene);
			stage.setTitle("Detail information for a vehicle");
			return stage;
			
		}else {
			Van van = (Van)vehicle;
			FileInputStream inputStream = new FileInputStream("images/" + van.getImageName() + "");
			Image image = new Image(inputStream);
			ImageView imageView = new ImageView(image);
			imageView.setX(20);
			imageView.setY(30);
			imageView.setFitHeight(350);
			imageView.setFitWidth(450);
		
			detail.setPrefHeight(250);
			detail.setPrefWidth(450);
			
			Group root = new Group(imageView);
			descriptionBox.getChildren().add(root);
			detail.getItems().add(van.getDetails());
			rent.setOnAction(e->{
				rentVehicle(van).show();;
			});
			Button returnVehicle = new Button("return Vehicle");
			Button performMaintenance = new Button("Perform Maintenance");
			Button completeMaintenance = new Button("Complete Maintenance");
			Button goBack = new Button("Go back");
			goBack.setOnAction(e->{
				stage.close();
			});
			
			returnVehicle.setOnAction(e ->{
				
				returnTimePickers(vehicle).show();;
			});
			
			performMaintenance.setOnAction(e ->{
				try {
					van.performMaintenance();
					alert.showInformation();
				}catch(MaintenanceException me) {
					alert.showWarning("Attension", me);;
				}
			});
			
			completeMaintenance.setOnAction(e->{
				cmTimePickers(vehicle).show();
			});
			
			buttonBox2.getChildren().addAll(detail, performMaintenance, completeMaintenance, goBack);
			buttonBox1.getChildren().addAll(rent, returnVehicle);
			descriptionBox.getChildren().add(buttonBox1);
			hBox.getChildren().addAll(descriptionBox, buttonBox2);
			functionUnitBox(primaryStage).setAlignment(Pos.TOP_CENTER);
			mainVBox.getChildren().addAll(functionUnitBox(primaryStage),hBox);
			Scene scene = new Scene(mainVBox, 950, 570);
			stage.setScene(scene);
			stage.setTitle("Detail information for a vehicle");
			return stage;
		}
		
	}
	
	// time picker for return vehicles
	public static Stage returnTimePickers(Vehicle vehicle) {
		Stage stage = new Stage();
		DatePicker returnDate = new DatePicker();
		returnDate.setId("pickDate");
	
		VBox vBox = new VBox();
		vBox.setPadding(new Insets(20, 20, 20, 20));
		vBox.setSpacing(10);
		vBox.setAlignment(Pos.CENTER);
		Button returnVehicleButton = new Button("Return vehicle");
		
		returnVehicleButton.setOnAction(e->{
			DateTime returnDateTime =new DateTime(returnDate.getValue().toString() ,1); 
			try {
				vehicle.returnVehicle(returnDateTime);
				alert.showInformation();
			} catch (Exception re) {
				alert.showWarning("Attention", re);
			}
			stage.close();
		});

		returnDate.setPromptText("Select date -->");
		returnDate.setShowWeekNumbers(true);
		returnDate.setValue(LocalDate.now());
		vBox.getChildren().addAll(returnDate, returnVehicleButton);
		
		Scene scene = new Scene(vBox, 280, 150);
		stage.setScene(scene);
		stage.setTitle("Return vehicle dialog");	
		return stage;
	}
	
	// time picker all which need to get time
	public static Stage cmTimePickers(Vehicle vehicle) {
		Stage stage = new Stage();
		DatePicker completeMaintenanceDate = new DatePicker();
		completeMaintenanceDate.setId("pickDate");
	
		VBox vBox = new VBox();
		vBox.setPadding(new Insets(20, 20, 20, 20));
		vBox.setSpacing(10);
		vBox.setAlignment(Pos.CENTER);
		
		Button returnVehicleButton = new Button("Complete maintenance");
		returnVehicleButton.setOnAction(e->{
			DateTime cmdDateTime = new DateTime(completeMaintenanceDate.getValue().toString(), 1);
			try {
				vehicle.completeMaintenance(cmdDateTime);
				alert.showInformation();
			} catch (Exception cme) {
				alert.showWarning("Attention", cme);
			}		
			stage.close();
		});

		completeMaintenanceDate.setPromptText("Select date -->");
		completeMaintenanceDate.setShowWeekNumbers(true);
		completeMaintenanceDate.setValue(LocalDate.now());
		vBox.getChildren().addAll(completeMaintenanceDate, returnVehicleButton);
		
		Scene scene = new Scene(vBox, 280, 150);
		stage.setScene(scene);
		stage.setTitle("Complete maintenance dialog");	
		return stage;
	}
	
	// include all the functional parts
	public static VBox functionUnitBox(Stage primaryStage) {
		VBox functionalUnit = new VBox();	
		functionalUnit.setSpacing(10);
		functionalUnit.setAlignment(Pos.TOP_CENTER);
		
		MenuBar menuBar = new MenuBar();
		Menu menu = new Menu("Menu");
		menuBar.getMenus().add(menu);

		// add other menu items in menu bar
		MenuItem importItem = new MenuItem("Import");
		MenuItem exportItem = new MenuItem("Export");
		exportItem.setOnAction( new ExportFileHandle("export_data.txt"));
		
		importItem.setOnAction(new ImportFileHandle("?"));
				

		menu.getItems().add(importItem);
		menu.getItems().add(exportItem);

		Menu subMenu = new Menu("Add Vehicle");

		MenuItem CarSubItem = new MenuItem("Car");
		MenuItem VanSubItem = new MenuItem("Van");
		menu.getItems().add(subMenu);
		subMenu.getItems().add(CarSubItem);
		subMenu.getItems().add(VanSubItem);

		CarSubItem.setOnAction(e -> {
			AddVeichle temp = new AddVeichle();
			temp.addCar();
		});

		VanSubItem.setOnAction(e -> {
			AddVeichle addVan = new AddVeichle();
			addVan.addVan();
		});

		// add exit menu item and exit action
		MenuItem exitItem = new MenuItem("Exit");
		menu.getItems().add(exitItem);
		exitItem.setOnAction(e -> {
			AlertDialog temp = new AlertDialog();
			temp.showConfirmation("Exit System?");
		});
		
		functionalUnit.getChildren().add(menuBar);
		functionalUnit.getChildren().add(filterFunction(primaryStage));
		
		return functionalUnit;
	}
	
	// include all comboBox function
	public static HBox filterFunction(Stage primaryStage) {
		HBox hBox = new HBox();
		hBox.setSpacing(10);
		hBox.setAlignment(Pos.TOP_RIGHT);
		hBox.setPadding(new Insets(10, 10, 10, 10));
		
		ComboBox numOfSeats = new ComboBox();
		ComboBox type = new ComboBox();
		ComboBox status = new ComboBox();
		ComboBox make = new ComboBox();
		Button search = new Button("Search");
		Button reset = new Button("Reset");
		
		numOfSeats.getItems().add("4");
		numOfSeats.getItems().add("7");
		numOfSeats.getItems().add("15");
		
		numOfSeats.setPromptText("NumberOfSeats");
		type.getItems().add("Car");
		type.getItems().add("Van");
		type.setPromptText("Type");

		status.getItems().add("Available");
		status.getItems().add("Rented");
		status.getItems().add("Maintenance");
		status.setPromptText("Status");

		make.getItems().add("Toyota");
		make.getItems().add("Audi");
		make.setPromptText("Make");
		
		
		hBox.getChildren().addAll(numOfSeats, type, status, make, search, reset);
		search.setOnAction(new SearchController(primaryStage));
		reset.setOnAction(e -> {
			numOfSeats.setValue(null);
			type.setValue(null);
			status.setValue(null);
			make.setValue(null);
		});
		
		return hBox;
	}
}
