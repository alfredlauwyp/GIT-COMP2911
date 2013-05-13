import java.awt.EventQueue;

import javax.swing.JFrame;

public class SudokuManager {
	
	public SudokuManager()
	{
		newGame(DIFFICULTY_EASY);
		for (int i = 0; i < BOARD_SIZE; i++)
		{
			for (int j = 0; j < BOARD_SIZE; j++)
			{
				System.out.print(board.getCellActualFinal(i, j) + " ");
			}
			System.out.print("\n");
		}
	}
	
	private void newGame(int difficulty)
	{
		board = new SudokuBoard(BOARD_SIZE, difficulty);
		
		//initialises frame with events handler
		EventQueue.invokeLater(new Runnable(){
			public void run()
			{
				SudokuFrame frame = new SudokuFrame(board);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
			}
		});
	}
	
	private SudokuBoard board;
	private static final int BOARD_SIZE = 9;
	private static final int DIFFICULTY_EASY = 1;
	private static final int DIFFICULTY_MEDIUM = 2;
	private static final int DIFFICULTY_HARD = 3;
}
