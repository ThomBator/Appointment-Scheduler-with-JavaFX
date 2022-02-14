package DAO;

import controller.LoginScreenController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;

public class AppointmentQuery {




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
