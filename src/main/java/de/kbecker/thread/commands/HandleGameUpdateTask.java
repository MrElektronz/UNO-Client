package de.kbecker.thread.commands;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.kbecker.cards.Card;
import de.kbecker.utils.Client;
import org.example.*;

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
            if(jobj.has("winner")){
                GameGUIController.getInstance().endGame();
                if(Client.getInstance().getUsername().equals(jobj.get("winner").getAsString())){
                    EndController.getInstance().setWinner();
                }
                return;
            }
            GameGUIController.getInstance().setCurrentCard(Card.deserialize(jobj.get("currentCard").getAsJsonObject()));
            JsonArray playerData = jobj.getAsJsonArray("players");
            ArrayList<Card> cards = new ArrayList<>();
            String currentPlayer = playerData.get(jobj.get("currentPlayer").getAsInt()).getAsJsonObject().get("username").getAsString();
            ArrayList<GameGUIController.Player> enemies = new ArrayList<>();
            for(JsonElement elem : playerData){
                if(elem.isJsonObject()){
                    JsonObject obj = elem.getAsJsonObject();
                    if(obj.get("username").getAsString().equals(Client.getInstance().getUsername())){
                        for(JsonElement cardElem : obj.get("cards").getAsJsonArray()){
                            if(elem.isJsonObject()){
                                cards.add(Card.deserialize(cardElem.getAsJsonObject()));
                            }
                            }
                    }else{
                        // create other player data to render
                        String name = obj.get("username").getAsString();
                        GameGUIController.Player enemy = new GameGUIController.Player(name,obj.get("cards").getAsJsonArray().size(),currentPlayer.equals(name));
                        enemies.add(enemy);
                    }
                }
            }

            GameGUIController.getInstance().drawPlayers(enemies);
            if(jobj.has("event")){
                String event = jobj.get("event").getAsString();
                if(event.equals("wildCard") && Client.getInstance().getUsername().equals(currentPlayer)){
                    GameGUIController.getInstance().showWildCardPicker();
                }
            }

            // Render wildcardcolor
            if(jobj.has("wildCardColor")){
                String color = jobj.get("wildCardColor").getAsString();
                GameGUIController.getInstance().setWildCardColor(color);
            }


                GameGUIController.getInstance().setPlayerCards(cards);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
