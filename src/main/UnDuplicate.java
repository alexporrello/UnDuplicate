package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class UnDuplicate {

	HashMap<String, ArrayList<String>> matches = new HashMap<String, ArrayList<String>>();

	HashSet<String> checked = new HashSet<String>();

	public UnDuplicate(String input) {
		findDuplicates(input);
	}
	
	public String findDuplicates(String input) {
		String[] split = input.split("\n|\\.");

		for(int j = 0; j < split.length; j++) {

			System.out.println("Completed: " + j + " of " + split.length);

			for(int k = 0; k < split.length; k++) {
				if(j != k) {
					if(!hashed(j, k)){
						if(split[j].length() > 15 && split[k].length() > 15) {
							if(StringUtils.computeLevenshteinDistance(split[j], split[k]) < 5) {		
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
		String toReturn;
		
		if(j < k) {
			toReturn = j + "," + k;
		} else {
			toReturn = k + "," + j;
		}
		
		if(checked.contains(toReturn)) {
			return true;
		} else {
			checked.add(toReturn);
			return false;
		}
	}
}
