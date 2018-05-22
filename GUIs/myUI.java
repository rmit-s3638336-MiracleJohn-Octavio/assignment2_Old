package GUIs;

/* @author: Adelbert Choi
 * created: May 5, 2018
 * 
 * = MyUI.java (GUI)
 * 
 */

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;

import javafx.geometry.Insets; 
import javafx.geometry.Pos;

public class myUI {
	
	// methods to create new buttons
	public static Button createButton(String text) {
		final String btnStyle = "-fx-padding: 12 25 12 25;"; //-fx-background-radius: 3,2,2,2;  
		final int btnWidth = 150;
		final int btnHeight = 20;
		
		Button newButton = new Button(text);
		newButton.setStyle(btnStyle);
 		newButton.setPrefSize(btnWidth, btnHeight);
 		newButton.getStyleClass().add("iphone-blue");	// add class (for css)
		return newButton;
	}
	
	public static ChoiceBox<String> createChoiceBox(ArrayList<String> choices, boolean IncludeEmptyStr) {
		ChoiceBox<String> newChoiceBox = new ChoiceBox<String>();
		final int boxWidth = 150;
		final int boxHeight = 20;
		
		for( String choice : choices) {
			newChoiceBox.getItems().add(choice);
		}
		
		if (IncludeEmptyStr) {
			newChoiceBox.getItems().add("");
			newChoiceBox.setValue("");
		} else {
			newChoiceBox.setValue(newChoiceBox.getItems().get(0));
		}
		
		newChoiceBox.setPrefSize(boxWidth, boxHeight);
		return newChoiceBox;
	}
	
	public static VBox createConnectionBox(ArrayList<String> friends, String title) {
		final int listWidth = 150;
		final int listHeight = 200;
		
		VBox newVBox = new VBox();
		ListView<String> newList = new ListView<String>();
		
		for(String connection : friends) {
			newList.getItems().add(connection);	
		}	
		
		Text boxTitle = new Text(title);
		boxTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
		
		newList.setMouseTransparent(false);
		newList.setFocusTraversable(false);
		newList.setPrefSize(listWidth, listHeight);
		newVBox.getChildren().add(boxTitle);
		newVBox.getChildren().add(newList);
		
		return newVBox;	
	}
		
	public static Label createLabel(String text) {
		final int labelWidth = 150;
		// final int labelHeight = 30;
			
		Label newLabel = new Label(text);
	 	//newLabel.setPrefSize(labelWidth, labelHeight);
		newLabel.setPrefWidth(labelWidth);
		return newLabel;
	}
	
	public static Label createLabel(String text, int prefWidth) { 
		// preWidth preferred to be 200-250
		Label newLabel = new Label(text);
	 	//newLabel.setPrefSize(labelWidth, labelHeight);
		newLabel.setPrefWidth(prefWidth);
		return newLabel;
	}
	
	public static TextField createTextField(String promptText) {
		final int fieldWidth = 250;
		// final int fieldHeight = 30;
			
		TextField newTextField = new TextField("");
	 	// newTextField.setPrefSize(fieldWidth, fieldHeight);
		newTextField.setPrefWidth(fieldWidth);
		newTextField.setPromptText(promptText);
		return newTextField;
	}
	
	public static Text createHeader(String text) {
		Text newHeader = new Text(text);
		
		newHeader.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		return newHeader;		
	}
	
	public static Text warningText(String text) {
		Text newWarning = new Text(text);
		newWarning.setFill(Color.FIREBRICK);
		newWarning.prefWidth(30);
		return newWarning;
	}
	
	public static GridPane createGridPane() {
		final int gridWdith = 900;
		final int gridHeight = 700;
		final int gridInset = 10;
		
		GridPane newGridPane = new GridPane();
		
		// size of pane
		newGridPane.setPrefSize(gridWdith, gridHeight);
		newGridPane.setPadding(new Insets(gridInset, gridInset, gridInset, gridInset));

		// vertical and horizontal gaps between the columns
		newGridPane.setVgap(8);
		newGridPane.setHgap(5);

		// grid alignment
		newGridPane.setAlignment(Pos.CENTER);

		return newGridPane;
	}	
	
}