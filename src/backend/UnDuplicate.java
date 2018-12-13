package backend;

import java.util.ArrayList;
import java.util.HashMap;

public class UnDuplicate {

	/** Contains all of the found matches. **/
	public ArrayList<Match> allMatches;

	/** Gets set to "true" if the pair has been checked. **/
	boolean[][] memoize; 

	/**
	 * Determines the required level of similarity
	 * that is required for two strings to be considered a "match."
	 * The lower the given number, the closer in equality the two 
	 * strings have to be in order to be considered a "match."
	 */
	private int desiredSimilarity;

	/** Only strings greater than the given length will be checked for duplicates. **/
	private int minCompareLength;

	/** If true, strings have to be a perfect match. **/
	private boolean perfectFidelity = false;
	
	/**
	 * Locates duplicate text in a given string.
	 * @param input the input string to be checked for duplicates
	 * @param desiredSimilarity number used to compute duplicate text; higher value = less similar.
	 * @param minCompareLength the minimum character length of a comparison.
	 * @param delims the Delimiters that will be used to split the input.
	 */
	public UnDuplicate(String input, int desiredSimilarity, int minCompareLength, Delimiter[] delims) {
		this.desiredSimilarity = desiredSimilarity;
		this.minCompareLength  = minCompareLength;

		if(desiredSimilarity == 0) {
			perfectFidelity = true;
		}
		
		this.allMatches = findDuplicates(input, combineDelims(delims));
	}

	/**
	 * Compares all items in the arrays to one another. This could likely use some optimization.
	 * @param input the input string to be checked for duplicates
	 * @param delims the delimiters used to split the input
	 * @return the results of the duplicate search.
	 */
	public ArrayList<Match> findDuplicates(String input, String delims) {
		//Sentences are split by period; however, e.g. was effing everything up.
		//Don't worry, it's put back in later.
		input = input.replaceAll("e.g.", "e#g#");

		String[] split = input.split(delims);

		memoize = new boolean[split.length][split.length];

		HashMap<String, ArrayList<String>> matches = searchForDuplicates(split);

		ArrayList<Match> allMatches = new ArrayList<Match>();

		int i = 0;
		
		for(String s : matches.keySet()) {
			allMatches.add(new Match(s, false, i++, matches.get(s)));
		}

		return allMatches;
	}

	/**
	 * Searches a given array for duplicate and near-duplicate strings.
	 * @param split the array that will be checked for duplicates.
	 * @return all of found duplicates.
	 */
	public HashMap<String, ArrayList<String>> searchForDuplicates(String[] split) {
		memoize = new boolean[split.length][split.length];

		HashMap<String, ArrayList<String>> matches = new HashMap<String, ArrayList<String>>();

		for(int a = 0; a < split.length; a++) {
			System.out.println("Completed: " + a + " of " + split.length);

			for(int b = 0; b < split.length; b++) {				
				if(!memoize[a][b] && !memoize[b][a]) {
					computeMatch(split[a], split[b], matches, a, b);
					memoize[a][b] = true;
					memoize[b][a] = true;
				}
			}
		}

		return matches;
	}

	/**
	 * Computes whether to strings are a match and remembers all matches. 
	 * @param left the first string to be checked.
	 * @param right the second string to be checked.
	 * @param matches the object that will remember the matches.
	 * @param j the left array int.
	 * @param k the right array int.
	 */
	private void computeMatch(String left, String right, HashMap<String, ArrayList<String>> matches, int j, int k) {
		if(left.length() > minCompareLength && right.length() > minCompareLength && j != k) {
			if(!perfectFidelity && StringUtils.computeLevenshteinDistance(left, right) < desiredSimilarity) {
				if(!matches.containsKey(left)) {
					matches.put(left, new ArrayList<String>());
				}

				matches.get(left).add(right.trim());
			} else {
				if(left.trim().equals(right.trim())) {
					if(!matches.containsKey(left)) {
						matches.put(left, new ArrayList<String>());
					}

					matches.get(left).add(right.trim());
				}
			}
		}
	}

	/**
	 * Combines a given array of Delimiters for splitting a string.
	 * @param delims the given array of Delimiters
	 * @return a string for splitting.
	 */
	private String combineDelims(Delimiter[] delims) {
		String delim = "";

		for(Delimiter d : delims) {
			delim = delim + "|" + d.delim;
		}

		return delim.substring(1, delim.length());
	}
}
