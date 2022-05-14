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
    	
		return name;
    }
    
    public void bookAppointment(int doseNumber, String citizenSSN, String hospitalID) throws SQLException {
        
        Statement st = con.createStatement();
		CallableStatement proc = con.prepareCall("call BookAppointment(?,?,?);");
		
		ResultSet rs;
		
		/*
		Random rnd = new Random();
		char c = (char) ('a' + rnd.nextInt(26));
		String appointmentID = citizenSSN.substring(0, 9) + c;
		*/
		
		proc.setInt(1, doseNumber);
		proc.setString(2, citizenSSN);
		proc.setString(3, hospitalID);
		
		rs = proc.executeQuery();
		
		rs.close();
		proc.close();
    }
    
    public void ConfirmAppointment(String ssn, int doseNumber, String hospitalID, String date, String timeSlot, String vaccine){
    	
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
    public String getCitizenAppointmentID(String ssn) throws SQLException{
    	String appointmentID = null;
    	
    	Statement stmt = con.createStatement();  
    	ResultSet rs = stmt.executeQuery("select appointmentID "
					    			+ "from Books "
					    			+ "where citizenSSN = " + ssn);  
    	
    	while(rs.next()) {
    		appointmentID = rs.getString("appointmentID");
    		System.out.println(appointmentID); 
    	}
    	
    	return appointmentID;
    } 
    */
    
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
    	database.con.close();
    	}
    }  
}