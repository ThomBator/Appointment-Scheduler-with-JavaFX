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

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLInvalidAuthorizationSpecException;
import java.sql.SQLOutput;
import java.util.Locale;
import java.util.ResourceBundle;

public class LoginScreenController implements Initializable {
    Stage stage;
    Parent scene;


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
    private Label localeLabel;

    @FXML
    void onExitProgram(ActionEvent event) {

    }

    @FXML
    void onSignIn(ActionEvent event) {
        try{

            if(usernameInput.getLength() == 0 || passwordInput.getLength() == 0) {
               throw new RuntimeException();
            }
            else {
                String userName = usernameInput.getText().trim();
                String password = passwordInput.getText().trim();


                Connection conn = DBConnection.getConnection(userName, password);
                if(conn.isValid(5)) {
                    stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                    scene = FXMLLoader.load(getClass().getResource("/view/mainView.fxml"));
                    stage.setScene(new Scene(scene));
                    stage.show();

                }

                else {
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

        }

        catch (SQLException | IOException e) {
            e.printStackTrace();
        }




    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        String userLocale = Locale.getDefault().toString();
        localeLabel.setText(userLocale);

        if(Locale.getDefault().getLanguage().equals("fr")) {
            ResourceBundle frRb = ResourceBundle.getBundle("controller/rb");
            exitLabel.setText(frRb.getString("exit"));
            passwordLabel.setText(frRb.getString("password"));
            signInLabel.setText(frRb.getString("signIn"));
            viewTitle.setText(frRb.getString("viewTitle"));
            signInInstructionsLabel.setText(frRb.getString("instructions"));
        }

    }
}
