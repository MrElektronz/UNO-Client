package org.example;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.kbecker.utils.Client;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {


    @FXML
    private TextField code;

    @FXML
    private Button bJoin;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        code.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                System.out.println(t1);
                if(t1.length()<8){
                    bJoin.setDisable(true);
                }else{
                    bJoin.setDisable(false);
                }
            }
        });
    }

    @FXML
    private void onHost() throws IOException {
        Client.getInstance().hostNewLobby();
        App.setRoot("Scene_Lobby",600,400);
    }

    @FXML
    private void onJoin() throws IOException {
      Client.getInstance().joinLobby(code.getText());
      App.setRoot("Scene_Lobby",600,400);
    }

}
