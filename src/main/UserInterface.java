package main;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class UserInterface extends JFrame {
	private static final long serialVersionUID = -9021319552711782577L;
	
	JButton button = new JButton("Find Duplicates");
	
	JTextArea input = new JTextArea();
	
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
			new UnDuplicate(input.getText());
		});
		add(button, BorderLayout.SOUTH);
		
		setVisible(true);
	}
	
	public static void setLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		setLookAndFeel();
		
		new UserInterface();
	}
}
