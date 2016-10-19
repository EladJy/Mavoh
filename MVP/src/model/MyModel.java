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
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
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
import algorithms.mazeGenerators.SimpleMaze3dGenerator;
import algorithms.search.BFS;
import algorithms.search.DFS;
import algorithms.search.Searcher;
import algorithms.search.Solution;
import io.MyCompressorOutputStream;
import io.MyDecompressorInputStream;
import presenter.Properties;
import presenter.PropertiesLoader;

/**
 * Make all the calculatios for specific command.
 * @author Elad Jarby
 * @version 1.0
 * @since 13.09.2016
 */
public class MyModel extends Observable implements Model {

	private ExecutorService threadPool;
	private HashMap<String, Maze3d> mazes;
	private HashMap<String, Solution<String>> mazeSolutions;
	private String message;
	private int[][] crossSection;
	private String[] fileList;
	private String mazeName;
	private Properties properties;

	/**
	 * Initialize all the options for Maze3d generators
	 * @return Hashmap of Maze3d generators
	 */
	public HashMap<String, Maze3dGenerator> getMaze3dGenerator() {
		HashMap<String, Maze3dGenerator> commands = new HashMap<String , Maze3dGenerator>();
		commands.put("simple", new SimpleMaze3dGenerator());
		commands.put("growing-random" , new GrowingTreeGenerator(new GrowingTreeRandomCell()));
		commands.put("growing-last" , new GrowingTreeGenerator(new GrowingTreeLastCell()));
		return commands;
	}

	/**
	 * Initialize all the options for searchers
	 * @return Hashmap of searchers
	 */
	public HashMap<String, Searcher<String>> getSearcher() {
		HashMap<String, Searcher<String>> commands = new HashMap<String , Searcher<String>>();
		commands.put("dfs", new DFS<String>());
		commands.put("bfs" , new BFS<String>());
		return commands;
	}

	/**
	 * Constructor to initialize all the fields.
	 * @param mazes - HashMap of Strings(maze name) and Maze3d(maze)
	 * @param mazeSolution - Hashmap of Strings(maze name) and Solution<String>(Solutions)
	 * @param properties - Get properties of the client
	 * @param threadPool - Thread pool 
	 */
	public MyModel() {
		mazes = new HashMap<String, Maze3d>();
		mazeSolutions = new HashMap<String, Solution<String>>();
		intializeIfZipped();
		checkProperties();
		properties = PropertiesLoader.getInstance().getProperties();
		threadPool = Executors.newFixedThreadPool(properties.getNumberOfThreads());
	}

	private void checkProperties() {
		
		File f=new File("./resources/properties.xml");
		if(!f.exists())	{
			makeProperties();
		}
	}

