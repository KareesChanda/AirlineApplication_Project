
public class Passenger {
	private int passengerID;
	private String firstName;
	private String lastName;
	private String email;
	private int age;
	
	public Passenger(int id, String first, String last, String email, int age) {
		this.passengerID = id;
		this.firstName = first;
		this.lastName = last;
		this.email = email;
		this.age = age;
	}

	public int getPassengerID() {
		return passengerID;
	}

	public void setPassengerID(int passengerID) {
		this.passengerID = passengerID;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
}
