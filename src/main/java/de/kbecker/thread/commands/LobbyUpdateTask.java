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
        System.out.println("Lobby update");
        if(jobj.has("slotText")) {
            String slotText = jobj.get("slotText").getAsString();
            int currentPlayers = Integer.parseInt(slotText.split("/")[0]);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    App.gThread.getLobby().getSlotText().setText(slotText);
                    //TODO: Switch to > 1 to enable only 2 players and more
                    if (currentPlayers > 1) {
                        App.gThread.getLobby().setStartButtonEnabled(true);
                    } else {
                        App.gThread.getLobby().setStartButtonEnabled(false);
                    }
                    if (jobj.get("lobbyID") != null) {
                        App.gThread.getLobby().getCode().setText(jobj.get("lobbyID").getAsString());
                    }
                    System.out.println("lobby update executed");

                }
            });
        }
    }
}
