package model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import algorithms.mazeGenerators.GrowingTreeGenerator;
import algorithms.mazeGenerators.GrowingTreeLastCell;
import algorithms.mazeGenerators.GrowingTreeRandomCell;
import algorithms.mazeGenerators.Maze3d;
import algorithms.mazeGenerators.Maze3dGenerator;
import algorithms.mazeGenerators.Maze3dSearchable;
import algorithms.mazeGenerators.SimpleMaze3dGenerator;
import algorithms.search.BFS;
import algorithms.search.DFS;
import algorithms.search.Searchable;
import algorithms.search.Searcher;
import algorithms.search.Solution;
import controller.Controller;
import io.MyCompressorOutputStream;
import io.MyDecompressorInputStream;

/**
 * Generate the code behind each command.
 * @author Elad Jarby
 * @version 1.0
 * @since 06.09.2016
 */
public class MyModel extends CommonModel {
	HashMap<String, Maze3d> mazes;
	HashMap<Maze3d, Solution<String>> mazeSolutions;
	ExecutorService threadPool;

	/**
	 * Constructor to initialize all the variables.
	 * @param controller - The controller that this Model use, All the solutions for problems passing through this controller.
	 */
	public MyModel(Controller controller) {
		super(controller);
		mazes = new HashMap<String, Maze3d>();
		threadPool = Executors.newFixedThreadPool(10);
		mazeSolutions = new HashMap<Maze3d, Solution<String>>();
	}

	/**
	 * Handling with command: dir < path ></br>
	 * @param dirArray - Array of string , containing string with path.
	 */
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

	/**
	 * Handling with command:</br> 
	 * generate_3d_maze < maze name > < floors > < width > < length > < algorithm ></br>
	 * <b>Algorithms:</b> simple , growing-last , growing-random.
	 * @param arr - Array of string with the parameters.
	 */
	@Override
	public void generate3dMaze(String[] arr) {
		String mazeName = arr[0];
		if(arr.length != 5) {
			controller.displayError("Invalid number of parameters");
			return;
		}
		int x = Integer.parseInt(arr[1]);
		int y = Integer.parseInt(arr[2]);
		int z = Integer.parseInt(arr[3]);
		String algorithm = arr[4];
		if( x < 1 || y < 1 || z < 1 ) {
			controller.displayError("Error on (z,y,x) , Must be positive numbers");
			return;
		}

		threadPool.execute(new Runnable() {
			@Override
			public void run() {
				Maze3dGenerator mg = null;
				if(algorithm.intern() == "simple") {
					mg = new SimpleMaze3dGenerator();
				} else if (algorithm.intern() == "growing-random") {
					mg = new GrowingTreeGenerator(new GrowingTreeRandomCell());
				} else if (algorithm.intern() == "growing-last") {
					mg = new GrowingTreeGenerator(new GrowingTreeLastCell());
				} else {
					controller.displayError("Invalid algorithm name");
					return;
				}
				if(mazes.containsKey(mazeName)) {
					System.out.println("You override maze: " + mazeName + " with new parameters.");
					mazes.remove(mazeName);
				}

				mazes.put(arr[0].toString(), mg.generate(Integer.parseInt(arr[1]), Integer.parseInt(arr[2]), Integer.parseInt(arr[3])));

				controller.displayGenerate3dMaze("Maze: " + arr[0].toString() + " is ready!");
			}
		});

	}

