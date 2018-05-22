package GUIs;

/* @author: Adelbert Choi
 * created: May 5, 2018
 * 
 * = SearchUser.java (GUI)
 * 
 */

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;

import Interfaces.GUIInterface;
import System.MyGlobals;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class SearchUser implements EventHandler<ActionEvent>, GUIInterface {

	/*+-----------------+*
	 *| Class Variables |
	 *+-----------------+*/
	
	private MyGlobals glob = new MyGlobals();
	
	private Stage primaryStage;
	
	GridPane searchUserPane 		= myUI.createGridPane();
	Scene searchuserScene 		= new Scene(searchUserPane, glob.WINDOW_W, glob.WINDOW_H);
	
	Text warnings		 		= myUI.warningText("");
	Text searchUserHeader 		= myUI.createHeader("Search A User");
	Label searchUserText 		= myUI.createLabel("Enter username: ");
	TextField searchUserField 	= myUI.createTextField("e.g., John Doe");
	
	HBox hbSceneButtons 			= new HBox(10);
	Button searchUserBtn 		= myUI.createButton("Search User");
	Button backBtn 				= myUI.createButton("Main Menu");
	
	public SearchUser(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	@Override
	public void initScene() {
		hbSceneButtons.setAlignment(Pos.BOTTOM_CENTER); 
		hbSceneButtons.getChildren().add(backBtn);
		hbSceneButtons.getChildren().add(searchUserBtn);
		
		searchUserPane.add(searchUserHeader, 0, 0, 2, 1);
		searchUserPane.add(searchUserText, 0, 1); 
		searchUserPane.add(searchUserField, 1, 1);
		searchUserPane.add(warnings, 0, 2, 2, 1);
		searchUserPane.add(hbSceneButtons, 0, 5, 2, 1);		
	}
	
	public boolean checkEnteredUser(String username) {
		// no username entered condition
		if ( username.equals("") ) {
			warnings.setText("No user entered");
			return false;

		// user does not exists condition
		} else if ( !MiniNet.people.existingUser(username) ) { 
			warnings.setText("No such user");
			return false;
			
		} else {
			return true;
		}
	}

	@Override
	public void handle(ActionEvent e) { 		
		
		// initialise the scene
		initScene(); 
		
		searchUserBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				if ( checkEnteredUser(searchUserField.getText()) ) {
					// if a user is found
					MiniNet.currentUser = MiniNet.people.getProfile(searchUserField.getText()); 
					
					ShowUser showUser = new ShowUser(primaryStage);
					showUser.displayUser();		
				}
		    }
		});
		
		// back to the main menu
		backBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				MainMenu mainMenu = new MainMenu(primaryStage);
				mainMenu.displayMainMenu();
		    }
		});
		searchuserScene.getStylesheets().add(getClass().getResource("/app.css").toExternalForm());
		primaryStage.setScene(searchuserScene);
		primaryStage.setTitle("Search a user");
		primaryStage.show();
	}	
}