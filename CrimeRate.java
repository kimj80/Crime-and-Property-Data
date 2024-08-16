package cmpt305;

public class CrimeRate {
	
	private String neighborhoodName;
	private String violation;
	private String year;
	private String quarter;
	private String month;
	private String occurrences;
	
	CrimeRate(String neighbourhoodDescription,	String violation, String year,
			String quarter,	String month, String occurrences){
		this.neighborhoodName = neighbourhoodDescription;
		this.violation = violation;
		this.year = year;
		this.quarter = quarter;
		this.month = month;
		this.occurrences = occurrences;
	}
	
	public String getNeighborhoodName() {
		return neighborhoodName;
	}
	public String getViolation() {
		return violation;
	}
	public String getYear() {
		return year;
	}
	public String getQuarter() {
		return quarter;
	}
	public String getMonth() {
		return month;
	}
	public String getOccurrences() {
		return occurrences;
	}
	public String toString() {
		return "Hood: " + neighborhoodName + "  Violation: " + violation + 
				year + " " + quarter + "  M:" + month + 
				"  Times: " + occurrences + "\n";
	}
	public CrimeRate clone() {
		return new CrimeRate(neighborhoodName,violation, year,
				quarter,month,occurrences);
	}
	
}
