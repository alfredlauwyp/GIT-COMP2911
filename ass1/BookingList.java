import java.util.ArrayList;
import java.util.Iterator;

public class BookingList {

	public BookingList()
	{
		listOfBookings = new ArrayList<Booking>();
	}
	
	// add booking
	//	is free space (check each week)
	//  is no time clash (compare times / dates)
	// change booking
	//	is free space (check each week)
	//  is no time clash (compare times / dates)
	//  NEW: month, date, time, duration
	// delete booking
	
	
	public boolean addBooking(String roomName, int numWeeks, int month, int date, int time, int duration, String title)
	{
		boolean allFree = true;
		for (int i = 0; i < numWeeks; i++)
		{
			if (isFreeSpace(roomName, month, date + (daysInWeek * i), time, duration))
			{
				allFree = false;
			}
		}
		if (allFree)
		{
			for (int i = 0; i < numWeeks; i++)
			{
				Booking newBooking = new Booking(roomName, month, date + numWeeks, time, duration, title);
				listOfBookings.add(newBooking);
			}
			return true;
		}
		else
		{
			return false;
		}				
	}
	
	
	
	private ArrayList<Booking> listOfBookings;
	private static final int daysInWeek = 7; 
}
