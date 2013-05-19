import java.util.LinkedList;


public class CourierDeliveryProblemState implements ProblemState {

	public CourierDeliveryProblemState(int distance, Node job, CourierDeliveryProblemState parent, LinkedList<Node> jobsRemaining) {
		this.distanceToState = distance;
		this.statesRemovedFromQueue = -1; 
		this.job = job;
		this.parent = parent;
		this.jobsRemaining = jobsRemaining;
		if(job != null) {
			jobsRemaining.remove(job);
			distanceToState += job.getManhattenDistance();
		}
	}

	@Override
	public LinkedList<ProblemState> getChildren() {
		
		LinkedList<ProblemState> children = new LinkedList<ProblemState>();

		for(Node nextJob : jobsRemaining) {
			@SuppressWarnings("unchecked")
			LinkedList<Node> newJobsRemaining = (LinkedList<Node>) jobsRemaining.clone();
			int newDistance = distanceToState;
			if(job != null) {
				newDistance += job.getTravelDistance(nextJob);
			} else {
				newDistance += nextJob.getXStart() + nextJob.getYStart();
			}
			ProblemState child = new CourierDeliveryProblemState(newDistance, nextJob, this, newJobsRemaining);
			children.add(child);
		}
		return children;
	}

	@Override
	public boolean isAnswer() {
		return jobsRemaining.size() == 0; 
	}
	
	public void setTotalStatesRemoved(int statesRemoved) {
		this.statesRemovedFromQueue = statesRemoved;
	}


	@Override
	public String toString() {
		String string =   statesRemovedFromQueue + " nodes explored";
		string = string + "\n";
		string = string + "cost = " + distanceToState;
		string = string + "\n";
	
		string = string + printParents(this, this);
		string = string + " " + this.getNodeObj().getXStart() + " " + this.getNodeObj().getYStart();
		string = string + "\n";
		string = string + ("Carry from " + this.getNodeObj().getXStart() + " " + this.getNodeObj().getYStart() + " to "  + this.getNodeObj().getXFinish() + " " + this.getNodeObj().getYFinish());
		return string;
		
	}	
	
	public String printParents(ProblemState state, ProblemState initialState) {
		String string = "";
		if(state.getParentState() == null) {
			string = string + ("Move from 0 0 to ");
		} else {
			state = state.getParentState();
			string = string + printParents(state, initialState);
			if(state != null) {
				if(state.getNodeObj() != null) {
					string = string + (state.getNodeObj ().getXStart() + " " + state.getNodeObj().getYStart());
					string = string + "\n";
					string = string + ("Carry from " + state.getNodeObj().getXStart() + " " + state.getNodeObj().getYStart() + " to "  + state.getNodeObj().getXFinish() + " " + state.getNodeObj().getYFinish());
					string = string + "\n";
					string = string + ("Move from "+ state.getNodeObj().getXFinish() + " " + state.getNodeObj().getYFinish() + " to");
				}
			}
		}
		return string;
	}
	
	@Override
	public int getDistanceToState() {
		return distanceToState;
	}
	
	public LinkedList<Node> getNodesRemaining() {
		return jobsRemaining;
	}
	
	public int getNumberOfJobsRemaining() {
		return jobsRemaining.size();
	}
	
	public Node getNodeObj() {
		return job;
	}
	
	public ProblemState getParentState() {
		return parent;
	}
		
	public String getCarryString() {
		String string = "";
		if(job != null) {
			string =  "Carry from " + job.getXStart() + " " + job.getYStart() + " to " + job.getXFinish() + " " + job.getYFinish();
		}
		return string;
	}

	private int distanceToState;
	private int statesRemovedFromQueue;
	private Node job;
	private ProblemState parent;
	private LinkedList<Node> jobsRemaining;
}
