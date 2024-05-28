import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class THE_CONNECTION {
    private static String servername = "localhost";
    private static String dbname = "mms";
    private static String username = "root";
    private static Integer portnumber = 3306;
    private static String password = "root@123";

    // Create a function to get the connection
    public static Connection getTheConnection() {
        Connection connection = null;
        try {
            // Establishing connection
            connection = DriverManager.getConnection("jdbc:mysql://" + servername + ":" + portnumber + "/" + dbname, username, password);
            System.out.println("Connected with the database successfully");
        } catch (SQLException e) {
            // Log or throw the exception for better error handling
            e.printStackTrace();
            // You may also throw a custom exception here if you want to handle it in the calling code
            // throw new RuntimeException("Error while connecting to the database", e);
        }
        return connection;
    }
}
