package de.kbecker.thread.commands;

import com.google.gson.JsonObject;
import de.kbecker.utils.Client;
import org.example.App;
import org.example.LobbyController;
import org.example.LoginController;

import java.io.IOException;

/**
 * @author Kevin Becker (kevin.becker@stud.th-owl.de)
 */
public class HandleLoginTask extends Task{
    @Override
    public void exec(JsonObject jobj) {
        if(jobj == null){
            LoginController.getInstance().getErrorLabel().setText("No connection to the server can be established");
            return;
        }

            if(jobj.get("code").getAsInt()==1){
                Client.getInstance().setSessionID(jobj.get("sessionID").getAsString());
                Client.getInstance().setUsername(jobj.get("username").getAsString());
                try {
                    App.setRoot("Scene_MainMenu",600,400);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                LoginController.getInstance().getErrorLabel().setText(jobj.get("message").getAsString());
            }

    }
}
