package de.kbecker.thread;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import de.kbecker.thread.commands.*;
import de.kbecker.utils.Client;
import org.example.LobbyController;

import java.util.HashMap;

/**
 * @author Kevin Becker (kevin.becker@stud.th-owl.de)
 */
public class TaskThread extends Thread{

    private boolean running;
    private Client client;
    private HashMap<String, Task> commandMap;
    private LobbyController lobby;


    public TaskThread(){
        client = Client.getInstance();
        running = true;
        commandMap = new HashMap<>();
        commandMap.put("lobbyUpdate", new LobbyUpdateTask());
        commandMap.put("login", new HandleLoginTask());
        commandMap.put("register", new HandleRegisterTask());
        commandMap.put("gameUpdate", new HandleGameUpdateTask());
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public void run() {
        while (running && !client.getSocket().isClosed()) {
            String received = "";
            try {
                received = client.getIn().readUTF();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            JsonElement jsonReceived = new JsonParser().parse(received);

            if (jsonReceived.isJsonObject() && jsonReceived.getAsJsonObject().get("task") != null) {
                commandMap.get(jsonReceived.getAsJsonObject().get("task").getAsString()).exec(jsonReceived.getAsJsonObject());
            }
        }
    }

    public void setLobby(LobbyController lobbyController) {
        this.lobby = lobbyController;
    }

    public LobbyController getLobby() {
        return lobby;
    }
}
