package de.kbecker.thread.commands;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.kbecker.cards.Card;
import de.kbecker.utils.Client;
import org.example.App;
import org.example.GameGUIController;
import org.example.MainMenuController;
import org.example.RegisterController;

import java.io.IOException;
import java.util.ArrayList;

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
            if(GameGUIController.getInstance() == null) {
                App.setRoot("GameGUI", 1200, 800);
            }
            GameGUIController.getInstance().setCurrentCard(Card.deserialize(jobj.get("currentCard").getAsJsonObject()));
            JsonArray playerData = jobj.getAsJsonArray("players");
            ArrayList<Card> cards = new ArrayList<>();
            for(JsonElement elem : playerData){
                if(elem.isJsonObject()){
                    JsonObject obj = elem.getAsJsonObject();
                    if(obj.get("username").getAsString().equals(Client.getInstance().getUsername())){
                        for(JsonElement cardElem : obj.get("cards").getAsJsonArray()){
                            if(elem.isJsonObject()){
                                cards.add(Card.deserialize(cardElem.getAsJsonObject()));
                            }
                            }
                    }
                }
            }
            GameGUIController.getInstance().setPlayerCards(cards);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
