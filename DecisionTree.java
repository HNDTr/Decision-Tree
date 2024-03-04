//`Group members`: Huy Tran & Eric Tran
//`Group honor hode`: “All group members were present and contributing during all work on this project” 
//`Middlebury honor code`: “I have neither given nor received unauthorized aid on this assignment”
import java.util.ArrayList;

public class DecisionTree {
	private TreeNode root = null; //stores the root of the decision tree
	
	
	public void train(ArrayList<Example> examples){
		int numFeatures = 0;
		if(examples.size()>0) //get the number of features in these examples
			numFeatures = examples.get(0).getNumFeatures();
		
		//initialize empty positive and negative lists
		ArrayList<Example> pos = new ArrayList<Example>();
		ArrayList<Example> neg = new ArrayList<Example>();
		
		//paritition examples into positive and negative ones
		for(Example e: examples){
			if (e.getLabel())
				pos.add(e);
			else
				neg.add(e);
		}
		
		//create the root node of the tree
		root = new TreeNode(null, pos, neg, numFeatures);
		
		//call recursive train()  on the root node
		train(root, numFeatures);
	}

	/**
	 * TODO: Complete this method
	 * The recursive train method that builds a tree at TreeNode node
	 * @param node: current node to train
	 * @param numFeatures: total number of features
	 */
	private void train(TreeNode node, int numFeatures){
//		1. If all remaining examples at this node have the same label L 
//		(Base Case 1):  
			
		if (node.pos.size() == 0 || node.neg.size() == 0) {
			
			// System.out.println("Got here, one of pos or neg is empty");
			// If there are only positive examples left
			if (node.pos.size() > 0 && node.neg.size() == 0) {
				node.decision = true;
				node.isLeaf = true;
			}

			
			// If there are only negative examples left
			if (node.pos.size() == 0 && node.neg.size() > 0) {
				// System.out.println("Got here, the decision is false, shouldn't calculate entropy");
				node.decision = false;
				node.isLeaf = true;
			}
		}

			
		// If there are no examples at this node, set this node's label to the majority label of its parent's examples
		if (node.pos.size() == 0 && node.neg.size() == 0) { 
			node.decision = node.parent.decision;
			node.isLeaf = true;
			// System.out.println("Both are empty");
			return;
		}
		

		// If there are any features remaining
		boolean featureRemaining = getFeatureRemaining(node, numFeatures);
		
		if (!featureRemaining) {
			node.isLeaf = true;
			if (node.pos.size() >= node.neg.size()) {
				node.decision = true;
			}
			else {
				node.decision = false; 
			}
		}
		
		// Otherwise, if there are enough examples and features
		else {
			// System.out.println("The value of featureRemaining is: " + featureRemaining);
			// Create the children and recursively train
			createChildren(node, numFeatures);
			train(node.trueChild, numFeatures);
			train(node.falseChild, numFeatures);
		}

	}
	
	/**
	 * Calculates if there are features that have not been used already
	 * @param node: the node at which to check the features on
	 * @param numFeatures: the total number of features
	 * @return: true if there is a feature remaining
	 */
	private boolean getFeatureRemaining(TreeNode node, int numFeatures) {		
		// Loop through all features
		for (int feature=0; feature < numFeatures; feature++) {
			// If the feature is not used, return true
			if (!node.featureUsed(feature))
				return true;
		}
		return false;
	}
	
	/**
	 * Creates the true and false children of TreeNode node
	 * @param node: node at which to create children
	 * @param numFeatures: total number of features
	 */
	private void createChildren(TreeNode node, int numFeatures){
		
		// System.out.println("In createChildren");
		// System.out.println("The positive nodes are: " + node.pos.size());
		// System.out.println("The negative nodes are: " + node.neg.size());
		
		// Splits the node on the bestFeature
		int bestFeature = getBestFeature(node, numFeatures);
		node.setSplitFeature(bestFeature);
		
		// Decision = True and feature = True
		ArrayList<Example> pos1 = new ArrayList<>();
		// Decision = True and feature = False
		ArrayList<Example> neg1 = new ArrayList<>();

		// Decision = False and feature = True
		ArrayList<Example> pos2 = new ArrayList<>();
		// Decision = False and feature = False
		ArrayList<Example> neg2 = new ArrayList<>();
		

		// Loop through the positive examples 
		for (Example currentExample : node.pos) {
			if (currentExample.getFeatureValue(bestFeature))
				pos1.add(currentExample);
			else pos2.add(currentExample);
		}
		
		// Loop through the negative examples
		for (Example currentExample : node.neg) {
			if (currentExample.getFeatureValue(bestFeature)) {
				neg1.add(currentExample);
				}
			else {
				neg2.add(currentExample);
			}
		}
		
		// Sets the children
		node.trueChild = new TreeNode(node, pos1, neg1, numFeatures);
		node.falseChild = new TreeNode(node, pos2, neg2, numFeatures);
	}
	
