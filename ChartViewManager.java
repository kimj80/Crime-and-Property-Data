package cmpt305;

import java.util.ArrayList;
import java.util.Arrays;

import javafx.scene.chart.Chart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ChartViewManager {
	HBox display;
	PropertyAssessments currentDisplay;
	CrimeRates currentCrimeDisplay;
	CrimeRates crime;
	DisplayManager manager;
	Chart currChart;
	HBox chartBox;
	
	ArrayList<String> searchedHoods = new ArrayList<String>();
	AutocompletionlTextField neighborhoodField;
	TextArea neighborhoodText;
	
	AutocompletionlTextField violationField;
	TextArea violationText;
	ArrayList<String> searchVio;
	
	TextField yearHigh;
	TextField yearLow;
	
	ChartViewManager(DisplayManager manager) {
		this.manager = manager;
		build();
	}

	public HBox getDisplay() {
		return display;
	}

	public void setItems(PropertyAssessments assessments) {
		currentDisplay = assessments;
		neighborhoodField.setItems(Arrays.asList(assessments.getNeighborHoodList()));
		
	}
	
	public void setCrime(CrimeRates crime) {
		violationField.setItems(Arrays.asList(crime.allViolations()));
	}
	
	private void build() {
		display = new HBox();
		VBox optionBox = generateOptionBox();
		VBox rightBox = new VBox();
		VBox searchBox = generateSearchBox();
		chartBox = new HBox();
		rightBox.getChildren().setAll(searchBox, chartBox);
		rightBox.setStyle("-fx-padding: 10;" + 
				"-fx-border-style: solid inside;" + 
				"-fx-border-width: 1;" +
				"-fx-border-insets: 5;" + 
				"-fx-border-radius: 0;" + 
				"-fx-border-color: grey;");
		
		display.getChildren().setAll(optionBox,rightBox);
	}
	
	private VBox generateSearchBox() {
		VBox mainBox = new VBox();
		
		HBox neighborhood = new HBox();
		neighborhoodField = new AutocompletionlTextField();
		Button addButton = new Button("add");
		Button removeButton =  new Button("reset");
		neighborhoodText = new TextArea();
		neighborhoodText.setText("");

		addButton.setOnAction(event -> {
			doAddNeighborhood();
		});
		removeButton.setOnAction(event -> {
			doResetNeighborhood();
		});
		neighborhood.getChildren().addAll(neighborhoodField, addButton, removeButton, neighborhoodText);
		
		HBox crime = new HBox();
		violationField = new AutocompletionlTextField();
		Button addButton2 = new Button("add");
		Button removeButton2 =  new Button("reset");
		violationText = new TextArea();
		violationText.setText("");
		addButton2.setOnAction(event -> {
			doAddViolation();
		});
		removeButton2.setOnAction(event -> {
			doResetViolation();
		});
		crime.getChildren().addAll(violationField,addButton2,removeButton2, violationText);
		
		HBox yearBox = new HBox();
		
		Label yearLLow = new Label("Lower");
		Label yearLHigh = new Label("Upper");
		yearLow = new TextField();
		yearHigh = new TextField();
		yearLow.setTextFormatter(
				new TextFormatter<Integer>(TableViewManager.integerFilter));
		yearHigh.setTextFormatter(
				new TextFormatter<Integer>(TableViewManager.integerFilter));
		
		VBox Vlow = new VBox();
		Vlow.getChildren().addAll(yearLLow, yearLow);
		VBox Vhigh = new VBox();
		Vhigh.getChildren().addAll(yearLHigh,yearHigh);

		Button searchButton = new Button("Search");
		searchButton.setOnAction(event -> {
			doSearch();
		});
		Button resetButton = new Button("Reset");
		resetButton.setOnAction(event -> {
			doFullReset();
		});
		
		yearBox.getChildren().addAll(Vlow,Vhigh, searchButton, resetButton);
		mainBox.getChildren().addAll(neighborhood, crime, yearBox);
		return mainBox;
	}
	
	private void doSearch() {
		if (searchedHoods.size() > 0) {
			ArrayList<PropertyAssessments> assessmentssss = new ArrayList<PropertyAssessments>();
			for (String i : searchedHoods) {
				assessmentssss.add(currentDisplay.getNeighborhood(i));
				currChart = ChartFactory.createSingleBarChartValues(assessmentssss);
				chartBox.getChildren().addAll(currChart);
			}
		}
	}
	
	private void doFullReset() {
		doResetNeighborhood();
		doResetViolation();
		yearHigh.clear();
		yearLow.clear();
	}
	
	private void doResetNeighborhood() {
		for (String i: searchedHoods) {
			neighborhoodField.addEntry(i);
		}
		searchedHoods.clear();
		neighborhoodField.clear();
		neighborhoodText.setText("");
	}
	private void doResetViolation() {
		for (String i: searchedHoods) {
			neighborhoodField.addEntry(i);
		}
		searchVio.clear();
		violationField.clear();
		violationText.setText("");
	}
	
	private void doAddNeighborhood() {
		String searchEntry = neighborhoodField.getText();
		if (neighborhoodField.entryExists(searchEntry)) {
			neighborhoodField.removeEntry(searchEntry);
			neighborhoodField.clear();
			neighborhoodText.setText(neighborhoodText.getText() + ", " +searchEntry.strip().toUpperCase());
			searchedHoods.add(searchEntry);
		}
	}
	private void doAddViolation() {
		if (violationField.entryExists(violationField.getText())) {
			violationField.removeEntry(violationField.getText());
			violationField.clear();
			violationText.setText(violationText.getText() + ", " +violationField.getText().strip().toUpperCase());
		}
	}
	private VBox generateOptionBox() {
		VBox optionBox = new VBox();
		
		Button stackedBarChart = new Button("Stacked bar chart");
		Button lineChart =  new Button("Line chart");
		Button singleBarChart = new Button("Single bar chart");
		
		stackedBarChart.setOnAction(event -> {
			currChart = ChartFactory.createStackedBarChart(currentDisplay,currentCrimeDisplay);
		});
		
		lineChart.setOnAction(event -> {
			currChart = ChartFactory.createLineChart(currentDisplay,currentCrimeDisplay);
		});
		
		singleBarChart.setOnAction(event -> {
			currChart = ChartFactory.createLineChart(currentDisplay,currentCrimeDisplay);
		});

		optionBox.getChildren().setAll(stackedBarChart, lineChart, singleBarChart);
		optionBox.setSpacing(10);
		optionBox.setStyle("-fx-padding: 10;" + 
				"-fx-border-style: solid inside;" + 
				"-fx-border-width: 1;" +
				"-fx-border-insets: 5;" + 
				"-fx-border-radius: 0;" + 
				"-fx-border-color: grey;");
		return optionBox;
	}
}

