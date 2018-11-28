package frontend;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import backend.FileManager;
import backend.Match;
import jm.JMButton;
import jm.JMFocusablePanel;
import jm.JMPanel;
import jm.JMScrollPane;
import jm.JMTextArea;
import jm.JMTextField;

public class ResultsWindow extends JFrame {
	private static final long serialVersionUID = -6023620346742595500L;

	private JMPanel insideScroll = new JMPanel();

	public ResultsWindow(ArrayList<Match> matches) {
		insideScroll.setLayout(new GridBagLayout());

		int y = 0;

		for(Match m : matches) {
			if(!m.hidden) {
				MatchUI match = new MatchUI(m);

				match.delete.addActionListener(e -> {
					insideScroll.remove(match.matchNum);
					insideScroll.remove(match.matchLabel);
					insideScroll.remove(match.delete);

					if(match.deviations != null) {
						insideScroll.remove(match.deviations);
					}

					insideScroll.revalidate();
					insideScroll.repaint();

					match.match.hidden = true;
					FileManager.ExportFile(matches);
				});

				insideScroll.add(match.matchNum,   new GridBagConstraints(0, y, 1, 1, 0.0, 0.0, 
						GridBagConstraints.NORTHEAST, GridBagConstraints.BOTH, new Insets(4, 2, 0, 1), 0, 0));
				insideScroll.add(match.matchLabel, new GridBagConstraints(1, y, 1, 1, 1.0, 0.0, 
						GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, new Insets(4, 1, 0, 1), 0, 0));
				insideScroll.add(match.delete,     new GridBagConstraints(2, y, 1, 1, 0.0, 0.0, 
						GridBagConstraints.NORTHEAST, GridBagConstraints.BOTH, new Insets(4, 1, 0, 2), 0, 0));

				y++;

				if(match.deviations != null) {
					insideScroll.add(match.deviations, new GridBagConstraints(1, y, 1, 1, 1.0, 0.0, 
							GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, new Insets(4, 1, 0, 2), 0, 0));
				}

				y++;
			}
		}

		add(setUpInput(), BorderLayout.CENTER);

		setSize(new Dimension(700, 500));
		setLocationByPlatform(true);
		setVisible(true);
	}

	private JScrollPane setUpInput() {
		JMScrollPane scroll = new JMScrollPane(insideScroll);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);		
		return scroll;
	}

	public class MatchUI extends JMPanel {
		private static final long serialVersionUID = 1335439437663300422L;

		Match match;

		JMTextArea matchLabel;

		JMTextField matchNum;

		JPanel deviations;

		JMButton delete = new JMButton(" X ");

		public MatchUI(Match match) {
			this.match = match;

			this.matchLabel = new JMTextArea(match.searchedOn.trim());
			this.matchNum   = new JMTextField(match.numMatches + "");

			this.matchLabel.setEditable(false);
			this.matchLabel.setFont(new JLabel().getFont());

			this.matchNum.setEditable(false);
			this.matchNum.setMinimumSize(new Dimension(15, 10));

			setLayout(new BorderLayout());

			buildUI();
		}

		public void buildUI() {
			add(matchLabel, BorderLayout.CENTER);
			add(matchNum,   BorderLayout.WEST);
			add(delete,     BorderLayout.EAST);

			try {
				deviations = makeDeviationsPanel();
				add(deviations, BorderLayout.SOUTH);
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("Deviations panel not added.");				
			}

			matchLabel.addMouseListener(new MouseAdapter() {				
				@Override
				public void mouseClicked(MouseEvent e) {
					if(e.getClickCount() == 1) {
						Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
						clipboard.setContents(new StringSelection(match.searchedOn), null);
					} else {
						if(deviations != null) {
							deviations.setVisible(!deviations.isVisible());
						} 
					}
				}
			});
		}

		public JPanel makeDeviationsPanel() throws Exception {
			if(match.matches.size() >= 1) {
				JMFocusablePanel deviationsPanel = new JMFocusablePanel();
				deviationsPanel.setLayout(new GridBagLayout());

				int y = 0;

				for(String m : match.matches) {
					if(!m.trim().toLowerCase().equals(match.searchedOn.trim().toLowerCase())) {
						deviationsPanel.add(new JLabel(m), new GridBagConstraints(0, y++, 1, 1, 1.0, 0.0, 
								GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, 
								new Insets(2, 0, 0, 2), 0, 0));
					}
				}

				if(y != 0) {
					return deviationsPanel;
				}
			}

			throw new Exception("No panel necessary, m8!");
		}
	}
}
