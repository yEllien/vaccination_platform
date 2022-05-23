package Controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Connector {

    private Connection con;

    public Connector() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            String url = "jdbc:mysql://localhost";
            String databaseName = "VaccinationPlatform";
            int port = 3306;
            String username = "root";
            String passwd = "";
            
            this.con = DriverManager.getConnection(url + ":" + port + "/" + databaseName + "?characterEncoding=UTF-8", username, passwd);

        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("ERROR: " + e.getMessage());
        }
    }

    public Connection getConnection() {
        return this.con;
    }
}

