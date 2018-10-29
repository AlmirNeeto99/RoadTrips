/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AcceptationTest;

import Controller.Controller;
import Exceptions.*;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import Model.*;
import Util.HashSet;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Random;

/**
 *
 * @author Almir and Jaevillen
 */
public class AcceptationTest {
    
    private final Controller controller;
    private City capela, novaFatima, riachao, itatiaia, gaviao, saoJose, capimGrosso;
    private Intersection inter;
    private Calendar date = Calendar.getInstance();
        
    public AcceptationTest() throws NoSuchAlgorithmException, DuplicatedDataException, UnsupportedEncodingException {
        controller = new Controller();
        date.set(10, 10, 10);
    
    }
    
    @Before
    public void setUp() {
 
    }
    
    @Test
    public void testNewUser() throws DuplicatedDataException, NoSuchAlgorithmException, UnsupportedEncodingException{
        User user = controller.newUser("Almir Neto", "123456", "123456789", "teste@gmail.com");
        Assert.assertEquals("123456789", user.getCpf());
    }
    
    @Test (expected = DuplicatedDataException.class)
    public void testDuplicatedUsers() throws DuplicatedDataException, NoSuchAlgorithmException, UnsupportedEncodingException{
        controller.newUser("Almir Neto", "123456", "123456789", "teste@gmail.com");
       
        controller.newUser("Luciano Araujo", "12345", "123456789", "teste@gmail.com");
    }
    @Test
    public void testRegisterOfTwoDifferentUsers() throws DuplicatedDataException, NoSuchAlgorithmException, UnsupportedEncodingException{
        controller.newUser("Almir Neto", "123456", "123456789", "teste@gmail.com");
        
        controller.newUser("Luciano Araujo", "12345", "987654321", "teste@gmail.com");
        
    }
    
    @Test
    public void testLogin() throws DuplicatedDataException, NotFoundException, NoSuchAlgorithmException, UnsupportedEncodingException{
        controller.newUser("Almir Neto", "123456", "123456789", "teste@gmail.com");
        
        User test = controller.doLogin("123456789", "123456");
        
        Assert.assertEquals("Almir Neto", test.getName());
        
    }
    
    @Test
    public void testAddCity() throws DuplicateEntryException, InexistentEntryException, NotFoundException, InvalidSpotException{
        capela = controller.addCity("Capela", 22.2, 12.1, 300, "1");
        novaFatima = controller.addCity("Nova Fátima", 22.2, 12.1, 301, "1");
        gaviao =  controller.addCity("Gavião", 22.2, 12.1, 310, "1");
        saoJose = controller.addCity("São José", 22.2, 12.1, 350, "1");
        riachao = controller.addCity("Riachão", 22.2, 12.1, 400, "1");
        capimGrosso = controller.addCity("Capim Grosso", 22.2, 12.1, 402, "1");
        itatiaia = controller.addCity("Itatiaia", 22.2, 12.1, 500, "1");
        
        Assert.assertEquals("CAPELA", controller.searchCity(300).getName());
        Assert.assertEquals("NOVA FÁTIMA", controller.searchCity(301).getName());
        Assert.assertEquals("GAVIÃO", controller.searchCity(310).getName());
        Assert.assertEquals("SÃO JOSÉ",controller.searchCity(350).getName());
        Assert.assertEquals("RIACHÃO", controller.searchCity(400).getName());
        Assert.assertEquals("CAPIM GROSSO", controller.searchCity(402).getName());
        Assert.assertEquals("ITATIAIA", controller.searchCity(500).getName());
    }
    
    @Test
    public void testAddIntersection() throws DuplicateEntryException, InexistentEntryException{
        Intersection intersection  = controller.addIntersection(TypeIntersection.ROTULA, 33.2, 23.2, 10);
        
        Assert.assertEquals(TypeIntersection.ROTULA, controller.searchIntersection(10).getType());
    }
    
    @Test(expected = DuplicateEntryException.class)
    public void testAddDuplicatedCity() throws DuplicateEntryException{
        capela = controller.addCity("Capela", 22.2, 12.1, 300, "1");
        
        capimGrosso = controller.addCity("Capim Grosso", 22.2, 12.1, 300, "1");
    }
    
    @Test
    public void testAddEatPoint() throws DuplicateEntryException, InexistentEntryException{
        capela = controller.addCity("Capela", 22.2, 12.1, 300, "1");
        
        controller.addEatPoint(300, "Módulo 8", "Próximo a UEFS", Rate.BOM);
    
    }
    
