import java.util.ArrayList;
import java.util.List;

/*
 * Name: Yukun Li
 * CS540 2019Fall P5
 * Other Source Credits:NA
 * Known Bugs:NA
 * 
 * @author Yukun Li
 * 
 * */

public class CrossValidation {
    /*
     * Returns the k-fold cross validation score of classifier clf on training data.
     */
    public static double kFoldScore(Classifier clf, List<Instance>  totalData, int k, int v) {
         double accuracy = 0.0; 
    	 // split samples 
         
    	 for (int i = 0 ; i < k ; i ++) {
    		 List<Instance> traindata = new ArrayList<Instance>();
    		 List<Instance> testdata = new ArrayList<Instance>();
    		 
    		 for(int j = 0; j < totalData.size(); j++) {
    			 if( i == j/(totalData.size()/k)) {
    				 testdata.add(totalData.get(j));
    			 }else {
    				 traindata.add(totalData.get(j));
    			 }
    		 }
    		 clf.train(traindata , v);
             ClassifyResult trainResult = new ClassifyResult();
             int correctval = 0; 
             int totalval = 0;
             for(Instance singleInstance : testdata) {
             trainResult = clf.classify(singleInstance.words);
             if(trainResult.label.equals(singleInstance.label)) {
            	 correctval++;
             }
             totalval ++;
             }
             
             accuracy = accuracy + (double)correctval/(double)totalval;
    	 }
        return accuracy/(double)k;
    }
}
