package view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Observable;
import java.util.Observer;

import algorithms.mazeGenerators.Maze3d;
import algorithms.search.Solution;

/**
 * Display all the generated information for specific command.
 * @author Elad Jarby
 * @version 1.0
 * @since 13.09.2016
 */
public class MyView extends Observable implements View , Observer {

	private BufferedReader in;
	private PrintWriter out;
	private CLI cli;

	/**
	 * Constructor to initialize all the fields.
	 * @param in - Input reader.
	 * @param out - Output writer.
	 */
	public MyView(BufferedReader in , PrintWriter out) {
		this.in = in;
		this.out = out;
		cli = new CLI(this.in,this.out);
		cli.addObserver(this);
	}

	/**
	 * Update the observer function
	 * @param o - Observable
	 * @param arg - Object
	 */
	@Override
	public void update(Observable o, Object arg) {
		if(o == cli) {
			this.setChanged();
			this.notifyObservers(arg);
		}

	}

	/**
	 * Starts CLI Loop
	 */
	@Override
	public void start() {
		cli.start();
	}

	/**
	 * Display a list of files/directories for specific path.
	 * @param dirArray - Array of string , containing  files/directories.
	 */
	@Override
	public void displayDirPath(String[] dirArray) {
		System.out.println("Files and directories in this path: ");
		out.flush();
		for(String s : dirArray) {
			out.println(s);
			out.flush();
		}

	}

	/**
	 * Displays an error message.
	 * @param error - Error message.
	 */
	@Override
	public void displayError(String error) {
		out.println(error);
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
				displayCrossSection(maze.getCrossSectionByZ(z));
			}
		} catch (IOException e) {
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
	 * Displays a message with maze with array of bytes.
	 * @param arr - Maze with array of bytes
	 * @param msg - Message
	 */
	@Override
	public void displayMessageWithMaze(byte[] arr , String msg) {}

	/**
	 * Displays a message.
	 * @param msg - Message.
	 */
	@Override
	public void displayMessage(String msg) {
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
