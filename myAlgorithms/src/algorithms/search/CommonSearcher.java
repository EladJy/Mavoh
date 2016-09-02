package algorithms.search;

import java.util.PriorityQueue;
/**
 * Abstract class which defines the algorithm that searches.
 * @author Elad Jarby
 *
 * @param <T> - Generic type of the state.
 */
public abstract class CommonSearcher<T> implements Searcher<T> {
	private int evaluatedNodes;
	protected PriorityQueue<State<T>> openList;
	public abstract Solution<T> Search(Searchable<T> s);
	
	/**
	 * Default constructor , Initialize the number of evaluated nodes (to 0)</br>
	 * and create priority queue - open list.
	 */
	public CommonSearcher() {
		openList = new PriorityQueue<State<T>>();
		this.evaluatedNodes = 0;
	}
	
	/**
	 * Get state from the priority queue.
	 * @return State
	 */
	protected State<T> dequeueFromOpenList() {
		evaluatedNodes++;
		return openList.poll();
	}
	
	/**
	 * Adds the given state to the priority queue.
	 * @param state - State to be added.
	 */
	protected void addToOpenList(State<T> state) {
		openList.add(state);
	}
	
	/**
	 * Get the number of states in the priority queue.
	 * @return Number of states.
	 */
	@Override
	public int getNumberOfNodesEvaluated() {
		return evaluatedNodes;
	}
	
	/**
	 * Goes from the goal state to the start state to find the solution path.
	 * @param goalState - Goal state of the maze.
	 * @param startState  - Start state of the maze.
	 * @return Solution of the maze.
	 */
	protected Solution<T> backTrace(State<T> goalState , State<T> startState) {
		Solution<T> solution = new Solution<T>();
		solution.addStateToSolution(goalState);
		State<T> tempState = goalState.getCameFrom();
		
		while(!tempState.equals(startState)) {
			solution.addStateToSolution(tempState);
			tempState = tempState.getCameFrom();
		}
		
		solution.addStateToSolution(startState);
		return solution;
	}
}
