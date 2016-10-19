package presenter;

import java.io.Serializable;

/**
 * Class that hold all the parameters for server properties
 * @author Elad Jarby
 * @version 1.0
 * @since 18.09.2016
 */
public class ServerProperties implements Serializable {

	/**
	 * Serial number
	 */
	private static final long serialVersionUID = 1L;
	
	protected String viewSetup;
	protected int numberOfClients;
	protected int port;
	protected int timeOut;
	
	/**
	 * Default constructor
	 */
	public ServerProperties() {
		super();
	}
	
	/**
	 * Copy constructor
	 * @param p - A properties object that contains all the properties of the project
	 */
	public ServerProperties(int port , int numberOfClients, int timeOut, String viewSetup) {
		this.port = port;
		this.numberOfClients = numberOfClients;
		this.timeOut = timeOut;
		this.viewSetup = viewSetup;
	}

	/**
	 * Getter for number of clients
	 * @return - Int with number of clients
	 */
	public int getNumberOfClients() {
		return numberOfClients;
	}

	/**
	 * Setter for number of clients
	 * @param numberOfClients - Number of clients
	 */
	public void setNumberOfClients(int numberOfClients) {
		this.numberOfClients = numberOfClients;
	}

	/**
	 * Getter for server port
	 * @return - Int of server port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Setter for server port
	 * @param port - Server port
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * Getter for time out
	 * @return - Int of time out
	 */
	public int getTimeOut() {
		return timeOut;
	}

	/**
	 * Setter for time out
	 * @param timeOut - Time out
	 */
	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}

	/**
	 * Getter for view setup
	 * @return View setup - cli or gui
	 */
	public String getViewSetup() {
		return viewSetup;
	}

	/**
	 * Setter for view setup
	 * @param viewSetup - View setup
	 */
	public void setViewSetup(String viewSetup) {
		this.viewSetup = viewSetup;
	}


}
