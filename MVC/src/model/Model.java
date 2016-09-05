package model;


public interface Model {
	
	public void dirPath(String[] dirArray);

	public void generate3dMaze(String[] arr);
	
	public void getMaze(String[] arr);
	
	public void getCrossSection(String[] arr) ;
	
	public void saveMaze(String[] arr);
	
	public void loadMaze(String[] arr);
	
	public void mazeSize(String[] arr);
	
	public void fileSize(String[] arr);
	
	public void getSolutionReady(String[] arr);
	
	public void getSolution(String[] arr);
	
	public void exitCommand(String[] emptyArr);
}
