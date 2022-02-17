package model;

import javafx.collections.ObservableList;

import java.time.LocalDateTime;

/**
 The Customer Class is used to take customer records from the customers database table and create Java objects within the program.
 */

public class Customer {
    private int customerID;
    private String customerName;
    private String address;
    private String postalCode;
    private String phoneNumber;
    private LocalDateTime dateCreated;
    private String createdBy;
    private LocalDateTime lastUpdated;
    private String lastUpdatedBy;
    private int divisionId;
    private String divisionName;
    private String countryName;

    public Customer(int customerID, String customerName, String address, String postalCode, String phoneNumber, LocalDateTime dateCreated, String createdBy, LocalDateTime lastUpdated, String lastUpdatedBy, int divisionId) {
        this.customerID = customerID;
        this.customerName = customerName;
        this.address = address;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
        this.dateCreated = dateCreated;
        this.createdBy = createdBy;
        this.lastUpdated = lastUpdated;
        this.lastUpdatedBy = lastUpdatedBy;
        this.divisionId = divisionId;
        setDivisionName(divisionId);
        setCountryName(divisionId);
    }

    @Override
    public String toString() {
        return this.getCustomerName();
    }



    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public int getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    public String getDivisionName() {
        return divisionName;
    }

    /**
     In order to have a division name easily accessible by the various forms and table views, this method was created to take the divisionID provided by the database
     record and use it to find the associated division name in the first_level_divisions database table. Once found, the name is added to each object under the divisionName field.
     @param parameterDivisionID the ID of the division whose name needs to be provided.
     */
    public void setDivisionName(int parameterDivisionID) {
        ObservableList<FirstLevelDivision> divisions = DAO.FirstLevelDivisionQuery.getFirstLevelDivisions();
        for(int i = 0; i < divisions.size(); i++) {
            FirstLevelDivision division =  divisions.get(i);
            if( parameterDivisionID == division.getDivisionID()) {
                this.divisionName = division.getDivisionName();
                break;
            }
        }


    }

    public String getCountryName() {
        return countryName;
    }
    /**
     The setCountryName method was implemented for essentially the same reasons as the setDivisionName method above. The customer database only provides a divisionID for each record.
     This method takes the provided ID and uses it to find the associated country ID in the first_level_divisions table in the database. Then, the method uses that countryID to find the associated country name from the countries table.
     @param parameterDivisionID the division ID provided from the customer database record.
     */
    public void setCountryName(int parameterDivisionID) {
        ObservableList<FirstLevelDivision> divisions = DAO.FirstLevelDivisionQuery.getFirstLevelDivisions();
        ObservableList<Country> countries = DAO.CountryQuery.getDBCountries();
        int countryIDMatch = -1;
        for(FirstLevelDivision division : divisions) {

            if (parameterDivisionID == division.getDivisionID()) {
                countryIDMatch = division.getCountryID();
                break;
            }

        }

        for (Country country : countries) {
            if(countryIDMatch == country.getCountryID()) {
                countryName = country.getCountryName();
                break;
            }
        }

    }


}
