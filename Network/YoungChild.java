package Network;
import Interfaces.ChildInterface;

/* @author: Adelbert Choi
 * created: May 5, 2018
 * 
 * = YoungChild.java
 * 
 * */

public class YoungChild extends User implements ChildInterface {

	// parents are need to be given values in instantiation
	private Adult parentOne;
	private Adult parentTwo;
	
	// constructor
	public YoungChild(String username, int age, Adult parentOne, Adult parentTwo) {
		super(username, age);
		this.parentOne = parentOne;
		this.parentTwo = parentTwo;
		configureParents(parentOne, parentTwo);
	}
	
	public YoungChild(String username, int age, String status, String image, String gender, String state, 
				  	 Adult parentOne, Adult parentTwo) { 
		super(username, age, status, image, gender, state);
		this.parentOne = parentOne;
		this.parentTwo = parentTwo;
		configureParents(parentOne, parentTwo);
	}
	
	public YoungChild (String username, String image, String status, String gender, int age, String state) {
		super(username, age, status, image, gender, state);
	}
	
	public void configureParents(Adult parentOne, Adult parentTwo) {
		parentOne.addDepedent(this); 
		parentTwo.addDepedent(this);
		parentOne.setPartner(parentTwo);
		parentTwo.setPartner(parentOne);
	}
	
	public void setParentOne(Adult parentOne) { this.parentOne = parentOne; }
	public void setParentTwo(Adult parentTwo) { this.parentTwo = parentTwo; }
	
	// getters - to obtain child class instance variables
	public Adult getParentOne() { return this.parentOne; }
	public Adult getParentTwo() { return this.parentTwo; }
	
}
