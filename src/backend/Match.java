package backend;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * A match contains the original string and all of its matches in an ArrayList.
 * @author Alexander Porrello
 */
public class Match implements Comparable<Match> {
	
	public HashSet<String> matches = new HashSet<String>();
	
	public String searchedOn;
	
	public int numMatches;
	
	public boolean hidden;
	
	public MatchSort sortBy = MatchSort.NONE;
	
	public Match(String searchedOn, Boolean hidden, ArrayList<String> matches) {
		this.numMatches = matches.size();
		this.searchedOn = searchedOn;
		this.hidden     = hidden;
		
		for(String s : matches) {
			this.matches.add(s.trim());
		}
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
		if(sortBy == MatchSort.NUM_MATCHES) {
			return (Integer.valueOf(o.numMatches)).compareTo(Integer.valueOf(numMatches));
		} else if(sortBy == MatchSort.TEXT_LENGTH) {
			return (Integer.valueOf(o.searchedOn.length())).compareTo(Integer.valueOf(searchedOn.length()));
		} else {
			return 0;
		}
	}
}
