package backend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import frontend.ResultsDialog;

public class UnDuplicate {

	private HashMap<String, ArrayList<String>> matches = new HashMap<String, ArrayList<String>>();

	private HashSet<String> checked = new HashSet<String>();
	
	private ArrayList<Match> allMatches;
	
	private int desiredSimilarity;
	
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
		
		allMatches = findDuplicates(input, combineDelims(delims));
		
		Collections.sort(allMatches);
		
		String toDisplay = "";
		
		for(Match m : allMatches) {
			toDisplay = toDisplay + m + "\n";
			
			System.out.println(m);
		}
		
		new ResultsDialog(toDisplay);
	}

	public ArrayList<Match> findDuplicates(String input, String delims) {
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
			allMatches.add(new Match(s, matches.get(s)));
		}

		return allMatches;
	}
	
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
	 * Combines a given array of Delimeters for splitting a string.
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
	
	/**
	 * A match contains the original string and all of its matches in an ArrayList.
	 * @author Alexander Porrello
	 */
	public class Match implements Comparable<Match> {
		
		public ArrayList<String> matches;
		
		public String searchedOn;
		
		public Match(String searchedOn, ArrayList<String> matches) {
			this.matches = matches;
			this.searchedOn = searchedOn;
		}
		
		public int getNumMatches() {
			return matches.size();
		}
		
		@Override
		public String toString() {
			String toReturn = searchedOn.trim();
			
			String otherOccurences = "";
			
			for(String s : matches) {
				if(!s.trim().equals(searchedOn.trim())) {
					otherOccurences = otherOccurences + "\n\t\t" + s;
				}
			}
			
			return toReturn + otherOccurences;
		}

		@Override
		public int compareTo(Match o) {
			return (Integer.valueOf(o.matches.size())).compareTo(Integer.valueOf(matches.size()));
		}
	}
}
