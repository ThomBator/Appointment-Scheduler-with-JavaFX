package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.User;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 This class handles all queries related to objects of the User class and associated database records in the users table.
 */

public class UserQuery {

    private static ObservableList<User> users = FXCollections.observableArrayList();

    /**
     The getDBUsers method uses a PreparedStatement object to query the database, which then supplies a ResultSet object containing all records in the users table.
     */

    public static ObservableList<User> getDBUsers() {

        if(users.isEmpty()) {

            try( Connection conn = DBConnection.getConnection();){

                final String SELECTSTATEMENT = "SELECT * FROM users";
                PreparedStatement preparedStatement = conn.prepareStatement(SELECTSTATEMENT);
                preparedStatement.execute();
                ResultSet result = preparedStatement.getResultSet();

                while (result.next()) {
                    int userID = result.getInt("User_ID");
                    String userName = result.getString("User_Name");
                    String password = result.getString("Password");
                    Timestamp dateCreated = result.getTimestamp("Create_Date");
                    String createdBy = result.getString("Created_By");
                    Timestamp lastUpdate = result.getTimestamp("Last_Update");
                    String lastUpdatedBy = result.getString("Last_Updated_By");

                    users.add(new User(userID, userName, password, dateCreated.toLocalDateTime(), createdBy, lastUpdate.toLocalDateTime(), lastUpdatedBy));

                }
                preparedStatement.close();
                result.close();



            }

            catch(SQLException e){
                System.out.println("Invalid statement. Please review your SQL input string.");

            }






        }

           return users;


    }












}
