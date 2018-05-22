package GUIs;

/* @author: Adelbert Choi
 * created: May 5, 2018
 * 
 * = MainMenu.java (GUI)
 * 
 */

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.util.Optional;

import System.MyGlobals;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class MainMenu {
	
	/*+-----------------+*
	 *| Class Variables |
	 *+-----------------+*/
	
	private MyGlobals glob = new MyGlobals();
	
	/*+----------+*
	 *| Controls |
	 *+----------+*/
	
	Text mainHeader = myUI.createHeader("Main Menu");
	Button searchUserBtn = myUI.createButton("Search User");
	Button listAllUsersBtn = myUI.createButton("List All Users");
	Button addUserBtn = myUI.createButton("Add User");
	Button exitBtn = myUI.createButton("Exit App");
	
	GridPane mainPane = myUI.createGridPane();	
	Scene mainScene 	= new Scene(mainPane, glob.WINDOW_W, glob.WINDOW_H);	
	Stage primaryStage;
	
	
	public MainMenu (Stage primaryStage) {
		this.primaryStage = primaryStage;
	}
	
	public void displayMainMenu() {
		mainPane.getStylesheets().add(getClass().getResource("/app.css").toExternalForm());
		mainPane.add(mainHeader, 0, 0, 2, 1);
		mainPane.add(searchUserBtn, 0, 1);
		mainPane.add(listAllUsersBtn, 0, 2);
		mainPane.add(addUserBtn, 0, 3);
		mainPane.add(exitBtn, 0, 4);

		// go to menu to search for a user
		searchUserBtn.setOnAction(new SearchUser(primaryStage));
		
		listAllUsersBtn.setOnAction(new ListAllUsers(primaryStage));
		
		// go to menu for add a user
		addUserBtn.setOnAction(new AddUser(primaryStage));
				
		// when exit button is clicked
		exitBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				if (isExitApplication()) {
					// End the Application
					System.exit(0);
				}
		    }
		});
				
		mainScene.getStylesheets().add(getClass().getResource("/app.css").toExternalForm());
		primaryStage.setScene(mainScene);
		primaryStage.setTitle("Main Menu");
		primaryStage.show();
	}
	
	
	private boolean isExitApplication() {
		boolean exitApplication = false;
		Optional<ButtonType> result = glob.myConfirm("Confirm", 
				"This will end your session!", "Are you sure you want to exit?", AlertType.CONFIRMATION);
		if (result.get() == ButtonType.OK){
			exitApplication = true;
	    	};
	    	
	    	// Return
	    	return exitApplication;
	}
	
}









