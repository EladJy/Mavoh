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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;
import algorithms.search.State;

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
	public static final int FREE = 0;
	public static final int WALL = 1;

	public MazeDisplay(Composite parent , int style) {
		super(parent,style);
		goalLevel = 1;
		inGame = false;
		goalPosition = new Position();
		gameCharacter = new GameCharacter(0,0);
		currentPosition = new Position(1,1,1);
		Image wall=new Image(getDisplay(),"resources/wall.jpg");
		Image free=new Image(getDisplay(),"resources/floor.jpg");
		Image goal=new Image(getDisplay(),"resources/goal.png");

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

	public void startGame() {
		inGame = true;
	}

	public void stopGame() {
		inGame = false;
	}

	public boolean isInGame() {
		return inGame;
	}

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

	public void set3DMaze(int[][][]maze3DArray) {
		this.maze3DArray = maze3DArray;
	}

	public void setSolution(Solution<String> solution) {
		this.solution = solution;
	}


	public void setNewFloorData(Position pos) {
		if(axis.equals("z")) {
			int z = pos.getZ();
			setLevel(z);
			if(z >= 0 && z < maze3DArray.length) {
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
			if(y > 0 && y < maze3DArray[0].length - 1) {
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
			if(x > 0 && x < maze3DArray[0][0].length - 1) {
				for(int z = 0 ; z < maze3DArray.length ; z++) {
					for(int y = 0 ; y < maze3DArray[0].length ; y++) {
						mazeData[z][y] = maze3DArray[z][y][x];
					}
				}
			}
		}

	}

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

	public void displayWinningMsg() {
		Thread thread = new Thread(new Runnable() {
			public void run() {
				final Display display = new Display();
				final Shell shell = new Shell();
				shell.setText("YOU WIN!");
				shell.setLayout(new FillLayout());

				Canvas canvas = new Canvas(shell, SWT.NONE);

				canvas.addPaintListener(new PaintListener() {

					@Override
					public void paintControl(PaintEvent e) {
						Image image = new Image(display , "./resources/winner.jpg");
						e.gc.drawImage(image, 0, 0);
						image.dispose();						
					}
				});

				shell.setSize(500,500);
				shell.open();
				while(!shell.isDisposed()) {
					if(!display.readAndDispatch()) {
						display.sleep();
					}
				}
				display.dispose();
			}
		});
		thread.start();
	}

	public void stopDisplaySolution() {
		if (displaySolution) {
			displaySolution = false;
			timer.cancel();
			timerTask.cancel();
		}
	}
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

	public void moveRight() {
		int length= getSize().x;
		int cellX = length/mazeData[0].length;
		if(axis.equals("z")) {
			if(currentPosition.getX() < maze3DArray[0][0].length) {
				if(maze3DArray[currentPosition.getZ()][currentPosition.getY()][currentPosition.getX() + 1] == 0) {
					gameCharacter.x = (currentPosition.getX() + 1) * cellX;
					currentPosition.setX(currentPosition.getX() + 1);
					redraw();
				}
			}
		} else if (axis.equals("y")) {
			if(currentPosition.getX() < maze3DArray[0][0].length) {
				if(mazeData[currentPosition.getZ()][currentPosition.getX() + 1] == 0) {
					gameCharacter.x = (currentPosition.getX() + 1) * cellX;
					currentPosition.setX(currentPosition.getX() + 1);
					redraw();
				}
			}
		} else if (axis.equals("x")) {
			if(currentPosition.getY() < maze3DArray[0].length) {
				if(mazeData[currentPosition.getZ()][currentPosition.getY() + 1] == 0) {
					gameCharacter.x = (currentPosition.getY() + 1) * cellX;
					currentPosition.setY(currentPosition.getY() + 1);
					redraw();
				}
			}
		}
	}

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

	public void performZoom(int scroll) {
		int length = getSize().x;
		int width = getSize().y;


		if(scroll < 0)
			setSize((int)(length*0.99), (int)(width*0.99));
		else
			setSize((int)(length*1.01), (int)(width*1.01));
	}


	public void setAxis(String axis) {
		this.axis = axis;
	}

	public void setCurrentPosition(Position position) {
		this.currentPosition = position;
	}

	public Position getCurrentPosition () {
		return currentPosition;
	}
	public void setLevel(int level) {
		this.goalLevel = level;
	}
	public void setGoalPosition(Position pos) {
		this.goalPosition = pos;
	}

	public boolean isInGoalPosition() {
		if(currentPosition.equals(goalPosition)) {
			return true;
		} else {
			return false;
		}
	}

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
