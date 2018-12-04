package frontend;

import java.awt.BorderLayout;
import java.nio.file.NoSuchFileException;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import backend.FileManager;
import backend.UnDuplicate;
import jm.JMButton;
import jm.JMPanel;
import jm.JMScrollPane;
import jm.JMTextArea;

public class FindDuplicatesWindow extends JFrame {
	private static final long serialVersionUID = -9021319552711782577L;

	private UnDuplicateMenu menuBar = new UnDuplicateMenu();

	private JMPanel processingWindow = new JMPanel();

	private JMScrollPane resultsWindow;

	public FindDuplicatesWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		setJMenuBar(menuBar);
		setSize(450, 450);

		add(makeInputPanel(), BorderLayout.CENTER);
		
		addActionsToMenuBar();
		
		setVisible(true);
	}

	/** Creates the panel by which a user adds the text to analyze. **/
	private JMPanel makeInputPanel() {
		JMTextArea input = new JMTextArea();
		JMScrollPane scroll = new JMScrollPane(input);
		scroll.setHorizontalScrollBarPolicy(JMScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		JMButton runUndiplicate = new JMButton("Find Duplicates");
		runUndiplicate.addActionListener(e -> {
			addResultsWindow(new UnDuplicate(input.getText(), 5, 10, menuBar.getSelectedDelims()).resultsWindow);
		});
		
		processingWindow.setLayout(new BorderLayout());
		processingWindow.add(scroll, BorderLayout.CENTER);
		processingWindow.add(runUndiplicate, BorderLayout.SOUTH);
		
		return processingWindow;
	}
	
	/** Adds actions to buttons in the MenuBar. **/
	private void addActionsToMenuBar() {
		menuBar.fileMenu.open.addActionListener(e -> {
			try {
				String file = FileChooser.openXML();
				addResultsWindow(new ResultsWindow(FileManager.ImportFile(file), file));
			} catch (NoSuchFileException e1) {
				e1.printStackTrace();
			}
		});
	}

	private void addResultsWindow(ResultsWindow rw) {
		remove(processingWindow);

		if(resultsWindow != null) {
			remove(resultsWindow);
		}

		add(setUpScroll(rw));

		revalidate();
		repaint();
	}

	private JScrollPane setUpScroll(ResultsWindow results) {
		resultsWindow = new JMScrollPane(results);
		resultsWindow.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);		
		return resultsWindow;
	}
}
