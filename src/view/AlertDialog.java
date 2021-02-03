package view;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;

public class AlertDialog {

	// an alert dialog with only an OK button
	public static void showMessage(String message) {
		Alert alert = new Alert(AlertType.INFORMATION);
		
		alert.setTitle("Information Dialog");
		alert.setHeaderText("Info");
		alert.setContentText(message);
		
		alert.showAndWait();
	}
	
	// an alert dialog with confirmation (two buttons) 
	public static void showConfirmation (String message) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation Dialog");
		alert.setHeaderText("Are you sure you want to ");
		alert.setContentText(message);
		
		Optional<ButtonType> result = alert.showAndWait();
		if(result.get() == ButtonType.OK) {
			System.exit(0);
		} else {
			alert.close();
		}
	}
	
	public static void showWarning (String message, Exception e) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Wrong information");
		alert.setHeaderText(message);
		alert.setContentText(e.getMessage());
		alert.showAndWait();
	}
	
	public static void showInformation() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information Dialog");
		alert.setHeaderText(null);
		alert.setContentText("Your operation has been successful!");
		alert.showAndWait();
	}
}
