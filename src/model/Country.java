package model;

import java.time.LocalDateTime;

/**
 The Country Class is used to take country records from the countries database table and create Java objects within the program.
 */

public class Country {
    private int countryID;
    private String countryName;
    private LocalDateTime dateCreated;
    private String createdBy;
    private LocalDateTime lastUpdate;
    private String lastUpdateBy;


    public Country() {
    }

    public Country(int countryID, String countryName, LocalDateTime dateCreated, String createdBy, LocalDateTime lastUpdate, String lastUpdateBy) {
        this.countryID = countryID;
        this.countryName = countryName;
        this.dateCreated = dateCreated;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdateBy = lastUpdateBy;
    }

    @Override
    public String toString() {
        return countryName;
    }

    public int getCountryID() {
        return countryID;
    }

    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
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

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }
}
