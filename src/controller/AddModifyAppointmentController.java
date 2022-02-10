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

public class AddModifyAppointmentController implements Initializable {
    Stage stage;
    Parent scene;
    private static ObservableList<Month> months = getMonths();
    private static ObservableList<Contact> contacts = DAO.ContactQuery.getDBContacts();
    private static ObservableList<User>  users = DAO.UserQuery.getDBUsers();
    private static ObservableList<Customer> customers = DAO.CustomerQuery.getCustomers();
    private static ObservableList<Integer> startDays = FXCollections.observableArrayList();
    private static  ObservableList<Integer> endDays = FXCollections.observableArrayList();
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
    private ComboBox<Integer> endDayCombo;

    @FXML
    private ComboBox<Integer> endHourCombo;

    @FXML
    private ComboBox<Integer> endMinuteCombo;

    @FXML
    private ComboBox<Month> endMonthCombo;

    @FXML
    private ComboBox<Integer> endYearCombo;

    @FXML
    private TextField locationField;

    @FXML
    private ComboBox<Integer> startDayCombo;

    @FXML
    private ComboBox<Integer> startHourCombo;

    @FXML
    private ComboBox<Integer> startMinuteCombo;

    @FXML
    private ComboBox<Month> startMonthCombo;

    @FXML
    private ComboBox<Integer> startYearCombo;

    @FXML
    private TextField titleField;

    @FXML
    private TextField typeField;

    @FXML
    private ComboBox<User> userCombo;

    private static void invalidDateAlert(String details) {
        Alert invalidDate = new Alert(Alert.AlertType.ERROR);
        invalidDate.setTitle("Invalid Date Entered");
        invalidDate.setContentText(details);
        invalidDate.showAndWait();
    }



