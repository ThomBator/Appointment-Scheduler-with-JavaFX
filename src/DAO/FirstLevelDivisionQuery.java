package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.FirstLevelDivision;
import model.User;

import java.sql.*;

public class FirstLevelDivisionQuery {
    public static ObservableList<FirstLevelDivision> firstLevelDivisions = FXCollections.observableArrayList();

    public static ObservableList<FirstLevelDivision> getFirstLevelDivisions() {
        if(firstLevelDivisions.isEmpty()) {
            try(Connection conn = DBConnection.getConnection()) {
                String selectStatement = "SELECT * FROM first_level_divisions";

                PreparedStatement preparedStatement = conn.prepareStatement(selectStatement);
                preparedStatement.execute();
                ResultSet resultSet = preparedStatement.getResultSet();


                while(resultSet.next()) {
                    int divisionID = resultSet.getInt("Division_ID");
                    String division = resultSet.getString("Division");
                    Timestamp createDate = resultSet.getTimestamp("Create_Date");
                    String createdBy = resultSet.getString("Created_By");
                    Timestamp lastUpdate = resultSet.getTimestamp("Last_Update");
                    String lastUpdatedBy = resultSet.getString("Last_Updated_By");
                    int countryID = resultSet.getInt("Country_ID");

                    firstLevelDivisions.add(new FirstLevelDivision(divisionID, division, createDate.toLocalDateTime(), createdBy, lastUpdate.toLocalDateTime(), lastUpdatedBy, countryID));

                }

                resultSet.close();
                preparedStatement.close();
                return firstLevelDivisions;

            }
            catch (SQLException e) {
                e.printStackTrace();
            }


        }


        return firstLevelDivisions;
    }
}
