package org.onetaiko.pdfwriter.pdf_writer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;

public class App {
	static {
		// Turn of PDFBox warnings, as it tends to complain about fonts when
		// setting form fields
		java.util.logging.Logger.getLogger("org.apache.pdfbox").setLevel(
			java.util.logging.Level.SEVERE
		);
	}

	private final static String PDF_RESOURCE_NAME = "ClassRegistrationEditable.pdf";
	
	
	List<CSVRecord> csvRecords;
	File outputFolder;
	
	private byte[] sourcePDFBytes;

	// Constructors
	public App (File outputFolder, File csvFile) throws Exception {
		try {
			getCSVRecords(csvFile);					
		} catch (Exception e) {
			throw new Exception(e);
		}
		validateOutputFolder(outputFolder);
		validateSourcePDF();
	}
	public App (File outputFolder, String csvUrl) throws Exception {
		try {
			getCSVRecords(csvUrl);					
		} catch (Exception e) {
			throw new Exception(e);
		}
		validateOutputFolder(outputFolder);
		validateSourcePDF();
	}
	
	/**
	 * Does the actual loading of CSV Form data into PDFs
	 *
	 * @return A result structure which maps success/fail to 
	 * a list of pdf documents it would have created
	 */
	public HashMap<String, ArrayList<String>> process() {
		HashMap<String, ArrayList<String>> resultMap = new HashMap<String, ArrayList<String>>();
		resultMap.put("success", new ArrayList<String>());
		resultMap.put("fail", new ArrayList<String>());
		for (CSVRecord record : csvRecords) {
			PDDocument sourcePDF = null;
			String outputPDFName = generatePDFNameFromRow(record);
			try {
				System.out.printf("Processing %s...\n", outputPDFName);
				sourcePDF = PDDocument.load(this.sourcePDFBytes);
				FieldSetter.setFormFields(record, sourcePDF);
				sourcePDF.save(this.outputFolder.getPath() + "/" + outputPDFName);
				resultMap.get("success").add(outputPDFName);
			} catch (IOException e) {
				System.err.println(e);
				resultMap.get("fail").add(outputPDFName);
			} finally {
				try {
					if (sourcePDF != null) {
						sourcePDF.close();
					}
				} catch (IOException e) {
					// Do nothing
				}
			}
		}
		return resultMap;
	}
	
	
	// Validators and data loaders
	private void validateOutputFolder(File outputFolder) throws Exception {
		if (outputFolder.isDirectory()) {
			this.outputFolder = outputFolder;
		} else {
			throw new Exception("'" + outputFolder.getAbsolutePath() + "' is not a folder");
		}
	}

	private void validateSourcePDF() throws Exception {
		ClassLoader cl = this.getClass().getClassLoader();
		sourcePDFBytes = IOUtils.resourceToByteArray(PDF_RESOURCE_NAME, cl);
		//InputStream stream = cl.getResourceAsStream(PDF_RESOURCE_NAME);
		//sourcePDFStream = stream;
		
		//String sourcePDFByteArray = IOUtils.resourceToByteArray(PDF_RESOURCE_NAME);
//
//		URL resource = this.getClass().getClassLoader().getResource(PDF_RESOURCE_NAME);
//		File file = new File(resource.getFile());
//		System.out.println("File to load: " + file.getPath());
//		if (file.exists()) {
//			this.sourcePDFFile = file;
//		} else {
//			System.err.println(file.getAbsolutePath());
//			debugFiles();
//			throw new Exception("Could not load original PDF");
//		}
	}
	
	private static void debugFiles() {
		String[] dirPathList = {"."};
		for (String dirPath: dirPathList) {
			System.out.println("Testing path: " + dirPath);
			File dir = new File(dirPath);
			System.out.println("Number of files: " + dir.listFiles().length);
			for (File f : dir.listFiles() ) {
				System.out.println("-------------");
				System.out.println(f.getAbsolutePath());
				System.out.println(f.toString());
				System.out.println(f.getPath());
				System.out.println("-------------");
			}
		}
	}

    private void getCSVRecords(String urlString) throws MalformedURLException, IOException {
    	Matcher matcher = Pattern.compile("(https://docs.google.com/spreadsheets/d/[^/]+)").matcher(urlString);
    	if (matcher.find()) {
    		String urlPrefix = matcher.group(0);
    		CSVParser parsedCSV = CSVParser.parse(
    			new URL(urlPrefix + "/export?format=csv"),
    			StandardCharsets.UTF_8, 
    			CSVFormat.DEFAULT.withFirstRecordAsHeader()
    		);
   			this.csvRecords = parsedCSV.getRecords();
    	} else {
    		throw new MalformedURLException("URL must be a valid google sheets url");
    	}
    }
    private void getCSVRecords(File csvFile) throws IOException {
    	CSVParser parsedCSV = CSVParser.parse(
    		csvFile,
   			StandardCharsets.UTF_8, 
   			CSVFormat.DEFAULT.withFirstRecordAsHeader()
   		);
    	this.csvRecords = parsedCSV.getRecords();
    }
    
 
    // Static helper methods
    @SuppressWarnings("unused")
	private static void printFields(PDDocument doc) {
    	PDAcroForm acroForm = doc.getDocumentCatalog().getAcroForm();
    	List<PDField> fields = acroForm.getFields();
    	System.out.println("There are " + fields.size() + " fields");
        for (PDField f : fields) {
        	System.out.println(f.toString());
        }
    }

    private static String generatePDFNameFromRow(CSVRecord record) {
    	String pdfName = record.get("First Name") + record.get("Last Name") + ".pdf";
    	pdfName.replaceAll("\\s+", "");
    	return pdfName;
    }
}
