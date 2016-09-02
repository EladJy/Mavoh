package algorithms.mazeGenerators;

/**
 * Position in maze by using x,y,z values.
 * @author Elad Jarby
 *
 */
public class Position {
	private int x;
	private int y;
	private int z;
	
	/**
	 * Default constructor , make default position as (0,0,0).
	 */
	public Position() {
		x=0;
		y=0;
		z=0;
	}
	
	/**
	 * Constructor , make a position with the given values
	 * @param z - Value of Z axis.
	 * @param y - Value of Y axis.
	 * @param x - Value of X axis.
	 */
	public Position(int z,int y,int x) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Get position in format (z,y,x).
	 * @return Position in string format.
	 */
	public String getPosition() {
		String str;
		str=("("+z+","+y+","+x+")");
		return str;
	}
	
	/**
	 * Get x value of current position.
	 * @return x value.
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Sets the x value in the current position.
	 * @param x - Value to be set.
	 */
	public void setX(int x) {
		this.x = x;
	}
	
	/**
	 * Get y value of current position.
	 * @return y value.
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * Sets the y value in the current position.
	 * @param y - Value to be set.
	 */
	public void setY(int y) {
		this.y = y;
	}
	
	/**
	 * Get z value of current position.
	 * @return z value.
	 */
	public int getZ() {
		return z;
	}
	
	/**
	 * Sets the z value in the current position.
	 * @param z - Value to be set.
	 */
	public void setZ(int z) {
		this.z = z;
	}
	
	/**
	 * Overriding the function toString from object.</br>
	 * In order to use print function in main.
	 */
	@Override
	public String toString() {
		return this.getPosition();
	}
	
	/**
	 * Converting position by string type to position type
	 * @param position - Position by string type that need to convert.
	 * @return Position position.
	 */
	public Position toPosition(String position)
	{
		int xyz[]=new int[3];
		String str=position.substring(1, position.length()-1);
		String[]retval=str.split(",");
		for(int i=0;i<retval.length;i++)
		{
			xyz[i]=Integer.parseInt(retval[i]);
		}
		Position p=new Position(xyz[0],xyz[1],xyz[2]);
		return p;
	}
	
	@Override
	public boolean equals(Object p)
	{
		if((p instanceof Position)==false) {
			return false;
		}
		return ((this.x==((Position)p).x) && (this.y==((Position)p).y) && (this.z==((Position)p).z));
	}
	
}
