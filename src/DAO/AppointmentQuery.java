package DAO;

import controller.LoginScreenController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
/**
 This class handles all queries related to objects of the Appointment class and associated database records in the appointments table.
 */
public class AppointmentQuery {



    /**
     The getAppointments uses a select-all SQL statement passed through a PreparedStatement object to query the database and provide all appointment records.
     Those records collected in a ResultSet object. The ResultSet is looped through and all individual appointments are used to create objects of the Appointment class.
     An ObservableList list of all created Appointment objects is then returned by the method call.
     */
    public static ObservableList<Appointment> getAppointments() {

        ObservableList<Appointment> appointments = FXCollections.observableArrayList();


            try(Connection conn = DBConnection.getConnection()){

                String selectStatement = "SELECT * from appointments";
                PreparedStatement preparedStatement = conn.prepareStatement(selectStatement);
                preparedStatement.execute();
                ResultSet result = preparedStatement.getResultSet();



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
    /**
     The addAppointment method takes the individual data fields that compose an Appointment record as input. These records are then passed to the database using a PreparedStatement object.
     The specific field data for each new appointment are passed into the Prepared Statement using bind variables for greater security.
     The prepared statement is then executed to insert a new appointment into the appointments' table in the database.
     @param title the appointment title String
     @param description the appointment description String
     @param location the appointment location String
     @param contactID the appointment contact ID int
     @param userID the appointment userID int
     @param customerID the appointment customerID int
     @param type the appointment type String
     @param startDateTime  the appointment start date and time LocalDateTime
     @param endDateTime the appointment end date and time LocalDateTime

     */
    public static String addAppointment(String title, String description, String location, int contactID, int userID, int customerID, String type, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        try(Connection conn = DBConnection.getConnection()) {
            String insertStatement = "INSERT INTO appointments (Appointment_ID, Title, Description, Location, Type, Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            Timestamp timestamp = Timestamp.from(Instant.now());
            PreparedStatement preparedStatement = conn.prepareStatement(insertStatement);
            preparedStatement.setInt(1, 0);
            preparedStatement.setString(2, title);
            preparedStatement.setString(3,description);
            preparedStatement.setString(4, location);
            preparedStatement.setString(5, type);
            preparedStatement.setTimestamp(6, Timestamp.valueOf(startDateTime));
            preparedStatement.setTimestamp(7, Timestamp.valueOf(endDateTime));
            preparedStatement.setTimestamp(8, timestamp);
            preparedStatement.setString(9, LoginScreenController.getCurrentUser().getUserName());
            preparedStatement.setTimestamp(10, timestamp);
            preparedStatement.setString(11, LoginScreenController.getCurrentUser().getUserName());
            preparedStatement.setInt(12, customerID);
            preparedStatement.setInt(13, userID);
            preparedStatement.setInt(14, contactID);
            preparedStatement.execute();
            preparedStatement.close();


        }
        catch(SQLException e) {
            e.printStackTrace();
        }

        return  "Appointment Added Successfully";



    }

    /**
     The updateAppointment method takes the individual data fields that compose an Appointment record as input. These records are then passed to the database using a PreparedStatement object to update an existing appointment in the database.
     The existing appointment's appointment ID is passed as a parameter to make sure the correct appointment is updated.
     The specific field data for each new appointment are passed into the Prepared Statement using bind variables for greater security.
     @param appointment_ID the ID of the appointment to be updated
     @param title the appointment title String
     @param description the appointment description String
     @param location the appointment location String
     @param contactID the appointment contact ID int
     @param userID the appointment userID int
     @param customerID the appointment customerID int
     @param type the appointment type String
     @param startDateTime  the appointment start date and time LocalDateTime
     @param endDateTime the appointment end date and time LocalDateTime

     */
    public static String updateAppointment (int appointment_ID, String title, String description, String location, int contactID, int userID, int customerID, String type, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        try(Connection conn = DBConnection.getConnection()) {
            String updateStatement = "UPDATE appointments SET  Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Last_Update = ?, Last_Updated_By = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";
            Timestamp timestamp = Timestamp.from(Instant.now());
            PreparedStatement preparedStatement = conn.prepareStatement(updateStatement);

            preparedStatement.setString(1, title);
            preparedStatement.setString(2,description);
            preparedStatement.setString(3, location);
            preparedStatement.setString(4, type);
            preparedStatement.setTimestamp(5, Timestamp.valueOf(startDateTime));
            preparedStatement.setTimestamp(6, Timestamp.valueOf(endDateTime));
            preparedStatement.setTimestamp(7, timestamp);
            preparedStatement.setString(8, LoginScreenController.getCurrentUser().getUserName());
            preparedStatement.setInt(9, customerID);
            preparedStatement.setInt(10, userID);
            preparedStatement.setInt(11, contactID);
            preparedStatement.setInt(12, appointment_ID);
            preparedStatement.execute();
            preparedStatement.close();


        }
        catch(SQLException e) {
            e.printStackTrace();
        }

        return  "Appointment Updated Successfully";



    }
    /**
     This method used a prepared statement to send a delete SQL statement to the database.
     The prepared statement uses a bind variable to insert the target appointment ID for greater security.
     @param appointmentID the ID of the appointment to be deleted.
     */
    public static void deleteAppointment(int appointmentID) {
        try(Connection connection = DBConnection.getConnection()) {
            String deleteStatement = "DELETE FROM appointments WHERE Appointment_ID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteStatement);
            preparedStatement.setInt(1, appointmentID);
           preparedStatement.execute();

            preparedStatement.close();

        }
        catch (SQLException e) {
            e.printStackTrace();
        }

    }





}
