package controller;
import DAO.DBConnection;
import com.sun.tools.javac.Main;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Appointment;
import model.User;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
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

/**
 This controller class manages the data flow and operations associated with the view defined by loginScreen.fxml.
 */

public class LoginScreenController implements Initializable {
    private Stage stage;
    private Parent scene;
    private static List<User> loginList;
    private static User currentUser;
    private static ObservableList<Appointment> appointmentsList;
    public static  LocalTime timeAtLogin = LocalTime.now();
    public static LocalDateTime currentLocalDateTime = LocalDateTime.now();







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


    /**
     This method is an event handler that exits the program when the exit button is clicked.
     @param event listens for click on the exit button.
     */
    @FXML
    void onExitProgram(ActionEvent event) {
        System.exit(0);

    }


    /**
     When the 'Sign In' button is clicked, this method checks the input username against all usernames in the database.
     If a username matches, the method checks to see whether the password associated with that user matches the password that was input.
     If the password/username do not match an exception is thrown.
     @param event listens for click of sign in button.
     */

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


                    if(loginUser.getUserName().equals(usernameInput.getText()) && loginUser.getPassword().equals(passwordInput.getText())) {
                        currentUser = loginUser;
                        updateLoginActivity(true, usernameInput.getText());
                        validInput = true;
                        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                        scene = FXMLLoader.load(getClass().getResource("/view/mainView.fxml"));
                        stage.setScene(new Scene(scene));
                        stage.show();




                    }




                }

                MainViewController mainViewController = new MainViewController();
                mainViewController.checkAppointmentTimes();

                if(!validInput) {

                    throw new RuntimeException();


                }


            }



        }
        catch(RuntimeException e) {
            updateLoginActivity(false, usernameInput.getText());
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


        }

        catch (IOException e) {
            e.printStackTrace();
        }




    }

    /**
     When every time the 'Sign In' Button is clicked, the onSignIn method calls this helper method to record the results of that sign in attempt to a file called
     login_activity.txt. The method prints details of the login attempt, time and whether the attempt was successful.
     @param wasSuccessful is passed from onSignIn as true if the login was successful, false if not.
     */
    public static void updateLoginActivity(boolean wasSuccessful, String username)  {

        try( FileWriter logUpdate = new FileWriter("login_activity.txt", true);
             BufferedWriter logUpdateBuff = new BufferedWriter(logUpdate);
             PrintWriter logUpdatePW = new PrintWriter(logUpdateBuff)) {
            Timestamp loginAttempt = Timestamp.from(Instant.now());
            String curID = "Invalid User";
            if(wasSuccessful == true) {
                curID = String.valueOf(currentUser.getUserID());

            }
            else{

                for(User user : loginList) {
                    if(username.equals(user.getUserName())) {
                        curID = String.valueOf(user.getUserID());
                    }
                }

            }
            String logEntry = "Login attempt at: "+ loginAttempt.toString() + "(UTC). User ID:" + curID  + ". Login Success: " + wasSuccessful + ".";
            logUpdatePW.println(logEntry);
            System.out.println(logEntry);


        }

        catch (IOException e) {
            System.out.println("File update failed. Invalid input.");
        }





    }

    /**
     When a login is successful, the onSignIn method sets the private static User currentUser equal to the User in the database whose login credentials were used.
     The getCurrentUser method can be called from other classes when the currently logged-in user's information is needed.
     */

    public static User  getCurrentUser() {
        return currentUser;
    }



    /**
     This class' initalize method establishes the system's Locale and default Timezone. Based on this information, the current timezone is displayed on screen.
     If the locale is identified as being a french-speaking country, this method will reset all labels to French using the values provided in the rb_fr.properties file.
     */
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
