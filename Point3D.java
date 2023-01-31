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
	/** the cluster the point belongs to */
	private int clusterLabel; // 0 means undefined, -1 means "Noise"

	/** @param x x coordinate value
	 * @param y y coordinate value
	 * @param z z coordinate value */
	public Point3D(double x, double y, double z){
		this.x = x;
		this.y = y;
		this.z = z;
		clusterLabel = 0; // each point's label is initialized to undefined
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

	/** gets coordinate x, y or z if axis 0, 1, or 2
	 * @param axis 0, 1 or 2 for x, y or z axis
	 * @return the coordinate of the point at that axis*/
	public double get(int axis) {
		switch(axis) {
			case 0: return x;
			case 1: return y;
			case 2: return z;
			default: return 0.0;
		}
	}

	/** @return the cluster the point belongs to*/
	public int getClusterLabel(){
		return clusterLabel;
	}

	/** @param clusterLabel the cluster the point belongs to */
	public void setClusterLabel(int clusterLabel){
		this.clusterLabel = clusterLabel;
	}

	/** converts this Point object to a {@link String} object */
	public String toString(){
		return "(" + this.x + ", " + this.y + ", " + this.z + ")";
	}

	/** computes the euclidean distance between two points
	 * @param point the point from which the distance to this Point object */
	public double distance(Point3D point){
		return sqrt( pow(this.x - point.x, 2) + pow(this.y - point.y, 2) + pow(this.z - point.z, 2) );
	}

}