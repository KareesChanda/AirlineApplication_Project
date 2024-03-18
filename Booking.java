
public class Booking {
	private int userID;
	private String firstName;
	private String lastName;
	private int flightID;
	private int ticketID;
	
	public Booking(int userID, String first, String last, int flightID, int ticketID) {
		// TODO Auto-generated constructor stub
		this.userID = userID;
		this.firstName = first;
		this.lastName = last;
		this.flightID = flightID;
		this.ticketID = ticketID;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public int getFlightID() {
		return flightID;
	}

	public void setFlightID(int flightID) {
		this.flightID = flightID;
	}

	public int getTicketID() {
		return ticketID;
	}

	public void setTicketID(int ticketID) {
		this.ticketID = ticketID;
	}
	
	
}
