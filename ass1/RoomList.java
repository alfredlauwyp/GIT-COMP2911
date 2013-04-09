import java.util.ArrayList;
import java.util.Iterator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.Locale;

public class RoomList /*Debug*/ extends Debug {

	public RoomList()
	{
		listOfRooms = new ArrayList<Room>();
	}
	
	public void addBookings(String[] parameters)
	{
		
		String userName = parameters[0];
		int desiredCapacity = Integer.parseInt(parameters[1]);
		int numweeks 		= Integer.parseInt(parameters[2]);
		int month			= this.stringMonthToInt(parameters[3]);
		int date 			= Integer.parseInt(parameters[4]);
		int time 			= Integer.parseInt(parameters[5]);
		int duration 		= Integer.parseInt(parameters[6]);
		String title 		= parameters[7];
		
		/*Debug*/errs("ATTEMPT: Room Book (" + userName + ") (" + desiredCapacity + " people) (" + numweeks + " wks) (" + month + " MM) (" + date + " DD) (" + time + " H) (" + duration + " Dur) (" + title + " Name)");
		
		boolean success = false;
		for(Iterator<Room> i = listOfRooms.iterator(); (i.hasNext() && !success);) {
			Room currentRoom = i.next();
			/*Debug*/err("Going into: attemptToAddBookings");
			success = currentRoom.attemptToAddBookings(desiredCapacity, userName, numweeks, month, date, time, duration, title);
			/*Debug*/err("Found available room? (" + success + ") (" + currentRoom.getName() + " name) (" + currentRoom.getCapacity() + " ActSize) (" + desiredCapacity + " WantedSize)");
			if (success)
			{
				this.speak("Room " + currentRoom.getName() + " assigned");
			}
		}
		if (!success)
		{
			this.speak("Booking rejected");
		}
	}
		
	public void changeBookings(String[] parameters)
	{
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
		
		Room selectedRoom = getRoomByName(roomName);		
		boolean successfulChange = selectedRoom.attemptToChangeBookings(capacity, userName, roomName, numweeks, month1, date1, time1, capacity, month2, date2, time2, duration2, title);
		if (successfulChange)
		{
			this.speak("Room " + selectedRoom.getName() + " assigned");
		}
		else
		{
			this.speak("Booking rejected");
		}	
	}
	
	public void removeBookings(String[] parameters)
	{
		String userName = parameters[0];
		String roomName = parameters[1];
		int numweeks 	= Integer.parseInt(parameters[2]);
		int month 		= this.stringMonthToInt(parameters[3]);
		int date 		= Integer.parseInt(parameters[4]);
		int time 		= Integer.parseInt(parameters[5]);
		
		Room selectedRoom = getRoomByName(roomName);
		boolean successfulDelete = selectedRoom.attemptToRemoveBookings(userName, numweeks, month, date, time);
		if (successfulDelete)
		{
			this.speak("Reservations deleted");
		}
		else
		{
			this.speak("Deletion rejected");
		}
	}
	
	public void printRoomBookings(String[] parameters)
	{		
		String roomName = parameters[0];
		Room selectedRoom = getRoomByName(roomName);
		String bookingList = selectedRoom.printBookingList();
		this.speak(bookingList);
	}
	
	public void initialiseRoom(String[] parameters)
	{	
		int capacity		 	= Integer.parseInt(parameters[0]);
		String roomName 		= parameters[1];
		/*Debug*/err("ATTEMPT: Room Added (" + capacity + ") (" + roomName + ")");
		
		Room newRoom = new Room(capacity, roomName);
		listOfRooms.add(newRoom);
		/*Debug*/err("SUCCESS: Room Added (" + capacity + ") (" + roomName + ")");
	}
	
	/*Debug*/public int size(){return listOfRooms.size();}

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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;	    
	}
	
	private void speak(String output)
	{
		System.out.println(output);
	}
	
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
