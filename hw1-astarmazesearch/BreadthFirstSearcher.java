
/*
 * Name: Yukun Li
 * CS540 2019Fall P1 
 * Other Source Credits:NA
 * Known Bugs:NA
 * 
 * @author Yukun Li
 * 
 * */
import java.util.LinkedList;

/**
 * Breadth-First Search (BFS)
 * 
 * You should fill the search() method of this class.
 */
public class BreadthFirstSearcher extends Searcher {

	/**
	 * Calls the parent class constructor.
	 * 
	 * @see Searcher
	 * @param maze initial maze.
	 */
	public BreadthFirstSearcher(Maze maze) {
		super(maze);
	}

	/**
	 * Main breadth first search algorithm.
	 * 
	 * @return true if the search finds a solution, false otherwise.
	 */
	public boolean search() {

		// explored list is a 2D Boolean array that indicates if a state associated with
		// a
		// given position in the maze has already been explored.
		boolean[][] explored = new boolean[maze.getNoOfRows()][maze.getNoOfCols()];

		// Queue implementing the Frontier list
		LinkedList<State> queue = new LinkedList<State>();
		// search for the start state and add in to queue

		// helper for pointer
		State currentState = new State(maze.getPlayerSquare(), null, 0, 0);
		queue.add(currentState);
		// System.out.print("hellooooo");
		// explored[currentState.getX()][currentState.getY()] = true;
		// while there are still unverified nodes
		while (!queue.isEmpty()) {
			// System.out.print("hellooooo2");
			if (this.getMaxSizeOfFrontier() < queue.size()) {
				this.maxSizeOfFrontier = queue.size(); // max size of frontier
			}
			currentState = queue.pop();

			this.noOfNodesExpanded++; // number of nodes added to Frontier
			explored[currentState.getX()][currentState.getY()] = true;
			this.cost = currentState.getGValue();

			if (currentState.isGoal(maze)) {
				// max depth
				this.maxDepthSearched = currentState.getDepth();
				// trace back parents to set path '.'
				// currentState = currentState.getParent();
				while (currentState.getParent() != null) {
					currentState = currentState.getParent();
					maze.setOneSquare(currentState.getSquare(), '.');
					// this.cost = this.cost + 1; // length of the solution path

				}
				maze.setOneSquare(currentState.getSquare(), 'S');
				return true;
			}
			for (int i = 0; i < currentState.getSuccessors(explored, maze).size(); i++) {
				State currentStateiState = currentState.getSuccessors(explored, maze).get(i);
				boolean add = true;
				for (State k : queue) {
					if (k.getX() == currentStateiState.getX() && k.getY() == currentStateiState.getY()) {
						add = false;
					}
				}
				if (add == true) {
					queue.add(currentStateiState);
				}
			}
		}

		// return false;
		return false;
	}
}
