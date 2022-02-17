package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;
import model.User;

import java.sql.*;
import java.time.Instant;


/**
 This class handles all queries related to objects of the Customer class and associated database records in the customers table.
 */
public class CustomerQuery {
    private static User currentUser = controller.LoginScreenController.getCurrentUser();



    /**
    The getCustomers uses a select-all SQL statement passed through a PreparedStatement object to query the database and provide all customer records.
    Those records collected in a ResultSet object. The ResultSet is looped through and all individual customers are used to create objects of the Customerclass.
    An ObservableList list of all created Customer objects is then returned by the method call.
     */
    public static  ObservableList<Customer> getCustomers() {

        ObservableList<Customer> customers   = FXCollections.observableArrayList();

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
    /**
     The addCustomer method takes input collected from the form in the view defined by AddModifyCustomer.fxml and uses it to add
     a new Customer to the customers table in the database. A prepared statement is used with bind variables to pass the specific values for each
     customer into the insertStatement String.
     @param address the customer address string
     @param divisionID the firstLevelDivision ID int
     @param name the customer's name as a String
     @param phoneNumber the customer's phone number as a String
     @param postalCode the customer's postal code as a String
     */
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


    /**
     The updateCustomer method takes input collected from the form in the view defined by AddModifyCustomer.fxml and uses it to update
     an existing customer record in the customers database table. A prepared statement is used with bind variables to pass the specific values for each
     customer into the insertStatement String.
     @param customerID the ID of the existing customer record to be modified.
     @param address the customer address string
     @param divisionID the firstLevelDivision ID int
     @param name the customer's name as a String
     @param phoneNumber the customer's phone number as a String
     @param postalCode the customer's postal code as a String
     */
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
    /**
     The deleteCustomer method executes a delete statement in the database, using a specified
     customerID as the reference for which record to delete in the customers table.
     @param customerID the ID of the customer to delete. 
     */
    public static void deleteCustomer(int customerID) {
        try (Connection connection = DBConnection.getConnection()) {
            String deleteStatement = "DELETE FROM customers WHERE Customer_ID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteStatement);
            preparedStatement.setInt(1, customerID);
            preparedStatement.execute();
            preparedStatement.close();



        }
        catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
