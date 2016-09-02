package algorithms.search;

import java.util.Stack;

/**
 * Define a solution for the searching problem.
 * @author Elad Jarby
 *
 * @param <T> - Generic type of the state.
 */
public class Solution<T> {
	private Stack<State<T>> states;
	
	/**
	 * Default Constructor , Initialize the stuck.
	 */
	public Solution() {
		states = new Stack<State<T>>();
	}
	
	/**
	 * Constructor , Initialize the stuck from the given stuck.
	 * @param states - Stack of states.
	 */
	public Solution (Stack<State<T>> states) {
		this.states = states;
	}
	
	/**
	 * Adds a given state to stuck.
	 * @param state - State that we need to add to the stuck.
	 */
	public void addStateToSolution(State<T> state) {
		states.push(state);
	}
	
	/**
	 * Remove a given state from the stuck.
	 * @param state - State that we need to remove from the stuck.
	 */
	public void removeStateFromSolution(State<T> state) {
		states.remove(state);
	}
	
	/**
	 * Get the size of the stuck.
	 * @return Size of the stuck.
	 */
	public int size() {
		return states.size();
	}
	
	/**
	 * Overriding the function toString from object.</br>
	 * In order to print the solution.
	 */
	@Override
	public String toString() {
		String str="Solution is: ";
		while(!states.isEmpty())
		{
			str=str+states.pop().getValue().toString()+" ";
		}
		return str;
	}
}
