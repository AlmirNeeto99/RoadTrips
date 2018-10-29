/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Facade;

import Controller.Controller;
import Exceptions.*;
import Model.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author Jaevillen
 */
public class Facade {
    
    private Controller controller;
    
    public Facade () throws NoSuchAlgorithmException, UnsupportedEncodingException, DuplicatedDataException{
        controller = new Controller();
    }
    
    public User newUser (String name, String password, String cpf, String email) throws DuplicatedDataException, NoSuchAlgorithmException, UnsupportedEncodingException{
        return controller.newUser(name, password, cpf, email);
    }
    
    public User doLogin(String cpf, String password) throws NotFoundException, NoSuchAlgorithmException, UnsupportedEncodingException{
        return controller.doLogin(cpf, password);
    }
    
    public City addCity(String cityName, double latitude, double longitude, int code, String population) throws DuplicateEntryException{
        return controller.addCity(cityName, latitude, longitude, code, population);
    }
    
    public Intersection addIntersection(TypeIntersection type, double latitude, double longitude, int code) throws DuplicateEntryException{
        return controller.addIntersection(type, latitude, longitude, code);
    }
    
    public void addEatPoint(int code, String name, String adress, Rate rate) throws InexistentEntryException{
        controller.addEatPoint(code, name, adress, rate);
    }
    
    public City searchCity(int code) throws InexistentEntryException{
        return controller.searchCity(code);
    }
    
    public void addRoad (City cityA, City cityB, double km) throws DuplicateEntryException, AlreadyHasAdjacency, LoopIsNotAllowedException, InexistentVertexException, InexistentEntryException{
        controller.addRoad(cityA, cityB, km);
    }
    
    public void addRoad(Intersection inter,City city, double km) throws DuplicateEntryException, AlreadyHasAdjacency, InexistentVertexException, LoopIsNotAllowedException{
        controller.addRoad(inter, city, 0);
    }
    public void addRoad(Intersection interA, Intersection interB, double km) throws DuplicateEntryException, AlreadyHasAdjacency, InexistentVertexException, LoopIsNotAllowedException, InexistentVertexException, LoopIsNotAllowedException{
        controller.addRoad(interA, interB, 0);
    }
    
    public Trip startTrip(String cpf, String tripName, Calendar date) throws NotFoundException, DuplicateEntryException{
        return controller.startTrip(cpf, tripName, date);
    }
    
    public City insertCityInTrip(String cpf, String tripName, Calendar in, Calendar out, int code) throws InexistentEntryException, NotFoundException{
        return controller.insertCityInTrip(cpf, tripName, in, out, code);
    }
    
    public Iterator shortestPath(String cpf, String tripName) throws NotFoundException, InexistentEntryException, DuplicateEntryException, InsufficientSpotsException, TheresNoEntryException, NoWaysException{
        return controller.shortestPath(cpf, tripName);
    }
    
    public LinkedList getCities() throws TheresNoCityException{
        return controller.getCities();
    }
    
    public boolean haveCities(){
        return controller.hasCities();
    }
    
    public boolean hasInter(){
        return controller.hasInter();
    }
    
    public LinkedList getInter() throws TheresNoInterException{
        return controller.getInter();
    }
    
    public LinkedList getUserTrips(User user){
        return controller.getUserTrips(user);
    }
    
    public boolean haveTrips(User user){
        return controller.haveTrips(user);
    }
    
    public void removeCityFromTrip(Trip trip, int code) throws InexistentEntryException{
        controller.removeCityFromTrip(trip, code);
    }
    
    public boolean changeUserPassword(User user, String oldPw, String newPw) throws NoSuchAlgorithmException, UnsupportedEncodingException{
        return controller.changeUserPassword(user, oldPw, newPw);
    }
    
    public void removeUser(User user){
        controller.removeUser(user);
    }
    
    public void readFirstFile(String directory) throws IOException, FileNotFoundException{
        controller.readCityAndInter(directory);
    }
    
    public void readRoads(String directory) throws IOException, FileNotFoundException{
        controller.readRoads(directory);
    }
    
    public boolean hasFile(){
        return controller.hasFile();
    }
    
    public void loadDataFile() throws IOException, FileNotFoundException, ClassNotFoundException{
        controller.loadDataFile();
    }
    
    public void saveDataFile() throws IOException{
        controller.saveDataFile();
    }
    
    public void removeCity(City city) throws EmptyHashException, InexistentEntryException{
        controller.removeCity(city);
    }
    
    public boolean removeEatPoint(City city, EatPoint eat) throws ThereNoPlaceToEat{
        return controller.removeEatPoint(city, eat);
    }
    
    public void removeInter(Intersection inter) throws EmptyHashException, InexistentEntryException{
        controller.removeInter(inter);
    }
    
    public ArrayList getEatPlaces(City city){
        return controller.getEatPlace(city);
    }
    
    public LinkedList getRoads() throws ThereNoKeysException{
        return controller.getRoads();
    }
}
