package model;

import presenter.ServerProperties;

/**
 * Model interface which make all the calculations for specific command.
 * @author Elad Jarby
 * @version 1.0
 * @since 18.10.2016
 */
public interface Model {
	
	/**
	 * Starting the server
	 */
	public void openServer();
	
	/**
	 * Close the server
	 */
	public void closeServer();
	
	/**
	 * Get message
	 * @return String Message
	 */
	public String getMessage();
	
	/**
	 * Save properties to file
	 * @param arr  - array of string with the parameters
	 */
	public void saveProperties(String[] args);
	
	/**
	 * Getter to get properties
	 * @return Properties
	 */
	public ServerProperties getProperties();
}
