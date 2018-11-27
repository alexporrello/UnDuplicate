package frontend;

import java.awt.Dimension;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ResultsDialog extends JDialog {
	private static final long serialVersionUID = -6023620346742595500L;
	
	private JTextArea input = new JTextArea();
	
	public ResultsDialog(String s) {
		add(setUpInput());
		input.setText(s);
		
		setSize(new Dimension(700, 500));
		setLocationByPlatform(true);
		setVisible(true);
	}
	
	private JScrollPane setUpInput() {
		JScrollPane scroll = new JScrollPane(input);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		input.setWrapStyleWord(true);
		input.setLineWrap(true);
		
		return scroll;
	}
	
}
