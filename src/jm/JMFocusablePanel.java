package jm;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Timer;

public class JMFocusablePanel extends JMPanel {
	private static final long serialVersionUID = -1875609067721697622L;

	private Color borderColor = JMColor.DEFAULT_BORDER_COLOR;

	private Timer fadeTimer;

	public JMFocusablePanel() {
		setUpPanel();
	}

	private void setUpPanel() {
		setBackground(JMColor.DEFAULT_BACKGROUND);
		setForeground(JMColor.DEFAULT_FONT_COLOR);

		createBorder(Color.LIGHT_GRAY);

		setFocusable(true);
		
		addMouseListener(new MouseAdapter() {			
			@Override
			public void mouseClicked(MouseEvent e) {
				requestFocus();
			}
		});
		
		addFocusListener(new FocusListener() {
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

	/**
	 * Sets the border for this JTextField.
	 * @param borderColor is the color of the border.
	 */
	private void createBorder(Color borderColor) {
		this.borderColor = borderColor;
		setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

		repaint();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D gg = (Graphics2D) g;

		gg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		gg.setColor(borderColor);
		gg.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 4, 4);
	}

	/** Stores a string and a caret position **/
	public class UndoString {
		String undoString;
		int    undoInt;

		public UndoString(String undoString, int undoInt) {
			this.undoString = undoString;
			this.undoInt    = undoInt;
		}
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
	
}
