import java.util.LinkedList;
import java.awt.Point;

public class JobPoint implements DualPoint
{

	public JobPoint(int fromX, int fromY, int toX, int toY)
	{
		Point from = new Point(fromX, fromY);
		Point to = new Point(toX, toY);
	
		jobs = new LinkedList<Point>();
		jobs.add(INDEX_FROM, from);
		jobs.add(INDEX_TO, to);
		
		// Determine internal distance
		int changeInX = Math.abs(this.getFromX() - this.getToX());
		int changeInY = Math.abs(this.getFromY() - this.getToY());
		this.internalDistance = (changeInX + changeInY);
	}
	
	public boolean equals(Object obj)
	{
		boolean result = false;
		
		if(obj == this)
		{
			result =  true;
		}
		else if (obj.getClass() == JobPoint.class)
		{
			if (this.getFromX() == ((JobPoint)obj).getFromX() && this.getFromY() == ((JobPoint)obj).getFromY())
			{
				if (this.getToX() == ((JobPoint)obj).getToX() && this.getToY() == ((JobPoint)obj).getToY())
				{
					result = true;
				}
			}
		}
		return result;
	}
	
	public int getInternalDistance()
	{
		return this.internalDistance;
	}
	
	public int getExternalDistanceTo(DualPoint jobTo)
	{
		int changeInX = Math.abs(jobTo.getFromX() - this.getToX());
		int changeInY = Math.abs(jobTo.getFromY() - this.getToY());
		return (changeInX + changeInY);
	}
	
	public int getFromX()
	{
		return ((int) getPointFrom().getX());
	}
	
	public int getFromY()
	{
		return ((int) getPointFrom().getY());
	}
	
	public int getToX()
	{
		return ((int) getPointTo().getX());
	}
	
	public int getToY()
	{
		return ((int) getPointTo().getY());
	}
	
	public String toString()
	{
		return new String("["+getFromX()+","+getFromY()+"]["+getToX()+","+getToY()+"]");
	}
	
	private Point getPointFrom()
	{
		return jobs.get(INDEX_FROM);
	}
	
	private Point getPointTo()
	{
		return jobs.get(INDEX_TO);
	}
	
	protected LinkedList<Point> jobs;
	protected int internalDistance;
	protected static final int INDEX_FROM = 0;
	protected static final int INDEX_TO = 1;
	
}