import java.util.ArrayList;

/**
* Class that allows creation, modification and access to
*  a Sudoku board
* @author Hayden
* @version 0.1
* 
*/
public class SudokuBoard implements Board {

	/**
	 * 
	 * @param size
	 * @param difficulty
	 */
	public SudokuBoard(int size)
	{	
		this.boardSize = size;
		board = new ArrayList<ArrayList<BoardCell>>();
		
		for (int i = 0; i < boardSize; i++)
		{
			board.add(new ArrayList<BoardCell>());
			ArrayList<BoardCell> row = board.get(i);
			for (int j = 0; j < boardSize; j++)
			{ 
				row.add(new BoardCell(boardSize));
			}
		}
	}
	
	/**
	 * Generate a new board
	 * @param difficulty The difficulty level the board
	 *  should be generated with
	 */
	public void generate(int difficulty)
	{
		reset();
		
		this.currentlyGenerating = true;
		this.difficulty = difficulty;
		
		for (int i = 1; i <= boardSize; i++)
		{
			for (int j = 1; j <= boardSize; j++)
			{
				this.setCellValue(i, j, i + j);
			}
		}
		
		/*BoardFiller filler = new BoardFiller(this);
		Removal r = new Removal(this);
		boolean filled = false;
		while(filled){
			filler.fillBoard();
		}

		filler.fillBoard();
		r.remove();*/

		this.currentlyGenerating = false;
	}
	
	/**
	 * Remove the value stored in a cell
	 * @param row Row cell is in
	 * @param col Column cell is in
	 */
	public void removeCellValue(int row, int col)
	{
		getCell(row, col).removeFinalValue();
		getCell(row, col).removeInputValue();
	}
	
	/**
	 * Get the value that is currently held within a particular cell
	 * @param row Row cell is in
	 * @param col Column cell is in
	 * @return Value currently held in the cell
	 */
	public int getCellValue(int row, int col)
	{
		//System.out.println("fc: getCellValue("+row+","+col+")");
		if (currentlyGenerating || isEmptyCell(row, col))
		{
			return getCell(row, col).getFinalValue();
		}
		else
		{
			return getCell(row, col).getInputValue();
		}
	}
	
	/**
	 * Set the value that is currently held within a particular cell
	 * @param row Row cell is in 
	 * @param col Column cell is in
	 * @param number Value currently held in the cell
	 */
	public void setCellValue(int row, int col, int number)
	{
		if (currentlyGenerating)
		{
			getCell(row, col).setFinalValue(number);
		}
		else
		{
			getCell(row, col).setInputValue(number);
		}
	}
	
	/**
	 * Determine whether a cell is currently visible for the current 
	 *  state of the board
	 * @param row Row cell is in
	 * @param col Column cell is in
	 * @return Whether the cell is currently visible at the current
	 *  particular state of the board
	 */
	public boolean isCurrentlyVisibleCell(int row, int col)
	{
		return getCell(row, col).isCurrentlyVisible();
	}
	
	/**
	 * Determine whether a cell is visible at the initial state 
	 *  of the board
	 * @param row Row cell is in
	 * @param col Column cell is in
	 * @return Whether the cell is visible at the initial state
	 *  of the board
	 */
	public boolean isInitiallyVisibleCell(int row, int col)
	{
		return getCell(row, col).isInitiallyVisible();
	}
	/**
	 * Set the visiblity of a cell on the board
	 * @param row Row cell is in
	 * @param col Column cell is in
	 * @param visiblity Whether the particular cell is visible or not
	 */
	public void setCellVisibility(int row, int col, boolean visibility)
	{
		if (currentlyGenerating)
		{
			getCell(row, col).setInitiallyVisible(visibility);
		}
		else
		{
			getCell(row, col).setCurrentlyVisible(visibility);
		}
	}
	
	/**
	 * TODO: HAYDEN I JUST COMMENTED THIS OUT FOR NOW TO MAKE IT COMPILE - Laura
	 */
	public boolean isEmptyCell(int row, int col)
	{
		if (currentlyGenerating)
		{
			return getCell(row, col).isEmptyFinal();
		}
		else
		{
			return getCell(row, col).isEmptyInput();
		}
	}
			
	/**
	 * Check is a particular cell value's temporary value for a 
	 *  particular number is visible
	 * @param row Row cell is in
	 * @param col Column Cell is in
	 * @param number Number of the temporary value that needs to
	 *  be checked if is visible
	 * @return Whether a particular cell value's temporary value for
	 *  a particular number is visible
	 */
	public boolean isVisibleCellTemp(int row, int col, int number)
	{
		return getCell(row, col).issetTemp(number);
	}
	
