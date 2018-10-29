
package Controller;

import Exceptions.ThereNoPlaceToEat;
import Exceptions.*;
import Model.*;
import Util.*;
import java.awt.geom.Line2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;
import java.util.StringTokenizer;

/**
 *
 * @author Jaevillen and Almir
 */
public class Controller {
   
    private Tree users; //HashSet with the logins of the Users.
    private Graph graph; //HashSet with the cities points.

    /**
     * Constructor of the class
     */
    public Controller() throws NoSuchAlgorithmException, UnsupportedEncodingException, DuplicatedDataException{
        
        users = new Tree();
        graph = new Graph();
        addAdmin();
    }

    /**
     * Method that add a new User in the Hash.
     * 
     * @param name The name of the User.
     * @param password The password of the User.
     * @param cpf The CPF of the User
     * @param email The e-Mail of the User.
     * @return The User after Created.
     * @throws DuplicatedDataException If there's already an User with the Login.
     * @throws java.security.NoSuchAlgorithmException
     * @throws java.io.UnsupportedEncodingException
     */
    public User newUser(String name, String password, String cpf, String email) throws DuplicatedDataException, NoSuchAlgorithmException, UnsupportedEncodingException{

        User newUser = new User(this.hashPassword(password), name, cpf, email, UserType.USER);
        
        try{
            alreadyIsUser(newUser);
            throw new DuplicatedDataException();
        }
        catch(NotFoundException nfx){
            users.inserir(newUser);
            return newUser;
        }         
    }
    /**
     * Method that tries to do Login to an User.
     * 
     * @param cpf CPF of the User.
     * @param password Password of the User.
     * @return TRUE If the User exists and the Password is correct, FALSE otherwise.
     * @throws NotFoundException If the User doesn't exist.
     * @throws java.security.NoSuchAlgorithmException
     * @throws java.io.UnsupportedEncodingException
     */
    public User doLogin(String cpf, String password) throws NotFoundException, NoSuchAlgorithmException, UnsupportedEncodingException{
        User user = new User(cpf, password);
        
        if(alreadyIsUser(user)){
            User verifyUser = getUser(cpf);

            if(verifyUser.getPassword().equalsIgnoreCase(this.hashPassword(password))){
                return verifyUser;
            }
        }
        return null;
    }
    
    /**
     *
     * Method that encrypts password of the user
     * 
     * @param password Password choose by the user.
     * @return the sequence that will be stored like password of the user.
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    private String hashPassword(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException{
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte hashPassword [] = messageDigest.digest(password.getBytes("UTF-8"));
        
        return Arrays.toString(hashPassword);
    }
    /**
     * Method that verifies if a User is already in Tree.
     * 
     * @param user User that will be verified.
     * @return TRUE if the User is already registered in the System.
     * @throws InexistentEntryException When the User isn't registered in the system.
     */
    private boolean alreadyIsUser(User user) throws NotFoundException{
        User verifyUser = (User)users.buscar(user);
        return true;
    }
    /**
     * Method that get an User in Tree and returns It.
     * @param cpf CPF of the User that will be searched.
     * @return The User with that CPF if exists.
     * @throws NotFoundException If the User with that CPF doesn't exist.
     */
    private User getUser(String cpf) throws NotFoundException{
        User user = new User(cpf);
        
        User ret = (User)users.buscar(user);
        
        return ret;
    }

    /**
     * Method that add a City into the Graph.
     * @param cityName The Name of the City.
     * @param latitude Latitude of the City.
     * @param longitude Longitude of the City.
     * @param code The Code of the City.
     * @param population The City's Population.
     * @return The City after created.
     * @throws DuplicateEntryException If has already a City with this Name.
     */
    public City addCity(String cityName, double latitude, double longitude, int code, String population) throws DuplicateEntryException{
        
        City city = new City(cityName.toUpperCase(), latitude, longitude, code);
        city.setPopulation(population);
        graph.addVertex(city); 
        return city;
    }
    
    
    /**
     * Method that add a Intersection into the Graph
     * @param type The type of the Intersection.
     * @param latitude Latitude of the Intersection.
     * @param longitude Longitude if the Intersection.
     * @param code The code of the Intersection.
     * @return The Intersection created.
     * @throws DuplicateEntryException If has already a Intersection with the code informed.
     */
    public Intersection addIntersection (TypeIntersection type, double latitude, double longitude, int code) throws DuplicateEntryException{
        Intersection intersection = new Intersection(type, latitude, longitude, code);
        
        graph.addVertex(intersection);
        
        return intersection;
    }
    