	private void makeProperties() {
		try 
		{
			XMLEncoder xmlE = new XMLEncoder(new FileOutputStream("./resources/properties.xml"));
			xmlE.writeObject(new Properties(10, "growing-random", "dfs" , 20,"gui","127.0.0.1",1234));
			xmlE.close();
			System.out.println("XML File create successfuly!");
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}		
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
		if(!isInteger(arr[1]) || !isInteger(arr[2]) || !isInteger(arr[2])) {
			setChanged();
			message = "Axis must be integers!";
			notifyObservers("error");
			return;
		}
		mazeName = arr[0];
		int maxMaze = properties.getMaxMazeSize();
		int z = Integer.parseInt(arr[1]);
		int y = Integer.parseInt(arr[2]);
		int x = Integer.parseInt(arr[3]);
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
		//		if(mazes.containsKey(mazeName)) {
		//			setChanged();
		//			message = "Maze already exist , try to generate with other name.";
		//			notifyObservers("error");
		//			return;
		//		}
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
				Maze3d maze = mg.generate(z , y , x);
				setChanged();
				if(!maze.equals(mazes.get(mazeName)) && mazes.get(mazeName) != null) {
					mazes.remove(mazeName);
					mazeSolutions.remove(mazeName);
					mazes.put(arr[0].toString(), maze);
					message = "Maze: " + arr[0] + " is already exist.\nThe maze is override and now ready!";
				} else {
					message = "Maze: " + mazeName + " is ready!";
				}
				mazes.put(mazeName, maze);
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
		//			setChanged();
		//			message = "Maze already exist , try to load with other name.";
		//			notifyObservers("error");
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
			Maze3d maze = new Maze3d(byteArr);
			setChanged();
			if(!maze.equals(mazes.get(mazeName)) &&  mazes.get(mazeName) != null) {
				mazes.remove(mazeName);
				mazeSolutions.remove(mazeName);
				message = mazeName + " is already exist.\nThe maze is override and has been loaded from file: "  + fileName + " ";	
			} else {
				message = mazeName + " has been loaded from file: "  + fileName;	
			}
			mazes.put(mazeName, maze);
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
		//		Maze3d maze=mazes.get(mazeName);
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
				//				Searchable<String> mazeSearch = new Maze3dSearchable(maze);
				Searcher<String> searchAlgorithm = searchers.get(algorithm);

				if(searchAlgorithm == null) {
					setChanged();
					message = "Invalid algorithm";
					notifyObservers("error");
					return null;
				}

				Solution<String> solution = askServerForSolution(algorithm,mazeName);
				if(solution==null)
				{
					setChanged();
					message = "Problem with the server , try to open server!";
					notifyObservers("error");
					return null;
				}
				mazeSolutions.put(mazeName, solution);
				setChanged();
				notifyObservers("displayMessage");


				return mazeSolutions.get(mazeName);
			}

		});

	}

	@SuppressWarnings("unchecked")
	private Solution<String> askServerForSolution(String algorithm , String mazeName) {
		Socket server=null;
		ObjectOutputStream outToServer = null;
		ObjectInputStream inFromServer = null;

		try {
			//defines the socket and the input and output sources
			server = new Socket(properties.getServerIp().toString(),properties.getServerPort());

			//now we connected to the server
			//taking out of socket his output and input streams
			outToServer = new ObjectOutputStream(server.getOutputStream());
			inFromServer=new ObjectInputStream(server.getInputStream());
			outToServer.writeObject("Hi");
			outToServer.flush();

			inFromServer.readObject();

			ArrayList<Object> packetToServer = new ArrayList<Object>();
			packetToServer.add(algorithm);
			packetToServer.add(mazeName);
			packetToServer.add((mazes.get(mazeName)).toByteArray());

			outToServer.writeObject(packetToServer);
			outToServer.flush();

			message = (String) inFromServer.readObject();
			
			outToServer.writeObject("solve");
			outToServer.flush();
			
			Solution<String> solution = (Solution<String>) inFromServer.readObject();
			
			outToServer.close();
			inFromServer.close();
			server.close();
			return solution;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
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
			FileOutputStream fos = new FileOutputStream("ClientSolutions.zip");
			GZIPOutputStream gz = new GZIPOutputStream(fos);
			ObjectOutputStream oos = new ObjectOutputStream(gz);
			oos.writeObject(mazeSolutions);
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
			FileInputStream fis = new FileInputStream("ClientSolutions.zip");
			GZIPInputStream gz = new GZIPInputStream(fis);
			ObjectInputStream ois = new ObjectInputStream(gz);
			mazeSolutions = (HashMap<String, Solution<String>>) ois.readObject();
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
		if (arr.length != 7) {
			setChanged();
			message = "Invalid number of parameters";
			notifyObservers("error");
			return;
		}
		if(!isInteger(arr[0]) || !isInteger(arr[3]) || !isInteger(arr[6])) {
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
		String serverIp = arr[5];
		int serverPort = Integer.parseInt(arr[6]);

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
		
		if( serverPort < 1000) {
			setChanged();
			message = "Minimum port number need to be 1000";
			notifyObservers("error");
			return;
		}
		
		if( serverPort > 9999) {
			setChanged();
			message = "Maximum port number need to be 9999";
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
			xmlE.writeObject(new Properties(numberOfThreads, algorithm, searchAlgorithm , maxSize ,view , serverIp , serverPort));
			xmlE.close();
			properties.setNumberOfThreads(numberOfThreads);
			properties.setDefaultAlgorithm(algorithm);
			properties.setSearchAlgorithm(searchAlgorithm);
			properties.setMaxMazeSize(maxSize);
			properties.setViewSetup(view);
			properties.setServerIp(serverIp);
			properties.setServerPort(serverPort);
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
		if(solutions.exists()) {
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
