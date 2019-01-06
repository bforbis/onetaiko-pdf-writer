package org.onetaiko.pdfwriter.pdf_writer.Display;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class CSVFileChooser extends JFileChooser {

	private static final long serialVersionUID = 1L;

	CSVFileChooser() {
		super();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV", "csv");
        setFileFilter(filter);
	}
}
