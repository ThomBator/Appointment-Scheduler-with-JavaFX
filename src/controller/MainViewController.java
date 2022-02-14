package controller;

import DAO.FirstLevelDivisionQuery;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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
import java.time.LocalTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Optional;
import java.util.PropertyPermission;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {
    Stage stage;
    Parent scene;
    private static ObservableList<Country> countriesList;
    private static ObservableList<Customer>customersList;
    private static ObservableList<Appointment> appointmentsList;
    public static  LocalTime timeAtLogin = LocalTime.now();
    public static LocalDateTime currentLocalDateTime = LocalDateTime.now();

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
    private RadioButton monthRadio;

    @FXML
    private TextField textFieldSearchApt;

    @FXML
    private TextField textFieldSearchCustomer;

    @FXML
    private ToggleGroup viewOptionsToggleGroup;

    @FXML
    private RadioButton weekRadio;

    @FXML
    void onAddNewAppointment(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AddModifyAppointment.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();


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
        int appointmentID = appointmentTable.getSelectionModel().getSelectedItem().getAppointmentID();
        Alert confirmDelete = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDelete.setTitle("Confirm Delete Appointment");
        confirmDelete.setContentText("Deleted appointments cannot be recovered. Click OK to proceed with deletion");
        Optional<ButtonType> result = confirmDelete.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK){
            AppointmentQuery.deleteAppointment(appointmentID);

            Alert deleteSuccess = new Alert(Alert.AlertType.INFORMATION);
            deleteSuccess.setTitle("Appointment Deleted");
            deleteSuccess.setContentText("Appointment Deleted Successfully");
            deleteSuccess.showAndWait();
            appointmentsList = AppointmentQuery.getAppointments();
            appointmentTable.setItems(appointmentsList);

        }




    }

    @FXML
    void onDeleteCustomer(ActionEvent event) {
        try {
            Customer customerToDelete = customerTable.getSelectionModel().getSelectedItem();

            Alert confirmDelete = new Alert(Alert.AlertType.CONFIRMATION);
            confirmDelete.setTitle("Confirm Delete Customer");
            confirmDelete.setContentText("Deleted customers cannot be recovered. Click OK to proceed with deletion");
            Optional<ButtonType> result = confirmDelete.showAndWait();
            if(result.isPresent() && result.get() == ButtonType.OK) {
                for(Appointment customerAppointments : appointmentsList) {
                    if (customerAppointments.getCustomerID() == customerToDelete.getCustomerID()) {
                        throw new RuntimeException();

                    }
                }
                CustomerQuery.deleteCustomer(customerToDelete.getCustomerID());
                Alert customerDeleted = new Alert(Alert.AlertType.INFORMATION);
                customerDeleted.setTitle("Customer deleted successfully");
                customerDeleted.setContentText("The selected customer has been deleted.");
            }

        }
        catch(RuntimeException e) {
            Alert cannotDeleteCustomer = new Alert(Alert.AlertType.ERROR);
            cannotDeleteCustomer.setTitle("Customer Can't Be Deleted");
            cannotDeleteCustomer.setContentText("This customer still has appointments booked in the system. Please delete all appointments for this customer before proceeding.");
            cannotDeleteCustomer.showAndWait();

        }

    }


    @FXML
    void onSelectAllRadio(ActionEvent event) {
        appointmentTable.setItems(appointmentsList);






    }

    @FXML
    void onSelectAppointmentReport(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/Reports.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();

    }

    @FXML
    void onSelectExit(ActionEvent event) {
        System.exit(0);

    }

    @FXML
    void onSelectMonthRadio(ActionEvent event) {
        Month currentMonth = currentLocalDateTime.getMonth();
        FilteredList<Appointment> appointmentsThisMonth = appointmentsList.filtered(a -> a.getStart().getMonth() == currentMonth);
        appointmentTable.setItems(appointmentsThisMonth);

        }




    @FXML
    void onSelectWeekRadio(ActionEvent event) {
    FilteredList<Appointment> appointmentsThisWeek = appointmentsList.filtered(a -> Math.abs(ChronoUnit.DAYS.between(a.getStart(), currentLocalDateTime)) <= 7);
    appointmentTable.setItems(appointmentsThisWeek);

    }

    @FXML
    void onUpdateAppointment(ActionEvent event) {
        try {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/AddModifyAppointment.fxml"));
        loader.load();
        AddModifyAppointmentController addModifyAppointmentController = loader.getController();
        Appointment appointment = appointmentTable.getSelectionModel().getSelectedItem();
        addModifyAppointmentController.modifyExistingAppointment(appointment);
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();
        }


    catch(RuntimeException e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("No Appointment Selected");
        alert.setContentText("You must select an appointment from the appointment table to update an appointment record.");
        alert.showAndWait();
        e.printStackTrace();

    }
    catch (IOException e) {
        e.printStackTrace();

    }


    }

    @FXML
    void onUpdateCustomer(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/AddModifyCustomer.fxml"));
            loader.load();
            AddModifyCustomerController customerController = loader.getController();
            Customer customer = customerTable.getSelectionModel().getSelectedItem();
            customerController.modifyExistingCustomer(customer);
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }

        catch(RuntimeException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No Customer Selected");
            alert.setContentText("You must select a customer from the customer table to update a customer record.");
            alert.showAndWait();

        }
        catch (IOException e) {
            e.printStackTrace();

        }

    }


    public void returnToMain(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Changes Not Saved");
        alert.setContentText("All unsaved changes will be lost and you will be returned to the Main dashboard. Press OK to continue.");
        Optional<ButtonType> result = alert.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK) {

            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/mainView.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();

        }

    }

    public static void checkAppointmentTimes() {
        Long timeUntilAppointment;
        String appointmentProximityAlert = "You have no appointments scheduled within 15 minutes of your login.";
        for(Appointment appointmentCheck : appointmentsList) {
            if(appointmentCheck.getUserID() == LoginScreenController.getCurrentUser().getUserID()) {
                //Do we need to find appointments that are currently going but ending within 15 minutes?
                LocalTime appointmentStart = appointmentCheck.getStart().toLocalTime();
                timeUntilAppointment = ChronoUnit.MINUTES.between(timeAtLogin, appointmentStart);
                long timeUntilAppointmentPositive = Math.abs(timeUntilAppointment);


                if(timeUntilAppointmentPositive <= 15) {
                    String appointmentDetails = "Appointment details: " + appointmentCheck.toString();
                    appointmentProximityAlert = "You have an appointment that begins in approximately " + timeUntilAppointmentPositive + " minutes."
                            + appointmentDetails;
                    if(timeUntilAppointment < 0) {
                        appointmentProximityAlert = "You have an appointment that started approximately " + timeUntilAppointmentPositive + " minutes ago. "
                                + appointmentDetails;
                    }



                }
            }

        }

        Alert appointmentAlert = new Alert(Alert.AlertType.INFORMATION);
        appointmentAlert.setTitle("Appointment Notification");
        appointmentAlert.setContentText(appointmentProximityAlert);
        appointmentAlert.showAndWait();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        appointmentsList = DAO.AppointmentQuery.getAppointments();
        appointmentTable.setItems(appointmentsList);
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
        customersList = CustomerQuery.getCustomers();
        customerTable.setItems(customersList);
        custIDCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        custNameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        custAddressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        custPostCol.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        custPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        custCountryNameCol.setCellValueFactory(new PropertyValueFactory<>("countryName"));
        custDivCol.setCellValueFactory(new PropertyValueFactory<>("divisionName"));















    }
}
