
public class Pilot {
	private int pilotID;
	private String firstName;
	private String lastName;
	private int experience;
	
	public Pilot(int pilotID, String first, String last, int experience) {
		this.pilotID = pilotID;
		this.firstName = first;
		this.lastName = last;
		this.experience = experience;
	}

	public int getPilotID() {
		return pilotID;
	}

	public void setPilotID(int pilotID) {
		this.pilotID = pilotID;
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

	public int getExperience() {
		return experience;
	}

	public void setExperience(int experience) {
		this.experience = experience;
	}
	
	
}
