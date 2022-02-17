package controller;

import DAO.AppointmentQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.*;

import javax.swing.text.DateFormatter;
import java.io.IOException;
import java.time.*;
import java.net.URL;
import java.sql.Time;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.logging.Filter;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 This controller class handles the data flow and operations relating to the AddModifyController view defined
 by AddModifyController.fxml.
 */

public class AddModifyAppointmentController implements Initializable {
    Stage stage;
    Parent scene;
    private static ObservableList<Contact> contacts = DAO.ContactQuery.getDBContacts();
    private static ObservableList<User>  users = DAO.UserQuery.getDBUsers();
    private static ObservableList<Customer> customers = DAO.CustomerQuery.getCustomers();
    private static ObservableList<Appointment> allAppointments = AppointmentQuery.getAppointments();
    private static boolean setToModify = false;
    private static int modAppointmentID;
    private static ZoneId easternZone = ZoneId.of("America/New_York");
    private static ZoneId systemZone = ZoneId.systemDefault();
    private static ZonedDateTime easternOpenTime = ZonedDateTime.of(2022, 1, 1, 8, 0, 0, 0, easternZone);
    private static ZonedDateTime easternCloseTime = ZonedDateTime.of(2022, 1, 1, 22, 0, 0, 0, easternZone);
    private static ZonedDateTime zonedLocalOpenTime =  easternOpenTime.withZoneSameInstant(systemZone);
    private static ZonedDateTime zonedLocalCloseTime = easternCloseTime.withZoneSameInstant(systemZone);
    private static LocalDateTime localOpenDate = zonedLocalOpenTime.toLocalDateTime();
    private static LocalDateTime localCloseDate = zonedLocalCloseTime.toLocalDateTime();
    private static LocalTime localOpenTime = localOpenDate.toLocalTime();
    private static LocalTime localCloseTime = localCloseDate.toLocalTime();
    //Start time values increment by 15 minutes between open time and 15 minutes before close time.
    private static ObservableList<LocalTime> appointmentStartTimes = FXCollections.observableArrayList();
    //End time values increment by 15 minutes from 15 minutes after the first start time and run until localCloseTime
    //See setAppointmentTimes() function for further details on the logic underlying the passed parameters.
    private static ObservableList<LocalTime> appointmentEndTimes = FXCollections.observableArrayList();
    private static String invalidInputMessage = "You must fill in all fields and make selections in all dropdown boxes. Please review your input before resubmitting.";




    @FXML
    private Button onAddUpdateButtonText;

    @FXML
    private Label addModifyTitle;
    @FXML
    private TextField appointmentIDField;

    @FXML
    private ComboBox<Contact> contactCombo;

    @FXML
    private ComboBox<Customer> customerCombo;

    @FXML
    private TextField descriptionField;

    @FXML
    private TextField locationField;



    @FXML
    private TextField titleField;

    @FXML
    private TextField typeField;

    @FXML
    private ComboBox<User> userCombo;


    @FXML
    private ComboBox<LocalTime> startTimeCombo;

    @FXML
    private ComboBox<LocalTime> endTimeCombo;

    @FXML
    private DatePicker appointmentDatePicker;



