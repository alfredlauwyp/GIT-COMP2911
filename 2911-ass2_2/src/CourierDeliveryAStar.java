import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;


public class CourierDeliveryAStar extends AStar {
	
	public CourierDeliveryAStar(Heuristic heuristic, LinkedList<Node> jobs) {
		super(heuristic);
		this.jobs = jobs;
	}

	public ProblemState getStartState() {
		ProblemState start = new CourierDeliveryProblemState(0, null, null, jobs);
		return start;
	}
	
	public Comparator<ProblemState> getComparator() {
		Comparator<ProblemState> comparator = new CourierDeliveryComparator(super.heuristic);
		return comparator;
	}
	
	/**
	 * Returns the initial capacity of the priority queue.
	 */
	public int getInitialCapacity() {
		return INITIAL_QUEUE_CAPACITY;
	}
	
	public boolean isAnswer(ProblemState state) {
		return state.isAnswer();
	}
	
	public LinkedList<ProblemState> getChildren(ProblemState state) {
		return state.getChildren();
	}
	
	public boolean prunedState(ProblemState state, ArrayList<ProblemState> visited) {
		
		boolean pruned = false;
		if (visited.size() > 0)
		{
			for (ProblemState discovered : visited)
			{
				if (state.getNodeObj() != null && discovered.getNodeObj() != null) {
					if (state.getNodeObj().getXStart() == discovered.getNodeObj().getXStart() &&
								state.getNodeObj().getYStart() == discovered.getNodeObj().getYStart() &&
								state.getNodeObj().getXFinish() == discovered.getNodeObj().getXFinish() &&
								state.getNodeObj().getYFinish() == discovered.getNodeObj().getYFinish())
					{
						boolean same = true;
						for(Node job : state.getNodesRemaining())
						{
							if(!(discovered.getNodesRemaining().contains(job)))
							{
								same = false;
							}
						}
						for(Node job : discovered.getNodesRemaining())
						{
							if(!(state.getNodesRemaining().contains(job)))
							{
								same = false;
							}
						}
						if(same == true) {
							pruned = true;
						}
					}
				}
			}
		}
		return pruned;
	}
	

	private static final int INITIAL_QUEUE_CAPACITY = 20;
	private LinkedList<Node> jobs;
	
}
