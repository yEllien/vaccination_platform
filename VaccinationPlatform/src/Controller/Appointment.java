package Controller;

import java.sql.Date;

public class Appointment {
	
	public String SSN;
	public String medicalCenterID;
	public String medicalCenterName;
	public String vaccineName;
	public int doseNumber;
	public String date;
	public String time;
	public int status;
	
	public Appointment(
			String SSN,
			String medicalCenterID,
			String medicalCenterName,
			String vaccineName,
			int doseNumber,
			String date,
			String time,
			int status
			) {
		
		this.SSN = SSN;
		this.medicalCenterID = medicalCenterID;
		this.medicalCenterName = medicalCenterName;
		this.vaccineName = vaccineName;
		this.doseNumber = doseNumber;
		this.date = date;
		this.time = time;
		this.status = status;
	}
	
	public Appointment (
			String SSN, int doseNumber
			) {
		this.SSN = SSN;
		this.medicalCenterID = null;
		this.medicalCenterName = null;
		this.vaccineName = null;
		this.doseNumber = doseNumber;
		this.date = null;
		this.time = null;
		this.status = -1;
	}
	
	public String getSSN () {
		return SSN;
	}
	
	public String getMedicalCenterID () {
		return medicalCenterID;
	}
	
	public String getMedicalCenterName () {
		return medicalCenterName;
	}

	public String getVaccineName () {
		return vaccineName;
	}
	
	public int getDoseNumber () {
		return doseNumber;
	}
	
	public String getDate () {
		return date;
	}
	
	public String getTime () {
		return time;
	}
	
	public int getStatus () {
		return status;
	}
	
}