    /**
     This method is an event handler that either adds a new appointment or updates an existing appointment.
     The static class boolean variable setToModify defines whether an appointment is being updated(setToModify = true)
     or added(setToModify = false). This method also relies heavily on the LocalTime variables LocalOpenTime and LocalCloseTime.
     These were created by first creating ZonedDateTime variables representing Eastern Time business hours for opening and closing.
     These eastern specific times were subsequently converted to their equivalent Open and Close times in whatever the Local System time may be.
     This method includes many checks to ensure appointment end times are not before start times, and that appointments do not overlap with existing appointments
     in the system for the same Customer. The method also checks to ensure that all fields are filled in with the appropriate data. Alert boxes are triggered
     directing the user to change input if any of these various checks fail.
     @param event is an ActionEvent object that is created when the add/update appointment button is pressed.
     */
    @FXML
    void onAddUpdate(ActionEvent event) {
        //Remember you need to check against eastern business hours, get Eastern start and end times and convert them to local time.
        try{

            //This section of the function extracts data from the form. If a field is empty, a runtime exception is thrown.
            //isBlank() is better than isEmpty()
            String title = titleField.getText();
            if(title.isBlank()) throw new RuntimeException(invalidInputMessage);
            String description = descriptionField.getText();
            if(description.isBlank())throw new RuntimeException(invalidInputMessage);
            String location = locationField.getText();
            if(location.isBlank()) throw new RuntimeException(invalidInputMessage);
            String type = typeField.getText();
            if(type.isBlank()) throw new RuntimeException(invalidInputMessage);
            int customerID = customerCombo.getValue().getCustomerID();
            if(customerCombo.getValue() == null) throw new RuntimeException(invalidInputMessage);
            int userID = userCombo.getValue().getUserID();
            if(userCombo.getValue() == null) throw new RuntimeException(invalidInputMessage);
            int contactID = contactCombo.getValue().getContactID();
            if(contactCombo.getValue() == null) throw new RuntimeException(invalidInputMessage);
            LocalDate appointmentDateFromPicker = appointmentDatePicker.getValue();
            if(appointmentDatePicker.getValue() == null) throw new RuntimeException(invalidInputMessage);
            LocalTime startTimeFromCombo = startTimeCombo.getValue();
            if(startTimeCombo.getValue() == null) throw new RuntimeException(invalidInputMessage);
            LocalTime endTimeFromCombo = endTimeCombo.getValue();
            if(endTimeCombo.getValue() == null) throw new RuntimeException(invalidInputMessage);
            LocalDateTime appointmentStartTime = LocalDateTime.of(appointmentDateFromPicker, startTimeFromCombo);
            LocalDateTime appointmentEndTime = LocalDateTime.of(appointmentDateFromPicker, endTimeFromCombo);



            //Improvements: Make specific messages for each
            //The success string will be used to print a custom message in the alert that notifies the user that an update or addition has been successfully completed.
            String success;
            String saveAlertTitle;

            //Time Check logic starts here.

            //This condition checks that all new appointments are in the future
            //Maybe leave this one off for testers

            //This condition checks to make sure the end time isn't before the start time
            if(appointmentEndTime.isBefore(appointmentStartTime)) {
                String endTimeBeforeStart = "Your current end time is before your start time. Please select an end time that is after your start date and time.";
                throw new RuntimeException(endTimeBeforeStart);

            }

            //This String is used as the exception message for both of the following conditions.
            String outsideBusinessHours = "Appointment times must be scheduled within business hours. Please select a time between" + localOpenTime.format(DateTimeFormatter.ISO_LOCAL_TIME)
                    + " and " + localCloseTime.format(DateTimeFormatter.ISO_LOCAL_TIME);

            //These two conditions check to ensure that the start and end times are not outside Eastern Business Hours.
            if(appointmentStartTime.toLocalTime().isBefore(localOpenTime) || appointmentStartTime.toLocalTime().isAfter(localCloseTime)) {
                throw new RuntimeException(outsideBusinessHours);
            }
            if(appointmentEndTime.toLocalTime().isBefore(localOpenTime) || appointmentEndTime.toLocalTime().isAfter(localCloseTime)) {
                throw new RuntimeException(outsideBusinessHours);
            }



            //This String is used as the exception message in the two following appointment overlap checks.
            String appointmentOverlapMessage = "Another appointment exists for the selected Customer at this time. Please reschedule.";






            if(setToModify == true) {

                for(Appointment appointment : allAppointments) {
                    LocalDateTime compareStart = appointment.getStart();
                    LocalDateTime compareEnd = appointment.getEnd();
                    if(appointment.getAppointmentID() == modAppointmentID) {
                        continue;
                    }
                    else {
                        if(appointment.getCustomerID() == customerID) {
                            //Conditions to check for appointment overlap were developed from 3 conditions presented in C195 "When Appointments Collide" (08-29-2021) video presented by Mark Kinkead.
                            if((appointmentStartTime.isAfter(compareStart) || appointmentStartTime.isEqual(compareStart)) && (appointmentStartTime.isBefore(compareEnd))) {
                                throw new RuntimeException(appointmentOverlapMessage);
                            }
                            else if(appointmentEndTime.isAfter(compareStart) && (appointmentEndTime.isBefore(compareEnd) || appointmentEndTime.isEqual(compareEnd))) {
                                throw new RuntimeException(appointmentOverlapMessage);
                            }
                            else if(appointmentStartTime.isBefore(compareStart) && appointmentEndTime.isAfter(compareEnd)) {
                                throw new RuntimeException(appointmentOverlapMessage);
                            }





                        }
                    }

                }



                setToModify = false;
                success =  AppointmentQuery.updateAppointment(modAppointmentID, title, description, location, contactID, userID, customerID, type, appointmentStartTime, appointmentEndTime);
                saveAlertTitle = "Appointment Updated!";
            }

            else {
                for (Appointment appointment : allAppointments) {
                    LocalDateTime compareStart = appointment.getStart();
                    LocalDateTime compareEnd = appointment.getEnd();
                    if (appointment.getCustomerID() == customerID) {
                        if((appointmentStartTime.isAfter(compareStart) || appointmentStartTime.isEqual(compareStart)) && (appointmentStartTime.isBefore(compareEnd))) {
                            throw new RuntimeException(appointmentOverlapMessage);
                        }
                        else if(appointmentEndTime.isAfter(compareStart) && (appointmentEndTime.isBefore(compareEnd) || appointmentEndTime.isEqual(compareEnd))) {
                            throw new RuntimeException(appointmentOverlapMessage);
                        }
                        else if(appointmentStartTime.isBefore(compareStart) && appointmentEndTime.isAfter(compareEnd)) {
                            throw new RuntimeException(appointmentOverlapMessage);
                        }


                    }

                }

                success = AppointmentQuery.addAppointment(title, description, location, contactID, userID, customerID, type, appointmentStartTime, appointmentEndTime);
                saveAlertTitle = "New Appointment Added";



            }


                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(saveAlertTitle);
                alert.setContentText(success);
                alert.showAndWait();
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/mainView.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();

















        }
        catch(RuntimeException e) {
            Alert missingInput = new Alert(Alert.AlertType.ERROR);
            missingInput.setTitle("Form Submission Error");
            missingInput.setContentText(e.getMessage());
            missingInput.showAndWait();

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }


        //Write logic that start time must be before end time too!


    }


