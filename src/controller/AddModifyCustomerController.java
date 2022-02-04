package controller;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Country;
import model.FirstLevelDivision;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AddModifyCustomerController implements Initializable {
    Stage stage;
    Parent scene;
    private Country selectedCountry;

    @FXML
    private Button addModifyButton;

    @FXML
    private Button cancelButton;

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
    void onActionCancel(ActionEvent event) {

    }

    @FXML
    void onActionSave(ActionEvent event) {

        try {
            String name = firstLastNameField.getText();
            String address = streetCityAddressField.getText();
            String postalCode = postalCodeField.getText();
            String phoneNumber = phoneNumberField.getText();
            int divisionID = firstLevelComboBox.getValue().getDivisionID();
            String success = DAO.CustomerQuery.addCustomer(name, address, phoneNumber, postalCode, divisionID);
            System.out.println(success);

        }
        catch (Exception e){

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
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ObservableList<Country> countries = DAO.CountryQuery.getDBCountries();
        countryComboBox.setItems(countries);
        countryComboBox.setPromptText("Select a Country");
        countryComboBox.setVisibleRowCount(3);
        firstLevelComboBox.setPromptText("Country Needed");


    }
}
