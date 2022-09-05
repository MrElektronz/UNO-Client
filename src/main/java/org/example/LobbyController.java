package org.example;

import de.kbecker.thread.TaskThread;
import de.kbecker.utils.Client;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LobbyController implements Initializable {


    @FXML
    private TextField code;
    @FXML
    private Text slotText;

    @FXML
    private Button bStart;


    public Text getSlotText() {
        return slotText;
    }

    public TextField getCode() {
        return code;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        App.gThread.setLobby(this);
    }

    @FXML
    private void onStart() throws IOException {
        Client.getInstance().startGame();
        setStartButtonEnabled(false);
    }

    public void setStartButtonEnabled(boolean enabled){
        bStart.setDisable(!enabled);
    }

    @FXML
    private void onLeave() throws IOException {
        App.setRoot("Scene_MainMenu",600,400);
        Client.getInstance().leaveLobby();
    }


}
