package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Contact;
import model.Customer;
import model.User;
import java.time.Month;
import java.net.URL;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AddModifyAppointmentController implements Initializable {
    Stage stage;
    Parent scene;


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

    }

    @FXML
    void onCancel(ActionEvent event) {

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
   //Problem: If you select month first and year second, the days box does not get triggered.
    @FXML
    void onSelectEndMonth(ActionEvent event) {
        try{
            Month endMonth = endMonthCombo.getValue();
            int endYear = endYearCombo.getValue();
            int endMonthLength = getDays(endMonth, endYear);
            ObservableList<Integer> endDays = numRangesForDates(1, endMonthLength + 1);
            endDayCombo.setItems(endDays);
            endDayCombo.setPromptText("Day");
        }
        catch(RuntimeException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Please Select Year and Month");
            alert.setContentText("You must select a year and a month before a day can be selected");
            alert.showAndWait();
            }

        }



    @FXML void onSelectStartMonth(ActionEvent event) {
        Month startMonth = startMonthCombo.getValue();
        int startYear = startYearCombo.getValue();
        int startMonthLength = getDays(startMonth, startYear);
        ObservableList<Integer> startDays = numRangesForDates(1, startMonthLength + 1);
        startDayCombo.setItems(startDays);
        startDayCombo.setPromptText("Day");

    }


    //Credit for using streams to return an ObservableList goes to: https://stackoverflow.com/questions/33849538/collectors-lambda-return-observable-list
    public static ObservableList<Integer> numRangesForDates(int first, int last) {
        return  IntStream.range(first, last).boxed().collect(Collectors.toCollection(FXCollections::observableArrayList));
    }
    public static ObservableList<Month> getMonths() {
        return Arrays.stream(Month.values()).collect(Collectors.toCollection(FXCollections::observableArrayList));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Integer> years = numRangesForDates(2022, 2050);
        startYearCombo.setItems(years);
        startYearCombo.setPromptText("Year");
        endYearCombo.setItems(years);
        endYearCombo.setPromptText("Year");
        ObservableList<Integer> hours = numRangesForDates(0, 23);
        startHourCombo.setItems(hours);
        startHourCombo.setPromptText("Hour");
        endHourCombo.setItems(hours);
        endHourCombo.setPromptText("Hour");
        ObservableList<Month> months = getMonths();
        ObservableList<Integer> minutes = numRangesForDates(0, 59);
        startMinuteCombo.setItems(minutes);
        startMinuteCombo.setPromptText("Minutes");
        endMinuteCombo.setItems(minutes);
        endMinuteCombo.setPromptText("Minutes");
        startMonthCombo.setItems(months);
        startMonthCombo.setPromptText("Month");
        endMonthCombo.setItems(months);
        endMonthCombo.setPromptText("Month");







    }
}
