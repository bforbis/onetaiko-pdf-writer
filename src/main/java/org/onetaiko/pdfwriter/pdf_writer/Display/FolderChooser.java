package org.onetaiko.pdfwriter.pdf_writer.Display;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

/**
 * Folder chooser is meant for finding a folder to save PDFs to
 * @author bforbis
 */
public class FolderChooser extends JFileChooser {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FolderChooser() {
		super();
		setDialogTitle("Choose a save location for PDFs");
    	setDialogTitle("Choose a save location for PDFs");
    	setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    	setAcceptAllFileFilterUsed(false);
    	setFileFilter(new FolderFilter());
    	setApproveButtonText("Select");
	}

	private class FolderFilter extends FileFilter {
		@Override
		public boolean accept(File f) {
			return f.isDirectory();
		}
		@Override
		public String getDescription() {
			return "Folders";
		}
	}
}
