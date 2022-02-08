package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Contact;
import model.User;

import java.sql.*;
import java.util.List;

public class ContactQuery {
    private static ObservableList<Contact> contacts = FXCollections.observableArrayList();

    public static ObservableList<Contact> getDBContacts() {

        if (contacts.isEmpty()) {

            try (Connection conn = DBConnection.getConnection();) {

                final String SELECTSTATEMENT = "SELECT * FROM contacts";
                PreparedStatement preparedStatement = conn.prepareStatement(SELECTSTATEMENT);
                preparedStatement.execute();
                ResultSet result = preparedStatement.getResultSet();

                while (result.next()) {
                    int contactID = result.getInt("Contact_ID");
                    String name = result.getString("Contact_Name");
                    String email = result.getString("Email");


                    contacts.add(new Contact(contactID, name, email));
                }
                preparedStatement.close();
                result.close();


            } catch (SQLException e) {
                System.out.println("Invalid statement. Please review your SQL input string.");

            }


        }

        return contacts;

    }



}
