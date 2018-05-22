package GUIs;

import java.util.ArrayList;

import Interfaces.GUIInterface;

import Network.Adult;
import Network.Child;
import Network.User;
import Network.YoungChild;
import System.MyGlobals;
import System.NotToBeCoupledException;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import javafx.geometry.Pos;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class AddChildUser implements GUIInterface {
	
	/*+-----------------+*
	 *| Class Variables |
	 *+-----------------+*/
	
	private MyGlobals glob = new MyGlobals();
	
	GridPane addChildUserPane 	= myUI.createGridPane();
	Scene addChildUserScene 		= new Scene(addChildUserPane, glob.WINDOW_W, glob.WINDOW_H);
	Stage primaryStage;
		
	Text addChildUserHeader 		= myUI.createHeader("");
	Text warnings				= myUI.warningText("");
	Label parentOneText			= myUI.createLabel("Choose Parent 1: ");
	Label parentTwoText 			= myUI.createLabel("Choose Parent 2: ");
		
	HBox hbAddChildUserBtn 		= new HBox(10);
	Button createChildUserBtn 	= myUI.createButton("Create User");
	Button backBtn 				= myUI.createButton("Main Menu");	
	
	ChoiceBox<String> choiceOne; 
	ChoiceBox<String> choiceTwo;
	ArrayList<String> choices = new ArrayList<String>();
	ArrayList<String> childDetails = new ArrayList<String>();
	
	public AddChildUser (Stage primaryStage, ArrayList<String> childDetails) {
		this.primaryStage = primaryStage;
		this.childDetails = childDetails;
	}
	
	public void initScene() {
		
		if ( Integer.parseInt(this.childDetails.get(1)) < 3 )
			addChildUserHeader.setText("Create a Young Child User");
		else 
			addChildUserHeader.setText("Create a Child User");
		
		hbAddChildUserBtn.setAlignment(Pos.BOTTOM_CENTER); // Pos.BOTTOM_RIGHT		
		hbAddChildUserBtn.getChildren().add(backBtn);
		hbAddChildUserBtn.getChildren().add(createChildUserBtn);

		for ( String adult : MiniNet.people.getAllProfiles().keySet() ) {
			if ( MiniNet.people.getProfile(adult) instanceof Adult )
				choices.add(adult);
		}
		
		choiceOne = myUI.createChoiceBox(choices, true);
		choiceTwo = myUI.createChoiceBox(choices, true);
		
		addChildUserPane.add(addChildUserHeader, 0, 0, 2, 1);
		addChildUserPane.add(parentOneText, 0, 1); addChildUserPane.add(choiceOne, 1, 1);
		addChildUserPane.add(parentTwoText, 0, 2); addChildUserPane.add(choiceTwo, 1, 2);
		addChildUserPane.add(warnings, 0, 4, 2, 3);
		addChildUserPane.add(hbAddChildUserBtn, 0, 9, 2, 1);
	}
	
	public void validateInput() throws NotToBeCoupledException, Exception {
		Adult userOne = null;
		Adult userTwo = null;
		
		if ( !choiceOne.getValue().isEmpty() )
			userOne = (Adult) MiniNet.people.getProfile(choiceOne.getValue());
		if ( !choiceTwo.getValue().isEmpty() )
			userTwo = (Adult) MiniNet.people.getProfile(choiceTwo.getValue());
		
		if ( choiceOne.getValue().isEmpty() || choiceTwo.getValue().isEmpty() ) {
			throw new Exception("Parents fields must not be empty");
		} else if ( choiceOne.getValue().equals(choiceTwo.getValue()) ) {
			throw new NotToBeCoupledException("Parents need to be different users");
		} else if ( (userOne != null) && (userTwo != null) ) {
			if ( (userOne.getPartner() != userTwo) && (userOne.getPartner() != null))
				throw new NotToBeCoupledException(choiceOne.getValue() + " has a partner. " + "Other parent must be " + userOne.getPartner().getUsername());
			else if ( ( (userTwo.getPartner() != userOne) && (userTwo.getPartner() != null)) )
				throw new NotToBeCoupledException(choiceTwo.getValue() + " has a partner. " + "Other parent must be " + userTwo.getPartner().getUsername());
			else 
				warnings.setText("");
		} else {		
			warnings.setText("");
		}
	}
	
	public String existPartner(String username) {
		if ( ((Adult) MiniNet.people.getProfile(username)).getPartner() != null ) {
			String partnerName = ((Adult) MiniNet.people.getProfile(username)).getPartner().getUsername();
			
			Alert partnerfoundAlert = new Alert(AlertType.INFORMATION); 
			partnerfoundAlert.setTitle("");
	 
			partnerfoundAlert.setHeaderText(username + " has an existing partner");
			partnerfoundAlert.setContentText("Other parent field will be set to " + partnerName);
			partnerfoundAlert.showAndWait();
			
			return partnerName;
		}
		return null;
	}
	
	public void displayAddChildUser() { 		
		
		initScene();
		
		choiceOne.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				try {
					validateInput();
				} catch (Exception exc) {
					warnings.setText(exc.getMessage());
				}
				
				String partner = existPartner(choiceOne.getValue()); 
				if (partner != null) {
					choiceTwo.setValue(partner);
					warnings.setText("");
				}
		    }
		});
		
		choiceTwo.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				try {
					validateInput();
				} catch (Exception exc) {
					warnings.setText(exc.getMessage());
				}
				
				String partner = existPartner(choiceTwo.getValue()); 
				if (partner != null) {
					choiceOne.setValue(partner);
					warnings.setText("");
				}
		    }
		});
		
		createChildUserBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				try {
					validateInput();
					User newChild;
					
					// add young child
					if ( Integer.parseInt(childDetails.get(1)) < 3 ) {
						newChild = new YoungChild(childDetails.get(0),
								Integer.parseInt(childDetails.get(1)), 
								childDetails.get(2),
								childDetails.get(3), 
								childDetails.get(4), 
								childDetails.get(5), 
								((Adult) MiniNet.people.getProfile(choiceOne.getValue())), 								
								((Adult) MiniNet.people.getProfile(choiceTwo.getValue())));						
					} else {
					// add a child
						newChild = new Child(childDetails.get(0),
								Integer.parseInt(childDetails.get(1)), 
								childDetails.get(2),
								childDetails.get(3), 
								childDetails.get(4), 
								childDetails.get(5), 
								((Adult) MiniNet.people.getProfile(choiceOne.getValue())), 								
								((Adult) MiniNet.people.getProfile(choiceTwo.getValue())));
					}
					
					MiniNet.people.addUser(newChild);
					glob.insertUser(newChild);
					MiniNet.currentUser = MiniNet.people.getProfile(newChild.getUsername()) ;
					
					// DONE
					// add implementation here to change connection type of adults if they are already connected
					// if not create a new connection which is just a couple relationship
					if ( MiniNet.links.existingRelationship(choiceOne.getValue(), choiceTwo.getValue()) ) {
						MiniNet.links.changeRelationship(choiceOne.getValue(), choiceTwo.getValue(), "spouse");
					} else {
						MiniNet.links.addRelationship(choiceOne.getValue(), choiceTwo.getValue(), "spouse");
					} 
					
					MiniNet.links.addRelationship(choiceOne.getValue(), newChild.getUsername(), "parent");
					MiniNet.links.addRelationship(choiceTwo.getValue(), newChild.getUsername(), "parent");
					
					ShowUser showUser = new ShowUser(primaryStage);
					showUser.displayUser();
				} catch (NotToBeCoupledException ntbce) {
					warnings.setText(ntbce.getMessage());
				} catch (Exception exc) {
					warnings.setText(exc.getMessage());
				}
		    }
		});
		
		// back to the main menu
		backBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				MainMenu mainMenu = new MainMenu(primaryStage);
				mainMenu.displayMainMenu();
			}
		});
		
		addChildUserScene.getStylesheets().add(getClass().getResource("/app.css").toExternalForm());
		primaryStage.setScene(addChildUserScene);
		primaryStage.setTitle("Add a Child/Young Child User");
		primaryStage.show();
	}
}