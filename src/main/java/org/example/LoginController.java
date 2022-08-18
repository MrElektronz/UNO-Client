package org.example;

import com.google.gson.JsonObject;
import de.kbecker.cards.Deck;
import de.kbecker.utils.Client;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {


    @FXML
    private TextField username;

    @FXML
    private TextField password;

    @FXML
    private Label errorLabel;

    private static LoginController instance;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instance = this;
    }

    @FXML
    private void onLogin() throws IOException {
        Client.getInstance().login(username.getText(),password.getText());

    }

    @FXML
    private void onRegisterHere() throws IOException {
        App.setRoot("Scene_Register",600,400);
    }

    public static LoginController getInstance() {
        return instance;
    }

    public Label getErrorLabel() {
        return errorLabel;
    }
}
