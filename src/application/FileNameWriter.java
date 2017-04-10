package application;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


public class FileNameWriter {
	
	private TextArea mainTextArea;
	private Stage primaryStage;
	//private FileChooser fileChooser;
	
	public FileNameWriter(Stage primaryStage, TextArea mainTextArea) {
		this.primaryStage = primaryStage;
		this.mainTextArea = mainTextArea;
	}
	
	public void saveToFile(File file) {
	
	    ObservableList<CharSequence> paragraph = mainTextArea.getParagraphs();
	    Iterator<CharSequence> iterator = paragraph.iterator();
	   
	    try
	    {
	        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
	        while(iterator.hasNext())
	        {
	            CharSequence seq = iterator.next();
	            bufferedWriter.append(seq);
	            bufferedWriter.newLine();
	        }
	        bufferedWriter.flush();
	        bufferedWriter.close();
	    }
	    catch (IOException e)
	    {
	        e.printStackTrace();
	    }
	}

}
