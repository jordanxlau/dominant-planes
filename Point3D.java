import static java.lang.Math.*;

/** represents a 3D point
 * @author Jordan Lau 340600240 */
public class Point3D{

	/** x coordinate value */
	private double x;
	/** y coordinate value*/
	private double y;
	/** z coordinate value*/
	private double z;

	/** @param x x coordinate value
	 * @param y y coordinate value
	 * @param z z coordinate value */
	public Point3D(double x, double y, double z){
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/** @return the x coordinate */
	public double getX(){
		return x;
	}

	/** @return the y coordinate */
	public double getY(){
		return y;
	}

	/** @return the z coorinate */
	public double getZ(){
		return z;
	}

	/** converts this Point object to a {@link String} object */
	public String toString(){
		return "(" + this.x + ", " + this.y + ", " + this.z + ")";
	}

	/** returns true iff the points are at the same coordinates */
	public boolean equals(Point3D that){
		return this.x == that.x && this.y == that.y && this.z == that.z;
	}

	/** computes the euclidean distance between two points
	 * @param point the point from which the distance to this Point object */
	public double distance(Point3D point){
		return sqrt( pow(this.x - point.x, 2) + pow(this.y - point.y, 2) + pow(this.z - point.z, 2) );
	}

}