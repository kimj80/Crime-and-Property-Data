package cmpt305;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class PropertyAssessments {
	private ArrayList<PropertyAssessment> assessments = null;

	PropertyAssessments(String filename) throws IOException{
		List<String> data = getData(filename);
		
		assessments = new ArrayList<PropertyAssessment>();
		
		for (String i : data) {
			assessments.add(createPropertyAssessmentFromString(i));
		}
	}
	
	PropertyAssessments(List<PropertyAssessment> assessments) {
		this.assessments = (ArrayList<PropertyAssessment>) assessments;
	}
	
	private static List<String> getData(String filename) throws IOException {

		Scanner file = new Scanner(Paths.get(filename));
		ArrayList <String> lines = new ArrayList<String>();

		if (file.hasNextLine()) {
			file.nextLine(); // skip first line
		}

		while (file.hasNextLine()) {
			lines.add(file.nextLine());
		}

		file.close();
		
		return lines;
		
		
	}

	private static PropertyAssessment createPropertyAssessmentFromString(String item) {
		// Each string is laid out as follows:
		// index:  0		1 		2				3			4				5					6				7			8		9		10		11
		// Account Number, Suite, House Number, Street Name, Assessed Value, Assessment Class, Neighborhood ID, Neighborhood, Ward, Garage, Latitude, Longitude
		String[] thing;
		thing = item.split(",");
		for(int i=0;i<12;i++) {
			thing[i] = thing[i].replaceAll("\"", "");
		}
		int accountNumber = (thing[0] != "" ? Integer.parseInt(thing[0]) : null);
		
		String suite = thing[1];
		
		int houseNumber = thing[2].length() != 0 ? Integer.parseInt(thing[2]) : -1;
		String streetName = thing[3];
		int value = thing[4] != "" ? Integer.parseInt(thing[4]) : null;
		String assessmentClass = thing[5];
		int neighborhoodID = thing[6].length() != 0 ? Integer.parseInt(thing[6]) : -1;
		String neighborhoodName = thing[7];
		
		String[] wardParse = thing[8].length() != 0 ? thing[8].split(" ") : null;
		int ward = -1;
		if (wardParse !=null) {
			ward = wardParse[1] != "" ? Integer.parseInt(wardParse[1]) : -1;
		}
		boolean garage = (thing[9] != "Y" ? true : false);
		double latitude = thing[10] != "" ? Double.parseDouble(thing[10]) : -1;
		double longitude = thing[11] != "" ? Double.parseDouble(thing[11]) : -1;
		
		Location loc = new Location(latitude,longitude);
		Neighborhood neigh = new Neighborhood(neighborhoodID, neighborhoodName, ward);
		Address addy =  new Address(suite, houseNumber, streetName);

		return new PropertyAssessment(accountNumber, addy, neigh, garage, loc, value, assessmentClass);
		

	}


	public int getMedian() {
		if (assessments == null) {
			return 0;
		}
		ArrayList<Integer> sortedAssessments =  new ArrayList<Integer>();
		for (PropertyAssessment i : assessments) {
			sortedAssessments.add(i.getValue());
		}
		Collections.sort(sortedAssessments);
		if (sortedAssessments.size() % 2 ==  1) { // if size is odd, return the middle value
		
			int middle = (sortedAssessments.size() / 2);
			return sortedAssessments.get(middle);
			
		}
		else {
			int mid1 = sortedAssessments.size()/2;
			int mid2 = mid1 + 1;
			return (sortedAssessments.get(mid1) + sortedAssessments.get(mid2)) / 2;
		}
		
	}


	public long getStandardDeviation() {
		double numerator = 0;
		double mean = (double)getMean();
		if (assessments != null) {
			for (PropertyAssessment i : assessments) {
				numerator += Math.pow((double)i.getValue() - mean, (double)(2));
				
			}

			double SD = Math.sqrt((numerator / (double)getNumberOfProperties()));
					
			return Math.round(SD);
		}
		return 0;
	}


	public  int getRange() {
		// returns the difference between the largest and smallest values
		return getMax() - getMin();
	}



	public  int getMean() {
		
		long accu = 0;
		
		if (assessments != null) {
			for (int i  = 0; i < assessments.size(); i++) {
				accu += assessments.get(i).getValue();
				
			}
			
			return Math.round(accu / getNumberOfProperties());
		}
		
		return 0;
	}


	public  int getMax() {
		int maxValue = 0;
		if (assessments != null) {
			for (PropertyAssessment i : assessments) {
				if (i.getValue() > maxValue) {
					maxValue = i.getValue();
				}
			}
		}
		return maxValue;
	}

	public  int getMin() {
		int minValue = 2147483647; // arbitrarily large
		if (assessments != null){
			for (PropertyAssessment i: assessments) {
				if (i.getValue() < minValue) {
					minValue = i.getValue();
				}
			}
		}
		return minValue;
	}

	public int getNumberOfProperties() {
		if (assessments != null) {
			return assessments.size();
		}
		return 0;
	}
	
	public PropertyAssessment getAccountNumber(int number) {
		for (PropertyAssessment i : assessments) {
			if (i.getAccountNumber() == number) {
				return i;
			}
		}
		return null;
	}

	public PropertyAssessments getNeighborhood(String neighborhood){
		neighborhood = neighborhood.toUpperCase();
		ArrayList<PropertyAssessment> output = new ArrayList<PropertyAssessment>();
		for (PropertyAssessment i : assessments) {
			if (i.getNeighborhood().getNeighborhoodName().equalsIgnoreCase(neighborhood)){
				output.add(i);
			}
		}

		return new PropertyAssessments(output);
	}

	public PropertyAssessments getAssessmentClass(String assessmentClass) {
		assessmentClass = assessmentClass.toUpperCase();
		ArrayList<PropertyAssessment> output = new ArrayList<PropertyAssessment>();
		for (PropertyAssessment i : assessments) {
			if (assessmentClass.equals(i.getAssessmentClass())){
				output.add(i);
			}
		}
		return new PropertyAssessments(output);
	}
	
	public PropertyAssessments getAddressContaining(String address) {
		address = address.toUpperCase();
		ArrayList<PropertyAssessment> output = new ArrayList<PropertyAssessment>();
		for (PropertyAssessment i : assessments) {
			if (i.getAddress().toString().contains(address)){
				output.add(i);
			}
		}
		
		return new PropertyAssessments(output);
	}
	
	public List<PropertyAssessment> getAssessments() {
		return assessments;
	}

	public PropertyAssessments getPropertiesInRange(int min, int max) {
		ArrayList<PropertyAssessment> output =  new ArrayList<PropertyAssessment>();
		if (assessments == null) {
			return null;
		}
		for (PropertyAssessment i : assessments) {
			int val = i.getValue();
			if (val >= min && val <= max ) {
				output.add(i);
			}
		}
		return new PropertyAssessments(output);
	}
	
	public String[] getNeighborHoodList() {
		List<String> output = new ArrayList<String>();
		for (PropertyAssessment i: assessments) {
			if (!(output.contains(i.getNeighborhood().getNeighborhoodName()))) {
				output.add(i.getNeighborhood().getNeighborhoodName());
			}
		}
		return (String[]) (output.toArray(new String[output.size()]));
	}
	
	public static ArrayList<PropertyAssessment> getAPIinfo() throws IOException {
		ArrayList<PropertyAssessment> list = new ArrayList<PropertyAssessment>();
		int returnCount = 50000;
		int offset = 0;
		while(returnCount >= 50000) {
			returnCount = singlePARequest(offset, list);
			offset += 50000;
		}
		return list;	    
	}


	private static int singlePARequest(int offset, ArrayList<PropertyAssessment> list) throws IOException {
		URL url = new URL("https://data.edmonton.ca/resource/q7d6-ambg.csv?$limit=50000&$offset=" + offset);
		int returnCount = 0;
	    String readLine = null;
	    HttpURLConnection conection = (HttpURLConnection) url.openConnection();
	    conection.setRequestMethod("GET");
	    int responseCode = conection.getResponseCode();
	    if (responseCode == HttpURLConnection.HTTP_OK) {
	        BufferedReader in = new BufferedReader(
	            new InputStreamReader(conection.getInputStream()));
	        readLine = in.readLine(); // Clear first row of headers
	        while ((readLine = in.readLine()) != null) {
	        	list.add(createPropertyAssessmentFromString(readLine.toString()));
	            returnCount++;
	        } in .close();
	        return returnCount;
	    } else {
	        System.out.println("GET NOT WORKED");
	        return 0;
	    }
	}
	public void setPropertyAssessments(List<PropertyAssessment> list) {
		this.assessments = (ArrayList<PropertyAssessment>) list;
	}
	
	public List<String> getAccountNumberList() {
		ArrayList<String> numbers =  new ArrayList<String>();
		for (PropertyAssessment i: assessments) {
			numbers.add(Integer.toString(i.getAccountNumber()));
		}
		return numbers;
	}
	
	
}







