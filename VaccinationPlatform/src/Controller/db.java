package Controller;

import java.sql.*;
import java.sql.Date;
import java.util.*;

import com.mysql.cj.xdevapi.Type;

public class db {
	
    private static final String url = "jdbc:mysql://localhost";
    private static final String databaseName = "hy351";
    private static final int 	port = 3306;
    private static final String username = "root";
    private static final String password = "";
    public Connection con;
    
    /*
     * Initializes a connection to the database 
     */
    
    public void init(){
        con = null;
 
        try{  
        	Class.forName("com.mysql.cj.jdbc.Driver");          	
        	con = DriverManager.getConnection("jdbc:mysql://localhost:" + port + "/" + databaseName,"root",""); 
        }catch(Exception e){ System.out.println(e);}  
    }
    
    /* 
     * Methods to execute queries for a Citizen 
     */
    
    /*
     * Returns first name and last name of Citizen
     */
    
    public String[] getCitizenName(String ssn) throws SQLException{
    	String name[] = {"", ""};
    	Statement stmt = con.createStatement();  
    	ResultSet rs = stmt.executeQuery("select firstName, lastName "
					    			+ "from Citizen "
					    			+ "where ssn = " + ssn);  
    	
    	while(rs.next()) {
    		name[0] = rs.getString("firstName");
    		name[1] = rs.getString("lastName");
    		System.out.println(name[0] + " " + name[1]); 
    	}
    	rs.close();
		return name;
    }
    
    /*
     * Returns Citizen's birth date in format yyyy/mm/dd
     */
    
    public String getCitizenBirthDate(String ssn) throws SQLException{
    	Date dateOfBirth;
    	String date = "";
    	Statement stmt = con.createStatement();  
    	ResultSet rs = stmt.executeQuery("select dateOfBirth "
					    			+ "from Citizen "
					    			+ "where ssn = " + ssn);  
    	while(rs.next()) {
    		dateOfBirth = rs.getDate("dateOfBirth");
    		date = dateOfBirth.toString();
    	}

    	rs.close();
		return date;
    }
    
    /*
     * Returns Citizen's gender
     */
    
    public String getCitizenGender(String ssn) throws SQLException{
    	String gender = "";
    	Statement stmt = con.createStatement();  
    	ResultSet rs = stmt.executeQuery("select gender "
					    			+ "from Citizen "
					    			+ "where ssn = " + ssn);  
    	while(rs.next()) {
    		gender = rs.getString("gender");
    	}
    	
    	rs.close();
		return gender;
    }
    
    /*
     * Returns an array with a Citizen's communication information;
     * Email, Phone Number and Postal Code
     */
    
    public String[] getCitizenCommunicationInfo(String ssn) throws SQLException{
    	String info[] = {"", "", ""};
    	Statement stmt = con.createStatement();  
    	ResultSet rs = stmt.executeQuery("select email, phoneNumber, postalCode "
					    			+ "from CommunicationInfo "
					    			+ "where SSN = " + ssn);  
    	
    	while(rs.next()) {
    		info[0] = rs.getString("email");
    		info[1] = rs.getString("phoneNumber");
    		info[2] = rs.getString("postalCode");
    	}
    	
    	rs.close();
		return info;
    }
    
    /*
     * Decrements capacity of given time slot in date and hospital scpecified by date and hospitalID
     */
    
    public void UpdateTimeSlotCapacity(String hospitalID, String date, String timeSlot) throws SQLException{
        Statement st = con.createStatement();
		CallableStatement proc = con.prepareCall("call UpdateCapacity(?,?,?);");
		ResultSet rs;
		
		proc.setString(1, hospitalID);
		proc.setString(2, date);
		proc.setString(3, timeSlot);
		
		rs = proc.executeQuery();
		
		rs.close();
		proc.close();
    }

    /*
     * Adds a new appointment for Citizen in corresponding database table 
     */
    
