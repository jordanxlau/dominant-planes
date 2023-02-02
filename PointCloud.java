import java.io.*;
import java.util.*;
import java.lang.Math;

public class PointCloud implements Iterator{

	protected String filename;
	protected List <Point3D> cloud;
	
	//A constructor from a xyz file
	public PointCloud(String filename){
		this.filename = filename;
		cloud = new LinkedList<Point3D>();

		Iterator i = iterator(filename);
		while (i.hasNext()){
			cloud.add(i.next());
		}
	}

	//An empty constructor that constructs an empty point cloud
	public PointCloud(){
		cloud = new LinkedList<Point3D>();
	}

	// ITERATOR
	// Iterator Instance variables
    protected Point3D current;

    //An iterator method that returns an iterator to the points in the cloud
    public Iterator iterator() {
        current = null;
        return this.Iterator;
    }

    public Object next() {
        try{
            BufferedReader br = new BufferedReader(new FileReader(filename));
            String[] array = br.readLine().split(","); // read row and process point information
            current = new Point3D(Double.valueOf(array[0]), Double.valueOf(array[1]), Double.valueOf(array[2]));
            br.close();
        } catch (IOException e) {}
        return current;
    }

    public boolean hasNext(){
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            br.close();
            return br.readLine() != null;
        } catch (IOException e) {}
    }

    //END OF ITERATOR

	//A addPoint method that adds a point to the point cloud
	public void addPoint(Point3D pt){
        try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
			bw.write(pt.getX() + " " + pt.getY() + " " + pt.getZ());
			bw.close();
        } catch (IOException e) {}
	}
	
	//A getPoint method that returns a random point from the cloud
	public Point3D getPoint(){
		int max = cloud.size() - 1;//max is exclusive
		Math.floor(Math.random() *(max - 0 + 1));
	}
	
	//A save method that saves the point cloud into a xyz file
	public void save(String filename){

	}

}