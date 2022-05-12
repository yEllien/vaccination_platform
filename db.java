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
					    			+ "from Appointments "
					    			+ "where citizenSSN = " + ssn);  
    	
    	while(rs.next())  
    	System.out.println(rs.getString("appointmentID")); 
    }
    
    public void bookAppointment(Connection con, String ssn) throws SQLException {
        // Creating a statement
        Statement st = con.createStatement();
        String sql = "insert into appointment values (\"0123456789\", \"2022/05/20\", \"08:00-12:00\", \"Pfizer\")";
        
        // Executing query
        int m = st.executeUpdate(sql);
        if (m == 1)
            System.out.println(
                "inserted successfully : " + sql);
        else
            System.out.println("insertion failed");
    }
    
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
    	
    	db database = new db();
    	Connection con = database.init();
    	
    	String ssn = "11018701926";
    	database.getCitizenName(con, ssn);
    	database.bookAppointment(con, ssn);
    	con.close();
    }  
}