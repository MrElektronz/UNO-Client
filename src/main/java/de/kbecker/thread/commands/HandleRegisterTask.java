package de.kbecker.thread.commands;

import com.google.gson.JsonObject;
import de.kbecker.utils.Client;
import org.example.App;
import org.example.LoginController;
import org.example.RegisterController;

import java.io.IOException;

/**
 * @author Kevin Becker (kevin.becker@stud.th-owl.de)
 */
public class HandleRegisterTask extends Task{
    @Override
    public void exec(JsonObject jobj) {
        if(jobj == null){
            //RegisterController.errorLabel.setText("No connection to the server can be established");
            RegisterController.getInstance().setErrorLabel("No connection to the server can be established");
            return;
        }
        RegisterController.getInstance().setErrorLabel(jobj.get("message").getAsString());

    }
}
