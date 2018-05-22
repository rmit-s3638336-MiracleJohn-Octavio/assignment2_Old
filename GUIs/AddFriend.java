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
import Network.Adult;
import Network.Child;
import Network.User;
import Network.YoungChild;
import System.MyGlobals;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import javafx.geometry.Pos;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

class AddFriend implements EventHandler<ActionEvent>, GUIInterface {
	
	/*+-----------------+*
	 *| Class Variables |
	 *+-----------------+*/
	
	private MyGlobals glob = new MyGlobals();
	
	private Stage primaryStage;
	
	GridPane addFriendPane = myUI.createGridPane();
	Scene addFriendScene = new Scene(addFriendPane, glob.WINDOW_W, glob.WINDOW_H);
	
	Text addFriendHeader = myUI.createHeader("Add a Friend");
	Label addFriendLabel = myUI.createLabel("Choose a friend: ");
	Label addFriendType = myUI.createLabel("Connection Type: ");
	Label addFriendNotif = new Label("");
	ChoiceBox<String> addFriendChoices = new ChoiceBox<String>();
	ListView<String> friendsList = new ListView<String>();
	
	HBox hbAddFriendBtn = new HBox(10);
	Button addFriendBtn = myUI.createButton("Add Network");
	Button backBtn = myUI.createButton("Back");
	
	ArrayList<String> possibleFriends = new ArrayList<String>();
	
	public AddFriend(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}
	
	public void getPossibleFriends() {		
		if ( MiniNet.currentUser instanceof Adult ) 
			getPossibleAdultFriends();
		if ( MiniNet.currentUser instanceof Child )
			getPossibleChildFriends();
		if ( MiniNet.currentUser instanceof YoungChild )
			getPossibleYoungChildFriends();
	}
	
	public void getPossibleAdultFriends() {
		for ( String possibleFriend : MiniNet.people.getAllProfiles().keySet() ) {
			// if already friend
			if ( MiniNet.currentUser.getFriends().contains(MiniNet.people.getProfile(possibleFriend)) ) 
				continue;
			// if this user
			else if ( MiniNet.currentUser.getUsername().equals(possibleFriend) ) 
				continue;
			// add other adult users
			if ( MiniNet.people.getProfile(possibleFriend) instanceof Adult )
				possibleFriends.add(possibleFriend);
			// add child users that are age 16
			if ( MiniNet.people.getProfile(possibleFriend).getAge() == 16 && MiniNet.currentUser.getAge() == 17)
				possibleFriends.add(possibleFriend);
		}
	}
	
	public void getPossibleChildFriends() {
		for (String possibleFriend : MiniNet.people.getAllProfiles().keySet()) {
			// if already friend
			if ( MiniNet.currentUser.getFriends().contains(MiniNet.people.getProfile(possibleFriend)) ) 
				continue;
			// if this user
			if ( MiniNet.currentUser.getUsername().equals(possibleFriend) ) 
				continue;
			// add adult users that are age 17
			if (MiniNet.people.getProfile(possibleFriend).getAge() == 17)
				possibleFriends.add(possibleFriend);
			
			// add other child users
			if (MiniNet.people.getProfile(possibleFriend) instanceof Child) {
				Child user = ((Child) MiniNet.currentUser);
				Child friend = ((Child) MiniNet.people.getProfile(possibleFriend));
				
				// child users must be from different families
				// if from same family pass that user
				if ( user.getParentOne().getUsername().equals(friend.getParentOne().getUsername()) ||
					 user.getParentTwo().getUsername().equals(friend.getParentTwo().getUsername()) ||
					 user.getParentOne().getUsername().equals(friend.getParentTwo().getUsername()) ||
					 user.getParentTwo().getUsername().equals(friend.getParentOne().getUsername()) ) {
					continue;
				}
				else {
					if ( Math.abs(friend.getAge() - user.getAge()) <= 3 )
						possibleFriends.add(possibleFriend);
				}					
			}
		}
	}
	
