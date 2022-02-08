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

    @FXML
    void onActionCancel(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/mainView.fxml"));
        loader.load();
        MainViewController mainViewController = loader.getController();
        mainViewController.returnToMain(event);




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

    public static FilteredList<FirstLevelDivision> setFirstLevel(Country country) {
        FilteredList<FirstLevelDivision> countryDivisions =
                DAO.FirstLevelDivisionQuery.getFirstLevelDivisions()
                        .filtered(fl -> fl.getCountryID() == selectedCountry.getCountryID());
        return countryDivisions;

    }

    @FXML
    void onCountrySelected(ActionEvent event) {

        selectedCountry = countryComboBox.getValue();

        firstLevelComboBox.setItems(setFirstLevel(selectedCountry));
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        countries = DAO.CountryQuery.getDBCountries();
        countryComboBox.setItems(countries);
        countryComboBox.setPromptText("Select a Country");
        countryComboBox.setVisibleRowCount(3);
        firstLevelComboBox.setPromptText("Country Needed");


    }
}
