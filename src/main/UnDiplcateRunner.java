package main;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import frontend.FindDuplicatesWindow;

public class UnDiplcateRunner {
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
		
		new FindDuplicatesWindow();
	}
}
