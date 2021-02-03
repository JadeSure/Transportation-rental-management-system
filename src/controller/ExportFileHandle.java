package controller;

import java.awt.Desktop.Action;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

import database.DatabaseHandle;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import model.Car;
import model.RentalRecord;
import model.Vehicle;

public class ExportFileHandle implements EventHandler<ActionEvent>{

	private Stage primaryStage;
	private String fileName;
	

	public ExportFileHandle (String fileName) {
		this.fileName = "export_data.txt";
	}
	
	
	@Override
	public void handle(ActionEvent Event) {

		DirectoryChooser dirChooser = new DirectoryChooser();
		dirChooser.setTitle("Select a file from folder");
		File selectedDir = dirChooser.showDialog(primaryStage);
		writeToFile(selectedDir.getAbsolutePath(), fileName);
		
	}

	private void writeToFile(String absolutePath, String fileName) {
		PrintWriter writer;
		try {
			writer = new PrintWriter(absolutePath + "/" + fileName, "UTF-8");
			
			DatabaseHandle databaseHandle = DatabaseHandle.getDataBaseHandle();
			List<Vehicle> vehicles = databaseHandle.getAllVehicles();
			List<RentalRecord> rentalRecords = databaseHandle.getAllRecords();
			for(Vehicle vehicle : vehicles) {
				writer.println(vehicle.toString());
				for(RentalRecord rentalRecord : rentalRecords) {
					if(rentalRecord.getVehicleId().equals(vehicle.getId())) {
						writer.println(rentalRecord.toString());
					}
				}
			}

			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			
			e.printStackTrace();
		}

	}

}
