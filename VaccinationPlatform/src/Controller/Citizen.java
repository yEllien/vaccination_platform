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
	
	String[] array;
	
	Appointment[] appointments;
	
	public Citizen (String SSN) throws SQLException {
		this.SSN = SSN;
		loadData();
	}
	
	public void loadData () throws SQLException {
		//TODO request personal info from database
		//e.g.
    	db database = new db();
    	database.init();
    	/*
		String [] info = new String [] {
				"Dwight",
				"Schrute",
				"12345678901",
				"123456789",
				"18-01-1978",
				"male",
				"6987948735",
				"dschrute@schrutefarms.com",
				"12345"
		};
		
		firstName 	= info[0];
		lastName 	= info[1];
		SSN 		= info[2];
		TIN 		= info[3];
		birthDate	= info[4];
		gender		= info[5];
		email		= info[6];
		postalCode	= info[7];
		toArray();
		*/
    	
    	String[] info = database.getCitizenName(this.SSN);
		firstName 	= info[0];
		lastName 	= info[1];

		birthDate	= database.getCitizenBirthDate(SSN);
		gender		= database.getCitizenGender(SSN);
		
		info = database.getCitizenCommunicationInfo(SSN);
		email		= info[0];
		postalCode	= info[2];
		toArray();
		
		//TODO request appointment info from database
		////////////////////////////////////////////////////////
		// db returns: ssn, dose number, appointment date, appointment time, hospital name
		
		/*
		ArrayList<String[]> apps = new ArrayList<String[]> ();
		String[] app = new String[] {
				"12345678901", 	//SSN
				"123", 			//medical center id
				"Vaccine name",
				"1",			//dose number
				"Medical Center Name",
				"09-01-2022", "10:00-12:00",
				"1"
		};
		apps.add(app);
		app = new String[] {
				"12345678901", //SSN
				"123",
				"Vaccine name",
				"2",
				"Medical Center Name",
				"20-02-2022", "08:00-10:00",
				"0"
		};
		apps.add(app);
		*/
		////////////////////////////////////////////////////////
		
		ArrayList<String[]> apps = database.GetBookedAppointments(SSN);
		
		appointments = new Appointment[apps.size()];
		
		for (String[] a : apps) {
			appointments[Integer.parseInt(a[3])-1] = new Appointment(
						a[0],
						a[1],
						a[1], //TODO get medical center name from id
						a[2],
						Integer.parseInt(a[3]),
						a[5],
						a[6],
						Integer.parseInt(a[7])
					);
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
				TIN,
				birthDate,
				gender,
				phoneNumber,
				email,
				postalCode
		};
	}
}
