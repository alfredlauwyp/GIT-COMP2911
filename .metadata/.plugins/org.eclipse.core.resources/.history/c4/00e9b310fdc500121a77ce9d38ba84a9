import java.awt.*;
import java.awt.event.*;
import java.lang.Integer;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.*;

/**
 * 
 * @author Hayden, Laura, Jerome, Steven
 * some code copied from Core Java Fundamentals Book 1 Chapter 8, Event Handling p329/ Cay Hortsmann
 * @version 0.1
 */


/**
 * A frame with a panel button
 * @param currentValue is the value the ActionListener uses to change grid labels
 */

//NB: JFrame's default layoutManager is BorderLayout (so no need to setLayout unless we want to change it)
class SudokuFrame extends JFrame
{
	public SudokuFrame(Board board)
	{
		setTitle("SudokuFrame");
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		
		currentValue = BLANK;
		draftMode = false;
		
		/*
		 * 
		 * Lists added in order to quickly find each button location via row and column (might be needed)
		 * 
		 */
		buttonGrid = new ArrayList<ArrayList<JButton>>(8);
		buttonRow = new ArrayList<JButton>(8);
		buttonValue = new LinkedList<JButton>();
		
		//button panel is created, which will be added to SudokuFrame
		buttonPanel = new JPanel();
		
		//create buttons - the makebutton method adds the buttons automatically to buttonPanel 
		for(int i = 0; i<9; i++)
		{
			for(int j = 0; j<9; j++){
				String cellVal;
				if (board.isCurrentlyVisibleCell(i,j))
				{
					cellVal = Integer.toString(board.getCellValue(i,j));
				}
				else
				{
					cellVal = BLANK;
				}
				buttonRow.add(makeGridButton(i, j ,cellVal, currentValue, buttonPanel));
			}
			buttonGrid.add(buttonRow);
		}

		buttonPanel.setLayout(new GridLayout(9,9));
		
		//creating panels
		
		commandPanel = new JPanel();
		commandPanel.setLayout(new FlowLayout());
		easyButton = makeMenuButton("NEW EASY", Color.PINK, commandPanel);
		mediumButton = makeMenuButton("NEW MEDIUM", Color.CYAN, commandPanel);
		hardButton = makeMenuButton("NEW HARD", Color.RED, commandPanel);
		hintButton = makeMenuButton("Click for Hint", Color.BLUE, commandPanel);
		solutionButton = makeMenuButton("Give up?", Color.GREEN, commandPanel);
		
		draftButton = makeDraftButton("DRAFT", commandPanel);
		checkButton = makeMenuButton("Check Square", Color.CYAN, commandPanel);
		
		scorePanel = new JPanel();
		scorePanel.setLayout(new FlowLayout());
		scorePanel.setVisible(true);
		
		makeMenuButton("PAUSE", Color.BLACK, scorePanel);
		timeButton = makeMenuButton("TIME ME", Color.DARK_GRAY, scorePanel);
		
		for (int i = 0; i < LABELS.length(); i++)
	      {
	         final String label = LABELS.substring(i, i + 1);
	         JButton keyButton = new JButton(label);
	         NumberSelect action = new NumberSelect(label);
	         keyButton.addActionListener(action);
	         keyButton.setBackground(DEFAULT_COLOR);
	         buttonValue.add(keyButton);
	         scorePanel.add(keyButton);
	      }
		
		
		//add or nest panels to frame
		add(buttonPanel,BorderLayout.CENTER);
		add(commandPanel,BorderLayout.NORTH);
		add(scorePanel,BorderLayout.SOUTH);
		
	}
	
	public JButton makeGridButton(int row, int col, String name, String currentValue, JPanel panel)
	{
		JButton button = new JButton(name);
		panel.add(button);
		NumberInsert insert = new NumberInsert(button);
		button.addActionListener(insert);
		button.setBackground(DEFAULT_COLOR);
		button.setLayout(new GridLayout(3,3));
		for (int i = 0; i < LABELS.length(); i++)
	      {
		    String label = LABELS.substring(i, i + 1);
		    JLabel l = new JLabel(label);
		    l.setVisible(false);
			button.add(l);
	      }
		
		return button;
	}
	
