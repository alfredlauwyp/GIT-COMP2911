import java.io.*;
import java.net.*;

public class Agent {

	public Agent()
	{
		this.iterations = -1;
		this.direction = 0;
		
		this.state = STATE_UNFOGGING;
		
		this.map = new char[MAP_SEARCH_SIZE][MAP_SEARCH_SIZE];
		this.mapTemp = new boolean[MAP_SEARCH_SIZE][MAP_SEARCH_SIZE];
		
		this.locX = (MAP_SEARCH_SIZE / 2);
		this.locY = (MAP_SEARCH_SIZE / 2);
		this.startX = this.locX;
		this.startY = this.locY;
		
		this.randomCounter = 0;
	}
	
	public char get_action(char view[][])
	{
		this.iterations++;
		try { Thread.sleep(400);} catch(InterruptedException e) { System.out.println("Interrupted"); }
		
		char move = 0;
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
			System.out.println("FFS");
			if (isObstacle(relativeChar(view, 1, 0)))
			{
				randomCounter = 1;
				return actionLeft();
			}
			else if (randomCounter == 2)
			{
				randomCounter = 0;
				return actionRight();
			}
			else
			{
				if (randomCounter == 1) randomCounter++;
				return actionForward();
			}
		}		
		return 0;
	}
	
	private boolean allSpacesExplored()
	{
		resetTempMap();
		boolean explored = spaceExplored(this.startX, this.startY);
		printTempMap();
		System.out.println("Space Explored? " + explored);
		return explored;
	}
	
	private boolean spaceExplored(int x, int y)
	{
		System.out.println("Coords: ("+x+","+y+")");
		addTempMap(x,y);
		boolean result = true;
		
		// Search Downward
		if (!inTempMap(x+1,y) && result == true)
		{
			System.out.println("DOWN level 1");
			if (!isObstacle(map[x + 1][y]))
			{
				System.out.println("DOWN level 2");
				if (isBlank(map[x+1][y]))
				{
					System.out.println("DOWN level 3a");
					result = false;
				}
				else
				{
					System.out.println("DOWN level 3b");
					result = spaceExplored(x+1,y);
				}
			}
		}
		if (!inTempMap(x-1,y) && result == true)
		{
			System.out.println("UP level 1");
			if (!isObstacle(map[x - 1][y]))
			{
				System.out.println("UP level 2");

				if (isBlank(map[x-1][y]))
				{
					System.out.println("UP level 3a");
					result = false;
				}
				else
				{
					System.out.println("UP level 3b");
					result = spaceExplored(x-1,y);
				}
			}
		}
		
		if (!inTempMap(x,y+1) && result == true)
		{
			System.out.println("RIGHT level 1");
			if (!isObstacle(map[x][y + 1]))
			{
				System.out.println("RIGHT level 2");
				if (isBlank(map[x][y + 1]))
				{
					System.out.println("RIGHT level 3a");
					result = false;
				}
				else
				{
					System.out.println("RIGHT level 3b");
					result = spaceExplored(x,y+1);
				}
			}
		}
		if (!inTempMap(x,y-1) && result == true)
		{
			System.out.println("LEFT level 1");
			if (!isObstacle(map[x][y - 1]))
			{
				System.out.println("LEFT level 2");

				if (isBlank(map[x][y - 1]))
				{
					System.out.println("LEFT level 3a");
					result = false;
				}
				else
				{
					System.out.println("LEFT level 3b");
					result = spaceExplored(x,y - 1);
				}
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
	// =========================== Series of Actions ================================
	// ==============================================================================
	
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
	
	private int randomCounter;
	
	// ==============================================================================
	// ============================= Definitions ====================================
	// ==============================================================================
	
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
	private static final int MAP_VIEW_SIZE 		= 5;
	private static final int MAP_VIEW_CENTRE 	= 2;
	
	// States
	private static final int STATE_UNFOGGING 	= 0;
	private static final int STATE_MAKINGPATH 	= 1;
	private static final int STATE_SEARCHINGGOAL= 2;

	// Obstacles and Tools
	private static final char OBSTACLE_UNSEEN	= 0;
	private static final char OBSTACLE_BLANK 	= ' ';
	private static final char OBSTACLE_EXPLORED	= '.';
	private static final char OBSTACLE_TREE 	= 'T';
	private static final char OBSTACLE_DOOR 	= '-';
	private static final char OBSTACLE_WALL		= '*';
	private static final char OBSTACLE_WATER 	= '~';
	private static final char TOOL_AXE 			= 'a';
	private static final char TOOL_KEY 			= 'k';
	private static final char TOOL_DYNAMITE		= 'd';
	private static final char TOOL_GOLD 		= 'g';
	
	// Actions
	private static final char ACTION_TURNRIGHT	= 'R';
	private static final char ACTION_TURNLEFT	= 'L';
	private static final char ACTION_MOVEFORWARD= 'F';
   
	// ==============================================================================
	// ============================= Map Printing ===================================
	// ==============================================================================
	
	private void printTempMap()
	{
		int left = MAP_SEARCH_SIZE;
		int right = 0;
		int top = MAP_SEARCH_SIZE;
		int bottom = 0;
		
		for (int i = 0; i < MAP_SEARCH_SIZE; i++)
		{
			for (int j = 0; j < MAP_SEARCH_SIZE; j++)
			{
				if (mapTemp[i][j])
				{
					if (left > i)	left = i;
					if (right < i)	right = i;
					if (top > j)	top = j;
					if (bottom < j) bottom = j;
				}
			}
		}
		System.out.println(left + ", " + right + ", " + top + ", " + bottom);
		for (int j = top; j <= bottom; j++)
		{
			System.out.print(j + " ");
		}
		System.out.print("   \n");
		for (int i = left; i <= right; i++)
		{
			System.out.print(i + " ");
			for (int j = top; j <= bottom; j++)
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
		for (int j = top; j <= bottom; j++)
		{
			System.out.print(j + " ");
		}
		System.out.print("   \n");
		
		
		
	}
	private void printMap()
	{
		System.out.println("Steps("+iterations+") Direction(" + getDirection() + ") at location (" + locX + "," + locY + ")");		
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