	/**
	 * Make a particular cell value's temporary value for a 
	 *  particular number either visible or not visible
	 * @param row Row cell is in
	 * @param col Column Cell is in
	 * @param number Number of the temporary value that needs to
	 *  have it's visiblity changed
	 * @param isSet Value to denote whether to have the cell visible
	 *  or not visible
	 */
	public void setCellTempVisibility(int row, int col, int number, boolean isSet)
	{
		getCell(row, col).setTemp(number, isSet);
	}
			
	/**
	 * Return whether the board has been correctly filled out
	 * @return Whether the board has been correctly filled out
	 */
	public boolean isCorrectBoard()
	{
		boolean allCorrect = true;
		for (int i = 1; i <= boardSize; i++)
		{
			for (int j = 1; i <= boardSize; j++)
			{
				if (!isCorrectCell(i, j))
				{
					allCorrect = false;
				}
			}
		}
		return allCorrect;
	}
	
	/**
	 * Return whether a particular cell in the board has been
	 *  correctly filled out 
	 * @param row Row cell is in
	 * @param col Column cell is in
	 * @return Whether a particular cell in the board has been correctly
	 *  filled out
	 */
	public boolean isCorrectCell(int row, int col)
	{
		return getCell(row, col).isCorrect();
	}
	
	/**
	 * Return the difficulty constant for "Easy"
	 * @return Difficulty constant for "Easy"
	 */
	public int difficultyValueEasy()
	{
		return DIFFICULTY_EASY;
	}
	
	/**
	 * Return the difficulty constant for "Medium"
	 * @return Difficulty constant for "Medium"
	 */
	public int difficultyValueMedium()
	{
		return DIFFICULTY_MEDIUM;
	}
	
	/**
	 * Return the difficulty constant for "Hard"
	 * @return Difficulty constant for "Hard"
	 */
	public int difficultyValueHard()
	{
		return DIFFICULTY_HARD;
	}
	
	/**
	 * Get the size of the board
	 * @return Size of the board
	 */
	public int getBoardSize()
	{
		return this.boardSize;
	}
	
	/**
	 * Reset all values in the board
	 */
	public void reset()
	{
		for (int i = 1; i <= getBoardSize(); i++)
		{
			for (int j = 1; j <= getBoardSize(); j++)
			{
				resetCell(i, j);				
			}
		}
	}
	

	/**
	 * TODO: Fill
	 * @param row
	 * @param value
	 * @return
	 */
	public boolean rowHas(int row, int value)
	{
		boolean result = false;
		for (int i = 1; i <= this.boardSize; i++)
		{
			if (getCellValue(row, i) == value)
			{
				if(isInitiallyVisibleCell(row, i)){
					result = true;
				}
			}
		}
		return result;
	}
	
	/**
	 * TODO: Fill
	 * @param col
	 * @param value
	 * @return
	 */
	public boolean columnHas(int col, int value)
	{
		boolean result = false;
		for (int i = 1; i <= this.boardSize; i++)
		{
			if (getCellValue(i, col) == value)
			{
				if(isInitiallyVisibleCell(i, col)){
					result = true;
				}
			}
		}
		return result;
	}
	
	/**
	 * TODO: Fill 
	 * @param sqr
	 * @param value
	 * @return
	 */
	public boolean squareHas(int sqrId, int value)
	{
		boolean result = false;
		for (int i = 1; i <= this.boardSize; i++)
		{
			for (int j = 1; j <= this.boardSize; j++)
			{
				if (squareSectionId(i, j) == sqrId)
				{
					if (getCellValue(i, j) == value)
					{
						if(isInitiallyVisibleCell(i, j)){
							result = true;
						}
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * TODO:
	 * @return
	 */
	public int getDifficulty()
	{
		return this.difficulty;
	}
	
	
	private int squareSectionId(int row, int col)
	{
		int sectionId = 0;
		if (col >= 1 && col <= 3)
		{
			sectionId += 1;
		}
		else if (col >= 4 && col <= 6)
		{
			sectionId += 2;
		}
		else if (col >= 7 && col <= 9)
		{
			sectionId += 3;
		}
		
		if (row <= 6)
		{
			sectionId += 3;
		}
		if (row <= 9)
		{
			sectionId += 3;
		}
		return sectionId;
	}
	
	/**
	 * TODO: Fill
	 * @param row
	 * @param col
	 */
	private void resetCell(int row, int col)
	{
		getCell(row, col).reset();
	}
	
	/**
	 * TODO: Fill
	 * @param row
	 * @param col
	 * @return
	 */
	private BoardCell getCell(int row, int col)
	{
		//System.out.println("fc: getCell("+row+","+col+")");
		return board.get(row - 1).get(col - 1);
	}	
	
	private ArrayList<ArrayList<BoardCell>> board;
	private int boardSize;
	private int difficulty;
	public boolean currentlyGenerating;
	private static final int DIFFICULTY_EASY = 0;
	private static final int DIFFICULTY_MEDIUM = 1;
	private static final int DIFFICULTY_HARD = 2;
	
	
}