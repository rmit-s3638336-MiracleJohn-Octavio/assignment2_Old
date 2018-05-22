package Network;
import Interfaces.ChildInterface;

/*     
 * ====== Child.java
 * 
 * */

public class Child extends User implements ChildInterface {

	// parents are need to be given values in instantiation
	private Adult parentOne;
	private Adult parentTwo;
	
	// constructor
	public Child(String username, int age, Adult parentOne, Adult parentTwo) {
		super(username, age);
		this.parentOne = parentOne;
		this.parentTwo = parentTwo;
		configureParents(parentOne, parentTwo);
	}
	
	public Child (String username, int age, String status, String image, String gender, String state, 
				  Adult parentOne, Adult parentTwo) { 
		super(username, age, status, image, gender, state);
		this.parentOne = parentOne;
		this.parentTwo = parentTwo;
		configureParents(parentOne, parentTwo);
	}
	
	public Child (String username, String image, String status, String gender, int age, String state) {
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
	@Override
	public Adult getParentOne() { return this.parentOne; }
	@Override
	public Adult getParentTwo() { return this.parentTwo; }	
}
