package controller;

import DAO.AppointmentQuery;
import DAO.ContactQuery;
import DAO.CustomerQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;
import model.Contact;
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.util.Arrays;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ReportsController implements Initializable {
    Stage stage;
    Parent scene;
    private static ObservableList<Contact> contactList = ContactQuery.getDBContacts();
    private static ObservableList<Customer> customerList = CustomerQuery.getCustomers();
    private static ObservableList<Appointment> allAppointments = AppointmentQuery.getAppointments();
    private static ObservableList<Month> allMonths = Arrays.stream(Month.values()).collect(Collectors.toCollection(FXCollections::observableArrayList));

    @FXML
    private Label CustomerTotalLabel;

    @FXML
    private Label MonthTotalLabel;

    @FXML
    private Label TypeTotalLabel;

    @FXML
    private TableColumn<Appointment, Integer> appointmentIDColumn;

    @FXML
    private ComboBox<Contact> contactCombo;

    @FXML
    private ComboBox<Customer> customerCombo;

    @FXML
    private Button clearSelections;

    @FXML
    private Button mainView;


    @FXML
    private TableColumn<Appointment, Integer> customerIDColumn;

    @FXML
    private TableColumn<Appointment, String> descriptionColumn;

    @FXML
    private TableColumn<Appointment, LocalDateTime> endColumn;

    @FXML
    private ComboBox<Month> monthCombo;

    @FXML
    private TableColumn<Appointment, LocalDateTime> startColumn;

    @FXML
    private TableColumn<Appointment, String> titleColumn;

    @FXML
    private TableColumn<Appointment, String> typeColumn;


    @FXML
    private ComboBox<String> typeCombo;

    @FXML
    private TableView<Appointment> aptByContactTableView;


    @FXML
    void onSelectContact(ActionEvent event){
        Contact selectedContact = contactCombo.getValue();
        FilteredList<Appointment> appointmentsByContact = allAppointments.filtered(a -> a.getContactID() == selectedContact.getContactID());
        aptByContactTableView.setItems(appointmentsByContact);
        appointmentIDColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        startColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
        endColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
        customerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
    }

    @FXML
    void onSelectCustomer(ActionEvent event) {
        Customer selectedCustomer = customerCombo.getValue();
        FilteredList<Appointment> appointmentsForSelectedCustomer = allAppointments.filtered(a -> a.getCustomerID() == selectedCustomer.getCustomerID());
        int totalByCustomer = appointmentsForSelectedCustomer.size();

       CustomerTotalLabel.setText("Total: " + totalByCustomer);
        }


    @FXML
    void onSelectMonth(ActionEvent event) {
        Month selectedMonth = monthCombo.getValue();
        Year currentYear = Year.of(LocalDateTime.now().getYear());
        FilteredList<Appointment> appointmentsWithSelectedMonth = allAppointments.filtered(a -> a.getStart().getMonth().equals(selectedMonth) && Year.of(a.getStart().getYear()).equals(currentYear));
        int totalByMonth = appointmentsWithSelectedMonth.size();
        MonthTotalLabel.setText("Total: " + totalByMonth);


    }

    @FXML
    void onSelectType(ActionEvent event) {
        String selectedType= typeCombo.getValue();
        FilteredList<Appointment> appointmentsWithSelectedType = allAppointments.filtered(a -> a.getType().equals(selectedType));
        int totalByType = appointmentsWithSelectedType.size();
        TypeTotalLabel.setText("Total: "+ totalByType);

    }


    @FXML
    void onReturnToMain(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/mainView.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    public static ObservableList<String> typeList() {
        ObservableList<String> types = FXCollections.observableArrayList();
        for(Appointment appointment : allAppointments) {
            if (!types.contains(appointment.getType())) {
                types.add(appointment.getType());
            }
        }
        return types;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customerCombo.setItems(customerList);
        customerCombo.setPromptText("Select Customer");
        contactCombo.setItems(contactList);
        contactCombo.setPromptText("Select Contact");
        monthCombo.setItems(allMonths);
        monthCombo.setPromptText("Select Month");
        typeCombo.setItems(typeList());
        typeCombo.setPromptText("Select Apt. Type");




    }
}
