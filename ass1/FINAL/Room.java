import java.util.LinkedList;
import java.util.Iterator;
import java.lang.StringBuffer;
import java.util.Calendar;

/**
 * Room deals with all operations to do with
 *  the bookings held within the room. This includes
 *  adding, changing and deleting bookings, as well
 *  as printing out a list of bookings
 * 
 * @author	Hayden Charles Smith, z3418003
 * 			Last modified: 20th April 2013
 */
public class Room {

	/**
	 * Construct a Room object
	 * @param capacity	Maximum people in room
	 * @param name		Name of room
	 */
	public Room(int capacity, String name)
	{
		this.capacity = capacity;
		this.name = name;
		listOfBookings = new LinkedList<Booking>();
	}

	/**
	 * Attempts to add a booking to the room, and returns
	 *  whether it was successful or not. To add a booking,
	 *  it ensures the room has appropriate capacity, and that
	 *  all requested new booking spaces are currently free.
	 *  
	 * @param desiredCapacity	Capacity required by booker
	 * @param userName			Name of booker
	 * @param numweeks			Number of repetitions over weeks for booking
	 * @param month				Month booking is in
	 * @param date				Date within month that booking is on
	 * @param time				24 hour time of booking (hours)
	 * @param duration			Duration of booking (integer)
	 * @param title				Title of particular booking
	 * @return boolean whether booking attempt was successful
	 */
	public boolean attemptToAddBookings(int desiredCapacity, String userName, int numweeks, int month, int date, int time, int duration, String title)
	{
		if (this.hasCapacity(desiredCapacity) && this.bookingsConflict("", numweeks, month, date, time, duration) == CONFLICT_OVERLAPS_WITH_NONE)
		{
			this.addBookings(userName, numweeks, month, date, time, duration, title);
			return true;
		}
		return false;
	}

	
	/**
	 * Attempts to remove a booking from the room, and returns
	 *  whether it was successful or not. To see whether booking
	 *  is able to be removed, it checks if all booking parameters
	 *  given by the user match every corresponding booking space
	 *  
	 * @param userName			Name of booker
	 * @param numweeks			Number of repetitions over weeks for booking
	 * @param month				Month booking is in
	 * @param date				Date within month booking is on
	 * @param time				24 hour time of booking (hours)
	 * @return boolean whether booking removal was successful
	 */
	public boolean attemptToRemoveBookings(String userName, int numweeks, int month, int date, int time)
	{
		if (this.bookingsConflict(userName, numweeks, month, date, time, IGNORE_DURATION) == CONFLICT_OVERLAPS_WITH_ALL)
		{
			this.removeBookings(numweeks, month, date, time);
			return true;
		}
		return false;
	}	

	/**
	 * Attempts to change a booking from one booking time
	 *  to another, and returns whether it was successful or not.
	 *  To see whether a booking can be changed, it ensures all old 
	 *  bookings currently exist, that all new booking spaces are free,
	 *  and that the new room has appropriate capacity
	 *  
	 * @param newRoom
	 * @param userName			Name of booker
	 * @param roomName			Name of current room
	 * @param numweeks			Number of weeks to change
	 * @param monthOld			Month current booking is in
	 * @param dateOld			Date within month current booking is on
	 * @param timeOld			24 hour time current booking is on (hours)
	 * @param capacity			New capacity required by booker
	 * @param monthNew			Month new booking is in
	 * @param dateNew			Date within month new booking is on
	 * @param timeNew			24 hour time new booking is on (hours)
	 * @param durationNew		Duration of new booking
	 * @param title				Title of new booking
	 * @return boolean whether booking change was successful 
	 */
	public boolean attemptToChangeBookings(Room newRoom, String userName, String roomName, int numweeks, int monthOld, int dateOld, int timeOld, int capacity, int monthNew, int dateNew, int timeNew, int durationNew, String title)
	{	
		if (
		 this.bookingsConflict(userName, numweeks, monthOld, dateOld, timeOld, IGNORE_DURATION) == CONFLICT_OVERLAPS_WITH_ALL &&
		 newRoom.hasCapacity(capacity) &&
		 newRoom.canChangeBookings(userName, monthOld, dateOld, timeOld, numweeks, monthNew, dateNew, timeNew, durationNew)
		)
		{
			this.removeBookings(numweeks, monthOld, dateOld, timeOld);	
			newRoom.addBookings(userName, numweeks, monthNew, dateNew, timeNew, durationNew, title);
			return true;
		}		
		return false;
	}
	
	
	/**
	 * Return the capacity of room object
	 * @return capacity of room
	 */
	public int getCapacity()
	{
		return this.capacity;
	}
	
	/**
	 * Return the name of the room
	 * @return name of room
	 */
	public String getName()
	{
		return this.name;
	}
	
	/**
	 * Returns a printout of all bookings for a room
	 * @return string printout of bookings for a room
	 */
	public String printBookingList()
	{
		// Initialise string buffer and add name of room to it
		StringBuffer returnBuffer = new StringBuffer();
		returnBuffer.append(this.getName());
		
		// Iteratively add each booking in the room
		for(Iterator<Booking> i = listOfBookings.iterator(); i.hasNext(); )
		{
			Booking item = i.next();
			returnBuffer.append("\n" + item.getUserName() + " " + item.getMonthString() + " " + item.getDate() + " " + item.getTime() + " " + item.getDuration() + " " + item.getTitle());
		}
		return returnBuffer.toString();
	}

