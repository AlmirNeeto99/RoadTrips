
package Model;

import java.io.Serializable;

/**
 *
 * @author Jaevillen and Almir
 */
public class Spot implements Serializable{
    
    private double latitude;
    private double longitude;
    private int code;

    
    /**
     * Contructor of the class.
     * @param latitude The Latitude of the Spot.
     * @param longitude The Longitude of the Spot.
     * @param code The code of the Spot.
     */
    public Spot(double latitude, double longitude, int code) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.code = code;
    }

    public Spot(int code){
        this.code = code;
    }
    
    public int getCode() {
        return code;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    } 
}
