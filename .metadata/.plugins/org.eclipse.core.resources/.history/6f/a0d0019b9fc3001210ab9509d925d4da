/**
* Class that allows creation, modification and access to
*  a board
* @author Hayden
* @version 0.1
* 
*/
public interface Board
{
	
	// Cell Values
	public int getCellValue(int row, int col);
	public void setCellValue(int row, int col, int number);
	
	// Cell Visiblity
	public boolean isCurrentlyVisibleCell(int row, int col);
	public boolean isInitiallyVisibleCell(int row, int col);
	public void setCellVisiblity(int row, int col, boolean visiblity);
	
	// Cell Temp Values
	public boolean isVisibleCellTemp(int row, int col, int number);
	public void setCellTemp(int row, int col, int number, boolean isSet);
	
	// Correctness
	public boolean isCorrectBoard();
	public boolean isCorrectCell(int row, int col);
	
	// Difficulty
	public int difficultyValueEasy();
	public int difficultyValueMedium();
	public int difficultyValueHard();
	
	// Board Size
	public int getBoardSize();
	
}