    /**
     * Method that Add a Point to Eat in a City.
     * @param code The Code of the city that the EatPoint is located.
     * @param name The Name of the Place.
     * @param adress The Address of the Place.
     * @param rate The Rate of the Place.
     * @throws InexistentEntryException If the City with this code doesn't exist.
     */
    public void addEatPoint(int code, String name, String adress, Rate rate) throws InexistentEntryException{
        EatPoint eat = new EatPoint(name.toUpperCase(), adress);
        eat.setRate(rate);

        City search = new City(code);

        City addEat = (City)graph.getKey(search);
        
        if(addEat.getPlaceEat() == null){
            addEat.setPlaceEat(new ArrayList());
            addEat.getPlaceEat().add(eat);
        }
        
        else{
            addEat.getPlaceEat().add(eat);
        }
    }
    /**
     * Method that Add a Road Between two Cities.
     * 
     * @param cityA The First City.
     * @param cityB The Second City.
     * @param km The length of the Road.
     * @throws DuplicateEntryException If there's already a Road between these Cities.
     * @throws AlreadyHasAdjacency If there's already a Road between these Cities.
     * @throws InexistentVertexException If one of the Cities doesn't exist.
     * @throws LoopIsNotAllowedException If the Codes are headed for the same City.
     * @throws Exceptions.InexistentEntryException
     */
    public void addRoad(City cityA, City cityB, double km) throws AlreadyHasAdjacency, InexistentVertexException, LoopIsNotAllowedException, InexistentEntryException, DuplicateEntryException{
        graph.addEdge(cityA, cityB, km);
    }
    
     /**
     * Method that adds a Road between a City and an Intersection.
     * 
     * @param inter The Intersection.
     * @param city The City.
     * @param km The length of the Road.
     * @throws DuplicateEntryException If there's already a Road between these Points.
     * @throws AlreadyHasAdjacency If there's already a Road between these Points.
     * @throws InexistentVertexException If one of the Points doesn't exist.
     * @throws LoopIsNotAllowedException If the Codes are headed for the same Point.
     */
    public void addRoad (Intersection inter,City city, double km) throws DuplicateEntryException, AlreadyHasAdjacency, InexistentVertexException, LoopIsNotAllowedException{
        graph.addEdge(inter,city, km);
    }
    
      /**
     * Method that adds a Road between a two Intersections.
     * 
     * @param interA The first Intersection.
     * @param interB The second Intersection.
     * @param km The length of the Road.
     * @throws DuplicateEntryException If there's already a Road between these Points.
     * @throws AlreadyHasAdjacency If there's already a Road between these Points.
     * @throws InexistentVertexException If one of the Points doesn't exist.
     * @throws LoopIsNotAllowedException If the Codes are headed for the same Point.
     */
    public void addRoad (Intersection interA, Intersection interB, double km) throws DuplicateEntryException, AlreadyHasAdjacency, InexistentVertexException, LoopIsNotAllowedException{
        graph.addEdge(interA, interB, km);
    }
    
