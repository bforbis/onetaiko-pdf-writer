package org.onetaiko.pdfwriter.pdf_writer;

import javax.swing.JFrame;

import org.onetaiko.pdfwriter.pdf_writer.Display.MainPanel;

public class View {

    public static void main(String[] args) {
    	System.out.println("Starting the main view class");
		JFrame frame = new JFrame("Odaiko New England Form Filler");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(new MainPanel(frame));
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
		
		System.out.println("Finished adding the main panel");
	}
}
	