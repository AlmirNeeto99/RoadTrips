
package Model;

import java.io.Serializable;

/**
 *
 * @author AlmirNeto
 */
public class EatPoint implements Serializable{
    
    private String name;
    private String adress;
    private Rate rate;
    
    /**
     * Contructor of the clas.
     * @param name The Name of the EatPoint.
     * @param adress The adress of the EatPoint.
     */
    public EatPoint(String name, String adress){
        this.name = name;
        this.adress = adress; 
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public Rate getRate() {
        return rate;
    }

    public void setRate(Rate rate) {
        this.rate = rate;
    }
    
    @Override
    public String toString(){
        return name;
    }
    
    
}
