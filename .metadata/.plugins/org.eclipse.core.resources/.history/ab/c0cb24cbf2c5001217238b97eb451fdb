import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;



public class SudokuManager {
	
	public SudokuManager()
	{
		
		pane = new OptionPanes();
		difficulty = pane.chooseLevel();
		
		while(difficulty == EXIT_VALUE || difficulty == pane.closedOption()){
			if(pane.exitMessageStart()){
				System.exit(0);
			} else difficulty = pane.chooseLevel();
		}

		board = new SudokuBoard(BOARD_SIZE);
		
		/*
		  
		  CODE BELOW LINKS GUI TO BACK-END
		  comment back in to re-link
		 
		board.generate(difficulty);//->generator used
		
		//board.generate(board.difficultyValueEasy()); ->not used for the moment
		
		
		for (int i = 0; i < board.getBoardSize(); i++)
		{
			for (int j = 0; j < board.getBoardSize(); j++)
			{
				System.out.print(board.getCellValue(i, j) + " ");
			}
			System.out.println();
		}
		
		*/
		
		
		//initialises frame with events handler
		EventQueue.invokeLater(new Runnable(){
			public void run()
			{
				SudokuFrame frame = new SudokuFrame(board);
				frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				frame.addWindowListener(new WindowAdapter() {
					// handle window closing 
					public void windowClosing(WindowEvent we) {
				        if(pane.exitMessageInGame())
				        	System.exit(0);
				    }
					});
				frame.setVisible(true);
			}
		});
	}
	
	private OptionPanes pane;
	private int difficulty;
	private SudokuBoard board;
	private static final int BOARD_SIZE = 9;
	private final int EXIT_VALUE = 4;
}
