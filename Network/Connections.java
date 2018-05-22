package Network;
       
/* @author: Adelbert Choi
 * created: May 5, 2018
 *           
 * = Connections.java
 * 
 */

import java.util.ArrayList;

public class Connections {
	
	// this class is a database for all created relationships
	ArrayList<Relationship> relationships = new ArrayList<Relationship>(); 
	
	// constructor
	public Connections() {
	}
	
	// this method returns all existing relationships created for the class
	public ArrayList<Relationship> getRelationships() {
		return this.relationships;
	}
	
	// this method retrieves all existing relationships of a certain user
	// this method is helpful when looking for a friend of a friend 
	public ArrayList<Relationship> getUserRelationships(String username) {
		ArrayList<Relationship> userRelationships = new ArrayList<Relationship>();
		
		for (int i = 0; i < relationships.size(); i++) {
			if ( relationships.get(i).getUsernameOne().equals(username) )
				userRelationships.add(relationships.get(i));
		}
		
		return userRelationships;
	}
	
	// this method checks whether a certain relationship exists
	public boolean existingRelationship(String usernameOne, String usernameTwo) {
		for (int i=0; i<relationships.size(); i++) {
			if ( relationships.get(i).getUsernameOne().equals(usernameOne) &&
			     relationships.get(i).getUsernameTwo().equals(usernameTwo) )
				return true;
		}
		return false;
	}
	
	// a method to return a certain relationship
	public Relationship getRelationship(String usernameOne, String usernameTwo) {
		for (int i=0; i<relationships.size(); i++) {
			if ( relationships.get(i).getUsernameOne().equals(usernameOne) &&
			     relationships.get(i).getUsernameTwo().equals(usernameTwo) )
				return relationships.get(i);
		}
		return null;
	}
	
	// a method to add a relationship.
	// type = parent, friend, classmate, colleague, siblings
	public void addRelationship(String usernameOne, String usernameTwo, String type) {
		relationships.add(new Relationship(usernameOne, usernameTwo, type));
		relationships.add(new Relationship(usernameTwo, usernameOne, type));
	}
	
	
	// a method to completely delete a relationship
	// similar to addRelationhips, the vice versa relationship should also be removed
	// TODO: FIX THIS, sometimes user relationships do not get removed
	public void deleteRelationships(String usernameOne, String usernameTwo) {
		for (int i = 0; i < relationships.size(); i++) {
			if (relationships.get(i).getUsernameOne().equals(usernameOne) && relationships.get(i).getUsernameTwo().equals(usernameTwo))
				relationships.remove(i);
			
			if (relationships.get(i).getUsernameTwo().equals(usernameOne) && relationships.get(i).getUsernameOne().equals(usernameTwo))
				relationships.remove(i);
		}
	}
	
	// this method removes all existing relationships of a certain user
	// this method is used when the program is permanently deleting a certain user.
	public void deleteAllUserRelationships(String username) {
		for (int i = 0; i < relationships.size(); i++)
			if ( relationships.get(i).getUsernameOne().equals(username) || 
			     relationships.get(i).getUsernameTwo().equals(username) )
				relationships.remove(i);
	}
	
	public void changeRelationship(String usernameOne, String usernameTwo, String newType) {
		for (int i = 0; i < relationships.size(); i++) {
			if (relationships.get(i).getUsernameOne().equals(usernameOne) && 
			    relationships.get(i).getUsernameTwo().equals(usernameTwo))
				relationships.get(i).changeType(newType);
			
			if (relationships.get(i).getUsernameOne().equals(usernameTwo) && 
			    relationships.get(i).getUsernameTwo().equals(usernameOne))
				relationships.get(i).changeType(newType);
		}
	}	
}
