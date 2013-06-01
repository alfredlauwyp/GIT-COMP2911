import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class Agent2 {

	public Agent2()
	{
		this.iterations = -1;
		this.direction = 0;
		
		this.state = STATE_UNFOGGING;
		
		this.map = new char[MAP_SEARCH_SIZE][MAP_SEARCH_SIZE];
		this.mapTemp = new boolean[MAP_SEARCH_SIZE][MAP_SEARCH_SIZE];
		this.mapTemp2 = new boolean[MAP_SEARCH_SIZE][MAP_SEARCH_SIZE];
		
		this.locY = this.startX = (MAP_SEARCH_SIZE / 2);
		this.locX = this.startY = (MAP_SEARCH_SIZE / 2);
		
		this.obstacleToDynamite = new Coordinate(NO_POINT, NO_POINT);
		this.obstacleToDynamiteLongTerm = this.obstacleToDynamite;
		
		this.findingPath = true;
		
		this.lastGetToCoord = new Coordinate(NO_POINT, NO_POINT);
	}
		
	private void wait(int time) { try { Thread.sleep(time);} catch(InterruptedException e) { speakln("Interrupted"); } }
	
	public char get_action(char view[][])
	{
		this.iterations++;
		speakln("=======Steps("+iterations+")==Loc("+locX+","+locY+")=====================================================================");
		char move = 0;
		addMapFeatures(view);
		
		speakln("State: BLOW THINGS UP we can see ("+viewForwardX()+","+viewForwardY()+") and dynamite blowup is ("+obstacleToDynamite+")");
		
		if (toolHave(TOOL_GOLD))
		{
			move = getTo(startX, startY);			
		}
		else if (canGetTo(TOOL_GOLD))
		{
			move = getTo(TOOL_GOLD);
		}		
		else if (toolHave(TOOL_AXE) && viewForward() == OBSTACLE_TREE)
		{
			move = ACTION_CHOP;
		}
		else if (toolHave(TOOL_KEY) && viewForward() == OBSTACLE_DOOR)
		{
			move = ACTION_OPEN;
		}
		else if (toolHave(TOOL_DYNAMITE) && (viewForwardX() == obstacleToDynamite.getX() && viewForwardY() == obstacleToDynamite.getY()))
		{
			move = ACTION_BLAST;
		}
		else if (!toolHave(TOOL_AXE) && canGetTo(TOOL_AXE))
		{
			move = getTo(TOOL_AXE);
		}
		else if (!toolHave(TOOL_KEY) && canGetTo(TOOL_KEY))
		{
			move = getTo(TOOL_KEY);
		}
		else if (!toolHave(TOOL_DYNAMITE) && canGetTo(TOOL_DYNAMITE))
		{
			move = getTo(TOOL_DYNAMITE);
		}
		else if (toolHave(TOOL_KEY) && canGetTo(OBSTACLE_DOOR))
		{
			move = getTo(OBSTACLE_DOOR);
		}
		else if (toolHave(TOOL_AXE) && canGetTo(OBSTACLE_TREE))
		{
			move = getTo(OBSTACLE_TREE);
		}
		else if (isPoint(findNearestUnseen()))
		{
			move = getTo(findNearestUnseen());
		}
		else if (toolHave(TOOL_DYNAMITE) && canGetToBehindObstacle())
		{
			move = getTo(obstacleToDynamite.getX(), obstacleToDynamite.getY());
		}
		
		move = checkForTools(relativeChar(view, 1, 0), move); //Check if the item in front of you is a tool
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
					//System.out.println("Trying point ("+i+","+j+")");
					int diffX = Math.abs(p.getX() - i);
					int diffY = Math.abs(p.getY() - i);
					if (diffX + diffY < distanceAway && canGetTo(i, j))
					{
						p = new Coordinate(i, j);
					}
				}
			}
		}
		
		//speakln("NEAREST: " + p);
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
		System.out.println("lastGetToCoord: " + lastGetToCoord + ", input: ("+x+","+y+")");
		if (findingPath)
		{
			speakln("Finding Path");
			findPath(x, y);			
			printPathHistory(findPathResult);
			wait(50);
			findingPath = false;
		}
		
			speakln("Found Path");
			printPathHistory(findPathResult);
			if (findPathResult.size() > 0)
			{
				int xCoord = findPathResult.get(0).getX();
				int yCoord = findPathResult.get(0).getY();
				speakln("=== Findign correct move ("+locX+","+locY+") ("+xCoord+","+yCoord+")");
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
		speakln("Move: " + move);
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
			//System.out.println("========================================= from ("+locX+","+locY+") looking for POINT FOUND ("+x+","+y+")");
			resetTempMap();
			return canGetToR(locX, locY, x, y);
		}
		//System.out.println("========================================= from ("+locX+","+locY+") looking for NOT A POINT FOUND");
		return false; // If no points are passed,
		
	}
	
	private boolean canGetToR(int x, int y, int finalX, int finalY)
	{
		addTempMap(x,y);
		boolean result = false;
		
		if (x == finalX && y == finalY) return true;		
		
		if (!inTempMap(x+1,y) && result == false && !isWater(map[x+1][y]) && (!isObstacle(map[x + 1][y]) || (x+1==finalX && y==finalY)) && !isBlank(map[x + 1][y]))
		{
			//System.out.print("DOWN ");
			result = canGetToR(x+1,y, finalX, finalY); // down
		}
		if (!inTempMap(x-1,y) && result == false && !isWater(map[x-1][y]) &&(!isObstacle(map[x - 1][y]) || (x-1==finalX && y==finalY)) && !isBlank(map[x - 1][y]))
		{
			//System.out.print("UP ");
			result = canGetToR(x-1,y, finalX, finalY); // up
		}		
		if (!inTempMap(x,y+1) && result == false && !isWater(map[x][y+1]) &&(!isObstacle(map[x][y+1]) || (x==finalX && y+1==finalY)) && !isBlank(map[x][y + 1]))
		{
			//System.out.print("RIGHT ");
			result = canGetToR(x,y+1, finalX, finalY); // right
		}
		if (!inTempMap(x,y-1) && result == false && !isWater(map[x][y-1]) &&(!isObstacle(map[x][y - 1]) || (x==finalX && y-1==finalY)) && !isBlank(map[x][y - 1]))
		{
			//System.out.print("LEFT ");
			result = canGetToR(x,y - 1, finalX, finalY); // left 
		}		
		return result;
	}
	
	
	
	
	
	private Coordinate obstacleToDynamite;
	private Coordinate obstacleToDynamiteLongTerm;
	
	private boolean canGetToBehindObstacle()
	{
		speakln("========================================= HOW CAN WE USE DYNAMITE?");
		boolean solution = false;
		
		char items[] = {TOOL_GOLD, TOOL_DYNAMITE, TOOL_AXE, TOOL_KEY};
		
		int count = 0;
		while (!solution && count < items.length)
		{
			resetTempMap();
			
			// Note this
			int obsX = obstacleToDynamiteLongTerm.getX();
			int obsY = obstacleToDynamiteLongTerm.getY();
			
			speakln("Checking if point! " + obstacleToDynamiteLongTerm);
			if (isPoint(obstacleToDynamiteLongTerm))
			{
				speakln("---Is Point! " +obstacleToDynamiteLongTerm + " is " + map[obsX][obsY] + " equal to " + toolToObstacle(items[count]));
				//if (map[obsX][obsY] == toolToObstacle(items[count]) || items[count] == TOOL_DYNAMITE)
				//{
					speakln("------Stay!! " +obstacleToDynamiteLongTerm);
					obstacleToDynamite = obstacleToDynamiteLongTerm;
					return true;
				//}
			}			
			
			obstacleToDynamite = new Coordinate(NO_POINT, NO_POINT);
			solution = canGetToBehindObstacleR(locX, locY, 1, items[count]);
			speakln("Solution: " + solution + " for "+items[count]+" towards " + obstacleToDynamite);
			count++;
		}
		speakln("Solution: " + items[count-1] + " towards " + obstacleToDynamite);
		obstacleToDynamiteLongTerm = obstacleToDynamite;
		return solution;
	}
	
	private boolean canGetToBehindObstacleR(int x, int y, int getOutOfJailFree, char itemOfInterest)
	{
		boolean result = false;
		if (getOutOfJailFree == 1)
		{
			addTempMap(x,y);
			resetTempMap2();
		}
		else
		{
			addTempMap2(x, y);
		}
		
		//printMap();
		//printTempMap();
		//printTempMap2();
		
		if (map[x][y] == itemOfInterest) return true;		
		
		if (!result) { result = canGetToBehindObstacleRInterim(x+1, y, getOutOfJailFree, itemOfInterest); } // down
		if (!result) { result = canGetToBehindObstacleRInterim(x-1, y, getOutOfJailFree, itemOfInterest); } // up
		if (!result) { result = canGetToBehindObstacleRInterim(x, y+1, getOutOfJailFree, itemOfInterest); } // right
		if (!result) { result = canGetToBehindObstacleRInterim(x, y-1, getOutOfJailFree, itemOfInterest); } // left		
		
		return result;
	}
	
	private boolean canGetToBehindObstacleRInterim(int x, int y, int getOutOfJailFree, char itemOfInterest)
	{
		//speak("("+x+","+y+") gOOJF: " + getOutOfJailFree + " .. isObstacle? " + map[x][y] + " .... CanGetThere: "+(!inTempMap(x,y) && !inTempMap2(x,y) && !isWater(map[x][y]) && (!isObstacle(map[x][y]) || getOutOfJailFree > 0) && !isBlank(map[x][y]))+"\n");
		if (!inTempMap(x,y) && !isWater(map[x][y]) && (!isObstacle(map[x][y]) || getOutOfJailFree > 0) && !isBlank(map[x][y]))
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private void printPathHistory(ArrayList<Coordinate> pathHistory)
	{
		for (int i = 0; i < pathHistory.size(); i++)
		{
			speak("(" + (int)pathHistory.get(i).getX() + "," + (int)pathHistory.get(i).getY() + "), ");
		}
	}
	
	private void findPath(int x, int y)
	{
		ArrayList<Coordinate> pathHistory = new ArrayList<Coordinate>();
		resetTempMap();
		speakln("=== FIND PATH to ("+x+","+y+")===");
		findPathR(locX, locY, x, y, pathHistory);
	}
	
	private boolean findPathR(int x, int y, int xGoal, int yGoal, ArrayList<Coordinate> pathHistory)
	{
		ArrayList<Coordinate> pathHistoryClone = new ArrayList<Coordinate>(pathHistory);
		speak("FINDPATHR: At point ("+x+","+y+") ("+map[x][y]+"), looking for goal ("+xGoal+","+yGoal+")\n");
		addTempMap(x,y);
		boolean result = false;
		if (x == xGoal && y == yGoal)
		{
			result = true;
			speakln("=====Found Goal!\n=====\n======\n======\n======");
			findPathResult = pathHistoryClone;
			
			// Holy fuck please lord forgive me for the below
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
							//speak("\nPREVIOUS: ");printPathHistory(findPathResult);
							//speak("\nCUTTING: " + findPathResult.get(i));
							findPathResult.remove(i);
							//speak("\nAFTER: ");printPathHistory(findPathResult);
							finishedRemoving = false;
							break;
						}
					}
				}
			}		
			
		}
		if (!result)
		{
			//speakln("No result yet");
			if (xGoal > x && Math.abs(xGoal - x) >= Math.abs(yGoal - y))
			{
				//speakln("Up");
				if (!result) result = findPathRinterim(x + 1, y, xGoal, yGoal, pathHistoryClone);
				if (!result) result = findPathRinterim(x, y + 1, xGoal, yGoal, pathHistoryClone);
				if (!result) result = findPathRinterim(x, y - 1, xGoal, yGoal, pathHistoryClone);
				if (!result) result = findPathRinterim(x - 1, y, xGoal, yGoal, pathHistoryClone);
			}
			else if (yGoal > y && Math.abs(xGoal - x) <= Math.abs(yGoal - y))
			{
				//speakln("Down");
				if (!result) result = findPathRinterim(x, y + 1, xGoal, yGoal, pathHistoryClone);
				if (!result) result = findPathRinterim(x - 1, y, xGoal, yGoal, pathHistoryClone);
				if (!result) result = findPathRinterim(x + 1, y, xGoal, yGoal, pathHistoryClone);
				if (!result) result = findPathRinterim(x, y - 1, xGoal, yGoal, pathHistoryClone);
			}
			else if (xGoal < x && Math.abs(xGoal - x) >= Math.abs(yGoal - y))
			{
				//speakln("Left");
				if (!result) result = findPathRinterim(x - 1, y, xGoal, yGoal, pathHistoryClone);
				if (!result) result = findPathRinterim(x, y - 1, xGoal, yGoal, pathHistoryClone);
				if (!result) result = findPathRinterim(x, y + 1, xGoal, yGoal, pathHistoryClone);
				if (!result) result = findPathRinterim(x + 1, y, xGoal, yGoal, pathHistoryClone);
			}
			else if (yGoal < y && Math.abs(xGoal - x) <= Math.abs(yGoal - y))
			{
				//speakln("Right");
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
						isBlank(map[i+1][j-1]) ||
						isBlank(map[i+1][j]) ||
						isBlank(map[i+1][j+1]) ||
						isBlank(map[i+1][j+2]) ||

						isBlank(map[i][j-2]) ||
						isBlank(map[i][j-1]) ||
						isBlank(map[i][j+1]) ||
						isBlank(map[i][j+2]) ||
						
						isBlank(map[i-1][j-2]) ||
						isBlank(map[i-1][j-1]) ||
						isBlank(map[i-1][j]) ||
						isBlank(map[i-1][j+1]) ||
						isBlank(map[i-1][j+2]) ||
						
						isBlank(map[i-2][j-2]) ||
						isBlank(map[i-2][j-1]) ||
						isBlank(map[i-2][j]) ||
						isBlank(map[i-2][j+1]) ||
						isBlank(map[i-2][j+2])
				)
				{
					//System.out.println("---1: Trying point ("+i+","+j+")");
					if (map[i][j] == OBSTACLE_EXPLORED)
					{
						//System.out.println("------2: Trying point ("+i+","+j+")");
						int diffX = Math.abs(locX - i);
						int diffY = Math.abs(locY - j);
						if (diffX + diffY < distanceAway && canGetTo(i, j))
						{
							//System.out.println("-----------3: Trying point ("+i+","+j+") - " + (diffX + diffY < distanceAway) + " | " + canGetTo(i, j));
							distanceAway = diffX + diffY;
							p = new Coordinate(i, j);
						}
					}
				}
			}
		}
		
		speakln("NEAREST: " + p);
		return p;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	// ==============================================================================
	// =========================== STATE 1: Unfogging ===============================
	// ==============================================================================
		
	private char getUnfoggingMove(char view[][])
	{
		char move = 0;
		if (isPoint(findNearestUnseen()))
		{
			Coordinate p = findNearestUnseen();
			System.out.println("Nearest Unseen: " + p);
			move = getTo(p.getX(), p.getY());
			System.out.println("Move to unfog: " + move);
		}
		else
		{
			changeState(STATE_TOOLUSING);
		}
		return move;
	}

	// ==============================================================================
	// =========================== STATE 2: Tool Using =============================
	// ==============================================================================
		
	private char getToolUsingMove(char view[][])
	{
		char move = 0;
		if (!toolHave(TOOL_AXE) && canGetTo(TOOL_AXE))
		{
			speakln("=== 1");
			move = getTo(TOOL_AXE);
		}
		else if (!toolHave(TOOL_KEY) && canGetTo(TOOL_KEY))
		{
			speakln("=== 3");
			move = getTo(TOOL_KEY);
		}
		else if (!toolHave(TOOL_DYNAMITE) && canGetTo(TOOL_DYNAMITE))
		{
			speakln("=== 5");
			move = getTo(TOOL_DYNAMITE);
		}
		else if (toolHave(TOOL_KEY) && canGetTo(OBSTACLE_DOOR))
		{
			speakln("=== 4");
			move = getTo(OBSTACLE_DOOR);
		}
		else if (toolHave(TOOL_AXE) && canGetTo(OBSTACLE_TREE))
		{
			speakln("=== 2");
			move = getTo(OBSTACLE_TREE);
		}
		else if (toolHave(TOOL_DYNAMITE) && canGetToBehindObstacle())
		{
			speakln("=== 6");
			move = getTo(obstacleToDynamite.getX(), obstacleToDynamite.getY());
		}
		else
		{
			speakln("=== CHANGING STATE BACK");
			changeState(STATE_UNFOGGING);
		}
		return move;
	}
	
	// ==============================================================================
	// =============================== Temporar. Map=================================
	// ==============================================================================

	private boolean inTempMap(int x, int y)
	{
		return mapTemp[x][y];
	}
	
	private void addTempMap(int x, int y)
	{
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
	
	private boolean inTempMap2(int x, int y)
	{
		return mapTemp2[x][y];
	}
	
	private void addTempMap2(int x, int y)
	{
		mapTemp2[x][y] = true;
	}
	
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
	
	// ==============================================================================
	// ======================= Adding Map / Item Features ===========================
	// ==============================================================================
	
	private void addMapFeatures(char view[][])
	{
		System.out.println("Adding to map, we are at ("+locX+","+locY+")");
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
	
	private char checkForTools(char chr, char prevMove)
	{
		char move = prevMove;
		if (isTool(chr))
		{
			toolAdd(chr);
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
	
	private int viewForwardX()
	{
		if (getDirection() == DIRECTION_UP) 	return locX - 1;
		if (getDirection() == DIRECTION_DOWN) 	return locX + 1;
		if (getDirection() == DIRECTION_LEFT) 	return locX;
		if (getDirection() == DIRECTION_RIGHT) 	return locX;
		return 0;
	}
	private int viewForwardY()
	{
		if (getDirection() == DIRECTION_UP) 	return locY;
		if (getDirection() == DIRECTION_DOWN) 	return locY;
		if (getDirection() == DIRECTION_LEFT) 	return locY - 1;
		if (getDirection() == DIRECTION_RIGHT) 	return locY + 1;
		return 0;
	}
	
	private char viewForward()
	{
		if (getDirection() == DIRECTION_UP) 	return map[locX - 1][locY];
		if (getDirection() == DIRECTION_DOWN) 	return map[locX + 1][locY];
		if (getDirection() == DIRECTION_LEFT) 	return map[locX][locY - 1];
		if (getDirection() == DIRECTION_RIGHT) 	return map[locX][locY + 1];
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
			System.out.println("---||||---- Moving Forward!");
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
	private char actionForward()
	{
		if (getDirection() == DIRECTION_UP) 	locX--;
		if (getDirection() == DIRECTION_DOWN) 	locX++;
		if (getDirection() == DIRECTION_RIGHT) 	locY++;
		if (getDirection() == DIRECTION_LEFT) 	locY--;
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
		if (chr == OBSTACLE_TREE || chr == OBSTACLE_DOOR || chr == OBSTACLE_WALL)
		{
			return true;
		}
		return false;
	}
	
	private boolean isWater(char chr)
	{
		if (chr == OBSTACLE_WATER)
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
	
	private char toolToObstacle(char chr)
	{
		switch(chr)
		{
			case TOOL_AXE: return OBSTACLE_TREE;
			case TOOL_KEY: return OBSTACLE_DOOR;
		}		
		return 0;
	}
	
	// ==============================================================================
	// ================================= Other ======================================
	// ==============================================================================
	
	private void changeState(int state)
	{
		this.state = state;
	}
	
	private boolean isPoint(int coord)
	{
		if (coord != NO_POINT)
		{
			return true;
		}
		return false;
	}
	
	private boolean isPoint(Coordinate coordinate)
	{
		if (isPoint(coordinate.getX()) && isPoint(coordinate.getY()))
		{
			return true;
		}
		return false;
	}
	
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
	
	// ==============================================================================
	// ================================= Fields =====================================
	// ==============================================================================
	
	private char[][] map;
	private boolean[][] mapTemp;
	private boolean[][] mapTemp2;
		
	private int iterations;
	private int direction;
	private int state;
	private int locY;
	private int locX;
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
	// States
	private static final int STATE_UNFOGGING 	= 0;
	private static final int STATE_TOOLUSING 	= 1;

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
	// ============================= Map Printing ===================================
	// ==============================================================================
	
	private void printTempMap()
	{
		for (int j = mapBoundTop; j <= mapBoundBottom; j++)
		{
			speak(j + " ");
		}
		speak("   \n");
		for (int i = mapBoundLeft; i <= mapBoundRight; i++)
		{
			speak(i + " ");
			for (int j = mapBoundTop; j <= mapBoundBottom; j++)
			{
				if (mapTemp[i][j])
				{
					speak(".  ");
				}
				else
				{
					speak("   ");
				}
			}
			speak(" " + i);
			speakln("");
		}
		speak("   ");
		for (int j = mapBoundTop; j <= mapBoundBottom; j++)
		{
			speak(j + " ");
		}
		speak("   \n");		
	}
	
	private void printTempMap2()
	{
		for (int j = mapBoundTop; j <= mapBoundBottom; j++)
		{
			speak(j + " ");
		}
		speak("   \n");
		for (int i = mapBoundLeft; i <= mapBoundRight; i++)
		{
			speak(i + " ");
			for (int j = mapBoundTop; j <= mapBoundBottom; j++)
			{
				if (mapTemp2[i][j])
				{
					speak(".  ");
				}
				else
				{
					speak("   ");
				}
			}
			speak(" " + i);
			speakln("");
		}
		speak("   ");
		for (int j = mapBoundTop; j <= mapBoundBottom; j++)
		{
			speak(j + " ");
		}
		speak("   \n");		
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
					if (getDirection() == DIRECTION_UP) speak("^  ");
					if (getDirection() == DIRECTION_LEFT) speak("<  ");
					if (getDirection() == DIRECTION_RIGHT) speak(">  ");
					if (getDirection() == DIRECTION_DOWN) speak("v  ");
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
	
	
	
	
	
	
	
	
	
	
	
   
	// ==============================================================================
	// ================================= Other ======================================
	// ==============================================================================
   
   void print_view( char view[][] )
   {
      int i,j;

      speakln("\n+-----+");
      for( i=0; i < 5; i++ ) {
         speak("|");
         for( j=0; j < 5; j++ ) {
            if(( i == 2 )&&( j == 2 )) {
               speak('^');
            }
            else {
               speak( view[i][j] );
            }
         }
         speakln("|");
      }
      speakln("+-----+");
   }

   public static void main( String[] args )
   {
      InputStream in  = null;
      OutputStream out= null;
      Socket socket   = null;
      Agent2  agent    = new Agent2();
      char   view[][] = new char[10][10];
      char   action   = 'F';
      int port;
      int ch;
      int i,j;
      if( args.length < 2 ) {
         speakln("Usage: java Agent -p <port>\n");
         System.exit(-1);
      }

      port = Integer.parseInt( args[1] );

      try { // open socket to Game Engine
         socket = new Socket( "localhost", port );
         in  = socket.getInputStream();
         out = socket.getOutputStream();
      }
      catch( IOException e ) {
         speakln("Could not bind to port: "+port);
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
            
            //agent.printMap();
            action = agent.get_action( view );
            out.write( action );
            //agent.printTempMap();
            //agent.printWeightMap();
            agent.printMap();
            
         }
      }
      catch( IOException e ) {
         speakln("Lost connection to port: "+ port );
         System.exit(-1);
      }
      finally {
         try {
            socket.close();
         }
         catch( IOException e ) {}
      }
   }

   private static void speak(String s)
   {
	   System.out.print(s);
   }
   private static void speak(char c)
   {
	  speak(new String("" + c));
   }
   private static void speakln(String s)
   {
	  speak(s + "\n");
   }
}
