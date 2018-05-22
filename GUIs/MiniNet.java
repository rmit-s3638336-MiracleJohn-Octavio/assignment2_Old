package GUIs;

import java.util.Optional;

import Network.Connections;
import Network.People;
import Network.User;
import System.FileReader;
import System.MyGlobals;
import javafx.application.Application;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class MiniNet extends Application {

	/*+-----------------+*
	 *| Class Variables |
	 *+-----------------+*/
	
	public static People people = new People();
	public static Connections links = new Connections();
	public static User currentUser;
	
	private MyGlobals glob = new MyGlobals();
	
	/*+---------+*
	 *| Methods |
	 *+---------+*/
	
	public static void main(String[] args) {
		Application.launch(args);
	}
	
	@Override
	public void start (Stage primaryStage) throws Exception {
		
		/*+---------+*
		 *| SQLLite |
		 *+---------+*/
		
		// Create Database
		glob.sqlCreateDatabase();
		glob.sqlCreateTable();
		
		// Validate Data Text File "people.tx and relations.txt" if Exist
		if(glob.isFileExist(glob.txtfilePeople) && glob.isFileExist(glob.txtfileRelations)) {
			
			
			// Load Text Data to SQLLite if table is empty
			if (glob.isTableEmpty()) {
				glob.loadTextDataToSQLData();
			}
			
			// Use the Text File
			FileReader f = new FileReader();
			f.initialiseTempPeople();				// Uses Text File to load Data to Arraylist
			f.initialiseTempRelations();
			
			for (String key : f.getTempPeople().getAllProfiles().keySet()) {
				people.addUser(f.getTempPeople().getAllProfiles().get(key));
			}
			
			for (int i=0; i<f.getTempLinks().getRelationships().size(); i++) {
				links.getRelationships().add(f.getTempLinks().getRelationships().get(i));
			}
			
		} else if (glob.isFileExist(glob.sqlDBFile) && glob.isFileExist(glob.txtfileRelations)) {
			
			if (glob.isTableEmpty())  {
				glob.myAlert("Warning", "Missing Data Text File and SQLite Data file", "This will end the Program", AlertType.INFORMATION);
				System.exit(0);
			} else {
				Optional<ButtonType> result = glob.myConfirm("Confirm", 
						"Your people.txt is missing!", 
						"Do you want to continue and use the SQLLite instead? \r\n" +
						"", 
						AlertType.CONFIRMATION);
				if (result.get() == ButtonType.OK){
					
					// Load Text Data to SQLLite if table is empty
					if (glob.isTableEmpty()) {
						glob.loadTextDataToSQLData();
					}
					
					// Use the Text File
					FileReader f = new FileReader();
					f.populateData();						// Uses SQLLite to load Data to ArrayList
					f.initialiseTempRelations();
					
					for (String key : f.getTempPeople().getAllProfiles().keySet()) {
						people.addUser(f.getTempPeople().getAllProfiles().get(key));
					}
					
					for (int i=0; i<f.getTempLinks().getRelationships().size(); i++) {
						links.getRelationships().add(f.getTempLinks().getRelationships().get(i));
					}
					
			    	} else {
			    		
			    		// End the program (No Data file was selected)
			    		System.exit(0);
			    		
			    	};
			}
		}
		
		// Main Menu UI
		MainMenu mainMenu = new MainMenu(primaryStage);
		mainMenu.displayMainMenu();
	}

}


