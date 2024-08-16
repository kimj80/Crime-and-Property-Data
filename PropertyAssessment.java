package cmpt305;

public class PropertyAssessment {
	private final int accountNumber;
	private final Address address;
	private final Neighborhood neighborhood;
	private final boolean garage;
	private final Location location;
	private final int value;
	private final String assessmentClass;
	
	PropertyAssessment(int accountNumber, Address address, Neighborhood neighborhood, boolean garage, Location location, int value, String assessmentClass){
		this.accountNumber = accountNumber;
		this.address = address;
		this.neighborhood = neighborhood;
		this.garage = garage;
		this.location = location;
		this.value = value;
		this.assessmentClass = assessmentClass.toUpperCase();
	}

	public int compare(PropertyAssessment o) {
		return Integer.compare(value, o.value);
	}

	public int getAccountNumber() {
		return accountNumber;
	}

	public String getAddressString() {
		return address.toString();
	}

	public Address getAddress() {
		return address;
	}

	public String getAssessmentClass() {
		return assessmentClass;
	}

	public Neighborhood getNeighborhood() {
		return neighborhood;
	}

	public boolean hasGarage() {
		return garage;
	}

	public Location getLocation() {
		return location;
	}

	public int getValue() {
		return value;
	}

	public int hashCode() {
		return (((((Integer.hashCode(accountNumber) * 31 + address.hashCode()) *31 + neighborhood.hashCode() ) * 31 
				+ Boolean.hashCode(garage)) * 31 + location.hashCode()) * 31 + Integer.hashCode(value)) *31 + assessmentClass.hashCode();
	}

	public String toString() {
		return "TODO: write toString for PropertyAssessment";
	}

	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof PropertyAssessment)) {
			return false;
		}
		PropertyAssessment other = (PropertyAssessment)o;
		return 
				(accountNumber == other.accountNumber)                &&
				(address.equals(other.getAddress()))                  &&
				(neighborhood.equals(other.getNeighborhood()))        &&
				(garage == other.hasGarage())                         &&
				(location.equals(other.getLocation()))                &&
				(value == other.getValue())                           &&
				(assessmentClass.equals(other.getAssessmentClass()));
		
	}
	
}
