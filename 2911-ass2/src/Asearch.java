import java.util.LinkedList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.ArrayList;

/**
 * A* Search Class.
 * 
 * Allows for searching of a given DirectedGraph with
 *  an A* search - by using a comparator in order
 *  to give weighting to particular state spaces over
 *  one another.
 *  
 * Please note this A* Search is based on the 
 *  DualNode class
 * 
 * @author	Hayden Charles Smith, z3418003
 * 			Last modified: 19th May 2013
 */
public class Asearch {

	/**
	 * Construct an Asearch Object
	 * @param graph Graph containing DualPoint's that need to 
	 *  be searched
	 */
	public Asearch(DirectedGraph<DualPoint> graph)
	{
		this.graph = graph;
		heuristic = new CourierDeliveryHeuristic(graph);
	}
	
	/**
	 * This method finds a path from the initial DualPoint passed
	 *  in to a final DualPoint, while passing through every other
	 *  DualPoint.
	 * @param initialPoint DualPoint in which to start from
	 * @param comp Comparator which orders items added to PriorityQueue.
	 *  This comparator is of generic type SearchNode<DualPoint>
	 * @return LinkedList of DualPoint's that make up the path 
	 */
	public LinkedList<DualPoint> findMinimalSpanningPath(DualPoint initialPoint, Comparator<SearchNode<DualPoint>> comp)
	{
		// Establish path to take
		LinkedList<DualPoint> result = new LinkedList<DualPoint>();
		PriorityQueue<SearchNode<DualPoint>> priorityQueue = new PriorityQueue<SearchNode<DualPoint>>(INITIAL_QUEUE_CAPACITY, comp);
		int explored = 0;
		if (graph.getNumNodes() > 0)
		{
			SearchNode<DualPoint> current = new AsearchNode<DualPoint>(initialPoint, 0);
			
			priorityQueue.add(current);
			
			boolean visitedAll = false;
			
			ArrayList<DualPoint> neighbours = null;
			
			while (!priorityQueue.isEmpty() && !visitedAll)
			{
				current = priorityQueue.poll();
				explored++;
				
				current.addVisited(current);
				neighbours = graph.getNeighbours(current.getNodeObj());
			    for(DualPoint currentNeighbour : neighbours)
			    {
			    	if (!current.hasVisitedObj(currentNeighbour))
			    	{
				    	int distanceDifference = current.getNodeObj().getExternalDistanceTo(currentNeighbour);
				    		
				    	int travelled = (current.getExternalDistanceTravelled() + distanceDifference);
				    	SearchNode<DualPoint> nodeToAdd = new AsearchNode<DualPoint>(currentNeighbour, travelled);
				    		
			    		for(SearchNode<DualPoint> alreadyVisited : current.getNodesVisited())
			    		{
			    			nodeToAdd.addVisited(alreadyVisited);
			    		}
			    		nodeToAdd.setEstimatedDistanceRemaining(heuristic.getEstimate(nodeToAdd));
			    		priorityQueue.add(nodeToAdd);
			    	}
			    }
				  
			    if (current.getNumNodesVisited() >= this.graph.getNumNodes())
				{
					visitedAll = true;
				}
			}
			if(current.getNumNodesVisited() == this.graph.getNumNodes()) {
				result = current.getNodeObjsVisited();
			}			 
		}
		this.nodesExplored = explored;
		return result;		
	}
	
	/**
	 * Return the number of nodes that have been
	 *  explored in the search
	 * @return Number of nodes that have been explored in the
	 *  search
	 */
	public int getNumNodesExplored()
	{
		return this.nodesExplored;
	}
		
	private int nodesExplored;
	private DirectedGraph<DualPoint> graph;
	private static final int INITIAL_QUEUE_CAPACITY = 100;
	private Heuristic<SearchNode<DualPoint>> heuristic;

}
