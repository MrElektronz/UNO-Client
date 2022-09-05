package de.kbecker.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.kbecker.cards.Card;
import de.kbecker.thread.TaskThread;
import javafx.scene.paint.Color;
import org.example.App;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.MessageDigest;


/**
 * This class handles all requests the client sends over to the server (e.g.
 * login or logout)
 * 
 * @author KBeck
 *
 */
public class Client {

	private String ip = "localhost";
	private int port = 3003;
	private String sessionID = "0";
	private String username = "";
	private int profilePicID = 0;
	private Socket client;
	private DataOutputStream out;
	private DataInputStream in;
	private static Client instance;

	/**
	 * private constructor, so no class can instanciate from Client
	 */
	private Client() {
		loadConfig();
	}


	private void loadConfig() {
		port = Integer.valueOf(ConfigManager.readFromProperties("server.port"));
		ip = ConfigManager.readFromProperties("server.url");
	}


	/**
	 * We are using the Singleton design pattern for this class
	 * 
	 * @return the one instance of this class
	 */
	public static Client getInstance() {
		if (instance == null) {
			instance = new Client();
		}
		return instance;
	}

	// Creates connection if not already exists
	private void initClient() {
		if (client == null) {
			try {
				client = new Socket(ip, port);
				out = new DataOutputStream(client.getOutputStream());
				in = new DataInputStream(client.getInputStream());
				App.gThread = new TaskThread();
				App.gThread.start();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 *
	 * @param username of the client
	 * @param password not-encrypted password
	 */
	public void login(String username, String password) {
		JsonObject request = new JsonObject();
		request.addProperty("command", "login");
		request.addProperty("username", username);
		request.addProperty("password", password);
		try {
			initClient();
			out.writeUTF(new Gson().toJson(request));
			out.flush();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 *
	 * @param username of the newly created account
	 * @param password of the newly created account
	 */
	public void register(String username, String password) {
		JsonObject request = new JsonObject();
		request.addProperty("command", "register");
		request.addProperty("username", username);
		request.addProperty("password", password);
		try {
			initClient();
			out.writeUTF(new Gson().toJson(request));
			out.flush();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 *
	 */
	public void startGame() {
		JsonObject request = new JsonObject();
		request.addProperty("command", "startGame");
		request.addProperty("sessionID", sessionID);

		try {
			initClient();
			out.writeUTF(new Gson().toJson(request));
			out.flush();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}


	/**
	 * Tries to draw a card
	 */
	public void drawCard() {
		JsonObject request = new JsonObject();
		request.addProperty("command", "drawCard");
		request.addProperty("sessionID", sessionID);

		try {
			initClient();
			out.writeUTF(new Gson().toJson(request));
			out.flush();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 *
	 * @param fill
	 */
	public void chooseColor(Color fill) {
		JsonObject request = new JsonObject();
		request.addProperty("command", "chooseColor");
		request.addProperty("sessionID", sessionID);
		request.addProperty("color", fill.toString());

		try {
			initClient();
			out.writeUTF(new Gson().toJson(request));
			out.flush();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}


	/**
	 * Tries to draw a card
	 */
	public void setCard(Card card) {
		JsonObject request = new JsonObject();
		request.addProperty("command", "setCard");
		request.addProperty("sessionID", sessionID);
		request.add("card", card.serialize());

		try {
			initClient();
			out.writeUTF(new Gson().toJson(request));
			out.flush();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * is client-side only
	 * 
	 * @param profilePicID resembles id of profile picture
	 */
	public void setProfilePicID(int profilePicID) {
		this.profilePicID = profilePicID;
	}

	public String getUsername() {
		return username;
	}

	/**
	 * 
	 * @return "0" if no active session
	 */
	public String getSessionID() {
		return sessionID;
	}

	public void hostNewLobby() {
		JsonObject request = new JsonObject();
		request.addProperty("command", "host");
		request.addProperty("sessionID", sessionID);
		try {
			initClient();
			out.writeUTF(new Gson().toJson(request));
			out.flush();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void joinLobby(String lobbyID) {
		JsonObject request = new JsonObject();
		request.addProperty("command", "join");
		request.addProperty("sessionID", sessionID);
		request.addProperty("lobbyID", lobbyID);
		try {
			initClient();
			out.writeUTF(new Gson().toJson(request));
			out.flush();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}



	public void setUsername(String username) {
		this.username = username;
	}


	/**
	 * tells the server that the player is ready to start the game (if he is in a
	 * lobby)
	 */
	public void leaveLobby() {
		JsonObject request = new JsonObject();
		request.addProperty("command", "leave");
		request.addProperty("sessionID", sessionID);
		try {
			initClient();
			out.writeUTF(new Gson().toJson(request));
			out.flush();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 
	 * @return DataInputStream of Socket
	 */
	public DataInputStream getIn() {
		return in;
	}

	/**
	 * 
	 * @return the client's socket
	 */
	public Socket getSocket() {
		return client;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}
}
