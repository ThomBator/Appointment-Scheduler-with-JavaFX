package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
/**
 The DBConnection class serves as the basis for all database interactions throughout this program. It contains
 all the methods and fields required to successfully login and interact with the database.
 */
public abstract class DBConnection {
    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String location = "//localhost/";
    private static final String databaseName = "client_schedule";
    private static final String jdbcUrl = protocol + vendor + location + databaseName + "?connectionTimeZone = SERVER"; // LOCAL
    private static final String driver = "com.mysql.cj.jdbc.Driver"; // Driver reference
    private static final String userName = "sqlUser"; // Username
    private static String password = "Passw0rd!"; // Password
    public static Connection connection;  // Connection Interface

    /**
     The getConnection method can be called by all the query classes in the DAO package to establish a connection with the database.
     A connection is created by passing key static fields created above including jdbcURL, userName and password to the static method
     DriverManger.getConnection(). This method call returns the needed connection object.
     @return a Connection object used to interact with the databse.
     */
    public static Connection getConnection()
    {
        try {
            Class.forName(driver); // Locate Driver
            connection = DriverManager.getConnection(jdbcUrl, userName, password); // Reference Connection object

            return connection;
        }
        catch(Exception e)
        {
            System.out.println("Error:" + e.getMessage());
        }

        return null;
    }
    /**
     Whenever a database query is completed, it is best practice to close the connection. This method
     can be called to close the connection as needed, although in most cases I have used a Try With Resources construct in
     my query classes. Using this approach and passing the Connection as the resource actually leads to the Connection being closed automatically.
     This method was included in the webinar on establishing a DB connection, so I just left it in in case there was an unforseen scenario where it was still needed.
     
     */
    public static void closeConnection() {
        try {
            connection.close();
            System.out.println("Connection closed!");
        }
        catch(Exception e)
        {
            System.out.println("Error:" + e.getMessage());
        }
    }
}
