
package Model;

import java.io.Serializable;

/**
 *
 * @author Jaevillen
 */
public class EntryDjikstra implements Comparable, Serializable{
    
    private Vertex cur;
    private Vertex prev;
    private double distance;

    
     /**
     * Constructor of the class.
     * @param prev The first Vertex.
     * @param cur The second Vertex.
     * @param distance The distance between the two Vertices.
     */
    public EntryDjikstra(Vertex cur,Vertex prev, double distance) {
        this.cur = cur;
        this.prev = prev;
        this.distance = distance;
    }

    public Vertex getPrev() {
        return prev;
    }

    public void setPrev(Vertex prev) {
        this.prev = prev;
    }

    public Vertex getCur() {
        return cur;
    }

    public void setCur(Vertex cur) {
        this.cur = cur;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    @Override
    public int compareTo(Object t) {
        EntryDjikstra e = (EntryDjikstra) t;
        
        if(this.distance > e.getDistance()){
            return 1;
        }
        else if(this.distance < e.getDistance()){
            return -1;
        }
        else{
            return 0;
        }
    }
    
    
}
