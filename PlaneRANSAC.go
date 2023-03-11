//VEUILLEZ NOTEZ: passer les args dans l'ordre filename, confidence, percentage, eps value
package main
import "fmt"
import "os"
import "sync"
import "bufio"
import "time"
import . "math"
import . "strings"
import . "strconv"
import ("math/rand")

var bestSupportedPlane Plane3DwSupport = Plane3DwSupport{Plane3D{0,0,0,0}, 0}

// method that returns the estimated number of iterations required to obtain a "good enough" plane
func GetNumberOfIterations(confidence float64, percentageOfPointsOnPlane float64) int{
    return int( Ceil( Log( 1 - confidence ) / Log( 1 - Pow(percentageOfPointsOnPlane,3) ) ) )
}

// reads an XYZ file and returns a slice of Point3D
func ReadXYZ(filename string) []Point3D{
    var cloud []Point3D
    file, err := os.Open(filename)

    if err != nil{
        fmt.Println("error reading file")
    } else {
        scanner := bufio.NewScanner(file)
        scanner.Scan()
        
        for scanner.Scan(){
            line := scanner.Text()
            array := Split(line, "\t")
            a, _ := ParseFloat(array[0], 64)
            b, _ := ParseFloat(array[1], 64)
            c, _ := ParseFloat(array[2], 64)
            cloud = append(cloud, Point3D{a, b, c} )
        }
    }
    
    return cloud
}

// saves a slice of Point3D into an XYZ file
func SaveXYZ(filename string, points []Point3D){
    file, err := os.Create(filename);
    
    if (err != nil){
        fmt.Println("error saving file")
    } else {
        writer := bufio.NewWriter(file)
        writer.WriteString("x\ty\tz\n")
        for _, pt := range points {
            writer.WriteString( fmt.Sprintf("%f\t%f\t%f\t", pt.X, pt.Y, pt.Z) )
            writer.WriteString("\n")
        }
    }
}

//It randomly selects a point from the provided slice of Point3D (the input point cloud).
//Its output channel transmits instances of Point3D.
func randomPointGenerator(wg *sync.WaitGroup, stop chan bool, cloud []Point3D) chan Point3D{
    outputStream := make(chan Point3D)

    rand.Seed(time.Now().UnixNano())

    go func() {
        defer func() {wg.Done()}()
        defer close(outputStream)
        defer fmt.Println("\nFin de randomPointGenerator...")
        fmt.Println(len(cloud))
        select {
            case <-stop:
                break
            case outputStream <- cloud[rand.Intn(len(cloud))]:
        }
    }()
    
    return outputStream
}

//It reads Point3D instances from its input channel and accumulate 3 points.
//Its output channel transmits arrays of Point3D (composed of three points).
func tripletOfPointsGenerator(wg *sync.WaitGroup, stop chan bool, inputStream chan Point3D) chan [3]Point3D{
    outputStream := make(chan [3]Point3D)

    go func() {
        defer func() {wg.Done()}()
        defer close(outputStream)
        defer fmt.Println("\nFin de tripletOfPointsGenerator...")
        select {
            //we close this portion of the pipeline
            case <-stop:
                break
            //main code of this method
            case outputStream <- [3]Point3D{ <- inputStream, <- inputStream, <- inputStream }:
        }
    }()

    return outputStream
}

//It reads arrays of Point3D and resend them. It automatically stops the pipeline after having received N arrays.
func takeN(wg *sync.WaitGroup, stop chan bool, inputStream chan [3]Point3D, n int) chan [3]Point3D{
    outputStream := make(chan [3]Point3D)
    
    go func() {
        defer func() {wg.Done()}()
        defer close(outputStream)
        defer fmt.Println("\nFin de takeN...")
        for i:=0; i<n; i++ {
           select {
                case <-stop:
                    break
                case outputStream <-<- inputStream:
           }
        }
    }()
    
    return outputStream
}

//It reads arrays of three Point3D and compute the plane defined by these points.
//Its output channel transmits Plane3D instances describing the computed plane parameters.Args
func planeEstimator(wg *sync.WaitGroup, stop chan bool, inputStream chan [3]Point3D) chan Plane3D{
    outputStream := make(chan Plane3D)
    
    go func() {
        defer func() {wg.Done()}()
        defer close(outputStream)
        defer fmt.Println("\nFin de planeEstimator...")
        select {
            case <-stop:
                break
            case outputStream <- GetPlane(<-inputStream):
        }
    }()
    
    return outputStream
}

