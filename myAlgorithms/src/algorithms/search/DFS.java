package algorithms.search;

import java.util.ArrayList;
import java.util.HashSet;
/**
 * Class which defines the DFS algorithm.
 * @author Elad Jarby
 *
 * @param <T> - Generic type of the state.
 */
public class DFS<T> extends CommonSearcher<T> {
	/**
	 * Find the solution by DFS algorithm.
	 * @param s - The searching problem.
	 * @return Solution of the searching problem.
	 */
	@Override
	public Solution<T> Search(Searchable<T> s) {
		addToOpenList(s.getStartState());
		HashSet<State<T>> discovered=new HashSet<State<T>>();
		
		
		while(!openList.isEmpty())
		{
			State<T> n=dequeueFromOpenList();
			
			if(n.equals(s.getGoalState()))
				return backTrace(n,s.getStartState()); 
			
			if(!discovered.contains(n))
			{
				 discovered.add(n);
				 ArrayList<State<T>> successors=s.getAllPossibleStates(n);
				 
				 for(State<T> state : successors)
				 {
					 state.setCameFrom(n);
					 addToOpenList(state);
				 }
				 
			}
		}
		return null;
	}

}
