package presenter;

import java.io.Serializable;

/**
 * Class that hold all the parameters for create a maze
 * @author Elad Jarby
 * @version 1.0
 * @since 18.09.2016
 */
public class Properties implements Serializable {


	/**
	 * Serial number
	 */
	private static final long serialVersionUID = 555913593004844041L;

	private String defaultAlgorithm;
	private int maxMazeSize;
	private String viewSetup;
	private int numberOfThreads;
	private String searchAlgorithm;
	private String serverIp;
	private int serverPort;
	/**
	 * Constructor to initialize all parameters
	 * @param numberOfThreads - Number of threads to run in model.
	 * @param defaultAlgorithm - Default algorithm - simple / Growing tree random / Growing tree last
	 * @param searchAlgorithm - Search algorithm - dfs / bfs
	 * @param maxMazeSize - Max size of the maze that allowed
	 * @param viewSetup - View setup - cli / gui
	 */
	public Properties(int numberOfThreads , String defaultAlgorithm , String searchAlgorithm , int maxMazeSize , String viewSetup, String serverIp , int serverPort) {
		super();
		this.numberOfThreads = numberOfThreads;
		this.defaultAlgorithm = defaultAlgorithm;
		this.viewSetup = viewSetup;
		this.maxMazeSize = maxMazeSize;
		this.searchAlgorithm = searchAlgorithm;
		this.serverIp = serverIp;
		this.serverPort = serverPort;
	}
	
	/**
	 * Copy constructor
	 * @param p - A properties object that contains all the properties of the project
	 */
	public Properties(Properties p) {
		defaultAlgorithm = p.defaultAlgorithm;
		maxMazeSize = p.maxMazeSize;
		numberOfThreads = p.numberOfThreads;
		searchAlgorithm = p.searchAlgorithm;
		serverIp = p.serverIp;
		serverPort = p.serverPort;
		
	}

	/**
	 * Default constructor
	 */
	public Properties() {
		super();
	}
	
	/**
	 * Getter for default algorithm
	 * @return - String with default algorithm
	 */
	public String getDefaultAlgorithm() {
		return defaultAlgorithm;
	}

	/**
	 * Setter for default algorithm
	 * @param defaultAlgorithm - Default algorithm
	 */
	public void setDefaultAlgorithm(String defaultAlgorithm) {
		this.defaultAlgorithm = defaultAlgorithm;
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

	/**
	 * Getter for number of threads
	 * @return Number of threads
	 */
	public int getNumberOfThreads() {
		return numberOfThreads;
	}

	/**
	 * Setter for set how many threads to run in model
	 * @param numberOfThreads - Number of threads
	 */
	public void setNumberOfThreads(int numberOfThreads) {
		this.numberOfThreads = numberOfThreads;
	}

	/**
	 * Getter for search algorithm
	 * @return Search algorithm - Simple / Growing tree random / Growing tree last
	 */
	public String getSearchAlgorithm() {
		return searchAlgorithm;
	}

	/**
	 * Setter for search algorithm
	 * @param searchAlgorithm - Search algorithm
	 */
	public void setSearchAlgorithm(String searchAlgorithm) {
		this.searchAlgorithm = searchAlgorithm;
	}

	/**
	 * Getter for max size of the maze
	 * @return Max size of the maze that allowed
	 */
	public int getMaxMazeSize() {
		return maxMazeSize;
	}

	/**
	 * Setter for max size of the maze
	 * @param maxMazeSize - Max size
	 */
	public void setMaxMazeSize(int maxMazeSize) {
		this.maxMazeSize = maxMazeSize;
	}

	public String getServerIp() {
		return serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}
}
