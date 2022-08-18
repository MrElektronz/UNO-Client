package de.kbecker.thread.commands;

import com.google.gson.JsonObject;
import de.kbecker.cards.Card;
import org.example.App;
import org.example.GameGUIController;
import org.example.MainMenuController;
import org.example.RegisterController;

import java.io.IOException;

/**
 * @author Kevin Becker (kevin.becker@stud.th-owl.de)
 */
public class HandleGameUpdateTask extends Task{
    @Override
    public void exec(JsonObject jobj) {
        if(jobj == null){
            return;
        }
        try {
            App.setRoot("GameGUI",1200,800);
            GameGUIController.getInstance().setCurrentCard(Card.deserialize(jobj.get("currentCard").getAsJsonObject()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
