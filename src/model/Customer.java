package model;

import javafx.collections.ObservableList;

import java.time.LocalDateTime;

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
