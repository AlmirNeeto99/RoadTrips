
package Model;

import Util.Queue;
import java.io.Serializable;

/**
 *
 * @author Jaevillen
 */
public class User implements Comparable, Serializable{
    
    private String password;;
    private String cpf;
    private String name;
    private String email;
    private Queue trip;
    private UserType type;
    
    /**
     * Constructor of the class.
     * @param password The password for the login.
     * @param name The Name of the User.
     * @param cpf The CPF of the User.
     * @param email The email of the User.
     * @param type The type of User
     */
    public User(String password, String name, String cpf, String email, UserType type){
        this.password = password;
        this.name = name;
        this.cpf = cpf;
        this.email = email;
        this.type = type;
    }

    public UserType getType() {
        return type;
    }
    
    /**
     * Constructor of the class.
     * @param cpf The CPF of the User.
     * @param password The password for the login.
     */
    public User(String cpf, String password){
        this.cpf = cpf;
        this.password = password;
    }
    
     
    /**
     * Constructor of the class.
     * @param cpf The CPF of the User.
     */
    public User(String cpf){
        this.cpf = cpf;
    }
            
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCpf() {
        return cpf;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Queue getTrip() {
        return trip;
    }

    public void setTrip(Queue trip) {
        this.trip = trip;
    }
        
    /**
     * Method that overwrite the Object equals and compares the CPF of the User.
     * @param obj The User to be compared.
     * @return True if the CPFs of the two Users are equals.
    */
    @Override
    public boolean equals(Object obj){
        User comp = (User)obj;
        
        return this.cpf.equalsIgnoreCase(comp.getCpf());
    }
    
    /*
     * Method that overwrite the Object hashCode and create a own HashCode for the class.
     * @return A hashCode.
    */
    @Override
    public int hashCode(){
        int h = 0;
        String newString = cpf.concat(password.concat(name.concat(email)));
        for(int i = 0; i < newString.length(); i++){
            h = 31 * newString.charAt(i) + i;
        }
        return h;
    }

    @Override
    public int compareTo(Object obj) {
        if(obj instanceof User){
            User user = (User )obj;
            if(cpf.compareTo(user.getCpf()) < 0){
                return -1;
            }
            else if(cpf.compareTo(user.getCpf()) > 0){
                return 1;
            }
            else{
                return 0;
            }     
        }
        // Throw Exception if the Object isn't an User.
        throw new RuntimeException();
    }
    
}