	/**
	 * This function adds a new booking into the room's
	 *  booking list. It does so in the correct order,
	 *  so that the new booking is placed after all dates
	 *  before it, and before all dates after it.
	 *  
	 * @param userName	Name of user requesting booking
	 * @param numweeks	Number of repetitions over weeks for booking
	 * @param month		Month booking is in
	 * @param date		Date within month booking is on
	 * @param time		24 hour time of booking (hours)
	 * @param duration	Duration of requested booking
	 * @param title		Title of requested booking
	 */
	private void addBookings(String userName, int numweeks, int month, int date, int time, int duration, String title)
	{
		// Load month/date/time into calendar instance
		Calendar newItemCalendar = this.buildCalendar(month, date, time);
		
		// Add booking for each week required
		for (int i = 0; i < numweeks; i++)
		{
			Boolean addedForThisWeek = false;
			
			// Search for correct place to new booking
			for(Iterator<Booking> j = listOfBookings.iterator(); j.hasNext(); )
			{
				Booking item = j.next();
				Calendar currentItemCalendar = this.buildCalendar(item.getMonth(), item.getDate(), item.getTime());
				
				if (newItemCalendar.before(currentItemCalendar))
				{
					Booking newBooking = new Booking(userName, newItemCalendar.get(Calendar.MONTH), newItemCalendar.get(Calendar.DAY_OF_MONTH), time, duration, title);
					listOfBookings.add(listOfBookings.indexOf(item), newBooking);
					addedForThisWeek = true;
					break; // Prevent concurrentModificationException
				}
			}
			
			if (!addedForThisWeek)
			{
				Booking newBooking = new Booking(userName, newItemCalendar.get(Calendar.MONTH), newItemCalendar.get(Calendar.DAY_OF_MONTH), time, duration, title);
				listOfBookings.add(newBooking);
			}
			newItemCalendar.add(Calendar.DAY_OF_MONTH, DAYS_IN_WEEK);
		}		
	}
	
	/**
	 * This function finds appropriate booking in the room by 
	 *  finding a room that has equivalent month, date and time,
	 *  and removes it from the booking list.
	 *  
	 * @param numweeks	Number of repetitions over weeks for booking
	 * @param month		Month booking is in
	 * @param date		Date within month booking is on
	 * @param time		24 hour time of booking (hours)
	 */
	private void removeBookings(int numweeks, int month, int date, int time)
	{
		// Load month/date/time into calendar instance
		Calendar calendar = this.buildCalendar(month, date, time);
		
		for (int i = 0; i < numweeks; i++)
		{
			for(Iterator<Booking> j = listOfBookings.iterator(); j.hasNext(); ) {
				Booking item = j.next();
				if (item.getMonth() == calendar.get(Calendar.MONTH) && item.getDate() == calendar.get(Calendar.DAY_OF_MONTH) && item.getTime() == time)
				{
					listOfBookings.remove(item);
					break; // Prevent concurrentModificationException
				}
			}
			
			// Add a week for next iteration
			calendar.add(Calendar.DAY_OF_MONTH, DAYS_IN_WEEK);
		}
	}
	
	/**
	 * Given a month, date and time, a calendar instance is 
	 *  returned so that operations can be completed on it.
	 *  
	 * @param month	Month to load into calendar
	 * @param date	Date to load into calendar
	 * @param time	Time to load into calendar
	 * @return an instance of calendar with loaded values
	 */
	private Calendar buildCalendar(int month, int date, int time)
	{
		Calendar calendarItem = Calendar.getInstance();
		calendarItem.set(Calendar.YEAR, DEFAULT_CALENDAR_YEAR);
		calendarItem.set(Calendar.MONTH, month);
		calendarItem.set(Calendar.DAY_OF_MONTH, date);
		calendarItem.set(Calendar.HOUR_OF_DAY, time);
		return calendarItem;
	}
	
	/**
	 * Determine if the room has an appropriate capacity
	 * @param capacity Desired capacity of the room
	 * @return whether the room has capacity
	 */
	private boolean hasCapacity(int capacity)
	{
		if (getCapacity() >= capacity)
		{
			return true;
		}
		return false;
	}
				
