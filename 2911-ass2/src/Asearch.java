import java.util.LinkedList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.lang.StringBuffer;
import java.util.ArrayList;

/**
 * Implements Breadth First Search Algorithm
 * @author Hayden Smith
 *
 */
public class Asearch<E> {

	@SuppressWarnings("unchecked")
	public Asearch(Graph graph)
	{
		this.graph = graph;
		this.allNodes = graph.getNodes();
	}
	
	public LinkedList<E> findPath(E initialNodeObj, Comparator<AsearchNode<E>> comp)
	{
		// Establish path to take
		LinkedList<E> path = new LinkedList<E>();
		PriorityQueue<AsearchNode<E>> priorityQueue = new PriorityQueue<AsearchNode<E>>(INITIAL_QUEUE_CAPACITY, comp);
		
		if (allNodes.size() > 0)
		{
			AsearchNode<E> current = new AsearchNode<E>(initialNodeObj, 0);
			priorityQueue.add(current);
			
			boolean visitedAll = false;
			
			ArrayList<E> neighbours = null;
			
			while (!priorityQueue.isEmpty() && !visitedAll)
			{
				current = priorityQueue.poll();
				if (current.getNumNodesVisited() < this.graph.getNumVertices())
				{
					current.addVisited(current);
				    
					neighbours = graph.getNeighbours(current.getNodeObj());
				    for(E currentNeighbour : neighbours)
				    {
				    	if (!current.hasVisited(currentNeighbour))
				    	{
				    		JobPoint temp = (JobPoint)current.getNodeObj();
				    		int distanceDifference = temp.getExternalDistanceTo((JobPoint)currentNeighbour);
				    		
				    		int travelled = (current.getDistanceTravelled() + distanceDifference);
							AsearchNode<E> nodeToAdd = new AsearchNode(currentNeighbour, travelled);
				    		
				    		for(AsearchNode<E> alreadyVisited : current.getNodesVisited())
				    		{
				    			nodeToAdd.addVisited(alreadyVisited);
				    		}
				    		priorityQueue.add(nodeToAdd);
				    	}
				    }
				}
				else
				{
					visitedAll = true;
				}
			}
			
			if(current.getNumNodesVisited() == this.graph.getNumVertices()) {
				path = current.getNodeObjsVisited();
			}			 
		}
		return path;		
	}
		
	private Graph graph;
	private ArrayList<ArrayList<E>> allNodes;
	private static final int INITIAL_QUEUE_CAPACITY = 100;

}