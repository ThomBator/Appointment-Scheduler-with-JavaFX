package DAO;

import com.mysql.cj.xdevapi.PreparableStatement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Country;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 This class handles all queries for Country objects and their associated records in the countries table of the database.
 */
public class CountryQuery {
    private static ObservableList<Country> countries = FXCollections.observableArrayList();
    /**
     The getDBCountries method uses a Prepared Statement object to execute an SQL select statement to the database.
     Ths returns a Result Set, which is then iterated through to take each country record and use the fields to create an object of the Country

     class.
     */
    public static ObservableList<Country> getDBCountries() {
        if (countries.isEmpty()) {
            try (Connection conn = DBConnection.getConnection()) {
                String selectStatement = "SELECT * FROM countries";

                PreparedStatement preparedStatement = conn.prepareStatement(selectStatement);
                preparedStatement.execute();
                ResultSet resultSet = preparedStatement.getResultSet();



                while (resultSet.next()) {
                    int countryID = resultSet.getInt("Country_ID");
                    String countryName = resultSet.getString("Country");
                    Timestamp dateCreated = resultSet.getTimestamp("Create_Date");
                    String createdBy = resultSet.getString("Created_By");
                    Timestamp lastUpdated = resultSet.getTimestamp("Last_Update");
                    String lastUpdatedBy = resultSet.getString("Last_Updated_By");
                    countries.add(new Country(countryID, countryName, dateCreated.toLocalDateTime(), createdBy, lastUpdated.toLocalDateTime(), lastUpdatedBy));


                }

                resultSet.close();
                preparedStatement.close();
                return countries;


            } catch (SQLException e) {
                System.out.println("Invalid input. Please check SQL statement.");

            }


        }


        return countries;
    }
}