	public JButton makeMenuButton(String name, Color backgroundColor, JPanel panel)
	{
		JButton button = new JButton(name);
		panel.add(button);
		NumberAction action = new NumberAction(backgroundColor);
		button.addActionListener(action);
		return button;
	}
	
	public JButton makeDraftButton(String name, JPanel panel)
	{
		JButton button = new JButton(name);
		panel.add(button);
		DraftFunction action = new DraftFunction();
		button.addActionListener(action);
		return button;
	}
/**
 * An action listener that sets the
 */
	
	private class NumberInsert implements ActionListener
	{
		public NumberInsert(JButton button)
		{
			b = button;
		}
		
		public void actionPerformed(ActionEvent event)
		{
			if(!getCurrentValue().equalsIgnoreCase(BLANK)){
				if(!draftMode){
					b.setText(getCurrentValue());
					for(Component labels:b.getComponents()){
						labels.setVisible(false);
					}
					//b.setFont(font)
				} else{
					toggleDraftValues(getCurrentValue(), b);
				}
			}
		}
		private JButton b;
	}
	
	public void toggleDraftValues(String value, JButton b){
		Component label = b.getComponent(Integer.parseInt(value)-1);
		if(label.isVisible()){
			label.setVisible(false);
		} else label.setVisible(true);
			if(!b.getText().equalsIgnoreCase(BLANK)){
				Component label2 = b.getComponent(Integer.parseInt(b.getText())-1);
				b.setText(BLANK);
				if(!label2.isVisible())
					label2.setVisible(true);
			}
	}
	
	private class NumberSelect implements ActionListener
	{
		public NumberSelect(String select)
		{
			newCurrentValue = select;
		}
		
		public void actionPerformed(ActionEvent event)
		{
			if(currentValue.equalsIgnoreCase(event.getActionCommand())){
				highlightValue(resetCurrentValue());
			}else{
				currentValue = event.getActionCommand();
				highlightValue(currentValue);
			}
		}
		private String newCurrentValue;
	}
	
	private class DraftFunction implements ActionListener
	{
		public DraftFunction()
		{
			//might not pass in values
			//newCurrentValue = select;
		}
		
		public void actionPerformed(ActionEvent event)
		{
			if(draftButton.getBackground().equals(DEFAULT_COLOR)){
				draftButton.setBackground(Color.RED);
				setDraftMode(true);
			}else {
				draftButton.setBackground(DEFAULT_COLOR);
				setDraftMode(false);
			}
			
			
		}
		//private String newCurrentValue;
	}
	
	public void setDraftMode(boolean enable){
		draftMode = enable;
	}
	
	//Dummy actionListner to be modified / deleted
	private class NumberAction implements ActionListener
	{
		public NumberAction(Color c)
		{
			backgroundColor = c;
		}
		
		public void actionPerformed(ActionEvent event)
		{
			buttonPanel.setBackground(backgroundColor);
			highlightValue(resetCurrentValue());
		}
		
		private Color backgroundColor;
	}
	
	public String resetCurrentValue(){
		this.currentValue.equalsIgnoreCase(BLANK);
		return currentValue;
	}
	
	public String getCurrentValue(){
		return this.currentValue;
	}
	
	private void highlightValue(String current){
		for(JButton jb: buttonValue){
			if(jb.getText().equalsIgnoreCase(current))
				jb.setBackground(Color.PINK);
			else jb.setBackground(DEFAULT_COLOR);
		}
	}
	
	
	private boolean draftMode;
	private Color DEFAULT_COLOR = new Color(238,238,238);
	private JButton easyButton;
	private JButton mediumButton;
	private JButton hardButton;
	private JButton draftButton;
	private JButton timeButton;
	private JButton hintButton;
	private JButton solutionButton;
	private JButton checkButton;
	private String currentValue;
	private JPanel buttonPanel;
	//private JPanel hintPanel;
	private JPanel commandPanel;
	//private JPanel modePanel;
	private JPanel scorePanel;
	private LinkedList<JButton> buttonValue;
	private ArrayList<ArrayList<JButton>> buttonGrid;
	private ArrayList<JButton> buttonRow;

	public static final String LABELS = "123456789";
	//public static final String EMPTY = "-1";
	public static final String BLANK = "";
	public static final int DEFAULT_WIDTH = 900;
	public static final int DEFAULT_HEIGHT = 700;
}





