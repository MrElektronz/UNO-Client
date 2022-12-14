package de.kbecker.thread.commands;

import com.google.gson.JsonObject;
import javafx.application.Platform;
import org.example.App;
import org.example.LobbyController;

/**
 * @author Kevin Becker (kevin.becker@stud.th-owl.de)
 */
public class LobbyUpdateTask extends Task{
    @Override
    public void exec(JsonObject jobj) {
        if(jobj.has("slotText")) {
            String slotText = jobj.get("slotText").getAsString();
            int currentPlayers = Integer.parseInt(slotText.split("/")[0]);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    App.gThread.getLobby().getSlotText().setText(slotText);

                    if (currentPlayers > 1) {
                        App.gThread.getLobby().setStartButtonEnabled(true);
                    } else {
                        App.gThread.getLobby().setStartButtonEnabled(false);
                    }
                    if (jobj.get("lobbyID") != null) {
                        App.gThread.getLobby().getCode().setText(jobj.get("lobbyID").getAsString());
                    }
                }
            });
        }
    }
}
