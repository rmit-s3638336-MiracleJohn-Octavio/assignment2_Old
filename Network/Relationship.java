package Network;
       
/* @author: Adelbert Choi
 * created: May 5, 2018
 *           
 * = Relationship.java
 * 
 * */

public class Relationship {
	
	private String usernameOne;
	private String usernameTwo;
	private String type; // Parent, friend, classmate, colleague, siblings
	
	
	// constructor for the Relationship class
	public Relationship(String usernameOne, String usernameTwo, String type) {
		this.usernameOne = usernameOne;
		this.usernameTwo = usernameTwo;
		this.type = type;
	}
	
	// getters - to obtain the usernames of the users part of a relationship
	public String getUsernameOne() { return this.usernameOne; }
	public String getUsernameTwo() { return this.usernameTwo; }
	public String getType() { return this.type; }
	
	// setters
	public void changeType(String newType) { this.type = newType; }
	
}
