package frontend;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import backend.Delimiter;
import backend.UnDuplicate;

public class UserInterface extends JFrame {
	private static final long serialVersionUID = -9021319552711782577L;
	
	JButton button = new JButton("Find Duplicates");
	
	JTextArea input = new JTextArea();
	
	Delimiter[] delims = {Delimiter.NEW_LINE, Delimiter.PERIOD};
	
	public UserInterface() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(450, 450);
		setLayout(new BorderLayout());

		JScrollPane scroll = new JScrollPane(input);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		input.setWrapStyleWord(true);
		input.setLineWrap(true);
		
		add(scroll, BorderLayout.CENTER);

		button.addActionListener(e -> {
			new UnDuplicate(input.getText(), 5, 10, delims);
		});
		add(button, BorderLayout.SOUTH);
		
		setVisible(true);
	}
}
