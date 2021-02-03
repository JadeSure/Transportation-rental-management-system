package view;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ReminderDialogBox  {

	private Stage reminderStage; 
	private String reminderInfo;
	private String reminderTitle;
	
	// constructor for reminder dialog box
	// this parameter can be used to customized reminder box info
	public ReminderDialogBox(String reminderTitle, String reminderInfo) {
		this.reminderInfo = reminderInfo;
		this.reminderTitle = reminderTitle;
		
		
		
		reminderStage = new Stage();
		// a dialog box for exit the system
		VBox vBox = new VBox();
		Label alertInfo = new Label(reminderInfo);
		vBox.setSpacing(10);
		vBox.setAlignment(Pos.CENTER);
	
		HBox hBox = new HBox();
		hBox.setSpacing(10);
		hBox.setAlignment(Pos.CENTER);
		
		Button yesButton = new Button("Yes");
		Button noButton = new Button("No");
		
		vBox.getChildren().add(alertInfo);
		hBox.getChildren().add(yesButton);
		hBox.getChildren().add(noButton);
		vBox.getChildren().add(hBox);
	
		yesButton.setOnAction(e ->{
			System.exit(0);
		});
		
		noButton.setOnAction(e ->{
			reminderStage.close();
		});
		
		Scene scene = new Scene(vBox,200,100);
		reminderStage.setTitle(reminderTitle);
		reminderStage.setScene(scene);
	
	}
	
	
	public void show() {
		reminderStage.show();
	}
	
}
