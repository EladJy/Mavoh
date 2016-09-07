package model;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import algorithms.mazeGenerators.GrowingTreeGenerator;
import algorithms.mazeGenerators.GrowingTreeLastCell;
import algorithms.mazeGenerators.GrowingTreeRandomCell;
import algorithms.mazeGenerators.Maze3d;
import algorithms.mazeGenerators.Maze3dGenerator;
import algorithms.mazeGenerators.SimpleMaze3dGenerator;
import algorithms.search.Solution;
import controller.Controller;

public class MyModel extends CommonModel {
	HashMap<String, Maze3d> mazes;
	HashMap<Maze3d, Solution<String>> mazeSolutions;
	ExecutorService threadPool;

	public MyModel(Controller controller) {
		super(controller);
		mazes = new HashMap<String, Maze3d>();
		threadPool = Executors.newFixedThreadPool(10);
		mazeSolutions = new HashMap<Maze3d, Solution<String>>();
	}

	@Override
	public void dirPath(String[] dirArray) {
		if(dirArray.length != 1) {
			controller.displayError("Invalid path");
			return;
		}

		File file = new File(dirArray[0].toString());
		if(!file.exists()) {
			controller.displayError("Directory not found");
			return;
		}

		if(!file.isDirectory()) {
			controller.displayError("Path is incorrect");
			return;
		}

		controller.displayDirPath(file.list());
	}

	@Override
	public void generate3dMaze(String[] arr) {
		if(arr.length != 5) {
			controller.displayError("Invalid number of parameters");
			return;
		}

		if((Integer.parseInt(arr[1]) < 1) || (Integer.parseInt(arr[2]) < 1) || (Integer.parseInt(arr[3]) < 1)) {
			controller.displayError("Error on (z,y,x) , Must be positive numbers");
			return;
		}

		threadPool.execute(new Runnable() {
			@Override
			public void run() {
				Maze3dGenerator mg = null;
				if(arr[arr.length-1].intern() == "simple") {
					mg = new SimpleMaze3dGenerator();
				} else if (arr[arr.length-1].intern() == "growing-random") {
					mg = new GrowingTreeGenerator(new GrowingTreeRandomCell());
				} else if (arr[arr.length-1].intern() == "growing-last") {
					mg = new GrowingTreeGenerator(new GrowingTreeLastCell());
				} else {
					controller.displayError("Invalid algorithm name");
					return;
				}
				if(mazes.containsKey(arr[0].toString())) {
					controller.displayError("Maze name is already exists , please try other maze");
					return;
				}

				mazes.put(arr[0].toString(), mg.generate(Integer.parseInt(arr[1]), Integer.parseInt(arr[2]), Integer.parseInt(arr[3])));

				controller.displayGenerate3dMaze("Maze: " + arr[0].toString() + " is ready!");
			}
		});

	}

	@Override
	public void getMaze(String[] arr) {
		if (arr.length != 1) {
			controller.displayError("Invalid number of parameters");
			return;
		}

		if(!mazes.containsKey(arr[0].toString())) {
			controller.displayError("There is no maze with the name: " + arr[0].toString());
			return;
		}

		try {
			controller.displayMaze(mazes.get(arr[0].toString()).toByteArray());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void getCrossSection(String[] arr) {
		Maze3d maze;
		String mazeName = arr[2];
		String axis = arr[0];
		int index = Integer.parseInt(arr[1]);
		maze = mazes.get(mazeName);

		if (arr.length != 3) {
			controller.displayError("Invalid number of parameters");
			return;
		}

		if (!mazes.containsKey(arr[2].toString())) {
			controller.displayError("There is no maze with the name: " + mazeName);
			return;
		}

		if(index < 0) {
			controller.displayError("The axis most be positive number!");
			return;
		}

		try {
			if(axis.equals("z")) {
				if (index < maze.getHeight()) {
					controller.displayCrossSection(maze.getCrossSectionByZ(index));
				} else {
					controller.displayError("The input of Z value must be smaller than: " + maze.getHeight());
					return;
				}
			} else if (axis.equals("y")){
				if(index < maze.getWidth()) {
					controller.displayCrossSection(maze.getCrossSectionByY(index));
				} else {
					controller.displayError("The input of Y value must be smaller than: " + maze.getWidth());
					return;
				}
			} else if (axis.equals("x")) {
				if(index < maze.getLength()) {
					controller.displayCrossSection(maze.getCrossSectionByX(index));
				} else {
					controller.displayError("The input of X value must be smaller than: " + maze.getLength());
					return;
				}
			} else {		
				controller.displayError("The axis most be x or y or z");
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		


	}

	@Override
	public void saveMaze(String[] arr) {
		// TODO Auto-generated method stub

	}

	@Override
	public void loadMaze(String[] arr) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mazeSize(String[] arr) {
		// TODO Auto-generated method stub

	}

	@Override
	public void fileSize(String[] arr) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getSolutionReady(String[] arr) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getSolution(String[] arr) {
		// TODO Auto-generated method stub

	}

	@Override
	public void exitCommand(String[] emptyArr) {
		// TODO Auto-generated method stub

	}

}
