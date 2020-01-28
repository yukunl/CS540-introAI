
/*
 * Name: Yukun Li
 * CS540 2019Fall P3
 * Other Source Credits:NA
 * Known Bugs:NA
 * 
 * @author Yukun Li
 * 
 * */

import java.util.ArrayList;
import java.util.List;

/**
 * Fill in the implementation details of the class DecisionTree using this file.
 * Any methods or secondary classes that you want are fine but we will only
 * interact with those methods in the DecisionTree framework.
 */
public class DecisionTreeImpl {
	public DecTreeNode root;
	public List<List<Integer>> trainData;
	public int maxPerLeaf;
	public int maxDepth;
	public int numAttr;

	// Build a decision tree given a training set
	DecisionTreeImpl(List<List<Integer>> trainDataSet, int mPerLeaf, int mDepth) {
		this.trainData = trainDataSet;
		this.maxPerLeaf = mPerLeaf;
		this.maxDepth = mDepth;
		if (this.trainData.size() > 0)
			this.numAttr = trainDataSet.get(0).size() - 1;
		this.root = buildTree();
	}

	private double conditionalEntropy(List<List<Integer>> datalist, int attribute, int threshold) {
		double num0small = 0;
		double num0big = 0;
		double num1small = 0;
		double num1big = 0;
		double totalnum = 1.0 * datalist.size();
		double conditionalEntropy = 0;
		for (int i = 0; i < datalist.size(); i++) {
			List<Integer> singleData = datalist.get(i);

			if (singleData.get(attribute) <= threshold) {
				if (singleData.get(numAttr) == 0) {
					num0small++;
				} else {
					num1small++;
				}

			}

			if (singleData.get(attribute) > threshold) {
				if (singleData.get(numAttr) == 0) {
					num0big++;
				} else {
					num1big++;
				}

			}

		}

		double lessprob = (num0small + num1small) / totalnum;
		double bigprob = (num0big + num1big) / totalnum;
		double lessprob0 = num0small / (num0small + num1small);
		double bigprob0 = num0big / (num1big + num0big);
		double lessprob1 = num1small / (num0small + num1small);
		double bigprob1 = num1big / (num1big + num0big);
		double left = lessprob * (lessprob0 * log2(lessprob0) + lessprob1 * log2(lessprob1));
		double right = bigprob * (bigprob0 * log2(bigprob0) + (bigprob1) * log2(bigprob1));

		conditionalEntropy = left + right;
		return -1.0 * conditionalEntropy;
	}

	private double getEntropy(List<List<Integer>> datalist) {
		double num1 = 0;
		double num0 = 0;
		for (int i = 0; i < datalist.size(); i++) {
			List<Integer> singleData = datalist.get(i);
			if (singleData.get(numAttr) == 1) {
				num1++;
			}
			if (singleData.get(numAttr) == 0) {
				num0++;
			}
		}
		double num0entropy = -(num1 / datalist.size()) * log2(num1 / datalist.size());
		double num1entropy = -(num0 / datalist.size()) * log2(num0 / datalist.size());
		return num0entropy + num1entropy;

	}

	private double informationGain(List<List<Integer>> datalist, int attribute, int threshold) {
//System.out.println("Information gain " + (this.getEntropy(datalist) - conditionalEntropy(datalist, attribute, threshold) ));
		return this.getEntropy(datalist) - conditionalEntropy(datalist, attribute, threshold);
	}

	private double log2(double m) {
		if (m == 0.0) {
			return 0;
		} else {
			return (Math.log(m) / Math.log(2));
		}
	}

	private boolean sameLabel(List<List<Integer>> n) {
		boolean same = true;
		for (int i = 0; i < n.size() - 1; i++) {
			if (n.get(i).get(n.get(i).size() - 1) != n.get(i + 1).get(n.get(i).size() - 1)) {
				same = false;
			}
		}
		return same;
	}

