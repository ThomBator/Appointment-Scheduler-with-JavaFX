package main;

import DAO.DBConnection;
import DAO.DBQuery;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Scanner;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/loginScreen.fxml"));
        stage.setTitle("Appointment Scheduling System");
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }

    public static void main(String[] args) throws SQLException {

        //To work with a database, you first need to create a Connection object
        //Connection conn = DBConnection.getConnection();


        launch(args);

        //DBConnection.closeConnection();
    }


}
