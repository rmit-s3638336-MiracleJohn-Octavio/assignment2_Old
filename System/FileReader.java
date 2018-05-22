package System;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.StringTokenizer;

import Interfaces.ChildInterface;
import Network.Adult;
import Network.User;
import Network.Child;
import Network.YoungChild;
import Network.Connections;
import Network.People;
import Network.Relationship;

public class FileReader {

	private People tempPeople = new People();
	private Connections tempLinks = new Connections();
	private MyGlobals glob = new MyGlobals();
	
	private ArrayList<Relationship> tempCouples = new ArrayList<Relationship>();
	private ArrayList<Relationship> tempParents = new ArrayList<Relationship>();
	private ArrayList<Relationship> tempOtherRelations = new ArrayList<Relationship>();

	public People getTempPeople() {
		return tempPeople;
	}
	
	public Connections getTempLinks() {
		return tempLinks;
	}
	
	public void initialiseTempPeople() {
		readPersonTextFile();
	}
	
	public void initialiseTempRelations() throws NoParentException {
		readRelationsTextFile();
		initialiseCouples();
		initialiseParent();
		initialiseOtherRelationships();
		validateChildUsers();
	}
	
	// Used to load Text Data to Array list
	private void initialiseUser(String line) {
		StringTokenizer tokens = new StringTokenizer(line, ",");

		// assuming the format is as is in the spec sheet
		String username = tokens.nextToken().trim();
		String image = tokens.nextToken().replaceAll("\"", "").trim();
		String status = tokens.nextToken().replaceAll("\"", "").trim();
		String gender = tokens.nextToken().trim();
		int age = Integer.parseInt(tokens.nextToken().trim());
		String state = tokens.nextToken().trim();

		User newUser;

		if (age > 16)
			newUser = new Adult(username, image, status, gender, age, state);
		else if (age > 2)
			newUser = new Child(username, image, status, gender, age, state);
		else
			newUser = new YoungChild(username, image, status, gender, age, state);

		tempPeople.addUser(newUser);
	}
	
	// Used to load Text Data to SQLLite
	public void populateData() {
        // SQL statement for creating a new table
        String sqlQuery = "SELECT * FROM people";
        
		String username;
		String image;
		String status;
		String gender;
		int age;
		String state;
	
		try (Connection conn = DriverManager.getConnection(glob.sqlConnString);
                Statement stmt = conn.createStatement()) {
            // create a new table
            ResultSet rs =  stmt.executeQuery(sqlQuery);
            
            while (rs.next()) {
	            	username = rs.getString("username");
	            	image = rs.getString("image");
	        		status = rs.getString("status");
	        		gender = rs.getString("gender");
	        		age = Integer.parseInt(rs.getString("age"));
	        		state = rs.getString("state");
            	
	        		User newUser;
	        		if (age > 16)
	        			newUser = new Adult(username, image, status, gender, age, state);
	        		else if (age > 2)
	        			newUser = new Child(username, image, status, gender, age, state);
	        		else
	        			newUser = new YoungChild(username, image, status, gender, age, state);
	        		tempPeople.addUser(newUser);
            }
            
        } catch (SQLException e) {
        		glob.printIt(e.getMessage());
        		System.exit(1);
        }
    }
	