    public void AddAppointment(int doseNumber, String citizenSSN, String hospitalID, 
			String date, String timeSlot, String vaccine) throws SQLException {

			Statement st = con.createStatement();
			CallableStatement proc = con.prepareCall("call AddAppointment(?,?,?,?,?);");
			ResultSet rs;
			
			proc.setString(1, citizenSSN);
			proc.setInt(2, doseNumber);
			proc.setString(3, date);
			proc.setString(4, timeSlot);
			proc.setString(5, vaccine);
			rs = proc.executeQuery();
			
			rs.close();
			proc.close();
    }
    
    /*
     * Adds a record with Citizen SSN, Dose Number and HospitalID in table corresponding to relationship Books
     */
    
    public void bookAppointment(int doseNumber, String citizenSSN, String hospitalID, 
    							String date, String timeSlot, String vaccine) throws SQLException {
        
        Statement st = con.createStatement();
		CallableStatement proc = con.prepareCall("call BookAppointment(?,?,?);");
		ResultSet rs;
		
		proc.setInt(1, doseNumber);
		proc.setString(2, citizenSSN);
		proc.setString(3, hospitalID);
		
		rs = proc.executeQuery();
	
		rs.close();
		proc.close();
		
		AddAppointment(doseNumber, citizenSSN, hospitalID, date, timeSlot, vaccine);
		UpdateTimeSlotCapacity(hospitalID, date, timeSlot);
    }
    
    /*
     * Returns doses successfully completed by Citizen 
     */
    
    public int getDosesCompleted(String ssn) throws SQLException {
    	int doses = 0;
    	Statement stmt = con.createStatement();  
    	ResultSet rs = stmt.executeQuery("select dosesCompleted "
					    			+ "from Citizen "
					    			+ "where ssn = " + ssn);  
    	
    	while(rs.next()) {
    		doses = rs.getInt("dosesCompleted");
    	}
    	
    	rs.close();
    	return doses;
    }
    
    /*
     * Make Appointment state confirmed
     */
    
    public void UpdateAppointment(String ssn, int doseNumber) throws SQLException {
    	Statement st = con.createStatement();  
    	CallableStatement proc = con.prepareCall("call UpdateAppointment(?,?);");  
    	ResultSet rs;
    	
    	proc.setString(1, ssn);
    	proc.setInt(2, doseNumber);
    	
    	rs = proc.executeQuery();
    	
    	rs.close();
    	proc.close();
    }
    
    /*
     * Updates number of completed doses and vaccination state of Citizen 
     */
    
    public void UpdateDosesAndVaccinationState(String ssn, int doseNumber) throws SQLException {
    	Statement st = con.createStatement();  
    	CallableStatement proc = con.prepareCall("call UpdateDosesAndState(?,?);");  
    	ResultSet rs;
    	
    	proc.setString(1, ssn);
    	proc.setInt(2, doseNumber);
    	
    	rs = proc.executeQuery();
    	
    	rs.close();
    	proc.close();
    	
    	UpdateAppointment(ssn, doseNumber);
    }
    
    /*
     * Returns vaccination state of Citizen; 
     * 'not vaccinated' , 'partially vaccinated', 'fully vaccinated'
     */
    
    public String getVaccinationState(String ssn) throws SQLException{
    	String state = "";
    	Statement stmt = con.createStatement();  
    	ResultSet rs = stmt.executeQuery("select vaccinationState "
					    			+ "from Citizen "
					    			+ "where ssn = " + ssn);  
    	
    	while(rs.next()) {
    		state = rs.getString("vaccinationState");
    	}
    	
    	rs.close();
		return state;
    }
    
    /*
     * Generates a random CertificateID that consists of Citizen's SSN and random characters
     */
    
    private String generateCertificateID(String ssn) {

		Random rnd = new Random();
		String id = ssn;
		int i = 0;
		
		while(i++ < 9) {			
			char c = (char) ('a' + rnd.nextInt(26));
			id = id + c ;
		}

		return id;
    }
    
    /*
     * Checks if Citizen has completed all required doses and 
     * issues a Vaccination Certificate .
     * Returns true if certificate is issued successfully, false otherwise.
     */
    
