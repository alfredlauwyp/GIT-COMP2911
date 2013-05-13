/**
 * Class to store and access values associated with an individual
 *  cell in the grid
 * @author Hayden, Laura, Jerome, Steven
 * @version 0.1
 * 
 */
public class Cell {

	/**
	 * Construct a cell in the grid
	 * @param actualFinal Value the cell "should" hold
	 * @param preset Whether or not cell value is given to user
	 * @param boardSize Size of board
	 */
	public Cell(int actualFinal, boolean preset, int boardSize)
	{
		this.actualFinal = actualFinal;
		this.preset = preset;
		
		for (int i = 0; i < boardSize; i++)
		{
			//inputTemp[i] = false;
		}
	}
	
	/**
	 * Return final value of cell
	 * @return What the actual final value of cell should be
	 */
	public int getActualFinal()
	{
		return actualFinal;
	}
	
	/**
	 * Return input value from user of cell
	 * @return What value the user has inputed into the cell
	 */
	public int getInputFinal()
	{
		return inputFinal;
	}
	
	/**
	 * Determine whether cell value is given to user
	 * @return Whether the cell is a preset cell
	 */
	public boolean isPreset()
	{
		return preset;
	}
	
	/**
	 * Determine whether a particular temporary value in a cell is set
	 * @param number Temporary value interested in
	 * @return Whether given temporary value has been set by user
	 */
	public boolean issetInputTemp(int number)
	{
		return inputTemp[number + 1];
	}
	
	/**
	 * Set the final value of a cell
	 * @param number Final number that you want the cell to be set to 
	 */
	public void setInputFinal(int number)
	{
		inputFinal = number;
	}
	
	/**
	 * Set the temporary value of a particular number of a cell
	 * @param number Which of the temporary numbers' visibility you 
	 *  want to toggle
	 * @param isSet Whether you want the temporary number to be visible
	 */
	public void setInputTemp(int number, boolean isSet)
	{
		inputTemp[number + 1] = isSet;
	}
	
	/**
	 * Determine whether the cell has the correct input value in it
	 * @return Whether cell has correct input value
	 */
	public boolean isCorrect()
	{
		if (actualFinal == inputFinal)
		{
			return true;
		}
		return false;
	}
	
	private int actualFinal;
	private int inputFinal;
	private boolean preset;
	private boolean inputTemp[];
	
}
