package view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Observable;
import java.util.Observer;

import algorithms.mazeGenerators.Maze3d;
import algorithms.search.Solution;

public class MyView extends Observable implements View , Observer {

	private BufferedReader in;
	private PrintWriter out;
	private CLI cli;
	
	public MyView(BufferedReader in , PrintWriter out) {
		this.in = in;
		this.out = out;
		cli = new CLI(this.in,this.out);
		cli.addObserver(this);
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if(o == cli) {
			this.setChanged();
			this.notifyObservers(arg);
		}
		
	}
	
	@Override
	public void start() {
		cli.start();
	}

	@Override
	public void displayDirPath(String[] dirArray) {
		System.out.println("Files and directories in this path: ");
		out.flush();
		for(String s : dirArray) {
			out.println(s);
			out.flush();
		}

	}

	@Override
	public void displayError(String error) {
		out.println(error);
		out.flush();
	}


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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

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
	
	@Override
	public void displayMessageWithMaze(byte[] arr , String msg) {}
	
	@Override
	public void displayMessage(String msg) {
		out.println(msg);
		out.flush();
	}

	@Override
	public void displaySolution(Solution<String> solution) {
		out.println(solution);
		out.flush();
	}
	
//	public void displayProperties(Properties p) {
//	}

}
