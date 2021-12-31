package main;

import DAO.DBConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/loginScreen.fxml"));
        stage.setTitle("Appointment Scheduling System");
        stage.setScene(new Scene(root, 1200, 800));
        stage.show();
    }

    public static void main(String[] args) {


        DBConnection.openConnection();

        launch(args);

        DBConnection.closeConnection();
    }


}
