package controller;
import DAO.UserQuery;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.User;

import java.net.URL;
import java.security.spec.RSAOtherPrimeInfo;
import java.util.*;


public class MainViewController implements Initializable {
    private static User user = LoginScreenController.getCurrentUser();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        System.out.println(user.toString());




    }
}
