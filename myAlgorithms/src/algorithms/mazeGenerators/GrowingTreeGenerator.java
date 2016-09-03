package algorithms.mazeGenerators;

import java.util.ArrayList;
import java.util.Random;

/**
 * Generates a 3D Maze by using "Growing tree Generator".
 * @author Elad Jarby
 *
 */
public class GrowingTreeGenerator extends CommonMaze3dGenerator {
	private Random r = new Random();
	private ChooseGrowingTree ct;
	private Maze3d maze;
	// Array to save all positions that we pass.
	private ArrayList<Position> savePositions = new ArrayList<Position>();

	/**
	 * Constructor , Initialize the select of growing tree.
	 * @param ct - The chosen growing tree.
	 */
	public GrowingTreeGenerator(ChooseGrowingTree ct) {
		this.ct = ct;
	}

	/**
	 * Overriding for generate method , generating growing tree maze.
	 * @param z - Height/Floors of the maze
	 * @param y - Width of the maze
	 * @param x - Length of the maze
	 * @return Maze3d - the maze
	 */
	@Override
	public Maze3d generate(int z,int y,int x) {
		// floors + 2 because we need floor and ceiling for to not get out of bounds
		maze = new Maze3d(z+2, y, x);
		maze.fillWithWalls();
		Position startPosition = chooseRandomPosition();
		if(startPosition.getZ() == 1)
		{
			maze.setFree(new Position(startPosition.getZ()-1,startPosition.getY(),startPosition.getX()));
			startPosition = new Position(startPosition.getZ()-1,startPosition.getY(),startPosition.getX());
		}
		maze.setStartPosition(startPosition);
		savePositions.add(startPosition);
		maze.setFree(startPosition);

		while(!savePositions.isEmpty()) {
			Position c = ct.selectPositionFromList(savePositions);
			Direction[] successors = getPossibleMoves(c);
			if(successors.length > 0) {
				int dirRandom = r.nextInt(successors.length);
				Position p = setNewPosition(c, successors, dirRandom);
				maze.setFree(p);
				savePositions.add(p);
			} else {
				savePositions.remove(c);
			}

		}
		Position goalPosition = chooseGoalPosition(startPosition);
		if(goalPosition.getZ() == maze.getHeight()-2 )
		{
			maze.setFree(new Position(startPosition.getZ()+1,startPosition.getY(),startPosition.getX()));
			goalPosition = (new Position(startPosition.getZ()+1,startPosition.getY(),startPosition.getX()));
		}
		maze.setGoalPosition(goalPosition);
		maze.setFreeOnEven();
		return maze;
	}

	/**
	 * Choose random position from floor,width,length.
	 * @return Position from the random floor,width,length.
	 */
	private Position chooseRandomPosition() {
		// Choose an odd row
		int x = r.nextInt(maze.getLength()-1);
		while (x % 2 == 0)
			x = r.nextInt(maze.getLength()-1);
		// Choose an odd column
		int y = r.nextInt(maze.getWidth()-1);
		while (y % 2 == 0)
			y = r.nextInt(maze.getWidth()-1);
		// Choose an odd floor
		int z = r.nextInt(maze.getHeight()-2)+1;
		return new Position(z,y,x);
	}
	
	/**
	 * Choose random goal position from length,floor,width.
	 * @param pos - To check if the goal position is not the start position.
	 * @return Position from the random floor,width,length.
	 */
	private Position chooseGoalPosition(Position pos) {
		int x,y,z;
		while(true){
			x = r.nextInt(maze.getLength());
			y = r.nextInt(maze.getWidth());
			z = r.nextInt(maze.getHeight());
			if(maze.getPointValue(z, y, x) == 0 && (z != pos.getZ() || y != pos.getY() || x != pos.getX())) {
				break;
			}
		}


//		while(maze.getPointValue(z, y, x) == 1 || (z == pos.getZ() && y == pos.getY() && x == pos.getX())) {
//			x = r.nextInt(maze.getLength());
//			y = r.nextInt(maze.getWidth());
//			z = r.nextInt(maze.getHeight());
//		}
		return new Position(z,y,x);
	}


