import java.io.*;
import java.util.*;
import java.lang.*;

public class PointCloud implements Iterable, Iterator{

	protected String filename;
	protected List <Point3D> cloud;
	
	//A constructor from a xyz file
	public PointCloud(String filename){
		this.filename = filename;
		cloud = new LinkedList<Point3D>();

		Iterator i = this.iterator();
		while (i.hasNext()){
			cloud.add((Point3D) i.next());
		}
	}

	//An empty constructor that constructs an empty point cloud
	public PointCloud(){
		cloud = new LinkedList<Point3D>();
	}

	// ITERATOR
	// Iterator Instance variables
    protected Point3D current;
    protected int currentLine;
    protected BufferedReader br;

    //An iterator method that returns an iterator to the points in the cloud
    public Iterator iterator() {
        current = null;
        currentLine = 1;
        try{
			br = new BufferedReader(new FileReader(filename));
		} catch (IOException e){}
        return this;
    }

    public Point3D next() {
		try{
			String[] array = br.readLine().split(" "); // read row and process point information
			current = new Point3D(Double.valueOf(array[0]), Double.valueOf(array[1]), Double.valueOf(array[2]));
		} catch (IOException e){}
		
		currentLine++;
		return current;
    }

    public boolean hasNext(){
    	try{
            return br.readLine() != null;
        } catch (IOException e) {
        	return false;
        }
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
		int size = cloud.size();
		int index = (int) Math.floor(Math.random() * size);
		return cloud.get(index);
	}

	//A remove method that removes a specified point from the cloud
	public void remove(Point3D p){
		Iterator i = this.iterator();
		for (int index = 0; i.hasNext(); index++){
			if (i.next().equals(p)){
				cloud.remove(index);
			}
		}
	}
	
	//A save method that saves the point cloud into a xyz file
	public void save(){
        for (int i = 0; i < cloud.size(); i++){
        	addPoint(cloud.get(i));
        }
    }

}