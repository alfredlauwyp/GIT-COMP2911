import java.util.ArrayList;
import java.util.LinkedList;

public class CourierDeliveryGraph implements Graph {
	
	public CourierDeliveryGraph() {
		nodes = new LinkedList<Node>();
	}

	@Override
	public void addNode(Node node) {
		nodes.add(node);
	}

	public LinkedList<Node> getNodes() {
		return nodes;
	}
	
	@Override
	public Node createNode(ArrayList<Integer> coordinates) {
		Node node = new CourierDeliveryJob(coordinates);
		return node;
	}

	private LinkedList<Node> nodes;

}
