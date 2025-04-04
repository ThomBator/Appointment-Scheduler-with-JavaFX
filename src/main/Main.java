package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
/**
 This is the Main Class for this Appointment Scheduler application.

 */

public class Main extends Application {


    /**This method creates the initial root node, stage and scene that are required for a JavaFX application.
     * @param stage extends javaFX.Window to launch the first window of the Graphical User Interface.
     */
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/loginScreen.fxml"));
        stage.setTitle("Appointment Scheduler System");
        stage.setScene(new Scene(root, 600, 400));
        stage.show();

    }
    /**
     The main method passes all the arguments created by the JavaFX application
     to the launch method with the statement launch(args).
     @param  args launches the JavaFx program.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