    @FXML
    void onAddUpdate(ActionEvent event) {
        //Remember you need to check against eastern business hours, get Eastern start and end times and convert them to local time.
        try{

            //This section of the function extracts data from the form. If a field is empty, a runtime exception is thrown.
            //isBlank() is better than isEmpty()
            String title = titleField.getText();
            if(title.isEmpty()) throw new RuntimeException(invalidInputMessage);
            String description = descriptionField.getText();
            if(description.isEmpty())throw new RuntimeException(invalidInputMessage);
            String location = locationField.getText();
            if(location.isEmpty()) throw new RuntimeException(invalidInputMessage);
            String type = typeField.getText();
            if(type.isEmpty()) throw new RuntimeException(invalidInputMessage);
            int startYear = startYearCombo.getValue();
            if(startYearCombo.getValue() == null) throw new RuntimeException(invalidInputMessage);
            Month startMonth = startMonthCombo.getValue();
            if(startMonthCombo.getValue() == null) throw new RuntimeException(invalidInputMessage);
            int startDay = startDayCombo.getValue();
            if(startDayCombo.getValue() == null)throw new RuntimeException(invalidInputMessage);
            int startHour = startHourCombo.getValue();
            if(startHourCombo.getValue() == null) throw new RuntimeException(invalidInputMessage);
            int startMinute = startMinuteCombo.getValue();
            if(startMinuteCombo.getValue() == null) throw new RuntimeException(invalidInputMessage);
            LocalDateTime appointmentStartTime = LocalDateTime.of(startYear, startMonth, startDay, startHour, startMinute);
            LocalDateTime today = LocalDateTime.now();
            int endYear = endYearCombo.getValue();
            if(endYearCombo.getValue() == null) throw new RuntimeException(invalidInputMessage);
            Month endMonth = endMonthCombo.getValue();
            int endDay = endDayCombo.getValue();
            if(endDayCombo.getValue() == null) throw new RuntimeException(invalidInputMessage);
            int endHour = endHourCombo.getValue();
            if(endHourCombo.getValue() == null) throw new RuntimeException(invalidInputMessage);
            int endMinute = endMinuteCombo.getValue();
            if(endMinuteCombo.getValue() == null) throw new RuntimeException(invalidInputMessage);
            LocalDateTime appointmentEndTime = LocalDateTime.of(endYear, endMonth, endDay, endHour, endMinute);
            int customerID = customerCombo.getValue().getCustomerID();
            if(customerCombo.getValue() == null) throw new RuntimeException(invalidInputMessage);
            int userID = userCombo.getValue().getUserID();
            if(userCombo.getValue() == null) throw new RuntimeException(invalidInputMessage);
            int contactID = contactCombo.getValue().getContactID();
            if(contactCombo.getValue() == null) throw new RuntimeException(invalidInputMessage);
            //Make specific messages for each
            //The success string will be used to print a custom message in the alert that notifies the user that an update or addition has been successfully completed.
            String success;
            String saveAlertTitle;

            //Time Check logic starts here.

            //This condition checks that all new appointments are in the future
            //Maybe leave this one off for testers
            if(appointmentStartTime.isBefore(today)) {
                String appointmentDatePassed = "Your selected start date and time has already passed, please select a time in the future.";
                throw new RuntimeException(appointmentDatePassed);

            }
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

                            //First condition: new appointment start time is after or equal to the comparison appointment start time AND is before or equal to the comparison appointment end time. ;
                            if((appointmentStartTime.isAfter(compareStart) || appointmentStartTime.isEqual(compareStart)) && (appointmentStartTime.isBefore(compareEnd) || appointmentStartTime.isEqual(compareEnd))) {
                                throw new RuntimeException(appointmentOverlapMessage);

                            }
                            //Second condition: new appointment end time is after the comparison appointment start time(equal is ok) AND new appointment end time is before the comparison appointment end time (equal is ok).
                            else if (appointmentEndTime.isAfter(compareStart) && appointmentEndTime.isBefore(compareEnd)) {
                                throw new RuntimeException(appointmentOverlapMessage);

                            }
                            //Third condition: new appointment start time is before the comparison appointment start time and new appointment end time is after the comparison appointment end time.
                            //This condition is testing to see if an existing appointment occurs entirely within the time window of the thew appointment.
                            else if (appointmentStartTime.isBefore(compareStart) && appointmentEndTime.isAfter(compareEnd) ) {
                                throw new RuntimeException(appointmentOverlapMessage);
                            }




                        }
                    }

                }




                success =  AppointmentQuery.updateAppointment(modAppointmentID, title, description, location, contactID, userID, customerID, type, appointmentStartTime, appointmentEndTime);
                saveAlertTitle = "Appointment Updated!";
            }

            else {
                for (Appointment appointment : allAppointments) {
                    LocalDateTime compareStart = appointment.getStart();
                    LocalDateTime compareEnd = appointment.getEnd();
                    if (appointment.getCustomerID() == customerID) {
                        //Conditions to check for appointment overlap were developed from 3 conditions presented in C195 "When Appointments Collide" (08-29-2021) video presented by Mark Kinkead.
                        //However, I believe only two if statements are actually needed. Will need to try against test cases to confirm.
                        //First condition, new appointment start time is after or equal to the comparison appointment start time AND is before or equal to the comparison appointment end time. ;
                        if ((appointmentStartTime.isAfter(compareStart) || appointmentStartTime.isEqual(compareStart)) && (appointmentStartTime.isBefore(compareEnd) || appointmentStartTime.isEqual(compareEnd))) {
                            throw new RuntimeException(appointmentOverlapMessage);

                        }
                        //Second condition, new appointment end time is after the comparison appointment start time(equal is ok) AND new appointment end time is before the comparison appointment end time (equal is ok).
                        else if (appointmentEndTime.isAfter(compareStart) && appointmentEndTime.isBefore(compareEnd)) {
                            throw new RuntimeException(appointmentOverlapMessage);

                        }

                        //Third condition: new appointment start time is before the comparison appointment start time and new appointment end time is after the comparison appointment end time.
                        //This condition is testing to see if an existing appointment occurs entirely within the time window of the thew appointment.
                        else if (appointmentStartTime.isBefore(compareStart) && appointmentEndTime.isAfter(compareEnd) ) {
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
            e.printStackTrace();
        }


        //Write logic that start time must be before end time too!


    }

    @FXML
    void onCancel(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/mainView.fxml"));
        loader.load();
        MainViewController mainViewController = loader.getController();
        mainViewController.returnToMain(event);


    }

    //Method to find leap year sourced from https://stackoverflow.com/questions/1021324/java-code-for-calculating-leap-year
    public static boolean isLeapYear(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        return calendar.getActualMaximum(Calendar.DAY_OF_YEAR) == 366;
    }

   public static int getDays(Month month, int year) {
       boolean leapYearCheck = isLeapYear(year);
       return month.length(leapYearCheck);

   }

    @FXML
    void onSelectEndYear(ActionEvent event) {
        endMonthCombo.setItems(months);


    }


    @FXML
    void onSelectStartYear(ActionEvent event) {
        startMonthCombo.setItems(months);


    }

    @FXML
    void onSelectEndMonth(ActionEvent event) {
        try{
            Month endMonth = endMonthCombo.getValue();
            int endYear = endYearCombo.getValue();
            int endMonthLength = getDays(endMonth, endYear);
            endDays = numRangesForDates(1, endMonthLength + 1);
            endDayCombo.setItems(endDays);

        }
        catch(RuntimeException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Please Select Year and Month");
            alert.setContentText("You must select a year and a month before a day can be selected");
            alert.showAndWait();
            }

        }

//Populate the month combo boxes only when the year is selected.

    @FXML void onSelectStartMonth(ActionEvent event) {
        Month startMonth = startMonthCombo.getValue();
        int startYear = startYearCombo.getValue();
        int startMonthLength = getDays(startMonth, startYear);
        startDays = numRangesForDates(1, startMonthLength + 1);
        startDayCombo.setItems(startDays);


    }


    //Credit for using streams to return an ObservableList goes to: https://stackoverflow.com/questions/33849538/collectors-lambda-return-observable-list
    public static ObservableList<Integer> numRangesForDates(int first, int last) {
        return  IntStream.range(first, last).boxed().collect(Collectors.toCollection(FXCollections::observableArrayList));

    }

    public static ObservableList<Month> getMonths() {
        return Arrays.stream(Month.values()).collect(Collectors.toCollection(FXCollections::observableArrayList));
    }

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
        startMonthCombo.setItems(months);
        endMonthCombo.setItems(months);
        System.out.println(appointmentStartDate.getYear());
        startYearCombo.getSelectionModel().select((Integer)appointmentStartDate.getYear());
        int selectedStartMonthLength = getDays(appointmentStartDate.getMonth(), appointmentStartDate.getYear());
        startDays = numRangesForDates(1, selectedStartMonthLength + 1);
        startDayCombo.setItems(startDays);
        int selectedEndMonthLength = getDays(appointmentEndDate.getMonth(), appointmentEndDate.getYear());
        endDays = numRangesForDates(1, selectedEndMonthLength + 1);
        endDayCombo.setItems(endDays);
        startMonthCombo.setValue(appointment.getStart().getMonth());
        startDayCombo.getSelectionModel().select(appointmentStartDate.getDayOfMonth());
        startHourCombo.getSelectionModel().select(appointmentStartDate.getHour());
        startMinuteCombo.getSelectionModel().select(appointmentStartDate.getMinute());
        endYearCombo.getSelectionModel().select((Integer) appointmentEndDate.getYear());
        endMonthCombo.getSelectionModel().select(appointmentEndDate.getMonth());
        endDayCombo.getSelectionModel().select(appointmentEndDate.getDayOfMonth());
        endHourCombo.getSelectionModel().select(appointmentEndDate.getHour());
        endMinuteCombo.getSelectionModel().select(appointmentEndDate.getMinute());

        for(Contact contact : contacts) {
            if(appointment.getContactID() == contact.getContactID()) {
                contactCombo.getSelectionModel().select(contact);
                break;
            }
        }
        for(User user : users) {
            if(appointment.getUserID() == user.getUserID()) {
                userCombo.getSelectionModel().select(user);
                break;
            }
        }
        for(Customer customer : customers) {
            if(appointment.getCustomerID() == customer.getCustomerID()) {
                customerCombo.getSelectionModel().select(customer);
            }
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ObservableList<Integer> years = numRangesForDates(2015, 2050);
        startYearCombo.setItems(years);
        startYearCombo.setPromptText("Year");
        endYearCombo.setItems(years);
        endYearCombo.setPromptText("Year");
        ObservableList<Integer> hours = numRangesForDates(0, 23);
        startHourCombo.setItems(hours);
        startHourCombo.setPromptText("Hour");
        endHourCombo.setItems(hours);
        endHourCombo.setPromptText("Hour");

        ObservableList<Integer> minutes = numRangesForDates(0, 59);
        startMinuteCombo.setItems(minutes);
        startMinuteCombo.setPromptText("Minutes");
        endMinuteCombo.setItems(minutes);
        endMinuteCombo.setPromptText("Minutes");
        endDayCombo.setPromptText("Day");
        startDayCombo.setPromptText("Day");
        startMonthCombo.setPromptText("Month");
        endMonthCombo.setPromptText("Month");
        contactCombo.setItems(contacts);
        contactCombo.setPromptText("Contacts");
        userCombo.setItems(users);
        userCombo.setPromptText("Users");
        customerCombo.setItems(customers);
        customerCombo.setPromptText("Customers");










    }
}