	private int majority(List<List<Integer>> n) {
		int num0 = 0;
		int num1 = 0;
		for (int i = 0; i < n.size(); i++) {
			if (n.get(i).get(numAttr) == 0) {
				num0++;
			} else {
				num1++;
			}
		}
		if (num0 > num1) {
			return 0;
		}
		return 1;
	}

	private DecTreeNode buildTreeHelper(List<List<Integer>> dataset, int depth) {
		if (dataset.size() == 0) {
			return new DecTreeNode(1, 0, 0);
		}

		int attribute = 0;
		int threshold = 0;
		List<List<Integer>> smallDataList = new ArrayList<List<Integer>>();
		List<List<Integer>> bigDataList = new ArrayList<List<Integer>>();
		double informationGain = 0.0;
		// find the best attribute's best threshold
		for (int i = 0; i < numAttr; i++) { // loop for attribute
			for (int j = 1; j < 10; j++) { // loop for threshold
				double maxinfogain = this.informationGain(dataset, i, j);

				if (maxinfogain > informationGain) {
					informationGain = maxinfogain;
					// System.out.println("Information gain" + informationGain);
					attribute = i;
					threshold = j;
				}
			}
		}
		DecTreeNode node = new DecTreeNode(this.majority(dataset), attribute, threshold);

		if (this.informationGain(dataset, attribute, threshold) == 0 || this.maxDepth == depth
				|| maxPerLeaf > dataset.size()) {
			return node;
		}

		// System.out.println("asdasdas");
		// seperate out the list
		for (int i = 0; i < dataset.size(); i++) {
			if (dataset.get(i).get(attribute) <= threshold) {
				smallDataList.add(dataset.get(i));

			} else {
				bigDataList.add(dataset.get(i));

			}
		}
			//System.out.println("this is left : " + smallDataList.size());
			//System.out.println("this is right : " + bigDataList.size());
		if (!sameLabel(dataset)) {
			node.left = buildTreeHelper(smallDataList, depth + 1);
			node.right = buildTreeHelper(bigDataList, depth + 1);
		}

		return node;

	}

	private DecTreeNode buildTree() {
		DecTreeNode node = buildTreeHelper(this.trainData, 0);
		return node;
	}

	public int classify(List<Integer> instance) {
		int classify = this.classifyhelper(root, instance).classLabel;

		return classify;
	}

	public DecTreeNode classifyhelper(DecTreeNode node, List<Integer> instance) {
		if (node.isLeaf()) {
			return node;
		}
		if (instance.get(node.attribute) <= node.threshold) {
			node = node.left;
			return classifyhelper(node, instance);
		} else {
			node = node.right;
			return classifyhelper(node, instance);
		}
       //return null;
	}

	// Print the decision tree in the specified format
	public void printTree() {
		printTreeNode("", this.root);
	}

	public void printTreeNode(String prefixStr, DecTreeNode node) {
		String printStr = prefixStr + "X_" + node.attribute;
		System.out.print(printStr + " <= " + String.format("%d", node.threshold));
		if(node.left.isLeaf()) {
			System.out.println(" : " + String.valueOf(node.left.classLabel));
		}
		else {
			System.out.println();
			printTreeNode(prefixStr + "|\t", node.left);
		}
		System.out.print(printStr + " > " + String.format("%d", node.threshold));
		if(node.right.isLeaf()) {
			System.out.println(" : " + String.valueOf(node.right.classLabel));
		}
		else {
			System.out.println();
			printTreeNode(prefixStr + "|\t", node.right);
		}
	}
	
	public double printTest(List<List<Integer>> testDataSet) {
		int numEqual = 0;
		int numTotal = 0;
		for (int i = 0; i < testDataSet.size(); i ++)
		{
			int prediction = classify(testDataSet.get(i));
			int groundTruth = testDataSet.get(i).get(testDataSet.get(i).size() - 1);
			System.out.println(prediction);
			if (groundTruth == prediction) {
				numEqual++;
			}
			numTotal++;
		}
		double accuracy = numEqual*100.0 / (double)numTotal;
		System.out.println(String.format("%.2f", accuracy) + "%");
		return accuracy;
	}
}
