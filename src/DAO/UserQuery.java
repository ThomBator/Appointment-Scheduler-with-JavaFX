package DAO;

import model.User;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class UserQuery {

    public static List<User> createUserList()  {
       try {

           Connection conn = DBConnection.getConnection();
           final String SELECTSTATEMENT = "SELECT * FROM users";
           PreparedStatement preparedStatement = conn.prepareStatement(SELECTSTATEMENT);
           preparedStatement.execute();
           ResultSet result = preparedStatement.getResultSet();
           List<User> users = new ArrayList<>();
           while (result.next()) {
               int userID = result.getInt("User_ID");
               String userName = result.getString("User_Name");
               String password = result.getString("Password");
               Timestamp dateCreated = result.getTimestamp("Create_Date");
               String createdBy = result.getString("Created_By");
               Timestamp lastUpdate = result.getTimestamp("Last_Update");
               String lastUpdatedBy = result.getString("Last_Updated_By");

               users.add(new User(userID, userName, password, dateCreated, createdBy, lastUpdate, lastUpdatedBy));

           }


           DBConnection.closeConnection();
           for(User user: users){
               System.out.println(user.toString());

           }
           return users;
       }

       catch(SQLException e) {
           System.out.println("Select statement invalid. Please review.");



           }

       return null;



       }












}
