import java.util.ArrayList;
import java.util.Iterator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.Locale;

/**
 * RoomControl acts as the middle-man between a Room
 *  booking system and the class that carries out the operations
 *  on each individual room.
 *  
 * This class finds the appropriate rooms to make the add/change
 *  actions on. Conditional upon the success of methods called, this
 *  class is also responsible for returning relevant messages for
 *  the primary class to print out.
 *  
 * @author	Hayden Charles Smith, z3418003
 * 			Last modified: 20th April 2013
 */
public class RoomControl {

	/**
	 * Creates RoomControl object
	 */
	public RoomControl()
	{
		listOfRooms = new ArrayList<Room>();
	}
	
	/**
	 * Attempts to add booking to first available room
	 * @param parameters String array of parameters from user
	 * @return String containing message to return to user
	 */
	public String addBookings(String[] parameters)
	{
		// Establish meaning of each parameter
		String userName 	= parameters[0];
		int desiredCapacity = Integer.parseInt(parameters[1]);
		int numweeks 		= Integer.parseInt(parameters[2]);
		int month			= this.stringMonthToInt(parameters[3]);
		int date 			= Integer.parseInt(parameters[4]);
		int time 			= Integer.parseInt(parameters[5]);
		int duration 		= Integer.parseInt(parameters[6]);
		String title 		= parameters[7];
		
		boolean successfulBooking = false;
		String response = new String();
		
		for(Iterator<Room> i = listOfRooms.iterator(); (i.hasNext() && !successfulBooking);) {
			Room currentRoom = i.next();
			successfulBooking = currentRoom.attemptToAddBookings(desiredCapacity, userName, numweeks, month, date, time, duration, title);
			if (successfulBooking)
			{
				response = "Room " + currentRoom.getName() + " assigned";
			}
		}		
		if (!successfulBooking)
		{
			response = "Booking rejected";
		}
		return response;
	}
	
	/**
	 * Attempts to change a booking to first newly available room
	 * @param parameters String array of parameters from user
	 * @return String containing message to return to user
	 */
	public String changeBookings(String[] parameters)
	{
		// Establish meaning of each parameter
		String userName = parameters[0];
		String roomName = parameters[1];
		int numweeks 	= Integer.parseInt(parameters[2]);
		int month1 		= this.stringMonthToInt(parameters[3]);
		int date1 		= Integer.parseInt(parameters[4]);
		int time1 		= Integer.parseInt(parameters[5]);
		int capacity 	= Integer.parseInt(parameters[6]);
		int month2 		= this.stringMonthToInt(parameters[7]);
		int date2 		= Integer.parseInt(parameters[8]);
		int time2 		= Integer.parseInt(parameters[9]);
		int duration2	= Integer.parseInt(parameters[10]);
		String title 	= parameters[11];
		
		Room currentRoom = getRoomByName(roomName);		
		String response = new String();
		boolean successfulChange = false;
		
		if (currentRoom != null)
		{			
			Room newRoom = currentRoom;		
			for(Iterator<Room> i = listOfRooms.iterator(); (i.hasNext() && !successfulChange);) {
				newRoom = i.next();
				successfulChange = currentRoom.attemptToChangeBookings(newRoom, userName, roomName, numweeks, month1, date1, time1, capacity, month2, date2, time2, duration2, title);
				if (successfulChange)
				{
					response = "Room " + newRoom.getName() + " assigned";
				}
			}
		}
		if (!successfulChange)
		{
			response = "Change rejected";
		}	
		return response;
	}
	
	/**
	 * Attempts to delete a booking
	 * @param parameters String array of parameters from user
	 * @return String containing message to return to user
	 */
	public String removeBookings(String[] parameters)
	{
		// Establish meaning of each parameter
		String userName = parameters[0];
		String roomName = parameters[1];
		int numweeks 	= Integer.parseInt(parameters[2]);
		int month 		= this.stringMonthToInt(parameters[3]);
		int date 		= Integer.parseInt(parameters[4]);
		int time 		= Integer.parseInt(parameters[5]);
		
		String response = new String();
		Room selectedRoom = this.getRoomByName(roomName);
		boolean successfulDelete = false;
		if (selectedRoom != null)
		{
			successfulDelete = selectedRoom.attemptToRemoveBookings(userName, numweeks, month, date, time);
		}
		if (successfulDelete)
		{
			response = "Reservations deleted";
		}
		else
		{
			response = "Deletion rejected";
		}
		return response;
	}
	
	/**
	 * Get a printout of the booking list for a particular room
	 * @param parameters String array of parameters from user
	 * @return String containing bookingList to return to user
	 */
	public String printRoomBookings(String[] parameters)
	{		
		// Establish meaning of each parameter
		String roomName = parameters[0];
		
		Room selectedRoom = getRoomByName(roomName);
		String bookingListStr = selectedRoom.printBookingList();
		return bookingListStr;
	}
	
	/** 
	 * Initialise a new room within the system
	 * @param parameters String array of parameters from user
	 * @return String containing bookingList to return to user
	 */
	public String initialiseRoom(String[] parameters)
	{	
		// Establish meaning of each parameter
		int capacity		 	= Integer.parseInt(parameters[0]);
		String roomName 		= parameters[1];
		
		Room newRoom = new Room(capacity, roomName);
		listOfRooms.add(newRoom);
		
		return new String(""); // return no message
	}
	
	/**
	 * Given a month in string format, return its corresponding integer value
	 * @param monthString A month of the year represented as 3 characters
	 * @return month of the year represented as an integer
	 */
	private int stringMonthToInt(String monthString)
	{
		try
		{
			SimpleDateFormat t = new SimpleDateFormat(DATE_STRING_FORMAT, Locale.ENGLISH);
			Date date = t.parse(monthString);
			Calendar cal = Calendar.getInstance();
		    cal.setTime(date);
		    return cal.get(Calendar.MONTH);
		}
		catch (ParseException e)
		{
			System.err.println("ParseException (month conversion): " + e.getMessage());
		}
		return -1;	    
	}
		
	/**
	 * Given the name of a particular room, return the object of said room
	 * @param roomNameWanted Name associated with particular room desired
	 * @return Room associated with the particular name
	 */
	private Room getRoomByName(String roomNameWanted)
	{
		for(Iterator<Room> i = listOfRooms.iterator(); i.hasNext();) {
			Room item = i.next();
			if (item.getName().equals(roomNameWanted))
			{
				return item;
			}
		}
		return null;
	}
	
	static private ArrayList<Room> listOfRooms;
	private String DATE_STRING_FORMAT = "MMM";
}
