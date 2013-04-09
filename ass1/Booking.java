import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * 
 * @author Hayden Smith
 * TODO: Description here
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
	
	public String getMonthString()
	{
	    DateFormat formatter = new SimpleDateFormat(DATE_STRING_FORMAT, Locale.ENGLISH);
	    Calendar calendar = Calendar.getInstance();
	    calendar.set(Calendar.MONTH, this.getMonth());
	    return formatter.format(calendar.getTime());
	}
	
	private String userName;
	private int month;
	private int date;
	private int time;
	private int duration;
	private String title;
	private String DATE_STRING_FORMAT = "MMM";
	
}