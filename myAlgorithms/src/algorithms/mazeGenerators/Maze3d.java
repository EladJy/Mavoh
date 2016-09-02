package algorithms.mazeGenerators;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Creates a new empty 3D maze.</br>
 * Maze represented as 3D array of integer values.</br>
 * Each value can be 0/1.</br>
 * 0 - Represents a empty/free position.</br>
 * 1 - Represents a position with wall.
 * @author Elad Jarby
 *
 */
public class Maze3d {
	private int[][][] maze3d;//x-axis,y-axis,z-axis
	private int length;
	private int width;
	private int floors;
	Position startPosition;//Start position
	Position goalPosition;//Goal position
	public static final int FREE = 0;
	public static final int WALL = 1;

	/**
	 * Constructor for maze3d</br>
	 * Create an empty maze with the given parameters.</br>
	 * @param z - Height/Floors of the maze
	 * @param y - Width of the maze
	 * @param x - Length of the maze
	 */
	public Maze3d(int z , int y , int x) {

		length=x; //length of the maze
		width=y; //width of the maze
		floors=z; //height of the maze
		maze3d = new int[z][y][x];

		for (int i = 0; i < z; i++) {
			for (int j = 0; j < y; j++) {
				for (int k = 0; k < x; k++) {
					maze3d[i][j][k] = 0;
				}
			}
		}

		//Setting first and last z/y planes to 1, 
		for(int i=0;i<z;i++)
		{
			for(int j=0;j<y;j++)
			{
				maze3d[i][j][0]=1;
				maze3d[i][j][x-1]=1;
			}
		}

		//Setting first and last z/x planes to 1, 
		for(int i=0;i<z;i++)
		{
			for(int j=0;j<x;j++)
			{
				maze3d[i][0][j]=1;
				maze3d[i][y-1][j]=1;
			}
		}

		//Setting first and last y/x planes to 1, 
		for(int i=0;i<y;i++)
		{
			for(int j=0;j<x;j++)
			{
				maze3d[0][i][j]=1;
				maze3d[z-1][i][j]=1;
			}
		}
	}

	public Maze3d(byte[] byteArr) throws IOException {
		ByteArrayInputStream in = new ByteArrayInputStream(byteArr);
		DataInputStream data = new DataInputStream(in);

		this.floors = data.readInt();
		this.width  = data.readInt();
		this.length = data.readInt();

		maze3d = new int[floors][width][length];

		for (int i = 0; i < floors; i++) {
			for (int j = 0; j < width; j++) {
				for (int z = 0; z < length; z++) {
					maze3d[i][j][z] = data.read();
				}
			}
		}

		startPosition = new Position(data.readInt() , data.readInt() , data.readInt());

		goalPosition = new Position(data.readInt() , data.readInt() , data.readInt());
	}
	/**
	 * Set wall for specific position.
	 * @param pos - Position for setting the wall.
	 */
	public void setWall(Position pos) {
		maze3d[pos.getZ()][pos.getY()][pos.getX()] = WALL;
	}

	/**
	 * Set free for specific position.
	 * @param pos - Position for setting it as free space.
	 */
	public void setFree(Position pos) {
		maze3d[pos.getZ()][pos.getY()][pos.getX()] = FREE;
	}

	/**
	 * Fill all the maze with walls.
	 */
	public void fillWithWalls(){
		for (int i = 0; i < getHeight(); i++) {
			for (int j = 0; j < getWidth(); j++) {
				for (int k = 0; k < getLength(); k++) {
					maze3d[i][j][k] = 1;
				}
			}
		}
	}

	/**
	 * For even maze only , set some of the cells free (0).
	 */
	public void setFreeOnEven(){
		String[]moves = new String[6];
		if(getWidth() % 2 == 0) {
			for (int i = 1; i < getHeight()-1; i++) {
				for (int j = 1; j < getLength()-1; j++) {
					moves = getPossibleMoves(new Position(i,width-2,j));
					if(moves.length < 2)
						maze3d[i][width-2][j] = 0;
				}
			}
		}
		if(getLength() % 2 == 0) {
			for (int i = 1; i < getHeight()-1; i++) {
				for (int j = 1; j < getWidth()-1; j++) {
					moves = getPossibleMoves(new Position(i,j,length-2));
					if(moves.length < 2)
						maze3d[i][j][length-2] = 0;
				}
			}
		}
	}

