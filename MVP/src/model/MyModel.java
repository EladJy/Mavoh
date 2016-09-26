package model;

import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Observable;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

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
import io.MyCompressorOutputStream;
import io.MyDecompressorInputStream;
import presenter.Properties;
import presenter.PropertiesLoader;


public class MyModel extends Observable implements Model {

	private ExecutorService threadPool;
	private HashMap<String, Maze3d> mazes;
	private HashMap<String, Solution<String>> mazeSolutions;
	private String message;
	private int[][] crossSection;
	private String[] fileList;
	private String mazeName;
	private Properties properties;

	
	public HashMap<String, Maze3dGenerator> getMaze3dGenerator() {
		HashMap<String, Maze3dGenerator> commands = new HashMap<String , Maze3dGenerator>();
		commands.put("simple", new SimpleMaze3dGenerator());
		commands.put("growing-random" , new GrowingTreeGenerator(new GrowingTreeRandomCell()));
		commands.put("growing-last" , new GrowingTreeGenerator(new GrowingTreeLastCell()));
		return commands;
	}
	
	public HashMap<String, Searcher<String>> getSearcher() {
		HashMap<String, Searcher<String>> commands = new HashMap<String , Searcher<String>>();
		commands.put("dfs", new DFS<String>());
		commands.put("bfs" , new BFS<String>());
		return commands;
	}
	
	public MyModel(String[] path) {
		mazes = new HashMap<String, Maze3d>();
		mazeSolutions = new HashMap<String, Solution<String>>();
		intializeIfZipped();
		properties = PropertiesLoader.getInstance().getProperties();
		threadPool = Executors.newFixedThreadPool(properties.getNumberOfThreads());
	}
	
	/**
	 * Handling with command: dir < path ></br>
	 * @param dirArray - Array of string , containing string with path.
	 */
	@Override
	public void dirPath(String[] dirArray) {
		if(dirArray == null || dirArray.length != 1) {
			setChanged();
			message = "Invalid path";
			notifyObservers("error");
			return;
		}

		File file = new File(dirArray[0].toString());
		if(!file.exists()) {
			setChanged();
			message = "Directory not found";
			notifyObservers("error");
			return;
		}

		if(!file.isDirectory()) {
			setChanged();
			message = "Path is incorrect";
			notifyObservers("error");
			return;
		}

		setChanged();
		fileList = file.list();
		notifyObservers("dir");		
	}

