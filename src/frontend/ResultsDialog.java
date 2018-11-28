package frontend;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import backend.Match;
import jm.JMButton;
import jm.JMPanel;
import jm.JMScrollPane;
import jm.JMTextArea;

public class ResultsDialog extends JFrame {
	private static final long serialVersionUID = -6023620346742595500L;

	private JMPanel insideScroll = new JMPanel();

	public ResultsDialog(ArrayList<Match> matches) {
		insideScroll.setLayout(new GridBagLayout());

		int y = 0;

		for(Match m : matches) {
			MatchUI match = new MatchUI(m);

			match.delete.addActionListener(e -> {
				insideScroll.remove(match);
				insideScroll.revalidate();
				insideScroll.repaint();
			});

			insideScroll.add(match, new GridBagConstraints(0, y++, 1, 1, 1.0, 0.0, 
					GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, 
					new Insets(-1, 5, 0, 5), 0, 0));
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
		JLabel matchNum;

		JPanel deviations;

		JMButton delete = new JMButton("×");

		public MatchUI(Match match) {
			this.match = match;

			this.matchLabel = new JMTextArea(match.searchedOn.trim());
			this.matchNum   = new JLabel(match.numMatches + "");

			this.matchLabel.setLineWrap(true);
			this.matchLabel.setWrapStyleWord(true);
			this.matchLabel.setOpaque(false);
			this.matchLabel.setEditable(false);
			this.matchLabel.setFont(new JLabel().getFont());

			setBorder(matchLabel);
			setBorder(matchNum);

			setLayout(new BorderLayout());

			buildUI();
		}

		private void setBorder(JComponent jc) {
			jc.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK, 1),
					BorderFactory.createEmptyBorder(3, 3, 3, 3)));
		}

		public void buildUI() {
			add(matchLabel, BorderLayout.CENTER);
			add(matchNum,   BorderLayout.WEST);
			add(delete,     BorderLayout.EAST);

			try {
				deviations = makeDeviationsPanel();
				add(deviations, BorderLayout.SOUTH);
			} catch (Exception e) {
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
			if(match.matches.size() > 1) {
				JPanel deviationsPanel = new JPanel() {
					private static final long serialVersionUID = 7154275559452566982L;

					@Override
					public void paintComponent(Graphics g) {
						super.paintComponent(g);
						Graphics2D g2d = (Graphics2D) g;
						g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
						int w = getWidth();
						int h = getHeight();
						Color color1 = Color.decode("#f1f3f4");
						Color color2 = Color.decode("#dee1e6");
						GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
						g2d.setPaint(gp);
						g2d.fillRect(0, 0, w, h);
					}
				};
				deviationsPanel.setLayout(new GridBagLayout());

				int y = 0;

				for(String m : match.matches) {					
					deviationsPanel.add(new JLabel(m), new GridBagConstraints(0, y++, 1, 1, 1.0, 0.0, 
							GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, 
							new Insets(2, 15, 0, 2), 0, 0));
				}

				return deviationsPanel;
			}

			throw new Exception("No panel necessary, m8!");
		}
	}
}
