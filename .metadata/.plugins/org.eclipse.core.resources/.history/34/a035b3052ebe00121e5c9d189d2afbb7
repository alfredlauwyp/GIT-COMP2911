import java.awt.*;
import java.awt.event.*;
import java.lang.Integer;
import javax.swing.*;

/**
 * 
 * @author Hayden, Laura, Jerome, Steven
 * some code copied from Core Java Fundamentals Book 1 Chapter 8, Event Handling p329/ Cay Hortsmann
 * @version 0.1
 */


/**
 * A frame with a panel button
 */

//NB: JFrame's default layoutManager is BorderLayout (so no need to setLayout unless we want to change it)
class SudokuFrame extends JFrame
{
	public SudokuFrame(Board board)
	{
		setTitle("SudokuFrame");
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		
		//button panel is created, which will be added to SudokuFrame
		buttonPanel = new JPanel();
		
		//create buttons - the makebutton method adds the buttons automatically to buttonPanel 
		for(int i = 0; i<9; i++)
		{
			for(int j = 0; j<9; j++){
				String cellVal;
				if (board.isVisibleCellValue(i,j))
				{
					cellVal = Integer.toString(board.getCellValue(i,j));
				}
				else
				{
					cellVal = "..";
				}
				makeButton(cellVal, Color.YELLOW, buttonPanel);
			}
		}
		
		/*
		 * buttons are set in a grid layout
		 * NB: I have read that GridBagLayout is a slightly more complex but highly customizable layout manager (we can determine the size of each button individually)
		 * - this can be looked into later
		 */
		buttonPanel.setLayout(new GridLayout(9,9));
		
		//creating a list of panels
		hintPanel = new JPanel();
		hintPanel.setLayout(new BoxLayout(hintPanel, DO_NOTHING_ON_CLOSE));
		makeButton("Click for Hint", Color.BLUE, hintPanel);
		makeButton("Give up?", Color.GREEN, hintPanel);
		
		commandPanel = new JPanel();
		commandPanel.setLayout(new FlowLayout());
		makeButton("NEW EASY", Color.PINK, commandPanel);
		makeButton("NEW MEDIUM", Color.CYAN, commandPanel);
		makeButton("NEW HARD", Color.RED, commandPanel);
		
		modePanel = new JPanel();
		modePanel.setLayout(new FlowLayout());
		makeButton("DRAFT", Color.PINK, modePanel);
		makeButton("WTF", Color.CYAN, modePanel);
		
		modePanel = new JPanel();
		modePanel.setLayout(new FlowLayout());
		makeButton("DRAFT", Color.PINK, modePanel);
		makeButton("WTF", Color.CYAN, modePanel);
		
		scorePanel = new JPanel();
		scorePanel.setLayout(new BoxLayout(scorePanel, DO_NOTHING_ON_CLOSE));
		makeButton("PAUSE", Color.BLACK, scorePanel);
		makeButton("TIME ME", Color.DARK_GRAY, scorePanel);
		
		//add or nest panels to frame
		add(buttonPanel,BorderLayout.CENTER);
		add(hintPanel,BorderLayout.EAST);
		add(commandPanel,BorderLayout.NORTH);
		add(modePanel,BorderLayout.SOUTH);
		add(scorePanel,BorderLayout.WEST);
		
		//random label for fun
		add(new JLabel("Hello there 2911"),BorderLayout.NORTH);
	}
	
	/*
	 * indiButton performs same as makeButton except has different return type
	 * @return JButton object
	 * Mainly used for random testing at this point
	 */
	public JButton indiButton(String name, Color backgroundColor)
	{
		JButton button = new JButton(name);
		ColorAction action = new ColorAction(backgroundColor);
		button.addActionListener(action);
		return button;
		
	/*
	 * makeButton makes a JButton, which is then added to the buttonPanel, with an attached ActionListener
	 * ActionListener should be detached to add different functionality to different buttons
	 * in the future, add 1 more argument to represent a type of ActionListener
	 */
	}
	public void makeButton(String name, Color backgroundColor, JPanel panel)
	{
		JButton button = new JButton(name);
		panel.add(button);
		ColorAction action = new ColorAction(backgroundColor);
		button.addActionListener(action);
	}
	
/**
 * An action listener that sets the panel's background color
 */
	
	private class ColorAction implements ActionListener
	{
		public ColorAction(Color c)
		{
			backgroundColor = c;
		}
		
		public void actionPerformed(ActionEvent event)
		{
			buttonPanel.setBackground(backgroundColor);
		}
		
		private Color backgroundColor;
	}
	
	private JPanel buttonPanel;
	private JPanel hintPanel;
	private JPanel commandPanel;
	private JPanel modePanel;
	private JPanel scorePanel;
	
	//you can play around with width and height as well - only affects initial window size, nothing more
	public static final int DEFAULT_WIDTH = 900;
	public static final int DEFAULT_HEIGHT = 700;
}





