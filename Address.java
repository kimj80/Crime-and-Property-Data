package cmpt305;

public class Address {
	private final String suite;
	private final int houseNumber;
	private final String streetName;
	
	Address(String suite, int houseNumber, String streetName){
		this.suite = suite.toUpperCase();
		this.houseNumber = houseNumber;
		this.streetName = streetName.toUpperCase();
	}
	
	String getSuite() {
		return suite;
	}
	
	int getHouseNumber() {
		return houseNumber;
	}
	
	String getStreetName() {
		return streetName;
	}
	
	public int hashCode() {
		return (suite.hashCode() *31 + Integer.hashCode(houseNumber)) * 31 + streetName.hashCode();
	}
	
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof Address)) {
			return false;
		}
		Address other = (Address)o;
		return (other.getSuite().equals(suite)) && (other.getHouseNumber() == houseNumber)
				&& (other.getStreetName() == streetName);
	}
	
	public String toString() {
		return (suite.length() > 0 ? suite + " " : "") + Integer.toString(houseNumber) + (streetName.length() > 0 ? " " + streetName + " " : "");
	}
}
