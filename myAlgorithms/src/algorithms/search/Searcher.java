package algorithms.search;

/**
 * Common interface  for the functionality of the searching problems.
 * @author Elad Jarby
 *
 * @param <T> - Generic type of the state.
 */
public interface Searcher<T> {
	/**
	 * Find the solution of the searching problem.
	 * @param s - The searching problem.
	 * @return The solution of the searching problem.
	 */
	public Solution<T> Search(Searchable<T> s);
	public int getNumberOfNodesEvaluated();
}
