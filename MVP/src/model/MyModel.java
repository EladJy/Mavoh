package model;

import java.util.HashMap;
import java.util.Observable;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import algorithms.mazeGenerators.GrowingTreeGenerator;
import algorithms.mazeGenerators.GrowingTreeLastCell;
import algorithms.mazeGenerators.GrowingTreeRandomCell;
import algorithms.mazeGenerators.Maze3d;
import algorithms.mazeGenerators.Maze3dGenerator;
import algorithms.mazeGenerators.SimpleMaze3dGenerator;

public class MyModel extends Observable implements Model {

	private ExecutorService threadPool;
	private HashMap<String, Maze3d> mazes;
	private String message;
	
	public MyModel() {
		threadPool = Executors.newFixedThreadPool(10);
		mazes = new HashMap<String, Maze3d>();
	}
	@Override
	public void dirPath(String[] dirArray) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void generate3dMaze(String[] arr) {
		String mazeName = arr[0];
		if(arr.length != 5) {
			setChanged();
			message = "Invalid number of parameters";
			notifyObservers("error");
			return;
		}
		int x = Integer.parseInt(arr[1]);
		int y = Integer.parseInt(arr[2]);
		int z = Integer.parseInt(arr[3]);
		String algorithm = arr[4];
		if( x < 1 || y < 1 || z < 1 ) {
			setChanged();
			message = "Error on (z,y,x) , Must be positive numbers";
			notifyObservers("error");
			return;
		}

		threadPool.submit(new Callable<Maze3d>() {
			@Override
			public Maze3d call() throws Exception {
				Maze3dGenerator mg = null;
				if(algorithm.intern() == "simple") {
					mg = new SimpleMaze3dGenerator();
				} else if (algorithm.intern() == "growing-random") {
					mg = new GrowingTreeGenerator(new GrowingTreeRandomCell());
				} else if (algorithm.intern() == "growing-last") {
					mg = new GrowingTreeGenerator(new GrowingTreeLastCell());
				}
				if(mazes.containsKey(mazeName)) {
					System.out.println("You override maze: " + mazeName + " with new parameters.");
					mazes.remove(mazeName);
				}

				mazes.put(arr[0].toString(), mg.generate(Integer.parseInt(arr[1]), Integer.parseInt(arr[2]), Integer.parseInt(arr[3])));
				setChanged();
				message = "Maze: " + arr[0] + " is ready!";
				notifyObservers("displayMessage");
				return mazes.get(mazeName);
			}
		});
		
	}

	@Override
	public void getMaze(String[] arr) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getCrossSection(String[] arr) {
		// TODO Auto-generated method stub
		
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
	public void getSolutionReady(String[] arr) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getSolution(String[] arr) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitCommand() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public byte[] getMazeFromHashMap(String maze) {
		return mazes.get(maze).toByteArray();
	}
	
	@Override
	public String getMessage() {
		return message;
	}
}