    public boolean IssueCertificate(String ssn) throws SQLException {
    	String state = getVaccinationState(ssn);
    	
    	if(state == "fully vaccinated"){

    		String certificateID = generateCertificateID(ssn);
        	
    		Statement st = con.createStatement();  
        	CallableStatement proc = con.prepareCall("call IssueVaccinationCertificate(?,?,?);");  
        	ResultSet rs;
        	
        	proc.setString(1, certificateID);
        	proc.setString(2, ssn);
        	proc.registerOutParameter(3, Types.BOOLEAN);
        	rs = proc.executeQuery();
        	
        	boolean valid = rs.getBoolean(3);
        	
        	rs.close();
        	proc.close();
        	
    		return valid;
    	}
    	
    	return false;
    }
    
    /*
     * Get date of Appointment booked by Citizen
     */
    
    public String getAppointmentDate(String ssn, int doseNum) throws SQLException {
    	String date = "";
    	Statement stmt = con.createStatement();  
    	ResultSet rs = stmt.executeQuery("select appointmentDate "
						    			+ "from appointment "
						    			+ "where citizenSSN = " + ssn
						    			+ " and doseNumber = " + doseNum);  
	    	
    	while(rs.next()) {
    		date = rs.getString("appointmentDate");
    	}
    	
    	rs.close();
    	return date;
    }
    
    /*
     * Returns Appointments booked by Citizen
     */
    
    public ArrayList<String[]> ViewBookedAppointments(String ssn) throws SQLException {
		CallableStatement st = con.prepareCall("call ViewBookedAppointments(?);");
		ArrayList<String[]> bookedAppointments = new ArrayList<String[]>();

		ResultSet rs;
		st.setString(1, ssn);
		rs = st.executeQuery();
		
		bookedAppointments =  ResultSetArray(rs);
		
		rs.close();
		st.close();
		
		return bookedAppointments;    
    }
    
    /*
     * Similar to ViewBookedAppointments().
     * Returns additional information about the VAccine name. 
     */
    
    public ArrayList<String[]> GetBookedAppointments(String ssn) throws SQLException {
		CallableStatement st = con.prepareCall("call GetBookedAppointments(?);");
		ArrayList<String[]> bookedAppointments = new ArrayList<String[]>();

		ResultSet rs;
		st.setString(1, ssn);
		rs = st.executeQuery();
		
		//while(rs.next()) {
		bookedAppointments =  ResultSetArray(rs);
		//}
		
		rs.close();
		st.close();
		
		return bookedAppointments;    
    }
    
    /*
     * Returns Appointments of Citizen that have been confirmed 
     */
    
    public ArrayList<String[]> ViewCompletedVaccinations(String ssn) throws SQLException {
		CallableStatement st = con.prepareCall("call ViewCompletedVaccinations(?);");
		ArrayList<String[]> completedVaccinations = new ArrayList<String[]>();

		ResultSet rs;
		st.setString(1, ssn);
		rs = st.executeQuery();
		
		completedVaccinations =  ResultSetArray(rs);
		
		rs.close();
		st.close();
		
		return completedVaccinations;    
    }
    
    /*
     * Checks if date is at least 3 days prior to the booked appointment 
     * and removes it from database, incrementing the capacity of the corresponding time slot.
     */ 
    
    public void CancelAppointment(String ssn, int doseNum) throws SQLException {
		Statement st = con.createStatement();  
    	CallableStatement proc = con.prepareCall("call CancelAppointment(?,?);");  
    	ResultSet rs;

    	proc.setString(1, ssn);
    	proc.setInt(2, doseNum);
    	rs = proc.executeQuery();
    	
		rs.close();
		proc.close();

    }
    
    /*
     * Changes date of an already booked Appointment
     */
    
