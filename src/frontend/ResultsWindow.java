package frontend;

import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import backend.FileManager;
import backend.Match;
import jm.JMColor;
import jm.JMPanel;

public class ResultsWindow extends JMPanel {
	private static final long serialVersionUID = -6023620346742595500L;

	private String url = System.getProperty("user.home") + "/Desktop/duplicates.xml";
	
	public ResultsWindow(ArrayList<Match> matches) {
		makeResultsWindow(matches);
		setOpaque(false);
		setBackground(JMColor.DEFAULT_BACKGROUND);
	}
	
	public ResultsWindow(ArrayList<Match> matches, String url) {
		this.url = url;
		
		makeResultsWindow(matches);
	}
	
	private void removeMatchFromPanel(ResultRow rw) {
		rw.removeFromJMPanel(this);
	}
	
	private void makeResultsWindow(ArrayList<Match> matches) {
		setLayout(new GridBagLayout());

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
	}
}
