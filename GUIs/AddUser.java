package GUIs;

import Interfaces.GUIInterface;

import java.util.ArrayList;
import java.util.Arrays;

import Network.Adult;
import Network.User;
import System.MyGlobals;
import System.NoSuchAgeException;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import javafx.geometry.Pos;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

class AddUser implements EventHandler<ActionEvent>, GUIInterface {
	
	/*+-----------------+*
	 *| Class Variables |
	 *+-----------------+*/
	
	private MyGlobals glob = new MyGlobals();
		
	GridPane addUserPane = myUI.createGridPane();
	Scene addUserScene = new Scene(addUserPane, glob.WINDOW_W, glob.WINDOW_H);
	Stage primaryStage;
	
	Text warnings = myUI.warningText("");
	Text addUserHeader = myUI.createHeader("Create a User");
	Label addUserNotes = myUI.createLabel("* are required fields\n");
	Label usernameText = myUI.createLabel("Enter Username *: ");
	Label ageText = myUI.createLabel("Enter Age: ");
	Label statusText = myUI.createLabel("Enter Status: ");
	Label imageText = myUI.createLabel("Add Image: ");
	Label genderText = myUI.createLabel("Add Gender: ");
	Label stateText = myUI.createLabel("Add State: ");
	TextField usernameField 	= myUI.createTextField("e.g., John Doe");
	TextField ageField = myUI.createTextField("e.g., 22");
	TextField statusField = myUI.createTextField("e.g., Working in RMIT");
	TextField imageField = myUI.createTextField("e.g., JohnDoe.png");
	
	ArrayList<String> genderChoices = new ArrayList<String>(Arrays.asList("M", "F"));
	ArrayList<String> stateChoices = new ArrayList<String>(Arrays.asList("ACT", "NSW", "NT", "QLD", "SA", "TAS", "VIC", "WA"));
	ChoiceBox<String> genderField = myUI.createChoiceBox(genderChoices , true);
	ChoiceBox<String> stateField  = myUI.createChoiceBox(stateChoices, true);		
	
	HBox hbAddUserBtn = new HBox(10);
	Button createUserBtn = myUI.createButton("Create User");
	Button backBtn = myUI.createButton("Main Menu");
	
	String enteredUsername;
	int userAge;
	
	public AddUser(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}
	
	public void initScene() {
		hbAddUserBtn.setAlignment(Pos.BOTTOM_CENTER); // Pos.BOTTOM_RIGHT
		hbAddUserBtn.getChildren().add(backBtn);
		hbAddUserBtn.getChildren().add(createUserBtn);		

		addUserPane.add(addUserHeader, 0, 0, 2, 1);
		addUserPane.add(usernameText, 0, 1); addUserPane.add(usernameField, 1, 1);
		addUserPane.add(ageText, 0, 2); addUserPane.add(ageField, 1, 2);
		addUserPane.add(statusText, 0, 3); addUserPane.add(statusField, 1, 3);
		addUserPane.add(imageText, 0, 4); addUserPane.add(imageField, 1, 4);
		addUserPane.add(genderText, 0, 5); addUserPane.add(genderField, 1, 5);
		addUserPane.add(stateText, 0, 6); addUserPane.add(stateField, 1, 6);		
		addUserPane.add(addUserNotes, 0, 7, 2, 1);
		addUserPane.add(warnings , 0, 8, 2, 1);
		addUserPane.add(hbAddUserBtn, 0, 11, 2, 1);
	}

	public void validateFields() throws NoSuchAgeException, Exception {
		enteredUsername = usernameField.getText();
		// userAge = 0;
		
		if ( enteredUsername.equals("") )
			throw new Exception("Enter a username");
		
		if ( MiniNet.people.existingUser(enteredUsername) )
			throw new Exception("Username taken. Enter another.");
		
		if ( !imageField.getText().equals("") ) {
			if ( !(imageField.getText().contains(".png") || imageField.getText().contains(".jpeg") || 
					 imageField.getText().contains(".jpg")) ) {
					throw new Exception("Extension of image must either be .png, .jpeg or .jpg");
				}
		}
		
		if ( !ageField.getText().equals("") ) {
			try {
				userAge = Integer.parseInt(ageField.getText());						
				if (userAge == 0) 
					throw new Exception("Must be at least a year old to create an account");
				
				if (userAge < 0)
					throw new NoSuchAgeException("Enter a value greater than 0 for age.");
				
				if (userAge > 150)
					throw new NoSuchAgeException("An age of " + userAge + " seems unlikely.");
				
			} catch ( NumberFormatException exc ) {
				throw new Exception("Age input must be an integer.");
			}	
		}
	}

	@Override
	public void handle(ActionEvent e) { 	
		
		initScene();			
		
		// creating a new user 
		createUserBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				try {
					validateFields();
					
					// create child or young child user
					if ( userAge > 0 && userAge < 17 ) {
						// store entered info into a list then pass to next class
						ArrayList<String> childDetails = new ArrayList<String>();
						childDetails.add(usernameField.getText());
						childDetails.add(Integer.toString(userAge));
						childDetails.add(statusField.getText());
						childDetails.add(imageField.getText());
						childDetails.add(genderField.getValue());
						childDetails.add(stateField.getValue());
						
						AddChildUser childUser = new AddChildUser(primaryStage, childDetails);
						childUser.displayAddChildUser();
					} else {
						// create adult user
						User newUser = new Adult(usernameField.getText(), 
												userAge, 
												statusField.getText(),
												imageField.getText(), 
												genderField.getValue(), 
												stateField.getValue());
						MiniNet.people.addUser(newUser);
						glob.insertUser(newUser);
						MiniNet.currentUser = MiniNet.people.getProfile(newUser.getUsername());
						
						ShowUser showUser = new ShowUser(primaryStage);
						showUser.displayUser();
					}
				} catch (NoSuchAgeException nsa) {
					warnings.setText(nsa.getMessage());
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
			
		addUserScene.getStylesheets().add(getClass().getResource("/app.css").toExternalForm());
		primaryStage.setScene(addUserScene);
		primaryStage.setTitle("Create A User");
		primaryStage.show();
	}
}