	/**
	 * Handling with command: display < maze name >
	 * @param arr - Array of one string , containing the maze name that need to display.
	 */
	@Override
	public void getMaze(String[] arr) {
		if (arr.length != 1) {
			controller.displayError("Invalid number of parameters");
			return;
		}
		String mazeName = arr[0];
		if(!mazes.containsKey(mazeName)) {
			controller.displayError("There is no maze with the name: " + mazeName);
			return;
		}

		try {
			controller.displayMaze(mazes.get(mazeName).toByteArray());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Handling with command: display_cross_section < axis > < index > < maze name ></br>
	 * <b>axis:</b> z - for floors , y - for width , x - for length.</br>
	 * <b>index:</b> can chose from 0 to axis-1 , otherwise return error.
	 * @param arr - Array of string with the parameters.
	 */
	@Override
	public void getCrossSection(String[] arr) {
		if (arr.length != 3) {
			controller.displayError("Invalid number of parameters");
			return;
		}
		Maze3d maze;
		String mazeName = arr[2];
		String axis = arr[0];
		int index = Integer.parseInt(arr[1]);
		maze = mazes.get(mazeName);
		if (!mazes.containsKey(mazeName)) {
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

	/**
	 * Handling with command: save_maze < maze name > < file name ></br>
	 * <b>maze name:</b> The maze name that need to be save on the file.</br>
	 * <b>file name:</b> The file name that we need to save maze in.
	 * @param arr - Array of string with the parameters.
	 */
	@Override
	public void saveMaze(String[] arr) {
		if (arr.length != 2) {
			controller.displayError("Invalid number of parameters");
			return;
		}
		String mazeName = arr[0];
		String fileName = arr[1];
//		File file = new File(fileName);
		if (!mazes.containsKey(mazeName)) {
			controller.displayError("There is no maze with the name: " + mazeName);
			return;
		}
//		if(file.exists() && !file.isDirectory()) {
//			controller.displayError("File already exist or is directory , try another name.");
//			return;
//		}
		Maze3d maze = mazes.get(mazeName);
		byte[] mazeInBytes = maze.toByteArray();

		MyCompressorOutputStream out;
		try {
			out = new MyCompressorOutputStream(new FileOutputStream(fileName));
			out.write(ByteBuffer.allocate(4).putInt(mazeInBytes.length).array());
			out.write(mazeInBytes);
			controller.displaySaveMaze(mazeName + " has been saved!");
			out.close();
			return;

		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Handling with command: load_maze < file name > < maze name ></br>
	 * <b>file name:</b> The file name we need to load the maze from.</br>
	 * <b>maze name:</b> The maze name we want to save from the loaded file.
	 * @param arr - Array of string with the parameters.
	 */
	@Override
	public void loadMaze(String[] arr) {
		if (arr.length != 2) {
			controller.displayError("Invalid number of parameters");
			return;
		}

		String fileName = arr[0];
		File file = new File(fileName);
		String mazeName = arr[1];

//		if (mazes.containsKey(mazeName)) {
//			controller.displayError("Maze already exist , try to load with other name.");
//			return;
//		}
		if(!file.exists() || file.isDirectory()) {
			controller.displayError("File not exist / it is directory!");
			return;
		}
		try {
			MyDecompressorInputStream in = new MyDecompressorInputStream(new FileInputStream(fileName));
			ByteArrayOutputStream outByte = new ByteArrayOutputStream();

			outByte.write(in.read());
			outByte.write(in.read());
			outByte.write(in.read());
			outByte.write(in.read());

			ByteArrayInputStream inByte = new ByteArrayInputStream(outByte.toByteArray());
			DataInputStream data = new DataInputStream(inByte);

			byte[] byteArr = new byte[data.readInt()];
			in.read(byteArr);
			mazes.put(mazeName, new Maze3d(byteArr));
			controller.displayLoadMaze(mazeName + " has been loaded from file: "  + fileName);		
			in.close();
			return;
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Handling with command: solve < maze name > < algorithm ></br>
	 * Solves the specific maze with algorithm.</b></br>
	 * <b>alogirthm:</b> dfs or bfs.
	 * @param arr - Array of string with the parameters.
	 */
	@Override
	public void getSolutionReady(String[] arr) {
		if (arr.length != 2) {
			controller.displayError("Invalid number of parameters");
			return;
		}
		String mazeName = arr[0];
		String algorithm = arr[1];
		Maze3d maze=mazes.get(mazeName);
		if (!mazes.containsKey(mazeName)) {
			controller.displayError("There is no maze with the name: " + mazeName);
			return;
		}

		threadPool.execute(new Runnable() {

			@Override
			public void run() {
				Searchable<String> mazeSearch = new Maze3dSearchable(maze);
				Searcher<String> searchAlgorithm;

				switch(algorithm) {
				case "bfs":
				case "BFS":
					searchAlgorithm = new BFS<String>();
					break;
				case "dfs":
				case "DFS":
					searchAlgorithm = new DFS<String>();
					break;
				default:
					controller.displayError("Invalid algorithm");
					return;
				}

				mazeSolutions.put(maze, searchAlgorithm.Search(mazeSearch));
				controller.displaySolutionReady("Solution for: " + mazeName + "is ready!");
			}
		});

	}

	/**
	 * Handling with command: display_solution < maze name ></br>
	 * Display existing solution.
	 * @param arr - Array of one string , containing the maze name that need to display his solution.
	 */
	@Override
	public void getSolution(String[] arr) {
		if (arr.length != 1) {
			controller.displayError("Invalid number of parameters");
			return;
		}
		String mazeName = arr[0];
		Maze3d maze=mazes.get(mazeName);
		if (!mazes.containsKey(mazeName)) {
			controller.displayError("There is no maze with the name: " + mazeName);
			return;
		}

		if(mazeSolutions.containsKey(maze)) {
			controller.displaySolution(mazeSolutions.get(maze));
			return;
		} else {
			controller.displayError("Solution doesn't exist! / use 'Solve' commannd first!");
		}
	}

	/**
	 * Closing all running threads.
	 */
	@Override
	public void exitCommand() {
		threadPool.shutdown();
		boolean terminated = false;
		while(!terminated)
		{
			try {
				terminated = (threadPool.awaitTermination(10, TimeUnit.SECONDS));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
