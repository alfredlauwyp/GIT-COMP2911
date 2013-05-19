import java.util.LinkedList;

public interface ProblemState {
	
	public LinkedList<ProblemState> getChildren();
	public int getDistanceToState();
	public boolean isAnswer();
	public Node getNodeObj();
	public LinkedList<Node> getNodesRemaining();
	public void setTotalStatesRemoved(int statesRemoved);
	public ProblemState getParentState();
	@Override
	public String toString();
}
