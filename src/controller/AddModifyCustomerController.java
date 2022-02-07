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
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AddModifyCustomerController implements Initializable {
    Stage stage;
    Parent scene;
    private Country selectedCountry;
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

    @FXML
    void onActionCancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Changes Not Saved");
        alert.setContentText("All unsaved changes will be lost, are you sure you want to cancel?");
        Optional<ButtonType> result = alert.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK) {

            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/mainView.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();

        }




    }

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

    @FXML
    void onCountrySelected(ActionEvent event) {

        selectedCountry = countryComboBox.getValue();
        FilteredList<FirstLevelDivision> countryDivisions =
                DAO.FirstLevelDivisionQuery.getFirstLevelDivisions()
                        .filtered(fl -> fl.getCountryID() == selectedCountry.getCountryID());
        firstLevelComboBox.setItems(countryDivisions);
        firstLevelComboBox.setPromptText("Select a division");
        firstLevelComboBox.setVisibleRowCount(5);




    }

    @FXML
    void onFirstLevelSelected(ActionEvent event) {

    }

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
                Country customerCountry = country;
                countryComboBox.getSelectionModel().select(customerCountry);
                break;
            }


        }
        ObservableList<FirstLevelDivision> firstLevelDivisionsList = DAO.FirstLevelDivisionQuery.getFirstLevelDivisions();
        for(FirstLevelDivision firstLevelDivision : firstLevelDivisionsList) {
            if(firstLevelDivision.getDivisionName().equals(customer.getDivisionName())) {
                FirstLevelDivision customerFirstLevelDivision = firstLevelDivision;
                firstLevelComboBox.getSelectionModel().select(customerFirstLevelDivision);
                break;
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        countries = DAO.CountryQuery.getDBCountries();
        countryComboBox.setItems(countries);
        countryComboBox.setPromptText("Select a Country");
        countryComboBox.setVisibleRowCount(3);
        firstLevelComboBox.setPromptText("Country Needed");


    }
}
