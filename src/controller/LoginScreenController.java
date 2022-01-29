package controller;
import DAO.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.User;

import java.util.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.sql.*;
import java.time.Instant;
import java.time.LocalDate;

public class LoginScreenController implements Initializable {
    private Stage stage;
    private Parent scene;
    private static List<User> loginList;
    private static User currentUser;
    //Tracks currently logged-in user to update database as needed.





    @FXML
    private Button exitLabel;

    @FXML
    private PasswordField passwordInput;

    @FXML
    private Label passwordLabel;

    @FXML
    private Button signInLabel;

    @FXML
    private Label signInInstructionsLabel;

    @FXML
    private Label viewTitle;

    @FXML
    private TextField usernameInput;

    @FXML
    private Label usernameLabel;

    @FXML
    private Label timeZoneLabel;



    @FXML
    void onExitProgram(ActionEvent event) {
        System.exit(0);

    }

    @FXML
    void onSignIn(ActionEvent event) throws IOException {
        try{

            if(usernameInput.getLength() == 0 || passwordInput.getLength() == 0) {
               throw new RuntimeException();
            }
            else {
                boolean validInput = false;
                for(int i = 0; i < loginList.size(); i++) {

                    User loginUser = loginList.get(i);


                    if(loginUser.getUserName().equals(usernameInput.getText()) &&
                            loginUser.getPassword().equals(passwordInput.getText())) {
                        updateLoginActivity(true);
                        currentUser = loginUser;
                        validInput = true;
                        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                        scene = FXMLLoader.load(getClass().getResource("/view/mainView.fxml"));
                        stage.setScene(new Scene(scene));
                        stage.show();



                    }




                }

                if(!validInput) {
                    throw new RuntimeException();


                }


            }



        }
        catch(RuntimeException e) {

            if(Locale.getDefault().getLanguage().equals("fr")) {
                ResourceBundle frAlert = ResourceBundle.getBundle("controller/rb");
                Alert inputAlert = new Alert(Alert.AlertType.ERROR);
                inputAlert.setTitle(frAlert.getString("inputAlertTitle"));
                inputAlert.setContentText(frAlert.getString("inputAlertMessage"));
                inputAlert.showAndWait();
            }

            else {
                Alert inputAlert = new Alert(Alert.AlertType.ERROR);
                inputAlert.setTitle("Input missing!");
                inputAlert.setContentText("Username or password was not entered or was invalid. Please enter a valid username and password.");
                inputAlert.showAndWait();
            }
            updateLoginActivity(false);

        }

        catch (IOException e) {
            e.printStackTrace();
        }




    }


    public static void updateLoginActivity(boolean wasSuccessful)  {

        try( FileWriter logUpdate = new FileWriter("login_activity.txt", true);
             BufferedWriter logUpdateBuff = new BufferedWriter(logUpdate);
             PrintWriter logUpdatePW = new PrintWriter(logUpdateBuff)) {
            Timestamp loginAttempt = Timestamp.from(Instant.now());
            String logEntry = "Login attempt at: "+ loginAttempt.toString() + "(UTC). Login Success: " + wasSuccessful;
            logUpdatePW.println(logEntry);
            System.out.println(logEntry);


        }

        catch (IOException e) {
            System.out.println("File update failed. Invalid input.");
        }





    }

    public static User  getCurrentUser() {
        return currentUser;
    }




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        String userLocale = Locale.getDefault().toString();
        String timezoneStr = TimeZone.getDefault().getDisplayName();



            loginList = DAO.UserQuery.getDBUsers();



        if(Locale.getDefault().getLanguage().equals("fr")) {
            ResourceBundle frRb = ResourceBundle.getBundle("controller/rb");
            exitLabel.setText(frRb.getString("exit"));
            passwordLabel.setText(frRb.getString("password"));
            signInLabel.setText(frRb.getString("signIn"));
            viewTitle.setText(frRb.getString("viewTitle"));
            signInInstructionsLabel.setText(frRb.getString("instructions"));
            timeZoneLabel.setText(frRb.getString("timeZoneTitle") + timezoneStr);
        }
        else {
            timeZoneLabel.setText("Current Timezone: " + " " +timezoneStr);
        }





    }
}
