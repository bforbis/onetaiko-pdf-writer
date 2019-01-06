package org.onetaiko.pdfwriter.pdf_writer;

import java.io.IOException;

import org.apache.commons.csv.CSVRecord;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;

public class FieldSetter {
	public static void setFormFields(CSVRecord row, PDDocument pdf) throws IOException {
    	PDAcroForm acroForm = pdf.getDocumentCatalog().getAcroForm();
    	
    	String fullName = row.get("First Name") + " " + row.get("Last Name");
    	
    	/**
    	 * Need to setNeedAppearances here before/after the changes, otherwise
    	 * the form data will not get refreshed into the pdf stream and won't
    	 * show up. Apparently this will be fixed in pdfbox v3.0
    	 * https://issues.apache.org/jira/browse/PDFBOX-3356
    	 */
    	acroForm.setNeedAppearances(false);
    	try {
    		setField(acroForm, "STUDENT_NAME", fullName);
			setField(acroForm, "AGREEMENT_NAME",fullName);
			setField(acroForm, "EMAIL",row.get("Email"));
			setField(acroForm, "ADDRESS",row.get("Mailing Address"));
			setField(acroForm, "CITY",row.get("City"));
			setField(acroForm, "STATE",row.get("State"));
			setField(acroForm, "ZIP",row.get("Zip"));
			setField(acroForm, "PHONE",row.get("Phone Number"));
			setField(acroForm, "EMERGENCY_CONTACT",row.get("Emergency Contact Name"));
			setField(acroForm, "EMERGENCY_PHONE",row.get("Emergency Contact Phone Number"));
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	private static void setField(PDAcroForm acroForm, String fieldName, String fieldValue) throws IOException {
		PDField field = acroForm.getField(fieldName);
		if (field != null) {
			field.setValue(fieldValue);
		}
	}
}
