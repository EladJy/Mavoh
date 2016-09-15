package view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;

import algorithms.mazeGenerators.Maze3d;
import algorithms.search.Solution;
import controller.Command;
import controller.Controller;

/**
 * Display all the generated information for specific command.
 * @author Elad Jarby
 * @version 1.0
 * @since 06.09.2016
 */
public class MyView extends CommonView {
	
	CLI cli;
    BufferedReader in;
    PrintWriter out;
	HashMap<String, Command> stringToCommand;

	/**
	 * Constructor to initialize all the fields.
	 * @param controller - The controller that this View use.
	 */
	public MyView(Controller controller) {
		super(controller);
		in = new BufferedReader(new InputStreamReader(System.in));
		out = new PrintWriter(System.out);
		cli = new CLI(in,out);
	}

	/**
	 * Starting the CLI.
	 */
	@Override
	public void start() {
		cli.start();		
	}

	/**
	 * Set the list of commands that supported by the controller.
	 * @param stringToCommand - Hash map with the key-name of commands and the value of command.
	 */
	@Override
	public void setStringToCommand(HashMap<String, Command> stringToCommand) {
		this.stringToCommand = stringToCommand;
		cli.setStringToCommand(stringToCommand);		
	}

	/**
	 * Display a list of files/directories for specific path.
	 * @param dirArray - Array of string , containing  files/directories.
	 */
	@Override
	public void displayDirPath(String[] dirArray) {
		out.println("Files and directories in this path: ");
		for(String s:dirArray) {
			out.println(s);
		}
		out.flush();
	}

	/**
	 * Displays an error message.
	 * @param msg - Error message.
	 */
	@Override
	public void displayError(String msg) {
		out.println(msg);
		out.flush();
	}

	/**
	 * Displays a 3d maze that was generated successfully and ready..
	 * @param msg - Message says that the maze is ready.
	 */
	@Override
	public void displayGenerate3dMaze(String msg) {
		out.println(msg);		
		out.flush();
	}

	/**
	 * Displays a 3d maze that generated.
	 * @param byteArray - Byte array representing the maze 3d.
	 * @throws Exception exception
	 */
	@Override
	public void displayMaze(byte[] byteArray) throws Exception {
		Maze3d maze;
		try {
			maze = new Maze3d(byteArray);
			for(int z=0; z < maze.getHeight(); z++)
			{
				out.println();
				out.println("Level: "+z);
				out.flush();
				displayCrossSection(maze.getCrossSectionByZ(z));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	/**
	 * Display he cross section that the client asked for.
	 * @param crossSection - Cross section , a matrix (2d array).
	 */
	@Override
	public void displayCrossSection(int[][] crossSection) {
		for(int i = 0; i < crossSection.length; i++)
		{
			for(int j = 0; j < crossSection[0].length; j++)
			{
				out.print(crossSection[i][j] + " ");
			}
			out.println();
		}	
		out.flush();
		
	}

	/**
	 * Display a message that saying the maze has been saved.
	 * @param str - Message that saying - the maze has been saved.
	 */
	@Override
	public void displaySaveMaze(String str) {
		out.println(str);
		out.flush();
	}

	/**
	 * Display a message that saying the maze has been loaded.
	 * @param str - Message that saying - the maze has been loaded.
	 */
	@Override
	public void displayLoadMaze(String str) {
		out.println(str);
		out.flush();
	}

	/**
	 * Display a message that the maze was solved.
	 * @param msg - Message that the maze was solved.
	 */
	@Override
	public void displaySolutionReady(String msg) {
		out.println(msg);
		out.flush();
	}

	/**
	 * Display the solution of the maze.
	 * @param solution - Solution of the maze.
	 */
	@Override
	public void displaySolution(Solution<String> solution) {
		out.println(solution);
		out.flush();
	}

}
