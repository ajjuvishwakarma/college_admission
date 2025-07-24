package collegeadmission.com;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL = "jdbc:mysql://localhost:4306/college_admission_db";
    private static final String USER = "root"; 
    private static final String PASSWORD = "Ajay2001*";
    		
    private static Connection connection = null;

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println(" Database connected successfully.");
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(" Database connection failed: " + e.getMessage());
        }
        return connection;
    }
}