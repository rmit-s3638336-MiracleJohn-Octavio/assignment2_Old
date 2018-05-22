package Network;
     
/*        
 * ====== Adult.java
 * 
 * */

import java.util.ArrayList;

import Interfaces.ChildInterface;
import System.NoParentException;

public class Adult extends User {
	
	// Adult instance variables
	// dependent and partner are set to null initially
	private ArrayList<User> dependents = new ArrayList<User>();
	private Adult partner = null;
	
	// Adult class constructors
	public Adult (String username) { 
		super(username); 
	}
	
	public Adult(String username, int age) {
		super(username, age);
	}
	
	public Adult (String username, int age, String status, String image, String gender, String state) { 
		super(username, age, status, image, gender, state);
	}
	
	// username, image, status, gender, age, state
	public Adult (String username, String image, String status, String gender, int age, String state) {
		super(username, age, status, image, gender, state);
	}
	
	// getters - to obtain adult class instance variables
	public ArrayList<User> getDependents() { return this.dependents; }
	public Adult getPartner() { return this.partner;  }
	
	// this method sets the dependent of this adult user
	public void addDepedent(ChildInterface child) {
		dependents.add((User) child);
	}
	
	// this method removes a dependent of this adult user
	public void removeDepedent(User dependent) {
		dependents.remove(dependents.indexOf(dependent));
	}
		
	// this method sets the partner of the adult user
	// Used in situations when a dependent is being set
	public void setPartner(Adult partner)  {
		// set partner
		
		this.partner = partner; 
		
		if ( partner != null && !super.getFriends().contains(partner) ) {
			super.addToFriends(partner);
			partner.addToFriends(this);	
		}
	}
	
	@Override
	public void deleteFriend(User friend) throws NoParentException {
		if ( (((Adult) friend).getPartner() == this) && !dependents.isEmpty())
			throw new NoParentException("Can't delete friend. User is partner to dependents");
		
		super.removeFromFriends(friend);
		friend.removeFromFriends(this);		
		
		if ( (partner != null) && partner.getUsername().equals(friend.getUsername()) ) {
			this.setPartner(null);
			((Adult) friend).setPartner(null);		
		}
	}	
}
