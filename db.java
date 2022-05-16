package Database;

import java.sql.*;
import java.util.*;

public class db {
	
    private static final String url = "jdbc:mysql://localhost";
    private static final String databaseName = "hy351";
    private static final int 	port = 3306;
    private static final String username = "root";
    private static final String password = "";
    public Connection con;
    
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
		
		
		CallableStatement proc2 = con.prepareCall("call AddAppointment(?,?,?,?,?);");
		proc2.setString(1, citizenSSN);
		proc2.setInt(2, doseNumber);
		proc2.setString(3, date);
		proc2.setString(4, timeSlot);
		proc2.setString(5, vaccine);
		rs = proc2.executeQuery();
		
		rs.close();
		proc2.close();
		
		UpdateTimeSlotCapacity(hospitalID, date, timeSlot);
    }
    
    public int getDosesCompleted(String ssn) throws SQLException {
    	int doses = 0;
    	Statement stmt = con.createStatement();  
    	ResultSet rs = stmt.executeQuery("select dosesCompleted "
					    			+ "from Citizen "
					    			+ "where ssn = " + ssn);  
    	
    	while(rs.next()) {
    		doses = rs.getInt("dosesCompleted");
    	}
    	
    	return doses;
    }
    
    public void UpdateDosesAndVaccinationState(String ssn, int doseNumber) throws SQLException {
    	Statement st = con.createStatement();  
    	CallableStatement proc = con.prepareCall("call UpdateDosesAndState(?,?);");  
    	ResultSet rs;
    	
    	proc.setString(1, ssn);
    	proc.setInt(2, doseNumber);
    	
    	rs = proc.executeQuery();
    	
    	rs.close();
    	proc.close();
    }
    
    public String getVaccinationState(String ssn) throws SQLException{
    	String state = "";
    	Statement stmt = con.createStatement();  
    	ResultSet rs = stmt.executeQuery("select vaccinationState "
					    			+ "from Citizen "
					    			+ "where ssn = " + ssn);  
    	
    	while(rs.next()) {
    		state = rs.getString("vaccinationState");
    	}
    	
		return state;
    }
    
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
    
    public boolean IssueCertificate(String ssn) throws SQLException {
    	String state = getVaccinationState(ssn);
    	
    	if(state == "fully vaccinated"){
    		String certificateID = generateCertificateID(ssn);
        	
    		Statement st = con.createStatement();  
        	CallableStatement proc = con.prepareCall("call IssueVaccinationCertificate(?,?);");  
        	ResultSet rs;
        	
        	proc.setString(1, certificateID);
        	proc.setString(2, ssn);
        	
        	rs = proc.executeQuery();
        	
        	rs.close();
        	proc.close();
    		return true;
    	}
    	
    	return false;
    }
    
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
    	
    	return date;
    }
    
    
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
    
    // Procedure CancelAppointment() checks if date is at least 3 days prior to the booked appointment
    // Doesn't check if citizen has cancelled an appointment before
    
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
     * Methods to execute queries for MedicalStaff 
     */
    
    public String[] getMedicalStaffName(String employeeID) throws SQLException{
    	String name[] = null;
    	Statement stmt = con.createStatement();  
    	ResultSet rs = stmt.executeQuery("select firstName, lastName "
					    			+ "from MedicalStaff "
					    			+ "where employeeID = " + employeeID);  
    	
    	while(rs.next()) {
    		name[0] = rs.getString("firstName");
    		name[1] = rs.getString("lastName");
    		System.out.println(name[0] + " " + name[1]); 
    	}
    	
		return name;
    }
    
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
     * Methods to execute queries for a Hospital 
     */
    
    public String getHospitalID(String hospitalName) throws SQLException {
    	Statement st = con.createStatement();  
    	String hospitalID = "";
    	
    	ResultSet rs = st.executeQuery("select hospitalID "
					    			+ "from Hospital "
					    			+ "where name = " + hospitalName );  

		while(rs.next()) {
			hospitalID = rs.getString("hospitalID");
		}
		
    	return hospitalID;
    }
    
    public ArrayList<String[]> getDateAndTimeSlotsAvailability(String hospitalID, String fromDate) throws SQLException {
    	Statement st = con.createStatement();  
    	ArrayList<String[]> dates = new ArrayList<String[]>();
    	
    	ResultSet rs = st.executeQuery("select day, timeSlot, capacity "
					    			+ "from Hospital_Time_Slots "
					    			+ "where hospitalID = " + hospitalID
					    			+ " and day >= " + fromDate);  

    	dates = ResultSetArray(rs);
    	return dates;
    }
    
    public ArrayList<String> getNearbyHospitals(String postalCode) throws SQLException{
    	Statement st = con.createStatement();  
    	ArrayList<String> hospitals = new ArrayList<String>();
    	
    	String pc = postalCode.substring(0, 2); 
    	ResultSet rs = st.executeQuery("select name "
					    			+ "from Hospital "
					    			+ "where postalCode LIKE " + "\'%" + pc + "%\'");  

		while(rs.next()) {
			hospitals.add(rs.getString("name"));
		}

		for(int i=0; i<hospitals.size() ; i++) {
			System.out.println(hospitals.get(i).toString() + " ");
		}
		
    	return hospitals;
    }
    
    /*
     * Helper functions
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
	
	private static void PrintArrayList(ArrayList<String[]> ar) {
	      for (int i = 0; i < ar.size();i++){ 		      
	    	  String[] row = ar.get(i);
	    	  for (int j = 0; j < row.length; j++){ 		      
		          System.out.print(row[j] + " ");
		      }   System.out.println();
	      }   	
	}
	
    
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
    	db database = new db();
    	database.init();
    	
    	System.out.println("connected");
    	
    	String ssn = "11018701926";
    	String[] name = database.getCitizenName(ssn);
    	
    	if(name!=null) {
    	//database.getCitizenAppointmentID(ssn);
    	//database.bookAppointment(1, ssn, "20309");
    	//ArrayList<String[]> ar = database.getDateAndTimeSlotsAvailability("20309", "2022/05/16");
    			//database.GetDailyAppointments("20309", "2022/05/19");
    	//PrintArrayList(ar);
    	database.getNearbyHospitals("21810");
    	database.bookAppointment(1, ssn, "20309", "2022/05/22", "08:00-12:00", "Pfizer");
    	//database.CancelAppointment(ssn, 1);
    	database.con.close();
    	}
    }  
}
