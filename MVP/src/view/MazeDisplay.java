package view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;
import algorithms.search.State;

/**
 * Maze display class , to display the game board
 * @author Elad Jarby
 * @version 1.0
 * @since 19.09.2016
 */
public class MazeDisplay extends Canvas {
	int[][]mazeData;
	int[][][]maze3DArray;
	boolean inGame;
	String axis;
	Timer timer;
	TimerTask timerTask;
	int goalLevel;
	Solution<String> solution;
	boolean displaySolution;
	GameCharacter gameCharacter;
	Position previousPosition;
	Position currentPosition;
	Position goalPosition;
	Image wall , free , goal;
	Image winner;
	public static final int FREE = 0;
	public static final int WALL = 1;
	/**
	 * Constructor to initialize all the parameters
	 * @param parent - Parent shell 
	 * @param style - SWT Style
	 */
	public MazeDisplay(Composite parent , int style) {
		super(parent,style);
		goalLevel = 1;
		inGame = false;
		goalPosition = new Position();
		gameCharacter = new GameCharacter();
		wall = new Image(getDisplay(),"resources/wall.jpg");
		free = new Image(getDisplay(),"resources/floor.jpg");
		goal = new Image(getDisplay(),"resources/goal.png");

		/**
		 * Paint listener - if the window is changing
		 */
		addPaintListener(new PaintListener() {

			@Override
			public void paintControl(PaintEvent e) {
				if(inGame) {
					int length = getSize().x;
					int width = getSize().y;

					int cellX = length / mazeData[0].length;
					int cellY = width / mazeData.length;

					for (int i = 0; i < mazeData.length; i++) {
						for (int j = 0; j < mazeData[i].length; j++) {
							int x = cellX * j;
							int y = cellY * i;
							if(mazeData[i][j] != FREE) {
								e.gc.drawImage(wall, 0, 0, wall.getBounds().width, wall.getBounds().height, x, y, cellX, cellY);
							} else {
								e.gc.drawImage(free, 0, 0, free.getBounds().width, free.getBounds().height, x, y, cellX, cellY);
								if(checkIsGoal(axis,goalLevel,i,j))
									e.gc.drawImage(goal, 0, 0, goal.getBounds().width, goal.getBounds().height, x, y, cellX, cellY);
							}
						}
					}

					gameCharacter.paint(e, cellX, cellY);
					if(axis.equals("z")) {
						gameCharacter.x = currentPosition.getX() * cellX;
						gameCharacter.y = currentPosition.getY() * cellY;
					} else if(axis.equals("y")) {
						gameCharacter.x = currentPosition.getX() * cellX;
						gameCharacter.y = currentPosition.getZ() * cellY;
					} else if(axis.equals("x")) {
						gameCharacter.x = currentPosition.getY() * cellX;
						gameCharacter.y = currentPosition.getZ() * cellY;
					}
				}
			}
		});
	}

	/**
	 * Setter for start the game
	 */
	public void startGame() {
		inGame = true;
	}

	/**
	 * Setter for stop the game
	 */
	public void stopGame() {
		inGame = false;
	}

	/**
	 * Function that check if in game or not
	 * @return True - if in game , otherwise false
	 */
	public boolean isInGame() {
		return inGame;
	}

	/**
	 * Set 2D maze data
	 * @param mazeData - 2D Maze
	 */
	public void setMazeData(int[][]mazeData) {
		this.mazeData = mazeData;

		int length = getSize().x;
		int width = getSize().y;

		int cellX = length / mazeData[0].length;
		int cellY = width / mazeData.length;


		if(axis.equals("z")) {
			gameCharacter.x = currentPosition.getX() * cellX;
			gameCharacter.y = currentPosition.getY() * cellY;
		} else if(axis.equals("y")) {
			gameCharacter.x = currentPosition.getX() * cellX;
			gameCharacter.y = currentPosition.getZ() * cellY;
		} else if(axis.equals("x")) {
			gameCharacter.x = currentPosition.getY() * cellX;
			gameCharacter.y = currentPosition.getZ() * cellY;
		}

		redraw();
	}

	/**
	 * Set 3D Maze
	 * @param maze3DArray - 3D Maze
	 */
	public void set3DMaze(int[][][]maze3DArray) {
		this.maze3DArray = maze3DArray;
	}

	/**
	 * Set the solution to this solution
	 * @param solution - Solution to set
	 */
	public void setSolution(Solution<String> solution) {
		this.solution = solution;
	}

