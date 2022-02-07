package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;

import java.sql.*;

public class AppointmentQuery {
    private static ObservableList<Appointment> appointments = FXCollections.observableArrayList();

    public static ObservableList<Appointment> getAppointments() {

        if(appointments.isEmpty()) {
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


        }

        return appointments;
    }


}
