
/*
 * Name: Yukun Li
 * CS540 2019Fall P1 
 * Other Source Credits:NA
 * Known Bugs:NA
 * 
 * @author Yukun Li
 * 
 * */
import java.util.PriorityQueue;

/**
 * A* algorithm search
 * 
 * You should fill the search() method of this class.
 */
public class AStarSearcher extends Searcher {

	/**
	 * Calls the parent class constructor.
	 * 
	 * @see Searcher
	 * @param maze initial maze.
	 */
	public AStarSearcher(Maze maze) {
		super(maze);
	}

	private double fVal(State state, Square goal) {
		return Math.sqrt(Math.pow((state.getX() - goal.X), 2) + Math.pow((state.getY() - goal.Y), 2))
				+ state.getGValue();
	}

	/**
	 * Main a-star search algorithm.
	 * 
	 * @return true if the search finds a solution, false otherwise.
	 */
	public boolean search() {
		boolean[][] explored = new boolean[maze.getNoOfRows()][maze.getNoOfCols()];
		PriorityQueue<StateFValuePair> frontier = new PriorityQueue<StateFValuePair>();
		State currentState = new State(maze.getPlayerSquare(), null, 0, 0);
		Square goal = maze.getGoalSquare();
		frontier.add(new StateFValuePair(currentState, fVal(currentState, goal)));

		while (!frontier.isEmpty()) {
			// StateFValuePair currentFPair = frontier.poll();
			currentState = frontier.poll().getState();
			noOfNodesExpanded++;
			explored[currentState.getX()][currentState.getY()] = true;
			// TODO return true if a solution has been found
			if (currentState.isGoal(maze)) {
				// max depth
				this.maxDepthSearched = currentState.getDepth();
				// trace back parents to set path '.'
				while (currentState.getParent() != null) {
					currentState = currentState.getParent();
					maze.setOneSquare(currentState.getSquare(), '.');
					this.cost = this.cost + 1; // length of the solution path

				}
				maze.setOneSquare(currentState.getSquare(), 'S');
				return true;
			} else {

				// loop for current's successor
				for (int i = 0; i < currentState.getSuccessors(explored, maze).size(); i++) {
					boolean canadd = true;
					State sucessorState = currentState.getSuccessors(explored, maze).get(i);
					StateFValuePair sucessorFval = new StateFValuePair(sucessorState, fVal(sucessorState, goal));
					// loop for elements in priority queue
					for (StateFValuePair index : frontier) {
						// if found same state in priority queue
						if (index.getState().getX() == sucessorState.getX()
								&& index.getState().getY() == sucessorState.getY()) {
							canadd = false;
							if (index.getState().getGValue() > sucessorState.getGValue()) {
								frontier.add(sucessorFval);
								frontier.remove(index);
								canadd = false;
							}
						}
					}
					// after looping priority queue, if there are no same states in the priority
					// queue
					if (canadd) {
						frontier.add(sucessorFval);
					}

				}

			}
			if (frontier.size() > this.maxSizeOfFrontier) {
				this.maxSizeOfFrontier = frontier.size();
			}
		}
		return false;

	}
}