	/**
	 * Set new floor data
	 * @param pos - Set the data of the level according to position
	 */
	public void setNewFloorData(Position pos) {
		if(axis.equals("z")) {
			int z = pos.getZ();
			setLevel(z);
			if(z >= 0 && z < maze3DArray.length - 1) {
				for(int y = 0 ; y < maze3DArray[0].length ; y++) {
					for(int x = 0 ; x < maze3DArray[0][0].length ; x++) {
						mazeData[y][x] = maze3DArray[z][y][x];
					}
				}
			}
		}

		if(axis.equals("y")) {
			int y = pos.getY();
			setLevel(y);
			if(y >= 0 && y < maze3DArray[0].length - 1) {
				for(int z = 0 ; z < maze3DArray.length ; z++) {
					for(int x = 0 ; x < maze3DArray[0][0].length ; x++) {
						mazeData[z][x] = maze3DArray[z][y][x];
					}
				}
			}
		}

		if(axis.equals("x")) {
			int x = pos.getX();
			setLevel(x);
			if(x >= 0 && x < maze3DArray[0][0].length - 1) {
				for(int z = 0 ; z < maze3DArray.length ; z++) {
					for(int y = 0 ; y < maze3DArray[0].length ; y++) {
						mazeData[z][y] = maze3DArray[z][y][x];
					}
				}
			}
		}

	}

	/**
	 * Start display the solution of the maze
	 */
	public void start() {
		displaySolution = true;
		ArrayList<State<String>> solutionPath = solution.getStates();
		Collections.reverse(solutionPath);
		setAxis("z");
		setNewFloorData((new Position()).toPosition(solutionPath.get(0).getValue().toString()));
		int startPositionY = ((new Position()).toPosition(solutionPath.get(0).getValue().toString())).getY();
		int startPositionX = ((new Position()).toPosition(solutionPath.get(0).getValue().toString())).getX();
		timer = new Timer();
		timerTask = new TimerTask() {

			@Override
			public void run() {
				getDisplay().syncExec(new Runnable() {
					public void run() {
						if(!solutionPath.isEmpty()) {
							int length = getSize().x;
							int width= getSize().y;

							int cellX = length / mazeData[0].length + startPositionX / mazeData[0].length;
							int cellY = width / mazeData.length + startPositionY / mazeData.length;

							Position currentPosition = new Position();
							if(!solutionPath.isEmpty()) {
								currentPosition = currentPosition.toPosition(solutionPath.get(0).getValue().toString());
								solutionPath.remove(0);
								gameCharacter.x = currentPosition.getX() * cellX;
								gameCharacter.y = currentPosition.getY() * cellY;
								setCurrentPosition(currentPosition);
								setNewFloorData(currentPosition);
								redraw();
							}
						} else {
							stopDisplaySolution();
							if(isInGoalPosition()) {
								displayWinningMsg();
								stopGame();
							}
						}

					}
				});
			}
		};
		timer.scheduleAtFixedRate(timerTask, 0, 300);

	}

	/**
	 * Display winning message
	 */
	public void displayWinningMsg() {
		getDisplay().syncExec(new Runnable() {
			public void run() {
				final Shell shell = new Shell();
				shell.setText("YOU WIN!");
				shell.setLayout(new FillLayout());

				Canvas canvas = new Canvas(shell, SWT.NONE);

				canvas.addPaintListener(new PaintListener() {

					@Override
					public void paintControl(PaintEvent e) {
						Image image = new Image(getDisplay() , "./resources/winner.jpg");
						e.gc.drawImage(image, 0, 0);
						image.dispose();						
					}
				});

				shell.setSize(500,500);
				shell.open();
			}
		});
	}

	/**
	 * Stop to display solution
	 */
	public void stopDisplaySolution() {
		if (displaySolution) {
			displaySolution = false;
			timer.cancel();
			timerTask.cancel();
		}
	}

	/**
	 * Move character left in the maze according to axis - x / y / z
	 */
	public void moveLeft() {
		int length= getSize().x;
		int cellX = length/mazeData[0].length;
		if(axis.equals("z")) {
			if(currentPosition.getX() > 0) {
				if(maze3DArray[currentPosition.getZ()][currentPosition.getY()][currentPosition.getX() - 1] == 0) {
					gameCharacter.x = (currentPosition.getX() - 1) * cellX;
					currentPosition.setX(currentPosition.getX() - 1);
					redraw();
				}
			}
		} else if (axis.equals("y")) {
			if(currentPosition.getX() > 0) {
				if(mazeData[currentPosition.getZ()][currentPosition.getX() - 1] == 0) {
					gameCharacter.x = (currentPosition.getX() - 1) * cellX;
					currentPosition.setX(currentPosition.getX() - 1);
					redraw();
				}
			}
		} else if (axis.equals("x")) {
			if(currentPosition.getY() > 0) {
				if(mazeData[currentPosition.getZ()][currentPosition.getY() - 1] == 0) {
					gameCharacter.x = (currentPosition.getY() - 1) * cellX;
					currentPosition.setY(currentPosition.getY() - 1);
					redraw();
				}
			}
		}
	}

