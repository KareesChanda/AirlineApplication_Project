import java.sql.*;


public class UserRequests {
	
	/**DONE: ================================== getUserAccount ==================================
	 * Get the data of a user used to validate account log in
	 */
	public ResultSet getUserAccount(Connection conn, String email, String pass) throws SQLException{
		ResultSet rs = null;
		PreparedStatement preparedStatement = null;

		String sql =  "SELECT * from User where email  = ? AND password = ?";
		preparedStatement = conn.prepareStatement(sql);
		preparedStatement.setString(1, email);
		preparedStatement.setString(2, pass);
		rs = preparedStatement.executeQuery();
		return rs;
	}//getUserAccount
	
	public ResultSet createUserAccount(Connection conn, String firstName, String lastName, 
								String email, String password, int age) throws SQLException{
		ResultSet rs = null;
		PreparedStatement preparedStatement = null;
		String sql =  "INSERT INTO User(firstName, lastName, email, password, age) " + 
				"VALUES(?, ?, ?, ?, ?)";
		
		preparedStatement= conn.prepareStatement(sql);
		preparedStatement.setString(1, firstName);
		preparedStatement.setString(2,lastName);
		preparedStatement.setString(3,email);
		preparedStatement.setString(4,password);
		preparedStatement.setInt(5, age);			
		preparedStatement.executeUpdate();
		
		return rs;
	}
	
	/**DONE: ================================== getPassengersByFlight ==================================
	 * Get the list of passengers boarding a specific flight given the flight number
	 */
	public ResultSet getPassengersByFlight(Connection conn, int flight_ID) throws SQLException{
		String sql = null;
		ResultSet rs = null;
		PreparedStatement preparedStatement = null;

		sql = "SELECT User.userID as passengerID, firstName, lastName, email, age "
				+ "FROM User, Booking " + 
					"WHERE Booking.flightID = ? AND "+
					"Booking.userID = User.userID "+ 
					"ORDER BY User.userID;";
		preparedStatement = conn.prepareStatement(sql);
		preparedStatement.setInt(1, flight_ID);
		rs = preparedStatement.executeQuery();
		return rs;
	}//getPassengersByFlight

	/**DONE: ================================== getEconomyPassengers ==================================
	 * Get the passengers boarding ECONOMY class in a flight
	 */
	public ResultSet getEconomyPassengers(Connection conn, int flight_ID) throws SQLException{
		String sql = null;
		ResultSet rs = null;
		PreparedStatement preparedStatement = null;
		
		sql = "SELECT User.userID as passengerID, firstName, lastName, email, age "+ 
				"FROM User LEFT OUTER JOIN Booking ON " + 
				"User.userID = Booking.userID " +
				"WHERE Booking.flightID = ? AND "+
				"Booking.class = 'Economy' "+ 
				"ORDER BY firstName;";
		preparedStatement = conn.prepareStatement(sql);
		preparedStatement.setInt(1, flight_ID);
		rs = preparedStatement.executeQuery();
		return rs;
	}//getEconomyPassengers

	/**DONE: ================================== getFirstPassengers ==================================
	 * Get the passengers boarding FIRST class in a flight
	 */
	public ResultSet getFirstPassengers(Connection conn, int flight_ID) throws SQLException{
		String sql = null;
		ResultSet rs = null;
		PreparedStatement preparedStatement = null;
		
		sql = "SELECT User.userID as passengerID, firstName, lastName, email, age "+ 
				"FROM User LEFT OUTER JOIN Booking ON " + 
				"User.userID = Booking.userID " +
				"WHERE Booking.flightID = ? AND "+
				"Booking.class = 'First' "+ 
				"ORDER BY firstName, lastName, age;";
		preparedStatement = conn.prepareStatement(sql);
		preparedStatement.setInt(1, flight_ID);
		rs = preparedStatement.executeQuery();
		return rs;
	}//getFirstPassengers	
	
	/**DONE: =================================== createFlight ==================================
	 * Create a flight with date, time, and route(depart, arrive, and airline)
	 */
	public int createFlight(Connection conn, String departDate, String departTime, 
				String arriveDate, String arriveTime, int routeID) throws SQLException{
		String sql = null;
		int rowAffected = 0;
		PreparedStatement preparedStatement = null;
		
		sql = "INSERT INTO flight (departureTime, departureDate, arrivalTime, arrivalDate, routeID) "+"VALUES(?, ?, ?, ?, ?);";

	    preparedStatement = conn.prepareStatement(sql);
	    preparedStatement.setString(1, departTime);
	    preparedStatement.setString(2, departDate);
	    preparedStatement.setString(3, arriveTime);
	    preparedStatement.setString(4, arriveDate);
	    preparedStatement.setInt(5, routeID);
	    rowAffected = preparedStatement.executeUpdate();

	    return rowAffected;		
	}//createFlight
	
