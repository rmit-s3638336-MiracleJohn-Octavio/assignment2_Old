package DjikstraShortestPath;

public class Edge {
	// private final String id;
	private final Vertex source;
	private final Vertex destination;
	private final int weight;

	// String id, 
	public Edge(Vertex source, Vertex destination) {
		// this.id = id;
		this.source = source;
		this.destination = destination;
		this.weight = 1;
	}

//	public String getId() {
//		return id;
//	}

	public Vertex getDestination() {
		return destination;
	}

	public Vertex getSource() {
		return source;
	}

	public int getWeight() {
		return weight;
	}

	@Override
	public String toString() {
		return source + " " + destination;
	}

}