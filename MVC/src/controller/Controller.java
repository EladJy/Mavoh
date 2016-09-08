package controller;

import algorithms.search.Solution;
import model.Model;
import view.View;

/**
 * Controller that link between view and model.</br>
 * Transfer commands and results between the View and Model.
 * @author Elad Jarby
 * @version 1.0
 * @since 04.09.2016
 */
public interface Controller {
	/**
	 * Sets the model of the controller , to pass the command and generate solution.
	 * @param model - Model of the controller.
	 */
	void setModel(Model model);
	
	/**
	 * Sets the view of the controller , to pass the command to be displayed.
	 * @param view - View of the controller.
	 */
	void setView(View view);
	
	/**
	 * Transfer the names of the files and directories to the view.
	 * @param dirArray - Array of strings containing the names of the files and directories in the specific path.
	 */
	public void displayDirPath(String[] dirArray);
	
	/**
	 * Transfer an error message.
	 * @param msg - Error message.
	 */
	public void displayError (String msg);
	
	/**
	 * Transfer the 3d maze was generated.
	 * @param msg - Message that the 3d maze is ready.
	 */
	public void displayGenerate3dMaze(String msg);
	
	/**
	 * Transfer the generated maze to the view to display the maze.
	 * @param byteArray - Byte array that containing the maze 3d.
	 * @throws Exception - exception
	 */
	public void displayMaze(byte[] byteArray) throws Exception;
	
	/**
	 * Transfer the cross section to view.
	 * @param crossSection - Cross section , a matrix(2d array).
	 */
	public void displayCrossSection(int[][] crossSection) ;
	
	/**
	 * Transfer the string that saying the maze has been saved.
	 * @param msg - Message that saying - the maze has been saved.
	 */
	public void displaySaveMaze(String msg);
	
	/**
	 * Transfer the string that saying the maze has been loaded
	 * @param msg - Message that saying - the maze has been loaded.
	 */
	public void displayLoadMaze(String msg);
	
	/**
	 * Transfer a message that the maze was solved.
	 * @param msg - Message that the maze was solved.
	 */
	public void displaySolutionReady(String msg);
	
	/**
	 * Transfer the solution of the maze.
	 * @param solution - Solution of the maze.
	 */
	public void displaySolution(Solution<String> solution);
}
