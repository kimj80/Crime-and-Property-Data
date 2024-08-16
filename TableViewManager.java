package cmpt305;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.UnaryOperator;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class TableViewManager {

	static int windowX = WindowSize.windowX;
	static int windowY = WindowSize.windowY;
	
	VBox searchBox;
	VBox tableBox;
	VBox statsBox;
	HBox mainBox;
	TextArea displayStatistics;

	TableView<PropertyAssessment> table;

	AutocompletionlTextField[] searchFields;
	TextField maxField;
	TextField minField;
	ComboBox<String> classBox;
	String[] assessmentClassOptions;

	private PropertyAssessments currentDisplay;
	
	private final DisplayManager manager;
	
	public HBox getDisplay() {
		return mainBox;
	}
	
	public void setItems(PropertyAssessments assessments) {
		this.currentDisplay = assessments;
		table.setItems(FXCollections.observableArrayList(currentDisplay.getAssessments()));
		updateComboBox();
		table.refresh();
		updateTextArea();
		searchFields[2].setItems(Arrays.asList(assessments.getNeighborHoodList()));
		searchFields[0].setItems(assessments.getAccountNumberList());
	}
	
	TableViewManager(DisplayManager manager){//PropertyAssessments assessments) {
		//this.assessments = assessments;
		//currentDisplay = this.assessments;
		this.manager = manager;
		build();
		//table.setItems(FXCollections.observableArrayList(currentDisplay.getAssessments()));
	}
	
	private void updateComboBox() {
		
		ArrayList<String> uniqueAssessmentClasses = new ArrayList<String>();
		for (PropertyAssessment i : currentDisplay.getAssessments()) {
			if (!(uniqueAssessmentClasses.contains(i.getAssessmentClass()))) {
				uniqueAssessmentClasses.add(i.getAssessmentClass());
			}
		}
		assessmentClassOptions = uniqueAssessmentClasses.toArray(new String[uniqueAssessmentClasses.size()]);
		classBox.setItems(FXCollections.observableArrayList(assessmentClassOptions));
		classBox.setValue("");
	}
	
	private void build() {
		table = generateTable();
		// default items, without search


		tableBox = generateTableVBox(table);
		searchBox = generateSearchVBox();
		statsBox = generateStatsBox();
		VBox sideBox = new VBox(2);
		Separator spacer =  new Separator();
		spacer.setPadding(new Insets(0,0,20,0));
		sideBox.getChildren().setAll(searchBox,spacer, statsBox);
		sideBox.setStyle("-fx-padding: 10;" + 
				"-fx-border-style: solid inside;" + 
				"-fx-border-width: 1;" +
				"-fx-border-insets: 5;" + 
				"-fx-border-radius: 0;" + 
				"-fx-border-color: grey;");
		mainBox = new HBox(2);

		mainBox.setPrefWidth(windowX);
		mainBox.getChildren().setAll(sideBox,tableBox);

	}

	
	private VBox generateStatsBox() {
		
		// generates the textArea for the stats and saves it to the member variable
		// statsBox, this allows for updating the text area

		displayStatistics = new TextArea();
		displayStatistics.setEditable(false);
		updateTextArea();

		VBox statsBox = new VBox(1);
		statsBox.getChildren().setAll(displayStatistics);
		return statsBox;
	}

	
	private VBox generateSearchVBox() {
		// generates the search box. function looks long but it's fairly simple,
		// the text areas need to be added to the list of text areas for search and reset to see them,
		// and they need to alternate with the labels, so they're all generated here
		
		VBox vBox =  new VBox(1);
		vBox.setPadding(new Insets(10,0,10,0));
		
		final Label label = new Label("Find Property Assessments");
		label.setFont(Font.font("Arial", FontWeight.BOLD, 16));

		Button searchButton = new Button("Search");
		searchButton.setOnAction(event -> {
			doSearch();
		});

		Button resetButton = new Button("Reset");
		resetButton.setOnAction(event -> {
			doReset();
		});
		
		HBox buttonBox = new HBox(searchButton, resetButton);
		buttonBox.setPadding(new Insets(10,0,20,0));
		buttonBox.setSpacing(10);

		classBox = new ComboBox<String>();
		classBox.setValue("");

		AutocompletionlTextField accountNumberField =  new AutocompletionlTextField();
		accountNumberField.setPrefWidth(windowX/3);
		AutocompletionlTextField addressField = new AutocompletionlTextField();
		addressField.setPrefWidth(windowX/3);
		AutocompletionlTextField neighborhoodField = new AutocompletionlTextField();
		neighborhoodField.setPrefWidth(windowX/3);

		searchFields = new AutocompletionlTextField[3];
		searchFields[0] = accountNumberField;
		searchFields[1] = addressField;
		searchFields[2] = neighborhoodField;
	
		accountNumberField.setTextFormatter(
				new TextFormatter<Integer>(integerFilter));
		accountNumberField.setOnKeyPressed((event) -> { 
        	if(event.getCode() == KeyCode.ENTER) {
            	doSearch();
        	} 
        });
		addressField.setOnKeyPressed((event) -> { 
        	if(event.getCode() == KeyCode.ENTER) {
            	doSearch();
        	} 
        });
		neighborhoodField.setOnKeyPressed((event) -> { 
        	if(event.getCode() == KeyCode.ENTER) {
            	doSearch();
        	} 
        });
		
		maxField = new TextField();
		minField = new TextField();
		
		maxField.setOnKeyPressed((event) -> { 
        	if(event.getCode() == KeyCode.ENTER) {
            	doSearch();
        	} 
        });
		minField.setOnKeyPressed((event) -> { 
        	if(event.getCode() == KeyCode.ENTER) {
            	doSearch();
        	} 
        });
		
		maxField.setTextFormatter(
				new TextFormatter<Integer>(integerFilter));
		minField.setTextFormatter(
				new TextFormatter<Integer>(integerFilter));
		Label maxLabel = new Label("Max");
		Label minLabel = new Label("Min");
		VBox minBox = new VBox(minField,minLabel);
		VBox maxBox = new VBox(maxField,maxLabel);
		HBox rangeBox = new HBox(minBox,maxBox);
		rangeBox.setPadding(new Insets(10,0,20,0));

		
		Label accountNumberLabel = new Label("Account Number:");
		accountNumberLabel.setPadding(new Insets(10,0,10,0));
		Label addressLabel = new Label("Address (#suite #house street)");
		addressLabel.setPadding(new Insets(10,0,10,0));
		Label neighborhoodLabel = new Label("Neighborhood");
		neighborhoodLabel.setPadding(new Insets(10,0,10,0));
		Label classLabel = new Label("Assessment Class");
		classLabel.setPadding(new Insets(10,0,10,0));

		vBox.getChildren().addAll(label,accountNumberLabel, accountNumberField, addressLabel, addressField, neighborhoodLabel, neighborhoodField,classLabel, classBox, rangeBox, buttonBox);
		return vBox;
	}

	void doSearch() {
		// search in 4 phases, depending if the thing was decided. inefficient?
		// O(n) complexity, but N is decreasing. NlogN????
		// searchFields[0] does account number
		// 1 does the address string
		// 2 does the neighborhood
		
		manager.reset();
		
		PropertyAssessments searchedAssessments = currentDisplay;
		
		// account number lookup is a special case because account numbers are unique
		if (!(searchFields[0].getText().equals(""))) {
			PropertyAssessment foundAssessment = searchedAssessments.getAccountNumber(Integer.parseInt(searchFields[0].getText()));
			ArrayList<PropertyAssessment> newAss = new ArrayList<PropertyAssessment>();
			newAss.add(foundAssessment);
			searchedAssessments = new PropertyAssessments(newAss);
			manager.giveSearchResults(searchedAssessments);
			table.setItems(FXCollections.observableArrayList(currentDisplay.getAssessments()));
			return;
		}

		if (!(searchFields[1].getText().equals(""))){
			searchedAssessments = searchedAssessments.getAddressContaining(searchFields[1].getText().strip());
		}

		if (!(searchFields[2].getText().equals(""))) {
			searchedAssessments = searchedAssessments.getNeighborhood(searchFields[2].getText().strip());
		}

		if (!(classBox.getValue().equals(""))) {
			searchedAssessments = searchedAssessments.getAssessmentClass(classBox.getValue());
		}
		int minSearch;
		if (((minField).getText().equals(""))) {
			minSearch = -1;
		}
		else {minSearch = Integer.parseInt(minField.getText());}
		int maxSearch;
		if (((maxField).getText().equals(""))) {
			maxSearch = -1;
		}
		else {maxSearch = Integer.parseInt(maxField.getText());}
		
		// this whole area is to prevent it from searching unnecessarily
		if (maxSearch != -1 || minSearch != -1) {
			int min, max;
			if (maxSearch == -1) {
				max = Integer.MAX_VALUE;
			} else max = maxSearch;
			
			if (minSearch == -1) {
				min = 0;
			} else min = minSearch;
			searchedAssessments = searchedAssessments.getPropertiesInRange(min,max);
		}

		manager.giveSearchResults(searchedAssessments);
	}
	
	void updateTextArea() {
		// generate a string and insert it in the textArea on the left
		// string contains stats data from the PropertyAssessments class that's currently
		// being displayed
		
		if (currentDisplay != null && currentDisplay.getNumberOfProperties() > 1) {
			NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();

			displayStatistics.setText(
				"Statistics of assessed Values\n"
				+ "\nNumber of Properties: " + currentDisplay.getNumberOfProperties()
				+ "\nMin: " + currencyFormat.format(currentDisplay.getMin())
				+ "\nMax: " + currencyFormat.format(currentDisplay.getMax())
				+ "\nRange: " + currencyFormat.format(currentDisplay.getRange())
				+ "\nMean: " + currencyFormat.format(currentDisplay.getMean())
				+ "\nMedian " + currencyFormat.format(currentDisplay.getMedian())
				+ "\nStandard Deviation: " + currencyFormat.format(currentDisplay.getStandardDeviation()));
		}
		else {
			displayStatistics.setText("");
		}
	}

	void doReset() {
		for (AutocompletionlTextField i : searchFields) {
			i.clear();
		}
		classBox.setValue("");
		minField.clear();
		maxField.clear();
		manager.reset();

	}

	private VBox generateTableVBox(@SuppressWarnings("rawtypes") TableView table) {
		VBox vBox = new VBox(7);
		vBox.setPadding(new Insets(10, 20, 10, 20));
		vBox.setPrefWidth(windowX);
		vBox.setPrefHeight(windowY);
		final Label label = new Label("Edmonton Property Assessments");
		label.setFont(Font.font("Arial",FontWeight.BOLD, 16));
		vBox.getChildren().addAll(label,table);
		return vBox;
	}

	@SuppressWarnings("unchecked")
	private TableView<PropertyAssessment> generateTable() {
		// unpacks the PropertyAssessment class into a javafx tableView
		// does not assign a particular list to it, however;

		TableView<PropertyAssessment> table = new TableView<>();
		table.setPrefHeight(windowY);
		table.setPrefWidth(2*(windowX)/3);

		TableColumn<PropertyAssessment, Integer> accountNumberCol = new TableColumn<>("Account");
		accountNumberCol.setCellValueFactory(new PropertyValueFactory<>("accountNumber"));
		accountNumberCol.prefWidthProperty().bind(table.widthProperty().multiply(0.1));

		TableColumn<PropertyAssessment, String> addressCol = new TableColumn<>("Address");
		addressCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getAddress().toString().contentEquals("-1") ? "" : cellData.getValue().getAddress().toString()));
		addressCol.prefWidthProperty().bind(table.widthProperty().multiply(0.2));

		NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
		TableColumn<PropertyAssessment, Integer> valueCol = new TableColumn<>("Assesed Value");
		valueCol.setCellValueFactory(new PropertyValueFactory<>("value"));
		valueCol.prefWidthProperty().bind(table.widthProperty().multiply(0.1));

		valueCol.setCellFactory(tc -> new TableCell<>() {
			@Override
			protected void updateItem(Integer value, boolean empty) {
				super.updateItem(value, empty);
				currencyFormat.setMaximumFractionDigits(0);
				setText(empty ? "" : currencyFormat.format(value));
			}
		});

		TableColumn<PropertyAssessment, String> classCol = new TableColumn<>("Assessment Class");
		classCol.setCellValueFactory(new PropertyValueFactory<>("assessmentClass"));
		classCol.prefWidthProperty().bind(table.widthProperty().multiply(0.1));

		TableColumn<PropertyAssessment, String> neighborhoodCol = new TableColumn<>("Neighborhood");
		neighborhoodCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getNeighborhood().getNeighborhoodName()));
		neighborhoodCol.prefWidthProperty().bind(table.widthProperty().multiply(0.2));

		TableColumn<PropertyAssessment, Double> latitudeCol = new TableColumn<>("Latitude");
		latitudeCol.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<Double>(cellData.getValue().getLocation().getLatitude()));
		latitudeCol.prefWidthProperty().bind(table.widthProperty().multiply(0.1));

		TableColumn<PropertyAssessment, Double> longitudeCol = new TableColumn<>("Longitude");
		longitudeCol.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<Double>(cellData.getValue().getLocation().getLongitude()));
		longitudeCol.prefWidthProperty().bind(table.widthProperty().multiply(0.1));

		table.getColumns().setAll(accountNumberCol, addressCol, valueCol, classCol, neighborhoodCol, latitudeCol, longitudeCol);

		return table;
	}

	static UnaryOperator<Change> integerFilter = change -> {
		String newText = change.getControlNewText();
		if (newText.matches("([1-9][0-9]*)?")) { 
			return change;
		}
		return null;
	};

}
