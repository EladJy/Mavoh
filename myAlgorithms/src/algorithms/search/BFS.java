package algorithms.search;

import java.util.ArrayList;
import java.util.HashSet;
/**
 * Class which defines the BFS algorithm.
 * @author Elad Jarby
 *
 * @param <T> - Generic type of the state.
 */

/* 
 1) BFS advantages:
 	- Shortest path searching.
 	- Testing a graph for bipartiteness.

 	DFS advantages:
 	 - Finding connected components.
 	 - Topological sorting.
 	 - Finding strongly connected components.
 	 
 2) I chose this way to write BFS,
    because it is the option that isolates the problem from the solution
*/
public class BFS<T> extends CommonSearcher<T> {

	/**
	 * Find the solution by BFS algorithm.
	 * @param s - The searching problem.
	 * @return Solution of the searching problem.
	 */
	@Override
	public Solution<T> Search(Searchable<T> s) {
		addToOpenList(s.getStartState());
		HashSet<State<T>> closedList = new HashSet<State<T>>();
		
		while(!openList.isEmpty()) {
			State<T> currState = dequeueFromOpenList();
			closedList.add(currState);
			
			if(currState.equals(s.getGoalState()))
				return backTrace(currState,s.getStartState());
			
			ArrayList<State<T>> neighbors = s.getAllPossibleStates(currState);
			
			for(State<T> neighbor : neighbors) {
				if(!openList.contains(neighbor) && !closedList.contains(neighbor)) {
					neighbor.setCameFrom(currState);
					neighbor.setCost(currState.getCost() + s.getMoveCost(currState, neighbor));
					addToOpenList(neighbor);
				} else {
					double newPathCost = currState.getCost() + s.getMoveCost(currState, neighbor);
					if(neighbor.getCost() > newPathCost) {
						neighbor.setCost(newPathCost);
						neighbor.setCameFrom(currState);
						if(! openList.contains(neighbor))
							addToOpenList(neighbor);
						else {
							openList.remove(neighbor);
							openList.add(neighbor);
						}
					}
				}
			}
		}
		return null;
	}

}
