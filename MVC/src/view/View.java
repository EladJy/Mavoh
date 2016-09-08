package view;

import java.util.HashMap;

import algorithms.search.Solution;
import controller.Command;

/**
 * View interface which display all the generated information for specific command.
 * @author Elad Jarby
 * @version 1.0
 * @since 06.09.2016
 */
public interface View {
	
	/**
	 * Starting the CLI.
	 */
	public void start();
	
	/**
	 * Set the list of commands that supported by the controller.
	 * @param stringToCommand - Hash map with the key-name of commands and the value of command.
	 */
	public void setStringToCommand(HashMap<String, Command> stringToCommand);
	
	/**
	 * Display a list of files/directories for specific path.
	 * @param dirArray - Array of string , containing  files/directories.
	 */
	public void displayDirPath(String[] dirArray);
	
	/**
	 * Displays an error message.
	 * @param msg - Error message.
	 */
	public void displayError (String msg);
	
	/**
	 * Displays a 3d maze that was generated successfully and ready..
	 * @param msg - Message says that the maze is ready.
	 */
	public void displayGenerate3dMaze(String msg);
	
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
	 * Display a message that saying the maze has been saved.
	 * @param str - Message that saying - the maze has been saved.
	 */
	public void displaySaveMaze(String str);
	
	/**
	 * Display a message that saying the maze has been loaded.
	 * @param str - Message that saying - the maze has been loaded.
	 */
	public void displayLoadMaze(String str);
	
	/**
	 * Display a message that the maze was solved.
	 * @param msg - Message that the maze was solved.
	 */
	public void displaySolutionReady(String msg);
	
	/**
	 * Display the solution of the maze.
	 * @param solution - Solution of the maze.
	 */
	public void displaySolution(Solution<String> solution);
}
