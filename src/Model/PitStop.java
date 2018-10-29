/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.Serializable;
import java.util.Calendar;

/**
 *
 * @author Jaevillen
 */
public class PitStop implements Serializable{
    private City city;
    private Calendar timeIn;
    private Calendar timeOut;

      /**
     * The constructor of the class.
     * @param city The Stop City. 
     * @param timeIn The arrival date.
     * @param timeOut The departure date.
     */
    public PitStop(City city, Calendar timeIn, Calendar timeOut) {
        this.city = city;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
    }
    
    public PitStop(City city){
        this.city = city;
    }
    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Calendar getTimeIn() {
        return timeIn;
    }

    public void setTimeIn(Calendar timeIn) {
        this.timeIn = timeIn;
    }

    public Calendar getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(Calendar timeOut) {
        this.timeOut = timeOut;
    }
    @Override
    public boolean equals(Object obj){
        PitStop ps = (PitStop)obj;
        
        if(ps.getCity().equals(this.city)){
            return true;
        }
        return false;
    }

}
