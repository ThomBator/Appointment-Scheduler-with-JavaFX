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
/**
 This controller class manages the data flow and operations associated with the Main View defined by mainView.fxml.
 */
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


    /**This event handler method redirects the user to the Add Appointment view defined by AddModifyAppointment.fxml
     when the Add Appointment button is clicked.
     @param event listens for the Add Appointment button to be clicked.
     */
    @FXML
    void onAddNewAppointment(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AddModifyAppointment.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();


    }

    /**This event handler method redirects the user to the Add Customer view defined by AddModifyCustomer.fxml, when the Add Customer button is clicked.
     @param event listens for the Add Customer button to be clicked.
     */
    @FXML
    void onAddNewCustomer(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AddModifyCustomer.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();

    }



    /**This event handler method deletes the appointment that has been selected by the user in the appointment table view.
     If no appointment is selected, an exception is thrown. This triggers an alert prompting the user to select an appointment
     before clicking delete.
     @param event listens for Delete Appointment button to be clicked.
     */
    @FXML
    void onDeleteAppointment(ActionEvent event) {
        try {
            Appointment cancelledAppointment = appointmentTable.getSelectionModel().getSelectedItem();
            String cancelledAppointmentType = cancelledAppointment.getType();
            int appointmentID = cancelledAppointment.getAppointmentID();
            if(appointmentTable.getSelectionModel().getSelectedItem() == null) {
                throw new RuntimeException();
            }
            Alert confirmDelete = new Alert(Alert.AlertType.CONFIRMATION);
            confirmDelete.setTitle("Confirm Delete Appointment");
            confirmDelete.setContentText("Deleted appointments cannot be recovered. Click OK to proceed with deletion");
            Optional<ButtonType> result = confirmDelete.showAndWait();

            if(result.isPresent() && result.get() == ButtonType.OK) {
                AppointmentQuery.deleteAppointment(appointmentID);
                String cancelMessage = "An appointment with Appointment ID: " + appointmentID +" and type: " + cancelledAppointmentType;

                Alert deleteSuccess = new Alert(Alert.AlertType.INFORMATION);
                deleteSuccess.setTitle("Appointment Cancelled");
                deleteSuccess.setContentText(cancelMessage);
                deleteSuccess.showAndWait();
                appointmentsList = AppointmentQuery.getAppointments();
                appointmentTable.setItems(appointmentsList);
            }

        }
        catch(RuntimeException e) {
            Alert noSelection = new Alert(Alert.AlertType.ERROR);
            noSelection.setTitle("No appointment selected");
            noSelection.setContentText("Please select an appointment from the Appointment Table before clicking delete.");
            noSelection.showAndWait();
        }





    }

    /**This event handler method deletes the Customer that has been selected by the user in the customer table view.
     The method will also delete any appointments associated with that customer.
     If no customer is selected, an exception is thrown. This triggers an alert prompting the user to select a customer
     before clicking delete.
     @param event listens for Delete Customer button to be clicked.
     */
    @FXML
    void onDeleteCustomer(ActionEvent event) {

        try {

            Customer customerToDelete = customerTable.getSelectionModel().getSelectedItem();
            if(customerToDelete == null) {
                throw new RuntimeException();
            }

            Alert confirmDelete = new Alert(Alert.AlertType.CONFIRMATION);
            confirmDelete.setTitle("Confirm Delete Customer");
            confirmDelete.setContentText("Deleted customers cannot be recovered. Any appointments for this customer will also be deleted. Click OK to proceed with deletion.");
            Optional<ButtonType> result = confirmDelete.showAndWait();
            if(result.isPresent() && result.get() == ButtonType.OK) {
                for(Appointment customerAppointment : appointmentsList) {
                    if (customerAppointment.getCustomerID() == customerToDelete.getCustomerID()) {
                        AppointmentQuery.deleteAppointment(customerAppointment.getAppointmentID());

                    }
                }
                CustomerQuery.deleteCustomer(customerToDelete.getCustomerID());
                appointmentTable.setItems(AppointmentQuery.getAppointments());
                customerTable.setItems(CustomerQuery.getCustomers());
                Alert customerDeleted = new Alert(Alert.AlertType.INFORMATION);
                customerDeleted.setTitle("Customer deleted successfully");
                customerDeleted.setContentText("The selected customer has been deleted.");
            }



        }

        catch(RuntimeException e) {
            Alert noSelection = new Alert(Alert.AlertType.ERROR);
            noSelection.setTitle("No customer selected");
            noSelection.setContentText("Please select a customer from the Customer Table before clicking delete.");
            noSelection.showAndWait();
        }
    }



    /**This method populates the Appointment Table view with all appointments in the database when clicked.
     @param event listens for the All radio button to be toggled.
     */
    @FXML
    void onSelectAllRadio(ActionEvent event) {
        appointmentTable.setItems(appointmentsList);






    }
    /**This method redirects users to the Reports view defined by Reports.fxml.
     @param event listen for click of reports button.
     */
    @FXML
    void onSelectAppointmentReport(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/Reports.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();

    }


    /**This method allows users to exit the program.
     @param event listens for click of the exit button.
     */
    @FXML
    void onSelectExit(ActionEvent event) {
        System.exit(0);

    }

    /**
     LAMBDA USE: This method uses a lambda expression on a FilteredList to return a filtered list of Appointments
     that occur during the current calendar month. This approach requires fewer lines of code than would be required
     to achieve the same result with a loop and conditional statements.
     This method populates the Appointment Table with all appointments scheduled for the current calendar month.
     @param event listens for toggle of Month radio button.
     */
    @FXML
    void onSelectMonthRadio(ActionEvent event) {
        Month currentMonth = currentLocalDateTime.getMonth();
        FilteredList<Appointment> appointmentsThisMonth = appointmentsList.filtered(a -> a.getStart().getMonth() == currentMonth);
        appointmentTable.setItems(appointmentsThisMonth);

    }



    /**LAMBDA USE: This method uses a lambda expression on a FilteredList to return a filtered list of Appointments
     that occur within the following week of the current day. This approach requires fewer lines of code than would be required
     to achieve the same result with a loop and conditional statements.
     This method populates the Appointment Table with all appointments scheduled within 1 week of the current day.
     @param event listens for toggle of Month radio button.
     */
    @FXML
    void onSelectWeekRadio(ActionEvent event) {
        FilteredList<Appointment> appointmentsThisWeek = appointmentsList.filtered(a -> ChronoUnit.DAYS.between(a.getStart(), currentLocalDateTime) >= 0 && ChronoUnit.DAYS.between(a.getStart(), currentLocalDateTime) <= 7);
        appointmentTable.setItems(appointmentsThisWeek);

    }

    /**This method redirects users to the Update Appointment view defined by AddModifyAppointment.fxml.
     @param event listens for click of the Update Appointment button.
     */
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


    /**This method redirects users to the Update Appointment view defined by AddModifyCustomer.fxml.
     @param event listens for click of the Update Customer button.
     */
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

    /**This method was created to reduce code-repetition in the AddModifyCustomer viewsand AddModifyAppointment views. It can be called
     from those methods to return a user to the main view. This method also provides a generic warning that the user will lose any unsaved input if they cancel and return to main view.
     */
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


    /**This method is called on login to check if there are any appointments scheduled within 15 minutes of login time.
     */
    public static void checkAppointmentTimes() {
        Long timeUntilAppointment;
        String appointmentProximityAlert = "You have no appointments scheduled within 15 minutes of your login.";
        for(Appointment appointmentCheck : appointmentsList) {
            if(appointmentCheck.getUserID() == LoginScreenController.getCurrentUser().getUserID()) {
                //Do we need to find appointments that are currently going but ending within 15 minutes?
                LocalTime appointmentStart = appointmentCheck.getStart().toLocalTime();
                timeUntilAppointment = ChronoUnit.MINUTES.between(timeAtLogin, appointmentStart);



                if(timeUntilAppointment <= 15 && timeUntilAppointment >= 0) {
                    String appointmentDetails = " Appointment details: " + appointmentCheck.toString();
                    appointmentProximityAlert = "You have an appointment that begins in approximately " + timeUntilAppointment + " minutes."
                            + appointmentDetails;

                }
            }

        }

        Alert appointmentAlert = new Alert(Alert.AlertType.INFORMATION);
        appointmentAlert.setTitle("Appointment Notification");
        appointmentAlert.setContentText(appointmentProximityAlert);
        appointmentAlert.showAndWait();

    }

    /**
     This initialize method populates the Customer Table and the Appointment Table with all current customers and appointments in the database.*/
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
