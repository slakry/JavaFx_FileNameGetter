package application;
	
import java.awt.event.WindowStateListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import com.sun.javafx.logging.Logger;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	
	private MenuBar menuBar;
	private Menu chooseMenu;
	private Menu authorMenu;

	private MenuItem selectDirectoryMenuItem;
	private MenuItem selectFilesMenuItem;
	private MenuItem saveAsMenuItem;
	private MenuItem exitMenuItem;
	private MenuItem aboutAuthorMenuItem;

	private TextArea mainTextArea;
	private Stage windowStage;

	private FileNameReader fileNameReader;
	private FileNameWriter fileNameWriter;
	private File file;
	private FileWriter fileWriter;
	private StringBuilder name;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			windowStage = primaryStage;
			windowStage.setTitle("File Name Reader ver. 1.0");
			windowStage.setOnCloseRequest(e -> {
				e.consume();			//
				closeProgram();
			});
			
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root,500,500);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			// --- Create MenuBar
			menuBar = new MenuBar();
			menuBar.prefWidthProperty().bind(windowStage.widthProperty());
			root.setTop(menuBar);
			
			//--- Create MENUS
			chooseMenu = new Menu("Choose");
				selectDirectoryMenuItem = new MenuItem("Select Directory");		
				selectDirectoryMenuItem.setOnAction(e -> {
					if (mainTextArea.getText().trim().length() != 0) {
						Optional<ButtonType> result = displayConfirmationAlert(windowStage, "Information", "File is not empty. \n"
								+ "Sure you want to abort this file?");	
						if ((result.isPresent()) && (result.get() == ButtonType.OK))
								selectDirectory();
					} else {
						selectDirectory();
					}
				});
				
				selectFilesMenuItem = new MenuItem("Select Files");
				selectFilesMenuItem.setOnAction(e -> {
					if (mainTextArea.getText().trim().length() != 0) {
						Optional<ButtonType> result = displayConfirmationAlert(windowStage, "Information", "File is not empty. \n"
								+ "Sure you want to abort this file?");	
						if ((result.isPresent()) && (result.get() == ButtonType.OK))
							selectMultipleFiles();
					} else {
						selectMultipleFiles();
					}
				});
				
				saveAsMenuItem = new MenuItem("Save File As...");
				saveAsMenuItem.setOnAction((ActionEvent event) -> {
					saveText();
				});
				
				exitMenuItem = new MenuItem("Exit");
				exitMenuItem.setOnAction(e -> closeProgram());
				
				chooseMenu.getItems().addAll(selectDirectoryMenuItem, selectFilesMenuItem, saveAsMenuItem, new SeparatorMenuItem(), exitMenuItem);
							
			authorMenu = new Menu("Author");
				aboutAuthorMenuItem = new MenuItem("About Author");
				aboutAuthorMenuItem.setOnAction(e -> displayInformationAlert(windowStage,"About Author", "S³awomir Kryniewski"));
				authorMenu.getItems().addAll(aboutAuthorMenuItem);
			menuBar.getMenus().addAll(chooseMenu, authorMenu);
			
			//--- TEXT AREA
			mainTextArea = new TextArea();
			root.setCenter(mainTextArea);
			
			windowStage.setScene(scene);
			windowStage.sizeToScene();
			windowStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void selectDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(windowStage);
         
        if(selectedDirectory == null){
            //mainTextArea.setText("No Directory selected");
        }else{
        	fileNameReader = new FileNameReader(selectedDirectory.getAbsolutePath(), mainTextArea);
        	fileNameReader.read();
        }
	}
	
	public void saveText() {
		FileChooser fileChooser = new FileChooser();
	    
	    FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
	    fileChooser.getExtensionFilters().add(extFilter);
	     
	    File file = fileChooser.showSaveDialog(windowStage);
	    
	    if (file == null) {
	    	//cancel
	    } else {
	    	fileNameWriter = new FileNameWriter(windowStage, mainTextArea);
	    	fileNameWriter.saveToFile(file);
	    }
	}
	
	private void selectMultipleFiles() {
		FileChooser fileChooser = new FileChooser();		
		fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", 	"*.*"),
                new FileChooser.ExtensionFilter("Text Files", 	"*.txt", "*.rtf", "*.docx","*.doc", "*.pdf",
                												"*.odt","*sxw"),
                new FileChooser.ExtensionFilter("Audio Files", 	"*.mp2","*.mp3","*.wav","*.wave","*.wma","*.mid",
                												"*.midi","*.kar","*.flac","*.mpeg3","*.aiff",
                												"*.acc","*.ogg"),
                new FileChooser.ExtensionFilter("Web Files", 	"*.html","*.htm","*.js","*.vbs","*.sfw","*.sql","*.php"),
                new FileChooser.ExtensionFilter("Image Files", 	"*.bmp","*.jpg","*.jpeg","*.gif","*.tif","*.tiff","*.swf",
                												"*.cdr", "*.fmw"),
                new FileChooser.ExtensionFilter("Video Files", 	"*.avi", "*.wmv","*.mov","*.dat","*.mkv","*.flv","*.rmvb",
                												"*.mp4","*.mpg","*.mpeg","*.svi")
            );
		
		List<File> selectedFiles  = fileChooser.showOpenMultipleDialog(windowStage);
		
		fileNameReader = new FileNameReader(mainTextArea);
		fileNameReader.readMultipleFiles(selectedFiles);
	}
	
	private static Optional<ButtonType> displayConfirmationAlert(Stage widowStage, String title, String message) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		
		alert.initOwner(widowStage);
		alert.setTitle(title);
		alert.setHeaderText(message);
		
		Optional<ButtonType> result = alert.showAndWait();
		
		return result;
	}
	
	private static void displayInformationAlert(Stage widowStage, String title, String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		
		alert.initOwner(widowStage);
		alert.setTitle(title);
		alert.setHeaderText(message);
		alert.showAndWait();
	}
	
	public void closeProgram() {
		Optional<ButtonType> result = displayConfirmationAlert(windowStage, "Close Program", "Sure you want to exit?");
		
		if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
			windowStage.close();
			Platform.exit();
			System.exit(0);
		}
	}
	
}
