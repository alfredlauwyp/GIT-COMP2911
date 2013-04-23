import java.util.Scanner;
import java.io.FileReader;
import java.io.FileNotFoundException;

/**
 * RoomBookingSystem serves as the "back-end" of a room booking system where
 *  users can make, change and delete booking reservations
 *  
 * This particular class deals with reading user information/instructions
 *  from standard input, error checking said input, calling the appropriate
 *  method, and printing what the method returns.
 * 
 * @author	Hayden Charles Smith, z3418003
 * 			Last modified: 20th April 2013
 */
public class RoomBookingSystem
{
	/**
	 * Main function for RoomBookingSystem
	 * @param args data passed in from standard input
	 */
	public static void main(String[] args)
	{
		try
	    {
			Scanner userInput = new Scanner(new FileReader(args[0]));
			roomControl = new RoomControl();
			
			while (userInput.hasNextLine())
			{
				String currentLine = userInput.nextLine();
				int firstSpace = currentLine.indexOf(" ");
				
				if (firstSpace != -1) // indexOf(" ") returns -1 if character does not occur
				{
					// Get the first parameter from standard input command
					String commandInput = currentLine.substring(0, firstSpace);
					
					// Get all remaining parameters from standard input
					String inputParameters[] = currentLine.substring(firstSpace + 1).split(" ");
					
					// Make call to appropriate method based on
					//  commandInput and number of parameters given
					String systemResponse = new String();
					if (commandInput.equals(COMMAND_ADDBOOKING) && inputParameters.length == PARAMETERS_ADDBOOKING)
					{
						systemResponse = roomControl.addBookings(inputParameters);
					}
					else if (commandInput.equals(COMMAND_CHANGEBOOKING) && inputParameters.length == PARAMETERS_CHANGEBOOKING)
					{
						systemResponse = roomControl.changeBookings(inputParameters);
					}
					else if (commandInput.equals(COMMAND_REMOVEBOOKING) && inputParameters.length == PARAMETERS_REMOVEBOOKING)
					{
						systemResponse = roomControl.removeBookings(inputParameters);
					}
					else if (commandInput.equals(COMMAND_INITIALISEROOM) && inputParameters.length == PARAMETERS_INITIALISEROOM)
					{
						systemResponse = roomControl.initialiseRoom(inputParameters);
					}
					else if (commandInput.equals(COMMAND_PRINTROOMBOOKINGS) && inputParameters.length == PARAMETERS_PRINTROOM)
					{
						systemResponse = roomControl.printRoomBookings(inputParameters);
					}
					
					if (!systemResponse.isEmpty())
					{
						speak(systemResponse);
					}
				}
			}
			userInput.close();
	    }
	    catch (FileNotFoundException e)
	    {
	    	System.err.println("FileNotFoundException: " + e.getMessage());
	    }
		catch (ArrayIndexOutOfBoundsException e)
		{
			System.err.println("Please add a single parameter that is an input file");
		}
		
	}
	
	/**
	 * Prints a given non-empty string to System output
	 * @param printout String value of what to print out
	 */
	private static void speak(String printout)
	{
		if (!printout.equals(""))
		{
			System.out.println(printout);
		}
	}
	
	private static RoomControl roomControl;
	
	private static String COMMAND_ADDBOOKING = "Book";
	private static String COMMAND_CHANGEBOOKING = "Change";
	private static String COMMAND_REMOVEBOOKING = "Delete";
	private static String COMMAND_INITIALISEROOM = "Room";
	private static String COMMAND_PRINTROOMBOOKINGS = "Print";
	
	private static final int PARAMETERS_ADDBOOKING = 8;
	private static final int PARAMETERS_CHANGEBOOKING = 12;
	private static final int PARAMETERS_REMOVEBOOKING = 6;
	private static final int PARAMETERS_INITIALISEROOM = 2;
	private static final int PARAMETERS_PRINTROOM = 1;
	
}
