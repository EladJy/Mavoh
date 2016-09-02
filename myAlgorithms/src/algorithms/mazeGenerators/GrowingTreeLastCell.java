package algorithms.mazeGenerators;

import java.util.ArrayList;

/**
 * Implement for chooseGrowingTree.</br>
 * by last cell.
 * @author Elad Jarby
 *
 */
public class GrowingTreeLastCell implements ChooseGrowingTree {


	/**
	 * Overriding for "selectPositionFromList".
	 * Selecting the last position from the list.
	 * @param list - List of positions.
	 */
	@Override
	public Position selectPositionFromList(ArrayList<Position> list) {
		return list.get(list.size()-1);
		
	}
}
