

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * Name: Yukun Li
 * CS540 2019Fall P5
 * Other Source Credits:NA
 * Known Bugs:NA
 * 
 * @author Yukun Li
 * 
 * */

/**
 * Your implementation of a naive bayes classifier. Please implement all four
 * methods.
 */

public class NaiveBayesClassifier implements Classifier {

	private int v = 0;
	private int docneg = 0;
	private int docpos = 0;
	private Map<String, Integer> negwrdperlabel = new HashMap<String, Integer>();
	private Map<String, Integer> poswrdperlabel = new HashMap<String, Integer>();
    private Map<Label, Integer> wordcountlabel = new HashMap<Label, Integer>();
	/**
	 * Trains the classifier with the provided training data and vocabulary size
	 */
	@Override
	public void train(List<Instance> trainData, int v) {
		this.v = v;
		Map<String, Integer> neg = new HashMap<String, Integer>();
		Map<String, Integer> pos = new HashMap<String, Integer>();
		for (int i = 0; i < trainData.size(); i++) {
			if (trainData.get(i).label.equals(Label.NEGATIVE)) {
				for (int j = 0; j < trainData.get(i).words.size(); j++) {	
					if (neg.get(trainData.get(i).words.get(j)) != null) {				
						int occurance = neg.get(trainData.get(i).words.get(j)) ;
						neg.replace(trainData.get(i).words.get(j), occurance + 1);
					} else {
						neg.put(trainData.get(i).words.get(j), 1);
					}
				}
			} else {
				for (int j = 0; j < trainData.get(i).words.size(); j++) {

					if (pos.get(trainData.get(i).words.get(j)) != null) {
						int occurance = pos.get(trainData.get(i).words.get(j)) ;
						pos.replace(trainData.get(i).words.get(j), occurance + 1);
					} else {
						pos.put(trainData.get(i).words.get(j), 1);
					}
				}

			}
		}
		
		negwrdperlabel = neg;
		poswrdperlabel = pos;
		getDocumentsCountPerLabel(trainData);
		wordcountlabel = getWordsCountPerLabel(trainData);

	}

	/*
	 * Counts the number of words for each label
	 */
	@Override
	public Map<Label, Integer> getWordsCountPerLabel(List<Instance> trainData) {
		int negword = 0;
		int posword = 0;
		Map<Label, Integer> doccountperlabelMap = new HashMap<Label, Integer>();
		for (Instance data : trainData) {
			if (data.label.equals(Label.NEGATIVE)) {
				negword = negword + data.words.size();
			} else {
				posword = posword + data.words.size();
			}
		}
		doccountperlabelMap.put(Label.NEGATIVE, negword);
		doccountperlabelMap.put(Label.POSITIVE, posword);
		return doccountperlabelMap;

	}

	/*
	 * Counts the total number of documents for each label
	 */
	@Override
	public Map<Label, Integer> getDocumentsCountPerLabel(List<Instance> trainData) {
		int countneg = 0;
		int countpos = 0;
		Map<Label, Integer> doccountperlabelMap = new HashMap<Label, Integer>();
		for (Instance data : trainData) {
			if (data.label.equals(Label.NEGATIVE)) {
				countneg++;
			} else {
				countpos++;
			}
		}
		doccountperlabelMap.put(Label.NEGATIVE, countneg);
		doccountperlabelMap.put(Label.POSITIVE, countpos);
		this.docneg = countneg;
		this.docpos = countpos;
		return doccountperlabelMap;
	}

	/**
	 * Returns the prior probability of the label parameter, i.e. P(POSITIVE) or
	 * P(NEGATIVE)
	 */
	private double p_l(Label label) {
		double total = docneg + docpos;
		if (label.equals(Label.NEGATIVE)) {
			return (double) docneg / (double) total;
		} else {
			return (double) docpos / (double) total;
		}
		// TODO : Implement
		// Calculate the probability for the label. No smoothing here.
		// Just the number of label counts divided by the number of documents.
	}

	/**
	 * Returns the smoothed conditional probability of the word given the label,
	 * i.e. P(word|POSITIVE) or P(word|NEGATIVE)
	 */
	private double p_w_given_l(String word, Label label) {
        
		double C1w = 0.0;
		double totalC1V = 0.0;
		// caculate positive or neg total number 
		if(label.equals(Label.NEGATIVE)) {
			totalC1V = wordcountlabel.get(Label.NEGATIVE);
		}
		else {
			totalC1V = wordcountlabel.get(Label.POSITIVE);
		}
		// check label given word 
		if (label.equals(Label.NEGATIVE)) {
			if(negwrdperlabel.get(word) == null ) {
				return (1.0 /((double)v + (double)totalC1V)) ;
			}
			C1w = negwrdperlabel.get(word);
			return ((double) C1w + 1.0)/ ((double) this.v + (double)totalC1V);
		//	totalC1V = wordcountlabel.get(Label.NEGATIVE);
		} else  {
			if(poswrdperlabel.get(word) == null ) {
				return 1.0 /((double)v + (double)totalC1V) ;
			}
			C1w = poswrdperlabel.get(word);
			return ((double) C1w + 1.0)/ ((double) this.v + (double)totalC1V);
		}
		
	}

	/**
	 * Classifies an array of words as either POSITIVE or NEGATIVE.
	 */
	@Override
	public ClassifyResult classify(List<String> words) {
		double logofpos = Math.log(p_l(Label.POSITIVE));
		double  logofneg = Math.log(p_l(Label.NEGATIVE));
		for( String singlewordString : words) {
			logofpos = logofpos + Math.log(p_w_given_l(singlewordString, Label.POSITIVE));
			logofneg = logofneg + Math.log(p_w_given_l(singlewordString, Label.NEGATIVE));
			
		}
		ClassifyResult newresult = new ClassifyResult();
		newresult.logProbPerLabel = new HashMap<Label, Double>();
		newresult.logProbPerLabel.put(Label.NEGATIVE, logofneg);
		newresult.logProbPerLabel.put(Label.POSITIVE, logofpos);
		if(logofneg > logofpos) {
			newresult.label = Label.NEGATIVE;
		}else {
			newresult.label = Label.POSITIVE;
		}
		// TODO : Implement
		// Sum up the log probabilities for each word in the input data, and the
		// probability of the label
		// Set the label to the class with larger log probability
		return newresult;
	}

}
