import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.LinkedList;

public class Solver {

	public static void main(String[] args) {
		readData(args);
		LinkedList<Node> jobs = graph.getNodes();
		Heuristic heuristic = new CourierDeliveryHeuristic(jobs);
		AStar search = new CourierDeliveryAStar(heuristic, jobs);
		System.out.println(search.findSolution());
		System.out.println("Prunes: " + Solver.prunes);
	}
	
	private static void readData(String[] args) {
		graph = new CourierDeliveryGraph();
		try {
			Scanner sc = new Scanner(new FileReader(args[0]));
			while(sc.hasNextLine()) {
				if(sc.hasNext()) {
					addJob(sc, graph);
				} else {
					break;
				}
			}
		} catch (FileNotFoundException e) {}
	}
	
	private static void addJob(Scanner sc, Graph graph) {
		ArrayList<Integer> jobCoords = new ArrayList<Integer>();
		sc.next();
		jobCoords.add(sc.nextInt());
		jobCoords.add(sc.nextInt());
		sc.next();
		jobCoords.add(sc.nextInt());
		jobCoords.add(sc.nextInt());
		graph.addNode(graph.createNode(jobCoords));
	}
	
	private static CourierDeliveryGraph graph;
	public static int prunes;
}
