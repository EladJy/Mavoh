package view;

import algorithms.mazeGenerators.Position;

public class Sync {
	MazeDisplay mazeDisplay;
	MazeWindow mazeWindow;
	
	public Sync() {
		mazeDisplay = null;
		mazeWindow = null;
	}
	
	public Sync(MazeDisplay mazeDisplay , MazeWindow mazeWindow) {
		this.mazeDisplay = mazeDisplay;
		this.mazeWindow = mazeWindow;
	}
	
	public void setCurrentPosition() {
		Position position = mazeDisplay.getCurrentPosition();
		mazeWindow.setCurrentPosition(position);
	}
	
	public void enablePerspective() {
		mazeWindow.setPerspective.setEnabled(true);
	}

	public void setMazeDisplay(MazeDisplay mazeDisplay) {
		this.mazeDisplay = mazeDisplay;
	}

	public void setMazeWindow(MazeWindow mazeWindow) {
		this.mazeWindow = mazeWindow;
	}
}
