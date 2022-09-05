package org.example;

import com.google.gson.JsonObject;
import de.kbecker.cards.Deck;
import de.kbecker.utils.Client;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {

    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private TextField password2;

    @FXML
    private Label errorLabel;

    private static RegisterController instance;

    @FXML
    private void onBackToLogin() throws IOException {
        App.setRoot("Scene_Login",600,400);
    }

    @FXML
    private void onRegister() throws IOException {
        if(!password.getText().equals(password2.getText())){
            setErrorLabel("Both passwords do not match");
            return;
        }

        Client.getInstance().register(username.getText(), password.getText());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instance = this;
    }

    public static RegisterController getInstance() {
        return instance;
    }

    public void setErrorLabel(String txt){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                errorLabel.setText(txt);
            }
        });
    }
}