    /**
     This method sends the user back to the Main View when the cancel button is clicked.
     It calls a method in main that confirms the user wants to leave the current view, and the user
     must click OK to proceed.
     @param  event listens for click of Cancel button.
     */
    @FXML
    void onCancel(ActionEvent event) throws IOException {
      MainViewController mainViewController = new MainViewController();
      mainViewController.returnToMain(event);


    }



    /**
     This method is called in the MainView when an appointment is selected to be modified.
     The method includes statements to populate the Update Appointment form with all relevant
     data from the existing appointment.
     @param appointment is the appointment passed from MainView that the user wishes to modify.
     */

    public void modifyExistingAppointment(Appointment appointment) {
        
        setToModify = true;
        addModifyTitle.setText("Update Appointment");
        onAddUpdateButtonText.setText("Update");
        modAppointmentID = appointment.getAppointmentID();
        appointmentIDField.setText(String.valueOf(modAppointmentID));
        titleField.setText(appointment.getTitle());
        descriptionField.setText(appointment.getDescription());
        locationField.setText(appointment.getLocation());
        typeField.setText(appointment.getType());
        LocalDateTime appointmentStartDate = appointment.getStart();
        LocalDateTime appointmentEndDate = appointment.getEnd();
        LocalDate appointmentDate = appointmentStartDate.toLocalDate();
        LocalTime appointmentStart = appointmentStartDate.toLocalTime();
        LocalTime appointmentEnd = appointmentEndDate.toLocalTime();
        appointmentDatePicker.setValue(appointmentDate);
        startTimeCombo.setValue(appointmentStart);
        endTimeCombo.setValue(appointmentEnd);

        for(Contact contact : contacts) {
            if(appointment.getContactID() == contact.getContactID()) {
                contactCombo.setValue(contact);
                break;
            }
        }
        for(User user : users) {
            if(appointment.getUserID() == user.getUserID()) {
                userCombo.setValue(user);
                break;
            }
        }
        for(Customer customer : customers) {
            if(appointment.getCustomerID() == customer.getCustomerID()) {
                customerCombo.setValue(customer);
            }
        }

    }

    /**
     This view includes combo boxes that allow the user to select start and end times for their appointment.
     This method takes the localOpenTime (local equivalent of Eastern Opening Time) and localCloseTime(local equivalent of Eastern Closing Time)
     values and generates a list of LocalTimes in 15 minute increments between Opening and closing time.
     There are conditions that ensure the last possible appointment start time is 15 minutes before Eastern closing time,
     and the first possible start time is right at Eastern opening time.
    The first possible appointment end time is 15 minutes after Eastern opening time and the last possible end time is right at Eastern closing time. This method can be called to create the needed
     time lists for both the start and end time combo boxes.
     @param localOpenTime the user's local timezone version of Eastern timezone opening time
     @param localCloseTime the user's local timezone version of Eastern timezone closing time
     @return the list of times between open and close in 15 minute intervals.
     */
    public static ObservableList<LocalTime> setAppointmentTimes(LocalTime localOpenTime, LocalTime localCloseTime) {
        /*This function can be used to create a list of appointment start times or appointment end times.
        When creating start times, simply pass the static localOpenTime variable created at the start of this class.
        Start times should end with enough time for a final appointment before the business hours end. With that in mind the while loop
        below has been written to increment until the last start time is 15 minutes before close.
        To use this function to create end times, your parameters should be localOpenTime.plusMinuteS(15), localCloseTime.plusMinutes(15);
        This will ensure that the first end time will be 15 minutes after the first start time, and the last end time will be 15 minutes after the last end time.
         */

        LocalTime nextTime = localOpenTime;
        ObservableList<LocalTime> appointmentTimes = FXCollections.observableArrayList();
        appointmentTimes.add(localOpenTime);
        while(nextTime.isBefore(localCloseTime.minusMinutes(15))) {
            nextTime = nextTime.plusMinutes(15);
           appointmentTimes.add(nextTime);

        }
        return appointmentTimes;
    }

    /**
     This initialize method contains statements to set the combo boxes ranges of values so the
     user can add/modify all appointment details.
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        appointmentStartTimes = setAppointmentTimes(localOpenTime, localCloseTime);
        appointmentEndTimes = setAppointmentTimes(localOpenTime.plusMinutes(15), localCloseTime.plusMinutes(15));
        appointmentDatePicker.setPromptText("Select a Date");
        startTimeCombo.setItems(appointmentStartTimes);
        endTimeCombo.setItems(appointmentEndTimes);
        customerCombo.setItems(customers);
        userCombo.setItems(users);
        contactCombo.setItems(contacts);



















    }
}
