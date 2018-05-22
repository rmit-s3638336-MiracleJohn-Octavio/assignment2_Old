package GUIs;

/* @author: Adelbert Choi
 * created: May 5, 2018
 * 
 * AddFriend.java (GUI)
 * 
 */

import java.util.ArrayList;
import java.util.Collections;

import DjikstraShortestPath.Vertex;
import DjikstraShortestPath.Edge;
import DjikstraShortestPath.Graph;
import DjikstraShortestPath.DijkstraAlgorithm;
import Interfaces.GUIInterface;
import Network.Relationship;
import System.MyGlobals;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import javafx.geometry.Pos;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

class FindFriend implements EventHandler<ActionEvent>, GUIInterface {
	
	/*+-----------------+*
	 *| Class Variables |
	 *+-----------------+*/
	
	private MyGlobals glob = new MyGlobals();
	
	private Stage primaryStage;
	
	private GridPane findFriendPane = myUI.createGridPane();
	private Scene findFriendScene = new Scene(findFriendPane, glob.WINDOW_W, glob.WINDOW_H);
	
	private Text findFriendHeader = myUI.createHeader("Find a Friend");
	private Label findFriendLabel = myUI.createLabel("Choose a friend: ");
	private Label findFriendNotif = new Label("");
	private ListView<String> notFriendsList = new ListView<String>();
	
	private HBox hbFindFriendBtn = new HBox(10);
	private Button findFriendBtn = myUI.createButton("Find Network");
	private Button backBtn = myUI.createButton("Back");
	
	private ArrayList<String> notFriends = new ArrayList<String>();
	private ArrayList<String> userFriends = new ArrayList<String>();
	
	// graph variables to implement shortest path to friend
	private ArrayList<Vertex> nodes = new ArrayList<Vertex>();
	private ArrayList<Edge> edges = new ArrayList<Edge>();
	private Graph graph; 
	
	public FindFriend(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}
	
	public void getNotFriends() {		
		ArrayList<Relationship> friends =  MiniNet.links.getUserRelationships(MiniNet.currentUser.getUsername());
		
		// get friends
		for (Relationship friend : friends) {
			userFriends.add(friend.getUsernameTwo());
		}

		// get non friends
		for (String user : MiniNet.people.getAllProfiles().keySet() ) {
			if ( userFriends.contains(user) ) 
				continue;
			if ( user.equals(MiniNet.currentUser.getUsername()) )
				continue;
			else 
				notFriends.add(user);
		}
	}
	
	public void initNotFriendsListView() {
		// sort not friends in alphabetical order
		Collections.sort(notFriends);
		
		for(String notFriend : notFriends) {
			notFriendsList.getItems().add(notFriend);	
		}
	}
	
	public Vertex getVertex(String username) {
		for ( Vertex node : nodes ) {
			if ( node.getId().equals(username) )
				return node;
		}
		return null;
	}
	
	public void initGraph() {
		int nodeOne;
		int nodeTwo;
		
		// add nodes
		for (String user : MiniNet.people.getAllProfiles().keySet() ) {
			nodes.add(new Vertex(user));
		}
		
		// add edges
		for (Relationship friend : MiniNet.links.getRelationships() ) {
			nodeOne = nodes.indexOf(getVertex(friend.getUsernameOne()));
			nodeTwo = nodes.indexOf(getVertex(friend.getUsernameTwo()));
			addLane(nodeOne, nodeTwo);
		}
		
		// initialise graph
		graph = new Graph(nodes, edges);		
	}
	
	public void addLane(int sourceLocNo, int destLocNo) {
		Edge lane = new Edge(nodes.get(sourceLocNo), nodes.get(destLocNo));
		edges.add(lane);
	}
 	
	public void initScene() {
		getNotFriends();		
		initNotFriendsListView();
		initGraph();
					
		hbFindFriendBtn.setAlignment(Pos.BOTTOM_CENTER); // Pos.BOTTOM_RIGHT
		hbFindFriendBtn.getChildren().add(backBtn);
		hbFindFriendBtn.getChildren().add(findFriendBtn);
				
		findFriendPane.add(findFriendHeader, 0, 0, 2, 1);  
		findFriendPane.add(findFriendLabel, 0, 1);
		findFriendPane.add(findFriendNotif, 0, 2, 2, 1);
		findFriendPane.add(notFriendsList, 0, 3, 2, 1);
		findFriendPane.add(hbFindFriendBtn, 0, 6, 2, 1);
	}
	
	
	public void findPathToFriend() {
		DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);
		Vertex currentUserNode = getVertex(MiniNet.currentUser.getUsername());
		Vertex friendNode = getVertex(notFriendsList.getSelectionModel().getSelectedItem());
		String pathString = "";
		
		dijkstra.execute(currentUserNode);
		//LinkedList<Vertex> path = dijkstra.getPath( );
		ArrayList<Vertex> path = dijkstra.getPath(friendNode);

		if (path != null) {
			for ( int i=0; i<path.size(); i++) {
				if ( i == 0 ) 
					pathString = pathString + path.get(i).getId() + "\n";
				else 
					pathString = pathString + " --> " + path.get(i).getId() + "\n";
					
			}
		} else {
			pathString = pathString + "No path found";
		}
		
		
		Alert findFriendAlert = new Alert(AlertType.INFORMATION); 
		findFriendAlert.setTitle("Find friend");
 
		findFriendAlert.setHeaderText("User following path to connect with " + friendNode.getId());
		findFriendAlert.setContentText(pathString);
		findFriendAlert.showAndWait();
	}
	
	@Override
	public void handle(ActionEvent e) { 		
		
		initScene();
		
		notFriendsList.setOnMouseClicked(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent arg0) {
				findFriendNotif.setTextFill(Color.BLACK);
				findFriendNotif.setText("Figure out how to connect with " + notFriendsList.getSelectionModel().getSelectedItem() + "?" );
			}
		});	
		
		// add friend button
		findFriendBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				
				try {
					findPathToFriend();
				} catch (NullPointerException np) {
					findFriendNotif.setTextFill(Color.RED);
					findFriendNotif.setText("Select a User first");
				}
			}
		});
		
		// back to user profile
		backBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {					
				ShowUser showUser = new ShowUser(primaryStage);
				showUser.displayUser();
			}
		});
	
		primaryStage.setTitle("Find a Friend");
		findFriendScene.getStylesheets().add(getClass().getResource("/app.css").toExternalForm());
		primaryStage.setScene(findFriendScene);
		primaryStage.show();
	}
}