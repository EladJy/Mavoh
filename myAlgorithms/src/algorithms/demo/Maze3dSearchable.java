package algorithms.demo;

import java.util.ArrayList;

import algorithms.mazeGenerators.Maze3d;
import algorithms.mazeGenerators.Position;
import algorithms.search.Searchable;
import algorithms.search.State;
/**
 * Object adapter , between "Maze" and "Searchable"
 * @author Elad Jarby
 *
 */
public class Maze3dSearchable implements Searchable<String> {

	private Maze3d maze;
	private State<String> startState;
	private State<String> goalState;

	/**
	 * Constructor , Initialize the maze and start/goal states.
	 * @param maze - 3D Maze that given and the algorithm will work on this maze.
	 */
	public Maze3dSearchable(Maze3d maze) {
		this.maze = maze;
		this.startState = new State<String>();
		this.startState.setValue(maze.getStartPosition().toString());
		this.goalState = new State<String>();
		this.goalState.setValue(maze.getGoalPosition().toString());
	}

	/**
	 * Getting start state.
	 * @return Starting state.
	 */
	@Override
	public State<String> getStartState() {
		return startState;
	}

	/**
	 * Getting goal state.
	 * @return Goal state.
	 */
	@Override
	public State<String> getGoalState() {
		return goalState;
	}

	/**
	 * Find all possible states from the current state.
	 * @param s - Current state.
	 * @return Array list of all possible states.
	 */
	@Override
	public ArrayList<State<String>> getAllPossibleStates(State<String> s) {
		Position p = new Position();
		ArrayList<State<String>> positions = new ArrayList<State<String>>();
		p = p.toPosition(s.getValue());
		String[] possibleMoves = maze.getPossibleMoves(p);
		for (int i = 0; i < possibleMoves.length; i++) {
			State<String> tempPos = new State<String>();
			tempPos.setValue(possibleMoves[i]);
			positions.add(tempPos);
		}
		return positions;
	}

	/**
	 * Get the moving cost between current state to neighbor state.
	 * @param currState - Current state.
	 * @param neighbor - Neighbor state.
	 * @return The cost to move from current state to neighbor state.
	 */
	@Override
	public double getMoveCost(State<String> currState, State<String> neighbor) {
		return 1;
	}

}