	/**
	 * Handling with command:</br> 
	 * generate_3d_maze < maze name > < floors > < width > < length > < algorithm ></br>
	 * <b>Algorithms:</b> simple , growing-last , growing-random.
	 * @param arr - Array of string with the parameters.
	 */
	@Override
	public void generate3dMaze(String[] arr) {
		if(arr == null || arr.length != 4) {
			setChanged();
			message = "Invalid number of parameters";
			notifyObservers("error");
			return;
		}
		if(!(isInteger(arr[1]) || isInteger(arr[2]) || isInteger(arr[2]))) {
			setChanged();
			message = "Axis must be integers!";
			notifyObservers("error");
			return;
		}
		mazeName = arr[0];
		int maxMaze = properties.getMaxMazeSize();
		int x = Integer.parseInt(arr[1]);
		int y = Integer.parseInt(arr[2]);
		int z = Integer.parseInt(arr[3]);
		String algorithm = properties.getDefaultAlgorithm();
		if( x < 1 || y < 1 || z < 1 ) {
			setChanged();
			message = "Error on (z,y,x) , Must be positive numbers";
			notifyObservers("error");
			return;
		}
		
		if( x > maxMaze || y > maxMaze || z > maxMaze) {
			setChanged();
			message = "Error , you can't generate maze that one of the axis greater than: " + maxMaze;
			notifyObservers("error");
			return;
		}
		
		if( x < 3 || y < 3 || z < 3) {
			setChanged();
			message = "Error , you can't generate maze that one of the axis lower than 3";
			notifyObservers("error");
			return;
		}

		threadPool.submit(new Callable<Maze3d>() {
			@Override
			public Maze3d call() throws Exception {
				HashMap<String, Maze3dGenerator> algorithms = getMaze3dGenerator();
				Maze3dGenerator mg = algorithms.get(algorithm);
				if(mg == null) {
					setChanged();
					message = "Invalid algorithm name";
					notifyObservers("error");
					return null;
				}
				if(mazes.containsKey(mazeName)) {
					message = "You override maze: " + mazeName + " with new parameters.";
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
	
	/**
	 * Handling with command: display < maze name >
	 * @param arr - Array of one string , containing the maze name that need to display.
	 */
	@Override
	public void getMaze(String[] arr) {
		if (arr == null || arr.length != 1) {
			setChanged();
			message = "Invalid number of parameters";
			notifyObservers("error");
			return;
		}
		mazeName = arr[0];
		if(!mazes.containsKey(mazeName)) {
			setChanged();
			message = "There is no maze with the name: " + mazeName;
			notifyObservers("error");
			return;
		}
		try {
			setChanged();
			notifyObservers("display");
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
	public void crossSection(String[] arr) {
		if (arr == null || arr.length != 3) {
			setChanged();
			message = "Invalid number of parameters";
			notifyObservers("error");
			return;
		}
		if(!isInteger(arr[1])) {
			setChanged();
			message = "Index must be integer";
			notifyObservers("error");
			return;
		}
		Maze3d maze;
		mazeName = arr[2];
		String axis = arr[0];
		int index = Integer.parseInt(arr[1]);
		maze = mazes.get(mazeName);
		if (!mazes.containsKey(mazeName)) {
			setChanged();
			message = "There is no maze with the name: " + mazeName;
			notifyObservers("error");
			return;
		}

		if(index < 0) {
			setChanged();
			message = "The axis most be positive number!";
			notifyObservers("error");
			return;
		}

		try {
			if(axis.equals("z")) {
				setChanged();
				if (index < maze.getHeight()) {
					crossSection = maze.getCrossSectionByZ(index);
					notifyObservers("display_cross_section");
				} else {
					message = "The input of Z value must be smaller than: " + maze.getHeight();
					notifyObservers("error");
					return;
				}
			} else if (axis.equals("y")){
				setChanged();
				if(index < maze.getWidth()) {
					crossSection = maze.getCrossSectionByY(index);
					notifyObservers("display_cross_section");
				} else {
					message = "The input of Y value must be smaller than: " + maze.getWidth();
					notifyObservers("error");	
					return;
				}
			} else if (axis.equals("x")) {
				setChanged();
				if(index < maze.getLength()) {
					crossSection = maze.getCrossSectionByX(index);
					notifyObservers("display_cross_section");
				} else {
					message = "The input of X value must be smaller than: " + maze.getLength();
					notifyObservers("error");	
					return;
				}
			} else {		
				setChanged();
				message = "The axis most be x or y or z";
				notifyObservers("error");
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
		if (arr == null || arr.length != 2) {
			setChanged();
			message = "Invalid number of parameters";
			notifyObservers("error");
			return;
		}
		mazeName = arr[0];
		String fileName = arr[1];
		//		File file = new File(fileName);
		if (!mazes.containsKey(mazeName)) {
			setChanged();
			message = "There is no maze with the name: " + mazeName;
			notifyObservers("error");
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
			out = new MyCompressorOutputStream(new FileOutputStream(fileName+".maz"));
			out.write(ByteBuffer.allocate(4).putInt(mazeInBytes.length).array());
			out.write(mazeInBytes);
			setChanged();
			message = mazeName + " has been saved!";
			notifyObservers("displayMessage");
			out.close();

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
		if (arr == null || arr.length != 2) {
			setChanged();
			message = "Invalid number of parameters";
			notifyObservers("error");
			return;
		}

		String fileName = arr[0] + ".maz";
		File file = new File(fileName);
		mazeName = arr[1];

		//		if (mazes.containsKey(mazeName)) {
		//			controller.displayError("Maze already exist , try to load with other name.");
		//			return;
		//		}
		if(!file.exists() || file.isDirectory()) {
			setChanged();
			message = "File not exist / it is directory!";
			notifyObservers("error");
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
			setChanged();
			message = mazeName + " has been loaded from file: "  + fileName;	
			notifyObservers("displayMessage");
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
		if (arr == null || arr.length != 1) {
			setChanged();
			message = "Invalid number of parameters";
			notifyObservers("error");
			return;
		}
		mazeName = arr[0];
		String algorithm = properties.getSearchAlgorithm();
		Maze3d maze=mazes.get(mazeName);
		if (!mazes.containsKey(mazeName)) {
			setChanged();
			message = "There is no maze with the name: " + mazeName;
			notifyObservers("error");
			return;
		}

		threadPool.submit(new Callable<Solution<String>>() {

			@Override
			public Solution<String> call() throws Exception {
				HashMap<String, Searcher<String>> searchers = getSearcher();
				Searchable<String> mazeSearch = new Maze3dSearchable(maze);
				Searcher<String> searchAlgorithm = searchers.get(algorithm);

				if(searchAlgorithm == null) {
					setChanged();
					message = "Invalid algorithm";
					notifyObservers("error");
					return null;
				}

				mazeSolutions.put(mazeName, searchAlgorithm.Search(mazeSearch));
				setChanged();
				message = "Solution for: " + mazeName + " is ready!";
				notifyObservers("displayMessage");
				return mazeSolutions.get(mazeName);
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
		if (arr == null || arr.length != 1) {
			setChanged();
			message = "Invalid number of parameters";
			notifyObservers("error");
			return;
		}
		mazeName = arr[0];
		//Maze3d maze=mazes.get(mazeName);
		if (!mazes.containsKey(mazeName)) {
			setChanged();
			message = "There is no maze with the name: " + mazeName;
			notifyObservers("error");
			return;
		}

		if(mazeSolutions.containsKey(mazeName)) {
			setChanged();
			notifyObservers("display_solution");
			return;
		} else {
			setChanged();
			message = "Solution doesn't exist! / use 'Solve' commannd first!";
			notifyObservers("error");
		}
	}		

	/**
	 * Closing all running threads.
	 * @param emptyArr - There is nothing in the array.
	 */
	@Override
	public void exitCommand() {
		saveMazesAndSolutions();
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

	/**
	 * Save mazes and solutions into specific files
	 */
	@Override
	public void saveMazesAndSolutions() {
		try {
			FileOutputStream fos = new FileOutputStream("Solutions.zip");
			GZIPOutputStream gz = new GZIPOutputStream(fos);
			ObjectOutputStream oos = new ObjectOutputStream(gz);
			oos.writeObject(mazeSolutions);
			oos.flush();
			oos.close();
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			FileOutputStream fos = new FileOutputStream("Mazes.zip");
			GZIPOutputStream gz = new GZIPOutputStream(fos);
			ObjectOutputStream oos = new ObjectOutputStream(gz);
			oos.writeObject(mazes);
			oos.flush();
			oos.close();
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Load mazes and solution from specific files
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void loadMazesAndSolutions() {

		try {
			FileInputStream fis = new FileInputStream("Solutions.zip");
			GZIPInputStream gz = new GZIPInputStream(fis);
			ObjectInputStream ois = new ObjectInputStream(gz);
			mazeSolutions = (HashMap<String, Solution<String>>) ois.readObject();
			ois.close();
			fis.close();
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			FileInputStream fis = new FileInputStream("Mazes.zip");
			GZIPInputStream gz = new GZIPInputStream(fis);
			ObjectInputStream ois = new ObjectInputStream(gz);
			mazes = (HashMap<String, Maze3d>) ois.readObject();
			ois.close();
			fis.close();
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Save properties to file
	 * @param arr  - array of string with the parameters
	 */
	@Override
	public void saveProperties(String[] arr) {
		if (arr.length != 5) {
			setChanged();
			message = "Invalid number of parameters";
			notifyObservers("error");
			return;
		}
		if(!(isInteger(arr[0]) || isInteger(arr[3]))) {
			setChanged();
			message = "Number of threads or max size of maze must be integers";
			notifyObservers("error");
			return;
		}
		int numberOfThreads = Integer.parseInt(arr[0]);
		String algorithm = arr[1];
		String searchAlgorithm = arr[2];
		int maxSize = Integer.parseInt(arr[3]);
		String view = arr[4];
		HashMap<String, Maze3dGenerator> algorithms = getMaze3dGenerator();
		HashMap<String, Searcher<String>> searchers = getSearcher();
		if( numberOfThreads < 5) {
			setChanged();
			message = "Minimum number of threads need to be 5";
			notifyObservers("error");
			return;
		}
		
		if(!algorithms.containsKey(algorithm)) {
			message = "Invalid algorithm";
			setChanged();
			notifyObservers("error");
			return;
		}
		
		if(!searchers.containsKey(searchAlgorithm)) {
			setChanged();
			message = "Invalid search algoirthm";
			notifyObservers("error");
			return;
		}
		
		if( maxSize > 60 ) {
			setChanged();
			message = "Maximum of max maze can be 60";
			notifyObservers("error");
			return;
		}
		
		if( maxSize < 3 ) {
			setChanged();
			message = "Minimum of max maze can be 3";
			notifyObservers("error");
			return;
		}
		
		if(!(view.equals("cli") || view.equals("gui"))) {
			setChanged();
			message = "View most to be cli or gui only";
			notifyObservers("error");
			return;
		}
		try 
		{
			XMLEncoder xmlE = new XMLEncoder(new FileOutputStream("./resources/properties.xml"));
			xmlE.writeObject(new Properties(numberOfThreads, algorithm, searchAlgorithm , maxSize ,view));
			xmlE.close();
			properties.setNumberOfThreads(numberOfThreads);
			properties.setDefaultAlgorithm(algorithm);
			properties.setSearchAlgorithm(searchAlgorithm);
			properties.setMaxMazeSize(maxSize);
			properties.setViewSetup(view);
			message = "You must restart the program before\nthe new setting will take effect.";
			setChanged();
			notifyObservers("displayMessage");
			
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
		
		
	}
	
	/**
	 * Getter to get byte of array from hash map by maze name
	 * @param maze - Maze name
	 * @return Array of byte of maze
	 */
	@Override
	public byte[] getMazeFromHashMap(String maze) {
		return mazes.get(maze).toByteArray();
	}
	
	/**
	 * Getter to get maze name
	 * @return Maze name
	 */
	public String getMazeName() {
		return mazeName;
	}

	/**
	 * Getter to get cross section
	 * @return Cross section of the specific request
	 */
	public int[][] getCrossSection() {
		return crossSection;
	}

	/**
	 * Getter to get solution from hash map by maze name
	 * @param maze - Maze name
	 * @return Solution of the specific maze
	 */
	public Solution<String> getSolutionFromHashMap(String maze) {
		return mazeSolutions.get(maze);
	}

	/**
	 * Get list of files / directories from directory
	 * @return List of files / directories
	 */
	public String[] getList() {
		return fileList;
	}
	
	/**
	 * Get message
	 * @return Message
	 */
	@Override
	public String getMessage() {
		return message;
	}

	/**
	 * Getter to get properties
	 * @return Properties
	 */
	public Properties getProperties() {
		return properties;
	}
	
	/**
	 * Check if there is files called: "Solutions" and "Mazes"</br>
	 * in the directory
	 */
	public void intializeIfZipped(){
		File solutions = new File("Solutions.zip");
		File mazes = new File("Mazes.zip");
		if(solutions.exists() || mazes.exists()) {
			loadMazesAndSolutions();
		}
	}

	/**
	 * Function that check if the string is int
	 * @param s - String that need to check
	 * @return True if is int , otherwise false
	 */
	public static boolean isInteger(String s) {
	    return isInteger(s,10);
	}

	public static boolean isInteger(String s, int radix) {
	    if(s.isEmpty()) return false;
	    for(int i = 0; i < s.length(); i++) {
	        if(i == 0 && s.charAt(i) == '-') {
	            if(s.length() == 1) return false;
	            else continue;
	        }
	        if(Character.digit(s.charAt(i),radix) < 0) return false;
	    }
	    return true;
	}
}