	private void validateChildUsers()   {
		try {
			for (String key : tempPeople.getAllProfiles().keySet()) {
				if (tempPeople.getProfile(key) instanceof Child) {
					Child temp = ((Child) tempPeople.getProfile(key));

					if (temp.getParentOne() == null || temp.getParentTwo() == null) {
						System.out.println("Child User " + key + " not added missing a parent");
						tempPeople.deleteUserFromInit(temp.getUsername());
					}
				}

				if (tempPeople.getProfile(key) instanceof YoungChild) {
					YoungChild temp = ((YoungChild) tempPeople.getProfile(key));

					if (temp.getParentOne() == null || temp.getParentTwo() == null) {
						System.out.println("Child User " + key + " not added missing a parent");
						tempPeople.deleteUserFromInit(temp.getUsername());
					}
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	private void initialiseCouples() {
		Adult userOne;
		Adult userTwo;
		
		for ( Relationship relation : tempCouples ) {
			userOne = (Adult) tempPeople.getProfile(relation.getUsernameOne());
			userTwo = (Adult) tempPeople.getProfile(relation.getUsernameTwo());
			
			if (userOne.getPartner() == null && userTwo.getPartner() == null) {
				userOne.setPartner(userTwo);
				userTwo.setPartner(userOne);
				tempLinks.addRelationship(relation.getUsernameOne(), relation.getUsernameTwo(), relation.getType());
			} else if (userOne.getPartner() != null) {
				System.out.println(userOne.getUsername() + " already has a partner");
			} else if (userTwo.getPartner() != null) {
				System.out.println(userTwo.getUsername() + " already has a partner");
			}
		}
	}
	
	private void initialiseParent() {
		Adult adult = null;
		ChildInterface child = null;
		
		for ( Relationship relation : tempParents ) {
			if ( tempPeople.getProfile(relation.getUsernameOne()) instanceof Adult ) {
				adult = (Adult) tempPeople.getProfile(relation.getUsernameOne());
				child = (ChildInterface) tempPeople.getProfile(relation.getUsernameTwo());
			} else {
				adult = (Adult) tempPeople.getProfile(relation.getUsernameTwo());
				child = (ChildInterface) tempPeople.getProfile(relation.getUsernameOne());
			}
			adult.addDepedent(child);
			
			if (child.getParentOne() == null) {
				child.setParentOne(adult);
			} else {
				child.setParentTwo(adult);
			}
			
			tempLinks.addRelationship(relation.getUsernameOne(), relation.getUsernameTwo(), relation.getType());
		}
	}
	
	private void initialiseOtherRelationships() {
		User userOne;
		User userTwo;
		
		for ( Relationship relation : tempOtherRelations ) {
			userOne = tempPeople.getProfile(relation.getUsernameOne());
			userTwo = tempPeople.getProfile(relation.getUsernameTwo());
			
			userOne.addToFriends(userTwo);
			userTwo.addToFriends(userOne);
			tempLinks.addRelationship(relation.getUsernameOne(), relation.getUsernameTwo(), relation.getType());
		}
	}
	
	private void storeRelations(String line) {
		StringTokenizer tokens = new StringTokenizer(line, ",");

		// assuming the format is as is in the spec sheet
		String usernameOne = tokens.nextToken().trim();
		String usernameTwo = tokens.nextToken().trim();
		String Type = tokens.nextToken().trim();
		
		switch(Type) {
			case "couple":
				tempCouples.add(new Relationship(usernameOne, usernameTwo, "couple"));
				break;
			case "parent": 
				tempParents.add(new Relationship(usernameOne, usernameTwo, "parent"));
				break;
			case "friends": 
				tempOtherRelations.add(new Relationship(usernameOne, usernameTwo, "friend"));
				break;
			case "classmates":
				tempOtherRelations.add(new Relationship(usernameOne, usernameTwo, "classmate"));
				break;
			case "colleagues":
				tempOtherRelations.add(new Relationship(usernameOne, usernameTwo, "colleague"));
				break;
			case "sibling":
				tempOtherRelations.add(new Relationship(usernameOne, usernameTwo, "sibling"));
				break;
		}
	}

	/*+----------------+*
	 *| Read Text File |
	 *+----------------+*/
	
	private void readPersonTextFile() {

		File file = new File(glob.txtfilePeople);
		BufferedReader input = null;

		try {
			java.io.FileReader fr = new java.io.FileReader(file);
			input = new BufferedReader(fr);

			String next = input.readLine();
			while (next != null) {
				initialiseUser(next);

				next = input.readLine();
			}
			input.close();

		} catch (FileNotFoundException e) {
			System.err.println("No Such File");
			System.exit(0);
		} catch (IOException e) {
			System.err.println("File Reading Error!");
			System.exit(0);
		} 
	}
	
	private void readRelationsTextFile() {

		File file = new File(glob.txtfileRelations);
		BufferedReader input = null;

		try {
			java.io.FileReader fr = new java.io.FileReader(file);
			input = new BufferedReader(fr);

			String next = input.readLine();
			while (next != null) {
				storeRelations(next);
				
				next = input.readLine();
			}
			input.close();

		} catch (FileNotFoundException e) {
			System.err.println("No Such File");
			System.exit(0);
		} catch (IOException e) {
			System.err.println("File Reading Error!");
			System.exit(0);
		} 
	}


}
