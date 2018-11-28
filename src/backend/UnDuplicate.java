package backend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import frontend.ResultsWindow;

public class UnDuplicate {

	/** Contains all of the array pairs that have been checked for duplicates. **/
	private HashSet<String> checked = new HashSet<String>();
	
	/** Contains all of the found matches. **/
	private ArrayList<Match> allMatches;
	
	/**
	 * Determines the required level of similarity
	 * that is required for two strings to be considered a "match."
	 * The lower the given number, the closer in equality the two 
	 * strings have to be in order to be considered a "match."
	 */
	private int desiredSimilarity;
	
	/** Only strings greater than the given length will be checked for duplicates. **/
	private int minCompareLength;
	
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
		
		this.allMatches = findDuplicates(input, combineDelims(delims));
		
		Collections.sort(allMatches);
		
		new ResultsWindow(allMatches);
	}

	/**
	 * Compares all items in the arrays to one another. This could likely use some optimization.
	 * @param input the input string to be checked for duplicates
	 * @param delims the delimiters used to split the input
	 * @return the results of the duplicate search.
	 */
	public ArrayList<Match> findDuplicates(String input, String delims) {
		HashMap<String, ArrayList<String>> matches = new HashMap<String, ArrayList<String>>();
		
		String[] split = input.split(delims);
		
		for(int j = 0; j < split.length; j++) {

			System.out.println("Completed: " + j + " of " + split.length);

			for(int k = 0; k < split.length; k++) {
				if(j != k) {
					if(!hashed(j, k)){
						if(split[j].length() > minCompareLength && split[k].length() > minCompareLength) {
							if(StringUtils.computeLevenshteinDistance(split[j], split[k]) < desiredSimilarity) {		
								if(!matches.containsKey(split[j])) {
									matches.put(split[j], new ArrayList<String>());
								}

								matches.get(split[j]).add(split[k].trim());
							}
						}
					}
				}
			}
		}

		ArrayList<Match> allMatches = new ArrayList<Match>();
		
		for(String s : matches.keySet()) {
			allMatches.add(new Match(s, false, matches.get(s)));
		}

		return allMatches;
	}
	
	/**
	 * Hashes all checked pairs.
	 * @param j the location in array A
	 * @param k the location in array B
	 * @return true if the value has already been hashed; else, false.
	 */
	public boolean hashed(int j, int k) {
		String hashedValue;

		if(j < k) {
			hashedValue = j + "," + k;
		} else {
			hashedValue = k + "," + j;
		}

		if(checked.contains(hashedValue)) {
			return true;
		} else {
			checked.add(hashedValue);
			return false;
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
