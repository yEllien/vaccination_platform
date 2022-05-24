package Controller;

public class MedicalCenter {
	String id;
	String name;
	String vaccine;
	
	public MedicalCenter (String id, String name, String vaccine) {
		this.id = id;
		this.name = name;
		this.vaccine = vaccine;
	}
	
	public String toString () {
		return name+", "+vaccine;
	}
	
	public String getID () {
		return id;
	}
	
	public String getName () {
		return name;
	}
	
	public String getVaccine () {
		return vaccine;
	}
}
