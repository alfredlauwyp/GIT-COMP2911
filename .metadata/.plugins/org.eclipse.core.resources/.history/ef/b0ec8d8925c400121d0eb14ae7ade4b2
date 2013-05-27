import java.awt.EventQueue;

import javax.swing.JFrame;

public class SudokuManager {
	
	public SudokuManager()
	{
		board = new SudokuBoard(BOARD_SIZE, DIFFICULTY_EASY);
		
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
