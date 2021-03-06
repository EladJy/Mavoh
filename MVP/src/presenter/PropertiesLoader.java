package presenter;

import java.beans.XMLDecoder;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Load properties from properties file
 * @author Elad Jarby
 * @version 1.0
 * @since 18.09.2016
 */
public class PropertiesLoader {
	private static PropertiesLoader instance;
	private Properties properties;
	
	/**
	 * Getter for properties
	 * @return Properties that was loaded
	 */
	public Properties getProperties() {
		return properties;
	}
	
	/**
	 * Constructor to load all the properties files
	 */
	private PropertiesLoader() 
	{
		try {
			XMLDecoder decoder = new XMLDecoder(new FileInputStream("./resources/properties.xml"));
			properties = (Properties)decoder.readObject();
			decoder.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Singleton function to set only one instance
	 * @return Properties loader
	 */
	public static PropertiesLoader getInstance() {
		if (instance == null) 
			instance = new PropertiesLoader();
		return instance;
	}
}
