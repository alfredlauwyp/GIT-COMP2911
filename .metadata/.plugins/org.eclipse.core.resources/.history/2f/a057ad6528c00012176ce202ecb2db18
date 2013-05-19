import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

public class CourierDeliveryHeuristic implements Heuristic {
	
	public CourierDeliveryHeuristic(LinkedList<Node> jobs) {
		shortestPaths = new int[jobs.size()];
		LinkedList<Integer> pathLengths = new LinkedList<Integer>();
		for(Node job : jobs) {
			for(Node path : jobs) {
				if(!path.equals(job)) {
					int dist = job.getTravelDistance(path);
					//System.out.println("distance from " + job.getXStart() + ", " + job.getYStart() + " to " + path.getXStart() + ", " + path.getYStart() + " is " + dist);
					pathLengths.add(dist);
				}
			}
		}
		Comparator<Integer> descending = new Comparator<Integer>() {
				public int compare(Integer path1, Integer path2) {
					int result = 0;
					if(path1 < path2) {
						result = -1;
					} if (path2 < path1) {
						result = 1;
					} 
					return result;
				}
		};
		Collections.sort(pathLengths);
		int counter = 0;
		for(counter = 0; counter < jobs.size(); counter++) {
			shortestPaths[counter] = pathLengths.get(counter);
		}
		//System.out.println("Printing the shortest paths");
		for(int i = 0; i < counter; i++) {
			//System.out.println(shortestPaths[i]);
		}
	}

	@Override
	public int getEstimateToGoal(ProblemState state) {
		CourierDeliveryProblemState current = (CourierDeliveryProblemState) state;
		int hValue = 0;
		//System.out.println("contribution from shortest paths is " + hValue);
		int counter = 0;
		int paths = 0;
		int jobs = 0;
		for(Node job : current.getNodesRemaining()) {
			paths += shortestPaths[counter];
			jobs += job.getManhattenDistance();
			counter++;
		}
		hValue = paths + jobs;
		//System.out.println("contribution from jobs is " + jobs + ", total from paths is " + paths);
		//return hValue;
		return 0;
	}
	
	private int[] shortestPaths;
}
