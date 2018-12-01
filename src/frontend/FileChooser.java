package frontend;

import java.io.File;
import java.nio.file.NoSuchFileException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

public class FileChooser {

	public static String openXML() throws NoSuchFileException {
		JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		jfc.setDialogTitle("Open File");
		jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);

		FileNameExtensionFilter filter = new FileNameExtensionFilter("XML Project Files", "xml", "XML");
		jfc.addChoosableFileFilter(filter);

		int returnValue = jfc.showOpenDialog(null);

		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File selectedFile = jfc.getSelectedFile();
			return selectedFile.getAbsolutePath();
		}

		throw new NoSuchFileException("The selected file either could not be found or was not an approved option.");
	}

	public static String saveXML() throws NoSuchFileException {
		JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		jfc.setDialogTitle("Save As...");
		jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);

		FileNameExtensionFilter filter = new FileNameExtensionFilter("XML Project Files", "xml", "XML");
		jfc.addChoosableFileFilter(filter);
		
		int returnValue = jfc.showSaveDialog(null);

		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File selectedFile = jfc.getSelectedFile();
			
			String path = selectedFile.getAbsolutePath();
			
			if(path.endsWith("xml")) {
				return path;
			} else {
				return path + ".xml";
			}
		}

		throw new NoSuchFileException("The file could not be saved to the selected location.");
	}

}
