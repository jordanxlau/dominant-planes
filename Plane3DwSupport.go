package main

type Plane3DwSupport struct {
    thisPlane Plane3D
    SupportSize int
}


// computes the support of a plane in a slice of points
func GetSupport(plane Plane3D, points []Point3D, eps float64) Plane3DwSupport{
	support := 0

	// iterate all points in the cloud and find current support
	for _, point := range points{
		if (plane.GetDistance(&point) < eps){
			support++
        }
	}

	return Plane3DwSupport{plane, support}
}

// extracts the points that supports the given plane
// and returns them in a slice of points
func GetSupportingPoints(plane Plane3D, points []Point3D, eps float64) []Point3D{
	
    //remove dominant plane points from the original point cloud and save it
    var supporting [] Point3D
    for _, point := range points{
        if (plane.GetDistance(&point) < eps){
            supporting = append(supporting, point)
        }
    }

    return supporting
}