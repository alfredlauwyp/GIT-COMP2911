import java.util.Comparator;


public class CourierDeliveryComparator implements Comparator<ProblemState> {
	
	public CourierDeliveryComparator(Heuristic heuristic) {
		this.heuristic = heuristic;
	}

	@Override
	public int compare(ProblemState p1, ProblemState p2) {
		int p1Estimate = p1.getDistanceToState() + heuristic.getEstimateToGoal(p1);
		int p2Estimate = p2.getDistanceToState() + heuristic.getEstimateToGoal(p2);
		
		int result = 0;
		if(p1Estimate < p2Estimate) {
			result = -1;
		} else if(p1Estimate > p2Estimate) {
			result = 1;
		} else {
			result = 0;
		}
		return result;
	}
	
	private Heuristic heuristic;
}
