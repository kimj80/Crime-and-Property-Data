package cmpt305;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.Chart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;

public class ChartFactory {
	// this is a tightly coupled design with ChartViewManager. nothing here is reusable;
	
	
	static Chart createStackedBarChart(PropertyAssessments assessments, CrimeRates crime) {
		
		CategoryAxis yAxis = new CategoryAxis();
		yAxis.setCategories(FXCollections.<String>observableArrayList(Arrays.asList(assessments.getNeighborHoodList())));
		NumberAxis xAxis = new NumberAxis();
		StackedBarChart<Number, String> chart = new StackedBarChart<Number, String>(xAxis, yAxis);
		chart.setTitle("Number of Violations by type");
		
		ArrayList<XYChart.Series<Number, String>> data = new ArrayList<Series<Number, String>>();
		
		return (Chart) chart;
	}
	
	static Chart createLineChart(PropertyAssessments assessments, CrimeRates crime) {
		return null;
	}
	
	static Chart createSingleBarChartValues(List<PropertyAssessments> assessmentssss) {
		CategoryAxis yAxis = new CategoryAxis();
		NumberAxis xAxis = new NumberAxis();
		
		ArrayList<XYChart.Series<Number, String>> data = new ArrayList<Series<Number, String>>();
		for (PropertyAssessments i : assessmentssss ) {
			XYChart.Series<Number, String> newSeries = new XYChart.Series<Number, String>();
			String neighborhoodName = i.getNeighborHoodList()[0];
			newSeries.getData().add(new XYChart.Data(i.getMedian(), neighborhoodName));
		}
		
		BarChart<Number, String> chart = new BarChart<Number, String>(xAxis, yAxis);
		chart.getData().addAll(data);
		return (Chart) chart;
	}
}