    public void ModifyDate(String ssn, int doseNum, String newDate, String oldTimeSlot, String hospitalID) throws SQLException {
		Statement st = con.createStatement();  
    	CallableStatement proc = con.prepareCall("call ModifyDate(?,?,?,?,?);");  
    	ResultSet rs;

    	proc.setString(1, ssn);
    	proc.setInt(2, doseNum);
    	proc.setString(3, newDate);
    	proc.setString(4, oldTimeSlot);
    	proc.setString(5, hospitalID);
    	rs = proc.executeQuery();
    	
		rs.close();
		proc.close();
    }
    
    /*
     * Changes time slot of an already booked Appointment 
     */
    
    public void ModifyTimeSlot(String ssn, int doseNum, String oldDate, String newTimeSlot, String hospitalID) throws SQLException {
		Statement st = con.createStatement();  
    	CallableStatement proc = con.prepareCall("call ModifyDate(?,?,?,?,?);");  
    	ResultSet rs;

    	proc.setString(1, ssn);
    	proc.setInt(2, doseNum);
    	proc.setString(3, oldDate);
    	proc.setString(4, newTimeSlot);
    	proc.setString(5, hospitalID);
    	rs = proc.executeQuery();
    	
		rs.close();
		proc.close();
    }
    
    /*
     * Returns information about date, dose number, vaccine name,
     * vaccine manufacturer and medical center name of a confirmed appointment.
     */
    
    public ArrayList<String[]> GetVaccinationInfo(String ssn) throws SQLException{
		CallableStatement st = con.prepareCall("call ViewVaccinationInfo(?);");
		ArrayList<String[]> vaccinationInfo = new ArrayList<String[]>();

		ResultSet rs;
		st.setString(1, ssn);
		rs = st.executeQuery();
		
		vaccinationInfo =  ResultSetArray(rs);
		
		rs.close();
		st.close();
		
		return vaccinationInfo;  
    }
    
    /*
     * Returns status of Appointment; 
     * Not Confirmed = false
     * Confirmed = true
     */
    
    public boolean getAppointmentStatus(String ssn, int doseNumber) throws SQLException{
    	boolean status = false;
    	Statement stmt = con.createStatement();  
    	ResultSet rs = stmt.executeQuery("select Confirmed "
					    			+ "from appointment a "
					    			+ "where a.citizenSSN = " + ssn
					    			+ " and a.doseNumber = " + doseNumber);  
    	while(rs.next()) {
    		status = rs.getBoolean("Confirmed");
    	}

    	rs.close();
		return status;
    }
    
    
    /*
     * Updates a Citizen's email address
     */
    
    public void UpdateEmail(String ssn, String email) throws SQLException {
    	PreparedStatement preparedStmt = con.prepareStatement("update CommunicationInfo "
												    			+ "set email = ? "
												    			+ "where ssn = ? ");
    	preparedStmt.setString(1, email);
    	preparedStmt.setString(2, ssn);
    	preparedStmt.executeUpdate();
    }
    
    /*
     * Updates a Citizen's phone number
     */
    
    public void UpdatePhoneNumber(String ssn, String phoneNumber) throws SQLException {
    	PreparedStatement preparedStmt = con.prepareStatement("update CommunicationInfo "
												    			+ "set phoneNumber = ? "
												    			+ "where ssn = ?" );
    	preparedStmt.setString(1, phoneNumber);
    	preparedStmt.setString(2, ssn);
    	preparedStmt.executeUpdate();
    }
    
    /*
     * Updates a Citizen's postal code
     */
    
    public void UpdatePostalCode(String ssn, String postalCode) throws SQLException {
    	PreparedStatement preparedStmt = con.prepareStatement("update CommunicationInfo "
												    			+ "set postalCode = ? "
												    			+ "where ssn = ? ");
    	preparedStmt.setString(1, postalCode);
    	preparedStmt.setString(2, ssn);
    	preparedStmt.executeUpdate();
    }
    
    /*
     * Adds a Citizen's communication information;
     * email, phone number and postal code
     */
    
