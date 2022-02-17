package controller;

import DAO.CustomerQuery;
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
import main.Main;
import model.Country;
import model.Customer;
import model.FirstLevelDivision;
import java.io.IOException;
import java.net.URL;
import java.util.InputMismatchException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 This controller class handles the data flow and operations associated with the view defined by AddModifyCustomer.fxml.

 */

public class AddModifyCustomerController implements Initializable {
    Stage stage;
    Parent scene;
    private static Country selectedCountry;
    private static boolean setToModify = false;
    private static int modID;
    private static ObservableList<Country> countries;

    @FXML
    private Button addModifyButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Label addModifyTitleLabel;

    @FXML
    private ComboBox<Country> countryComboBox;

    @FXML
    private TextField customerIDField;

    @FXML
    private TextField firstLastNameField;

    @FXML
    private ComboBox<FirstLevelDivision> firstLevelComboBox;

    @FXML
    private TextField phoneNumberField;

    @FXML
    private TextField postalCodeField;

    @FXML
    private TextField streetCityAddressField;

    /**
     The onActionCancel method is an event handler that allows users to cancel adding or updating a customer and returning to the main view.
     This method calls the returnToMain method in MainView, which allows for the same logic to handle cancellation in both the AddModifyCustomer and
     AddModifyController views.
     @param event listens for the cancel button to be clicked.
     */

    @FXML
    void onActionCancel(ActionEvent event) throws IOException {
        MainViewController mainViewController = new MainViewController();
        mainViewController.returnToMain(event);




    }

    /**
     The onActionSave method is an event handler that saves a new customer or updates an existing customer. The static boolean setToModify
     defines whether an update(setToModify = true) or add(setToModify = false) is to be performed. This method includes many statements to check
     that all fields have been filed out appropriately. If all fields are filled out, the method will either call the DAO.CustomerQuery.addCustomer() or
     DAO.CustomerQuery.updateCustomer(). These methods will respectively add a new record to the database or update a database record.
     */

    @FXML
    void onActionSave(ActionEvent event) {

        try {
            String saveAlertTitle;
            String name = firstLastNameField.getText();
            String address = streetCityAddressField.getText();
            String postalCode = postalCodeField.getText();
            String phoneNumber = phoneNumberField.getText();
            int divisionID = firstLevelComboBox.getValue().getDivisionID();
            String success;
            if(name.isEmpty()) throw new RuntimeException();
            if(address.isEmpty()) throw new RuntimeException();
            if(postalCode.isEmpty())throw new RuntimeException();
            if(phoneNumber.isEmpty()) throw new RuntimeException();
            if(firstLevelComboBox.getValue().getDivisionName().isEmpty()) throw new RuntimeException();

            if (setToModify == true) {

                success = DAO.CustomerQuery.updateCustomer(modID, name, address, phoneNumber, postalCode, divisionID);
                setToModify = false;
                saveAlertTitle = "Customer Updated";



            }
            else {
                success = DAO.CustomerQuery.addCustomer(name, address, phoneNumber, postalCode, divisionID);
                saveAlertTitle = "Customer Added";
            }
            System.out.println(success);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(saveAlertTitle);
            alert.setContentText(success);
            alert.showAndWait();

            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/mainView.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();





        }
        catch (RuntimeException e){

            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setContentText("All fields must be completed and country and division selections made to add/update a customer record");
            alert.showAndWait();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /** LAMBDA USE: This method uses a lambda on a FilteredList to select first level divisions that
     match the input Country's countryID. This approach requires less lines of logic than would be required to accomplish this same
     outcome with conditional statements and a loop.
     The setFirstLevel method creates a list of all First Level Divisions with a specific Country ID.
     @param country the Country whose ID must match all First Level Division Country IDs.
     @return the filtered list of First Level Divisions for the specified country.
     */

    public static FilteredList<FirstLevelDivision> setFirstLevel(Country country) {
        FilteredList<FirstLevelDivision> countryDivisions =
                DAO.FirstLevelDivisionQuery.getFirstLevelDivisions()
                        .filtered(fl -> fl.getCountryID() == selectedCountry.getCountryID());
        return countryDivisions;

    }
    /**
     The onCountrySelected method is an event handler that sets the firstLevelComboBox with First Level Divisions
     from the selected country.
     @param event listens for a country to be selected from the countryComboBox
     */
    @FXML
    void onCountrySelected(ActionEvent event) {

        selectedCountry = countryComboBox.getValue();

        firstLevelComboBox.setItems(setFirstLevel(selectedCountry));
        firstLevelComboBox.setPromptText("Select a division");
        firstLevelComboBox.setVisibleRowCount(5);





    }

    /**
     The modifyExistingCustomer method is called from the Main View when an existed customer is selected from Main's customer table view.
     This method takes the attributes of the selected customer and uses them to populate the update customer form with the customer's current data.
     This method also sets the setToModify boolean to true, so that the onActionSave method knows to update an existing database record rather than add a new one.
     @param customer is the customer to be modified.
     */

    public void modifyExistingCustomer (Customer customer) {
        setToModify = true;
        modID = customer.getCustomerID();
        customerIDField.setText(String.valueOf(modID));
        firstLastNameField.setText(customer.getCustomerName());
        streetCityAddressField.setText(customer.getAddress());
        phoneNumberField.setText(customer.getPhoneNumber());
        postalCodeField.setText(customer.getPostalCode());
        addModifyTitleLabel.setText("Update Customer Info");
        addModifyButton.setText("Update");





        for(Country country : countries) {
            if(country.getCountryName().equals(customer.getCountryName())) {

                countryComboBox.getSelectionModel().select(country);


                break;
            }


        }
        ObservableList<FirstLevelDivision> firstLevelDivisionsList = DAO.FirstLevelDivisionQuery.getFirstLevelDivisions();
        for(FirstLevelDivision firstLevelDivision : firstLevelDivisionsList) {
            if(firstLevelDivision.getDivisionName().equals(customer.getDivisionName())) {
                firstLevelComboBox.getSelectionModel().select(firstLevelDivision);
                break;
            }
            selectedCountry = countryComboBox.getValue();
            firstLevelComboBox.setItems(setFirstLevel(selectedCountry));
            firstLevelComboBox.setPromptText("Select a division");
            firstLevelComboBox.setVisibleRowCount(5);
        }
    }

    /**
     This initialize method includes statements to populate the countryComboBox items and prompt text,
     as well as the firstLevelComboBox prompt text.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        countries = DAO.CountryQuery.getDBCountries();
        countryComboBox.setItems(countries);
        countryComboBox.setPromptText("Select a Country");
        countryComboBox.setVisibleRowCount(3);
        firstLevelComboBox.setPromptText("Country Needed");


    }
}
