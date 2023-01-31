import static java.lang.Math.*;

public class Plane3D {

	protected double a, b, c, d;

	//Constructor from 3 points
	public Plane3D(Point3D p1, Point3D p2, Point3D p3){
		//normal vector = cross product of direction vectors v1 and v2
		Point3D v1 = new Point3D(p1.getX()-p2.getX(), p1.getY()-p2.getY(), p1.getZ()-p2.getZ());
		Point3D v2 = new Point3D(p2.getX()-p3.getX(), p2.getY()-p3.getY(), p2.getZ()-p3.getZ());
		a = v1.getY()*v2.getZ() - v2.getY()*v1.getZ();
		b = v1.getZ()*v2.getX() - v2.getZ()*v1.getX();
		c = v1.getX()*v2.getY() - v2.getX()*v1.getY();
		d = -1*a*p1.getX() -1*b*p1.getY() -1*c*p1.getZ();
	}
	
	//Constructor from 4 parameters
	public Plane3D(double a, double b, double c, double d){
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
	}
	
	//getDistance method that returns the distance from a point to the plane
	public double getDistance(Point3D pt){
		double numerator, denominator;
		numerator = abs(a*pt.getX() + b*pt.getY() + c*pt.getZ() + d);
		denominator = sqrt(pow(a,2) + pow(b,2) + pow(c,2));
		return numerator/denominator;
	}

}