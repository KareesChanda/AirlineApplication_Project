
public class FlightsPerAirline {

	private int airlineID;
	private String airlineName;
	private int totalFlights;
	
	public FlightsPerAirline(int id, String name, int total) {
		// TODO Auto-generated constructor stub
		this.airlineID = id;
		this.airlineName = name;
		this.totalFlights = total;
	}

	public int getAirlineID() {
		return airlineID;
	}

	public void setAirlineID(int flightID) {
		this.airlineID = flightID;
	}

	public String getAirlineName() {
		return airlineName;
	}

	public void setAirlineName(String airlineName) {
		this.airlineName = airlineName;
	}

	public int getTotalFlights() {
		return totalFlights;
	}

	public void setTotalFlights(int totalFlights) {
		this.totalFlights = totalFlights;
	}
	
	
}