    /**
     * Method that creates a Trip.
     * @param cpf The CPF of the user.
     * @param tripName The name identifies the trip.
     * @param date The Date of the Start of the Trip 
     * @return The trip.
     * @throws DuplicateEntryException If there's already a trip with the name informed.
     * @throws NotFoundException If the user doesn't exist.
     */
    public Trip startTrip(String cpf, String tripName, Calendar date) throws DuplicateEntryException, NotFoundException{
        
        User user = getUser(cpf);
        
        Trip trip = new Trip(tripName.toUpperCase());
        trip.setInitialDate(date);
        
        if(user.getTrip() == null){
            user.setTrip(new Queue());
            user.getTrip().put(trip);
        }
        else{
            if(user.getTrip().contains(trip)){
                throw new DuplicateEntryException();
            }
            user.getTrip().put(trip);
        }
        return trip;
    }
    
     /**
     * Method that search a City for your code. 
     * 
     * @param code The code of the City.
     * @return The City found.
     * @throws InexistentEntryException If there's no City in the Graph with the code informed.
     */
    public City searchCity(int code) throws InexistentEntryException{
        City city = new City(code);
        
        City realCity = (City) graph.getKey(city);
        
        return realCity;
    }
    
    /**
     * Method that search a Intersection for your code. 
     * 
     * @param code The code of the Intersection.
     * @return The Intersection found.
     * @throws InexistentEntryException If there's no Intersection in the Graph with the code informed.
     */
    public Intersection searchIntersection(int code) throws InexistentEntryException{
        Intersection inter = new Intersection(code);
        
        Intersection realInter = (Intersection) graph.getKey(inter);
        
        return realInter;
    }
    
    /**
     * Method that search a trip by the name.
     * @param cpf The CPF of the user.
     * @param tripName The name of the trip searched.
     * @return The trip.
     * @throws NotFoundException If doesn't exist a trip with the name informed.    
     */
    public Trip searchTrip(String cpf, String tripName) throws NotFoundException{

        User user = getUser(cpf);
        
        Trip trip = new Trip(tripName);
        if(user.getTrip().contains(trip)){
            Iterator it = user.getTrip().iterator();
            while(it.hasNext()){
                Trip test = (Trip) it.next();
                if(test.getName().equalsIgnoreCase(tripName)){
                    return test;
                }
            }
        }
        throw new NotFoundException();
    }
    
    /**
     * Method that insert a stop city in the trip
     * @param cpf The CPF of the user. 
     * @param tripName The name of the trip to be changed
     * @param in The arrival date in the city. 
     * @param out The departure date.
     * @param code The code of the stop city. 
     * @return The stop city.
     * @throws InexistentEntryException If doesn't exist city with the name informed.
     * @throws NotFoundException If doesn't exist trip with the name informed.
     */
    public City insertCityInTrip(String cpf, String tripName, Calendar in, Calendar out, int code) throws InexistentEntryException, NotFoundException{
        City realCity = this.searchCity(code);
       
        Trip trip = this.searchTrip(cpf, tripName);
        
        PitStop ps = new PitStop(realCity, in, out);
        
        trip.getSpots().add(ps);
        
        return realCity;
    }
    
