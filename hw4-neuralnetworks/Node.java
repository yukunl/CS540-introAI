import java.util.*;

/*
 * Name: Yukun Li
 * CS540 2019Fall P4
 * Other Source Credits:NA
 * Known Bugs:NA
 * 
 * @author Yukun Li
 * 
 * */

/**
 * Class for internal organization of a Neural Network. There are 5 types of
 * nodes. Check the type attribute of the node for details. Feel free to modify
 * the provided function signatures to fit your own implementation
 */

public class Node {
	private int type = 0; // 0=input,1=biasToHidden,2=hidden,3=biasToOutput,4=Output
	public ArrayList<NodeWeightPair> parents = null; // Array List that will contain the parents (including the bias
														// node) with weights if applicable

	private double inputValue = 0.0;
	private double outputValue = 0.0;
	private double outputGradient = 0.0;
	private double delta = 0.0; // input gradient

	// Create a node with a specific type
	Node(int type) {
		if (type > 4 || type < 0) {
			System.out.println("Incorrect value for node type");
			System.exit(1);

		} else {
			this.type = type;
		}

		if (type == 2 || type == 4) {
			parents = new ArrayList<>();
		}
	}

	// For an input node sets the input value which will be the value of a
	// particular attribute
	public void setInput(double inputValue) {
		if (type == 0) { // If input node
			this.inputValue = inputValue;
		}
	}

	/**
	 * Calculate the output of a node. You can get this value by using getOutput()
	 */
	public void calculateOutput(ArrayList<Node> outputNodes) {
		if (type == 2 || type == 4) { // Not an input or bias node
			if (type == 2) { // if hidden layer, use ReLu
				outputValue = (double) Math.max(0.0, inputValue);
			} else { // if output layer, use Softmax
				double count = 0.0;
				for (int i = 0; i < outputNodes.size(); i++) {
					count = count + Math.exp(outputNodes.get(i).inputValue);
				    }
				outputValue = (double) Math.exp(inputValue) / (double) count;
			}
		}
	}


	// Gets the output value
	public double getOutput() {

		if (type == 0) { // Input node
			return inputValue;
		} else if (type == 1 || type == 3) { // Bias node
			return 1.00;
		} else {
			return outputValue;
		}

	}

	// Calculate the delta value of a node.
	public void calculateDelta(double targetValue, ArrayList<Node> outputNodes, int nodeIndex) {
		if (type == 2 || type == 4) {
			if (type == 2 && inputValue <= 0) {
				this.delta = 0;
			} else if (type == 2 && inputValue > 0) {
				double val = 0;
				for(int i = 0; i < outputNodes.size(); i++) {
					val = val + outputNodes.get(i).parents.get(nodeIndex).weight * outputNodes.get(i).delta;
				}
				this.delta = val;
			} else if (type == 4) {
				this.delta = targetValue - outputValue;
			}
		}
	}

	
	public void calcWeightedInputSum() {

		if (type == 2 || type == 4) {
			double count = 0.0;
			for (int i = 0; i < parents.size(); i++) {
				count = count + parents.get(i).node.getOutput() * parents.get(i).weight;
			}
			inputValue = count;
		}
	}
	
	// Update the weights between parents node and current node
	public void updateWeight(double learningRate) {
		if (type == 2 || type == 4) {
			// TODO: add code here
			for(int i = 0; i < this.parents.size(); i++) {
				parents.get(i).weight = learningRate * parents.get(i).node.getOutput() * delta + parents.get(i).weight;
			}
		}
	}
}
