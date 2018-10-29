
package Model;

import java.io.Serializable;

/**
 *
 * @author AlmirNeto
 */
public class Edge implements Serializable{
    
    private Vertex A;
    private Vertex B;
    private Object weight;
    
     /**
     * The constructor of the class.
     * @param a The first Vertex.
     * @param b The second Vertex.
     * @param weight The weight of the Edge.
     */
    public Edge(Vertex a, Vertex b, Object weight){
        this.A = a;
        this.B = b;
        this.weight = weight;
    }

    public Vertex getA() {
        return A;
    }

    public void setA(Vertex A) {
        this.A = A;
    }

    public Vertex getB() {
        return B;
    }

    public void setB(Vertex B) {
        this.B = B;
    }

    public Object getWeight() {
        return weight;
    }

    public void setWeight(Object weight) {
        this.weight = weight;
    }
    
    
     /**
     * Method that overwrite the Object equals and compares the Vertices of the Edge.
     * @param obj The Vertices to be compared.
     * @return True if the Vertices of the two Edges are equals.
     */
    @Override
    public boolean equals(Object obj){
        if(obj instanceof Edge){
            Edge edge = (Edge)obj;
            if((edge.getA() == null && edge.getB() == null) && (this.getA() == null && this.getB() == null)){
                return true;
            }
            else{
                return (this.getA().equals(edge.getA()) && this.getB().equals(edge.getB()) || this.getB().equals(edge.getA()) && this.getA().equals(edge.getB()));
            }
        }
        return false;
    }
    
     /**
     * Method that overwrite the Object hashCode and create a own HashCode for the class.
     * @return A hashCode.
     */
    @Override
    public int hashCode(){
        return (this.A.hashCode() + this.B.hashCode());
    }
}
