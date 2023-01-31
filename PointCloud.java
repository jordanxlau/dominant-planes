import java.io.*;

public class PointCloud{

	protected String filename;
	
	//A constructor from a xyz file
	public PointCloud(String filename){
		this.filename = filename;
	}

	//An empty constructor that constructs an empty point cloud
	public PointCloud(){

	}

	//A addPoint method that adds a point to the point cloud
	public void addPoint(Point3D pt){
        try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
			bw.write(pt.getX() + " " + pt.getY() + " " + pt.getZ()); // read first row (column labels)
			bw.close();
        } catch (IOException e) {}
	}
	
	//A getPoint method that returns a random point from the cloud
	public Point3D getPoint(){

	}
	
	//A save method that saves the point cloud into a xyz file
	public void save(String filename){

	}

}