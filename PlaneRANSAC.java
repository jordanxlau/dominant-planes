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
	
	//method that returns the estimated number of iterations required to obtain a certain level
	//of confidence to identify a plane made of a certain percentage of points
	public int getNumberOfIterations(double confidence, double percentageOfPointsOnPlane){

	}

	// run method that runs the RANSAC algorithm for identifying the dominant plane of the point cloud (only one plane)
	// filename being the xyz file that will contain the points of the dominant plane
	// this method will also remove the plane points from the point cloud
	public void run(int numberOfIterations, String filename){
		
	}

}