package algorithms.mazeGenerators;

import java.util.Random;

/**
 * Simple Maze 3D Generator.
 * Uses random() function from start position in each position till it gets to the goal position.
 * @author Elad Jarby
 *
 */
public class SimpleMaze3dGenerator extends CommonMaze3dGenerator {

	/**
	 * Override for generate method , generating simple maze.
	 * @param z - Height/Floors of the maze
	 * @param y - Width of the maze
	 * @param x - Length of the maze
	 * @return Maze3d - the maze
	 */
	@Override
	public Maze3d generate(int z,int y,int x) {
		// floors + 2 because we need floor and ceiling for to not get out of bounds
		Maze3d maze = new Maze3d(z+2, y, x);

		Random r = new Random();
		maze.fillWithWalls();
		
		int i = 1 , j = 0, k = 0;
		int direction;
		Position p = new Position(k,j,i);
		maze.setStartPosition(p);
		
		while(i < x && j < y && k < z+2 && i >= 0 && j >= 0 && k >= 0)
		{
			maze.setPointValue(k, j, i , 0);
			direction = r.nextInt(3);
			switch(direction)
			{
			case 0:
				if(!(k == z+1 || k == 0) && !(j == 0 || j == y-1))
					i++;
				break;

			case 1:
				k++;
				break;
			case 2:
				if(!(k == z+1 || k == 0) && !(i == 0 || i == x-1))
					j++;
				break;
			default:
				if(!(k == z+1 || k == 0) && !(j == 0 || j == y-1))
					i++;
				break;
			}
		}
		if(i == x)
		{
			maze.setGoalPosition(new Position(k, j, i-1));
		}
		if(j == y)
		{
			maze.setGoalPosition(new Position(k, j-1, i));
		}
		if(k == z+2)
		{
			maze.setGoalPosition(new Position(k-1, j, i));
		}
		return maze;
	}

}
