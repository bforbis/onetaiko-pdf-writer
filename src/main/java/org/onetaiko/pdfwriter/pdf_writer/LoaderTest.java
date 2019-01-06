package org.onetaiko.pdfwriter.pdf_writer;

import java.io.File;
import java.net.URL;

public class LoaderTest {
	static String PDF = "src/main/resources/ClassRegistrationEditable.pdf";
	
	public static void main(String[] args) {
		File f = new File(PDF);
		if (f.exists()) {
			System.out.println("EXITS");
		} else {
			System.out.println("NOT EXISTS");
		}

		LoaderTest obj = new LoaderTest();
		obj.getFile();
	}
	
	public void getFile() {
		ClassLoader classLoader = this.getClass().getClassLoader();
		System.out.println(classLoader);
		URL resource = classLoader.getResource("ClassRegistrationEditable.pdf");
		System.out.println(resource.toString());
		File f = new File(resource.getFile());
		System.out.println(f.getAbsolutePath());
	}
}
