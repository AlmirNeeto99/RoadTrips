
package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

/**
 *
 * @author Jaevillen and Almir
 */
public class Trip implements Serializable{
    
    private ArrayList<PitStop> spots;
    private Calendar initialDate;
    private String name;
    
    /**
     * Contructor of the class.
     * @param name The name of the Trip.
     */
    public Trip(String name){
        this.name = name;
        this.spots = new ArrayList();
    }

    public ArrayList<PitStop> getSpots() {
        return spots;
    }

    public void setSpots(ArrayList<PitStop> spots) {
        this.spots = spots;
    }

    public Calendar getInitialDate() {
        return initialDate;
    }

    public void setInitialDate(Calendar initialDate) {
        this.initialDate = initialDate;
    }

    public String getName() {
        return name;
    }
    
     
    /**
    * Method that overwrite the Object equals and compares the name of the Trip.
    * @param obj The Trip to be compared.
    * @return True if the names of the two Trip are equals.
    */
    
    @Override
    public boolean equals(Object obj){
        Trip trip = (Trip)obj;
        return this.name.equalsIgnoreCase(trip.name);
    }
}
