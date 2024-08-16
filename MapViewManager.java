package cmpt305;

import java.net.URL;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class MapViewManager {
	HBox display = new HBox(); //new WebMaps().buildGoogleMaps();
	WebView webView = new WebView();
	WebEngine webEngine = webView.getEngine();
	MyBrowser myBrowser;
	int counter = 0;

	private final DisplayManager manager;

	MapViewManager(DisplayManager manager) {
		this.manager = manager;
	}
	
	public HBox getDisplay() {
		//Load up google maps by going to Browser function
		myBrowser = new MyBrowser();

		display.getChildren().add(myBrowser);

		return display;
	}

	public void setItems(PropertyAssessments assessments) {

		ArrayList<PropertyAssessment> elements = (ArrayList)assessments.getAssessments();
		int numberOfObjects = elements.size();

		if (numberOfObjects < 14000 && numberOfObjects > 0){
			webEngine.executeScript("document.setMarkersToNull()");
			webEngine.executeScript("document.clearCluster()");
			for (PropertyAssessment i : elements){
				float findLat = (float) i.getLocation().getLatitude();
				float findLng = (float) i.getLocation().getLongitude();
				//String findValue = i.getNeighborhood().getNeighborhoodName();
				webEngine.executeScript("document.multipleMarkers(" + findLat + ", " + findLng + ")");
			}
			webEngine.executeScript("document.markerCluster()");
		}

		else if (counter == 0){
			for (Boundaries element : Boundaries.boundariesList) {
				for (Location l : element.polygon) {
					float findLat = (float) l.getLatitude();
					float findLng = (float) l.getLongitude();
					webEngine.executeScript("document.addBoundary(" + findLat + ", " + findLng + ")");
				}
				webEngine.executeScript("document.addNeighbourhood()");
			}
			counter ++;
		}
/*		else{
			for (Boundaries matchNeighbourhood : Boundaries.boundariesList){
				if elements.
			}
		}*/
	}


	class MyBrowser extends BorderPane {
		public MyBrowser(){

			final URL urlGoogleMaps = getClass().getResource("googlemaps.html");
			webEngine.load(urlGoogleMaps.toExternalForm());
			setCenter(webView);
			webView.setPrefSize(5000, 5000);
		}
	}
}