package cmpt305;

import java.io.File;
import java.io.IOException;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Main extends Application {

	DisplayManager display;
	Scene mainScene;
	
	PropertyAssessments mainAssessments;
	CrimeRates crime;
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		display = new DisplayManager(this);
		MenuBar menuBar = createMenuBar();
		
		Boundaries.getBoundaries();
		
		crime = CrimeRates.crimeRatesAPI();
		display.setCrime(crime);
		
		VBox mainBox = new VBox();
		mainBox.setPrefWidth(1600);
		mainBox.setPrefHeight(900);
		Scene mainScene = new Scene(mainBox);
		primaryStage.setScene(mainScene);
		mainBox.getChildren().addAll(menuBar, display.display());
		primaryStage.show();
	}

	public PropertyAssessments getAssessments() {
		return mainAssessments;
	}
	
	private MenuBar createMenuBar() throws CloneNotSupportedException {
		MenuBar menubar = new MenuBar();
		Menu fileMenu = new Menu("File");
		Menu dataMenu = new Menu("Data");
		
		MenuItem openCSV = new MenuItem("Open CSV File");
		MenuItem refreshProperties = new MenuItem("Refresh Properties");
		MenuItem clearData = new MenuItem("Clear Data");
		MenuItem refreshCrime = new MenuItem("Refresh Crime Rates");
		MenuItem addData = new MenuItem("Add Data");
		
		openCSV.setOnAction(e -> {
			try {
				mainAssessments = choosePropAssCSV();
				update();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		refreshProperties.setOnAction(e -> {
			System.out.println("Refresh properties selected");
			try {
				mainAssessments = new PropertyAssessments(PropertyAssessments.getAPIinfo());
				display.setItems(getAssessments());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		clearData.setOnAction(e -> {
			System.out.println("Clear data selected");
		});
		refreshCrime.setOnAction(e -> {
			System.out.println("Refresh CrimeRates Data");
			try {
				crime = CrimeRates.crimeRatesAPI();
				display.setCrime(crime);
			} catch (CloneNotSupportedException | IOException e1) {
				e1.printStackTrace();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
		});
		addData.setOnAction(e -> {
			System.out.println("Add data selected");
		});
		
		fileMenu.getItems().addAll(openCSV, addData);
		dataMenu.getItems().addAll(clearData, refreshCrime, refreshProperties);
		// open csv file
		// refresh
		// clear data
		// refresh crime rates
		// add data (csv or api)
		menubar.getMenus().addAll(fileMenu, dataMenu);
		return menubar;
	}
	
	private void update() {
		display.setItems(mainAssessments);
	}
	
	private static PropertyAssessments choosePropAssCSV() throws IOException {
		Stage newStage = new Stage();
		FileChooser chooser = new FileChooser();
		File selectedFile = chooser.showOpenDialog(newStage);
		newStage.close();
		String fileName = selectedFile.getAbsolutePath();
		return new PropertyAssessments(fileName);
	}
}
