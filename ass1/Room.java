import java.util.LinkedList;
import java.util.Iterator;
import java.lang.StringBuffer;

import java.util.Calendar;

public class Room extends Debug {

	public Room(int capacity, String name)
	{
		this.capacity = capacity;
		this.name = name;
				
		listOfBookings = new LinkedList<Booking>();

	}
	
	public boolean attemptToRemoveBookings(String userName, int numweeks, int month, int date, int time)
	{
		if (this.conflictStatus(userName, numweeks, month, date, time, DEFAULT_OVERLAP_DURATION) == CONFLICTSTATUS_OVERLAP_ALL)
		{
			this.removeBookings(numweeks, month, date, time);
			return true;
		}
		return false;
	}
	
	public boolean attemptToAddBookings(int desiredCapacity, String userName, int numweeks, int month, int date, int time, int duration, String title)
	{
		///*Debug*/err("Checking room? (capacity? " + this.hasCapacity(desiredCapacity) + ") (Conflict Status? " + this.conflictStatus("", numweeks, month, date, time, duration) + ", want " + CONFLICTSTATUS_OVERLAP_NONE + ")");
		/*Debug*/err("Checking room " + this.getName() + ": (Conflict Status needs 0. Is(" + this.conflictStatus("", numweeks, month, date, time, duration) + "))");
		if (this.hasCapacity(desiredCapacity) && this.conflictStatus("", numweeks, month, date, time, duration) == CONFLICTSTATUS_OVERLAP_NONE)
		{
			err("HELLO THERE OMG!!");
			this.addBookings(userName, numweeks, month, date, time, duration, title);
			return true;
		}
		return false;
	}

	public boolean attemptToChangeBookings(int desiredCapacity, String userName, String roomName, int numweeks, int month1, int date1, int time1, int capacity, int month2, int date2, int time2, int duration2, String title)
	{	
		if (
		 this.conflictStatus(userName, numweeks, month1, date1, time1, DEFAULT_OVERLAP_DURATION) == CONFLICTSTATUS_OVERLAP_ALL &&
		 this.hasCapacity(desiredCapacity) &&
		 this.conflictStatus(userName, numweeks, month2, date2, time2, duration2) == CONFLICTSTATUS_OVERLAP_NONE
		)
		{
			this.removeBookings(numweeks, month1, date1, time1);
			this.addBookings(userName, numweeks, month2, date2, time2, duration2, title);
			return true;
		}		
		return false;
	}
	
