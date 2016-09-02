package algorithms.mazeGenerators;

/**
 * Abstract function that must be implemented in extended class
 * @author Elad Jarby
 *
 */
public abstract class CommonMaze3dGenerator implements Maze3dGenerator {

	/**
	 * Abstract function that must be implemented in extended class
	 * @param z - Height/Floors of the maze
	 * @param y - Width of the maze
	 * @param x - Length of the maze
	 * @return Maze3d - the maze
	 */
	@Override
	public abstract Maze3d generate(int z,int y,int x);

	/**
	 * Measure the time of generating maze from start time to end time.
	 * @param z - Height/Floors of the maze
	 * @param y - Width of the maze
	 * @param x - Length of the maze
	 * @return Running time of the maze in milliseconds
	 */
	@Override
	public String measureAlgorithmTime(int z,int y,int x) {
		long startTime = System.currentTimeMillis();
		this.generate(z, y, x);
		long endTime = System.currentTimeMillis();
		return "Running time: " + String.valueOf(endTime - startTime) + " in milliseconds";
	}

}
