package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;

public class AppointmentQuery {
    private static ObservableList<Appointment> appointments = FXCollections.observableArrayList();

    public static ObservableList<Appointment> getAppointments() {


            try(Connection conn = DBConnection.getConnection()){
                String selectStatement = "SELECT * from appointments";
                PreparedStatement preparedStatement = conn.prepareStatement(selectStatement);
                preparedStatement.execute();
                ResultSet result = preparedStatement.getResultSet();
                int index = 0;

                while(result.next()) {
                    int appointmentID = result.getInt("Appointment_ID");
                    String title = result.getString("Title");
                    String description = result.getString("Description");
                    String location = result.getString("Location");
                    String type = result.getString("Type");
                    Timestamp start = result.getTimestamp("Start");
                    Timestamp end = result.getTimestamp("End");
                    Timestamp dateCreated  = result.getTimestamp("Create_Date");
                    String createdBy = result.getString("Created_By");
                    Timestamp lastUpdate = result.getTimestamp("Last_Update");
                    String lastUpdatedBy = result.getString("Last_Updated_By");
                    int customerID = result.getInt("Customer_ID");
                    int userID = result.getInt("User_ID");
                    int contactID = result.getInt("Contact_ID");

                    appointments.add(new Appointment(appointmentID, title, description, location, type, start.toLocalDateTime(), end.toLocalDateTime(), dateCreated.toLocalDateTime(), createdBy, lastUpdate.toLocalDateTime(), lastUpdatedBy, customerID, userID, contactID));






                }

                preparedStatement.close();
                result.close();




            }
            catch (SQLException e) {



            }




        return appointments;
    }

    public static String addAppointment(String title, String description, String location, int contactID, int userID, int customerID, String type, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        try(Connection conn = DBConnection.getConnection()) {
            String insertStatement = "INSERT INTO appointments(Appointment_ID, Title, Description, Location, Type, Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID" +
                    " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            Timestamp timestamp = Timestamp.from(Instant.now());
            PreparedStatement preparedStatement = conn.prepareStatement(insertStatement);
            preparedStatement.setInt(1, 0);
            preparedStatement.setString(2, title);
            preparedStatement.setString(3,description);
            preparedStatement.setString(4, location);
            preparedStatement.setString(5, type);
            //figure out time conversion from localdate time to timestamp UTC.


        }
        catch(SQLException e) {
            e.printStackTrace();
        }

        return  "Appointment Added Successfully";



    }




}
