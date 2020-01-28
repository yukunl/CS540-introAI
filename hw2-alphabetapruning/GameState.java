

/*
 * Name: Yukun Li
 * CS540 2019Fall P2 
 * Other Source Credits:NA
 * Known Bugs:NA
 * 
 * @author Yukun Li
 * 
 * */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GameState {
	private int size; // The number of stones
	private boolean[] stones;// Game state: true for available stones, false for taken ones
	private int lastMove; // The last move

	/**
	 * Class constructor specifying the number of stones.
	 */
	public GameState(int size) {

		this.size = size;

		// For convenience, we use 1-based index, and set 0 to be unavailable
		this.stones = new boolean[this.size + 1];
		this.stones[0] = false;

		// Set default state of stones to available
		for (int i = 1; i <= this.size; ++i) {
			this.stones[i] = true;
		}

		// Set the last move be -1
		this.lastMove = -1;
	}

	/**
	 * Copy constructor
	 */
	public GameState(GameState other) {
		this.size = other.size;
		this.stones = Arrays.copyOf(other.stones, other.stones.length);
		this.lastMove = other.lastMove;
	}

	/**
	 * This method is used to compute a list of legal moves
	 *
	 * @return This is the list of state's moves
	 */
	public List<Integer> getMoves() {
		List<Integer> successorValIntegers = new ArrayList<Integer>();
		double num1 = this.size / 2.0;
		if (lastMove == -1) {
			for (int i = 1; i < num1; i++) {
				if (i % 2.0 != 0) {
					// System.out.println(i);
					successorValIntegers.add(i);
				}
			}
		} else {
			for (int i = 1; i <= this.size; i++) {
				if (stones[i] == true && ((i % this.lastMove) == 0 || (this.lastMove % i) == 0)) {
					// System.out.println(i);
					successorValIntegers.add(i);
				}
			}
		}
		return successorValIntegers;
	}

	/**
	 * This method is used to generate a list of successors using the getMoves()
	 * method
	 *
	 * @return This is the list of state's successors
	 */
	public List<GameState> getSuccessors() {
		return this.getMoves().stream().map(move -> {
			GameState state = new GameState(this);
			state.removeStone(move);
			return state;
		}).collect(Collectors.toList());
	}

	/**
	 * This method is used to evaluate a game state based on the given heuristic
	 * function
	 *
	 * @return int This is the static score of given state
	 */
	public double evaluate() {

		int stonetaken = 0;
		for (int j = 1; j < size; j++) {
			if (!stones[j]) {
				stonetaken++;
			}
		}
		// determine who if the max player
		boolean playermax;
		if (stonetaken % 2 == 0) {
			playermax = true;
		} else {
			playermax = false;
		}
		// if end of game
		if (playermax && this.getMoves().size() == 0) {
			return -1.0;
		} else if (!playermax && this.getMoves().size() == 0) {
			return 1.0;
		} else {

			// else
			double returnVal = 0.0;
			// if(playermax) {
			if (stones[1]) {
				returnVal = 0.0;
			} else if (lastMove == 1) {
				int num = this.getMoves().size();
				if (num % 2 != 0) {
					returnVal = 0.5;
				} else {
					returnVal = -0.5;
				}
			} else if (Helper.isPrime(this.lastMove)) {
				int maxMultiple = size / lastMove;
				int maxincrement = 0;
				for (int i = 0; i < this.getMoves().size(); i++) {
					if (this.getMoves().get(i) % this.getLastMove() == 0
							|| this.lastMove % this.getMoves().get(i) == 0) {
						maxincrement++;
					}
				}
				if (maxincrement % 2 != 0) {
					returnVal = 0.7;
				} else {
					returnVal = -0.7;
				}
			} else if (!Helper.isPrime(lastMove)) {

				int largestprimefactor = Helper.getLargestPrimeFactor(this.lastMove);
				int maxMultiple = size / lastMove;
				int maxIncrement = 0;
				for (int i = 0; i < this.getMoves().size(); i++) {
					if (this.getMoves().get(i) % largestprimefactor == 0
							|| largestprimefactor % this.getMoves().get(i) == 0) {
						maxIncrement++;
					}
				}
				if (maxIncrement % 2 != 0) {
					returnVal = 0.6;
				} else {
					returnVal = -0.6;
				}
			}
			// return the value
			if (playermax == true) {
				return returnVal;
			} else {
				return returnVal * -1;
			}
		}

	}

	/**
	 * This method is used to take a stone out
	 *
	 * @param idx Index of the taken stone
	 */
	public void removeStone(int idx) {
		this.stones[idx] = false;
		this.lastMove = idx;
	}

	/**
	 * These are get/set methods for a stone
	 *
	 * @param idx Index of the taken stone
	 */
	public void setStone(int idx) {
		this.stones[idx] = true;
	}

	public boolean getStone(int idx) {
		return this.stones[idx];
	}

	/**
	 * These are get/set methods for lastMove variable
	 *
	 * @param move Index of the taken stone
	 */
	public void setLastMove(int move) {
		this.lastMove = move;
	}

	public int getLastMove() {
		return this.lastMove;
	}

	/**
	 * This is get method for game size
	 *
	 * @return int the number of stones
	 */
	public int getSize() {
		return this.size;
	}

}
