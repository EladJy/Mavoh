package model;

import presenter.ServerProperties;

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