	/**
	 * Set new position , left or right , up or down , forward or backward
	 * @param currentPosition - Current position in the maze.
	 * @param dirs - Array of directions that you can move from current position.
	 * @param r - Choose randomly from the array of directions.
	 * @return Position position.
	 */
	private Position setNewPosition(Position currentPosition, Direction[] dirs, int r) {
		Position newPosition = null;
		switch(dirs[r]) {
		case Right:
			maze.setPointValue(currentPosition.getZ(), currentPosition.getY(), currentPosition.getX()+1, 0);
			//maze.setPointValue(currentPosition.getZ(), currentPosition.getY(), currentPosition.getX()+2, 0);
			newPosition = new Position(currentPosition.getZ() , currentPosition.getY() , currentPosition.getX()+2);
			savePositions.add(newPosition);
			break;
		case Left:
			maze.setPointValue(currentPosition.getZ(), currentPosition.getY(), currentPosition.getX()-1, 0);
			//maze.setPointValue(currentPosition.getZ(), currentPosition.getY(), currentPosition.getX()-2, 0);
			newPosition = new Position(currentPosition.getZ() , currentPosition.getY() , currentPosition.getX()-2);
			savePositions.add(newPosition);
			break;
		case Backward:
			maze.setPointValue(currentPosition.getZ(), currentPosition.getY()+1, currentPosition.getX(), 0);
			//maze.setPointValue(currentPosition.getZ(), currentPosition.getY()+2, currentPosition.getX(), 0);
			newPosition = new Position(currentPosition.getZ() , currentPosition.getY()+2 , currentPosition.getX());
			savePositions.add(newPosition);
			break;
		case Forward:
			maze.setPointValue(currentPosition.getZ(), currentPosition.getY()-1, currentPosition.getX(), 0);
			//maze.setPointValue(currentPosition.getZ(), currentPosition.getY()-2, currentPosition.getX(), 0);
			newPosition = new Position(currentPosition.getZ() , currentPosition.getY()-2 , currentPosition.getX());
			savePositions.add(newPosition);
			break;
		case Up:
			maze.setPointValue(currentPosition.getZ()+1, currentPosition.getY(), currentPosition.getX(), 0);
			newPosition = new Position(currentPosition.getZ()+1 , currentPosition.getY() , currentPosition.getX());
			savePositions.add(newPosition);
			break;
		case Down:
			maze.setPointValue(currentPosition.getZ()-1, currentPosition.getY(), currentPosition.getX(), 0);
			newPosition = new Position(currentPosition.getZ()-1 , currentPosition.getY() , currentPosition.getX());
			savePositions.add(newPosition);
			break;
		default:
			break;
		}

		return newPosition;
	}

	/**
	 * Get all possible moves from current position.
	 * @param p - Current position to check where you can move to.
	 * @return List of directions you can move from the current position.
	 */
	public Direction[] getPossibleMoves(Position p) {
		ArrayList<Direction> moves = new ArrayList<Direction>();
		//Checks if there is path to right
		if(p.getX()+2<maze.getLength()-1 && p.getZ() != 0 && maze.getPointValue(p.getZ(), p.getY(), p.getX()+2)==1)
		{
			moves.add(Direction.Right);
		}
		//Checks if there is path to left
		if(p.getX()-2>=0 && p.getZ() != 0 && maze.getPointValue(p.getZ(), p.getY(), p.getX()-2)==1)
		{
			moves.add(Direction.Left);
		}
		//Checks if there is path to up
		if(p.getZ()+1<maze.getHeight()-1&&maze.getPointValue(p.getZ()+1, p.getY(), p.getX())==1)
		{
			moves.add(Direction.Up);
		}
		//Checks if there is path to down
		if(p.getZ()-1 > 0&&maze.getPointValue(p.getZ()-1, p.getY(), p.getX())==1)
		{
			moves.add(Direction.Down);
		}
		//Checks if there is path backward
		if(p.getY()+2<maze.getWidth()-1 
				&& p.getZ() != 0 && maze.getPointValue(p.getZ(), p.getY()+2, p.getX())==1)
		{
			moves.add(Direction.Backward);
		}
		//Checks if there is path forward
		if(p.getY()-2>=0&&p.getZ() != 0 && maze.getPointValue(p.getZ(), p.getY()-2, p.getX())==1)
		{
			moves.add(Direction.Forward);
		}
		return moves.toArray(new Direction[moves.size()]);
	}
}
