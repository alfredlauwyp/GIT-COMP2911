import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Agent4 {

	/**
	 * Construct the Agent class
	 */
	public Agent4()
	{
		initialiseFields();
	}
	
	/**
	 * Initialise all relevant fields in order
	 *  to set up the Agent class for valid calls 
	 *  to "get_action"
	 */
	private void initialiseFields()
	{
		this.direction = 0;
		
		this.map = new char[MAP_SEARCH_SIZE][MAP_SEARCH_SIZE];
		this.mapTemp1 = new boolean[MAP_SEARCH_SIZE][MAP_SEARCH_SIZE];
		this.mapTemp2 = new boolean[MAP_SEARCH_SIZE][MAP_SEARCH_SIZE];
		this.mapTemp3 = new boolean[MAP_SEARCH_SIZE][MAP_SEARCH_SIZE];
		
		this.locY = this.startX = (MAP_SEARCH_SIZE / 2);
		this.locX = this.startY = (MAP_SEARCH_SIZE / 2);
		
		this.obstacleToDynamite = new Coordinate(NO_POINT, NO_POINT);
		this.obstacleToDynamiteLongTerm = this.obstacleToDynamite;
		
		this.findingPath = true;
		this.lastGetToCoord = new Coordinate(NO_POINT, NO_POINT);
	}
	
	/**
	 * Given a "view" from the Rogue, determine what action
	 *  should be returned for the next move. The action is chosen
	 *  specifically to maximise the speed at which the goal state is
	 *  reached
	 * @param view Array of characters from the Rogue that represents
	 *  a specific segment of the map that is visible at a particular moment
	 *  in time
	 * @return Action to take (i.e. "the next move")
	 */
	public char get_action(char view[][])
	{
		char move = 0;
		addMapFeatures(view);
		
		if (toolHave(TOOL_GOLD))										move = getTo(startX, startY);			
		else if (canGetTo(TOOL_GOLD))									move = getTo(TOOL_GOLD);
		else if (toolHave(TOOL_AXE) && viewForward() == OBSTACLE_TREE)	move = ACTION_CHOP;
		else if (toolHave(TOOL_KEY) && viewForward() == OBSTACLE_DOOR)	move = ACTION_OPEN;
		else if (toolHave(TOOL_DYNAMITE) && inFrontOfDynamite())		move = ACTION_BLAST;
		else if (!toolHave(TOOL_AXE) && canGetTo(TOOL_AXE))				move = getTo(TOOL_AXE);
		else if (!toolHave(TOOL_KEY) && canGetTo(TOOL_KEY))				move = getTo(TOOL_KEY);
		else if (!toolHave(TOOL_DYNAMITE) && canGetTo(TOOL_DYNAMITE))	move = getTo(TOOL_DYNAMITE);
		else if (toolHave(TOOL_KEY) && canGetTo(OBSTACLE_DOOR))			move = getTo(OBSTACLE_DOOR);
		else if (toolHave(TOOL_AXE) && canGetTo(OBSTACLE_TREE))			move = getTo(OBSTACLE_TREE);
		else if (isPoint(findNearestUnseen()))							move = getTo(findNearestUnseen());
		else if (toolHave(TOOL_DYNAMITE) && canGetToBehindObstacle())	move = getTo(obstacleToDynamite.getX(), obstacleToDynamite.getY());
		
		move = checkForTools(move); //Check if the item in front of you is a tool
		doAction(move); // Carry out the decided action
		return move;		
	}
	
	
	

	private Coordinate findNearest(char chr)
	{
		int itemY = NO_POINT;
		int itemX = NO_POINT;
		int distanceAway = 1000;
		Coordinate p = new Coordinate(itemY, itemX);
		for (int i = mapBoundLeft; i <= mapBoundRight; i++)
		{
			for (int j = mapBoundTop; j <= mapBoundBottom; j++)
			{
				if (map[i][j] == chr)
				{
					if (Math.abs(p.getX() - i) + Math.abs(p.getY() - i) < distanceAway && canGetTo(i, j))
					{
						p = new Coordinate(i, j);
					}
				}
			}
		}
		
		return p;
	}
	private Coordinate lastGetToCoord;
	
	
	private char getTo(int x, int y)
	{
		return getToMid(x, y);
	}
	
	private char getTo(char chr)
	{
		return getToMid(findNearest(chr).getX(), findNearest(chr).getY());
	}
	
	private char getTo(Coordinate coordinate)
	{
		return getToMid(coordinate.getX(), coordinate.getY());
	}
	
	private char getToMid(int x, int y)
	{
		char move = 0;
		if (findingPath)
		{
			findPath(x, y);			
			findingPath = false;
		}
		if (findPathResult.size() > 0)
		{
			int xCoord = findPathResult.get(0).getX();
			int yCoord = findPathResult.get(0).getY();
			if (locX - 1 == xCoord && locY == yCoord) // Up
			{ 
				if (getDirection() == DIRECTION_UP)
				{
					findPathResult.remove(0);
					move = ACTION_MOVEFORWARD;
				}
				else if (getDirection() == DIRECTION_RIGHT) move = ACTION_TURNLEFT;
				else move = ACTION_TURNRIGHT;
			}
			else if (locX + 1 == xCoord  && locY == yCoord)
			{
				if (getDirection() == DIRECTION_DOWN) // Down
				{
					findPathResult.remove(0);
					move = ACTION_MOVEFORWARD;
				}
				else if (getDirection() == DIRECTION_RIGHT) move = ACTION_TURNRIGHT;
				else move = ACTION_TURNLEFT;
			}
			else if (locX == xCoord && locY - 1== yCoord) // Left
			{
				if (getDirection() == DIRECTION_LEFT)
				{
					findPathResult.remove(0);
					move = ACTION_MOVEFORWARD;
				}
				else if (getDirection() == DIRECTION_UP) move = ACTION_TURNLEFT;
				else move = ACTION_TURNRIGHT;
			}
			else if (locX == xCoord && locY + 1 == yCoord) // Right
			{
				if (getDirection() == DIRECTION_RIGHT)
				{
					findPathResult.remove(0);
					move = ACTION_MOVEFORWARD;
				}
				else if (getDirection() == DIRECTION_DOWN) move = ACTION_TURNLEFT;
				else move = ACTION_TURNRIGHT;
			}
			else if (locX == xCoord && locY == yCoord)
			{
				findPathResult.remove(0);
			}
			else
			{
				findingPath = true;
			}
		}
		if (findPathResult.size() == 0) findingPath = true;
		lastGetToCoord.setX(x);
		lastGetToCoord.setY(y);
		return move;
	}	
	private boolean findingPath;
	
	
	
	
	
	
	
	
	
	
	
	
	
	private boolean canGetTo(char chr)
	{
		return canGetToMid(findNearest(chr).getX(), findNearest(chr).getY());
	}
	
	private boolean canGetTo(int x, int y)
	{
		return canGetToMid(x, y);
	}
	
	private boolean canGetToMid(int x, int y)
	{
		if (isPoint(x) && isPoint(y))
		{
			resetTempMap();
			return canGetToR(locX, locY, x, y);
		}
		return false; // If no points are passed,
		
	}
	
	private boolean canGetToR(int x, int y, int finalX, int finalY)
	{
		addTempMap(x,y);
		boolean result = false;
		
		if (x == finalX && y == finalY) return true;		
		
		if (!inTempMap(x+1,y) && result == false && !isWater(map[x+1][y]) && (!isObstacle(map[x + 1][y]) || (x+1==finalX && y==finalY)) && !isBlank(map[x + 1][y]))
		{
			result = canGetToR(x+1,y, finalX, finalY); // down
		}
		if (!inTempMap(x-1,y) && result == false && !isWater(map[x-1][y]) &&(!isObstacle(map[x - 1][y]) || (x-1==finalX && y==finalY)) && !isBlank(map[x - 1][y]))
		{
			result = canGetToR(x-1,y, finalX, finalY); // up
		}		
		if (!inTempMap(x,y+1) && result == false && !isWater(map[x][y+1]) &&(!isObstacle(map[x][y+1]) || (x==finalX && y+1==finalY)) && !isBlank(map[x][y + 1]))
		{
			result = canGetToR(x,y+1, finalX, finalY); // right
		}
		if (!inTempMap(x,y-1) && result == false && !isWater(map[x][y-1]) &&(!isObstacle(map[x][y - 1]) || (x==finalX && y-1==finalY)) && !isBlank(map[x][y - 1]))
		{
			result = canGetToR(x,y - 1, finalX, finalY); // left 
		}		
		return result;
	}
	
	
	
	

	
	
	
	
	
	private Coordinate obstacleToDynamite;
	private Coordinate obstacleToDynamiteLongTerm;
	
	private boolean canGetToBehindObstacle()
	{
		boolean solution = false;
		
		char items[] = {TOOL_GOLD, TOOL_DYNAMITE, TOOL_AXE, TOOL_KEY};
		
		int count = 0;
		while (!solution && count < items.length)
		{
			resetTempMap3();
			resetTempMap2();
			obstacleToDynamite = new Coordinate(NO_POINT, NO_POINT);
			
			if (isPoint(obstacleToDynamiteLongTerm))
			{
				obstacleToDynamite = obstacleToDynamiteLongTerm;
				return true;
			}						
			
			solution = canGetToBehindObstacleR(locX, locY, 1, items[count]);
			count++;
		}
		if (!solution)
		{
			resetTempMap3();
			resetTempMap2();
			solution = canGetToBehindObstacleR(locX, locY, 1, OBSTACLE_EXPLORED);
		}
		obstacleToDynamiteLongTerm = obstacleToDynamite;
		return solution;
	}
	
	private void wait(int time) { try { Thread.sleep(time);} catch(InterruptedException e) { System.out.println("Interrupted"); } }
	
	private boolean canGetToBehindObstacleR(int x, int y, int getOutOfJailFree, char itemOfInterest)
	{
		boolean result = false;
		if (getOutOfJailFree == 1)
		{
			addTempMap3(x,y);
			resetTempMap2();
		}
		else
		{
			addTempMap2(x, y);
		}
		
		if (map[x][y] == itemOfInterest && getOutOfJailFree == 0 && !canGetTo(x,y))
			{
			return true;		
			}
		
		if (!result) { result = canGetToBehindObstacleRInterim(x+1, y, getOutOfJailFree, itemOfInterest); } // down
		if (!result) { result = canGetToBehindObstacleRInterim(x-1, y, getOutOfJailFree, itemOfInterest); } // up
		if (!result) { result = canGetToBehindObstacleRInterim(x, y+1, getOutOfJailFree, itemOfInterest); } // right
		if (!result) { result = canGetToBehindObstacleRInterim(x, y-1, getOutOfJailFree, itemOfInterest); } // left		
		
		return result;
	}
	
	private boolean canGetToBehindObstacleRInterim(int x, int y, int getOutOfJailFree, char itemOfInterest)
	{
		if (itemOfInterest == '.')
		{
			System.out.println("RAWR");
			printMap();
			printTempMap3();
			printTempMap2();
			wait(50);
		}
		if (!inTempMap3(x,y) && !isWater(map[x][y]) && (!isObstacle(map[x][y]) || getOutOfJailFree > 0) && !isBlank(map[x][y]))
		{			
			if (getOutOfJailFree > 0)
			{
				if (isObstacle(map[x][y]))
				{
					obstacleToDynamite = new Coordinate(x, y);
					getOutOfJailFree--;
				}
				return canGetToBehindObstacleR(x,y, getOutOfJailFree, itemOfInterest);
			}
			else if (!inTempMap2(x,y))
			{
				return canGetToBehindObstacleR(x,y, getOutOfJailFree, itemOfInterest);
			}	 
		}
		return false;
	}
	
	
	
	
	
	
	
	
	
	
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
				if (mapTemp1[i][j])
				{
					System.out.print(".  ");
				}
				else
				{
					System.out.print("   ");
				}
			}
			System.out.print(" " + i);
			System.out.print("\n");
		}
		System.out.print("   ");
		for (int j = mapBoundTop; j <= mapBoundBottom; j++)
		{
			System.out.print(j + " ");
		}
		System.out.print("   \n");		
	}
	
	private void printTempMap2()
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
				if (mapTemp2[i][j])
				{
					System.out.print(".  ");
				}
				else
				{
					System.out.print("   ");
				}
			}
			System.out.print(" " + i);
			System.out.print("\n");
		}
		System.out.print("   ");
		for (int j = mapBoundTop; j <= mapBoundBottom; j++)
		{
			System.out.print(j + " ");
		}
		System.out.print("   \n");		
	}
	
	private void printTempMap3()
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
				if (mapTemp3[i][j])
				{
					System.out.print(".  ");
				}
				else
				{
					System.out.print("   ");
				}
			}
			System.out.print(" " + i);
			System.out.print("\n");
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
		System.out.print("Direction(" + getDirection() + ") at location (" + locY + "," + locX + "), Axes("+toolCount(TOOL_AXE)+"), Keys("+toolCount(TOOL_KEY)+"), Dyns("+toolCount(TOOL_DYNAMITE)+"), Golds("+toolCount(TOOL_GOLD)+")\n");		
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
				if (i == obstacleToDynamite.getX() && j == obstacleToDynamite.getY())
				{
					System.out.print("X ");
				}
				else if (i == locX && j == locY)
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
			System.out.print("\n");
		}
		System.out.print("   ");
		for (int j = mapBoundTop; j <= mapBoundBottom; j++)
		{
			System.out.print(j + " ");
		}
		System.out.print("   \n");
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
		
	private void findPath(int x, int y)
	{
		ArrayList<Coordinate> pathHistory = new ArrayList<Coordinate>();
		resetTempMap();
		findPathR(locX, locY, x, y, pathHistory);
	}
	
	private boolean findPathR(int x, int y, int xGoal, int yGoal, ArrayList<Coordinate> pathHistory)
	{
		ArrayList<Coordinate> pathHistoryClone = new ArrayList<Coordinate>(pathHistory);
		addTempMap(x,y);
		boolean result = false;
		if (x == xGoal && y == yGoal)
		{
			result = true;
			findPathResult = pathHistoryClone;
			
			// This strips away the (x,x) coordinates that don't get pruned in the DFS
			boolean finishedRemoving = false;
			while (!finishedRemoving)
			{
				finishedRemoving = true;
				for (int i = 0; i < findPathResult.size(); i++)
				{
					if (i < findPathResult.size() - 1) {
						int xCo1 = (int)findPathResult.get(i).getX();
						int yCo1 = (int)findPathResult.get(i).getY();
						int xCo2 = (int)findPathResult.get(i+1).getX();
						int yCo2 = (int)findPathResult.get(i+1).getY();
						if ((Math.abs(xCo2 - xCo1) > 0 && Math.abs(yCo2 - yCo1) > 0) || Math.abs(yCo2 - yCo1) > 1 || Math.abs(xCo2 - xCo1) > 1)
						{
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
			else if (xGoal < x && Math.abs(xGoal - x) >= Math.abs(yGoal - y))
			{
				if (!result) result = findPathRinterim(x - 1, y, xGoal, yGoal, pathHistoryClone);
				if (!result) result = findPathRinterim(x, y - 1, xGoal, yGoal, pathHistoryClone);
				if (!result) result = findPathRinterim(x, y + 1, xGoal, yGoal, pathHistoryClone);
				if (!result) result = findPathRinterim(x + 1, y, xGoal, yGoal, pathHistoryClone);
			}
			else if (yGoal < y && Math.abs(xGoal - x) <= Math.abs(yGoal - y))
			{
				if (!result) result = findPathRinterim(x, y - 1, xGoal, yGoal, pathHistoryClone);
				if (!result) result = findPathRinterim(x + 1, y, xGoal, yGoal, pathHistoryClone);
				if (!result) result = findPathRinterim(x - 1, y, xGoal, yGoal, pathHistoryClone);
				if (!result) result = findPathRinterim(x, y + 1, xGoal, yGoal, pathHistoryClone);
			}
		}		
		return result;
	}
	
	private ArrayList<Coordinate> findPathResult;
	
	private boolean findPathRinterim(int x, int y, int xGoal, int yGoal, ArrayList<Coordinate> pathHistory)
	{
		if (!inTempMap(x,y) && !isWater(map[x][y]) && (!isObstacle(map[x][y]) || (x == xGoal && y == yGoal)) && !isBlank(map[x][y]))
		{
			pathHistory.add(new Coordinate(x, y));
			return findPathR(x, y, xGoal, yGoal, pathHistory);
		}
		return false;
	}
	
	
	
	
	
	
	
	
	
	
	private Coordinate findNearestUnseen()
	{
		int itemY = NO_POINT;
		int itemX = NO_POINT;
		int distanceAway = 1000;
		Coordinate p = new Coordinate(itemY, itemX);
		for (int i = mapBoundLeft; i <= mapBoundRight; i++)
		{
			for (int j = mapBoundTop; j <= mapBoundBottom; j++)
			{
				if (
						isBlank(map[i+2][j-2]) ||
						isBlank(map[i+2][j-1]) ||
						isBlank(map[i+2][j]) ||
						isBlank(map[i+2][j+1]) ||
						isBlank(map[i+2][j+2]) ||
						
						isBlank(map[i+1][j-2]) ||
						isBlank(map[i+1][j+2]) ||

						isBlank(map[i][j-2]) ||
						isBlank(map[i][j+2]) ||
						
						isBlank(map[i-1][j-2]) ||
						isBlank(map[i-1][j+2]) ||
						
						isBlank(map[i-2][j-2]) ||
						isBlank(map[i-2][j-1]) ||
						isBlank(map[i-2][j]) ||
						isBlank(map[i-2][j+1]) ||
						isBlank(map[i-2][j+2])
				)
				{
					if (map[i][j] == OBSTACLE_EXPLORED)
					{
						int diffX = Math.abs(locX - i);
						int diffY = Math.abs(locY - j);
						if (diffX + diffY < distanceAway && canGetTo(i, j))
						{
							distanceAway = diffX + diffY;
							p = new Coordinate(i, j);
						}
					}
				}
			}
		}
		return p;
	}
	
	
		
	
		
	// ==============================================================================
	// =============================== Temporary Map ================================
	// ==============================================================================

	/**
	 * Check if item at a given location has already been
	 *  explored in the primary temporary map
	 * @param x X-coordinate of item to check if in map
	 * @param y Y-coordinate of item to check if in map
	 * @return If item at location has already been explored in primary
	 *  temporary map
	 */
	private boolean inTempMap(int x, int y)
	{
		return mapTemp1[x][y];
	}
	
	/**
	 * Add an item at a given location to the primary
	 *  temporary map
	 * @param x X-coordinate of item in map
	 * @param y Y-coordinate of item in map
	 */
	private void addTempMap(int x, int y)
	{
		mapTemp1[x][y] = true;
	}

	/**
	 * Reset all values to default in the primary temporary
	 *  map
	 */
	private void resetTempMap()
	{
		for (int i = 0; i < MAP_SEARCH_SIZE; i++)
		{
			for (int j = 0; j < MAP_SEARCH_SIZE; j++)
			{
				mapTemp1[i][j] = false;
			}
		}
	}
	
	/**
	 * Check if item at a given location has already been
	 *  explored in the second temporary map
	 * @param x X-coordinate of item to check if in map
	 * @param y Y-coordinate of item to check if in map
	 * @return If item at location has already been explored in second
	 *  temporary map
	 */
	private boolean inTempMap2(int x, int y)
	{
		return mapTemp2[x][y];
	}
	
	/**
	 * Add an item at a given location to the second
	 *  temporary map
	 * @param x X-coordinate of item in map
	 * @param y Y-coordinate of item in map
	 */
	private void addTempMap2(int x, int y)
	{
		mapTemp2[x][y] = true;
	}
	
	/**
	 * Reset all values to default in the second temporary
	 *  map
	 */
	private void resetTempMap2()
	{
		for (int i = 0; i < MAP_SEARCH_SIZE; i++)
		{
			for (int j = 0; j < MAP_SEARCH_SIZE; j++)
			{
				mapTemp2[i][j] = false;
			}
		}
	}
	

	/**
	 * Check if item at a given location has already been
	 *  explored in the second temporary map
	 * @param x X-coordinate of item to check if in map
	 * @param y Y-coordinate of item to check if in map
	 * @return If item at location has already been explored in second
	 *  temporary map
	 */
	private boolean inTempMap3(int x, int y)
	{
		return mapTemp3[x][y];
	}
	
	/**
	 * Add an item at a given location to the second
	 *  temporary map
	 * @param x X-coordinate of item in map
	 * @param y Y-coordinate of item in map
	 */
	private void addTempMap3(int x, int y)
	{
		mapTemp3[x][y] = true;
	}
	
	/**
	 * Reset all values to default in the second temporary
	 *  map
	 */
	private void resetTempMap3()
	{
		for (int i = 0; i < MAP_SEARCH_SIZE; i++)
		{
			for (int j = 0; j < MAP_SEARCH_SIZE; j++)
			{
				mapTemp3[i][j] = false;
			}
		}
	}
	
	// ==============================================================================
	// ======================= Adding Map / Item Features ===========================
	// ==============================================================================
	
	/**
	 * Given a "view" from the Rogue, with knowledge of the Agent's direction and location,
	 *  update the map that is stored within this class.
	 * @param view
	 */
	private void addMapFeatures(char view[][])
	{
		for (int i = -MAP_VIEW_HALF_SIZE; i <= MAP_VIEW_HALF_SIZE; i++)
		{
			for (int j = -MAP_VIEW_HALF_SIZE; j <= MAP_VIEW_HALF_SIZE; j++)
			{
				if (isBlank(relativeChar(view, i, j)))
				{
					view[i][j] = OBSTACLE_EXPLORED;
				}				
				
				if (getDirection() == DIRECTION_UP)
				{
					map[locX + i][locY + j] = relativeChar(view, -i, -j);
				}
				if (getDirection() == DIRECTION_DOWN)
				{
					map[locX + i][locY - j] = relativeChar(view, i, -j);
				}
				if (getDirection() == DIRECTION_RIGHT)
				{
					map[locX - j][locY + i] = relativeChar(view, i, j);
				}
				if (getDirection() == DIRECTION_LEFT)
				{
					map[locX + j][locY + i] = relativeChar(view, -i, j);
				}
			}
		}		
		updateMapBounds();
	}
	
	/**
	 * Update the bounds of the current map. This in essence 'trims'
	 *  the parts of the map that have yet to be explored, so that when
	 *  printing and searching the map we only have to concern ourselves
	 *  with the section of the map that has been explored / has relevant
	 *  information
	 */
	private void updateMapBounds()
	{
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
	
	/**
	 * Checks if the item directly in front of you is a tool, and if
	 *  it is forces a forward movement to collect it
	 * @param prevMove Move to execute if no tool in front of you
	 * @return Move to execute
	 */
	private char checkForTools(char prevMove)
	{
		char move = prevMove;
		if (isTool(viewForward()))
		{
			toolAdd(viewForward());
			if (getDirection() == DIRECTION_UP) 	map[locX - 1][locY] = OBSTACLE_EXPLORED;
			if (getDirection() == DIRECTION_DOWN)	map[locX + 1][locY] = OBSTACLE_EXPLORED;
			if (getDirection() == DIRECTION_LEFT) 	map[locX][locY - 1] = OBSTACLE_EXPLORED;
			if (getDirection() == DIRECTION_RIGHT)	map[locX][locY + 1] = OBSTACLE_EXPLORED;
			move = ACTION_MOVEFORWARD;
		}
		return move;
	}
		
	// ==============================================================================
	// ============================== View Stuff ====================================
	// ==============================================================================
	
	/**
	 * Return X location of cell in front of agent given it's current
	 *  direction
	 * @return X location of cell in front of agent given it's current location
	 */
	private int viewForwardX()
	{
		if (getDirection() == DIRECTION_UP) 	return locX - 1;
		if (getDirection() == DIRECTION_DOWN) 	return locX + 1;
		if (getDirection() == DIRECTION_LEFT) 	return locX;
		if (getDirection() == DIRECTION_RIGHT) 	return locX;
		return 0;
	}
	
	/**
	 * Return Y location of cell in front of agent given it's current
	 *  direction
	 * @return Y location of cell in front of agent given it's current location
	 */
	private int viewForwardY()
	{
		if (getDirection() == DIRECTION_UP) 	return locY;
		if (getDirection() == DIRECTION_DOWN) 	return locY;
		if (getDirection() == DIRECTION_LEFT) 	return locY - 1;
		if (getDirection() == DIRECTION_RIGHT) 	return locY + 1;
		return 0;
	}
	
	/**
	 * Return the item that is in front of the agent given it's
	 *  current direction
	 * @return Item in front of agent given it's current direction
	 */
	private char viewForward()
	{
		return map[viewForwardX()][viewForwardY()];
	}
	
	// ==============================================================================
	// =============================== Actions ======================================
	// ==============================================================================
	
	/**
	 * Carry out a particular action
	 * @param chr Action to be carried out
	 */
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
		case ACTION_BLAST:
			toolRemove(TOOL_DYNAMITE);
		break;
		}
	}
	
	/**
	 * Update the current location when moving forward, 
	 *  depending on the current direction facing
	 * @return The move forward action
	 */
	private char actionForward()
	{
		if (getDirection() == DIRECTION_UP) 	locX--;
		if (getDirection() == DIRECTION_DOWN) 	locX++;
		if (getDirection() == DIRECTION_RIGHT) 	locY++;
		if (getDirection() == DIRECTION_LEFT) 	locY--;
		return ACTION_MOVEFORWARD;
	}
	
	/**
	 * Update the current direction when turning
	 * @return The turn left action
	 */
	private char actionLeft()
	{
		rotate(ROTATE_CCW);
		return ACTION_TURNLEFT;
	}
	
	/**
	 * Update the current direction when turning
	 * @return The turn right action
	 */
	private char actionRight()
	{
		rotate(ROTATE_CW);
		return ACTION_TURNRIGHT;
	}
	
	// ==============================================================================
	// =========================== Rotating / Direction =============================
	// ==============================================================================
			
	/**
	 * Rotate the object a particular direction either clockwise
	 *  or counter-clockwise. 
	 * @param direction Direction to rotate (either ROTATE_CCW or
	 *  ROTATE_CW)
	 */
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
	
	/**
	 * Set the current direction the agent is facing
	 * @param direction Direction the agent is facing
	 */
	private void setDirection(int direction)
	{
		this.direction = direction;
	}

	/**
	 * Get the current direction the agent is facing
	 * @return Current direction agent is facing
	 */
	private int getDirection()
	{
		return this.direction;
	}
	
	// ==============================================================================
	// =========================== Checking Cells ===================================
	// ==============================================================================
	
	/**
	 * Check whether a given character is an obstacle
	 * @param chr Character to check if is obstacle
	 * @return Whether given character is an obstacle
	 */
	private boolean isObstacle(char chr)
	{
		if (chr == OBSTACLE_TREE || chr == OBSTACLE_DOOR || chr == OBSTACLE_WALL)
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Check whether a given character is water
	 * @param chr Character to check if is water
	 * @return Whether given character is water
	 */
	private boolean isWater(char chr)
	{
		if (chr == OBSTACLE_WATER)
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Check whether a given character is a blank
	 * @param chr Character to check if is blank
	 * @return Whether given character is a blank
	 */
	private boolean isBlank(char chr)
	{
		if (chr == OBSTACLE_BLANK || chr == OBSTACLE_UNSEEN)
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Check whether a given character is a tool
	 * @param chr Character to check if is tool
	 * @return Whether given character is a tool
	 */
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
	
	/**
	 * Add a tool to the toolset
	 * @param tool Tool to add to the toolset
	 */
	private void toolAdd(char tool)
	{
		toolChange(tool, 1);
	}
	
	/**
	 * Remove a tool to the toolset
	 * @param tool Tool to remove from the toolset
	 */
	private void toolRemove(char tool)
	{
		toolChange(tool, -1);
	}
	
	/**
	 * Check if Agent has a particular tool in the toolset
	 * @param tool Tool to check if exists in toolset
	 * @return If the tool exists in the toolset
	 */
	private boolean toolHave(char tool)
	{
		int numberOfInterest = toolCount(tool);
		if (numberOfInterest > 0)
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Return number of tools Agent has in the toolset (of
	 *  a particular type)
	 * @param tool Tool to check how many exist in the toolset
	 * @return How many tools exist of a particular tool in the toolset
	 */
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
	
	/**
	 * Change the number of a particular tool in the toolset
	 * @param tool Tool to change number of in the toolset
	 * @param change How many to add/subtract of a particular item
	 *  in the toolset
	 */
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
	
	/**
	 * Check if a given integer coordinate is a valid point
	 * @param coord integer coordinate to check if valid
	 * @return If integer coordinate is valid
	 */
	private boolean isPoint(int coord)
	{
		if (coord != NO_POINT)
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Check if Coordinate is a valid coordinate
	 * @param coordinate Coordinate to check if valid coordinate
	 * @return If Coordinate is valid coordinate
	 */
	private boolean isPoint(Coordinate coordinate)
	{
		if (isPoint(coordinate.getX()) && isPoint(coordinate.getY()))
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Given a relative distance from the centre of the 'view',
	 *  return the item that the Agent can see from the input
	 *  given in the 'view' from the Rogue
	 * @param view View input from Rogue
	 * @param forward Relative direction looking forward
	 * @param left Relative direction looking left
	 * @return Item that the agent can see given the relative
	 *  location of the objects in the 'view'
	 */
	private char relativeChar(char view[][], int forward, int left)
	{
		if (Math.abs(forward) <= 2 || Math.abs(left) <= 2)
		{
			if (isBlank(view[MAP_VIEW_HALF_SIZE - forward][MAP_VIEW_HALF_SIZE - left]))
			{
				return '.';
			}
			return view[MAP_VIEW_HALF_SIZE - forward][MAP_VIEW_HALF_SIZE - left];
		}
		return OBSTACLE_WATER;
	}
		
	/**
	 * Check if agent currently has the dynamite item
	 *  directly in front of it
	 * @return Whether the agent currently has the dynamite
	 *  item directly in front of it
	 */
	private boolean inFrontOfDynamite()
	{
		if (viewForwardX() == obstacleToDynamite.getX() && viewForwardY() == obstacleToDynamite.getY())
		{
			return true;
		}
		return false;
	}
	
	// ==============================================================================
	// ================================= Fields =====================================
	// ==============================================================================
	
	// Maps
	private char[][] map;
	private boolean[][] mapTemp1;
	private boolean[][] mapTemp2;
	private boolean[][] mapTemp3;
		
	// Directions and locations
	private int direction;
	private int locY;
	private int locX;
	private int startX;
	private int startY;
	
	// Map Bounds
	private int mapBoundLeft;
	private int mapBoundRight;
	private int mapBoundTop;
	private int mapBoundBottom;
	
	// Tool Counters
	private int numToolsAxe;
	private int numToolsKey;
	private int numToolsDynamite;
	private int numToolsGold;
	
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
	
	// Map Properties
	private static final int MAP_SEARCH_SIZE 	= 170;
	private static final int MAP_VIEW_HALF_SIZE	= 2;

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
	private static final char ACTION_CHOP		= 'C';
	private static final char ACTION_OPEN		= 'O';
	private static final char ACTION_BLAST 		= 'B';
   	
	private static final int NO_POINT			= -1;
	
	// ==============================================================================
	// ============================== Pre-defined ===================================
	// ==============================================================================
   
	/**
	 * Main Function as provided in Assignment 3
	 * @param args Arguments from standard input
	 */
   public static void main( String[] args )
   {
      InputStream in  = null;
      OutputStream out= null;
      Socket socket   = null;
      Agent4  agent    = new Agent4();
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
            action = agent.get_action( view );
            out.write( action );
            
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