//It counts the number of points in the provided slice of Point3D (the input point cloud) that supports the receive 3D plane.
//Its output channel transmits the plane parameters and the number of supporting points in a Point3DwSupport instance. 
func supportingPointFinder(wg *sync.WaitGroup, stop chan bool, inputStream chan Plane3D, points []Point3D, eps float64) chan Plane3DwSupport{
    outputStream := make(chan Plane3DwSupport)
    
    go func() {
        defer func() {wg.Done()}()
        defer close(outputStream)
        defer fmt.Println("\nFin de supportingPointFinder...")
        select {
            case <-stop:
                break
            case outputStream <- GetSupport(<-inputStream, /*point cloud*/ points, eps):
        }
    }()
    
    return outputStream
}

//multiplexes the results received from multiple channels into one output channel
func fanIn(wg *sync.WaitGroup, stop chan bool, channels []chan Plane3DwSupport) chan Plane3DwSupport {

    var multiplexGroup sync.WaitGroup
    outputStream := make(chan Plane3DwSupport)
    
    //reads and puts the contents of each channel into a single channel, outputStream
    reader:= func(ch chan Plane3DwSupport) {
        //defers happen when the function is exited
        defer func() {multiplexGroup.Done()}()
        for i := range ch {
           select {
            case <-stop:
                return
            case outputStream <- i:
           }
        }
    }

    // launch the reader for each channel
    multiplexGroup.Add(len(channels))
    for _, ch := range channels {
        //calls the function above for each channel
        go reader(ch)
    }
    
    //will launch, staying behind to close the channel Outputstream, even while outputStream is returned
    // all goroutines must return before the output channel is closed
    go func() {
       defer func() {wg.Done()}()
       defer close(outputStream)
       defer fmt.Println("\nFin de fanIn...")
       multiplexGroup.Wait()
    }()

    return outputStream
}

//It receives Plane3DwSupport instances and keep in memory the plane with the best support received so far.
//This component does not output values, it simply maintains the provided *Plane3DwSupport variable. 
func dominantPlaneIdentifier(wg *sync.WaitGroup, stop chan bool, inputStream chan Plane3DwSupport){    
    go func() {
        defer func() {wg.Done()}()
        defer fmt.Println("\nFin de dominantPointIdentifier...")
        var plane Plane3DwSupport
        plane = <- inputStream
        if (plane.SupportSize > bestSupportedPlane.SupportSize){
            bestSupportedPlane = plane
            fmt.Println(bestSupportedPlane.thisPlane)
        }
        select {
            case <-stop:
                break
        }
    }()
}

/** run method that runs the RANSAC algorithm for identifying the dominant plane of the point cloud (only one plane)
 * this method will also remove the plane points from the point cloud 
 * and return a slice with the points of the dominant plane */
func main(){
    //filename := os.Args[0]
    //confidence := os.Args[1]
    //percentage := os.Args[2]
    //eps = os.Args[3]

    //OVERWRITE ARGS
    filename :=  "PointCloud1.xyz"
    confidence,_ := ParseFloat("0.20", 64)
    percentage,_ := ParseFloat("0.05", 64)
    eps,_ := ParseFloat("0.3", 64)

    pc := ReadXYZ(filename) //Point Cloud (slice of Points)
    SaveXYZ(string(filename[0:len(filename) - 4]) + "_p0.xyz", pc) //save old file
    
    numberOfIterations := GetNumberOfIterations(confidence, percentage)
    //fmt.Println("iterations: ", numberOfIterations)
    
    //PIPELINE
    stop := make(chan bool)
    var wg sync.WaitGroup
    wg.Add(4)
    pipeline := make(chan Plane3D)
    pipeline = planeEstimator(&wg, stop, takeN(&wg, stop, tripletOfPointsGenerator(&wg, stop, randomPointGenerator(&wg, stop, pc)), numberOfIterations))

    fanOut:= 8
    wg.Add(fanOut)
    //making an array of channels
    streams:= make([]chan Plane3DwSupport, fanOut)
    for i:=0; i<fanOut; i++ {
        streams[i] = supportingPointFinder(&wg, stop, pipeline, pc, eps)
    }

    wg.Add(2)
    dominantPlaneIdentifier(&wg, stop, fanIn(&wg, stop, streams))

    close(stop) // stop the threads
    wg.Wait()

    //STEP 5
    SaveXYZ(string(filename[0:len(filename) - 4]) + "_p1.xyz", GetSupportingPoints(bestSupportedPlane.thisPlane, pc, eps)) //save new file
}