	public void removeBookings(int numweeks, int month, int date, int time)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH,  month);
		calendar.set(Calendar.DAY_OF_MONTH,  date);
		for (int i = 0; i < numweeks; i++)
		{
			calendar.add(Calendar.DAY_OF_MONTH, DAYS_IN_WEEK);
			for(Iterator<Booking> j = listOfBookings.iterator(); j.hasNext(); ) {
				Booking item = j.next();
				if (item.getMonth() == calendar.get(Calendar.MONTH) && item.getDate() == calendar.get(Calendar.HOUR) && item.getTime() == time)
				{
					listOfBookings.remove(item);
					break;
				}
			}
		}
	}
	
	public void addBookings(String userName, int numweeks, int month, int date, int time, int duration, String title)
	{
		Calendar newItemCalendar = Calendar.getInstance();
		Calendar currentItemCalendar = Calendar.getInstance();
		newItemCalendar.set(Calendar.YEAR, DEFAULT_CALENDAR_YEAR);
		newItemCalendar.set(Calendar.MONTH,  month);
		newItemCalendar.set(Calendar.DAY_OF_MONTH,  date);
		newItemCalendar.set(Calendar.HOUR,  time);
		Boolean addedForThisWeek;
		
		for (int i = 0; i < numweeks; i++)
		{
			addedForThisWeek = false;
			newItemCalendar.add(Calendar.DAY_OF_MONTH, DAYS_IN_WEEK);
			for(Iterator<Booking> j = listOfBookings.iterator(); j.hasNext(); ) {
				Booking item = j.next();
				currentItemCalendar.set(Calendar.YEAR, DEFAULT_CALENDAR_YEAR);
				currentItemCalendar.set(Calendar.MONTH, item.getMonth());
				currentItemCalendar.set(Calendar.DAY_OF_MONTH, item.getDate());
				currentItemCalendar.set(Calendar.HOUR, item.getTime());
				
				if (newItemCalendar.before(currentItemCalendar))
				{
					Booking newBooking = new Booking(userName, newItemCalendar.get(Calendar.MONTH), newItemCalendar.get(Calendar.DAY_OF_MONTH), time, duration, title);
					listOfBookings.add(newBooking);
					addedForThisWeek = true;
				}
			}
			if (!addedForThisWeek)
			{
				Booking newBooking = new Booking(userName, newItemCalendar.get(Calendar.MONTH), newItemCalendar.get(Calendar.DAY_OF_MONTH), time, duration, title);
				listOfBookings.add(newBooking);
			}
		}		
	}
		
	private boolean isTimeClash(int time1, int duration1, int time2, int duration2)
	{
		for (int i = time1; i < (time1 + duration1); i++)
		{
			for (int j = time2; j < (time2 + duration2); j++)
			{
				if (time1 == time2)
				{
					return true;
				}
			}
		}
		return false;
	}

	private boolean isClash(String userName, int month, int date, int time, int duration)
	{
		for(Iterator<Booking> i = listOfBookings.iterator(); i.hasNext(); ) {
			Booking item = i.next();
			err("COMPARE (Store|New): (Name " + item.getUserName() + "|" + userName + ") ( " + item.getMonth()  + "|" + month + ") ( " + item.getDate()  + "|" + date + ") (Not Time clash: " + !isTimeClash(item.getTime(), item.getDuration(), time, duration) + ")");
			if (item.getUserName().equals(userName) && item.getMonth() == month && item.getDate() == date && !isTimeClash(item.getTime(), item.getDuration(), time, duration))
			{
				return true;
			}
		}
		return false;
	}
	
	private int conflictStatus(String userName, int numweeks, int month, int date, int time, int duration)
	{
		/*Debug*/err("Starting Conflict Status Check");
		boolean anyClashes = false;
		boolean notAnyClashes = true;
		Calendar tempCalendar = Calendar.getInstance();
		
		tempCalendar.set(Calendar.MONTH,  month);
		tempCalendar.set(Calendar.DAY_OF_MONTH,  date);
		
		for (int i = 0; i < numweeks; i++)
		{
			tempCalendar.add(Calendar.DAY_OF_MONTH, DAYS_IN_WEEK);
			if (isClash(userName, tempCalendar.get(Calendar.MONTH), tempCalendar.get(Calendar.DAY_OF_MONTH), time, duration))
			{
				/*Debug*/err("Conflict Status - CLASH (Test)");
				anyClashes = true;
			}
			else
			{
				/*Debug*/err("Conflict Status - NO-CLASH (Test)");
				notAnyClashes = false;
			}
		}
		/*Debug*/err("Conflict Status result: anyClashes(" + anyClashes + "), notAnyClashes(" + notAnyClashes + ")");
		if (!anyClashes && notAnyClashes || this.emptyList())
		{
			return CONFLICTSTATUS_OVERLAP_NONE;
		}
		else if (anyClashes && !notAnyClashes)
		{
			return CONFLICTSTATUS_OVERLAP_ALL;
		}
		else if (anyClashes && notAnyClashes)
		{
			return CONFLICTSTATUS_OVERLAP_PARTIAL;
		}
		return -1;
		
	}
	
	public boolean emptyList()
	{
		if (listOfBookings.size() > 0)
		{
			return false;
		}
		return true;
	}
	
	private boolean hasCapacity(int capacity)
	{
		if (getCapacity() >= capacity)
		{
			return true;
		}
		else 
		{
			return false;
		}
	}
	
	public String printBookingList()
	{
		StringBuffer returnBuffer = new StringBuffer();
		returnBuffer.append(this.getName() + "\n");
		for(Iterator<Booking> i = listOfBookings.iterator(); i.hasNext(); )
		{
			Booking item = i.next();
			returnBuffer.append(item.getUserName() + " " + item.getMonthString() + " " + item.getDate() + " " + item.getTime() + " " + item.getDuration() + " " + item.getTitle() + "\n");
		}
		return returnBuffer.toString();
	}
	
	public int getCapacity()
	{
		return this.capacity;
	}
	
	public String getName()
	{
		return this.name;
	}
		
	private LinkedList<Booking> listOfBookings;
	private int capacity;
	private String name;
	private int DEFAULT_CALENDAR_YEAR = 2013;
	private static final int CONFLICTSTATUS_OVERLAP_NONE = 0;
	private static final int CONFLICTSTATUS_OVERLAP_PARTIAL = 1;
	private static final int CONFLICTSTATUS_OVERLAP_ALL = 2;
	private static final int DEFAULT_OVERLAP_DURATION = 1;
	private static final int DAYS_IN_WEEK = 7;
}
