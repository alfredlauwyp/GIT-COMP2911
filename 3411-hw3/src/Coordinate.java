public class Coordinate {

	public Coordinate(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public int getX()
	{
		return this.x;
	}
	
	public int getY()
	{
		return this.y;
	}
	
	public void setX(int x)
	{
		this.x = x;
	}
	
	public void setY(int y)
	{
		this.y = y;
	}
	
	public String toString()
	{
		return new String("("+x+","+y+")");
	}
	
	public boolean equals(Object obj)
	{
		Coordinate a = (Coordinate)obj;
		if (a.x == x && a.y == y)
		{
			return true;
		}
		return false;
	}
	
	private int x;
	private int y;
}
