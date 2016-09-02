package algorithms.mazeGenerators;

import java.util.ArrayList;
import java.util.Random;

/**
 * Implement for chooseGrowingTree.</br>
 * by random cell.
 * @author Elad Jarby
 *
 */
public class GrowingTreeRandomCell implements ChooseGrowingTree{

	/**
	 * Overriding for "selectPositionFromList".
	 * Selecting random position from the list.
	 * @param list - List of positions.
	 */
	@Override
	public Position selectPositionFromList(ArrayList<Position> list) {
		Random r = new Random();
		return list.get(r.nextInt(list.size()));
	}
	
}
