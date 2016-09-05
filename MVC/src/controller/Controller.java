package controller;

import algorithms.search.Solution;
import model.Model;
import view.View;

public interface Controller {
	
	void setModel(Model model);
	
	void setView(View view);
	
	public void displayDirPath(String[] dirArray);
	
	public void displayError (String msg);
	
	public void displayGenerate3dMaze(String msg);
	
	public void displayMaze(byte[] byteArray) throws Exception;
	
	public void displayCrossSection(int[][] crossSection) ;
	
	public void displaySaveMaze(String str);
	
	public void displayLoadMaze(String str);
	
	public void displayMazeSize(int size);
	
	public void displayFileSize(long size);
	
	public void displaySolutionReady(String msg);
	
	public void displaySolution(Solution<String> solution);
}
