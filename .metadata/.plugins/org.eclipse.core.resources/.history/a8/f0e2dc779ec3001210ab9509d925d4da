/**
 * Class to store and access values associated with an individual
 *  cell in the grid
 * @author Hayden, Laura, Jerome, Steven
 * @version 0.1
 * 
 */
public class SimpleCell implements Cell {

	@Override
	public boolean equals(Object obj){
		boolean result = false;
		SimpleCell other;
		
		if(obj == this){
			result =  true;
		} else if (obj == null){
			result = false;
		} else if (obj.getClass() == SimpleCell.class){
			other = (SimpleCell)obj;
			if(other.valueMain == this.valueMain){
				result = true;
			}
		} else if (obj.getClass() == int.class){
			if(this.valueMain == Integer.valueOf((String) obj)) {
				result = true;
			}
		} 
		return result;
	}
	
	public SimpleCell(int actualFinal)
	{
		this.valueMain = actualFinal;
	}
	
	public int getValue()
	{
		return this.valueMain;
	}
	
	public void setValue(int val)
	{
		this.valueMain = val;
	}
	
	public boolean isVisible()
	{
		return this.visible;
	}
	
	public void setVisible(boolean preset)
	{
		this.visible = preset;
	}
	
	protected int valueMain;
	protected boolean visible;
	
}
