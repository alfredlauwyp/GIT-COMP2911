import java.util.Scanner;
import java.io.FileReader;
import java.io.FileNotFoundException;

public class RoomBookingSystem /*Debug*/ extends Debug {

	public static void main(String[] args)
	{
		try
	    {
			Scanner userInput = new Scanner(new FileReader(args[0]));
			roomList = new RoomList();
			
			while (userInput.hasNextLine())
			{
				String currentLine = userInput.nextLine();
				int firstSpace = currentLine.indexOf(" ");
				
				if (firstSpace != -1) // indexOf(" ") returns -1 if character does not occur
				{
					String commandInput = currentLine.substring(0, firstSpace);
					String inputParameters[] = currentLine.substring(firstSpace + 1).split(" ");
					
					if (commandInput.equals(COMMAND_ADDBOOKING) && inputParameters.length == PARAMETERS_ADDBOOKING)
					{
						roomList.addBookings(inputParameters);
					}
					else if (commandInput.equals(COMMAND_CHANGEBOOKING) && inputParameters.length == PARAMETERS_CHANGEBOOKING)
					{
						roomList.changeBookings(inputParameters);
					}
					else if (commandInput.equals(COMMAND_REMOVEBOOKING) && inputParameters.length == PARAMETERS_REMOVEBOOKING)
					{
						roomList.removeBookings(inputParameters);
					}
					else if (commandInput.equals(COMMAND_INITIALISEROOM) && inputParameters.length == PARAMETERS_INITIALISEROOM)
					{
						roomList.initialiseRoom(inputParameters);
						/*Debug*/errs("FINAL: Total Rooms (" + roomList.size() + ")");
					}
					else if (commandInput.equals(COMMAND_PRINTROOMBOOKINGS) && inputParameters.length == PARAMETERS_PRINTROOM)
					{
						roomList.printRoomBookings(inputParameters);
					}
				}
			}
			userInput.close();
	    }
	    catch (FileNotFoundException e) {}
		
	}
	
	private static RoomList roomList;
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
