public class SudokuTester {

	public static void main(String[] args)
	{
		SudokuBoard board = new SudokuBoard(9);
		board.generate(board.difficultyValueEasy());
		
		board.currentlyGenerating = true;
		for (int i = 1; i < 10; i++)
		{
			for (int j = 1; j < 10; j++)
			{
				board.setCellValue(i, j, i + j);
			}
		}
		printBoard(board);
		board.currentlyGenerating = false;
		
		for (int i = 4; i < 7; i++)
		{
			for (int j = 4; j < 7; j++)
			{
				board.setCellValue(i, j, 1);
			}
		}
		
		board.removeCellValue(5,5);
		
		printBoard(board);
	}
	
	private static void printBoard(Board board)
	{
		for (int i = 1; i < 10; i++)
		{
			for (int j = 1; j < 10; j++)
			{
				int number = board.getCellValue(i, j);
				if (number > 9)
				{
					System.out.print(number + " ");
				}
				else
				{
					System.out.print(number + "  ");
				}
			}
			System.out.println();
		}
		System.out.println();
	}
}
