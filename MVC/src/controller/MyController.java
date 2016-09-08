package controller;

import algorithms.search.Solution;

/**
 * Current controller , which passes the command from view to model</br>
 * And pass that results from model to view.
 * @author Elad Jarby
 * @version 1.0
 * @since 04.09.2016
 *
 */
public class MyController extends CommonController {

	/**
	 * Constructor to initialize controller.  
	 */
	public MyController() {
		super();
	}
	
	/**
	 * Override to initialize the commands by the implementation of Common Controller.
	 */
	@Override
	protected void initCommands() {
		stringToCommand.put("dir", new Command() {

			@Override
			public void doCommand(String[] strings) {
				model.dirPath(strings);				
			}
		});

		stringToCommand.put("generate_maze", new Command() {

			@Override
			public void doCommand(String[] strings) {
				model.generate3dMaze(strings);			
			}
		});

		stringToCommand.put("generate_3d_maze", new Command() {

			@Override
			public void doCommand(String[] strings) {
				model.generate3dMaze(strings);			
			}
		});

		stringToCommand.put("display", new Command() {

			@Override
			public void doCommand(String[] strings) {
				model.getMaze(strings);		
			}
		});

		stringToCommand.put("display_cross_section", new Command() {

			@Override
			public void doCommand(String[] strings) {
				model.getCrossSection(strings);		
			}
		});

		stringToCommand.put("save_maze", new Command() {

			@Override
			public void doCommand(String[] strings) {
				model.saveMaze(strings);
			}
		});

		stringToCommand.put("load_maze", new Command() {

			@Override
			public void doCommand(String[] strings) {
				model.loadMaze(strings);
			}
		});

		stringToCommand.put("solve", new Command() {

			@Override
			public void doCommand(String[] strings) {
				model.getSolutionReady(strings);	
			}
		});

		stringToCommand.put("display_solution", new Command() {

			@Override
			public void doCommand(String[] strings) {
				model.getSolution(strings);			
			}
		});
		
		stringToCommand.put("exit", new Command() {
			
			@Override
			public void doCommand(String[] strings) {
				model.exitCommand();				
			}
		});
	}

	/**
	 * Transfer the names of the files and directories to the view.
	 * @param dirArray - Array of strings containing the names of the files and directories in the specific path.
	 */
	@Override
	public void displayDirPath(String[] dirArray) {
		view.displayDirPath(dirArray);
	}

	/**
	 * Transfer an error message.
	 * @param msg - Error message.
	 */
	@Override
	public void displayError(String msg) {
		view.displayError(msg);
	}

	/**
	 * Transfer the 3d maze was generated.
	 * @param msg - Message that the 3d maze is ready.
	 */
	@Override
	public void displayGenerate3dMaze(String msg) {
		view.displayGenerate3dMaze(msg);
	}

	/**
	 * Transfer the generated maze to the view to display the maze.
	 * @param byteArray - Byte array that containing the maze 3d.
	 * @throws Exception - exception
	 */
	@Override
	public void displayMaze(byte[] byteArray) throws Exception {
		view.displayMaze(byteArray);
	}

	/**
	 * Transfer the cross section to view.
	 * @param crossSection - Cross section , a matrix (2d array).
	 */
	@Override
	public void displayCrossSection(int[][] crossSection) {
		view.displayCrossSection(crossSection);
	}

	/**
	 * Transfer the string that saying the maze has been saved.
	 * @param msg - Message that saying - the maze has been saved.
	 */
	@Override
	public void displaySaveMaze(String msg) {
		view.displaySaveMaze(msg);
	}

	/**
	 * Transfer the string that saying the maze has been loaded
	 * @param msg - Message that saying - the maze has been loaded.
	 */
	@Override
	public void displayLoadMaze(String msg) {
		view.displayLoadMaze(msg);
	}

	/**
	 * Transfer a message that the maze was solved.
	 * @param msg - Message that the maze was solved.
	 */
	@Override
	public void displaySolutionReady(String msg) {
		view.displaySolutionReady(msg);
	}

	/**
	 * Transfer the solution of the maze.
	 * @param solution - Solution of the maze.
	 */
	@Override
	public void displaySolution(Solution<String> solution) {
		view.displaySolution(solution);
	}
	
}
