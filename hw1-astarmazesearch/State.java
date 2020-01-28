/*
 * Name: Yukun Li
 * CS540 2019Fall P1 
 * Other Source Credits:NA
 * Known Bugs:NA
 * 
 * @author Yukun Li
 * 
 * */
import java.util.ArrayList;

/**
 * A state in the search represented by the (x,y) coordinates of the square and
 * the parent. In other words a (square,parent) pair where square is a Square,
 * parent is a State.
 * 
 * You should fill the getSuccessors(...) method of this class.
 * 
 */
public class State {

	private Square square;
	private State parent;

	// Maintain the gValue (the distance from start)
	// You may not need it for the BFS but you will
	// definitely need it for AStar
	private int gValue;

	// States are nodes in the search tree, therefore each has a depth.
	private int depth;

	/**
	 * @param square
	 *            current square
	 * @param parent
	 *            parent state
	 * @param gValue
	 *            total distance from start
	 */
	public State(Square square, State parent, int gValue, int depth) {
		this.square = square;
		this.parent = parent;
		this.gValue = gValue;
		this.depth = depth;
	}

	/**
	 * @param visited
	 *            explored[i][j] is true if (i,j) is already explored
	 * @param maze
	 *            initial maze to get find the neighbors
	 * @return all the successors of the current state
	 */
	public ArrayList<State> getSuccessors(boolean[][] explored, Maze maze) {
		// FILL THIS METHOD
		ArrayList<State> successor = new ArrayList<State>();
		// TODO check all four neighbors in left, down, right, up order
		// add left 
		
		if(square.Y - 1 >= 0 && isPath(maze,explored, square.X, square.Y -1 )) {
			
				successor.add( new State(new Square(square.X, square.Y -1), this, gValue +1 , depth +1) );
		}	
		// add down 
		if(square.X + 1 < maze.getMazeMatrix().length && isPath(maze,explored, square.X + 1, square.Y )) {
			
			successor.add( new State(new Square(square.X + 1, square.Y), this, gValue +1 , depth +1) );
	}
		
		// add right
		if(square.Y + 1 < maze.getMazeMatrix()[0].length && isPath(maze,explored, square.X, square.Y + 1 )) {
			
			successor.add( new State(new Square(square.X , square.Y + 1), this, gValue +1 , depth +1) );
	}
		// add up
		if(square.X - 1 >= 0 && isPath(maze,explored, square.X - 1, square.Y )) {
			
			successor.add( new State(new Square(square.X - 1, square.Y), this, gValue +1 , depth +1) );
	}
		// TODO remember that each successor's depth and gValue are
		// +1 of this object.
		return successor;
	}

	private boolean isPath(Maze maze, boolean[][] explored, int x, int y) {
		// a path is valid only when it is not a wall symbol and has not been explored 
		if (maze.getSquareValue(x, y) != '%' && explored[x][y]!= true) {
			return true;
		}
		return false;
	}
	/**
	 * @return x coordinate of the current state
	 */
	public int getX() {
		return square.X;
	}

	/**
	 * @return y coordinate of the current state
	 */
	public int getY() {
		return square.Y;
	}

	/**
	 * @param maze initial maze
	 * @return true if the current state is a goal state
	 */
	public boolean isGoal(Maze maze) {
		if (square.X == maze.getGoalSquare().X
				&& square.Y == maze.getGoalSquare().Y)
			return true;

		return false;
	}

	/**
	 * @return the current state's square representation
	 */
	public Square getSquare() {
		return square;
	}

	/**
	 * @return parent of the current state
	 */
	public State getParent() {
		return parent;
	}

	/**
	 * You may not need g() value in the BFS but you will need it in A-star
	 * search.
	 * 
	 * @return g() value of the current state
	 */
	public int getGValue() {
		return gValue;
	}

	/**
	 * @return depth of the state (node)
	 */
	public int getDepth() {
		return depth;
	}
}
