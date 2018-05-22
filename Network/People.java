package Network;
         
/*       
 * = Profiles.java
 * 
 * */

import java.util.HashMap;

import System.NoParentException;

public class People {
	
	// this list keeps track of all types of profiles created
	HashMap<String, User> profiles = new HashMap<String, User>();
	
	// constructor for a complete list of profiles
	public People() {
	}
	
	// method to return the entire list of created users
	public HashMap<String, User> getAllProfiles() {
		return this.profiles;
	}
	
	// method to check whether a certain user is currently in the list or not
	public boolean existingUser(String username) {
		if ( profiles.containsKey(username) )
			return true;
		else 
			return false;
	}
	
	// method to check if a certain user has a certain friend
	public boolean areFriends(User user, User friend) {		
		if ( user.getFriends().contains(friend) )
			return true;
		else 
			return false;
	}
	
	// method to add a new user to the current profiles list
	public void addUser(User newUser) {
		if ( !existingUser(newUser.getUsername()) )
			profiles.put(newUser.getUsername(), newUser);
	}

	// this is the overall function to delete a user from the list of user profiles
	// depending on the type of user it calls the right method to check for 
	// conditions before a user is fully deleted from the list of profiles
	public boolean deleteUser(String username) throws NoParentException { 
		// if no user with username exist cannot delete anything
		if ( !existingUser(username) )
			return true;
			
		if ( getProfile(username) instanceof Adult )
			return deleteAdultUser(username);
		else if ( getProfile(username) instanceof Child )
			return deleteChildUser(username);
		else
			return deleteYoungChildUser(username);
	}
	
	// method to delete a certain user from the list of friends of other users
	// only works, if they the user is present in the list of friends of other users
	private void deleteUserfromFriends(User user) throws NoParentException {
		for ( int i=0; i<user.getFriends().size(); i++ )
			user.getFriends().get(i).deleteFriend(user);
		
		// this loop throws java.util.ConcurrentModificationException don't know why
		// looks like the same thing
		//for ( User friends : user.getFriends() ) {
		//	friends.deleteFriend(user);
		//}
	}
	
	// method to deleting an adult type user permanently from the list of profiles 
	// certain conditions need to be met before an adult user can be deleted 
	private boolean deleteAdultUser(String username) throws NoParentException {
		Adult user = ((Adult) getProfile(username));

		if (!user.getDependents().isEmpty())
			throw new NoParentException("Can't delete user. User has dependents");
		
		// if user profile has a partner adjust partners instance variables accordingly
		// before deleting the adult user
		if ( user.getPartner() != null )
			user.getPartner().setPartner(null);

		// delete the adult user profile from the list of user profiles
		deleteUserfromFriends(user);
		profiles.remove(username);	
		return true;	
	}		
	
	// method to deleting a child type user permanently from the list of profiles 
	// this method will always return true, since a child profile can always be deleted
	// however, making the method return true will be beneficial for better implementation 
	// in the Driver class
	private boolean deleteChildUser(String username) throws NoParentException {
		Child user = ((Child) getProfile(username));
		
		// set the dependents of parents to null
		// since this child user profile will no longer exist
		user.getParentOne().removeDepedent(user); 
		user.getParentTwo().removeDepedent(user);

		// delete the child user profile from the list of user profiles
		// delete the adult user profile from the list of user profiles
		deleteUserfromFriends(user);
		profiles.remove(username);	
		return true;	
	}
	
	private boolean deleteYoungChildUser(String username) throws NoParentException {
		YoungChild user = ((YoungChild) getProfile(username));
		
		// set the dependents of parents to null
		// since this child user profile will no longer exist
		user.getParentOne().removeDepedent(user); 
		user.getParentTwo().removeDepedent(user);

		// delete the child user profile from the list of user profiles
		// delete the adult user profile from the list of user profiles
		deleteUserfromFriends(user);
		profiles.remove(username);	
		return true;	
	}
	
	// method to delete a user from instantiation
	public void deleteUserFromInit(String username) {
		// if no user with username exist cannot delete anything
		if (!existingUser(username))
			return;

		profiles.remove(username);			
	}
	
	// method to return a certain user 
	public User getProfile(String username) {
		if ( existingUser(username) )
			return profiles.get(username);
		else
			return null;
	}	
}
