
public class Route {
	private int routeID;
	private String airline;
	
	public Route(int id, String airline) {
		// TODO Auto-generated constructor stub
		this.routeID = id;
		this.airline = airline;
	}

	public int getRouteID() {
		return routeID;
	}

	public void setRouteID(int routeID) {
		this.routeID = routeID;
	}

	public String getAirline() {
		return airline;
	}

	public void setAirline(String airline) {
		this.airline = airline;
	}
	
	
}
