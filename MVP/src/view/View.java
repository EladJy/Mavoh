package view;

import algorithms.search.Solution;

/**
 * View interface which display all the generated information for specific command.
 * @author Elad Jarby
 * @version 1.0
 * @since 13.09.2016
 */
public interface View {
	/**
	 * Starting the user interface.
	 */
	public void start();
	
	/**
	 * Display a list of files/directories for specific path.
	 * @param dirArray - Array of string , containing  files/directories.
	 */
	public void displayDirPath(String[] dirArray);
	
	/**
	 * Displays an error message.
	 * @param error - Error message.
	 */
	public void displayError (String error);
	
	/**
	 * Displays a message.
	 * @param msg - Message.
	 */
	public void displayMessage(String msg);
	
	/**
	 * Displays a message with maze with array of bytes.
	 * @param arr - Maze with array of bytes
	 * @param msg - Message
	 */
	public void displayMessageWithMaze(byte[] arr , String msg);
	
	/**
	 * Displays a 3d maze that generated.
	 * @param byteArray - Byte array representing the maze 3d.
	 * @throws Exception exception
	 */
	public void displayMaze(byte[] byteArray) throws Exception;
	
	/**
	 * Display he cross section that the client asked for.
	 * @param crossSection - Cross section , a matrix (2d array).
	 */
	public void displayCrossSection(int[][] crossSection) ;
	
	/**
	 * Display the solution of the maze.
	 * @param solution - Solution of the maze.
	 */
	public void displaySolution(Solution<String> solution);
}
