import java.util.LinkedList;
import java.util.Comparator;
import java.util.Collections;
import java.lang.StringBuffer;

/**
 * Implements Breadth First Search Algorithm
 * @author Hayden Smith
 *
 */
public class BFS {

	public BFS(DirectedStringGraph graph)
	{
		this.graph = graph;
		allNodes = graph.getNodeObjects();
	}
	
	public String findPath(String nodeStart, String nodeFinish, Comparator<DSGEdge> comp)
	{
		LinkedList<DSGNode> nodesVisited = new LinkedList<DSGNode>();
		LinkedList<DSGNode> nodesToVisit = new LinkedList<DSGNode>();
		LinkedList<DSGNode> parent = new LinkedList<DSGNode>();
		DSGNode rootNode = allNodes.get(0);
		
		nodesToVisit.add(rootNode);
		nodesVisited.add(rootNode);
		
		if (nodesVisited.size() == 1)
		{
			parent.add(null);
		}
		else
		{
			parent.addFirst(nodesVisited.get(1));
		}
		while(!nodesToVisit.isEmpty() && !nodesVisited.isEmpty() && !nodesVisited.contains(graph.findNode(nodeFinish)))
		{
			System.out.println("===LOOP===");
			nodesToVisit.toString();
			nodesVisited.toString();
			System.out.println("Contains nodeFinish? " + nodesVisited.contains(graph.findNode(nodeFinish)));
			DSGNode current = nodesVisited.remove();
			LinkedList<DSGEdge> toAdd = current.getEdges();
			Collections.sort(toAdd, comp);
			for (DSGEdge e : toAdd) 
			{
				if (!nodesVisited.contains(e.getTo()))
				{
					nodesToVisit.addLast(e.getTo());
					parent.addLast(current);
				}
			}
		}
		
		if (nodesVisited.contains(graph.findNode(nodeFinish)))
		{
			StringBuffer result = new StringBuffer();
			DSGNode current = graph.findNode(nodeFinish);
			result.append(current.getValue() + " -> ");
			int index = nodesVisited.indexOf(nodeFinish);
			DSGNode currentParent = parent.get(index);
			while (current != graph.findNode(nodeFinish) && currentParent != null)
			{
				current = currentParent;
				index = nodesVisited.indexOf(nodeFinish);
				currentParent = parent.get(index);
				result.append(current.getValue() + " -> ");
			}
			return result.toString();
		}
		return null;
		
	}
	
	private DirectedStringGraph graph;
	private static LinkedList<DSGNode> allNodes;

}