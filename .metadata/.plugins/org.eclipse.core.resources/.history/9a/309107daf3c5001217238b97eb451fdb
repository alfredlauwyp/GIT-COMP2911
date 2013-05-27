/**
 * Class to store and access values associated with an individual
 *  cell in the grid
 * @author Hayden, Laura, Jerome, Steven
 * @version 0.1
 * 
 */
public class BoardCell {

	/**
	 * TODO:
	 * @param boardSize
	 */
	public BoardCell(int boardSize)
	{		
		this.boardSize = boardSize;
		this.tempNumbers = new boolean[boardSize];
		for (int i = 0; i < boardSize; i++)
		{
			this.tempNumbers[i] = false;
		}
		//TODO HAYDEN I ADDED THESE - then realised u r calling reset before using the board anyway, so these can be removed if u prefer
		isCurrentlyVisible = true;
		isInitiallyVisible = true;
		valueFinal = NOT_SET;
		valueInput = NOT_SET;
	}
	
	/**
	 * TODO:
	 * @return
	 */
	public int getFinalValue()
	{
		return valueFinal;
	}
	
	/**
	 * TODO:
	 * @return
	 */
	public int getInputValue()
	{
		return valueInput;
	}
		
	/**
	 * TODO:
	 * @param number
	 */
	public void setFinalValue(int number)
	{
		valueFinal = number;
	}
	
	/**
	 * TODO:
	 * @param number
	 */
	public void setInputValue(int number)
	{
		valueInput = number;
	}
	
	/**
	 * TODO:
	 */
	public void removeInputValue()
	{
		this.valueInput = NOT_SET;
	}
	
	/**
	 * TODO:
	 */
	public void removeFinalValue()
	{
		this.valueFinal = NOT_SET;
	}
	
	/**
	 * TODO:
	 * @return
	 */
	public boolean isCurrentlyVisible()
	{
		return this.isCurrentlyVisible;
	}
	
	/**
	 * TODO:
	 * @return
	 */
	public boolean isInitiallyVisible()
	{
		return this.isInitiallyVisible;
	}

	/**
	 * TODO:
	 * @param isVisible
	 */
	public void setCurrentlyVisible(boolean isVisible)
	{
		this.isCurrentlyVisible = isVisible;
	}
	
	/**
	 * TODO:
	 * @param isVisible
	 */
	public void setInitiallyVisible(boolean isVisible)
	{
		this.isInitiallyVisible = isVisible;
	}
	
	/**
	 * TODO:
	 * @param number
	 * @return
	 */
	public boolean isSetTemp(int number)
	{
		return tempNumbers[number - 1];
	}

	/**
	 * TODO:
	 * @param number
	 * @param isSet
	 */
	public void setTemp(int number, boolean isSet)
	{
		tempNumbers[number - 1] = isSet;
	}
	
	/**
	 * TODO:
	 */
	public void reset()
	{
		this.valueFinal = NOT_SET;
		this.valueInput = NOT_SET;
		//TODO I CHANGED THESE
		this.isCurrentlyVisible = true;
		this.isInitiallyVisible = true;
		for (int i = 0; i < boardSize; i++)
		{
			tempNumbers[i] = false;
		}
	}
	
	/**
	 * TODO:
	 * @return
	 */
	public boolean isEmptyFinal()
	{
		if (this.valueFinal == NOT_SET)
		{
			return true;
		}
		return false;
	}
	
	/**
	 * TODO:
	 * @return
	 */
	public boolean isEmptyInput()
	{
		if (this.valueInput == NOT_SET)
		{
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isCorrect()
	{
		if (valueInput == valueFinal)
		{
			return true;
		}
		return false;
	}
	
	private int boardSize;
	private int valueFinal;
	private int valueInput;
	private boolean isInitiallyVisible;
	private boolean isCurrentlyVisible;
	private boolean tempNumbers[];
	//TODO I ADDED THIS
	private static final int NOT_SET = -1;
	
}