    public void AddCommunicationInfo(String ssn, String email, String phoneNumber, String postalCode) throws SQLException {
		CallableStatement st = con.prepareCall("call AddCommunicationInfo(?, ?, ?, ?);");

		ResultSet rs;
		st.setString(1, ssn);
		st.setString(2, email);
		st.setString(3, phoneNumber);
		st.setString(4, postalCode);
		rs = st.executeQuery();
		
		rs.close();
		st.close();
    }
    
    /* 
     * Methods to execute queries for MedicalStaff 
     */
    
    /*
     * Returns first name and last name of a Medical Staff
     */
    
    public String[] getMedicalStaffName(String employeeID) throws SQLException{
    	String name[] = {"", ""};
    	Statement stmt = con.createStatement(); 
    	
    	ResultSet rs = stmt.executeQuery("select firstName, lastName "
					    			+ "from MedicalStaff "
					    			+ "where employeeID = " + employeeID);  
    	
    	while(rs.next()) {
    		name[0] = rs.getString("firstName");
    		name[1] = rs.getString("lastName");
    		System.out.println(name[0] + " " + name[1]); 
    	}
    	
    	rs.close();
		return name;
    }

    /*
     * Returns name of Medical Center a Medical Staff works at
     */

    
    public String getMedicalStaffHospital(String employeeID) throws SQLException{
    	String hospitalID = null;
    	String hospitalName = null;
    	
    	Statement stmt = con.createStatement();  
    	ResultSet rs = stmt.executeQuery("select hospitalID "
					    			+ "from MedicalStaff "
					    			+ "where employeeID = " + employeeID);  
    	
    	while(rs.next()) {
    		hospitalID = rs.getString("hospitalID");
    	}
    	
    	rs = stmt.executeQuery("select name "
				    			+ "from Hospital "
				    			+ "where hospitalID = " + hospitalID);  

		while(rs.next()) {
			hospitalName = rs.getString("name");
		}
    	
		return hospitalName;
    }
    
    
    /*
     * Returns ID of Medical Center a Medical Staff works at
     */

    public String getMedicalStaffHospitalId(String employeeID) throws SQLException{
    	String hospitalID = "";
    	
    	Statement stmt = con.createStatement();  
    	ResultSet rs = stmt.executeQuery("select hospitalID "
					    			+ "from MedicalStaff "
					    			+ "where employeeID = " + employeeID);  
    	
    	while(rs.next()) {
    		hospitalID = rs.getString("hospitalID");
    	}
    	
		return hospitalID;
    }
    
    /*
     * Returns Appointments booked at given Vaccination Center.
     * If date is specified it returns the corresponding Appointments, 
     * otherwise it returns the Appointments of current date.
     */
    
    public ArrayList<String[]> GetDailyAppointments(String hospitalID, String date) throws SQLException{
    	
		CallableStatement st = con.prepareCall("call ViewDailyAppointments(?,?);");
		ArrayList<String[]> dailyAppointments = new ArrayList<String[]>();

		ResultSet rs;
		st.setString(1, hospitalID);
		st.setString(2, date);
		rs = st.executeQuery();
		
		dailyAppointments =  ResultSetArray(rs);
		
		rs.close();
		st.close();
		
		return dailyAppointments;    	
    }
    
    /*
     * Returns Appointments booked by given Citizen (from those returned by ViewDailyAppointments) 
     */
    
    public ArrayList<String[]> GetAppointmentsBySSN(String hospitalID, String ssn) throws SQLException{
    	
		CallableStatement st = con.prepareCall("call ViewAppointmentsBySSN(?,?);");
		ArrayList<String[]> appointments = new ArrayList<String[]>();

		ResultSet rs;
		st.setString(1, hospitalID);
		st.setString(2, ssn);
		rs = st.executeQuery();
		
		appointments =  ResultSetArray(rs);
		
		rs.close();
		st.close();
		
		return appointments;    	
    }
    
    /*
     * Confirms a Citizen's Vaccination
     */
    
    public void ConfirmVaccination(String ssn, int doseNumber) throws SQLException {
    	UpdateDosesAndVaccinationState(ssn, doseNumber);
    	
    }
    
