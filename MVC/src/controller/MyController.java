package controller;

import algorithms.search.Solution;

public class MyController extends CommonController {

	public MyController() {
		super();
	}
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
				model.exitCommand(strings);				
			}
		});
	}

	@Override
	public void displayDirPath(String[] dirArray) {
		view.displayDirPath(dirArray);
	}

	@Override
	public void displayError(String msg) {
		view.displayError(msg);
	}

	@Override
	public void displayGenerate3dMaze(String msg) {
		view.displayGenerate3dMaze(msg);
	}

	@Override
	public void displayMaze(byte[] byteArray) throws Exception {
		view.displayMaze(byteArray);
	}

	@Override
	public void displayCrossSection(int[][] crossSection) {
		view.displayCrossSection(crossSection);
	}

	@Override
	public void displaySaveMaze(String str) {
		view.displaySaveMaze(str);
	}

	@Override
	public void displayLoadMaze(String str) {
		view.displayLoadMaze(str);
	}

	@Override
	public void displaySolutionReady(String msg) {
		view.displaySolutionReady(msg);
	}

	@Override
	public void displaySolution(Solution<String> solution) {
		view.displaySolution(solution);
	}
	
}
