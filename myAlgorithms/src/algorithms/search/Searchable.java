package algorithms.search;

import java.util.ArrayList;

/**
 * Common interface  for the functionality of the searching problem.
 * @author Elad Jarby
 *
 * @param <T> - Generic type of the state.
 */
public interface Searchable<T> {
	/**
	 * Getting start state.
	 * @return Starting state.
	 */
	State<T> getStartState();
	
	/**
	 * Getting goal state.
	 * @return Goal state.
	 */
	State<T> getGoalState();
	
	/**
	 * Find all possible states from the current state.
	 * @param s - Current state.
	 * @return Array list of all possible states.
	 */
	ArrayList<State<T>> getAllPossibleStates(State<T> s);
	
	/**
	 * Get the moving cost between current state to neighbor state.
	 * @param currState - Current state.
	 * @param neighbor - Neighbor state.
	 * @return The cost to move from current state to neighbor state.
	 */
	double getMoveCost(State<T> currState , State<T> neighbor);
}
