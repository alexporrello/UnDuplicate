package frontend;

import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;

import backend.FileManager;
import backend.Match;
import backend.MatchSort;
import jm.JMColor;
import jm.JMPanel;

public class ResultsWindow extends JMPanel {
	private static final long serialVersionUID = -6023620346742595500L;

	private String url = System.getProperty("user.home") + "/Desktop/duplicates.xml";
	
	private ArrayList<Match> matches;
	
	private MatchSort sortMethod = MatchSort.NONE;
	
	public ResultsWindow(ArrayList<Match> matches) {
		this.matches = matches;
		
		setLayout(new GridBagLayout());
		setOpaque(false);
		setBackground(JMColor.DEFAULT_BACKGROUND);
		
		makeResultsWindow();
	}
	
	public ResultsWindow(ArrayList<Match> matches, String url) {
		this.url = url;
		this.matches = matches;
		
		setLayout(new GridBagLayout());
		setOpaque(false);
		setBackground(JMColor.DEFAULT_BACKGROUND);
		
		makeResultsWindow();
	}
	
	private void removeMatchFromPanel(ResultRow rw) {
		rw.removeFromJMPanel(this);
	}
	
	public void changeSortMethod(MatchSort sortMethod) {
		this.sortMethod = sortMethod;
		
		for(Match m : matches) {
			m.sortBy = this.sortMethod;
		}
		
		if(sortMethod != MatchSort.NONE) {
			Collections.sort(matches);
		}
		
		makeResultsWindow();
	}
	
	private void makeResultsWindow() {
		this.removeAll();
		
		int y = 0;
		
		for(Match m : matches) {
			if(!m.hidden) {
				ResultRow match = new ResultRow(m);

				match.hide.addMouseListener(new MouseAdapter() {					
					@Override
					public void mouseClicked(MouseEvent e) {
						removeMatchFromPanel(match);
						
						if(match.deviations != null) {
							remove(match.deviations);
						}

						revalidate();
						repaint();

						m.hidden = true;
						FileManager.ExportFile(matches, url);
					}
				});

				y = match.addToJMPanel(this, y);
			}
		}
		
		revalidate();
		repaint();
	}
}
