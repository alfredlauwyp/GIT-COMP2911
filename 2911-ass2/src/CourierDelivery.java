import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Comparator;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.LinkedList;

public class CourierDelivery
{
	public static void main(String[] args)
	{
		try
	    {
			Scanner userInput = new Scanner(new FileReader(args[0]));
			graph = new AdjListGraph<JobPoint>();
			
			JobPoint initialJobPoint = new JobPoint(0, 0, 0, 0);
			graph.addNode(initialJobPoint);
			
			while (userInput.hasNextLine())
			{
				String input[] = userInput.nextLine().split(" ");
				int fromX = Integer.parseInt(input[COODINATE_FROM_X]);
				int fromY = Integer.parseInt(input[COODINATE_FROM_Y]);
				int toX = Integer.parseInt(input[COODINATE_TO_X]);
				int toY = Integer.parseInt(input[COODINATE_TO_Y]);
				graph.addNode(new JobPoint(fromX, fromY, toX, toY));
			}
			userInput.close();
			graph.connectAllNodes();
			
			Comparator<AsearchNode<JobPoint>> comparator = new Comparator<AsearchNode<JobPoint>>() {
			    public int compare(AsearchNode e1, AsearchNode e2) { 
			    	if (e1.getDistanceTravelled() > e2.getDistanceTravelled()) return 1;
			    	if (e1.getDistanceTravelled() < e2.getDistanceTravelled()) return -1;
			    	else return 0;
			    }
			};
			Asearch asearch = new Asearch(graph);
			LinkedList<JobPoint> path = asearch.findPath(initialJobPoint, comparator);
			speak(path.size() + " nodes explored\n");
			JobPoint initial = path.get(0);
			for (int i = 1; i < path.size(); i++)
			{
				speakln("Move from " + path.get(i - 1).getToX() + " " + path.get(i - 1).getToY() + " to " + path.get(i).getToX() + " " + path.get(i).getToY());
				speakln("Carry from " + path.get(i - 1).getToX() + " " + path.get(i - 1).getToY() + " to " + path.get(i).getToX() + " " + path.get(i).getToY());
			}
			
	    }
	    catch (FileNotFoundException e)
	    {
	    	speakln("FileNotFoundException: " + e.getMessage());
	    }
		catch (ArrayIndexOutOfBoundsException e)
		{
			speakln("Please add a single parameter that is an input file");
		}
		
	}
	
	private static void speakln(String printout)
	{
		speak(printout + "\n");
	}
	
	private static void speak(String printout)
	{
		if (!printout.equals(""))
		{
			System.out.print(printout);
		}
	}
	
	static AdjListGraph<JobPoint> graph = new AdjListGraph<JobPoint>();
	private static final int COODINATE_FROM_X = 1;
	private static final int COODINATE_FROM_Y = 2;
	private static final int COODINATE_TO_X = 4;
	private static final int COODINATE_TO_Y = 5;

}

