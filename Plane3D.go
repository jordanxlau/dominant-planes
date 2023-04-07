package main

import . "math"

type Plane3D struct {
    A float64
    B float64
    C float64
    D float64
}

// computes the plane defined by a slice of 3 points
func GetPlane(points [3]Point3D) Plane3D{
    p1 := points[0]
    p2 := points[1]
    p3 := points[2]

    //direction vectors v1 and v2
    v1 := Point3D{p1.X-p2.X, p1.Y-p2.Y, p1.Z-p2.Z}
    v2 := Point3D{p2.X-p3.X, p2.Y-p3.Y, p2.Z-p3.Z}

    //cross product of v1 and v2 (to get normal vector)
    a := v1.Y*v2.Z - v2.Y*v1.Z
    b := v1.Z*v2.X - v2.Z*v1.X
    c := v1.X*v2.Y - v2.X*v1.Y
    
    //solve for last variable
    d := -1*a*p1.X -1*b*p1.Y -1*c*p1.Z
    
    return Plane3D{a,b,c,d}
}

// I copied this helper function from
// https://stackoverflow.com/questions/37334119/how-to-delete-an-element-from-a-slice-in-golang
func remove(slice []Point3D, s int) []Point3D {
    return append(slice[:s], slice[s+1:]...)
}

// creates a new slice of points in which all points belonging to the plane have been removed
func RemovePlane(plane Plane3D, points []Point3D, eps float64) []Point3D{
    newPoints := make([]Point3D,0)
    for i, point := range(newPoints){
        if (plane.GetDistance(point) < eps){
            newPoints = remove(newPoints, i)
        }
    }
    return newPoints
}

/** computes the euclidean distance between a point and the plane*/
func (plane Plane3D) GetDistance(pt Point3D) float64{
    numerator := Abs(plane.A*pt.X + plane.B*pt.Y + plane.C*pt.Z + plane.D)
    denominator := Sqrt(Pow(plane.A,2) + Pow(plane.B,2) + Pow(plane.C,2))
    return numerator/denominator
}