    /* 
     * Methods to execute queries for a Hospital 
     */
    
    /*
     * Returns ID of Hospital specified by hospitalName 
     */
    
    public String getHospitalID(String hospitalName) throws SQLException {
    	Statement st = con.createStatement();  
    	String hospitalID = "";
    	
    	ResultSet rs = st.executeQuery(" select hospitalID "
					    			+ "from Hospital "
					    			+ "where name = " + "\"" + hospitalName + "\"");  

		while(rs.next()) {
			hospitalID = rs.getString("hospitalID");
		}
		
    	return hospitalID;
    }
    
    /*
     * Returns available Time Slots for given Hospital and date 
     */
    
    public ArrayList<String[]> getDateAndTimeSlotsAvailability(String hospitalID) throws SQLException {

    	Statement st = con.createStatement();  
    	ArrayList<String[]> dates = new ArrayList<String[]>();
    	
    	ResultSet rs = st.executeQuery("select day, timeSlot, capacity "
					    			+ "from Hospital_Time_Slots "
					    			+ "where hospitalID = " + hospitalID
					    			+ " and day >= CURDATE()");  

    	dates = ResultSetArray(rs);
    	return dates;
    }
    
    /*
     * Returns Hospitals near Citizen by comparing the first digit of
     * postal codes of Hospital and Citizen
     */
    
    public ArrayList<String> getNearbyHospitals(String postalCode) throws SQLException{
    	Statement st = con.createStatement();  
    	ArrayList<String> hospitals = new ArrayList<String>();
    	
    	String pc = postalCode.substring(0, 1); 
    	ResultSet rs = st.executeQuery("select name "
					    			+ "from Hospital "
					    			//+ "where postalCode LIKE " + "\'%" + pc + "%\'"
					    			+ "where (select substring(postalCode, 1, 1) like "
					    			//+ "\'" + pc + "\'"
					    			+ " (select substring(" + "\'" + postalCode +"\'" + ", 1, 1)))"
    								);  

		while(rs.next()) {
			hospitals.add(rs.getString("name"));
		}
		
		rs.close();
    	return hospitals;
    }
    
    /*
     * Returns the name of the Vaccine used by given Hospital 
     */
    
    public String getHospitalVaccine(String hospitalID) throws SQLException {     	
		CallableStatement st = con.prepareCall("call GetHospitalVaccine(?);");
		ArrayList<String[]> appointments = new ArrayList<String[]>();
		String vaccineName = "";

		ResultSet rs;
		st.setString(1, hospitalID);
		rs = st.executeQuery();
		
		while(rs.next()) {
			vaccineName = rs.getString("name");
		}
		
		rs.close();
		st.close();
		
		return vaccineName;
    }
    
    /*
     * Helper functions
     */
    
    /*
     * Converts ResultSet into ArrayList<String[]>
     */
    
	private ArrayList<String[]> ResultSetArray(ResultSet rs) throws SQLException{
		ArrayList<String[]> list = new ArrayList<String[]>();
		ResultSetMetaData md = rs.getMetaData();
		
		if(rs.next() == false) return list;
		
		String[] row = new String[md.getColumnCount()];
		
		for (int i = 0 ; i < md.getColumnCount() ; i++){			
			row[i] = md.getColumnName(i+1);
		}
		list.add(row);
		
		do{
			row = new String[md.getColumnCount()];
			for(int i = 0 ; i < md.getColumnCount() ; i++){				
				row[i] = rs.getString(i+1);
			}
			list.add(row);
		
		}while(rs.next());
		
		return list;
	}
	
	/*
	 * Prints an ArrayList<String[]> 
	 */
	
	public static void PrintArrayList(ArrayList<String[]> ar) {
	      for (int i = 0; i < ar.size();i++){ 		      
	    	  String[] row = ar.get(i);
	    	  for (int j = 0; j < row.length; j++){ 		      
		          System.out.print(row[j] + " ");
		      }   System.out.println();
	      }   	
	}   
    
}