	/**DONE: ================================== getFlightsByAirline ==================================
	 * Given the airline, list all the flights
	 */
	public ResultSet getFlightsByAirline(Connection conn, String airlineName) throws SQLException{
		String sql = null;
		ResultSet rs = null;
		PreparedStatement preparedStatement = null;

		sql = 	"SELECT flightID as flightNumber, departureDate, departureTime, r.departureAirport, arrivalDate, arrivalTime, r. arrivalAirport, r.airlineName " +
				"FROM flight, (SELECT routeID, airline.name as airlineName, a1.name as departureAirport, a2.name as arrivalAirport " +
						  "FROM route, airline, airport as a1, airport as a2 " +
						  "WHERE route.departureAirportID = a1.airportID " +
						  "AND route.arrivalAirportID = a2.airportID " +
						  "AND route.airlineID = airline.airlineID) r " +
			    "WHERE flight.routeID = r.routeID " +
			    "AND r.airlineName LIKE ? ORDER BY departureDate;";
		
		preparedStatement = conn.prepareStatement(sql);
		preparedStatement.setString(1, airlineName);
		rs = preparedStatement.executeQuery();
		return rs;
	}//getFlightsByAirline

	/**DONE: ================================= getFlightsByRouteDate ==================================
	 * Search for existing flights based on given departure city, arrival city, and departure date
	 */
	public ResultSet getFlightsByRouteDate(Connection conn, String origin, 
									String destination, String date) throws SQLException{
		PreparedStatement preparedStatement = null;

		String sql = "SELECT flightID as flightNumber, departureTime, r.departureAirport, arrivalTime, r. arrivalAirport, r.airlineName "+
				"FROM flight, (SELECT routeID, airline.name as airlineName, "+
							"a1.name as departureAirport, a2.name as arrivalAirport "+
						"FROM route, airline, airport as a1, airport as a2 "+
						"WHERE a1.city = ? "+
						"AND a2.city = ? "+
						"AND route.departureAirportID = a1.airportID "+
						"AND route.arrivalAirportID = a2.airportID "+
						"AND route.airlineID = airline.airlineID) r " +
				"WHERE flight.routeID = r.routeID "+
				"AND flight.departureDate = ?;";
		preparedStatement = conn.prepareStatement(sql);
		preparedStatement.setString(1, origin);
		preparedStatement.setString(2, destination);
		preparedStatement.setString(3, date);
		ResultSet rs = preparedStatement.executeQuery();
		return rs;
	}//getFlightsByRouteDate

	/**DONE: ================================== getFlightsByFlightID ====================================
	 * Return a list of flights that match the given flightID number
	 */
	public ResultSet getFlightsByFlightID(Connection conn, int flightID) throws SQLException{
		String sql = null;
		ResultSet rs = null;
		PreparedStatement preparedStatement = null;

		sql = 	"SELECT flightID as flightNumber, departureTime, r.departureAirport, arrivalTime, r. arrivalAirport, r.airlineName " +
				"FROM flight, (SELECT routeID, airline.name as airlineName, a1.name as departureAirport, a2.name as arrivalAirport " +
						  "FROM route, airline, airport as a1, airport as a2 " +
						  "WHERE route.departureAirportID = a1.airportID " +
						  "AND route.arrivalAirportID = a2.airportID " +
						  "AND route.airlineID = airline.airlineID) r " +
			    "WHERE flight.routeID = r.routeID " +
			    "AND flight.flightID = ?;";
		preparedStatement = conn.prepareStatement(sql);
		preparedStatement.setString(1, Integer.toString(flightID));
		rs = preparedStatement.executeQuery();
		return rs;
	}//getFlightsByFlightID

	/**DONE: ================================== getTicketNumber =======================================
	 * After the user reserves a flight, find the ticket number and show it with given flightID and userID
	 */
	public ResultSet getTicketNumber(Connection conn, int flight_ID, int user_ID) throws SQLException{
		String sql = null;
		ResultSet rs = null;
		PreparedStatement preparedStatement = null;

		sql = "SELECT ticketID FROM Booking WHERE flightID = ? AND userID = ?;";
		preparedStatement = conn.prepareStatement(sql);
		preparedStatement.setInt(1, flight_ID);
		preparedStatement.setInt(2, user_ID);
		rs = preparedStatement.executeQuery();
		return rs;
	}//getTicketNumber

	/**DONE: ================================== getBookingByUser =======================================
	 * Search for all the flight reservations made by a specific user with given userID
	 */
	public ResultSet getBookingByUser(Connection conn, int user_ID) throws SQLException{
		String sql = null;
		ResultSet rs = null;
		PreparedStatement preparedStatement = null;

		sql = "SELECT * FROM Booking WHERE userID = ?";
		preparedStatement = conn.prepareStatement(sql);
		preparedStatement.setInt(1, user_ID);
		rs = preparedStatement.executeQuery();
		return rs;
	}//getBookingByUser
	
