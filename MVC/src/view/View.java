package view;

import java.util.HashMap;

import algorithms.search.Solution;
import controller.Command;

public interface View {
	public void start();
	
	public void setStringToCommand(HashMap<String, Command> stringToCommand);
	
	public void displayDirPath(String[] dirArray);
	
	public void displayError (String msg);
	
	public void displayGenerate3dMaze(String msg);
	
	public void displayMaze(byte[] byteArray) throws Exception;
	
	public void displayCrossSection(int[][] crossSection) ;
	
	public void displaySaveMaze(String str);
	
	public void displayLoadMaze(String str);
	
	public void displaySolutionReady(String msg);
	
	public void displaySolution(Solution<String> solution);
}
