package controller;

import DAO.FirstLevelDivisionQuery;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;
import model.Country;
import model.Customer;
import DAO.*;

import java.io.IOException;
import java.net.IDN;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.PropertyPermission;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {
    Stage stage;
    Parent scene;
    private static ObservableList<Country> countriesList;

    @FXML
    private RadioButton allRadio;

    @FXML
    private TableView<Appointment> appointmentTable;

    @FXML
    private TableColumn<Appointment, Integer> aptAptIDCol;

    @FXML
    private TableColumn<Appointment, String> aptContactCol;

    @FXML
    private TableColumn<Appointment, Integer> aptCustIDCol;

    @FXML
    private TableColumn<Appointment, String> aptDescriptionCol;

    @FXML
    private TableColumn<Appointment, LocalDateTime> aptEndCol;

    @FXML
    private TableColumn<Appointment, String> aptLocationCol;

    @FXML
    private TableColumn<Appointment, LocalDateTime> aptStartCol;

    @FXML
    private TableColumn<Appointment, String > aptTitleCol;

    @FXML
    private TableColumn<Appointment, String> aptTypeCol;

    @FXML
    private TableColumn<Appointment, Integer> aptUserIDCol;


    @FXML
    private TableColumn<Customer, String> custCountryNameCol;





    @FXML
    private TableColumn<Customer, String> custDivCol;

    @FXML
    private TableColumn<Customer, Integer> custIDCol;



    @FXML
    private TableColumn<Customer, String> custNameCol;

    @FXML
    private TableColumn<Customer, String> custPhoneCol;

    @FXML
    private TableColumn<Customer, String> custPostCol;



    @FXML
    private TableColumn<Customer, String> custAddressCol;

    @FXML
    private TableView<Customer> customerTable;

    @FXML
    private RadioButton montRadio;

    @FXML
    private TextField textFieldSearchApt;

    @FXML
    private TextField textFieldSearchCustomer;

    @FXML
    private ToggleGroup viewOptionsToggleGroup;

    @FXML
    private RadioButton weekRadio;

    @FXML
    void onAddNewAppointment(ActionEvent event) {

    }

    @FXML
    void onAddNewCustomer(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AddModifyCustomer.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();

    }

    @FXML
    void onDeleteAppointment(ActionEvent event) {

    }

    @FXML
    void onDeleteCustomer(ActionEvent event) {

    }

    @FXML
    void onSearchAppointment(ActionEvent event) {

    }

    @FXML
    void onSearchCustomer(ActionEvent event) {

    }

    @FXML
    void onSelectAllRadio(ActionEvent event) {






    }

    @FXML
    void onSelectAppointmentReport(ActionEvent event) {

    }

    @FXML
    void onSelectExit(ActionEvent event) {

    }

    @FXML
    void onSelectMonthRadio(ActionEvent event) {

    }

    @FXML
    void onSelectWeekRadio(ActionEvent event) {

    }

    @FXML
    void onUpdateAppointment(ActionEvent event) {

    }

    @FXML
    void onUpdateCustomer(ActionEvent event) {

    }

    //NEEDED: Write method to check for appointments within 15 minutes of login and send an alert if needed.

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        appointmentTable.setItems(AppointmentQuery.getAppointments());
        aptAptIDCol.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        aptTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        aptDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        aptLocationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        aptTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        aptStartCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        aptEndCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        aptCustIDCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        aptUserIDCol.setCellValueFactory(new PropertyValueFactory<>("userID"));

        //Need to create lookup function that grabs contact name based on contact ID
        aptContactCol.setCellValueFactory(new PropertyValueFactory<>("contactID"));

        customerTable.setItems(CustomerQuery.getCustomers());
        custIDCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        custNameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        custAddressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        custPostCol.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        custPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));


        custCountryNameCol.setCellValueFactory(new PropertyValueFactory<>("countryName"));
        custDivCol.setCellValueFactory(new PropertyValueFactory<>("divisionName"));
















    }
}
