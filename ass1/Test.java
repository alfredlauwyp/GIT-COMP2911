import java.util.Calendar;
import java.util.TimeZone;
import java.text.DateFormatSymbols;

public class Test {

	public static void main(String[] args)
	{
		TimeZone tz = TimeZone.getTimeZone("EAST");
		Calendar c = Calendar.getInstance();
		
		c.setTimeZone(tz);
		c.set(Calendar.YEAR, 2013);
		c.set(Calendar.MONTH, 3);
		c.set(Calendar.DAY_OF_MONTH, 30);
		c.set(Calendar.HOUR, 7);
		
		Calendar d = Calendar.getInstance();
		d.setTimeZone(tz);
		
		d.set(Calendar.YEAR, 2013);
		d.set(Calendar.MONTH, 3);
		d.set(Calendar.DAY_OF_MONTH, 30);
		d.set(Calendar.HOUR, 7);
		
		System.out.println(c.after(d));
		
		
	}
	
	
}
