package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import algorithms.mazeGenerators.Maze3d;
import algorithms.mazeGenerators.Maze3dSearchable;
import algorithms.search.BFS;
import algorithms.search.DFS;
import algorithms.search.Searchable;
import algorithms.search.Searcher;
import algorithms.search.Solution;
import algorithms.search.State;

/**
 * Client handler that handle solution for a specified maze
 * @author Elad Jarby
 * @version 1.0
 * @since 19.10.2016
 */
public class MyClientHandler implements ClientHandler {
	HashMap<String, Solution<String>> mazeSolutions;
	HashMap<String, Maze3d> mazes;
	String message;

	public HashMap<String, Searcher<String>> getSearcher() {
		HashMap<String, Searcher<String>> commands = new HashMap<String , Searcher<String>>();
		commands.put("dfs", new DFS<String>());
		commands.put("bfs" , new BFS<String>());
		return commands;
	}

	public MyClientHandler() {
		mazeSolutions = new HashMap<String, Solution<String>>();
		mazes = new HashMap<String, Maze3d>();
		File solutions = new File("ServerSolutions.zip");
		if(solutions.exists()) {
			loadMazesAndSolutions();
		}
	}

	/**
	 * Handle client when he connected to the server
	 * @param inFromClient - Data that come from client
	 * @param outToClient - Data that server send to the client
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void handleClient(InputStream inFromClient, OutputStream outToClient) 
	{
		try{
			ObjectInputStream inputStream=new ObjectInputStream(inFromClient);
			ObjectOutputStream outputStream=new ObjectOutputStream(outToClient);
			String line;
			line=(String) inputStream.readObject();
			// Wait for "Hi" from client
			if(line.equals("Hi"))
			{
				// Return "Hello" to the client
				outputStream.writeObject("Hello");
				outputStream.flush();

				// Reading the ArrayList with algorithm , maze name , maze
				ArrayList<Object> packetFromClient=(ArrayList<Object>)inputStream.readObject();
				String algorithm = (String)packetFromClient.get(0);
				String mazeName = (String)packetFromClient.get(1);
				Maze3d maze=new Maze3d((byte[])packetFromClient.get(2));

				Solution<String> solution = new Solution<String>(new ArrayList<State<String>>());
				HashMap<String, Searcher<String>> searchers = getSearcher();
				Searchable<String> mazeSearch = new Maze3dSearchable(maze);
				Searcher<String> searchAlgorithm = searchers.get(algorithm);

				// Check if there is a solution in cache
				if(maze.equals(mazes.get(mazeName)))
				{
					solution = mazeSolutions.get(mazeName);
					message = "\tSolution is already exist , now sending to client";
					outputStream.writeObject("Solution for: " + mazeName + " is already exist!");
					outputStream.flush();
				} else {
					mazes.remove(mazeName);
					mazeSolutions.remove(mazeName);
					solution = searchAlgorithm.Search(mazeSearch);
					message = "\tSolution is ready , now sending to client";
					outputStream.writeObject("Solution for: " + mazeName + " is ready!");
					outputStream.flush();
					mazeSolutions.put(mazeName , solution);
					mazes.put(mazeName, maze);
				}

				line= (String) inputStream.readObject();
				if(line.equals("solve")) {

					// Write solution to the server
					outputStream.writeObject(solution);
					outputStream.flush();
				} else {
					outputStream.writeObject(null);
					outputStream.flush();
				}

				inputStream.close();
				outputStream.close();
			}			

		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
		} 


	}

	/**
	 * Function that save the solution when server is shutting down
	 */
	public void close()
	{
		try 
		{
			ObjectOutputStream objectOut;
			objectOut = new ObjectOutputStream(new GZIPOutputStream(new FileOutputStream("ServerSolutions.zip")));
			objectOut.writeObject(mazeSolutions);
			objectOut.writeObject(mazes);
			objectOut.flush();
			objectOut.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	/**
	 * Load all mazes and solution from ServerSolutions.zip file (cache)
	 */
	@SuppressWarnings("unchecked")
	private void loadMazesAndSolutions() {
		try {
			FileInputStream fis = new FileInputStream("ServerSolutions.zip");
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
	 * Message that need to print on server status
	 * @return String message
	 */
	@Override
	public String getMessage() {
		return message;
	}

}
