package Controller;

import java.sql.SQLException;
import java.util.ArrayList;

public class Citizen {

	String firstName;
	String lastName;
	String SSN;
	String TIN;
	String birthDate;
	String gender;
	String phoneNumber;
	String email;
	String postalCode;
	String vaccinationState;
	
	String[] array;
	
	Appointment[] appointments;
	
	public Citizen (String SSN) throws SQLException {
		this.SSN = SSN;
		loadData();
	}
	
	public void loadData () throws SQLException {

    	db database = new db();
    	database.init();
    	
    	String[] info = database.getCitizenName(this.SSN);
		firstName 	= info[0];
		lastName 	= info[1];

		birthDate	= database.getCitizenBirthDate(SSN);
		gender		= database.getCitizenGender(SSN);
		
		info = database.getCitizenCommunicationInfo(SSN);
		email		= info[0];
		phoneNumber = info[1];
		postalCode	= info[2];
		
		vaccinationState = database.getVaccinationState(SSN);
		toArray();
		
		ArrayList<String[]> apps = database.GetBookedAppointments(SSN);
		if (apps == null) System.out.println("apps is null");
		if (apps.size() != 0) { 
		appointments = new Appointment[apps.size()-1];
				
	     for (int i = 1; i < apps.size();i++){ 		      
	    	  String[] a = apps.get(i);
	    	  
	    	 appointments[Integer.parseInt(a[3])-1]= new Appointment(
						a[0],	//ssn
						a[1],	//medical center id
						a[4],	//medical center name
						a[2], //vaccine name //TODO get medical center name from id
						Integer.parseInt(a[3]),	//dose number
						a[5],	//appointment date
						a[6],	//appointment time
						Integer.parseInt(a[7])
					); 
	    	 
	    	 Appointment tmp = appointments[Integer.parseInt(a[3])-1];
	    	 
	      } 

		}
		else 
		{
			appointments = new Appointment[0];
		}
		database.con.close();
	}
	
	public String getSSN () {
		return SSN;
	}
	
	public String getTIN () {
		return TIN;
	}
	
	public String getFirstName () {
		return firstName;
	}
	
	public String getLastName () {
		return lastName;
	}
	
	public String getBirthDate () {
		return birthDate;
	}
	
	public String getGender () {
		return gender;
	}
	
	public String getPhoneNumber () {
		return phoneNumber;
	}
	
	public String getEmail () {
		return email;
	}
	
	public String getPostalCode () {
		return postalCode;
	}
	
	public String[] getArray () {
		return array;
	}
	
	public Appointment[] getAppointments () {
		return appointments;
	}
	
	private void toArray () {
		array = new String[]{
				firstName,
				lastName,
				SSN,
				//TIN,
				birthDate,
				gender,
				phoneNumber,
				email,
				postalCode
		};
	}
}