	/**
	 * Get all possible moves , check if all the neighbors is free.
	 * If they free , return who is free.
	 * @param p - Position to get all possible moves from.
	 * @return Array of all position that you can move to , type: string.
	 */
	public String[] getPossibleMoves(Position p) {
		ArrayList<String> moves = new ArrayList<String>();
		Position temp;
		//Checks if there is path to right
		if(p.getX()<length&&maze3d[(p.getZ())][p.getY()][p.getX()+1]==0)
		{
			temp=new Position(p.getZ(),p.getY(),p.getX()+1);
			moves.add(temp.getPosition());
		}
		//Checks if there is path to left
		if(p.getX()>0&&maze3d[(p.getZ())][p.getY()][p.getX()-1]==0)
		{
			temp=new Position(p.getZ(),p.getY(),p.getX()-1);
			moves.add(temp.getPosition());
		}
		//Checks if there is path to up
		if(p.getY()<width &&maze3d[p.getZ()][p.getY()+1][p.getX()]==0)
		{
			temp=new Position(p.getZ(),p.getY()+1,p.getX());
			moves.add(temp.getPosition());
		}
		//Checks if there is path to down
		if(p.getY()>0&&maze3d[p.getZ()][p.getY()-1][p.getX()]==0)
		{
			temp=new Position(p.getZ(),p.getY()-1,p.getX());
			moves.add(temp.getPosition());
		}
		//Checks if there is path forward
		if(p.getZ()<floors&&maze3d[p.getZ()+1][p.getY()][p.getX()]==0)
		{
			temp=new Position(p.getZ()+1,p.getY(),p.getX());
			moves.add(temp.getPosition());
		}
		//Checks if there is path backward
		if(p.getZ()>0&&maze3d[p.getZ()-1][p.getY()][p.getX()]==0)
		{
			temp=new Position(p.getZ()-1,p.getY(),p.getX());
			moves.add(temp.getPosition());
		}

		String[] movesArray = moves.toArray(new String[moves.size()]);
		return movesArray;
	}

	/**
	 * Set value for specific position by x,y,z.
	 * @param z - Position according to Z axis.
	 * @param y - Position according to Y axis.
	 * @param x - Position according to X axis.
	 * @param value - change the value of specific position to 0/1 (free/wall).
	 */
	public void setPointValue(int z , int y , int x , int value) {
		maze3d[z][y][x] = value;
	}

	/**
	 * Getter , get value from specific position by x,y,z.
	 * @param z - Position according to Z axis.
	 * @param y - Position according to Y axis.
	 * @param x - Position according to X axis.
	 * @return Value from the given position.</br>
	 * 0 - free , 1 - wall
	 */
	public int getPointValue(int z , int y , int x) {
		return maze3d[z][y][x];
	}

	/**
	 * Getter , get start position.
	 * @return Start position of the maze.
	 */
	public Position getStartPosition() {
		return startPosition;
	}

	/**
	 * Setter , set start position.
	 * @param startPosition - Start position of the maze.
	 */
	public void setStartPosition(Position startPosition) {
		this.startPosition = startPosition;
	}
	/**
	 * Getter , get goal position.
	 * @return Goal position of the maze.
	 */
	public Position getGoalPosition() {
		return goalPosition;
	}

	/**
	 * Setter , set goal position.
	 * @param goalPosition - Goal position of the maze.	 
	 */
	public void setGoalPosition(Position goalPosition) {
		this.goalPosition = goalPosition;
	}

	/**
	 * Get length of the maze.
	 * @return Length of the maze.
	 */
	public int getLength() {
		return length;
	}

	/**
	 * Get width of maze.
	 * @return Width of the maze.
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Get how many floors on maze.
	 * @return Height/Floors of the maze.
	 */
	public int getHeight() {
		return floors;
	}

