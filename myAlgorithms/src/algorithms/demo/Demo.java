package algorithms.demo;

import algorithms.mazeGenerators.GrowingTreeGenerator;
import algorithms.mazeGenerators.GrowingTreeRandomCell;
import algorithms.mazeGenerators.Maze3d;
import algorithms.mazeGenerators.Maze3dGenerator;
import algorithms.mazeGenerators.Maze3dSearchable;
import algorithms.mazeGenerators.Position;
import algorithms.search.BFS;
import algorithms.search.DFS;
import algorithms.search.Searchable;
import algorithms.search.Searcher;
import algorithms.search.Solution;

public class Demo {

	/**
	 * Running the demo. </br>
	 * Testing BFS and DFS algorithms
	 * @param z - Height/Floors of the maze
	 * @param y - Width of the maze
	 * @param x - Length of the maze
	 */
	public void run(int z , int y , int x)
	{
		Demo demo=new Demo(); 	
		Maze3dGenerator mg = new GrowingTreeGenerator(new GrowingTreeRandomCell());
		Solution<String> solution1 = new Solution<String>();
		Solution<String> solution2 = new Solution<String>();
		Searcher<String> BFS = new BFS<String>();
		Searcher<String> DFS = new DFS<String>();
		Maze3d maze=mg.generate(z,y,x);
		Position p=maze.getStartPosition();
		System.out.println("Start position: " + p);
		System.out.println("All possible movies from position: " + p + " are: "); // format "{z,y,x}"
		// get all the possible moves from a position
		String[] moves=maze.getPossibleMoves(p);
		// print the moves
		for(String move : moves)
			System.out.println(move);
		System.out.println("Goal position: " + maze.getGoalPosition());
		System.out.println();
		System.out.println("=== Growing tree. Last cell ===");
		try {
			demo.printMaze(maze);
			solution1=demo.testSearcher(BFS, new Maze3dSearchable(maze));
			solution2=demo.testSearcher(DFS, new Maze3dSearchable(maze));
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
		System.out.println("===============================");
		System.out.println("==== TEST OF BFS ALGORITHM ====");
		System.out.println("===============================");
		System.out.println("Number of nodes evaluated: " + BFS.getNumberOfNodesEvaluated());
		System.out.println("Shortest solution: " + solution1.size());
		System.out.println(solution1);
		System.out.println("===============================");
		System.out.println("==== TEST OF DFS ALGORITHM ====");
		System.out.println("===============================");
		System.out.println("Number of nodes evaluated: " + DFS.getNumberOfNodesEvaluated());
		System.out.println("Solution: " + solution2.size());
		System.out.println(solution2);
	}
	
	/**
	 * Testing given searching algorithm with the searchable.
	 * @param searcher - Searching algorithm
	 * @param searchable - Searching problem that we need to solve by algorithm.
	 * @return - Solution of the algorithm
	 */
	private Solution<String> testSearcher(Searcher<String> searcher, Searchable<String> searchable){
		 Solution<String> solution=searcher.Search(searchable);
		 return solution;
	}

	/**
	 * Print the 3D Maze.
	 * @param maze - The maze that need to be print.
	 * @throws Exception IndexOutOfBoundsException
	 */
	private void printMaze(Maze3d maze) throws Exception
	{
		Demo demo=new Demo();
		for(int z=0;z<maze.getHeight();z++)
		{
			System.out.println();
			System.out.println("Level: "+z);
			demo.printSection(maze.getCrossSectionByZ(z),"z");
		}
	}
	
	/**
	 * Print a cross section of the 3D Maze.
	 * @param CrossSection - Cross section that need to be print , by x/y/z.
	 * @param axis - The axis that we need to print. by x/y/z.
	 */
	private void printSection(int[][] CrossSection,String axis)
	{
		if(axis.equals("x"))
			System.out.println("Cross section by X:");
		if(axis.equals("y"))
			System.out.println("Cross section by Y:");
		if(axis.equals("z"))
			System.out.println("Cross section by Z:");
		for(int i = 0; i < CrossSection.length; i++)
		{
			for(int j = 0; j < CrossSection[0].length; j++)
			{
				System.out.print(CrossSection[i][j] + " ");
			}
			System.out.println();
		}	
	}
}
