import static java.lang.Math.*;
import static java.lang.Double.*;

/** implements RANdom SAmpling Consensus algorithm
 * @author Jordan Lau 340600240 */
public class PlaneRANSAC{
	protected double eps;
	protected PointCloud pc;

	/** Constructor
	 * @param pc a point cloud */
	public PlaneRANSAC(PointCloud pc){
		this.pc = pc;
	}
	
	/** setter for the epsilon value 
	 * @param eps new epsilon value */
	public void setEps(double eps){
		this.eps = eps;
	}
	
	/** getter for the epsilon value */
	public double getEps(){
		return eps;
	}
	
	/** method that returns the estimated number of iterations required to obtain a "good enough" plane
	 * @param confidence level of confidence that our plane is "good enough" ∈ [0,1]
	 * @param percentage the percentage of points making up the plane we wish to identify ∈ [0,1] */
	public int getNumberOfIterations(double confidence, double percentage){
		return (int) ceil( log( 1 - confidence ) / log( 1 - pow(percentage,3) ) );
	}

	/** run method that runs the RANSAC algorithm for identifying the dominant plane of the point cloud (only one plane)
	 * this method will also remove the plane points from the point cloud 
	 * @param filename being the xyz file that will contain the points of the dominant plane */
	public void run(int numberOfIterations, String filename){
		int currentSupport;
		int bestSupport = 0;
		Plane3D currentPlane;
		Plane3D dominantPlane = new Plane3D(0,0,0,0);
		Point3D point;

		for (int i = 0; i < numberOfIterations; i++){
			System.out.println(i);
			currentPlane = new Plane3D(pc.getPoint(), pc.getPoint(), pc.getPoint());
			currentSupport = 0;

			//This is taking too long
			for (Object o : pc){ // iterate all points in the cloud and find current support
				point = (Point3D) o;
				if (currentPlane.getDistance(point) < eps)
					currentSupport++;
				if (currentSupport >= bestSupport){
					dominantPlane = currentPlane;
					bestSupport = currentSupport;
					break;
				}
			}
		}
		
		//remove dominant plane points from the original point cloud and save it
		PointCloud newFile = new PointCloud();
		for (Object o : pc){
			point = (Point3D) o;
			if (dominantPlane.getDistance(point) < eps){
				newFile.addPoint(point);
			}
			//pc.remove(point);
		}

		newFile.save(filename);
	}

	public static void main(String[] args){
		args = new String[]{"PointCloud1.xyz", "0.3", "0.99", "0.05", "1"};
		String filename = args[0];
		double eps = valueOf(args[1]);
		double confidence = valueOf(args[2]);
		double percentage = valueOf(args[3]);
		String trialNumber = args[4];

		PointCloud pc = new PointCloud(filename);
		PlaneRANSAC ransac = new PlaneRANSAC(pc);
		filename = filename.substring(0, filename.length() - 4) + "_p" + trialNumber + ".xyz";
		int numberOfIterations = 500;//ransac.getNumberOfIterations(confidence, percentage);
		ransac.setEps(eps);
		ransac.run(numberOfIterations, filename);
	}

}