package frontend;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import backend.Delimiter;
import backend.UnDuplicate;

public class UserInterface extends JFrame {
	private static final long serialVersionUID = -9021319552711782577L;
	
	private JButton button = new JButton("Find Duplicates");
	
	private JTextArea input = new JTextArea();
	
	private UnDiplicateMenu menuBar = new UnDiplicateMenu();
	
	public UserInterface() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(450, 450);
		setLayout(new BorderLayout());
		setJMenuBar(menuBar);
		
		add(setUpInput(),  BorderLayout.CENTER);
		add(setUpButton(), BorderLayout.SOUTH);
				
		setVisible(true);
	}
	
	private JScrollPane setUpInput() {
		JScrollPane scroll = new JScrollPane(input);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		input.setWrapStyleWord(true);
		input.setLineWrap(true);
		
		return scroll;
	}
	
	private JButton setUpButton() {
		button = new JButton("Find Duplicates");
		button.addActionListener(e -> {
			new UnDuplicate(input.getText(), 5, 10, menuBar.getSelectedDelims());
		});
		
		return button;
	}
	
	public class UnDiplicateMenu extends JMenuBar {
		private static final long serialVersionUID = -8381487169056702349L;

		public Settings settingsMenu = new Settings();
		
		public UnDiplicateMenu() {
			setBorder(BorderFactory.createEmptyBorder());
			
			add(settingsMenu);
		}
		
		public Delimiter[] getSelectedDelims() {
			return settingsMenu.selectedDelims();
		}
		
		public class Settings extends JMenu {
			private static final long serialVersionUID = -1272025294155087559L;
			
			public JMenu delimiters;
			
			JCheckBoxMenuItem newLine;
			JCheckBoxMenuItem period;
			JCheckBoxMenuItem space;
			JCheckBoxMenuItem comma;
			JCheckBoxMenuItem colon;
			JCheckBoxMenuItem semiColon;
			JCheckBoxMenuItem questionMark;
			JCheckBoxMenuItem exclamationPoint;
			
			public Settings() {
				super("Settings");
				
				add(setUpDelimitersMenu());
			}
			
			private JMenu setUpDelimitersMenu() {
				delimiters = new JMenu("Delimiters");
				
				newLine = new JCheckBoxMenuItem("New Line");
				period = new JCheckBoxMenuItem("Period");
				space = new JCheckBoxMenuItem("Space");
				comma = new JCheckBoxMenuItem("Comma");
				colon = new JCheckBoxMenuItem("Colon");
				semiColon = new JCheckBoxMenuItem("Semi-colon");
				questionMark = new JCheckBoxMenuItem("Qustion Mark");
				exclamationPoint = new JCheckBoxMenuItem("Exclamation Point");
				
				delimiters.add(newLine);
				delimiters.add(period);
				delimiters.add(space);
				delimiters.add(comma);
				delimiters.add(colon);
				delimiters.add(semiColon);
				delimiters.add(questionMark);
				delimiters.add(exclamationPoint);
				
				newLine.setSelected(true);
				period.setSelected(true);
				
				return delimiters;
			}
			
			public Delimiter[] selectedDelims() {
				ArrayList<Delimiter> delims = new ArrayList<Delimiter>();
				
				if(newLine.isSelected()) delims.add(Delimiter.NEW_LINE);
				if(period.isSelected()) delims.add(Delimiter.PERIOD);
				if(space.isSelected()) delims.add(Delimiter.SPACE);
				if(comma.isSelected()) delims.add(Delimiter.COMMA);
				if(colon.isSelected()) delims.add(Delimiter.COLON);
				if(semiColon.isSelected()) delims.add(Delimiter.SEMI_COLON);
				if(questionMark.isSelected()) delims.add(Delimiter.QUESTION_MARK);
				if(exclamationPoint.isSelected()) delims.add(Delimiter.EXCLAMATION_POINT);
				
				return delims.toArray(new Delimiter[delims.size()]);
			}
		}
	}
}
