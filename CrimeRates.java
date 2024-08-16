package cmpt305;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class CrimeRates {


	private ArrayList<CrimeRate> masterList;
	
	CrimeRates(List<CrimeRate> list) {
		masterList = new ArrayList<CrimeRate>();
		Iterator<CrimeRate> data = list.iterator();
        while(data.hasNext()){
            masterList.add((CrimeRate) data.next().clone());
        }
	}
	
	static CrimeRates crimeRatesAPI() throws Exception {
		ArrayList<CrimeRate> list = new ArrayList<CrimeRate>();
		int returnCount = 50000;
		int offset = 0;
		while(returnCount >= 50000) {
			returnCount = singleCRRequest(offset, list);
			offset += 50000;
		}
		return new CrimeRates((List<CrimeRate>)list);
	}
	
	private static int singleCRRequest(int offset, ArrayList<CrimeRate> list) 
																throws IOException {
		URL url = new URL("https://dashboard.edmonton.ca/resource/xthe-mnvi." +
	    		"csv?$limit=50000&$offset=" + offset);
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
	        	list.add(convertCrimeRatesString(readLine.toString()));
	            returnCount++;
	        } in .close();
	        return returnCount;
	    } else {
	        System.out.println("GET NOT WORKED");
	        return 0;
	    }
	}
	
	// Used to convert the crimrates string returned from the api call into an object
	private static CrimeRate convertCrimeRatesString(String line) {
		// Split string on commas
		String[] parts = line.split(",");
		// Remove all quotes
		for(int i=0;i<6;i++) {
			parts[i] = parts[i].replaceAll("\"", "");
		}
		CrimeRate cr = new CrimeRate(parts[0], parts[1], parts[2], parts[3],
				parts[4], parts[5]);
		return cr;
	}
	
	
	
	// Returns master list of crime rates (No search filters)
	public ArrayList<CrimeRate> getCrimeRatesList() throws CloneNotSupportedException {
		ArrayList<CrimeRate> newList = new ArrayList<CrimeRate>();
		Iterator<CrimeRate> list = masterList.iterator();
        while(list.hasNext()){
            newList.add((CrimeRate) list.next().clone());
        }
        return newList;
	}
	
	// Update search list with results and return a copy of list
	public CrimeRates getCrimeRatesByYears(int upper, int lower) throws Exception {
		ArrayList<CrimeRate> newList = new ArrayList<CrimeRate>();
		for(int i=0;i<masterList.size();i++) {
			if(Integer.parseInt(masterList.get(i).getYear())>= upper && Integer.parseInt(masterList.get(i).getYear())<= lower) {
				newList.add(masterList.get(i));
			}
		}
		return new CrimeRates((List<CrimeRate>) newList);
	}
	
	// Filters search list by violation and returns copy of string.
	public CrimeRates getCrimeRatesByViolations(String violation) throws Exception {
		ArrayList<CrimeRate> newList = new ArrayList<CrimeRate>();
		for(int i=0;i<masterList.size();i++) {
			if(masterList.get(i).getViolation().contentEquals(violation)) {
				newList.add(masterList.get(i));
			}
		}
		return new CrimeRates((List<CrimeRate>)newList);
	}
	
	// Total occurences of a ceratin violation
	public int violationCount(String v) {
		int count = 0;
		for(int i=0;i<masterList.size();i++) {
			if(masterList.get(i).getViolation().contentEquals(v)) {
				count += Integer.parseInt(masterList.get(i).getOccurrences());
			}
		}
		return count;
	}
	
	// String list of types of violations
	public String[] allViolations() {
		HashSet<String> hash = new HashSet<String>();
		for(int i=0;i<masterList.size();i++) {
			hash.add(masterList.get(i).getViolation());
		}
		return hash.toArray(new String[hash.size()]);
	}
	
	
}





