package de.kbecker.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.kbecker.thread.TaskThread;
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

	private final String IP = "localhost";//ClientMain.readProperty("server.ip");
	private final int PORT = 3003;//Integer.valueOf(ClientMain.readProperty("server.port"));
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
				client = new Socket(IP, PORT);
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
	 * is client-side only
	 * 
	 * @param profilePicID resembles id of profile picture
	 */
	public void setProfilePicID(int profilePicID) {
		this.profilePicID = profilePicID;
	}

	public String getUsername() {
		if (username.equals("")) {
			username = requestUsername();
		}
		return username;
	}

	public int getProfilePicID() {
		return profilePicID;
	}

	/**
	 * is server-side only
	 * 
	 * @param id resembles id of profile picture
	 */
	public void sendNewProfilePicID(int id) {
		try {
			initClient();
			out.writeUTF(buildServerCommand("SetProf", sessionID, id + ""));
			out.flush();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void updateProfilePic() {
		try {
			initClient();
			out.writeUTF(buildServerCommand("GetProf", getUsername()));
			int value = in.readInt();
			setProfilePicID(value);
			out.flush();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * sends a message to the server to delete the current session TODO: Send client
	 * back to 'Scene_Login'
	 */
	public void logout() {
		if (!sessionID.equals("0")) {
			try {
				initClient();
				out.writeUTF(buildServerCommand("Logout", sessionID));
				out.flush();
				sessionID = "0";
				in.close();
				out.close();
				client.close();
				// Could receive answer from server after logout
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * This method is not really in use right now, but can become handy in the
	 * future (The player can change his username through memory editing and this
	 * provides a safe way to get his real username). It is only used once when
	 * getUsername() has been called for the first time.
	 * 
	 * @return the client's username, as stored on the server
	 */
	private String requestUsername() {
		String user = "?";
		if (!sessionID.equals("0")) {
			try {
				initClient();
				out.writeUTF(buildServerCommand("RequestUsername", sessionID));
				out.flush();
				user = in.readUTF();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return user;
	}

	/**
	 * 
	 * @param username of the account which password should be reset
	 * @return 1 if reset code was send, 0 if username not found, 2 other error
	 */
	public byte requestResetPassword(String username) {
		if (username.equals("") || username == null) {
			return 0;
		}
		try {
			initClient();
			out.writeUTF(buildServerCommand("RequestResetPW", username));
			out.flush();
			byte value = in.readByte();
			return value;
		} catch (IOException ex) {
			ex.printStackTrace();
			return 2;
		}
	}



	/**
	 * sends a simple message to the server to let it know the client is still
	 * connected. The Server refreshes the variable 'lastTimeSeen' for the current
	 * session if the message was received by it if not: The server deletes the
	 * current session after multiple pings did not come through
	 */
	public void pingServer() {
		if (!sessionID.equals("0")) {
			try {
				initClient();
				// Ping command: $P;ID
				out.writeUTF(buildServerCommand("P", sessionID));
				out.flush();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * @return "0" if no active session
	 */
	public String getSessionID() {
		return sessionID;
	}



	/**
	 *
	 * @param prefix
	 * @param args
	 * @return string of syntax: $prefix;args[0];args[1]...
	 */
	private String buildServerCommand(String prefix, String... args) {
		String s = "$" + prefix + ";";
		for (String a : args) {
			s += a + ";";
		}
		return s.substring(0, s.length() - 1);
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


	/**
	 * If successful, send new game update to all clients
	 */
	public void requestRollDice() {
		try {
			initClient();
			out.writeUTF(buildServerCommand("RequestRoll", sessionID));
			out.flush();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * If successful, send new game update to all clients
	 * 
	 * @param number to add to bank
	 */
	public void requestAddBankDice(int number) {
		try {
			initClient();
			out.writeUTF(buildServerCommand("RequestAddBank", sessionID, "" + number));
			out.flush();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * If successful, send new game update to all clients
	 * 
	 * @param number to remove from bank
	 */
	public void requestRemoveBankDice(int number) {
		try {
			initClient();
			out.writeUTF(buildServerCommand("RequestRemoveBank", sessionID, "" + number));
			out.flush();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * If successful, send new game update to all clients
	 * 
	 * @param scoreID the score field id which is selected
	 */
	public void selectScore(int scoreID) {
		try {
			initClient();
			out.writeUTF(buildServerCommand("SelectScore", sessionID, "" + scoreID));
			out.flush();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
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