	/**
	 * Move character right in the maze according to axis - x / y / z
	 */
	public void moveRight() {
		int length= getSize().x;
		int cellX = length/mazeData[0].length;
		if(axis.equals("z")) {
			if(currentPosition.getX() < maze3DArray[0][0].length - 1) {
				if(maze3DArray[currentPosition.getZ()][currentPosition.getY()][currentPosition.getX() + 1] == 0) {
					gameCharacter.x = (currentPosition.getX() + 1) * cellX;
					currentPosition.setX(currentPosition.getX() + 1);
					redraw();
				}
			}
		} else if (axis.equals("y")) {
			if(currentPosition.getX() < maze3DArray[0][0].length - 1) {
				if(mazeData[currentPosition.getZ()][currentPosition.getX() + 1] == 0) {
					gameCharacter.x = (currentPosition.getX() + 1) * cellX;
					currentPosition.setX(currentPosition.getX() + 1);
					redraw();
				}
			}
		} else if (axis.equals("x")) {
			if(currentPosition.getY() < maze3DArray[0].length - 1) {
				if(mazeData[currentPosition.getZ()][currentPosition.getY() + 1] == 0) {
					gameCharacter.x = (currentPosition.getY() + 1) * cellX;
					currentPosition.setY(currentPosition.getY() + 1);
					redraw();
				}
			}
		}
	}

	/**
	 * Move character backward in the maze according to axis - x / y / z
	 */
	public void moveBackward() {
		int width = getSize().y;
		int cellY = width / mazeData.length;

		if(axis.equals("z")) {
			if(currentPosition.getY() > 0) {
				if(maze3DArray[currentPosition.getZ()][currentPosition.getY() - 1][currentPosition.getX()] == 0) {
					gameCharacter.y = (currentPosition.getY() - 1) * cellY;
					currentPosition.setY(currentPosition.getY() - 1);
				}
			}
		} else if (axis.equals("y")) {
			if(currentPosition.getZ() > 0) {
				if(mazeData[currentPosition.getZ() - 1][currentPosition.getX()] == 0) {
					gameCharacter.y = (currentPosition.getZ() - 1) * cellY;
					currentPosition.setZ(currentPosition.getZ() - 1);
				}
			}
		} else if (axis.equals("x")) {
			if(currentPosition.getZ() > 0) {
				if(mazeData[currentPosition.getZ()-1][currentPosition.getY() ] == 0) {
					gameCharacter.y = (currentPosition.getZ() - 1) * cellY;
					currentPosition.setZ(currentPosition.getZ() - 1);
				}
			}
		}
		redraw();
	}

	/**
	 * Move character forward in the maze according to axis - x / y / z
	 */
	public void moveForward() {
		int width = getSize().y;
		int cellY = width / mazeData.length;

		if(axis.equals("z")) {
			if(currentPosition.getY() < maze3DArray[0].length - 1) {
				if(maze3DArray[currentPosition.getZ()][currentPosition.getY() + 1][currentPosition.getX()] == 0) {
					gameCharacter.y = (currentPosition.getY() + 1) * cellY;
					currentPosition.setY(currentPosition.getY() + 1);
				}
			}
		} else if (axis.equals("y")) {
			if(currentPosition.getZ() < maze3DArray.length - 1) {
				if(mazeData[currentPosition.getZ() + 1][currentPosition.getX()] == 0) {
					gameCharacter.y = (currentPosition.getZ() + 1) * cellY;
					currentPosition.setZ(currentPosition.getZ() + 1);	
				}
			}
		} else if (axis.equals("x")) {
			if(currentPosition.getZ() < maze3DArray.length - 1) {
				if(mazeData[currentPosition.getZ() + 1][currentPosition.getY()] == 0) {
					gameCharacter.y = (currentPosition.getZ() + 1) * cellY;
					currentPosition.setZ(currentPosition.getZ() + 1);
				}
			}
		}
		redraw();
	}

