package main
import . "math"

type Point3D struct {
    X float64
    Y float64
    Z float64
}

// computes the distance between points p1 and p2
func (p1 *Point3D) GetDistance(p2 *Point3D) float64{
    return Sqrt( Pow(p1.X - p2.X, 2) + Pow(p1.Y - p2.Y, 2) + Pow(p1.Z - p2.Z, 2) )
}