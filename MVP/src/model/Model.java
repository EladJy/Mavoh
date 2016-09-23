package model;

import algorithms.search.Solution;
import presenter.Properties;

public interface Model {
	/**
	 * Handling with command: dir < path ></br>
	 * @param dirArray - Array of string , containing string with path.
	 */
	public void dirPath(String[] dirArray);

	/**
	 * Handling with command:</br> 
	 * generate_3d_maze < maze name > < floors > < width > < length > < algorithm ></br>
	 * <b>Algorithms:</b> simple , growing-last , growing-random.
	 * @param arr - Array of string with the parameters.
	 */
	public void generate3dMaze(String[] arr);
	
	/**
	 * Handling with command: display < maze name >
	 * @param arr - Array of one string , containing the maze name that need to display.
	 */
	public void getMaze(String[] arr);
	
	/**
	 * Handling with command: display_cross_section < axis > < index > < maze name ></br>
	 * <b>axis:</b> z - for floors , y - for width , x - for length.</br>
	 * <b>index:</b> can chose from 0 to axis-1 , otherwise return error.
	 * @param arr - Array of string with the parameters.
	 */
	public void crossSection(String[] arr) ;

	/**
	 * Handling with command: save_maze < maze name > < file name ></br>
	 * <b>maze name:</b> The maze name that need to be save on the file.</br>
	 * <b>file name:</b> The file name that we need to save maze in.
	 * @param arr - Array of string with the parameters.
	 */
	public void saveMaze(String[] arr);
	
	/**
	 * Handling with command: load_maze < file name > < maze name ></br>
	 * <b>file name:</b> The file name we need to load the maze from.</br>
	 * <b>maze name:</b> The maze name we want to save from the loaded file.
	 * @param arr - Array of string with the parameters.
	 */
	public void loadMaze(String[] arr);
	
	/**
	 * Handling with command: solve < maze name > < algorithm ></br>
	 * Solves the specific maze with algorithm.</b></br>
	 * <b>alogirthm:</b> dfs or bfs.
	 * @param arr - Array of string with the parameters.
	 */
	public void getSolutionReady(String[] arr);
	
	/**
	 * Handling with command: display_solution < maze name ></br>
	 * Display existing solution.
	 * @param arr - Array of one string , containing the maze name that need to display his solution.
	 */
	public void getSolution(String[] arr);
	
	/**
	 * Closing all running threads.
	 * @param emptyArr - There is nothing in the array.
	 */
	public void exitCommand();
	
	/**
	 * Save mazes and solutions into specific files
	 */
	public void saveMazesAndSolutions();
	
	/**
	 * Load mazes and solution from specific files
	 */
	public void loadMazesAndSolutions();
	
	/**
	 * Save properties to file
	 * @param arr  - array of string with the parameters
	 */
	public void saveProperties(String[] arr);
	
	/**
	 * Getter to get byte of array from hash map by maze name
	 * @param maze - Maze name
	 * @return Array of byte of maze
	 */
	public byte[] getMazeFromHashMap(String maze);
	
	/**
	 * Getter to get solution from hash map by maze name
	 * @param maze - Maze name
	 * @return Solution of the specific maze
	 */
	public Solution<String> getSolutionFromHashMap(String maze);
	
	/**
	 * Getter to get cross section
	 * @return Cross section of the specific request
	 */
	public int[][] getCrossSection();
	
	/**
	 * Get list of files / directories from directory
	 * @return List of files / directories
	 */
	public String[] getList();
	
	/**
	 * Get message
	 * @return Message
	 */
	public String getMessage();
	
	/**
	 * Getter to get maze name
	 * @return Maze name
	 */
	public String getMazeName();
	
	/**
	 * Getter to get properties
	 * @return Properties
	 */
	public Properties getProperties();
}
