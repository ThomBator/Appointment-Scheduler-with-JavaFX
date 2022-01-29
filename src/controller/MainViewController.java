package controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Appointment;
import model.Country;
import model.Customer;

import java.net.IDN;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {
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
    private TableColumn<Customer, String> custCreatorCol;

    @FXML
    private TableColumn<Customer, String> custDivCol;

    @FXML
    private TableColumn<Customer, Integer> custIDCol;

    @FXML
    private TableColumn<Customer, Timestamp> custLastUpdateCol;

    @FXML
    private TableColumn<Customer, String> custNameCol;

    @FXML
    private TableColumn<Customer, String> custPhoneCol;

    @FXML
    private TableColumn<Customer, String> custPostCol;

    @FXML
    private TableColumn<Customer, String> custUpdateByCol;

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
    void onAddNewCustomer(ActionEvent event) {

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


            customerTable.setItems(DAO.CustomerQuery.getCustomers());
            custIDCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
            custNameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
            custAddressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
            custPostCol.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
            custPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
            custCreatorCol.setCellValueFactory(new PropertyValueFactory<>("createdBy"));
            custLastUpdateCol.setCellValueFactory(new PropertyValueFactory<>("lastUpdated"));
            custUpdateByCol.setCellValueFactory(new PropertyValueFactory<>("lastUpdatedBy"));
            custDivCol.setCellValueFactory(new PropertyValueFactory<>("divisionId"));

            //I think I need all my times to be grabbed from DB as timestamp and set to DB as timestamp but live on in the model classes as LocalDateTime to get things working as described.









    }
}
