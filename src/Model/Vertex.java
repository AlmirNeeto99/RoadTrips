
package Model;

import Util.HashMap;
import java.io.Serializable;

/**
 *
 * @author AlmirNeto
 */
public class Vertex implements Serializable{
    
    private Object obj;
    private HashMap adjacency;
    
    /**
     * Constructor of the class
     * @param obj The Object to be stored.
     */
    public Vertex(Object obj){
        this.obj = obj;
        this.adjacency = new HashMap();
    }

    public Object getVertex() {
        return obj;
    }

    public void setVertex(Object vertex) {
        this.obj = vertex;
    }

    public HashMap getAdjacency() {
        return adjacency;
    }

    public void setAdjacency(HashMap adjacency) {
        this.adjacency = adjacency;
    }
    
    /**
     * Method that overwrite the Object equals and compares the object of the Vertex.
     * @param obj The Vertex to be compared.
     * @return True if the object of the two Vertex are equals.
     */
    @Override
    public boolean equals(Object obj){
        if(obj instanceof Vertex){
            Vertex vertexObj = (Vertex)obj;
            if(this.obj.equals(vertexObj.getVertex())){
                return true;
            }
            return false;
        }
        return false;
    }
    
    /**
     * Method that overwrite the Object hashCode and create a own HashCode for the class.
     * @return A hashCode.
    */
    @Override
    public int hashCode(){
        return obj.hashCode();
    }
}
