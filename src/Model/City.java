
package Model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Jaevillen and Almir
 */
public class City extends Spot implements Serializable{
    
    private String name;
    private String population;
    private ArrayList<EatPoint> placeEat;

    
    /**
     * Constructor of the class.
     * @param name The Name of the City.
     * @param latitude The Latitude of the City.
     * @param longitude The Longitude of the City.
     * @param code The Code of the City.
     */
    public City(String name, double latitude, double longitude, int code) {
        super(latitude, longitude, code);
        this.name = name;
    }

    public void setPopulation(String population) {
        this.population = population;
    }
    
    public City(int code){
        super(code);
    }

    public String getName() {
        return name;
    }

    public String getPopulation() {
        return population;
    }

    public ArrayList<EatPoint> getPlaceEat() {
        return placeEat;
    }

    public void setPlaceEat(ArrayList<EatPoint> placeEat) {
        this.placeEat = placeEat;
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
     * Method that overwrite the Object equals and compares the code of the City.
     * @param obj The City to be compared.
     * @return True if the codes of the two Cities are equals.
     */
    @Override
    public boolean equals(Object obj){
        if(obj instanceof City){
        City city = (City)obj;
        
            if(this.getCode() == city.getCode()){
                return true;
            }
        }
        return false;
    }
    
    @Override
    public String toString(){
        return this.getCode() + "-"+this.getName();
    }
}
