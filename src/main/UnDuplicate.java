package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class UnDuplicate {

	private HashMap<String, ArrayList<String>> matches = new HashMap<String, ArrayList<String>>();

	private HashSet<String> checked = new HashSet<String>();
	
	/**
	 * Locates duplicate text in a given string.
	 * @param input the input string to be checked for duplicates
	 * @param desiredSimilarity number used to compute duplicate text; higher value = less similar.
	 * @param delims
	 */
	public UnDuplicate(String input, int desiredSimilarity, Delimiter[] delims) {
		findDuplicates(input, desiredSimilarity, combineDelims(delims));
	}

	public String findDuplicates(String input, int desiredSimilarity, String delims) {
		String[] split = input.split(delims);

		for(int j = 0; j < split.length; j++) {

			System.out.println("Completed: " + j + " of " + split.length);

			for(int k = 0; k < split.length; k++) {
				if(j != k) {
					if(!hashed(j, k)){
						if(split[j].length() > 10 && split[k].length() > 10) {
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

		String output = "";

		for(String s : matches.keySet()) {
			String thisLine = "\"" + s + "\"" + "," + (matches.get(s).size() + 1);

			output = thisLine + "\n" + output;
		}

		System.out.println(output);

		return output;
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
}