	/**DONE: ================================== getAirportByCityCountry =================================
	 * Get the airports based on the city or country. It doesn't require both params
	 */
	public ResultSet getAirportByCityCountry(Connection conn, String city, String country) throws SQLException{
		String sql = null;
		ResultSet rs = null;
		PreparedStatement preparedStatement = null;

		sql = "SELECT * FROM Airport "
				+ "WHERE city LIKE ? "
				+ "AND country LIKE ?;";
		preparedStatement = conn.prepareStatement(sql);
		preparedStatement.setString(1, city);
		preparedStatement.setString(2, country);
		rs = preparedStatement.executeQuery();
		return rs;
	}//getAirportByCityCountry
	
	/**DONE: =================================== getRouteByAirline =================================
	 * Get the route given departing airport ID and arriving airport ID
	 */
	public ResultSet getRouteByAirline(Connection conn, int departID, int arriveID) throws SQLException{
		String sql = null;
		ResultSet rs = null;
		PreparedStatement preparedStatement = null;
		sql = 	"SELECT route.routeID, airline.name "
				+ "FROM Route, Airline "
				+ "WHERE route.airlineID = airline.airlineID "
				+ "AND route.departureAirportID = ? "
				+ "AND route.arrivalAirportID = ?;";
		preparedStatement = conn.prepareStatement(sql);
		preparedStatement.setInt(1, departID);
		preparedStatement.setInt(2, arriveID);
		rs = preparedStatement.executeQuery();
		return rs;
	}//getRouteByAirline
	
	/**DONE: =================================== getBookingOrderedByUserID =================================
	 * Get all the bookings for a flight
	 */
	public ResultSet getBookingOrderedByUserID(Connection conn, int flightID) throws SQLException{
		String sql = null;
		ResultSet rs = null;
		PreparedStatement preparedStatement = null;

		sql = "SELECT User.userID, User.firstname, User.lastname, Booking.flightID, Booking.ticketID " +
			"FROM User " +
			"LEFT OUTER JOIN Booking " +
			"ON User.userID = Booking.userID "+ 
			"WHERE Booking.flightID = ? "+
			"ORDER BY User.userID;";
	    preparedStatement = conn.prepareStatement(sql);
	    preparedStatement.setInt(1, flightID);
	    rs = preparedStatement.executeQuery();
		return rs;
	}//getBookingOrderedByUserID
	

	/**DONE: =================================== getTotalFlightsPerAirline =================================
	 * For each airline, find the total number of flights
	 */
	public ResultSet getTotalFlightsPerAirline(Connection conn, String airlineName) throws SQLException{
		String sql = null;
		ResultSet rs = null;
		PreparedStatement preparedStatement = null;

		sql = "SELECT Airline.airlineID, Airline.name, count(flightID) AS totalFlights "
				+ "FROM Airline, Route, Flight "
				+ "WHERE Flight.routeID = Route.routeID  "
				+ "AND Route.airlineID = Airline.airlineID "
				+ "AND airline.name LIKE ? "
				+ "GROUP BY Airline.airlineID;";
		
	    preparedStatement = conn.prepareStatement(sql);
	    preparedStatement.setString(1, airlineName);
	    rs = preparedStatement.executeQuery();
		return rs;
	}//getTotalFlightsPerAirline

	
	/**DONE: =================================== getMostExperiencedPilots =================================
	 * Find the most experienced pilots
	 */
	public ResultSet getMostExperiencedPilots(Connection conn) throws SQLException{
		String sql = null;
		ResultSet rs = null;
		Statement statement = conn.createStatement();

		sql = 	"SELECT * FROM Pilot pilot1 "+ 
				"WHERE 0 = "+ 
					"(SELECT COUNT(DISTINCT(pilot2.experience)) "+ 
					"FROM Pilot pilot2 "+ 
					"WHERE pilot2.experience > pilot1.experience);";
		rs = statement.executeQuery(sql);
		return rs;
	}//getMostExperiencedPilots
	
	/**DONE: =================================== getLeastExperiencedPilots =================================
	 * Find the least experienced pilots
	 */
	public ResultSet getLeastExperiencedPilots(Connection conn) throws SQLException{
		String sql = null;
		ResultSet rs = null;
		Statement statement = conn.createStatement();

		sql = 	"SELECT * FROM Pilot pilot1 "+ 
				"WHERE 0 = "+ 
					"(SELECT COUNT(DISTINCT(pilot2.experience)) "+ 
					"FROM Pilot pilot2 "+ 
					"WHERE pilot2.experience < pilot1.experience);";

		rs = statement.executeQuery(sql);
		return rs;
	}//getLeastExperiencedPilots	
	
	/**DONE: =================================== archiveFlights =================================
	 * Archive flights with given date
	 */
	public ResultSet archiveFlights(Connection conn, String date) throws SQLException{
		CallableStatement cs = conn.prepareCall("{CALL ArchiveFlights(?)}");
		String timestamp = date + " 00:00:00";
		cs.setString(1, timestamp);
		ResultSet rs = cs.executeQuery();
		return rs;
	}//archiveFlights
}//UserRequests
