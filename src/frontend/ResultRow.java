package frontend;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import backend.Match;
import jm.JMColor;
import jm.JMPanel;

public class ResultRow {

	private Color borderColor = JMColor.DEFAULT_BORDER_COLOR;

	private Timer fadeTimer;

	JLabel    numMatches;
	JTextArea matchingText;
	JLabel    hide;
	JLabel    showMore;

	JMPanel   deviations;

	public ResultRow(Match m) {

		setupNumMatches(m);
		setupMatchingText(m);

		if(m.matches.size() > 0) {
			setupShowMore(m);
			setupDeviations(m);	
		}

		setupHideButton(m);
	}

	private void setupNumMatches(Match m) {
		numMatches = new JLabel(m.numMatches + "") {
			private static final long serialVersionUID = -8148145318501362945L;

			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D gg = setupG2(g);

				gg.drawLine(0, 0, getWidth(), 0); // Top
				gg.drawLine(0, 0, 0, getHeight()); // Left
				if(!deviations.isVisible()) gg.drawLine(0, getHeight()-1, getWidth(), getHeight()-1); // Bottom
			}
		};

		setUpComponent(numMatches, 8, 8);
	}

	private void setupMatchingText(Match m) {
		matchingText =  new JTextArea(m.searchedOn) {
			private static final long serialVersionUID = -8148145318501362946L;

			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D gg = setupG2(g);

				gg.drawLine(0, 0, getWidth(), 0); // Top
				if(!deviations.isVisible()) gg.drawLine(0, getHeight()-1, getWidth(),   getHeight()-1); // bottom

				gg.setColor(JMColor.DEFAULT_BORDER_COLOR);
				gg.drawLine(0, 6, 0, getHeight()-6); // Left padding border
				gg.drawLine(getWidth()-1, 6, getWidth()-1, getHeight()-6); // Right padding border
			}
		};

		setUpComponent(matchingText, 12, 12);

		matchingText.setFont(new JLabel().getFont());
		matchingText.setLineWrap(true);
		matchingText.setWrapStyleWord(true);
	}

	private void setupShowMore(Match m) {
		showMore = new JLabel(" V ") {
			private static final long serialVersionUID = -8148145318501362947L;

			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D gg = setupG2(g);

				gg.drawLine(0, 0, getWidth(), 0); // Top
				if(!deviations.isVisible()) gg.drawLine(0, getHeight()-1, getWidth(), getHeight()-1); // bottom
			}
		};

		setUpComponent(showMore, 8, 8);

		showMore.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				deviations.setVisible(!deviations.isVisible());
			}
		});
	}

	private void setupHideButton(Match m) {
		hide = new JLabel(" X ") {
			private static final long serialVersionUID = -8148145318501362947L;

			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D gg = setupG2(g);

				gg.drawLine(0, 0, getWidth(), 0); // Top
				gg.drawLine(getWidth()-1, 0, getWidth()-1, getHeight()); // Right

				if(!deviations.isVisible()) {
					gg.drawLine(0, getHeight()-1, getWidth(), getHeight()-1); // Bottom
				}
			}

		};

		setUpComponent(hide, 0, 8);
	}
	
	private void setupDeviations(Match m) {
		deviations = new JMPanel() {
			private static final long serialVersionUID = -8148145318501362948L;

			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D gg = setupG2(g);

				gg.drawLine(0, 0, 0, getHeight()); // Left
				gg.drawLine(getWidth()-1, 0, getWidth()-1, getHeight()); // Right
				gg.drawLine(0, getHeight()-1, getWidth(), getHeight()-1); // Bottom

				gg.setColor(JMColor.DEFAULT_BORDER_COLOR);
				gg.drawLine(6, 0, getWidth()-6, 0);
			}
		};
		
		setUpComponent(deviations, 6, 6);
		
		deviations.setVisible(false);
	}

	private Graphics2D setupG2(Graphics g) {
		Graphics2D gg = (Graphics2D) g;

		gg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		gg.setColor(borderColor);

		return gg;
	}

	private void repaint() {
		numMatches.repaint();
		matchingText.repaint();
		hide.repaint();
		showMore.repaint();
		deviations.repaint();
	}	

	private void setUpComponent(JComponent jc, int left, int right) {
		jc.setOpaque(true);
		jc.setBackground(JMColor.DEFAULT_BACKGROUND);
		jc.setForeground(JMColor.DEFAULT_FONT_COLOR);
		jc.setFocusable(true);
		
		createBorder(jc, Color.LIGHT_GRAY, left, right);

		
		
		jc.addMouseListener(new MouseAdapter() {			
			@Override
			public void mouseClicked(MouseEvent e) {
				jc.requestFocus();
			}
		});

		jc.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent arg0) {
				new ColorFader().fadeColor(JMColor.HOVER_BORDER_COLOR, 0);
			}

			@Override
			public void focusLost(FocusEvent arg0) {
				new ColorFader().fadeColor(JMColor.DEFAULT_BORDER_COLOR, 0);
			}
		});
	}
	
	public void removeFromJMPanel(JMPanel panel) {
		panel.remove(numMatches);
		panel.remove(matchingText);
		panel.remove(showMore);
		panel.remove(hide);
		panel.remove(deviations);
	}

	public int addToJMPanel(JMPanel panel, int y) {
		int x = 0;

		panel.add(numMatches, 
				new GridBagConstraints(x++, y, 1, 1, 0.0, 0.0, 
						GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0,0,0,0), 0, 0));
		panel.add(matchingText,
				new GridBagConstraints(x++, y, 1, 1, 1.0, 0.0, 
						GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0,0,0,0), 0, 0));

		if(showMore != null) {
			panel.add(showMore,
					new GridBagConstraints(x++, y, 1, 1, 0.0, 0.0, 
							GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0,0,0,0), 0, 0));
		}

		panel.add(hide,
				new GridBagConstraints(x++, y, 1, 1, 0.0, 0.0, 
						GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0,0,0,0), 0, 0));
		
		if(deviations != null) {
			y+=1;
			panel.add(deviations,
					new GridBagConstraints(0, y, x, 1, 1.0, 1.0, 
							GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0,0,0,0), 0, 0));
		}

		return y+=1;
	}

	/**
	 * Sets the border for this JTextField.
	 * @param borderColor is the color of the border.
	 */
	private void createBorder(JComponent jc, Color borderColor, int left, int right) {
		this.borderColor = borderColor;
		jc.setBorder(BorderFactory.createEmptyBorder(6, left, 6, right));
		jc.repaint();
	}

	/** Responsible for the color fading animation **/
	private class ColorFader {
		private void fadeColor(Color fadeToButton, int fadeSpeed) {
			if(fadeTimer != null && fadeTimer.isRunning()) {
				fadeTimer.stop();
			}

			fadeTimer = new Timer(fadeSpeed, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent evt) {
					colorFader(borderColor, fadeToButton);
					repaint();
				}    
			});

			fadeTimer.start();
		}

		private void colorFader(Color buttonfadeFrom, Color buttonFadeTo) {		
			int[] buttonA = makeRGBArray(buttonfadeFrom);
			int[] buttonB = makeRGBArray(buttonFadeTo);

			buttonA = fader(buttonA, buttonB);

			borderColor = arrayToColor(buttonA);

			if((buttonA[0] + "" + buttonA[1] + "" + buttonA[2]).equals(buttonB[0] + "" + buttonB[1] + "" + buttonB[2])) {
				repaint();
				fadeTimer.stop();
			}
		}

		/**
		 * Adds or subtracts 1 so that values of a will equal values of b.
		 * @param a the array whose values are changed
		 * @param b the array whose values are static
		 * @return an updated
		 */
		private int[] fader(int[] a, int[] b) {
			for(int i = 0; i < 3; i++) {
				if(a[i] > b[i]) {
					a[i] = a[i]-1;
				} else if(a[i] < b[i]) {
					a[i] = a[i]+1;
				}
			}

			return a;
		}

		/**
		 * Receives a color and returns the color represented as an array of integers.
		 * @param color the color to be converted
		 * @return an array of size three where...
		 * <ul>
		 * 		<li>0: Red</li>
		 *      <li>1: Green</li>
		 *      <li>2: Blue</li>
		 * </ul>
		 */
		private int[] makeRGBArray(Color color) {
			return new int[]{color.getRed(), color.getGreen(), color.getBlue()};
		}

		/**
		 * Receives an array representation of a color and returns a color.
		 * @param rgb accepts array of integers made in {@link #makeRGBArray(Color)}.
		 * @return the Color represented by rgb.
		 */
		private Color arrayToColor(int[] rgb) {
			return new Color(rgb[0], rgb[1], rgb[2]);
		}
	}


	public static void setLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}

//	public static void main(String[] args) {
//		setLookAndFeel();
//
//		JFrame frame = new JFrame();
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.setSize(new Dimension(300, 300));
//
//		JMPanel panel = new JMPanel();
//
//		panel.setLayout(new GridBagLayout());
//
//		ArrayList<Match> matches = FileManager.ImportFile("C:\\Users\\Alexander\\Desktop\\test.xml");
//
//		ResultRow rr = new ResultRow(matches.get(0));
//		ResultRow rr2 = new ResultRow(matches.get(1));
//
//		int y = 0;
//
//		y = rr.addToJMPanel(panel, y);
//		y = rr2.addToJMPanel(panel, y);
//
//		frame.add(panel);
//		frame.setVisible(true);
//	}

}
