package System;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.StringTokenizer;

import Network.Adult;
import Network.Child;
import Network.People;
import Network.User;
import Network.YoungChild;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Screen;

public class MyGlobals {
	
	/*+-----------+*
	 *| Variables |
	 *+-----------+*/
	
	// Window Size
	public final Double WINDOW_W = (double) 650;
	public final Double WINDOW_H = (double) 650;
	
	// Text Files
	public final String txtfilePeople = "people.txt";
	public final String txtfileRelations = "relations.txt";
	
	// SQLLite
	public final String sqlDBFile = "MiniNet.db";
	public final String sqlConnString = "jdbc:sqlite:" + sqlDBFile; // Database Connection String

	/*+------+*
	 *| Data |
	 *+------+*/
	
	public void sqlCreateDatabase() {
		File tmpDir = new File(sqlDBFile);
		if (!tmpDir.exists()) {
			try (Connection conn = DriverManager.getConnection(sqlConnString)) {
	            if (conn != null) {
	                DatabaseMetaData meta = conn.getMetaData();
	                printIt(
	                		"The driver name is \n" + meta.getDriverName() +
	                		"Database was successfully created!");
	            }
	            
	        } catch (SQLException e) {
	        		printIt(e.getMessage());
	            System.exit(1);
	        }
		} 
	}
	
	public void sqlCreateTable() {
        // SQL statement for creating a new table
        String sqlQuery = "CREATE TABLE IF NOT EXISTS people "
        		+ "(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
        		+ "username TEXT NOT NULL, "
        		+ "image TEXT, "
        		+ "status TEXT, "
        		+ "gender TEXT, "
        		+ "age INTEGER, "
        		+ "state TEXT);";
        
        try (Connection conn = DriverManager.getConnection(sqlConnString);
                Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sqlQuery);
        } catch (SQLException e) {
        		printIt(e.getMessage());
        		System.exit(1);
        }
    }
	
	public void sqlExecQuery(String sqlQuery) {
        try (Connection conn = DriverManager.getConnection(sqlConnString);
                Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sqlQuery);
        } catch (SQLException e) {
        		printIt(e.getMessage());
        		System.exit(1);
        }
    }
	
	public boolean isTableEmpty() {
        // SQL statement for creating a new table
		boolean returnValue = true;
        String sqlQuery = "SELECT count(*) row_counter FROM people";
        
        try (Connection conn = DriverManager.getConnection(sqlConnString);
                Statement stmt = conn.createStatement()) {
            // create a new table
            ResultSet rs =  stmt.executeQuery(sqlQuery);
            
            rs.next();
            int rowCounter = Integer.parseInt(rs.getString("row_counter"));
            if (rowCounter > 0) {
            		returnValue = false;
			}
        } catch (SQLException e) {
        		printIt(e.getMessage());
        		System.exit(1);
        }
        return returnValue;
    }
	
	/*+------+*
	 *| File |
	 *+------+*/
	
	public boolean isFileExist(String fileName) {
		// Declare Variable
		boolean isFileExist = false; 
		
		// Validate
		File tmpDir = new File(fileName);
		if (tmpDir.exists()) {
			isFileExist = true;
		}
		
		// Return the value
		return isFileExist;
	}
	
	public void loadTextDataToSQLData() {
		File file = new File(txtfilePeople);
		BufferedReader input = null;

		try {
			java.io.FileReader fr = new java.io.FileReader(file);
			input = new BufferedReader(fr);

			String next = input.readLine();
			while (next != null) {
				// Initialize Token
				StringTokenizer tokens = new StringTokenizer(next, ",");
				String username = "'" + tokens.nextToken().trim() + "'";
				String image = "'" + tokens.nextToken().replaceAll("\"", "").trim() + "'";
				String status = "'" + tokens.nextToken().replaceAll("\"", "").trim() + "'";
				String gender = "'" + tokens.nextToken().trim() + "'";
				int age = Integer.parseInt(tokens.nextToken().trim());
				String state = "'" + tokens.nextToken().trim() + "'";

				// Insert the record to SQLLite
				sqlExecQuery("INSERT INTO people"
						+ "("
						+ "username, "
						+ "image, "
						+ "status, "
						+ "gender, "
						+ "age, "
						+ "state"
						+ ") values "
						+ "(" + username 
						+ "," + image 
						+ "," + status 
						+ "," + gender 
						+ "," + age 
						+ "," + state 
						+ ")");
				
				System.out.println(next);
				next = input.readLine();
			}
			input.close();

		} catch (IOException e) {
			System.err.println("File Reading Error!");
			System.exit(0);
		} 
	}
	
	public void insertUser(User newUser) {
		// Insert the record to SQLLite
		sqlExecQuery("INSERT INTO people"
				+ "("
				+ "username, "
				+ "image, "
				+ "status, "
				+ "gender, "
				+ "age, "
				+ "state"
				+ ") values "
				+ "('" + newUser.getUsername() + "'" 
				+ ",'" + newUser.getImage() + "'"
				+ ",'" + newUser.getStatus() + "'"
				+ ",'" + newUser.getGender() + "'"
				+ ",'" + newUser.getAge() + "'"
				+ ",'" + newUser.getState() + "'"
				+ ")");
	}
	
	public void updateUser(User newUser) {
		try {
			sqlExecQuery("UPDATE people "
					+ "SET "
					+ "image='" + newUser.getImage() + "', "
					+ "status='" + newUser.getStatus() + "', "
					+ "gender='" + newUser.getGender() + "', "
					+ "state='" + newUser.getState() + "' "
					+ "where username='" + newUser.getUsername() + "'");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void deleteUser(User newUser) {
		try {
			sqlExecQuery("DELETE FROM people "
					+ "where username='" + newUser.getUsername() + "'");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	/*+-----+*
	 *| UDF |
	 *+-----+*/
	
	public void printIt(String str) {
		System.out.println(str);
	}
	
	public void myAlert(String title, String header, String message, AlertType alertType) {
		Alert alert = new Alert(alertType);
	    	alert.setTitle(title);
	    	Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
//        alert.setX(bounds.getMaxX() / 4);
//        alert.setY(bounds.getMaxY() / 5);
	    	alert.setHeaderText(header);
	    	alert.setContentText(message);
	    	alert.showAndWait();
	}
	public void myAlert(String message) {
		this.myAlert("Message Alert","Header",message, AlertType.INFORMATION);
	}
	public void myAlert() {
		this.myAlert("Message Alert","Header","This is a dummy alert!", AlertType.INFORMATION);
	}

	public Optional<ButtonType> myConfirm(String title, String header, String message, AlertType alertType) {
		Alert alert = new Alert(alertType);
	    	alert.setTitle(title);
	    	Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
	    	alert.setHeaderText(header);
	    	alert.setContentText(message);
	    	
	    	Optional<ButtonType> result = alert.showAndWait();
	    	return result;
	}
	
}
