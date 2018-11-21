package main;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class Main {

	public static void main(String[] args) {
		new Main();
	}

	HashMap<String, ArrayList<String>> matches = new HashMap<String, ArrayList<String>>();

	HashSet<String> checked = new HashSet<String>();

	public Main() {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(300, 300);
		frame.setLayout(new BorderLayout());

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		JTextArea original = new JTextArea("");
		JTextArea output   = new JTextArea("");
		panel.add(original, BorderLayout.CENTER);
		panel.add(output, BorderLayout.EAST);

		frame.add(panel, BorderLayout.CENTER);

		JButton button = new JButton("Run");
		button.addActionListener(e -> {
			run(original.getText());
		});
		frame.add(button, BorderLayout.SOUTH);

		frame.setVisible(true);
	}

	public String run(String input) {
		String toReturn = "";

		String[] split = input.split("\n|\\.");

		for(int j = 0; j < split.length; j++) {

			System.out.println("Completed: " + j + " of " + split.length);

			for(int k = 0; k < split.length; k++) {
				if(j != k) {
					if(!checked.contains(recordWorkString(j, k))){
						if(split[j].length() > 15 && split[k].length() > 15) {
							if(StringUtils.computeLevenshteinDistance(split[j], split[k]) < 5) {		
								if(!matches.containsKey(split[j])) {
									matches.put(split[j], new ArrayList<String>());
								}
								
								matches.get(split[j]).add(split[k].trim());
							}
						}
					}

					checked.add(recordWorkString(j, k));
				}
			}
		}

		String output = "";
		
		for(String s : matches.keySet()) {
			String thisLine = "\"" + s + "\"" + "," + (matches.get(s).size() + 1);
//			System.out.println(s);
//			
//			for(String t : matches.get(s)) {
//				System.out.println("\t" + t);
//			}
			
			output = thisLine + "\n" + output;
		}
		
		System.out.println(output);

		return toReturn;
	}

	public String recordWorkString(int j, int k) {
		if(j < k) {
			return j + "," + k;
		} else {
			return k + "," + j;
		}
	}
}
