package org.example;

import de.kbecker.utils.Client;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EndController implements Initializable {


    @FXML
    private Label myLabel;

    private static EndController instance;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instance = this;
    }

    public static EndController getInstance() {
        return instance;
    }

    @FXML
    private void onBack() throws IOException {
        App.setRoot("Scene_MainMenu",600,400);
    }


    public void setWinner(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                myLabel.setText("You have won!");
                myLabel.setTextFill(Color.GREEN);
            }
        });

    }

}