	/**
	 * Calculates the best feature to split on by
	 * calculating the information gain.
	 * @param node: node at which to split on
	 * @param numFeatures: total number of features
	 * @return bestFeature: the best feature
	 */
	private int getBestFeature(TreeNode node, int numFeatures) {
		// Variables to keep track of the best feature
		int bestFeature = -1;
		double bestInformationGain = -1.0;
		
		// The entropy of the node
		double entropy = getEntropy(node.pos.size(), node.neg.size());
		//System.out.println("The entropy is: " + entropy);
			// Somehow the entropy is wrong
		
		// Finds the feature with the most information gain
		for (int feature = 0; feature < numFeatures; feature++) {
			// Checks if feature has been used in the tree
			if (!node.featureUsed(feature)) {
				// If not, calculates the information gain
				double informationGain = entropy - getRemainingEntropy(feature, node);
				// If better than the current, change them to be the max
				if (informationGain >= bestInformationGain) {
					bestInformationGain = informationGain;
					bestFeature = feature;
				}
			}
		}
		return bestFeature;
	}
	
	/**
	 * Computes and returns the remaining entropy if feature is chosen
	 * at node.
	 * @param feature: the feature number
	 * @param node: node at which to find remaining entropy
	 * @return remaining entropy at node
	 */
	private double getRemainingEntropy(int feature, TreeNode node) {
        
        // Default value is 1.0, since it is the worst value
        double remainingEntropy = 1.0;
        
        // The total number of examples
        double totalExamples = node.pos.size() + node.neg.size();
        
        // Decision = True and feature = True
        int pos1 = 0;
        // Decision = True and feature = False
        int pos2 = 0;

        // Decision = False and feature = True
        int neg1 = 0;
        // Decision = False and feature = False
        int neg2 = 0;
        
        
        // Loop through the positive examples 
        for (Example currentExample : node.pos) {
            if (currentExample.getFeatureValue(feature))
                pos1++;
            else pos2++;
        }
        
        // Loop through the negative examples
        for (Example currentExample : node.neg) {
            if (currentExample.getFeatureValue(feature))
                neg1++;
            else neg2++;
        }
        
        // The weight/probability of the feature being True
        double weight1 = (pos1 + neg1) / totalExamples;
        // The weight/probability of the feature being False
        double weight2 = (pos2 + neg2) / totalExamples;
        
        // System.out.println("pos1 + neg1 is: " + (pos1+neg1));
        // System.out.println("pos2 + neg2 is: " + (pos2+neg2));
        // System.out.println("totalExamples is: " + totalExamples);
        
        
        // System.out.println("weight1 is: " + weight1);
        // System.out.println("weight2 is: " + weight2);
        
        // Calculate the remaining entropy by multiplying the entropy with the weight/probability
        remainingEntropy = weight1 * getEntropy(pos1, neg1) + weight2 * getEntropy(pos2, neg2);
        
        return remainingEntropy;
    }

	
	/**
	 * Computes the entropy of a node given the number of positive and negative examples it has
	 * @param numPos: number of positive examples
	 * @param numNeg: number of negative examples
	 * @return - entropy
	 */
	private double getEntropy(int numPos, int numNeg) {
        // Calculate the entropy of a node given the number of positive and negative
        // examples
        double total = numPos + numNeg;
        if (total == 0) {
            return 0.0;
        }

        double pPos = (double) numPos / total;
        double pNeg = (double) numNeg / total;

        // Handle cases where pPos or pNeg is 0 to avoid NaN in the log calculation
        if (pPos == 0.0 || pNeg == 0.0) {
            return 0.0;
        }

        return (-pPos * log2(pPos)) + (- pNeg * log2(pNeg));
    }

	
	/**	
	 * Computes log_2(d) (To be used by the getEntropy() method)
	 * @param d - value
	 * @return log_2(d)
	 */
	private double log2(double d) {
		return Math.log(d)/Math.log(2);
	}
	
	/**
     * TODO: complete this method
     * Classifies example e using the learned decision tree
     * 
     * @param e: example
     * @return true if e is predicted to be positive, false otherwise
     */
    public boolean classify(Example e) {
        // Classify example e using the learned decision tree

        // Start from the root and traverse the tree
        TreeNode current = root;
        while (!current.isLeaf) {
            int splitFeature = current.getSplitFeature();
            if (e.getFeatureValue(splitFeature)) {
                current = current.trueChild;
            } else {
                current = current.falseChild;
            }
        }

        return current.decision;
    }
	
	
	//----------DO NOT MODIFY CODE BELOW------------------
	public void print(){
		printTree(root, 0);
	}
	

	
	private void printTree(TreeNode node, int indent){
		if(node== null)
			return;
		if(node.isLeaf){
			if(node.decision)
				System.out.println("Positive");
			else
				System.out.println("Negative");
		}
		else{
			System.out.println();
			doIndents(indent);	
			System.out.print("Feature "+node.getSplitFeature() + " = True:" );
			printTree(node.trueChild, indent+1);
			doIndents(indent);
			System.out.print("Feature "+node.getSplitFeature() + " = False:" );//+  "( " + node.falseChild.pos.size() + ", " + node.falseChild.neg.size() + ")");
			printTree(node.falseChild, indent+1);
		}
	}
	
	private void doIndents(int indent){
		for(int i=0; i<indent; i++)
			System.out.print("\t");
	}
}



