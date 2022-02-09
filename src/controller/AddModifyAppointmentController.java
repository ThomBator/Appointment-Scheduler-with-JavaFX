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

import java.io.IOException;
import java.time.*;
import java.net.URL;
import java.sql.Time;
import java.time.Month;
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
    private static boolean setToModify = false;
    private static int modAppointmentID;
    private static ZoneId easternZone = ZoneId.of("America/New_York");
    private static ZoneId systemZone = ZoneId.systemDefault();
    private static ZonedDateTime easternOpenTime = ZonedDateTime.of(2022, 1, 1, 8, 0, 0, 0, easternZone);
    private static ZonedDateTime easternCloseTime = ZonedDateTime.of(2022, 1, 1, 22, 0, 0, 0, easternZone);
    private static ZonedDateTime zonedLocalOpenTime =  easternOpenTime.withZoneSameInstant(systemZone);
    private static ZonedDateTime zonedLocalCloseTime = easternCloseTime.withZoneSameInstant(systemZone);
    private static LocalDateTime localOpenTime = zonedLocalOpenTime.toLocalDateTime();
    private static LocalDateTime localCloseTime = zonedLocalCloseTime.toLocalDateTime();



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

    @FXML
    void onAddUpdate(ActionEvent event) {
        //Remember you need to check against eastern business hours, get Eastern start and end times and convert them to local time.
        try{


            String title = titleField.getText();
            String description = descriptionField.getText();
            String location = locationField.getText();
            String type = typeField.getText();
            int startYear = startYearCombo.getValue();
            Month startMonth = startMonthCombo.getValue();
            int startDay = startDayCombo.getValue();
            int startHour = startHourCombo.getValue();
            int startMinute = startMinuteCombo.getValue();
            LocalDateTime appointmentStartTime = LocalDateTime.of(startYear, startMonth, startDay, startHour, startMinute);
            LocalDateTime today = LocalDateTime.now();
            int endYear = endYearCombo.getValue();
            Month endMonth = endMonthCombo.getValue();
            int endDay = endDayCombo.getValue();
            int endHour = endHourCombo.getValue();
            int endMinute = endMinuteCombo.getValue();
            LocalDateTime appointmentEndTime = LocalDateTime.of(endYear, endMonth, endDay, endHour, endMinute);
            int customerID = customerCombo.getValue().getCustomerID();
            int userID = userCombo.getValue().getUserID();
            int contactID = contactCombo.getValue().getContactID();
            String success;
            if(setToModify == true) {
                success =  AppointmentQuery.updateAppointment(modAppointmentID, title, description, location, contactID, userID, customerID, type, appointmentStartTime, appointmentEndTime);
            }

            else {
                success =   AppointmentQuery.addAppointment(title, description, location, contactID, userID, customerID, type, appointmentStartTime, appointmentEndTime);
            }

            System.out.println(success);














        }
        catch(RuntimeException e) {

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
        startMonthCombo.getSelectionModel().select(appointmentStartDate.getMonth());
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
