package controller;

import java.awt.Desktop.Action;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.print.StreamPrintService;

import database.DatabaseHandle;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Car;
import model.RentalRecord;
import model.Status;
import model.Van;
import model.Vehicle;
import util.DateTime;

public class ImportFileHandle implements EventHandler<ActionEvent> {

	private Stage primaryStage;
	private String fileName;

	public ImportFileHandle(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public void handle(ActionEvent Event) {

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select a file from folder");
		File selectedFile = fileChooser.showOpenDialog(primaryStage);
		DatabaseHandle databaseHandle = DatabaseHandle.getDataBaseHandle();
		databaseHandle.deleteAllTables();
		databaseHandle.initAllTables();
		try {
			writeFromFile(selectedFile.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void writeFromFile(String absolutePath) throws IOException {
		FileReader fileReader;
		DatabaseHandle databaseHandle = DatabaseHandle.getDataBaseHandle();
		try {
			fileReader = new FileReader(absolutePath);

			BufferedReader input = new BufferedReader(fileReader);
			String next = input.readLine();
			List<RentalRecord> rentalRecords = new ArrayList<RentalRecord>();
			List<Car> cars = new ArrayList<Car>();
			List<Van> vans = new ArrayList<Van>();
			String lastVehicleId = "";
			while (next != null) {
				String[] strs = next.split(":");
				// for recorders
				if (strs.length == 6) {
					// this vehicle has been rented but did not return
					if (strs[3].equals("none")) {
						rentalRecords.add(new RentalRecord(strs[0].substring(lastVehicleId.length(), strs[0].length()),
								new DateTime(strs[1], 0), new DateTime(strs[2], 0), null, 0, 0, strs[0]));
						// this vehicle doesn't have a penalty fee
					} else if (strs[5].equals("none")) {
						rentalRecords.add(new RentalRecord(strs[0].substring(lastVehicleId.length(), strs[0].length()),
								new DateTime(strs[1], 0), new DateTime(strs[2], 0), new DateTime(strs[3], 0),
								Double.valueOf(strs[4]), 0, strs[0]));
					} else {
						// this vehicle has been returned
						rentalRecords.add(new RentalRecord(strs[0].substring(lastVehicleId.length(), strs[0].length()),
								new DateTime(strs[1], 0), new DateTime(strs[2], 0), new DateTime(strs[3], 0),
								Double.valueOf(strs[4]), Double.valueOf(strs[5]), strs[0]));
					}
					// for can and van
				} else if (strs.length == 7) {

					lastVehicleId = strs[0];
					cars.add(new Car(strs[0], Integer.valueOf(strs[1]), strs[2], strs[3], Integer.valueOf(strs[4]),
							Status.valueOf(strs[5]), strs[6]));

				} else {
					lastVehicleId = strs[0];
					vans.add(new Van(strs[0], Integer.valueOf(strs[1]), strs[2], strs[3], Integer.valueOf(strs[4]),
							Status.valueOf(strs[5]), new DateTime(strs[6], 0), strs[7]));

				}

				next = input.readLine();
			}

			input.close();
			for (Van van : vans) {
				databaseHandle.addVanToDataBase(van);
			}
			for (Car car : cars) {
				databaseHandle.addCarToDataBase(car);
			}

		} catch (FileNotFoundException | UnsupportedEncodingException e) {

			e.printStackTrace();
		}

	}

}
