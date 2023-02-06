import java.io.*;
import java.util.*;
import java.lang.*;

/** represents a cloud of {@link Point3D}
 * @author Jordan Lau 340600240 */
public class PointCloud implements Iterable, Iterator{

	/** name of .xyz file to read points from */
	protected String filename;
	/** list of all the points in the point cloud */
	protected List <Point3D> cloud;
	
	/** @param filename name of .xyz file to read points from */
	public PointCloud(String filename){
		this.filename = filename;
		cloud = new LinkedList<Point3D>();

		try{
			BufferedReader br = new BufferedReader(new FileReader(filename));
			String[] array = br.readLine().split(" "); // read row and process point information
			cloud.add( new Point3D(Double.valueOf(array[0]), Double.valueOf(array[1]), Double.valueOf(array[2])) );
		} catch (IOException e){}
	}

	/** */
	public PointCloud(){
		cloud = new LinkedList<Point3D>();
	}

	//ITERATOR
    /** current index of list of points being read by {@link Iterator} */
    protected int current;

    /** An {@link Iterator} method that returns an iterator to the points in the cloud */
    public Iterator iterator() {
        current = 0;
        return this;
    }

    /** returns the next {@link Point3D} in the cloud for the iterator*/
    public Point3D next() {
		current++;
		return cloud.get(current - 1);
    }

    /** determines if the cloud has another point */
    public boolean hasNext(){
	    return current < cloud.size();
    }

    //END OF ITERATOR

	/** adds a point to the cloud */
	public void addPoint(Point3D pt){
        cloud.add(new Point3D(pt.getX(), pt.getY(), pt.getZ()));
	}
	
	/** returns a random point from the cloud */
	public Point3D getPoint(){
		int size = cloud.size();
		int index = (int) Math.floor(Math.random() * size);
		return cloud.get(index);
	}

	/** removes a specified point from the cloud */
	public void remove(Point3D p){
		cloud.remove(p);
	}
	
	/** saves the point cloud into a .xyz file
	 * @param filename name of new .xyz file to store the points in */
	public void save(String filename){
		Point3D pt;
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
			bw.write("x y z\n");
			for (int i = 0; i < cloud.size(); i++){
				pt = cloud.get(i);
				bw.write(pt.getX() + " " + pt.getY() + " " + pt.getZ());
				bw.write("\n");
				bw.close();
			}
		} catch (IOException e) {}
	}

}