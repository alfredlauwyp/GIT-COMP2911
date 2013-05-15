import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Comparator;
import java.util.Scanner;

public class CourierDelivery
{
	public static void main(String[] args)
	{
		try
	    {
			Scanner userInput = new Scanner(new FileReader(args[0]));
			graph = new AdjListGraph<DualPoint>();
			
			JobPoint initialJobPoint = new JobPoint(0, 0, 0, 0);
			graph.addNode(initialJobPoint);
			
			while (userInput.hasNextLine())
			{
				String input[] = userInput.nextLine().split(" ");
				int fromX = Integer.parseInt(input[COODINATE_FROM_X]);
				int fromY = Integer.parseInt(input[COODINATE_FROM_Y]);
				int toX =	Integer.parseInt(input[COODINATE_TO_X]);
				int toY = 	Integer.parseInt(input[COODINATE_TO_Y]);
				graph.addNode(new JobPoint(fromX, fromY, toX, toY));
			}
			userInput.close();
			graph.connectAllNodes();
			
			Comparator<AsearchNode> comparator = new Comparator<AsearchNode>() {
			    public int compare(AsearchNode e1, AsearchNode e2) { 
			    	if (e1.getExternalDistanceTravelled() > e2.getExternalDistanceTravelled()) return 1;
			    	if (e1.getExternalDistanceTravelled() < e2.getExternalDistanceTravelled()) return -1;
			    	else return 0;
			    }
			};
			Asearch asearch = new Asearch(graph);
			//PrintGraph print = new PrintGraph(graph);
			speak("Thinking...");
			speak(asearch.findPath(initialJobPoint, comparator));			
			
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
	
	static AdjListGraph<DualPoint> graph = new AdjListGraph<DualPoint>();
	private static final int COODINATE_FROM_X = 1;
	private static final int COODINATE_FROM_Y = 2;
	private static final int COODINATE_TO_X = 4;
	private static final int COODINATE_TO_Y = 5;

}


