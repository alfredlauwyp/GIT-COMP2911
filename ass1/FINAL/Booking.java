import java.text.DateFormatSymbols;

/**
 * Booking is a class that stores all primary values
 *  associated with a particular booking (e.g. month/
 *  date/time/duration of booking, who it is booking under, etc).
 *    
 * @author	Hayden Charles Smith, z3418003
 * 			Last modified: 20th April 2013
 */
public class Booking
{
	/**
	 * Construct a Booking Object
	 * @param userName	name of user booking room
	 * @param month		month in which booking is made
	 * @param date		date on which booking is made
	 * @param time		time at which booking starts (integer hour)
	 * @param duration	duration of booking (hours)
	 * @param title		title of booking
	 */
	public Booking(String userName, int month, int date, int time, int duration, String title)
	{
		this.userName = userName;
		this.month = month;
		this.date = date;
		this.time = time;
		this.duration = duration;
		this.title = title;
	}
	
	/**
	 * Return "userName" field data
	 * @return name of user booking room
	 */
	public String getUserName()
	{
		return userName;
	}
	
	/**
	 * Return "month" field data
	 * @return month in which booking is made
	 */
	public int getMonth()
	{
		return month;
	}
	
	/**
	 * Return "date" field data
	 * @return date on which booking is made
	 */
	public int getDate()
	{
		return date;
	}
	
	/**
	 * Return "time" field data
	 * @return time at which booking starts (integer hour)
	 */
	public int getTime()
	{
		return time;
	}
	
	/**
	 * Return "duration" field data
	 * @return duration of booking (hours)
	 */
	public int getDuration()
	{
		return duration;
	}
	
	/**
	 * Return "title" field data
	 * @return title of booking
	 */
	public String getTitle()
	{
		return title;
	}
	
	/**
	 * Returns the month the booking is stored in as a string
	 * @return the month as a 3 character string
	 */
	public String getMonthString()
	{
		int m = this.getMonth();
		String month = "N/A"; // Default string for the month
	    DateFormatSymbols dfs = new DateFormatSymbols();
	    String[] months = dfs.getShortMonths();
	    if (m >= 0 && m <= 11 ) {
	        month = months[m];
	    }
	    return month;
               
	}
	
	private String userName;
	private int month;
	private int date;
	private int time;
	private int duration;
	private String title;
	
}