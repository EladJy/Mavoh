package algorithms.mazeGenerators;

/**
 * Common interface for the functionality of the maze generators.
 * @author Elad Jarby
 *
 */
public interface Maze3dGenerator {
	public Maze3d generate(int z,int y,int x);
	public String measureAlgorithmTime(int z,int y,int x);
}
