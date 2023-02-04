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
	
	//method that returns the estimated number of iterations required to obtain a certain level of confidence to identify a plane made of a certain percentage of points
	public int getNumberOfIterations(double confidence, double percentageOfPointsOnPlane){
		/* how many iterations should we perform if we want to be almost certain (let’s say at 99%) that we have found the dominant plane?
		First, suppose that the percentage of points that support the dominant plane is p% of the total number of points in the cloud.

		The probability of randomly picking three points that belong to this plane is therefore p^3%

		We can then conclude that the probability of picking a set of random that contains at least one outlier is (1- p^3)%.
		If we pick k random triplets of points, the probability that these sets always contains an outlier is (1- p3 ) k %.

		Consequently, the probability of finding at least one set made of 3
		points that belongs to the dominant plane is 1-( 1- p3 ) k %.

		We must therefore find the value of k that give us a confidence probability of, let’s say, C= 99%.
		k = log( 1 - C ) / log( 1- p^3 )

		To find the three most dominant plane, you then need to repeat the complete procedure 3 times.*/
	}

	// run method that runs the RANSAC algorithm for identifying the dominant plane of the point cloud (only one plane)
	// filename being the xyz file that will contain the points of the dominant plane
	// this method will also remove the plane points from the point cloud
	public void run(int numberOfIterations, String filename){
		int currentSupport;
		int bestSupport = 0;
		Plane3D currentPlane;
		Plane3D dominantPlane;

		for (int i = 0; i < this.getNumberOfIterations; i++){
			Point3D p1 = pc.getPoint();
			Point3D p2 = pc.getPoint();
			Point3D p3 = pc.getPoint();
			currentPlane = new Plane3D(p1, p2, p3);
			currentSupport = 0;
			for (Point3D p : pc){ // iterate all points in the cloud and find current support
				if (plane.getDistance(eps) < eps)
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
		newFile.add(p1);
		newFile.add(p2);
		newFile.add(p3);
		newFile.save();
	}

}