   /**
     * Method that finds the shortest path for the Trip.
     * @param cpf The CPF of the user.
     * @param tripName The name of the Trip registered.
     * @return The Iterator of the ArrayList with the points of the shortest route.
     * @throws NotFoundException If there's not a Trip or an User registered in the system with the data informed.
     * @throws InexistentEntryException If there some City in the Trip that doesn't exists in the Graph.
     * @throws DuplicateEntryException 
     * @throws InsufficientSpotsException If the Trip don't has a least two points.
     */
    public Iterator shortestPath (String cpf, String tripName) throws NotFoundException, InexistentEntryException, DuplicateEntryException, InsufficientSpotsException, TheresNoEntryException, NoWaysException{

        Trip trip = this.searchTrip(cpf, tripName);
        ArrayList spots = trip.getSpots();
        if(spots.size() < 2){
            throw new InsufficientSpotsException();
        }
        
        Iterator it = spots.iterator();
        
        City start = null; 
        City end = null;
        EntryDjikstra anali = null, temp = null;
        
        ArrayList <EntryDjikstra> finalPath = new ArrayList();
        while(it.hasNext()){
            end = (City) ((PitStop) it.next()).getCity();
            if(start != null){
                Stack itStack = graph.path(start, end, graph.shortestPath(start));
                while(!itStack.empty()){
                    temp = (EntryDjikstra) itStack.pop();
                    if(anali == null || !anali.getCur().equals(temp.getCur())){
                        finalPath.add(temp); 
                    } 
                    
                } 
                anali = temp;
            }
            start = end;  
        }
        return finalPath.iterator();
    } 
    /**
     * Method that add an Admin to System.
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     * @throws DuplicatedDataException 
     */
    private void addAdmin() throws NoSuchAlgorithmException, UnsupportedEncodingException, DuplicatedDataException{
        
        String pw = hashPassword("pbl4");
        User admin = new User("admin", pw);
        admin.setName("Admin");
        
        users.inserir(admin);
    }
    /**
     * Method that verifies if There are Intersections in System.
     * @return TRUE if There's any Inter in System.
     */
    public boolean hasInter(){
        LinkedList<Vertex> inter;
        try{
            inter = graph.getAllVertex();
        } catch (ThereNoKeysException ex) {
            return false;
        }
        for(Vertex vertex : inter){
            if(vertex.getVertex() instanceof Intersection){
                return true;
            }
        }
        return false;
    }
    /**
     * Method that returns all Intersection of the System.
     * @return A LinkedList with all Intersections of the System.
     * @throws TheresNoInterException If there's no Intersection in System.
     */
    public LinkedList getInter() throws TheresNoInterException{
        LinkedList<Vertex> inter;
        try{
            inter = graph.getAllVertex();
        } catch (ThereNoKeysException ex) {
            throw new TheresNoInterException();
        }
       
        LinkedList casted = new LinkedList();
        
        for(Vertex vertex : inter){
            if(vertex.getVertex() instanceof Intersection){
                casted.add(vertex.getVertex());
            }
        }
        return casted;
    }
    /**
     * Method that returns all Cities of the System.
     * @return All Cities of The System.
     * @throws TheresNoCityException When there's no City in the System. 
     */
    public LinkedList getCities() throws TheresNoCityException{
        
        LinkedList<Vertex> city;
        try{
            city = graph.getAllVertex();
        } catch (ThereNoKeysException ex) {
            throw new TheresNoCityException();
        }
       
        LinkedList casted = new LinkedList();
        
        for(Vertex vertex : city){
            if(vertex.getVertex() instanceof City){
                casted.add(vertex.getVertex());
            }
        }
        return casted;
    }
    /**
     * Method that verifies if there are Cities in System.
     * @return TRUE If there are City in System.
     */
    public boolean hasCities(){
        LinkedList<Vertex> city;
        try{
            city = graph.getAllVertex();
        } catch (ThereNoKeysException ex) {
            return false;
        }
        for(Vertex vertex : city){
            if(vertex.getVertex() instanceof City){
                return true;
            }
        }
        return false;
    }
    /**
     * Method that returns the Trips from an User.
     * @param user The User that will be verified if there aew Trips.
     * @return A LinkedList with all User's Trips.
     */
    public LinkedList getUserTrips(User user){
        
        LinkedList trips;
        if(haveTrips(user)){
            trips = user.getTrip().toList();
            return trips;
        }
        return null;
    }
    /**
     * Method that verifies if an User has Trips registered.
     * @param user The User that will be verified if there are Trips.
     * @return TRUE if there's the User has Trips.
     */
    public boolean haveTrips(User user){
        if(user.getTrip() == null){
            return false;
        }
        else{
            return true;
        }
    }
    /**
     * Method that remove a City from a Trip
     * @param trip The Trip that the city will be removed.
     * @param code The Code of the City that will be removed.
     * @throws InexistentEntryException If the City with the Code doesn't exist.
     */
    public void removeCityFromTrip(Trip trip, int code) throws InexistentEntryException{
        
        ArrayList<PitStop> spots = trip.getSpots();
        City city = searchCity(code);
        
        PitStop remove = new PitStop(city);
        
        spots.remove(remove);
    }
    /**
     * Method that change an User's password if s/he wants.
     * @param user The User that wants to change the password.
     * @param oldPw The Old Password to verify if it's the Owner of the account.
     * @param newPw The New Password of the User's Account.
     * @return TRUE If the password was changed.
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException 
     */
    public boolean changeUserPassword(User user, String oldPw, String newPw) throws NoSuchAlgorithmException, UnsupportedEncodingException{
        String oldPw_hash = this.hashPassword(oldPw);
        
        if(!user.getPassword().equals(oldPw_hash)){
            return false;
        }
        else{
            String newPw_hash = this.hashPassword(newPw);
            user.setPassword(newPw_hash);
            return true;
        }
    }
    /**
     * Method that removes an User from the System, if it Wants.
     * @param user 
     */
    public void removeUser(User user){
        try{
            users.remover(user);
        } catch (NotFoundException ex) {
            //Will never enter here.
        }
    }
    /**
     * Method that Read a File containing Cities and Intersection and Up them to System.
     * @param directory The Directory of the File.
     * @throws FileNotFoundException If the File doesn't exist.
     * @throws IOException 
     */
    public void readCityAndInter(String directory) throws FileNotFoundException, IOException{
        File arq = new File(directory);
        
        FileReader file = new FileReader(arq);
        BufferedReader br = new BufferedReader(file);
        
        String line = br.readLine(); 
        
        while(line != null){
            
            StringTokenizer token = new StringTokenizer(line);
            int colls = token.countTokens();
            if(colls == 4){
                String type = token.nextToken();
                String code = token.nextToken();
                String lat = token.nextToken();
                String longi = token.nextToken();
                
                int intCode = Integer.parseInt(code);
                double latDouble = Double.parseDouble(lat);
                double longiDouble = Double.parseDouble(longi);

                if(type.equals("R")){
                    Intersection inter;
                    
                    try{
                        inter = this.addIntersection(TypeIntersection.ROTULA, latDouble, longiDouble, intCode);
                    } catch (DuplicateEntryException ex) {
                        //If there's already an Intersection with this Code, it'll not add other Inter.
                    }
                }
                else if(type.equals("C")){
                    Intersection inter;
                    
                    try{
                        inter = this.addIntersection(TypeIntersection.CROSSING, latDouble, longiDouble, intCode);
                    } catch (DuplicateEntryException ex) {
                        //If there's already an Intersection with this Code, it'll not add other Inter.
                    }
                }
                else if(type.equals("S")){
                    Intersection inter;
                    
                    try{
                        inter = this.addIntersection(TypeIntersection.SEMAPHORE, latDouble, longiDouble, intCode);
                    } catch (DuplicateEntryException ex) {
                        //If there's already an Intersection with this Code, it'll not add other Inter.
                    }
                }
                line = br.readLine();
            }
            else if(colls > 4){
                String cityName = "";
                int name = colls - 4;
                for(int i = 0; i < name; i++){
                    if(i == 0){
                        cityName = token.nextToken();
                    }
                    else{
                        cityName = cityName + " " + token.nextToken();
                    }
                }
                String code = token.nextToken();
                String lat = token.nextToken();
                String longi = token.nextToken();
                String pop = token.nextToken();
                
                int intCode = Integer.parseInt(code);
                double latDouble = Double.parseDouble(lat);
                double longiDouble = Double.parseDouble(longi);
                City city;
                try{
                    city = this.addCity(cityName, latDouble, longiDouble, intCode, pop);
                } catch (DuplicateEntryException ex) {
                }
                line = br.readLine();
            }
        }
        file.close();
        br.close();
        
    }
    /**
     * Method that Read a File containing Roads and Up them to System.
     * @param directory The Directory of the File.
     * @throws FileNotFoundException If the File doesn't exist.
     * @throws IOException 
     */
    public void readRoads(String directory) throws FileNotFoundException, IOException{
        
        File arq = new File(directory);
        
        FileReader file = new FileReader(arq);
        BufferedReader br = new BufferedReader(file);
        
        String line = br.readLine();
        
        while(line != null){
            StringTokenizer token = new StringTokenizer(line);
            String start = token.nextToken();
            String end = token.nextToken();
            String weight = token.nextToken();
            
            int startCode = Integer.parseInt(start);
            int endCode = Integer.parseInt(end);
            double weightDouble = Double.parseDouble(weight);
            City cityA = null;
            City cityB = null;
            try{
                cityA = this.searchCity(startCode);
                cityB = this.searchCity(endCode);
                this.addRoad(cityA, cityB, weightDouble);
            }
             catch (InexistentEntryException ex) {
                //If any of both Cities doesn't exist, it'll not add a Road to System.
            } catch (AlreadyHasAdjacency ex) {
                //If there's already a Road between these cities.
            } catch (InexistentVertexException ex) {
                //If one of the cities doesn't exist.
            } catch (LoopIsNotAllowedException ex) {
                //If both cities are equal.
            } catch (DuplicateEntryException ex) {
            }
            line = br.readLine();
        }
        file.close();
        br.close();
    }
    /**
     * Create a new File of System Data.
     * @throws IOException 
     */
    private void createNewFile() throws IOException{
        File file = new File("src/Data.ser");
        file.createNewFile();
    }
    /**
     * Load the Data File, if exists.
     * @throws FileNotFoundException
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    public void loadDataFile() throws FileNotFoundException, IOException, ClassNotFoundException{
        File file;
        
        file = new File("src/Data.ser");
        if(file.exists()){
            ObjectInputStream in;
            in = new ObjectInputStream(new FileInputStream(file));
            users = (Tree)in.readObject();
            graph = (Graph)in.readObject();   
            in.close();
        }
    }
    /**
     * Verify if the Data File exists.
     * @return TRUE if the File exists.
     */
    public boolean hasFile(){
         File file;
        
        file = new File("src/Data.ser");
        return file.exists();
    }
    /**
     * Save the System Data in the Data File.
     * @throws IOException
     * @throws FileNotFoundException 
     */
    public void saveDataFile() throws IOException, FileNotFoundException{

        File file = new File("src/Data.ser");
        
        if(!hasFile()){
            createNewFile();
        }
        ObjectOutputStream out;
        out = new ObjectOutputStream(new FileOutputStream(file));
        
        out.writeObject(users);
        out.writeObject(graph);
        out.close();
    }
    /**
     * Method that removes a City from System.
     * @param city The City that will be removed.
     * @throws EmptyHashException If there's no cities in System.
     * @throws InexistentEntryException If the City doesn't exists.
     */
    public void removeCity(City city) throws EmptyHashException, InexistentEntryException{
        graph.removeVertex(city);
    }
    /**
     * Method that removes a Place to Eat from a City.
     * @param city The City that the Place if located.
     * @param eat The Place that will be removed.
     * @return TRUE if the place was removed with success.
     * @throws ThereNoPlaceToEat If the City doesn't have Eat Points.
     */
    public boolean removeEatPoint(City city, EatPoint eat) throws ThereNoPlaceToEat{
        boolean boo;
        if(city.getPlaceEat() == null){
            throw new ThereNoPlaceToEat();
        }
        else{
            boo = city.getPlaceEat().remove(eat);
        }
        return boo;
    }
    /**
     * Method that remove an Intersection from System.
     * @param inter The inter that will be removed.
     * @throws EmptyHashException If there's no Inter in System.
     * @throws InexistentEntryException If the intersection doesn't exists.
     */
    public void removeInter(Intersection inter) throws EmptyHashException, InexistentEntryException{
        graph.removeVertex(inter);
    }
    
    public ArrayList getEatPlace(City city){
        if(city.getPlaceEat() != null){
            if(city.getPlaceEat().size() > 0){
                return city.getPlaceEat();
            }
        }
        else{
            return null;
        }
        return null;
    }
    
    public LinkedList getRoads() throws ThereNoKeysException{
        return graph.getAllEdges();
    }
}
