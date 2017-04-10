package application;

import java.io.File;
import java.util.List;

import javafx.scene.control.TextArea;

public class FileNameReader {
	
	String path;
	TextArea mainTextArea;
	
	public FileNameReader(String path, TextArea mainTextArea) {
		this.path = path;
		this.mainTextArea = mainTextArea;
	}
	
	public FileNameReader(TextArea mainTextArea) {
		this.mainTextArea = mainTextArea;
	}
	
	public void read() {
		mainTextArea.setText("");
		String fileNameWithExtention;
		String fileName;
		
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();

		    for (int i = 0; i < listOfFiles.length; i++) {
		    	String filePath = listOfFiles[i].toString();
		      if (listOfFiles[i].isFile()) {
		    	  fileNameWithExtention = listOfFiles[i].getName();
		    	  mainTextArea.appendText(fileNameWithExtention +"\n");
		      } 
		    }    
	}
	
	public void readMultipleFiles(List <File> selectedFiles) {
		String filePath;
		String fileName;
		if (selectedFiles != null) {
			for (int i = 0; i < selectedFiles.size(); i++) {
				filePath = selectedFiles.get(i).getName();
				mainTextArea.appendText(filePath +"\n");
			}
		} else {
			//
		}
	}
}