	/**
	 * Move character up in the maze according to axis - x / y / z
	 */
	public void moveUp() {
		if(axis.equals("z")) {
			if(currentPosition.getZ() < maze3DArray.length - 1) {
				if(maze3DArray[currentPosition.getZ() + 1][currentPosition.getY()][currentPosition.getX()] == 0) {
					currentPosition.setZ(currentPosition.getZ() + 1);
					setNewFloorData(currentPosition);
					setLevel(currentPosition.getZ());
				}
			}
		} else if(axis.equals("y")) {
			if(currentPosition.getY() < maze3DArray[0].length - 1) {
				if(maze3DArray[currentPosition.getZ()][currentPosition.getY() + 1][currentPosition.getX()] == 0) {
					currentPosition.setY(currentPosition.getY() + 1);
					setNewFloorData(currentPosition);
					setLevel(currentPosition.getY());
				}
			}
		} else if(axis.equals("x")) {
			if(currentPosition.getX() < maze3DArray[0][0].length - 1) {
				if(maze3DArray[currentPosition.getZ()][currentPosition.getY()][currentPosition.getX() + 1] == 0) {
					currentPosition.setX(currentPosition.getX() + 1);
					setNewFloorData(currentPosition);
					setLevel(currentPosition.getX());
				}
			}
		}
		redraw();
	}

	/**
	 * Move character down in the maze according to axis - z / y / x
	 */
	public void moveDown() {
		if(axis.equals("z")) {
			if(currentPosition.getZ() > 0) {
				if(maze3DArray[currentPosition.getZ() - 1][currentPosition.getY()][currentPosition.getX()] == 0) {
					currentPosition.setZ(currentPosition.getZ() - 1);
					setNewFloorData(currentPosition);
					setLevel(currentPosition.getZ());
				}
			}
		} else if(axis.equals("y")) {
			if(currentPosition.getY() > 0) {
				if(maze3DArray[currentPosition.getZ()][currentPosition.getY() - 1][currentPosition.getX()] == 0) {
					currentPosition.setY(currentPosition.getY() - 1);
					setNewFloorData(currentPosition);
					setLevel(currentPosition.getY());
				}
			}
		} else if(axis.equals("x")) {
			if(currentPosition.getX() > 0) {
				if(maze3DArray[currentPosition.getZ()][currentPosition.getY()][currentPosition.getX() - 1] == 0) {
					currentPosition.setX(currentPosition.getX() - 1);
					setNewFloorData(currentPosition);
					setLevel(currentPosition.getX());
				}
			}
		}
		redraw();
	}

	/**
	 * Function to perform zoom in or zoom out with ctrl + mouse wheel
	 * @param scroll - Positive for zoom in , Negative for zoom out
	 */
	public void performZoom(int scroll) {
		int length = getSize().x;
		int width = getSize().y;


		if(scroll < 0)
			setSize((int)(length*0.99), (int)(width*0.99));
		else
			setSize((int)(length*1.01), (int)(width*1.01));
	}

	/**
	 * Setter for axis - z / y /x 
	 * @param axis - String that represents axis z / y / x
	 */
	public void setAxis(String axis) {
		this.axis = axis;
	}

	/**
	 * Setter for current position
	 * @param position - Position
	 */
	public void setCurrentPosition(Position position) {
		this.currentPosition = position;
	}

	/**
	 * Getter to get the current position
	 * @return Current position
	 */
	public Position getCurrentPosition () {
		return currentPosition;
	}
	
	/**
	 * Setter to set the goal level
	 * @param level - Goal level
	 */
	public void setLevel(int level) {
		this.goalLevel = level;
	}
	
	/**
	 * Setter to set the goal position of the maze
	 * @param pos - Goal position of the maze
	 */
	public void setGoalPosition(Position pos) {
		this.goalPosition = pos;
	}

	/**
	 * Function to check between current position to goal position
	 * @return True if is in goal position , otherwise return false
	 */
	public boolean isInGoalPosition() {
		if(currentPosition.equals(goalPosition)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Function to check where to set the goal position image for axis: z / y / x
	 * @param axis - Axis z / y / x
	 * @param level - Goal position level
	 * @param i - Length cell in the maze of the goal position image need to be set
	 * @param j - Length cell in the maze the goal position image need to be set
	 * @return - True if found goal position need to be set , otherwise return false
	 */
	private boolean checkIsGoal(String axis, int level, int i, int j) {
		if(axis.equals("z")) {
			if(level == goalPosition.getZ() && i == goalPosition.getY() && j == goalPosition.getX())
				return true;
		} else if(axis.equals("y")) {
			if(level == goalPosition.getY() && i == goalPosition.getZ() && j == goalPosition.getX())
				return true;
		} else if(axis.equals("x")) {
			if(level == goalPosition.getX() && i == goalPosition.getZ() && j == goalPosition.getY())
				return true;
		}

		return false;
	}
}
