package cmpt305;

public class Location {
	private final double latitude;
	private final double longitude;
	
	Location(double latitude, double longitude){
		
		this.latitude = rangeCheck(latitude, 90, "latitude");
		this.longitude = rangeCheck(longitude, 180, "longitude");
	}
	
	double getLatitude() {
		return latitude;
	}
	
	double getLongitude() {
		return longitude;
	}
	
	public String toString() {
		return "(" + Double.toString(latitude) + ") , (" + Double.toString(longitude) + ")";
	}
	
	private static double rangeCheck(double val, int max, String arg) {
		if (val < (double)(max*-1) || val > (double)max)
			throw new IllegalArgumentException(arg + ": " + val);
		return val;
	}
	
	
	public int hashCode() {
		return Double.hashCode(latitude) * 31 + Double.hashCode(longitude);
	}
	
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof Location)) {
			return false;
		}
		Location other = (Location)o;
		return other.latitude == latitude && other.longitude == longitude;
		
	}

}