    @Test
    public void testIfTheEatPointIsRegistered() throws InexistentEntryException, DuplicateEntryException{
        capela = controller.addCity("Capela", 22.2, 12.1, 300, "1");
        
        controller.addEatPoint(300, "Módulo 8", "Próximo a UEFS", Rate.BOM);
        
        EatPoint ep = capela.getPlaceEat().get(0);
        
        Assert.assertEquals("MÓDULO 8", ep.getName());
    }
    
    @Test (expected = InexistentEntryException.class)
    public void testRegisterAnEatPointIntoACityThatDoesntExist() throws InexistentEntryException{
        
        controller.addEatPoint(300, "Módulo 8", "Próximo a UEFS", Rate.BOM);
    }
    
    @Test
    public void testAddRoadBetweenTwoCitiesOrIntersections() throws DuplicateEntryException, InexistentEntryException, AlreadyHasAdjacency, InexistentVertexException, LoopIsNotAllowedException{
        capela = controller.addCity("Capela", 22.2, 12.1, 300, "1");
        gaviao =  controller.addCity("Gavião", 22.2, 12.1, 310, "1");
        inter = controller.addIntersection(TypeIntersection.ROTULA, 34.3, 23.2, 10);
        
        capela = controller.searchCity(300);
        gaviao = controller.searchCity(310);
        inter = controller.searchIntersection(10);
        
        controller.addRoad(capela, gaviao, 50);
        controller.addRoad(inter, capela, 30.0);
        controller.addRoad(inter, gaviao, 40.8);
    }
    
    @Test (expected = LoopIsNotAllowedException.class)
    public void testAddRoadBetweenTheSameCity() throws DuplicateEntryException, InexistentEntryException, AlreadyHasAdjacency, InexistentVertexException, LoopIsNotAllowedException{
        
        capela = controller.addCity("Capela", 22.2, 12.1, 300, "1");
        City cityA = controller.searchCity(300);
        
        controller.addRoad(cityA, cityA, 50);
    }
    
    @Test (expected = InexistentEntryException.class)
    public void testAddRoadBetweenACityThatDoesntExist() throws InexistentEntryException, DuplicateEntryException, AlreadyHasAdjacency, LoopIsNotAllowedException, InexistentVertexException{
        
        capela = controller.addCity("Capela", 22.2, 12.1, 300, "1");
        City cityA = controller.searchCity(12);
        City cityB = controller.searchCity(300);
        controller.addRoad(cityA, cityB, 50);
    }
    
    @Test (expected = AlreadyHasAdjacency.class)
    public void testAddRoadBetweenTwoCitiesThatHaveAlreadyAdjacency() throws InexistentEntryException, DuplicateEntryException, AlreadyHasAdjacency, InexistentVertexException, LoopIsNotAllowedException{
        
        capela = controller.addCity("Capela", 22.2, 12.1, 300, "1");
        
        gaviao =  controller.addCity("Gavião", 22.2, 12.1, 310, "1");
        
        City cityA = controller.searchCity(310);
        City cityB = controller.searchCity(300);
        
        controller.addRoad(cityA, cityB, 50);
        
        controller.addRoad(cityA, cityB, 150);
    }
    
    @Test
    public void testStartATrip() throws DuplicatedDataException, NoSuchAlgorithmException, UnsupportedEncodingException, DuplicateEntryException, NotFoundException{
        controller.newUser("Almir Neto", "123456", "123456789", "teste@gmail.com");
        
        
        
        controller.startTrip("123456789", "Rolê pra Cabuçu", date);
    }
    
    @Test (expected = DuplicateEntryException.class)
    public void testStartATripWithTheSameName() throws DuplicateEntryException, DuplicatedDataException, NoSuchAlgorithmException, UnsupportedEncodingException, NotFoundException{
        controller.newUser("Almir Neto", "123456", "123456789", "teste@gmail.com");
        
        controller.startTrip("123456789", "Rolê pra Cabuçu", date);
        
        controller.startTrip("123456789", "Rolê pra Cabuçu",date);
        
    }
    
    @Test (expected = InexistentEntryException.class)
    public void testInsertSpotsInexistentsInTheTrip() throws DuplicateEntryException, DuplicatedDataException, NoSuchAlgorithmException, UnsupportedEncodingException, NotFoundException, InexistentEntryException{
        controller.newUser("Almir Neto", "123456", "123456789", "teste@gmail.com");
        
        controller.startTrip("123456789", "Rolê pra Cabuçu", date);
        Calendar data = Calendar.getInstance();
        
        controller.insertCityInTrip("123456789", "Rolê pra Cabuçu",data , data, 300);
    }
    
