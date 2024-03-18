
public class Airport {
	private int airportID;
	private String name;
	private String city;
	private String country;
	private String code;
	
	public Airport(int id, String name, String city, String country, String code) {
		// TODO Auto-generated constructor stub
		this.airportID = id;
		this.name = name;
		this.city = city;
		this.country = country;
		this.code = code;
	}
	
	public int getAirportID() {
		return airportID;
	}
	public void setAirportID(int airportID) {
		this.airportID = airportID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
}
