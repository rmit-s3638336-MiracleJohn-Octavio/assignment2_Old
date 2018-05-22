package GUIs;

/* @author: Adelbert Choi
 * created: May 5, 2018
 * 
 * AddFriend.java (GUI)
 * 
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import Interfaces.GUIInterface;
import Network.Adult;
import Network.Child;
import Network.User;
import Network.YoungChild;
import System.MyGlobals;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.geometry.Pos;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

class ListAllUsers implements EventHandler<ActionEvent>, GUIInterface {
	
	/*+-----------------+*
	 *| Class Variables |
	 *+-----------------+*/
	
	private MyGlobals glob = new MyGlobals();
	
	private Stage primaryStage;
	
	GridPane selectUserPane		= myUI.createGridPane();
	Scene selectUserScene 		= new Scene(selectUserPane, glob.WINDOW_W, glob.WINDOW_H);
	
	Text warnings		 			= myUI.warningText("");
	Text selectUserHeader		 	= myUI.createHeader("All Users List"); 
	Label chooseUserLabel 			= myUI.createLabel("Select a user: ");
	Label chooseUserNotif 			= new Label("");
	ArrayList<String> userTypeField 	= new ArrayList<String>(Arrays.asList("All", "Adult", "Child", "Young Child"));
	ChoiceBox<String> userTypeChoices = myUI.createChoiceBox(userTypeField , false);
	ArrayList<String> users 			= new ArrayList<String>();
	ListView<String> usersList 		= new ListView<String>();
	
	HBox hbSceneButtons 	= new HBox(10);
	Button showUserBtn = myUI.createButton("Show User");
	Button backBtn = myUI.createButton("Main Menu");
		
	public ListAllUsers(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}
	
	@Override
	public void initScene() {
		getUsersSubset("All");		
		initUsersListView();
					
		hbSceneButtons.setAlignment(Pos.BOTTOM_CENTER); // Pos.BOTTOM_RIGHT
		hbSceneButtons.getChildren().add(backBtn);
		hbSceneButtons.getChildren().add(showUserBtn);
				
		selectUserPane.add(selectUserHeader, 0, 0, 2, 1);  
		selectUserPane.add(chooseUserLabel, 0, 1);
		selectUserPane.add(chooseUserNotif, 0, 2, 2, 1);
		selectUserPane.add(usersList, 0, 3, 2, 1);
		selectUserPane.add(userTypeChoices, 0, 4);
		selectUserPane.add(hbSceneButtons, 0, 7, 2, 1);
	}
	
	public void getUsersSubset(String userType) {
		users.clear();
		
		for ( String user : MiniNet.people.getAllProfiles().keySet() ) {
			if (userType.equals("All"))
				users.add(user);
			else if (userType.equals("Adult") && MiniNet.people.getProfile(user) instanceof Adult)
				users.add(user);
			else if (userType.equals("Child") && MiniNet.people.getProfile(user) instanceof Child)
				users.add(user);
			else if (userType.equals("Young Child") && MiniNet.people.getProfile(user) instanceof YoungChild)
				users.add(user);
		}
		
		Collections.sort(users);
	}
	
	public void initUsersListView() {
		usersList.getItems().clear();
		for(String user : users) {
			usersList.getItems().add(user);
		}		
	}
	
	
	@Override
	public void handle(ActionEvent e) { 		
		
		initScene();
		
		usersList.setOnMouseClicked(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent arg0) {
				chooseUserNotif.setTextFill(Color.BLACK);
				chooseUserNotif.setText("Show " + usersList.getSelectionModel().getSelectedItem() + "'s user profile?. ");
			}
		});	
		
		// select a user button
		showUserBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {	
				try {
					User userToShow = MiniNet.people.getProfile(usersList.getSelectionModel().getSelectedItem());
					MiniNet.currentUser = userToShow;
					
					chooseUserNotif.setText("");
					ShowUser showUser = new ShowUser(primaryStage);
					showUser.displayUser();
				} catch (NullPointerException np) {
					chooseUserNotif.setTextFill(Color.FIREBRICK);
					chooseUserNotif.setText("Select a user.");
				}
			}
		});
		
		userTypeChoices.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {

				usersList.refresh();
				String userType = userTypeChoices.getSelectionModel().getSelectedItem();
				
				if ( userType.equals("All")) {
					getUsersSubset("All");
				} else if (userType.equals("Adult")) {
					getUsersSubset("Adult");
				} else if (userType.equals("Child")) {
					getUsersSubset("Child");
				} else {
					getUsersSubset("Young Child");
				}
				
				initUsersListView();
			}
		});
		
		// back to user profile
		backBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				MainMenu mainMenu = new MainMenu(primaryStage);
				mainMenu.displayMainMenu();
		    }
		});
	
		primaryStage.setTitle("All users");
		selectUserScene.getStylesheets().add(getClass().getResource("/app.css").toExternalForm());
		primaryStage.setScene(selectUserScene);
		primaryStage.show();
	}
}