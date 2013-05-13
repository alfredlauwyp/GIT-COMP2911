/**
 * This will probably inherit from the Cell class once we combine everything
 * At the moment the methods are different but they can be re-named later if need be
 * @author laura
 *
 */
public class CellGen {
	public CellGen(int value){
		this.value = value;
		presence = true;
	}
	
	@Override
	public boolean equals(Object obj){
		boolean result = false;
		CellGen other;
		
		if(obj == this){
			result =  true;
		} else if (obj == null){
			result = false;
		} else if (obj.getClass() == CellGen.class){
			other = (CellGen)obj;
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
	
	public int getValue(){
		return value;
	}
	
	public void setValue(int x){
		value = x;
	}
	
	public void setPresence(boolean x){
		presence = x;
	}
	
	public boolean getPresence(){
		return presence;
	}
	
	
	private int value;
	private boolean presence;
}
