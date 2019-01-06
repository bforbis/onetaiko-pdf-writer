package org.onetaiko.pdfwriter.pdf_writer.Display;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.onetaiko.pdfwriter.pdf_writer.App;
import org.onetaiko.pdfwriter.pdf_writer.Display.FolderChooser;

public class MainPanel extends JPanel implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	private static final Color BK_COLOR_OK = Color.WHITE;
	private static final Color BK_COLOR_ERROR = Color.decode("#FFCCCC");
	private static final Color SUCCESS_COLOR = Color.decode("#4BB543");
	
	
	private JFrame rootFrame;
	
	private JLabel instructionLabel;

	// Variables for choosing the output folder location 	
	private JLabel outputFolderLabel;
	private JTextField outputFolderTextField;
	private JButton outputFolderChooserButton;
	private FolderChooser outputFolderChooser;
	private JLabel outputFolderErrorLabel;
	
	
	// Variables for choosing the CSV File
	private JLabel csvURLLabel;
	private JTextField csvURLTextField;
	private JLabel csvURLErrorLabel;

	private JLabel csvFileLocationLabel;
	private JTextField csvFileLocationTextField;
	private JButton csvFileChooserButton;
	private CSVFileChooser csvFileChooser;
	private JLabel csvFileErrorLabel;
	
	
   // Exit / Run Buttons
	private JButton exitButton;
	private JButton runButton;
	private JLabel resultLabel;
	
	public MainPanel(JFrame frame) {
		rootFrame = frame;
		setPreferredSize(new Dimension(540, 270));
		setLayout(null);
		
		instructionLabel = new JLabel(
			"Choose a folder to output PDFS to, and a CSV or URL to load data from"
		);

		// Initialize folder chooser components
		outputFolderLabel = new JLabel("Output Folder");
		outputFolderLabel.setToolTipText("Choose a folder to save output PDFs to");
		outputFolderTextField = new JTextField("");
		outputFolderChooserButton = new JButton("...");
		outputFolderChooserButton.setToolTipText("Click to find a folder");
		outputFolderChooser = new FolderChooser();

		outputFolderErrorLabel = new JLabel();
		outputFolderErrorLabel.setForeground(Color.RED);
		outputFolderErrorLabel.setVisible(false);
		
		// Initialize CSV URL components
		csvURLLabel = new JLabel("Google Sheets URL");
		csvURLLabel.setToolTipText(
			"Enter in the URL of the google sheet to pull responses from. "
			+ "This option is mutually exclusive from setting a local CSV file location."
		);
		csvURLTextField = new JTextField("");
		csvURLErrorLabel = new JLabel();
		csvURLErrorLabel.setForeground(Color.RED);
		csvURLErrorLabel.setVisible(false);


		// Initialize CSV file chooser components
		csvFileLocationLabel = new JLabel("CSV File Location");
		csvFileLocationLabel.setToolTipText(
			"Enter in a CSV file location on your computer. "
			+ "This option is mutually exclusive from setting a google sheets URL."
		);
		csvFileLocationTextField = new JTextField("");
		csvFileChooserButton = new JButton("...");
		csvFileChooserButton.setToolTipText("Click to find a CSV file");
		csvFileChooser = new CSVFileChooser();
		csvFileErrorLabel = new JLabel();
		csvFileErrorLabel.setForeground(Color.RED);
		csvFileErrorLabel.setVisible(false);
		
		exitButton = new JButton("Exit");
		runButton = new JButton("Run");
		resultLabel = new JLabel("");
		resultLabel.setVisible(false);

		// All components have the same height
		final int height = 25;
		final int labelX = 20;
		final int labelWidth = 120;
		final int textFieldX = 160;
		final int textFieldWidth = 300;
		final int errorFieldX = textFieldX + 5;
		final int chooserButtonX = 460;
		final int chooserButtonWidth = 50;
		
		addComponentWithPosition(instructionLabel, labelX, 10, 500, 25);
		
		// Calculate y positions dynamically
		int y = 40;
		addComponentWithPosition(outputFolderLabel, labelX, y, labelWidth, height);
		addComponentWithPosition(outputFolderTextField, textFieldX, y, textFieldWidth, height);
		addComponentWithPosition(outputFolderChooserButton, chooserButtonX, y, chooserButtonWidth, height);
		addComponentWithPosition(outputFolderErrorLabel, errorFieldX, y+height, textFieldWidth, height);
		
		y = outputFolderErrorLabel.getBounds().y + height;
		addComponentWithPosition(csvFileLocationLabel, labelX, y, labelWidth, height);
		addComponentWithPosition(csvFileLocationTextField, textFieldX, y, textFieldWidth, height);
		addComponentWithPosition(csvFileChooserButton, chooserButtonX, y, chooserButtonWidth, height);
		addComponentWithPosition(csvFileErrorLabel, errorFieldX, y+height, textFieldWidth, height);
		
		y = csvFileErrorLabel.getBounds().y + height;
		addComponentWithPosition(csvURLLabel, labelX, y, labelWidth+20, height);
		addComponentWithPosition(csvURLTextField, textFieldX, y, textFieldWidth, height);
		addComponentWithPosition(csvURLErrorLabel, errorFieldX, y+25, textFieldWidth, height);
		
		y = csvURLErrorLabel.getBounds().y + 50;
		addComponentWithPosition(exitButton, 330, y, 100, height);
		addComponentWithPosition(runButton, 430, y, 100, height);
		addComponentWithPosition(resultLabel, labelX, y+25, 300, height);

	}
	
	private void addComponentWithPosition(JComponent component, int x, int y, int width, int height) {
		component.setBounds(x, y, width, height);
		add(component);
		if (component instanceof JButton) {
			((JButton) component).addActionListener(this);
		}
	}


	/**
	 * Handles action events and dispatches them to the custom handlers below
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == outputFolderChooserButton) {
			handleFolderChooseEvent();
		} else if (e.getSource() == csvFileChooserButton) {
			handleCSVChooserEvent();
		} else if (e.getSource() == runButton) {
			handleRunButton();
		} else if (e.getSource() == exitButton) {
			handleExitButton();
		}
	}
	
	// Button Handlers
	private void handleRunButton() {
		resultLabel.setVisible(false);
		Boolean validCSV = validateCSVFields();
		Boolean validFolder = validateOutputFolder();
		if (validCSV && validFolder) {
			doRun();
		} else {
			System.out.println(":(");
		}
	}
	
	private void handleExitButton() {
		rootFrame.dispatchEvent(new WindowEvent(rootFrame, WindowEvent.WINDOW_CLOSING));
	}
	
	private void handleFolderChooseEvent() {
		int returnCode = outputFolderChooser.showDialog(null, null);
		if (returnCode == FolderChooser.APPROVE_OPTION) {
			File chosenFolder = outputFolderChooser.getSelectedFile();
			outputFolderTextField.setText(chosenFolder.getAbsolutePath());
			validateOutputFolder();
		}
	}
	
	private void handleCSVChooserEvent() {
		int returnCode = csvFileChooser.showOpenDialog(null);
		if (returnCode == CSVFileChooser.APPROVE_OPTION) {
			File chosenFile = csvFileChooser.getSelectedFile();
			csvFileLocationTextField.setText(chosenFile.getAbsolutePath());
			validateCSVFields();
		}
	}
	
	
	// Form validations
	/**
	 * Validates that the CSV input fields have correct data.
	 * 1. CSV File and CSV URL are mutually exclusive
	 * 2. If CSV URL, it looks like a valid URL
	 * 3. If CSV File, it is a valid file
	 * 
	 * If any errors occur, error labels will be made visible with the
	 * appropriate error text. Also, the error field text will be made red.
	 * 
	 * If fields are valid, any existing error message will be made invisible
	 * 
	 * @return - True if the fields are good, false otherwise
	 */
	private Boolean validateCSVFields() {
		String csvURLString = csvURLTextField.getText();
		String csvFilePath = csvFileLocationTextField.getText();
		
		Boolean urlValid = true;
		Boolean filePathValid = true;
		
		String errorMessage = "";
		
		// These fields must be mutually exclusive
		if (
			(csvURLString != null && csvURLString.length() > 0)
			&& (csvFilePath != null && csvFilePath.length() > 0)
		) {
			urlValid = false;
			filePathValid = false;
			errorMessage = "Can't have both a URL and file path selected";
		}
		// csvURLString must be a valid URL
		else if (
			(csvURLString != null && csvURLString.length() > 0)
		) {
			try {
				new URL(csvURLString);
				if (!csvURLString.startsWith("https://docs.google.com/spreadsheets/d/")) {
					urlValid = false;
					errorMessage = "Must be a valid google sheets URL";
				}
			} catch (MalformedURLException e) {
				errorMessage = "Malformed URL";
				urlValid = false;
			}
		}
		// CSV file must exist
		else if (
			(csvFilePath != null && csvFilePath.length() > 0)
		) {
			File f = new File(csvFilePath);
			if (!f.isFile()) {
				errorMessage = "CSV file does not exist";
				filePathValid = false;
			}
		}
		// Both fields are not set
		else {
			filePathValid = false;
			urlValid = false;
			errorMessage = "A CSV File or URL must be provided";
		}
		
		csvFileLocationTextField.setBackground(filePathValid ? BK_COLOR_OK : BK_COLOR_ERROR);
		csvURLTextField.setBackground(urlValid ? BK_COLOR_OK : BK_COLOR_ERROR);
		
		csvFileErrorLabel.setVisible(!filePathValid);
		csvURLErrorLabel.setVisible(!urlValid);
		csvFileErrorLabel.setText(errorMessage);
		csvURLErrorLabel.setText(errorMessage);
		
		return filePathValid && urlValid;
	}

	/**
	 * Validates that the output folder to save PDFs to is valid
	 * 
	 * If the folder has not been set, or is malformed in any way,
	 * the field will be highlighted red and an error message will be
	 * set.
	 *
	 * @return - True if the folder is valid, false otherwise
	 */
	private Boolean validateOutputFolder() {
		String outputFolderPath = outputFolderTextField.getText();
		Boolean isValid = false;
		String errorMessage = "";
		if (outputFolderPath != null && outputFolderPath.length() > 0) {
			File folder = new File(outputFolderPath);
			if (!folder.isDirectory()) {
				errorMessage = "Not a valid folder";
			} else {
				isValid = true;
			}
		}
		else {
			errorMessage = "Output folder is required";
		}
		outputFolderTextField.setBackground(isValid ? BK_COLOR_OK : BK_COLOR_ERROR);
		outputFolderErrorLabel.setText(errorMessage);
		outputFolderErrorLabel.setVisible(!isValid);
		return isValid;
	}
	
	/**
	 * Instantiates an "App" instance and actually runs the PDF program.
	 * The result is then printed to the panel
	 * 
	 * NOTE: Form fields should be validated prior to calling this!
	 */
	private void doRun() {
		String csvFile = csvFileLocationTextField.getText();
		String csvURL = csvURLTextField.getText();
		String folderPath = outputFolderTextField.getText();
		
		runButton.setEnabled(false);
		try {
			App app = null;
			if (csvFile.length() > 0) {
				app = new App(new File(folderPath), new File(csvFile));
			} else {
				app = new App(new File(folderPath), csvURL);
			}
			HashMap<String, ArrayList<String>> result = app.process();
			System.out.println(result.toString());
			int success = result.get("success").size();
			int fail = result.get("fail").size();
			if (success > 0 && fail > 0) {
				resultLabel.setText("PARTIAL SUCCESS: "
						+ success + " success, "
						+ fail + " failures");
				resultLabel.setForeground(Color.RED);
			} else if (success > 0) {
				resultLabel.setText("SUCCESS: " + success + " document(s)");
				resultLabel.setForeground(SUCCESS_COLOR);
			} else if (fail > 0) {
				resultLabel.setText("FAILURE: " + fail + " document(s)");
				resultLabel.setForeground(Color.RED);
			} else if (success == 0 && fail == 0 ) {
				resultLabel.setText("ERROR: No rows to process");
				resultLabel.setForeground(Color.RED);
			}
		} catch (Exception e) {
			resultLabel.setText("FAILURE: " + e.getMessage());
			resultLabel.setForeground(Color.RED);
			e.printStackTrace();
		} finally {
			runButton.setEnabled(true);
			resultLabel.setVisible(true);
		}
	}

}
