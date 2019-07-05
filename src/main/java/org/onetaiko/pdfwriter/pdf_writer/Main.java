package org.onetaiko.pdfwriter.pdf_writer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {
	public static void main(String[] args) {
    	try {
    		// Set these variables to test
    		File outputFolder = new File("/Users/bforbis/Desktop/out");
    		File csvFile = new File("/Users/bforbis/Desktop/test.csv");
//    		String url = "";
			App app = new App(outputFolder, csvFile);
			HashMap<String, ArrayList<String>> resultMap = app.process();
			System.out.printf("Success: %d Fail: %d\n", resultMap.get("success").size(), resultMap.get("fail").size());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
