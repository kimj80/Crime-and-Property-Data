package cmpt305;

public class Neighborhood {
	private final int neighborhoodID;
	private final String neighborhoodName;
	private final int ward;
	
	Neighborhood(int neighborhoodID, String neighborhoodName, int ward){
		this.neighborhoodID = neighborhoodID;
		this.neighborhoodName = neighborhoodName.toUpperCase();
		this.ward = ward;
	}

	int getNeighborhoodID() {
		return neighborhoodID;
	}

	String getNeighborhoodName() {
		return neighborhoodName;
	}

	int getWard() {
		return ward;
	}
	
	public int hashCode() {
		return (Integer.hashCode(neighborhoodID) * 31 + neighborhoodName.hashCode()) * 31 + Integer.hashCode(ward); 
	}

	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof Neighborhood)) {
			return false;
		}
		Neighborhood other = (Neighborhood)o;
		return 
				(other.getNeighborhoodID() == neighborhoodID)
				&& (neighborhoodName.contentEquals(other.getNeighborhoodName()))
				&& (other.getWard() == ward);
		
	}
	public String toString() {
		return "ID: " + Integer.toString(neighborhoodID) + " Name: " + neighborhoodName + " Ward: " + Integer.toString(ward);
	}
}
