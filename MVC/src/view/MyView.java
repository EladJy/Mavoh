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

public class MyView extends CommonView {
	
	CLI cli;
    BufferedReader in;
    PrintWriter out;
	HashMap<String, Command> stringToCommand;

	public MyView(Controller controller) {
		super(controller);
		in = new BufferedReader(new InputStreamReader(System.in));
		out = new PrintWriter(System.out);
		cli = new CLI(in,out);
	}

	@Override
	public void start() {
		cli.start();		
	}

	@Override
	public void setStringToCommand(HashMap<String, Command> stringToCommand) {
		this.stringToCommand = stringToCommand;
		cli.setStringToCommand(stringToCommand);		
	}

	@Override
	public void displayDirPath(String[] dirArray) {
		System.out.println("Files and directories in this path: ");
		for(String s:dirArray) {
			System.out.println(s);
		}
	}

	@Override
	public void displayError(String msg) {
		System.out.println(msg);
	}

	@Override
	public void displayGenerate3dMaze(String msg) {
		System.out.println(msg);		
	}

	@Override
	public void displayMaze(byte[] byteArray) throws Exception {
		Maze3d maze;
		try {
			maze = new Maze3d(byteArray);
			for(int z=0; z < maze.getHeight(); z++)
			{
				System.out.println();
				System.out.println("Level: "+z);
				displayCrossSection(maze.getCrossSectionByZ(z),"z");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	@Override
	public void displayCrossSection(int[][] crossSection , String axis) {
		if(axis.equals("x"))
			System.out.println("Cross section by X:");
		if(axis.equals("y"))
			System.out.println("Cross section by Y:");
		if(axis.equals("z"))
			System.out.println("Cross section by Z:");
		for(int i = 0; i < crossSection.length; i++)
		{
			for(int j = 0; j < crossSection[0].length; j++)
			{
				System.out.print(crossSection[i][j] + " ");
			}
			System.out.println();
		}	
		
	}

	@Override
	public void displaySaveMaze(String str) {
		System.out.println(str);		
	}

	@Override
	public void displayLoadMaze(String str) {
		System.out.println(str);
	}

	@Override
	public void displayMazeSize(int size) {
		System.out.println("Size of maze is: " + size);		
	}

	@Override
	public void displayFileSize(long size) {
		System.out.println("Size of file is: " + size);		
	}

	@Override
	public void displaySolutionReady(String msg) {
		System.out.println(msg);
	}

	@Override
	public void displaySolution(Solution<String> solution) {
		System.out.println(solution);
	}

}
