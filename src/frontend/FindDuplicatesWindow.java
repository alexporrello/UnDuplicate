package frontend;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.nio.file.NoSuchFileException;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import backend.FileManager;
import backend.MatchSort;
import backend.UnDuplicate;
import jm.JMButton;
import jm.JMLabel;
import jm.JMPanel;
import jm.JMScrollPane;
import jm.JMTextArea;

public class FindDuplicatesWindow extends JFrame {
	private static final long serialVersionUID = -9021319552711782577L;

	private UnDuplicateMenu menuBar = new UnDuplicateMenu();

	private JMPanel processingWindow = new JMPanel();

	private JMScrollPane resultsWindow;

	private ResultsWindow mainResultsWindow;

	private JMTextArea input = new JMTextArea();
	
	private int fidelity = 5;

	public FindDuplicatesWindow() {
		super("UnDuplicate");

		completeSetUp();
	}

	public FindDuplicatesWindow(String url) {
		super("UnDuplicate");

		completeSetUp();
		addResultsWindow(new ResultsWindow(FileManager.ImportFile(url), url));
	}

	public void completeSetUp() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		setJMenuBar(menuBar);
		setSize(450, 450);

		input.setFont(new JMLabel("").getFont());

		add(makeInputPanel(), BorderLayout.CENTER);

		addActionsToMenuBar();

		setVisible(true);
	}

	/** Creates the panel by which a user adds the text to analyze. **/
	private JMPanel makeInputPanel() {
		input.setText("");
		JMScrollPane scroll = new JMScrollPane(input);
		scroll.setHorizontalScrollBarPolicy(JMScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		JMButton runUndiplicate = new JMButton("Find Duplicates");
		runUndiplicate.addActionListener(e -> {
			UnDuplicate results = new UnDuplicate(input.getText(), fidelity, 10, menuBar.getSelectedDelims());
			ResultsWindow resultsWindow;

			try {
				String exportURL = FileChooser.saveXML();
				FileManager.ExportFile(results.allMatches, exportURL);
				resultsWindow = new ResultsWindow(results.allMatches, exportURL);
			} catch (NoSuchFileException f) {
				FileManager.ExportFile(results.allMatches);
				resultsWindow = new ResultsWindow(results.allMatches);
			}

			addResultsWindow(resultsWindow);
		});

		processingWindow.setLayout(new GridBagLayout());
		processingWindow.add(scroll,
				new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,       new Insets(5,5,5,5), 0, 0));
		processingWindow.add(runUndiplicate,
				new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0,5,5,5), 0, 0));

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
		menuBar.fileMenu.generateExample.addActionListener(e -> {
			input.setText(exampleText());
		});
		menuBar.fileMenu.processText.addActionListener(e -> {
			addProcessingWindow();
		});

		menuBar.settingsMenu.changeSort.numMatches.addActionListener(e -> {
			if(mainResultsWindow != null) {
				this.mainResultsWindow.changeSortMethod(MatchSort.NUM_MATCHES);
			}
		});

		menuBar.settingsMenu.changeSort.original.addActionListener(e -> {
			if(mainResultsWindow != null) {
				this.mainResultsWindow.changeSortMethod(MatchSort.NONE);
			}
		});

		menuBar.settingsMenu.changeSort.textLength.addActionListener(e -> {
			if(mainResultsWindow != null) {
				this.mainResultsWindow.changeSortMethod(MatchSort.TEXT_LENGTH);
			}
		});

		menuBar.settingsMenu.changeFidelity.high.addActionListener(e -> {
			this.fidelity = 0;
		});
		menuBar.settingsMenu.changeFidelity.med.addActionListener(e -> {
			this.fidelity = 5;
		});
		menuBar.settingsMenu.changeFidelity.low.addActionListener(e -> {
			this.fidelity = 12;
		});
	}

	private void addResultsWindow(ResultsWindow rw) {
		remove(processingWindow);

		if(resultsWindow != null) {
			remove(resultsWindow);
		}

		this.mainResultsWindow = rw;

		add(setUpScroll(), BorderLayout.CENTER);

		revalidate();
		repaint();
	}

	private void addProcessingWindow() {
		remove(processingWindow);

		if(resultsWindow != null) {
			remove(resultsWindow);
		}

		add(processingWindow, BorderLayout.CENTER);

		revalidate();
		repaint();
	}

	private JScrollPane setUpScroll() {
		resultsWindow = new JMScrollPane(this.mainResultsWindow);
		resultsWindow.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);		
		return resultsWindow;
	}

	private String exampleText() {
		return "Lorem ipsum dolor sit amet, consectetur adipiscing elit, "
				+ "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. "
				+ "Excepteur sint occaecat cupidatat non proident, "
				+ "sunt in culpa qui officia deserunt mollit anim id est laborumus. "
				+ "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris "
				+ "nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in "
				+ "reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla "
				+ "pariatur. Excepteur sint occaecat cupidatat non proident, "
				+ "sunt in culpa qui officia deserunt mollit anim id est laborum. "
				+ "Lorem ipsum dolor sit amet, consectetur adipiscing elit, "
				+ "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";
	}
}