	/**
	 * Given a time/duration of two events, determine whether
	 *  they have any overlapping times (i.e. clashes).
	 *  
	 * @param time1 Time of first event item
	 * @param duration1 Duration of first event item
	 * @param time2 Time of second event item
	 * @param duration2 Duration of second event item
	 * @return
	 */
	private boolean isTimeClash(int time1, int duration1, int time2, int duration2)
	{
		for (int i = time1; i < (time1 + duration1); i++)
		{
			for (int j = time2; j < (time2 + duration2); j++)
			{
				if (i == j)
				{
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Determine the nature of the bookings conflict between
	 *  given parameters and currently stored bookings
	 *  
	 * @param userName	Name of user requesting booking
	 * @param numweeks	Number of repetitions over weeks for booking
	 * @param month		Month booking is in
	 * @param date		Date within month booking is on
	 * @param time		24 hour time of booking (hours)
	 * @param duration	Duration of requested booking
	 * @return integer value that reflects the relative amount
	 *  of overlapping of current and inputed bookings
	 */
	private int bookingsConflict(String userName, int numweeks, int month, int date, int time, int duration)
	{
		boolean clashes = false;
		boolean allClashes = true;
		Calendar tempCalendar = this.buildCalendar(month, date, time);
		
		for (int i = 0; i < numweeks; i++)
		{
			if (isClash(userName, tempCalendar.get(Calendar.MONTH), tempCalendar.get(Calendar.DAY_OF_MONTH), time, duration))
			{
				clashes = true;
			}
			else
			{
				allClashes = false;
			}
			
			// Add 7 days to calendar for next iteration
			tempCalendar.add(Calendar.DAY_OF_MONTH, DAYS_IN_WEEK);
		}
		if (!clashes)
		{
			return CONFLICT_OVERLAPS_WITH_NONE;
		}
		else if (clashes && allClashes)
		{
			return CONFLICT_OVERLAPS_WITH_ALL;
		}
		return -1;
	}
	
	/**
	 * Determine whether, given a month/date/time/duration,
	 *  there is a clash with any current bookings that are
	 *  booked under the room
	 *  
	 * It should be noted that if isClash is being called 
	 *  from a delete function, the durations are not compared.
	 *  This is due to the IGNORE_DURATION flag
	 *  
	 * @param userName	Name of user requesting booking
	 * @param month		Month booking is in
	 * @param date		Date within month booking is on
	 * @param time		24 hour time of booking (hours)
	 * @param duration	Duration of requested booking
	 * @return whether given month/date/time/duration clash with 
	 *  any current bookings
	 */
	private boolean isClash(String userName, int month, int date, int time, int duration)
	{
		for(Iterator<Booking> i = listOfBookings.iterator(); i.hasNext(); )
		{
			Booking item = i.next();
			if (item.getMonth() == month && item.getDate() == date && (item.getUserName().equals(userName) || userName.equals("")))
			{
				if (duration == IGNORE_DURATION && time == item.getTime())
				{
					return true;
				}
				else if (isTimeClash(item.getTime(), item.getDuration(), time, duration))
				{	
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Determine whether there is a clash upon changing
	 *  a booking, for each week the change in booking is
	 *  requested.
	 *  
	 * @param userName	Name of user requesting booking
	 * @param numweeks	Number of repetitions over weeks for booking
	 * @param monthOld	Month old booking is in
	 * @param dateOld	Date within month old booking is on
	 * @param timeOld	24 hour time of old booking (hours)
	 * @param monthNew	Month new booking is in
	 * @param dateNew	Date within month new booking is on
	 * @param timeNew	24 hour time of new booking (hours)
	 * @param duration	Duration of requested booking
	 * @return integer value that reflects the relative amount
	 *  of overlapping of current and inputed bookings
	 */
	private boolean canChangeBookings(String userName, int monthOld, int dateOld, int timeOld, int numWeeks, int monthNew, int dateNew, int timeNew, int duration)
	{
		boolean clash = false;
		Calendar oldCal = this.buildCalendar(monthOld, dateOld, timeOld);
		Calendar newCal = this.buildCalendar(monthNew, dateNew, timeNew);
		
		for (int i = 0; i < numWeeks; i++)
		{
			for(Iterator<Booking> j = listOfBookings.iterator(); j.hasNext(); )
			{
				Booking item = j.next();
				
				// Ensure to ignore clashes with older bookings changing from
				if (!(userName.equals(item.getUserName()) && (oldCal.get(Calendar.HOUR_OF_DAY) == item.getTime()) && (oldCal.get(Calendar.MONTH) == item.getMonth()) && (oldCal.get(Calendar.DAY_OF_MONTH) == item.getDate())))
				{
					if (item.getMonth() == newCal.get(Calendar.MONTH) && item.getDate() == newCal.get(Calendar.DAY_OF_MONTH) && item.getUserName().equals(userName) && isTimeClash(item.getTime(), item.getDuration(), newCal.get(Calendar.HOUR_OF_DAY), duration))
					{
						clash = true;
					}
				}
			}
			
			// Add 7 days to calendar for next iteration
			oldCal.add(Calendar.DAY_OF_MONTH, DAYS_IN_WEEK);
			newCal.add(Calendar.DAY_OF_MONTH, DAYS_IN_WEEK);
		}
		if (!clash)
		{
			return true;
		}
		return false;
	}
	
	private LinkedList<Booking> listOfBookings;
	private int capacity;
	private String name;
	private int DEFAULT_CALENDAR_YEAR = 2013;
	private static final int CONFLICT_OVERLAPS_WITH_NONE = 0;
	private static final int CONFLICT_OVERLAPS_WITH_ALL = 1;
	private static final int IGNORE_DURATION = -1;
	private static final int DAYS_IN_WEEK = 7;
}
