package de.kbecker.utils;

import org.example.App;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/**
 * 
 * This class only contains one method, it is used to read from the config file.
 * 
 * @author KBeck
 * 
 *
 */
public class ConfigManager {

	/**
	 * 
	 * @param key to read from
	 * @return value stored in the config file under the key
	 */
	public static String readFromProperties(String key) {
		try (InputStream in = App.class.getResourceAsStream("/config.properties")) {
			Properties prop = new Properties();

			prop.load(in);
			return prop.getProperty(key);

		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return "";
	}

}
