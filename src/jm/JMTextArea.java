package jm;

import javax.swing.JTextArea;

public class JMTextArea extends JTextArea {
	private static final long serialVersionUID = -2346021547935315452L;
	
	public JMTextArea() {
		setupTextField();
	}

	public JMTextArea(String s) {
		super(s);
		setupTextField();
	}

	private void setupTextField() {
		setFont(new JMTextField("").getFont());
		setLineWrap(true);
		setWrapStyleWord(true);
		setOpaque(false);
	}
}
