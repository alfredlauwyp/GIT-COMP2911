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
		this.valueInput = -1;
	}
	
	/**
	 * TODO:
	 */
	public void removeFinalValue()
	{
		this.valueFinal = -1;
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
	public boolean issetTemp(int number)
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
		this.valueFinal = -1;
		this.valueInput = -1;
		this.isCurrentlyVisible = false;
		this.isInitiallyVisible = false;
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
		if (this.valueFinal == -1)
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
		if (this.valueInput == -1)
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
	
}