    @Test 
    public void testInsertSpotsInTheTripWithSucess() throws DuplicateEntryException, DuplicatedDataException, NoSuchAlgorithmException, UnsupportedEncodingException, NotFoundException, InexistentEntryException{
        controller.newUser("Almir Neto", "123456", "123456789", "teste@gmail.com");
        
        controller.startTrip("123456789", "Rolê pra Cabuçu", date);
        Calendar data = Calendar.getInstance();
        
        capela = controller.addCity("Capela", 22.2, 12.1, 300, "1");
        
        City city = controller.insertCityInTrip("123456789", "Rolê pra Cabuçu",data , data, 300);
        
        Assert.assertEquals(capela, city);
    }
    
  
    @Test
    public void shortestPath() throws DuplicateEntryException, DuplicatedDataException, NoSuchAlgorithmException, UnsupportedEncodingException, NotFoundException, AlreadyHasAdjacency, InexistentVertexException, LoopIsNotAllowedException, InexistentEntryException, InsufficientSpotsException, TheresNoEntryException, NoWaysException{
        
        capela = controller.addCity("Capela", 22.2, 12.1, 300, "1");
        novaFatima = controller.addCity("Nova Fátima", 22.2, 12.1, 301, "1");
        gaviao =  controller.addCity("Gavião", 22.2, 12.1, 310, "1");
        saoJose = controller.addCity("São José", 22.2, 12.1, 350, "1");
        riachao = controller.addCity("Riachão", 22.2, 12.1, 400, "1");
        capimGrosso = controller.addCity("Capim Grosso", 22.2, 12.1, 402, "1");
        itatiaia = controller.addCity("Itatiaia", 22.2, 12.1, 500, "1");

        gaviao = controller.searchCity(310);
        capela = controller.searchCity(300);
        capimGrosso = controller.searchCity(402);
        itatiaia = controller.searchCity(500);
        novaFatima = controller.searchCity(301);
        
        controller.addRoad(novaFatima, capela, 20.0);
        controller.addRoad(gaviao, novaFatima, 30.0);
        controller.addRoad(gaviao, capela, 50.5);
        controller.addRoad(capimGrosso, gaviao, 34.2);
        controller.addRoad(capimGrosso, itatiaia, 400.4);
        
        controller.newUser("Almir Neto", "123456", "123456789", "teste@gmail.com");
        
        controller.startTrip("123456789", "Rolê pra Cabuçu", date);
        
        Calendar data = Calendar.getInstance();
        
        City city = controller.insertCityInTrip("123456789", "Rolê pra Cabuçu",data , data, 310);       
        Assert.assertEquals(gaviao, city);
        city = controller.insertCityInTrip("123456789", "Rolê pra Cabuçu",data , data, 300);
        Assert.assertEquals(capela, city);
        city = controller.insertCityInTrip("123456789", "Rolê pra Cabuçu",data , data, 402);
        city = controller.insertCityInTrip("123456789", "Rolê pra Cabuçu",data , data, 500);

        Iterator it = controller.shortestPath("123456789", "Rolê pra Cabuçu");
        
        EntryDjikstra obj = (EntryDjikstra) it.next();
        Assert.assertEquals("GAVIÃO", ((City)((Vertex) obj.getCur()).getVertex()).getName());
     
        obj = (EntryDjikstra) it.next();
        Assert.assertEquals("NOVA FÁTIMA",((City)((Vertex) obj.getCur()).getVertex()).getName());
      
        obj = (EntryDjikstra) it.next();
        Assert.assertEquals("CAPELA", ((City)((Vertex) obj.getCur()).getVertex()).getName());
        Assert.assertEquals(50.0,obj.getDistance()); 

        obj = (EntryDjikstra) it.next();
        Assert.assertEquals("NOVA FÁTIMA", ((City)((Vertex) obj.getCur()).getVertex()).getName());
       
        obj = (EntryDjikstra) it.next();
        Assert.assertEquals("GAVIÃO",  ((City)((Vertex) obj.getCur()).getVertex()).getName());
       
        obj = (EntryDjikstra) it.next();
        Assert.assertEquals("CAPIM GROSSO", ((City)((Vertex) obj.getCur()).getVertex()).getName());
        Assert.assertEquals(84.2,obj.getDistance());
        
        obj = (EntryDjikstra) it.next();
        Assert.assertEquals("ITATIAIA", ((City)((Vertex) obj.getCur()).getVertex()).getName());
        Assert.assertEquals(400.4,obj.getDistance());
    }
}