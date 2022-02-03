package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;

import java.sql.*;

public class CustomerQuery {
    private static ObservableList<Customer> customers   = FXCollections.observableArrayList();

    public static  ObservableList<Customer> getCustomers() {

        if(customers.isEmpty()) {
            try( Connection conn = DBConnection.getConnection()){

                String selectStatement = "SELECT * FROM customers";
                PreparedStatement preparedStatement = conn.prepareStatement(selectStatement);
                preparedStatement.execute();
                ResultSet result = preparedStatement.getResultSet();

                while(result.next()) {
                    int customerID = result.getInt("Customer_ID");
                    String customerName = result.getString("Customer_Name");
                    String address = result.getString("Address");
                    String postalCode = result.getString("Postal_Code");
                    String phone = result.getString("Phone");
                    Timestamp createDate = result.getTimestamp("Create_Date");
                    String createdBy = result.getString("Created_By");
                    Timestamp lastUpdate = result.getTimestamp("Last_Update");
                    String lastUpdatedBy = result.getString("Last_Updated_By");
                    int divisionID = result.getInt("Division_Id");
                    customers.add(new Customer(customerID, customerName, address, postalCode, phone, createDate.toLocalDateTime(), createdBy, lastUpdate.toLocalDateTime(), lastUpdatedBy, divisionID));



                }

                for(Customer customer: customers) {
                    System.out.println(customer.toString());
                }
                result.close();
                preparedStatement.close();
            }

            catch (SQLException e) {
                e.printStackTrace();
            }








        }

        return customers;

    }

}
