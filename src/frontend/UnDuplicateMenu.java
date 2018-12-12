package frontend;

import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import backend.Delimiter;

public class UnDuplicateMenu extends JMenuBar {
	private static final long serialVersionUID = -8381487169056702349L;

	public Settings settingsMenu = new Settings();
	public File     fileMenu     = new File();

	public UnDuplicateMenu() {
		setBorder(BorderFactory.createEmptyBorder());

		add(fileMenu);
		add(settingsMenu);
	}

	public Delimiter[] getSelectedDelims() {
		return settingsMenu.selectedDelims();
	}

	public class File extends JMenu {
		private static final long serialVersionUID = 9210779921126243883L;

		JMenuItem open = new JMenuItem("Open File...");
		JMenuItem processText = new JMenuItem("Open Text Processing Screen");
		JMenuItem generateExample = new JMenuItem("Generate Example Text");

		ChangeSortMenu changeSort = new ChangeSortMenu();
		
		public File() {
			super("File");

			add(open);

			addSeparator();

			add(processText);
			add(changeSort);
			
			addSeparator();

			add(generateExample);
		}

		public class ChangeSortMenu extends JMenu {
			private static final long serialVersionUID = -8269980835818019418L;

			public JMenuItem none = new JMenuItem("No Sort");
			public JMenuItem numMatches = new JMenuItem("Number of Duplicates");
			public JMenuItem textLength = new JMenuItem("Length of Duplicate String");
			
			public ChangeSortMenu() {
				super("Change Sort Order");
				
				add(none);
				add(numMatches);
				add(textLength);
			}
		}
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