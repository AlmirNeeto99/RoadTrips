/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.Serializable;

/**
 *
 * @author Jaevillen
 */
public class Intersection extends Spot implements Serializable{
    
    private TypeIntersection type;

    
    /**
     * Constructor of the class.
     * @param type The type of the Intersection.
     * @param latitude The Latitude of the Intersection.
     * @param longitude The Longitude of the Intersection.
     * @param code The Code of the Intersection.
     */
    public Intersection(TypeIntersection type, double latitude, double longitude, int code) {
        super(latitude, longitude, code);
        this.type = type;
    }
    
    public Intersection(double latitude, double longitude, int code){
        super(latitude, longitude, code);
    }
    
    public Intersection(int code){
        super (code);
    }

    public TypeIntersection getType() {
        return type;
    }
    
    /**
     * Method that overwrite the Object hashCode and create a own HashCode for the class.
     * @return A hashCode.
     */
    @Override
    public int hashCode(){
        return 31 * this.getCode() * 47;
    }
    
     /**
     * Method that overwrite the Object equals and compares the code of the Intersection.
     * @param obj The Intersection to be compared.
     * @return True if the codes of the two Intersection are equals.
     */
    @Override
    public boolean equals(Object obj){
        if(obj instanceof Intersection){
            Intersection inter = (Intersection)obj;
        
            if(this.getCode() == inter.getCode()){
                return true;
            }
        }
        
        return false;
    }
    
    @Override
    public String toString(){
        return this.type.name() + "-"+this.getCode();
    }
    
    
 

}
