

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
import java.util.List;

public class AlphaBetaPruning {
	int bestmove;
	double value;
	int nodeVisitied;
	int nodeEvaluated;
	int maxDepth;
	int avfBf;
	int searchDepth;
	List<Double> possibleMoves= new ArrayList<Double>();
	//GameState state = new GameState();
	public AlphaBetaPruning() {

		bestmove = 0;
		value = 0;
		nodeVisitied = 0;
		nodeEvaluated = 0;
		maxDepth = 0;
		avfBf = 0;
		searchDepth = 0;
		
		
	}

	/**
	 * This function will print out the information to the terminal, as specified in
	 * the homework description.
	 */
	public void printStats() {
		
		
		System.out.println("Move: " + bestmove);
		System.out.println("Value: " + value);		
		System.out.println("Number of Nodes Visited: " + nodeVisitied);
		System.out.println("Number of Nodes Evaluated: " + nodeEvaluated);
		System.out.println("Max Depth Reached: " + maxDepth);
		double avfBf = ((double)(nodeVisitied - 1)/ (nodeVisitied - nodeEvaluated));
		System.out.println("Avg Effective Branching Factor: " + avfBf);
	}

//    private boolean isMaxPlayer() {
//    	
//    }

	/**
	 * This function will start the alpha-beta search
	 * 
	 * @param state This is the current game state
	 * @param depth This is the specified search depth
	 */
	public void run(GameState state, int depth) {
		
		searchDepth = depth;
		int stonetaken = 0;
		for (int j = 1; j <= state.getSize(); j++) {
			if (!state.getStone(j)) {
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
		this.value = alphabeta(state, depth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, playermax);
		int index = this.possibleMoves.indexOf(this.value);
		this.bestmove = state.getMoves().get(index);
		
		// TODO Add your code here
	}

	/**
	 * This method is used to implement alpha-beta pruning for both 2 players
	 * 
	 * @param state     This is the current game state
	 * @param depth     Current depth of search
	 * @param alpha     Current Alpha value
	 * @param beta      Current Beta value
	 * @param maxPlayer True if player is Max Player; Otherwise, false
	 * @return int This is the number indicating score of the best next move
	 */
	private double alphabeta(GameState state, int depth, double alpha, double beta, boolean maxPlayer) {
		maxDepth = Math.max(maxDepth, searchDepth - depth);
		nodeVisitied++;
		if (depth == 0 || state.getSuccessors().size() == 0) {
			if(depth == searchDepth - 1) {
				possibleMoves.add(state.evaluate());
			}
			nodeEvaluated++;
			return state.evaluate();
			//return this.value;
		}
		double finalValue = 0; 
		if (maxPlayer) {
			
			finalValue = Double.NEGATIVE_INFINITY;
			for (GameState current : state.getSuccessors()) {
				double val = alphabeta(current, depth - 1, alpha, beta, false);
				finalValue = Math.max(finalValue, val);
				if(finalValue >= beta) {
					break ;
				}
				alpha = Math.max(alpha, finalValue);
			}
			

		} else { // if min state

			finalValue = Double.POSITIVE_INFINITY;
			for (GameState current : state.getSuccessors()) {
				double val = alphabeta(current, depth - 1, alpha, beta, true);
				finalValue = Math.min(finalValue, val);
				if(finalValue <= alpha) {
					break ;
				}
				beta = Math.min(beta, finalValue);		
			}
		}
		if(depth == searchDepth - 1) {
			possibleMoves.add(finalValue);
		}
		return finalValue;
		

	}
}