	public void getPossibleYoungChildFriends() {
		for (String possibleFriend : MiniNet.people.getAllProfiles().keySet()) {
			// if already friend
			if (MiniNet.currentUser.getFriends().contains(MiniNet.people.getProfile(possibleFriend)))
				continue;
			// if this user
			if (MiniNet.currentUser.getUsername().equals(possibleFriend))
				continue;

			if (MiniNet.people.getProfile(possibleFriend) instanceof YoungChild) {
				YoungChild user = ((YoungChild) MiniNet.currentUser);
				YoungChild friend = ((YoungChild) MiniNet.people.getProfile(possibleFriend));

				// young child possible friends must be from same family
				if (user.getParentOne() == friend.getParentOne() || user.getParentOne() == user.getParentTwo()
						|| user.getParentTwo() == friend.getParentTwo()) {
					possibleFriends.add(possibleFriend);
				}
			}

			if (MiniNet.people.getProfile(possibleFriend) instanceof Child) {
				YoungChild user = ((YoungChild) MiniNet.currentUser); // since the current user is a young child
				Child friend = ((Child) MiniNet.people.getProfile(possibleFriend));

				// young child possible friends must be from same family
				if (user.getParentOne() == friend.getParentOne() || user.getParentOne() == user.getParentTwo()
						|| user.getParentTwo() == friend.getParentTwo()) {
					possibleFriends.add(possibleFriend);
				}
			}
		}
	}
	
	public void initFriendsListView() {
		// sort friends in alphabetical order
		Collections.sort(possibleFriends);
		
		for(String friend : possibleFriends) {
			friendsList.getItems().add(friend);	
		}
	}
	
	public void initScene() {
		
		// add friend type choices
		if ( MiniNet.currentUser instanceof YoungChild )
			addFriendChoices.getItems().add("sibling");
		else 
			addFriendChoices.getItems().add("friend");
		
		// young childs cannot add classmates
		if ( !(MiniNet.currentUser instanceof YoungChild) )
			addFriendChoices.getItems().add("classmate");
		
		// if adult user add additional choice
		if ( (MiniNet.currentUser instanceof Adult) ) 
			addFriendChoices.getItems().add("colleague");
		
		addFriendChoices.setValue(addFriendChoices.getItems().get(0));
		addFriendChoices.setPrefSize(150, 20);
		
		getPossibleFriends();		
		initFriendsListView();
					
		hbAddFriendBtn.setAlignment(Pos.BOTTOM_CENTER); // Pos.BOTTOM_RIGHT
		hbAddFriendBtn.getChildren().add(backBtn);
		hbAddFriendBtn.getChildren().add(addFriendBtn);
				
		addFriendPane.add(addFriendHeader, 0, 0, 2, 1);  
		addFriendPane.add(addFriendLabel, 0, 1);
		addFriendPane.add(addFriendNotif, 0, 2, 2, 1);
		addFriendPane.add(friendsList, 0, 3, 2, 1);
		addFriendPane.add(addFriendType, 0, 4);
		addFriendPane.add(addFriendChoices, 1, 4);
		addFriendPane.add(hbAddFriendBtn, 0, 7, 2, 1);
	}
	
	@Override
	public void handle(ActionEvent e) { 		
		
		initScene();
		
		friendsList.setOnMouseClicked(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent arg0) {
				addFriendNotif.setTextFill(Color.BLACK);
				addFriendNotif.setText("Add " + friendsList.getSelectionModel().getSelectedItem() + " as " +  
									   addFriendChoices.getValue() + "?" );
			}
		});	
		
		// add friend button
		addFriendBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {	
				
				try {
					User friendToAdd = MiniNet.people.getProfile(friendsList.getSelectionModel().getSelectedItem());
					
					MiniNet.currentUser.addFriend(friendToAdd);
					MiniNet.links.addRelationship(MiniNet.currentUser.getUsername(), friendToAdd.getUsername(), addFriendChoices.getValue());
					addFriendNotif.setText("");
					
					friendsList.getItems().remove(friendsList.getSelectionModel().getSelectedItem());
				} catch (NullPointerException np) {
					addFriendNotif.setTextFill(Color.FIREBRICK);
					addFriendNotif.setText("Select a friend first");
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
	
		primaryStage.setTitle("Add a friend to user");
		addFriendScene.getStylesheets().add(getClass().getResource("/app.css").toExternalForm());
		primaryStage.setScene(addFriendScene);
		primaryStage.show();
	}
}