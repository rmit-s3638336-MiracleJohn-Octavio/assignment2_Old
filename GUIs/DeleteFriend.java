package GUIs;

/* @author: Adelbert Choi
 * created: May 5, 2018
 * 
 * AddFriend.java (GUI)
 * 
 */

import java.util.ArrayList;
import java.util.Collections;

import Interfaces.GUIInterface;
import Network.Relationship;
import Network.User;
import System.MyGlobals;
import System.NoParentException;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import javafx.geometry.Pos;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

class DeleteFriend implements EventHandler<ActionEvent>, GUIInterface {
	
	/*+-----------------+*
	 *| Class Variables |
	 *+-----------------+*/
	
	private MyGlobals glob = new MyGlobals();
	
	private Stage primaryStage;
	
	GridPane deleteFriendPane = myUI.createGridPane();
	Scene deleteFriendScene = new Scene(deleteFriendPane, glob.WINDOW_W, glob.WINDOW_H);	
	
	Text deleteFriendHeader = myUI.createHeader("Delete a Friend");
	Label deleteFriendLabel = myUI.createLabel("Choose a friend to delete: ", DETAILS_LABEL_WIDTH);
	Label deleteFriendNotif = new Label("");
	ChoiceBox<String> deleteFriendChoices = new ChoiceBox<String>();
	ListView<String> friendsListView = new ListView<String>();
	
	HBox hbDeleteFriendBtn = new HBox(10);
	Button deleteFriendBtn = myUI.createButton("Delete Network");
	Button backBtn = myUI.createButton("Back");
	
	ArrayList<Relationship> friends = new ArrayList<Relationship>(); 
	Alert deleteFriendAlert;
	
	public DeleteFriend(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}
	
	public void getFriends() {
		for ( Relationship link  : MiniNet.links.getRelationships() ) {
			if ( link.getUsernameOne().equals(MiniNet.currentUser.getUsername()) )
				friends.add(link);
		}
	}
	
	public void initFriendsListView() {
		for(Relationship friend : friends) {
			if (friend.getType() != "parent")
				friendsListView.getItems().add(friend.getUsernameTwo());	
		}
		
		Collections.sort(friendsListView.getItems());
	}
	
	public void initScene() {
		getFriends();		
		initFriendsListView();
					
		hbDeleteFriendBtn.setAlignment(Pos.BOTTOM_CENTER); // Pos.BOTTOM_RIGHT
		hbDeleteFriendBtn.getChildren().add(backBtn);
		hbDeleteFriendBtn.getChildren().add(deleteFriendBtn);
				
		deleteFriendPane.add(deleteFriendHeader, 0, 0, 2, 1);  
		deleteFriendPane.add(deleteFriendLabel, 0, 1);
		deleteFriendPane.add(deleteFriendNotif, 0, 2, 2, 1);
		deleteFriendPane.add(friendsListView, 0, 3, 2, 1);
		deleteFriendPane.add(hbDeleteFriendBtn, 0, 6, 2, 1);
	}
	
	public void deleteFriendConfirm(String friend) {	
		deleteFriendAlert = new Alert(AlertType.CONFIRMATION); 
		deleteFriendAlert.setTitle("Delete friend");
 
		deleteFriendAlert.setHeaderText(null);
		deleteFriendAlert.setContentText("Are you sure you want to delete " + friend + " from the network?");
		deleteFriendAlert.showAndWait();
		// add try catch to get errors when delete a user
	}

	
	@Override
	public void handle(ActionEvent e) { 		
		
		initScene();
		
		friendsListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				deleteFriendNotif.setTextFill(Color.BLACK);
				String[] selection = friendsListView.getSelectionModel().getSelectedItem().split(" ");
				deleteFriendNotif.setText("Delete " + selection[1].replaceAll("\\p{P}", "") + " " + selection[0]);
			}
		});	
		
		// add friend button
		deleteFriendBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {	
				
				try {
					String selection = friendsListView.getSelectionModel().getSelectedItem();
					
					deleteFriendConfirm(selection);
					
					if ( deleteFriendAlert.getResult().getText().equals("OK") ) {
						User friendToDelete = MiniNet.people.getProfile(selection);
						
						MiniNet.currentUser.deleteFriend(friendToDelete);
						// DONT KNOW WHY THIS IS NOT WORKING PROPERLY
						MiniNet.links.deleteRelationships(MiniNet.currentUser.getUsername(), friendToDelete.getUsername());
						MiniNet.links.deleteRelationships(friendToDelete.getUsername(), MiniNet.currentUser.getUsername());

						deleteFriendNotif.setText("");
						friendsListView.getItems().remove(friendsListView.getSelectionModel().getSelectedItem());
					}
				} catch (NullPointerException nu) {
					deleteFriendNotif.setTextFill(Color.FIREBRICK);
					deleteFriendNotif.setText("Select a friend first");
				} catch (NoParentException np) {
					deleteFriendNotif.setTextFill(Color.FIREBRICK);
					deleteFriendNotif.setText(np.getMessage());
				}
			}
		});
		
		// back to user profile
		backBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {					
				ShowUser showUser = new ShowUser(primaryStage);
				showUser.displayUser();
			}
		});

		
		primaryStage.setTitle("Delete a user's friend");
		deleteFriendScene.getStylesheets().add(getClass().getResource("/app.css").toExternalForm());
		primaryStage.setScene(deleteFriendScene);
		primaryStage.show();
	}
}