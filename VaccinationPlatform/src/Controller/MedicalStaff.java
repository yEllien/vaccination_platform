package Controller;

import java.sql.SQLException;

public class MedicalStaff {
	
	String medicalCenterID;
	String employeeID;
	String firstName;
	String lastName;
	
	MedicalCenter medicalCenter;
	
	String [] array;
	
	
	public MedicalStaff (
			String employeeID, 
			String medicalCenterID
			) {
		
		this.employeeID = employeeID;
		this.medicalCenterID = medicalCenterID;
		
		array = new String[] {medicalCenterID, employeeID, null, null};
	}
	
	public void loadData () throws SQLException {
		//TODO request info form database
		//e.g.
		/*
		System.out.println("test");
		String [] info = new String [] {
				"12",
				"34",
				"Lawrence",
				"Kutner"				
		};
		
		medicalCenterID = info[0];
		employeeID 		= info[1];
		firstName 		= info[2];
		lastName		= info[3];
		*/
		
    	db database = new db();
    	database.init();
    	
		String name[] = database.getMedicalStaffName(employeeID);
		this.firstName = name[0];
		this.lastName = name[1];
		this.medicalCenterID = database.getMedicalStaffHospitalId(employeeID);
		String hospitalName = database.getMedicalStaffHospital(employeeID);
		
		toArray();
		
		//TODO request info about medical center? name?
		
		database.con.close();
	}
	
	public String getEmployeeID () {
		return employeeID;
	}
	
	public String getMedicalCenterID () {
		return medicalCenterID;
	}
	
	public String getFirstName () {
		return firstName;
	}
	
	public String getLastName () {
		return lastName;
	}
	
	public String[] getArray () {
		return array;
	}
	
	void toArray() {
		array = new String[] {
				medicalCenterID,
				employeeID,
				firstName,
				lastName
		};
	}
	
}
