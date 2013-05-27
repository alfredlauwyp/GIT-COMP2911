import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;


public class Removal {
	
	public Removal(Board b){
		this.board = board;
		checker = new SudokuSolver();
		removable = new ArrayList<Point>();
		initialise();
	}
	
	public void remove(){
		simpleRemove();
		//checker.isSudokuSolutionUnique(b);
		if (difficulty > board.difficultyValueEasy()){
			//complexRemove();
		}
	}
	
	private void simpleRemove(){
		Random r = new Random();
		int toRemove;
		int x, y;
		int z;
		int count = 0;
		boolean can;
		if(difficulty <= board.difficultyValueEasy()){
			toRemove = 20;
		} else if (difficulty <= board.difficultyValueMedium()){
			toRemove = 30;
		} else {
			toRemove = 80;
		}
		while(toRemove > 0){
			x = r.nextInt(9);
			y = r.nextInt(9);
			z = board.getCellValue(x+1, y+1);
			can = true;
			if(board.isInitiallyVisibleCell(x+1, y+1) == false){
				continue;
			}
			//TODO the randomness and trial and error nature of this seems inefficient
			if (count > 1000){
				break;
			}
			for(int i = 0; i < 9; i++){
				if(i != z){
					if (!(board.rowHas(x+1, i) || board.columnHas(y+1, i) || board.squareHas(x+1,y))){
						can = false;
						count++;
						break;
					}
				}
			}
			if(can){
				board.setCellVisibility(x+1, y+1, false);
				toRemove--;
			}
		}
	}
	
	private void complexRemove(){
		int complexity = (difficulty+1)*DIFF_CALIBRATION;
		int i = 0;
		int x, y, z;
		int diff;
		Random r = new Random();
		while (i < complexity){
			x = r.nextInt(9);
			y = r.nextInt(9);
			board.setCellVisibility(x+1, y+1, false);
			//diff = checker.isSudokuSolutionUnique(b);
			//if(diff < difficulty){
			if(checker.isSudokuSolutionUnique(board)){
				i += 1;//diff;
			} else {
				board.setCellVisibility(x+1, y+1, false);
			}
		}
	}
	
	private void initialise(){
		int row, col;
		for(row = 0; row < 9; row++){
			for(col = 0; col < 9; col++){
				removable.add(new Point(row, col));
			}
		}
	}
	
	private Board board;
	private int difficulty;
	SudokuSolver checker;
	ArrayList<Point> removable;
	private static final int EASY = 1;
	private static final int MEDIUM = 2;
	private static final int HARD = 3;
	private static final int MISSING = -1;
	private static final int DIFF_CALIBRATION = 10;
}