	/**
	 * Get cross section by x and check the bounds.
	 * @param x - Position according to X axis.
	 * @return 2D array of integers.
	 * @throws Exception IndexOutOfBoundsException </br>
	 * If input is negative or if input is smaller than length of the maze.
	 */
	public int[][] getCrossSectionByX(int x) throws Exception {
		if(x < 0)
			throw new IndexOutOfBoundsException("The input of X value must be non-negative number!");
		if(x > length -1)
			throw new IndexOutOfBoundsException("The input of X value must be smaller than: " + length);
		int [][]crossSection = new int[floors][width];
		for (int z = 0; z < floors; z++) {
			for (int y = 0; y < width; y++) {
				crossSection[z][y] = maze3d[z][y][x];
			}
		}
		return crossSection;
	}

	/**
	 * Get cross section by y and check the bounds.
	 * @param y - Position according to Y axis.
	 * @return 2D array of integers.
	 * @throws Exception IndexOutOfBoundsException </br>
	 * If input is negative or if input is smaller than width of the maze.
	 */
	public int[][] getCrossSectionByY(int y) throws Exception {
		if(y < 0)
			throw new IndexOutOfBoundsException("The input of Y value must be non-negative number!");
		if(y > width -1)
			throw new IndexOutOfBoundsException("The input of Y value must be smaller than: " + width);
		int [][]crossSection = new int[floors][length];
		for (int z = 0; z < floors; z++) {
			for (int x = 0; x < length; x++) {
				crossSection[z][x] = maze3d[z][y][x];
			}
		}
		return crossSection;
	}

	/*
	 * Get cross section by z and check the bounds.
	 * @param z - Position according to Z axis.
	 * @return 2D array of integers.
	 * @throws Exception IndexOutOfBoundsException </br>
	 * If input is negative or if input is smaller than floors of the maze.
	 */
	public int[][] getCrossSectionByZ(int z) throws Exception {
		if(z < 0)
			throw new IndexOutOfBoundsException("The input of Z value must be non-negative number!");
		if(z > floors -1)
			throw new IndexOutOfBoundsException("The input of Z value must be smaller than: " + floors);
		int [][]crossSection = new int[width][length];
		for (int y = 0; y < width; y++) {
			for (int x = 0; x < length; x++) {
				crossSection[y][x] = maze3d[z][y][x];
			}
		}
		return crossSection;
	}

	/**
	 * Print the 3D Maze.
	 */
	public void print() {
		for (int i = 0; i < getHeight(); i++) {
			for (int j = 0; j < getWidth(); j++) {
				for (int k = 0; k < getLength(); k++) { 
					System.out.print(maze3d[i][j][k] + " ");
				}
				System.out.println();
			}
			System.out.println();
		}
	}

	public byte[] toByteArray() {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		DataOutputStream data = new DataOutputStream(out);

		try {
			data.writeInt(floors);
			data.writeInt(width);
			data.writeInt(length);

			for (int i = 0; i < floors; i++) {
				for (int j = 0; j < width; j++) {
					for (int k = 0; k < length; k++) {
						data.write(maze3d[i][j][k]);
					}
				}
			}

			data.writeInt(startPosition.getZ());
			data.writeInt(startPosition.getY());
			data.writeInt(startPosition.getX());

			data.writeInt(goalPosition.getZ());
			data.writeInt(goalPosition.getY());
			data.writeInt(goalPosition.getX());

		} catch (IOException e) {
			e.printStackTrace();
		}
		return out.toByteArray();
	}

	public boolean equals(Object obj)
	{

		if(obj==null)
		{
			return false;
		}
		if(this==obj)
		{
			return true;
		}
		if(!(obj instanceof Maze3d))
		{
			return false;
		}

		Maze3d other = (Maze3d) obj;//now we know it is worker

		if(this.length!=other.length || this.width!=other.width || this.floors!=other.floors) {
			return false;
		} else if (this.startPosition.equals(other.startPosition)==false 
				|| this.goalPosition.equals(other.goalPosition)==false) {
			return false;
		} else {
			for(int i=0;i<floors;i++)
			{
				for(int j=0;j<width;j++)
				{
					for(int n=0;n<length;n++)
					{
						if(this.maze3d[i][j][n]!=other.maze3d[i][j][n])
						{
							return false;
						}
					}
				}
			}
		}

		return true;
	}
}
