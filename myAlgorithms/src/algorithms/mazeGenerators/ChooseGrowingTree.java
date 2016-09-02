package algorithms.mazeGenerators;

import java.util.ArrayList;

/**
 * Common interface for the functionality of the growing trees generators.
 * @author Elad Jarby
 *
 */
public interface ChooseGrowingTree {
	/**
	 * Selecting position from list of positions.
	 * @param list - List of positions.
	 */
	public Position selectPositionFromList(ArrayList<Position> list);
}
