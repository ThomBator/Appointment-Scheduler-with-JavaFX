package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;
import model.User;

import java.sql.*;
import java.time.Instant;

public class CustomerQuery {
    private static ObservableList<Customer> customers   = FXCollections.observableArrayList();
    private static User currentUser = controller.LoginScreenController.getCurrentUser();

    public static  ObservableList<Customer> getCustomers() {


            try( Connection conn = DBConnection.getConnection()){

                String selectStatement = "SELECT * FROM customers";
                PreparedStatement preparedStatement = conn.prepareStatement(selectStatement);
                preparedStatement.execute();
                ResultSet result = preparedStatement.getResultSet();
                customers.clear();

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


                result.close();
                preparedStatement.close();
            }

            catch (SQLException e) {
                e.printStackTrace();
            }










        return customers;

    }

    public static String addCustomer(String name, String address, String phoneNumber, String postalCode, int divisionID) {
        try(Connection conn = DBConnection.getConnection()) {
            Timestamp timestamp = Timestamp.from(Instant.now());

            String insertStatement = "INSERT INTO customers (Customer_ID, Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID) VALUES (?,?, ?, ? , ? , ? , ? , ? , ? , ? )";
            PreparedStatement preparedStatement = conn.prepareStatement(insertStatement);
            preparedStatement.setInt(1, 0);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, address);
            preparedStatement.setString(4, postalCode);
            preparedStatement.setString(5, phoneNumber);
            preparedStatement.setTimestamp(6, timestamp);
            preparedStatement.setString(7, currentUser.getUserName());
            preparedStatement.setTimestamp(8, timestamp);
            preparedStatement.setString(9, currentUser.getUserName());
            preparedStatement.setInt(10, divisionID);

            preparedStatement.execute();
            preparedStatement.close();


        }
        catch(SQLException e) {
            e.printStackTrace();

        }

        return "New Customer Successfully Added";


    }

    public static String updateCustomer (int customerID, String name, String address, String phoneNumber, String postalCode, int divisionID) {

     try(Connection conn = DBConnection.getConnection()) {
         Timestamp timestamp = Timestamp.from(Instant.now());
         String updateStatement = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Last_Update = ?, Last_Updated_By = ?, Division_ID = ? WHERE Customer_ID = ?";
         PreparedStatement preparedStatement = conn.prepareStatement(updateStatement);
         preparedStatement.setString(1, name);
         preparedStatement.setString(2, address);
         preparedStatement.setString(3, postalCode);
         preparedStatement.setString(4, phoneNumber);
         preparedStatement.setTimestamp(5, timestamp);
         preparedStatement.setString(6, currentUser.getUserName());
         preparedStatement.setInt(7, divisionID);
         preparedStatement.setInt(8, customerID);

         preparedStatement.execute();
         preparedStatement.close();
         DBConnection.closeConnection();


     }
     catch(SQLException e) {
         e.printStackTrace();
     }
        return "Customer Info Updated Successfully";


    }


}
