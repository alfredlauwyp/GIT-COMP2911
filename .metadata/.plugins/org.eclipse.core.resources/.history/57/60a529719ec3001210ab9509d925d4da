/**
 * This will probably inherit from the Cell class once we combine everything
 * At the moment the methods are different but they can be re-named later if need be
 * @author laura
 *
 */
public class StandardCell implements Cell{
	public StandardCell(int value){
		this.value = value;
		presence = true;
	}
	
	@Override
	public boolean equals(Object obj){
		boolean result = false;
		StandardCell other;
		
		if(obj == this){
			result =  true;
		} else if (obj == null){
			result = false;
		} else if (obj.getClass() == StandardCell.class){
			other = (StandardCell)obj;
			if(other.value == this.value){
				result = true;
			}
		} else if (obj.getClass() == int.class){
			if(this.value == (int)obj){
				result = true;
			}
		} 
		return result;
	}
	
	@Override
	public int getValue(){
		return value;
	}
	
	@Override
	public void setValue(int x){
		value = x;
	}
	
	@Override
	public void setVisible(boolean x){
		presence = x;
	}
	
	@Override
	public boolean isVisible(){
		return presence;
	}
	
	
	private int value;
	private boolean presence;
}
