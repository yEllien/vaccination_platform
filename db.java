package hy351;

import java.sql.*;
import java.util.*;

public class db {
	
    private static final String url = "jdbc:mysql://localhost";
    private static final String databaseName = "hy351";
    private static final int port = 3306;
    private static final String username = "root";
    private static final String password = "";
    
    public Connection init(){
        Connection con = null;
 
        try{  
        	Class.forName("com.mysql.cj.jdbc.Driver");  
        	/*con = DriverManager.getConnection(  
        	"jdbc:mysql://localhost:3306/hy351","root","");   
        	*/
        	
        	con = DriverManager.getConnection("jdbc:mysql://localhost:" + port + "/" + databaseName,"root","");   
        	 
        	//con.close();  
        }catch(Exception e){ System.out.println(e);}  
		
    	return con; 
    }
    
    public void getCitizenName(Connection con, String ssn) throws SQLException{
    	Statement stmt = con.createStatement();  
    	ResultSet rs = stmt.executeQuery("select firstName, lastName "
					    			+ "from Citizen "
					    			+ "where ssn = " + ssn);  
    	
    	while(rs.next())  
    	System.out.println(rs.getString("firstName")+ " " + rs.getString("lastName")); 
    }
    
    public void getCitizenAppointmentID(Connection con, String ssn) throws SQLException{
    	Statement stmt = con.createStatement();  
    	ResultSet rs = stmt.executeQuery("select appointmentID "
					    			+ "from Books "
					    			+ "where citizenSSN = " + ssn);  
    	
    	while(rs.next())  
    	System.out.println(rs.getString("appointmentID")); 
    }
    
    public void bookAppointment(Connection con, String citizenSSN, String hospitalID) throws SQLException {
        
        Statement st = con.createStatement();
		CallableStatement proc = con.prepareCall("call BookAppointment(?,?,?);");
		
		ResultSet rs;
		
		Random rnd = new Random();
		char c = (char) ('a' + rnd.nextInt(26));
		
		String appointmentID = citizenSSN.substring(0, 9) + c;
		
		proc.setString(1, appointmentID);
		proc.setString(2, citizenSSN);
		proc.setString(3, hospitalID);
		
		rs = proc.executeQuery();
		
		rs.close();
		proc.close();
    }
    
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
    	
    	db database = new db();
    	Connection con = database.init();
    	
    	String ssn = "11018701926";
    	database.getCitizenName(con, ssn);
    	database.bookAppointment(con, ssn, "20309");
    	con.close();
    }  
}