package Network;
        
/*        
 * ====== User.java
 * 
 * */

import java.util.ArrayList;

import System.NoParentException;

public abstract class User {
	
	private String username;
	private int age = 0;
	private String status = "";
	private String image = "";
	private String gender = ""; // M or F or none
	private String state = ""; // VIC, NSW etc.
	private ArrayList<User> friends = new ArrayList<User>();
	
	// constructors
	public User(String username, int age) {
		this.username = username;
		this.age = age;
	}
	
	// alternative constructor 
	// User profile constructor - this is mostly used for instantiating adult users
	public User(String username) {
		this.username = username;
	}
	
	public User(String username, int age, String status, String image, String gender, String state) {
			this.username = username;
			this.age = age;
			this.status = status;
			this.image = image;
			this.gender = gender;
			this.state = state;
	}
	
	// getters - to obtain user class instance variables 
	// to obtain user profile information
	public String getUsername() { return this.username; }
	public int getAge() { return this.age; }
	public String getStatus() { return this.status; }
	public String getImage() { return this.image; }
	public String getGender() { return this.gender; }
	public String getState() { return this.state; }
	public ArrayList<User> getFriends() { return this.friends; }
	
	// setters - to allow changes to user class instance variables
	public void setUsername(String newUsername) { this.username = newUsername; }
	public void setAge(int newAge) { this.age = newAge; }
	public void setStatus(String newStatus) { this.status = newStatus; }
	public void setImage(String newImage) { this.image = newImage; }
	public void setGender(String newGender) { this.gender = newGender; }
	public void setState(String newState) { this.state = newState; }

	// can be considered a setter
	// this method adds a friend to a user's friend list
	// this method is required to make changes to friends instance variable i.e. list of friends
	// otherwise, modifying friends would be impossible in subclass methods
	public void addToFriends(User friend) { this.friends.add(friend); } 
	
	// method to add a friend
	public void addFriend(User friend) {
		if ( !friends.contains(friend) ) {
			addToFriends(friend);
			friend.addToFriends(this);
		}
	}
	 	 	
	// modifier 
	// this method removes a friend from the user's friend list
	// this method is required to make changes to friends instance variables
	// otherwise, modifying friends would be impossible in subclass methods
	public void removeFromFriends(User friend) { 
		friends.remove(friends.indexOf(friend)); 
	}
	
	// method to delete a friend
	// this method returns true if friend was successfully deleted from friends list
	// otherwise false
	public void deleteFriend(User friend) throws NoParentException {	
		removeFromFriends(friend);
		friend.removeFromFriends(this);
	}

}
