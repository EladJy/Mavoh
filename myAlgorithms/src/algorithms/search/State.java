package algorithms.search;

import java.io.Serializable;

/**
 * Define a state in the searching problem.
 * @author Elad Jarby
 *
 * @param <T> - Generic type of the state.
 */
public class State<T> implements Comparable<State<T>> , Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9011115900445010262L;
	private State<T> cameFrom;
	private double cost;
	private T value;
	
	/**
	 * Constructor , Initialize the state with given value.
	 * @param value -  Value of the state.
	 */
	public State(T value) {
		this.value = value;
		this.cost = 1;
		this.cameFrom = null;
	}
	
	/**
	 * Default constructor , Initialize the state.
	 */
	public State() {
		this.value = null;
		this.cost = 1;
		this.cameFrom = null;
	}

	/**
	 * Getting the state from how we get to this state.
	 * @return State
	 */
	public State<T> getCameFrom() {
		return cameFrom;
	}

	/**
	 * Sets the state from how we get to this state.
	 * @param cameFrom - State that we need to set from where we came from.
	 */
	public void setCameFrom(State<T> cameFrom) {
		this.cameFrom = cameFrom;
	}

	/**
	 * Get the cost of state.
	 * @return Cost
	 */
	public double getCost() {
		return cost;
	}

	/**
	 * Setting the cost of state.
	 * @param cost - Cost to be set.
	 */
	public void setCost(double cost) {
		this.cost = cost;
	}

	/**
	 * Get the value of the state.
	 * @return Value
	 */
	public T getValue() {
		return value;
	}

	/**
	 * Setting the value of state.
	 * @param value - State to be set.
	 */
	public void setValue(T value) {
		this.value = value;
	}
	
	/**
	 * Function that comparing between 2 states.
	 * @param s - Given state to compare to.
	 * @return > 0 if this > s</br>
	 * < 0 if this < s </br>
	 * = if this == s
	 */
	@Override
	public int compareTo(State<T> s) {
		return (int)(this.cost - s.getCost());
	}
	
	/**
	 * Function that comparing between 2 states
	 * @param obj - The state we compare to
	 * @return Tue if equal otherwise false.
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj == null || obj.getClass() != this.getClass()) {
			return false;
		}	
		@SuppressWarnings("unchecked")
		State<T> s = (State<T>)obj;
		return this.value.equals(s.value);
	}
	
	/**
	 * Saving states in the HashSet.
	 * @return Returns a hash code value for the object.
	 */
	@Override
	public int hashCode() {
		return value.hashCode();
	}
}
