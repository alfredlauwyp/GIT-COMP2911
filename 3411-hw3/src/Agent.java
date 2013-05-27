import java.awt.Point;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class Agent {

	public Agent()
	{
		this.iterations = -1;
		this.direction = 0;
		
		this.state = STATE_UNFOGGING;
		
		this.map = new char[MAP_SEARCH_SIZE][MAP_SEARCH_SIZE];
		this.mapTemp = new boolean[MAP_SEARCH_SIZE][MAP_SEARCH_SIZE];
		this.mapWeight = new int[MAP_SEARCH_SIZE][MAP_SEARCH_SIZE];
		
		this.locX = (MAP_SEARCH_SIZE / 2);
		this.locY = (MAP_SEARCH_SIZE / 2);
		this.startX = this.locX;
		this.startY = this.locY;
		
		this.firstMove = true;
		this.findingPath = true;
		
		weightedMapReset();
	}
	
	private boolean findingPath;
	
	public char get_action(char view[][])
	{
		this.iterations++;
		try { Thread.sleep(100);} catch(InterruptedException e) { System.out.println("Interrupted"); }
		
		char move = 0;
		if (toolCount(TOOL_GOLD) > 0)
		{
			if (findingPath)
			{
				findPath(startY, startX);
				findingPath = false;
			}
			else
			{
				if (findPathResult.size() == 0)
				{
					findingPath = true;
				}
				
				System.out.println("(" + (int)findPathResult.get(0).getX() + "," + (int)findPathResult.get(0).getY() + ")");
				int xCoord = (int)findPathResult.get(0).getX();
				int yCoord = (int)findPathResult.get(0).getY();
				if (locY - 1 == xCoord && locX == yCoord)
				{
					//System.out.println("Trying to go up");
					// move up 
					if (getDirection() == DIRECTION_UP)
					{
						findPathResult.remove(0);
						move = ACTION_MOVEFORWARD;
					}
					else if (getDirection() == DIRECTION_RIGHT) move = ACTION_TURNLEFT;
					else move = ACTION_TURNRIGHT;
				}
				else if (locY + 1 == xCoord  && locX == yCoord)
				{
					System.out.println("Trying to go down (" + getDirection() + " -- " + DIRECTION_DOWN + ")");
					// Move down
					if (getDirection() == DIRECTION_DOWN)
					{
						findPathResult.remove(0);
						move = ACTION_MOVEFORWARD;
					}
					else if (getDirection() == DIRECTION_RIGHT) move = ACTION_TURNRIGHT;
					else move = ACTION_TURNLEFT;
				}
				else if (locY == xCoord && locX - 1== yCoord)
				{
					//System.out.println("Trying to go left");
					// Move left
					if (getDirection() == DIRECTION_LEFT)
					{
						findPathResult.remove(0);
						move = ACTION_MOVEFORWARD;
					}
					else if (getDirection() == DIRECTION_UP) move = ACTION_TURNLEFT;
					else move = ACTION_TURNRIGHT;
				}
				else if (locY == xCoord && locX + 1 == yCoord)
				{
					//System.out.println("Trying to go right");
					// Move right
					if (getDirection() == DIRECTION_RIGHT)
					{
						findPathResult.remove(0);
						move = ACTION_MOVEFORWARD;
					}
					else if (getDirection() == DIRECTION_DOWN) move = ACTION_TURNLEFT;
					else move = ACTION_TURNRIGHT;
				}
					
			}	
			
		}
		else
		{
			switch (this.state)
			{
				case STATE_UNFOGGING:
					move = getUnfoggingMove(view);
				break;
				case STATE_MAKINGPATH:
					while(true);//move = getPathMakingMove(view);
				//break;
				case STATE_SEARCHINGGOAL:
					move = getGoalSearchingMove(view);
				break;
			}	
		}
		doAction(move);
		return move;
		
	}
	
	private char relativeChar(char view[][], int forward, int left)
	{
		if (Math.abs(forward) <= 2 || Math.abs(left) <= 2)
		{
			if (isBlank(view[MAP_VIEW_CENTRE - forward][MAP_VIEW_CENTRE - left]))
			{
				return '.';
			}
			return view[MAP_VIEW_CENTRE - forward][MAP_VIEW_CENTRE - left];
		}
		return OBSTACLE_WATER;
	}
	
	private void addMapFeatures(char view[][])
	{
		checkForTools(view[MAP_VIEW_SPACE_INFRONT_X][MAP_VIEW_SPACE_INFRONT_Y]);
		for (int i = -MAP_VIEW_CENTRE; i <= MAP_VIEW_CENTRE; i++)
		{
			for (int j = -MAP_VIEW_CENTRE; j <= MAP_VIEW_CENTRE; j++)
			{
				if (isBlank(relativeChar(view, i, j)))
				{
					view[i][j] = OBSTACLE_EXPLORED;
				}				
				
				if (getDirection() == DIRECTION_UP)
				{
					map[locY + i][locX + j] = relativeChar(view, -i, -j);
				}
				if (getDirection() == DIRECTION_DOWN)
				{
					map[locY + i][locX - j] = relativeChar(view, i, -j);
				}
				if (getDirection() == DIRECTION_RIGHT)
				{
					map[locY - j][locX + i] = relativeChar(view, i, j);
				}
				if (getDirection() == DIRECTION_LEFT)
				{
					map[locY + j][locX + i] = relativeChar(view, -i, j);
				}
			}
		}
		
		mapBoundLeft = MAP_SEARCH_SIZE;
		mapBoundRight = 0;
		mapBoundTop = MAP_SEARCH_SIZE;
		mapBoundBottom = 0;
		
		for (int i = 0; i < MAP_SEARCH_SIZE; i++)
		{
			for (int j = 0; j < MAP_SEARCH_SIZE; j++)
			{
				if (!isBlank(map[i][j]))
				{
					if (mapBoundLeft > i)	 mapBoundLeft = i;
					if (mapBoundRight < i)	 mapBoundRight = i;
					if (mapBoundTop > j)	 mapBoundTop = j;
					if (mapBoundBottom < j) mapBoundBottom = j;
				}
			}
		}
	}
	
	private void checkForTools(char chr)
	{
		if (isTool(chr))
		{
			toolAdd(chr);
			if (getDirection() == DIRECTION_UP)
			{
				System.out.println("========================UP!");
				map[locY - 1][locX] = OBSTACLE_EXPLORED;
			}
			if (getDirection() == DIRECTION_DOWN)
			{
				System.out.println("========================DOWN!");
				map[locY + 1][locX] = OBSTACLE_EXPLORED;
			}
			if (getDirection() == DIRECTION_LEFT) 
			{
				System.out.println("========================LEFT!");
				map[locY][locX - 1] = OBSTACLE_EXPLORED;
			}
			if (getDirection() == DIRECTION_RIGHT)
			{
				System.out.println("========================RIGHT! ("+locY+","+(locX + 1)+")");
				map[locY][locX + 1] = OBSTACLE_EXPLORED;
			}
		}
	}
	
	// ==============================================================================
	// =========================== STATE 1: Unfogging ===============================
	// ==============================================================================
	
	private char getUnfoggingMove(char view[][])
	{
		addMapFeatures(view);
		
		if (this.allSpacesExplored())
		{
			changeState(STATE_MAKINGPATH);
		}
		else
		{
			return headTowards(getGreatestMapWeightX(), getGreatestMapWeightY());
		}		
		return 0;
	}
	
	private char headTowards(int x, int y)
	{
		if (firstMove) { weightedMap(); firstMove = false;}
		char move = 0;
		move = turnTowards();
		if (move == 0)
		{
			weightedMap();
			System.out.println("Tits");
			move = ACTION_MOVEFORWARD;
		}		
		return move;
	}
	private boolean firstMove;
	
	private char turnTowards()
	{		
		int turnDirection = greatestAdjacentWeight();
		if (turnDirection == DIRECTION_RELATIVE_LEFT)
		{
			return ACTION_TURNLEFT;
		}
		if (turnDirection == DIRECTION_RELATIVE_RIGHT)
		{
			return ACTION_TURNRIGHT;
		}
		if (turnDirection == DIRECTION_RELATIVE_BACKWARD)
		{
			return ACTION_TURNLEFT;
		}
		return 0;
	}
	
	private int getGreatestMapWeightX()
	{
		return this.greatestMapWeightX;
	}
	
	private int getGreatestMapWeightY()
	{
		return this.greatestMapWeightX;
	}
	
	private void weightedMapReset()
	{
		for (int i = 0; i < MAP_SEARCH_SIZE; i++)
		{
			for (int j = 0; j < MAP_SEARCH_SIZE; j++)
			{
				mapWeight[i][j] = 50;
			}
		}
	}
	
	private int greatestAdjacentWeight()
	{
		int greatestDirection = DIRECTION_RELATIVE_FORWARD;
		int greatest = viewForwardWeight();
		if (viewRightWeight() > greatest)
		{	
			greatestDirection = DIRECTION_RELATIVE_RIGHT;
		}
		if (viewBackwardWeight() > greatest)
		{
			greatestDirection = DIRECTION_RELATIVE_BACKWARD;
		}
		if (viewLeftWeight() > greatest)
		{
			greatestDirection = DIRECTION_RELATIVE_LEFT;
		}
		return greatestDirection;
	}
	
	private void weightedMap()
	{
		mapWeight[locY][locX] -= 9;
		
		mapWeight[locY - 1][locX] -= 2;
		mapWeight[locY + 1][locX] -= 2;
		mapWeight[locY][locX - 1] -= 2;
		mapWeight[locY][locX + 1] -= 2;
		mapWeight[locY - 1][locX + 1] -= 2;
		mapWeight[locY - 1][locX - 1] -= 2;
		mapWeight[locY + 1][locX + 1] -= 2;
		mapWeight[locY + 1][locX - 1] -= 2;
		
		mapWeight[locY + 2][locX] -= 1;
		mapWeight[locY + 2][locX + 1] -= 1;
		mapWeight[locY + 2][locX + 2] -= 1;
		mapWeight[locY + 2][locX - 1] -= 1;
		mapWeight[locY + 2][locX - 2] -= 1;
		
		mapWeight[locY - 2][locX] -= 1;
		mapWeight[locY - 2][locX + 1] -= 1;
		mapWeight[locY - 2][locX + 2] -= 1;
		mapWeight[locY - 2][locX - 1] -= 1;
		mapWeight[locY - 2][locX - 2] -= 1;
		
		mapWeight[locY - 1][locX + 2] -= 1;
		mapWeight[locY - 1][locX - 2] -= 1;
		mapWeight[locY + 1][locX + 2] -= 1;
		mapWeight[locY + 1][locX - 2] -= 1;
		mapWeight[locY][locX + 2] -= 1;
		mapWeight[locY][locX - 2] -= 1;
		
		for (int i = mapBoundLeft; i <= mapBoundRight; i++)
		{
			for (int j = mapBoundTop; j <= mapBoundBottom; j++)
			{
				if (isTool(map[i][j]))
				{
					//mapWeight[i][j] += 5;
				}				
				if (isObstacle(map[i][j]))
				{
					mapWeight[i][j] = -1;
				}
				else if (mapWeight[i][j] < 0)
				{
					mapWeight[i][j] = 0;
				}	
			}
		}
	}
	
	private int numBlanksAround(int x, int y)
	{
		int counts = 0;
		if (isBlank(map[x + 1][y])) counts++;
		if (isBlank(map[x - 1][y])) counts++;
		if (isBlank(map[x][y + 1])) counts++;
		if (isBlank(map[x][y - 1])) counts++;
		return counts;
	}
	
	private boolean allSpacesExplored()
	{
		resetTempMap();
		boolean explored = spaceExplored(this.startX, this.startY);
		System.out.println("Explored all possible map? " + explored);
		return explored;
	}
	
	private boolean spaceExplored(int x, int y)
	{
		addTempMap(x,y);
		//System.out.println("Recursive Call");
		boolean result = true;
		
		if (isBlank(map[x+2][y - 2])) result = false;	
		if (isBlank(map[x+2][y - 1])) result = false;	
		if (isBlank(map[x+2][y])) result = false;		
		if (isBlank(map[x+2][y + 1])) result = false;	
		if (isBlank(map[x+2][y + 2])) result = false;
		
		if (isBlank(map[x+1][y - 2])) result = false;
		if (isBlank(map[x+1][y - 1])) result = false;
		if (isBlank(map[x+1][y])) result = false;	
		if (isBlank(map[x+1][y + 1])) result = false;
		if (isBlank(map[x+1][y + 2])) result = false;
		
		if (isBlank(map[x][y - 2])) result = false;
		if (isBlank(map[x][y - 1])) result = false;
		if (isBlank(map[x][y])) result = false;		
		if (isBlank(map[x][y + 1])) result = false;
		if (isBlank(map[x][y + 2])) result = false;
		
		if (isBlank(map[x-1][y - 2])) result = false;
		if (isBlank(map[x-1][y - 1])) result = false;
		if (isBlank(map[x-1][y])) result = false;	
		if (isBlank(map[x-1][y + 1])) result = false;
		if (isBlank(map[x-1][y + 2])) result = false;
		
		if (isBlank(map[x-2][y - 2])) result = false;
		if (isBlank(map[x-2][y - 1])) result = false;
		if (isBlank(map[x-2][y])) result = false;	
		if (isBlank(map[x-2][y + 1])) result = false;
		if (isBlank(map[x-2][y + 2])) result = false;
		
		// Search Downward
		if (!inTempMap(x+1,y) && result == true)
		{
			//System.out.println("Down");
			if (!isObstacle(map[x + 1][y]))
			{
				result = spaceExplored(x+1,y);
			}
		}
		if (!inTempMap(x-1,y) && result == true)
		{
			//System.out.println("Up");
			if (!isObstacle(map[x - 1][y]))
			{
				result = spaceExplored(x-1,y);
			}
		}		
		if (!inTempMap(x,y+1) && result == true)
		{
			//System.out.println("Right");
			if (!isObstacle(map[x][y + 1]))
			{
				result = spaceExplored(x,y+1);
			}
		}
		if (!inTempMap(x,y-1) && result == true)
		{
			//System.out.println("Left");
			if (!isObstacle(map[x][y - 1]))
			{
				result = spaceExplored(x,y - 1);
			}
		}
		
		return result;
	}
	
	private boolean inTempMap(int x, int y)
	{
		return mapTemp[x][y];
	}
	
	private void addTempMap(int x, int y)
	{
		//System.out.println("Adding to temp map ("+x+","+y+")");
		mapTemp[x][y] = true;
	}
	
	private void resetTempMap()
	{
		for (int i = 0; i < MAP_SEARCH_SIZE; i++)
		{
			for (int j = 0; j < MAP_SEARCH_SIZE; j++)
			{
				mapTemp[i][j] = false;
			}
		}
	}
	
	private void findPath(int x, int y)
	{
		ArrayList<Point> pathHistory = new ArrayList<Point>();
		System.out.println("Finding Path to ("+x+","+y+")!");
		resetTempMap();
		
		findPathR(locY, locX, x, y, pathHistory);		
	}
	
	private void printPathHistory(ArrayList<Point> pathHistory)
	{
		for (int i = 0; i < pathHistory.size(); i++)
		{
			System.out.print("(" + (int)pathHistory.get(i).getX() + "," + (int)pathHistory.get(i).getY() + "), ");
		}
	}
	
	private boolean findPathR(int x, int y, int xGoal, int yGoal, ArrayList<Point> pathHistory)
	{
		ArrayList<Point> pathHistoryClone = new ArrayList<Point>(pathHistory);
		//System.out.print("At point ("+x+","+y+")\n");
		addTempMap(x,y);
		boolean result = false;
		if (x == xGoal && y == yGoal)
		{
			result = true;
			//System.out.println("Found Goal! ");
			findPathResult = pathHistoryClone;
			
			// Holy fuck please lord forgive me for the below
			// This strips away the (x,x) coordinates that don't get pruned in the DFS
			boolean finishedRemoving = false;
			while (!finishedRemoving)
			{
				finishedRemoving = true;
				for (int i = 0; i < findPathResult.size(); i++)
				{
					if (i < findPathResult.size() - 1)
					{
						int xCo1 = (int)findPathResult.get(i).getX();
						int yCo1 = (int)findPathResult.get(i).getY();
						int xCo2 = (int)findPathResult.get(i+1).getX();
						int yCo2 = (int)findPathResult.get(i+1).getY();
						if (
						 (xCo2 == xCo1 + 2 && yCo2 == yCo1) || 
						 (yCo2 == yCo1 + 2 && xCo2 == xCo1) ||
						 (xCo2 == xCo1 - 2 && yCo2 == yCo1) ||
						 (yCo2 == yCo1 - 2 && xCo2 == xCo1)
						)
						{
							System.out.println("HALLO!!!");
							findPathResult.remove(i);
							finishedRemoving = false;
							break;
						}
					}
				}
			}
		}
		if (!result)
		{
			if (xGoal > x && Math.abs(xGoal - x) >= Math.abs(yGoal - y))
			{
				if (!result) result = findPathRinterim(x + 1, y, xGoal, yGoal, pathHistoryClone);
				if (!result) result = findPathRinterim(x, y + 1, xGoal, yGoal, pathHistoryClone);
				if (!result) result = findPathRinterim(x, y - 1, xGoal, yGoal, pathHistoryClone);
				if (!result) result = findPathRinterim(x - 1, y, xGoal, yGoal, pathHistoryClone);
			}
			else if (yGoal > y && Math.abs(xGoal - x) <= Math.abs(yGoal - y))
			{
				if (!result) result = findPathRinterim(x, y + 1, xGoal, yGoal, pathHistoryClone);
				if (!result) result = findPathRinterim(x - 1, y, xGoal, yGoal, pathHistoryClone);
				if (!result) result = findPathRinterim(x + 1, y, xGoal, yGoal, pathHistoryClone);
				if (!result) result = findPathRinterim(x, y - 1, xGoal, yGoal, pathHistoryClone);
			}
			else if (xGoal < x && Math.abs(xGoal - x) > Math.abs(yGoal - y))
			{
				if (!result) result = findPathRinterim(x - 1, y, xGoal, yGoal, pathHistoryClone);
				if (!result) result = findPathRinterim(x, y + 1, xGoal, yGoal, pathHistoryClone);
				if (!result) result = findPathRinterim(x, y - 1, xGoal, yGoal, pathHistoryClone);
				if (!result) result = findPathRinterim(x + 1, y, xGoal, yGoal, pathHistoryClone);
			}
			else if (yGoal < y && Math.abs(xGoal - x) < Math.abs(yGoal - y))
			{
				if (!result) result = findPathRinterim(x + 1, y, xGoal, yGoal, pathHistoryClone);
				if (!result) result = findPathRinterim(x, y + 1, xGoal, yGoal, pathHistoryClone);
				if (!result) result = findPathRinterim(x, y - 1, xGoal, yGoal, pathHistoryClone);
				if (!result) result = findPathRinterim(x - 1, y, xGoal, yGoal, pathHistoryClone);
			}
		}
		
		return result;
	}
	
	private ArrayList<Point> findPathResult;
	
	private boolean findPathRinterim(int x, int y, int xGoal, int yGoal, ArrayList<Point> pathHistory)
	{
		if (!inTempMap(x,y) && !isObstacle(map[x][y]) && !isBlank(map[x][y]))
		{
			pathHistory.add(new Point(x, y));
			return findPathR(x, y, xGoal, yGoal, pathHistory);
		}
		return false;
	}
	
	// ==============================================================================
	// =========================== STATE 2: Path Making =============================
	// ==============================================================================
		
	private char getPathMakingMove(char view[][])
	{
		return 0;
	}
	
	// ==============================================================================
	// ========================= STATE 3: Goal Searching ============================
	// ==============================================================================
		
	private char getGoalSearchingMove(char view[][])
	{
		return 0;
	}

	// ==============================================================================
	// ============================== View Stuff ====================================
	// ==============================================================================
	
	private char viewForward()
	{
		if (getDirection() == DIRECTION_UP) 	return map[locY - 1][locX];
		if (getDirection() == DIRECTION_DOWN) 	return map[locY + 1][locX];
		if (getDirection() == DIRECTION_LEFT) 	return map[locY][locX - 1];
		if (getDirection() == DIRECTION_RIGHT) 	return map[locY][locX + 1];
		return 0;
	}
	
	private char viewLeft()
	{
		if (getDirection() == DIRECTION_UP) 	return map[locY][locX - 1];
		if (getDirection() == DIRECTION_DOWN) 	return map[locY][locX + 1];
		if (getDirection() == DIRECTION_LEFT) 	return map[locY + 1][locX];
		if (getDirection() == DIRECTION_RIGHT) 	return map[locY - 1][locX];
		return 0;
	}
	
	private char viewRight()
	{
		if (getDirection() == DIRECTION_UP) 	return map[locY][locX + 1];
		if (getDirection() == DIRECTION_DOWN) 	return map[locY][locX - 1];
		if (getDirection() == DIRECTION_LEFT) 	return map[locY - 1][locX];
		if (getDirection() == DIRECTION_RIGHT) 	return map[locY + 1][locX];
		return 0;
	}
	
	private char viewBackward()
	{
		if (getDirection() == DIRECTION_UP) 	return map[locY + 1][locX];
		if (getDirection() == DIRECTION_DOWN) 	return map[locY - 1][locX];
		if (getDirection() == DIRECTION_LEFT) 	return map[locY][locX + 1];
		if (getDirection() == DIRECTION_RIGHT) 	return map[locY][locX - 1];
		return 0;
	}
	
	private int viewForwardWeight()
	{
		if (getDirection() == DIRECTION_UP) 	return mapWeight[locY - 1][locX];
		if (getDirection() == DIRECTION_DOWN) 	return mapWeight[locY + 1][locX];
		if (getDirection() == DIRECTION_LEFT) 	return mapWeight[locY][locX - 1];
		if (getDirection() == DIRECTION_RIGHT) 	return mapWeight[locY][locX + 1];
		return 0;
	}
	
	private int viewLeftWeight()
	{
		if (getDirection() == DIRECTION_UP) 	return mapWeight[locY][locX - 1];
		if (getDirection() == DIRECTION_DOWN) 	return mapWeight[locY][locX + 1];
		if (getDirection() == DIRECTION_LEFT) 	return mapWeight[locY + 1][locX];
		if (getDirection() == DIRECTION_RIGHT) 	return mapWeight[locY - 1][locX];
		return 0;
	}
	
	private int viewRightWeight()
	{
		if (getDirection() == DIRECTION_UP) 	return mapWeight[locY][locX + 1];
		if (getDirection() == DIRECTION_DOWN) 	return mapWeight[locY][locX - 1];
		if (getDirection() == DIRECTION_LEFT) 	return mapWeight[locY - 1][locX];
		if (getDirection() == DIRECTION_RIGHT) 	return mapWeight[locY + 1][locX];
		return 0;
	}
	
	private int viewBackwardWeight()
	{
		if (getDirection() == DIRECTION_UP) 	return mapWeight[locY + 1][locX];
		if (getDirection() == DIRECTION_DOWN) 	return mapWeight[locY - 1][locX];
		if (getDirection() == DIRECTION_LEFT) 	return mapWeight[locY][locX + 1];
		if (getDirection() == DIRECTION_RIGHT) 	return mapWeight[locY][locX - 1];
		return 0;
	}
	
	// ==============================================================================
	// =========================== Series of Actions ================================
	// ==============================================================================
	
	private void doAction(char chr)
	{
		switch (chr)
		{
		case ACTION_MOVEFORWARD:
			actionForward();
		break;
		case ACTION_TURNLEFT:
			actionLeft();
		break;
		case ACTION_TURNRIGHT:
			actionRight();
		break;
		}
	}
	private char actionForward()
	{
		if (getDirection() == DIRECTION_UP) 	locY--;
		if (getDirection() == DIRECTION_DOWN) 	locY++;
		if (getDirection() == DIRECTION_RIGHT) 	locX++;
		if (getDirection() == DIRECTION_LEFT) 	locX--;
		return ACTION_MOVEFORWARD;
	}
	
	private char actionLeft()
	{
		rotate(ROTATE_CCW);
		return ACTION_TURNLEFT;
	}
	
	private char actionRight()
	{
		rotate(ROTATE_CW);
		return ACTION_TURNRIGHT;
	}
	
	// ==============================================================================
	// =========================== Rotating / Direction =============================
	// ==============================================================================
			
	private void rotate(int direction)
	{
		this.direction += direction;
		if (this.direction < 0)
		{
			setDirection(NUM_DIRECTIONS - 1);
		}
		else if (this.direction >= NUM_DIRECTIONS)
		{
			setDirection(0);
		}
	}
	
	private void setDirection(int direction)
	{
		this.direction = direction;
	}
	
	private int getDirection()
	{
		return this.direction;
	}
	
	// ==============================================================================
	// =========================== Checking Cells ===================================
	// ==============================================================================
	
	private boolean isObstacle(char chr)
	{
		if (chr == OBSTACLE_TREE || chr == OBSTACLE_DOOR || chr == OBSTACLE_WALL || chr == OBSTACLE_WATER)
		{
			return true;
		}
		return false;
	}
	
	private boolean isBlank(char chr)
	{
		if (chr == OBSTACLE_BLANK || chr == OBSTACLE_UNSEEN)
		{
			return true;
		}
		return false;
	}
	
	private boolean isTool(char chr)
	{
		if (chr == TOOL_AXE || chr == TOOL_KEY || chr == TOOL_DYNAMITE || chr == TOOL_GOLD)
		{
			return true;
		}
		return false;
	}

	// ==============================================================================
	// ================================= Tools ======================================
	// ==============================================================================
	
	private void toolAdd(char tool)
	{
		toolChange(tool, 1);
	}
	private void toolRemove(char tool)
	{
		toolChange(tool, -1);
	}
	
	private boolean toolHave(char tool)
	{
		int numberOfInterest = toolCount(tool);
		if (numberOfInterest > 0)
		{
			return true;
		}
		return false;
	}
	
	private int toolCount(char tool)
	{
		int numberOfInterest = 0;
		switch (tool)
		{
		case TOOL_AXE:
			numberOfInterest = numToolsAxe;
		break;
		case TOOL_KEY:
			numberOfInterest = numToolsKey;
		break;
		case TOOL_DYNAMITE:
			numberOfInterest = numToolsDynamite;
		break;
		case TOOL_GOLD:
			numberOfInterest = numToolsGold;
		break;
		}
		return numberOfInterest;
	}
	
	private void toolChange(char tool, int change)
	{
		switch (tool)
		{
		case TOOL_AXE:
			numToolsAxe += change;
		break;
		case TOOL_KEY:
			numToolsKey += change;
		break;
		case TOOL_DYNAMITE:
			numToolsDynamite += change;
		break;
		case TOOL_GOLD:
			numToolsGold += change;
		break;
		}
	}
	
	// ==============================================================================
	// ================================= Other ======================================
	// ==============================================================================
	
	private void changeState(int state)
	{
		this.state = state;
	}
	
	// ==============================================================================
	// ================================= Fields =====================================
	// ==============================================================================
	
	private char[][] map;
	private boolean[][] mapTemp;
	private int[][] mapWeight;
	
	private int greatestMapWeightX;
	private int greatestMapWeightY;
	
	private int iterations;
	private int direction;
	private int state;
	private int locX;
	private int locY;
	private int startX;
	private int startY;
	
	private int mapBoundLeft;
	private int mapBoundRight;
	private int mapBoundTop;
	private int mapBoundBottom;
	
	private int numToolsAxe;
	private int numToolsKey;
	private int numToolsDynamite;
	private int numToolsGold;
	
	private int checkingForTools;
	
	// ==============================================================================
	// ============================= Definitions ====================================
	// ==============================================================================
	
	// Tools
	private static final char TOOL_AXE 			= 'a';
	private static final char TOOL_KEY 			= 'k';
	private static final char TOOL_DYNAMITE		= 'd';
	private static final char TOOL_GOLD 		= 'g';
	
	// Rotations / Directions
	private static final int ROTATE_CCW 		= 1;
	private static final int ROTATE_CW 			= -1;
	private static final int NUM_DIRECTIONS 	= 4;
	private static final int DIRECTION_UP	 	= 0;
	private static final int DIRECTION_LEFT 	= 1;
	private static final int DIRECTION_DOWN 	= 2;
	private static final int DIRECTION_RIGHT 	= 3;
	private static final int DIRECTION_RELATIVE_FORWARD 	= 0;
	private static final int DIRECTION_RELATIVE_LEFT 		= 1;
	private static final int DIRECTION_RELATIVE_RIGHT 		= 2;
	private static final int DIRECTION_RELATIVE_BACKWARD 	= 3;
	
	
	// Map Properties
	private static final int MAP_SEARCH_SIZE 	= 170;
	private static final int MAP_VIEW_SIZE 		= 5;
	private static final int MAP_VIEW_CENTRE 	= 2;
	private static final int MAP_VIEW_SPACE_INFRONT_X = 1;
	private static final int MAP_VIEW_SPACE_INFRONT_Y = 2;
	// States
	private static final int STATE_UNFOGGING 	= 0;
	private static final int STATE_MAKINGPATH 	= 1;
	private static final int STATE_SEARCHINGGOAL= 2;

	// Obstacles
	private static final char OBSTACLE_UNSEEN	= 0;
	private static final char OBSTACLE_BLANK 	= ' ';
	private static final char OBSTACLE_EXPLORED	= '.';
	private static final char OBSTACLE_TREE 	= 'T';
	private static final char OBSTACLE_DOOR 	= '-';
	private static final char OBSTACLE_WALL		= '*';
	private static final char OBSTACLE_WATER 	= '~';
	
	// Actions
	private static final char ACTION_TURNRIGHT	= 'R';
	private static final char ACTION_TURNLEFT	= 'L';
	private static final char ACTION_MOVEFORWARD= 'F';
   
	// ==============================================================================
	// ============================= Map Printing ===================================
	// ==============================================================================
	
	private void printTempMap()
	{
		for (int j = mapBoundTop; j <= mapBoundBottom; j++)
		{
			System.out.print(j + " ");
		}
		System.out.print("   \n");
		for (int i = mapBoundLeft; i <= mapBoundRight; i++)
		{
			System.out.print(i + " ");
			for (int j = mapBoundTop; j <= mapBoundBottom; j++)
			{
				if (mapTemp[i][j])
				{
					System.out.print(".  ");
				}
				else
				{
					System.out.print("   ");
				}
			}
			System.out.print(" " + i);
			System.out.println();
		}
		System.out.print("   ");
		for (int j = mapBoundTop; j <= mapBoundBottom; j++)
		{
			System.out.print(j + " ");
		}
		System.out.print("   \n");		
	}
	
	private void printWeightMap()
	{
		System.out.println("Greatest Weight ("+greatestMapWeightX+","+greatestMapWeightY+"): " + mapWeight[greatestMapWeightX][greatestMapWeightY]);
		System.out.println("Steps("+iterations+") Direction(" + getDirection() + ") at location (" + locX + "," + locY + "), Axes("+toolCount(TOOL_AXE)+"), Keys("+toolCount(TOOL_KEY)+"), Dyns("+toolCount(TOOL_DYNAMITE)+"), Golds("+toolCount(TOOL_GOLD)+")");		
		System.out.print("   ");
		for (int j = mapBoundTop; j <= mapBoundBottom; j++)
		{
			System.out.print(j + " ");
		}
		System.out.print("   \n");
		for (int i = mapBoundLeft; i <= mapBoundRight; i++)
		{
			System.out.print(i + " ");
			for (int j = mapBoundTop; j <= mapBoundBottom; j++)
			{
				if (locY == i && locX == j)
				{
					System.out.print(".  ");
				}
				else if (mapWeight[i][j] > 99)
				{
					System.out.print(mapWeight[i][j] + "");
				}
				else if (mapWeight[i][j] > 9 || mapWeight[i][j] < 0)
				{
					System.out.print(mapWeight[i][j] + " ");
				}
				else
				{
					System.out.print(mapWeight[i][j] + "  ");
				}
				
			}
			System.out.print(" " + i);
			System.out.println();
		}
		System.out.print("   ");
		for (int j = mapBoundTop; j <= mapBoundBottom; j++)
		{
			System.out.print(j + " ");
		}
		System.out.print("   \n");
	}
	
	private void printMap()
	{
		System.out.println("Steps("+iterations+") Direction(" + getDirection() + ") at location (" + locX + "," + locY + "), Axes("+toolCount(TOOL_AXE)+"), Keys("+toolCount(TOOL_KEY)+"), Dyns("+toolCount(TOOL_DYNAMITE)+"), Golds("+toolCount(TOOL_GOLD)+")");		
		System.out.print("   ");
		for (int j = mapBoundTop; j <= mapBoundBottom; j++)
		{
			System.out.print(j + " ");
		}
		System.out.print("   \n");
		for (int i = mapBoundLeft; i <= mapBoundRight; i++)
		{
			System.out.print(i + " ");
			for (int j = mapBoundTop; j <= mapBoundBottom; j++)
			{
				if (i == locY && j == locX)
				{
					if (getDirection() == DIRECTION_UP) System.out.print("^  ");
					if (getDirection() == DIRECTION_LEFT) System.out.print("<  ");
					if (getDirection() == DIRECTION_RIGHT) System.out.print(">  ");
					if (getDirection() == DIRECTION_DOWN) System.out.print("v  ");
				}
				else
				{
					if (map[i][j] == 0) { map[i][j] = ' '; }
					System.out.print(map[i][j] + "  ");
				}
			}
			System.out.print(" " + i);
			System.out.println();
		}
		System.out.print("   ");
		for (int j = mapBoundTop; j <= mapBoundBottom; j++)
		{
			System.out.print(j + " ");
		}
		System.out.print("   \n");
	}
	
	
	
	
	
	
	
	
	
	
	
   
	// ==============================================================================
	// ================================= Other ======================================
	// ==============================================================================
   
   void print_view( char view[][] )
   {
      int i,j;

      System.out.println("\n+-----+");
      for( i=0; i < 5; i++ ) {
         System.out.print("|");
         for( j=0; j < 5; j++ ) {
            if(( i == 2 )&&( j == 2 )) {
               System.out.print('^');
            }
            else {
               System.out.print( view[i][j] );
            }
         }
         System.out.println("|");
      }
      System.out.println("+-----+");
   }

   public static void main( String[] args )
   {
      InputStream in  = null;
      OutputStream out= null;
      Socket socket   = null;
      Agent  agent    = new Agent();
      char   view[][] = new char[10][10];
      char   action   = 'F';
      int port;
      int ch;
      int i,j;

      if( args.length < 2 ) {
         System.out.println("Usage: java Agent -p <port>\n");
         System.exit(-1);
      }

      port = Integer.parseInt( args[1] );

      try { // open socket to Game Engine
         socket = new Socket( "localhost", port );
         in  = socket.getInputStream();
         out = socket.getOutputStream();
      }
      catch( IOException e ) {
         System.out.println("Could not bind to port: "+port);
         System.exit(-1);
      }
      
      try { // scan 5-by-5 wintow around current location
         while( true ) {
            for( i=0; i < 5; i++ ) {
               for( j=0; j < 5; j++ ) {
                  if( !(( i == 2 )&&( j == 2 ))) {
                     ch = in.read();
                     if( ch == -1 ) {
                        System.exit(-1);
                     }
                     view[i][j] = (char) ch;
                  }
               }
            }
            agent.print_view( view ); // COMMENT THIS OUT BEFORE SUBMISSION
            
            action = agent.get_action( view );
            out.write( action );
            //agent.printTempMap();
            //agent.printWeightMap();
            agent.printMap();
            
         }
      }
      catch( IOException e ) {
         System.out.println("Lost connection to port: "+ port );
         System.exit(-1);
      }
      finally {
         try {
            socket.close();
         }
         catch( IOException e ) {}
      }
   }
   
}
