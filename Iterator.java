public class Iterator<Point3D>{
    // Iterator Instance variables
    private Node current;

    //An iterator method that returns an iterator to the points in the cloud
    public Iterator<Point3D> iterator() {
        current = null;
        return this;
    }

    public Comparable next() {
        if ( current == null ) {
            current = head;
        } else {
            current = current.next;
        }
        return current.value; //hasNext() usually called before!
    }

    public boolean hasNext(){
        return ;
    }
}