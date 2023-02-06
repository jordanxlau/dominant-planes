import static java.lang.Math.*;

public class PlaneRANSAC{
	protected double eps;
	protected PointCloud pc;

	//A constructor that takes as input a point cloud
	public PlaneRANSAC(PointCloud pc){
		this.pc = pc;
	}
	
	//setter for the epsilon value
	public void setEps(double eps){
		this.eps = eps;
	}
	
	//getter for the epsilon value
	public double getEps(){
		return eps;
	}
	
	/** method that returns the estimated number of iterations required to obtain a "good enough" plane
	@param confidence level of confidence that our plane is "good enough"
	@param percentage the percentage of points making up the plane we wish to identify */
	public int getNumberOfIterations(double confidence, double percentage){
		return (int) Math.ceil( log( 1 - confidence ) / log( 1 - pow(percentage,3) ) );
	}

	// run method that runs the RANSAC algorithm for identifying the dominant plane of the point cloud (only one plane)
	// filename being the xyz file that will contain the points of the dominant plane
	// this method will also remove the plane points from the point cloud
	public void run(int numberOfIterations, String filename){
		int currentSupport;
		int bestSupport = 0;
		Plane3D currentPlane;
		Plane3D dominantPlane;
		Point3D p1 = pc.getPoint();
		Point3D p2 = pc.getPoint();
		Point3D p3 = pc.getPoint();

		for (int i = 0; i < this.getNumberOfIterations(99, 1); i++){
			p1 = pc.getPoint();
			p2 = pc.getPoint();
			p3 = pc.getPoint();
			currentPlane = new Plane3D(p1, p2, p3);
			currentSupport = 0;
			for (Object o : pc){ // iterate all points in the cloud and find current support
				Point3D point = (Point3D) o;
				if (currentPlane.getDistance(point) < eps)
					currentSupport++;
			}
			if (currentSupport >= bestSupport){
				dominantPlane = currentPlane;
				bestSupport = currentSupport;
			}
		}
		
		//save the newfound dominant plane
		PointCloud newFile = new PointCloud(filename);
		pc.remove(p1);
		pc.remove(p2);
		pc.remove(p3);
		newFile.addPoint(p1);
		newFile.addPoint(p2);
		newFile.addPoint(p3);
		newFile.save();
	}

}