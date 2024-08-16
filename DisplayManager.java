package cmpt305;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;

@SuppressWarnings("unused")
public class DisplayManager {

	private PropertyAssessments currentDisplay;
	private TabPane mainTabs;
	private VBox app;
	private MapViewManager map = null;
	private TableViewManager table = null;
	//private ChartViewManager chart = null;
	private Main main;

	DisplayManager(Main main) {
		this.main = main;
		mainTabs = makeTabPane();
		app = new VBox(mainTabs);
	}

	public VBox display() {
		return app;
	}
	
	public void setItems(PropertyAssessments assessments) {
		currentDisplay = assessments;
		map.setItems(assessments);
		table.setItems(assessments);
		//chart.setItems(assessments);
	}
	
	public void setCrime(CrimeRates crime) {
		//chart.setCrime(crime);
	}
	
	public void reset() {
		setItems(main.getAssessments());
	}

	public void giveSearchResults(PropertyAssessments results) {
		setItems(results);
	}

	private MapViewManager makeMapView() {
		return new MapViewManager(this);
	}
	
	private TableViewManager makeTableView() {
		return new TableViewManager(this);
	}
	
	private ChartViewManager makeChartView() {
		return new ChartViewManager(this);
	}
	
	private TabPane makeTabPane() {
		map = makeMapView();
		table = makeTableView();
		//chart = makeChartView();
	
		TabPane tabs = new TabPane();
		
		Tab tableTab =  new Tab("Table View");
		Tab mapTab = new Tab("Map");
		//Tab chartTab = new Tab("Charts");
		
		tableTab.setContent(table.getDisplay());
		mapTab.setContent(map.getDisplay());
		//chartTab.setContent(chart.getDisplay());
		
		tabs.getTabs().setAll(tableTab, mapTab);// chartTab);
		return tabs;
	}

}
