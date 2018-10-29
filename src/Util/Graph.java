
package Util;

import Exceptions.TheresNoEntryException;
import Model.*;
import Exceptions.*;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;

/**
 *
 * @author Almir and Jaevillen 
 */
public class Graph implements IGraph, Serializable{
    
    private int numVertex;
    private int numEdges;
    private HashSet vertex;
    private HashSet edges;
    

    /**
     * The Constructor of the class.
     */
    public Graph(){
        vertex = new HashSet();  
        edges = new HashSet();
    }


     /**
     * Method that add a Vertex in the Graph.
     * @param key The Key of the Object to be insert.
     * @throws DuplicateEntryException If there's already an Object with the Key informed.
     */
    @Override
    public void addVertex(Object key) throws DuplicateEntryException{
        Vertex newVertex = new Vertex(key);
        vertex.put(newVertex);
        numVertex++;
    }


    /**
     * Method that returns an Iterator for the HashSet that organizes the vertices.
     * @return An Iteterator.
     */
    @Override
    public Iterator vertex() {
        Iterator it;
        try{
            it = vertex.iterator();
        } catch (ThereNoKeysException ex) {
            return null;
        }
        return it;
    }

    /**
     * Method that returns the number of vertices in the Graph.
     * @return The number of vertices in the Graph.
     */
    @Override
    public int numVertex() {
        return numVertex;
    }
    

    /**
     * Method that removes vertices that are in the Graph.
     * @param key The Key of the vertex to be removed.
     * @throws EmptyHashException If the HashSet is empty.
     * @throws InexistentEntryException If there's no vertex with the key informed in the HashSet.
     */
    @Override
    public void removeVertex(Object key) throws EmptyHashException, InexistentEntryException{
        Vertex verify = new Vertex(key);
        
        Vertex searched = (Vertex)vertex.get(verify);
        
        HashMap verifyMap = searched.getAdjacency();
        
        Entry[] entries = verifyMap.toArray();
        
        for(int i = 0; i < verifyMap.size(); i++){
            if(entries[i] == null){
            }
            else if(entries[i].getKey() == null && entries[i].getValue() == null){
            } 
            else{
                Vertex point = (Vertex)entries[i].getKey();
                HashMap map = point.getAdjacency();
                
                Edge removed = this.getEdge(searched, point);
                edges.remove(removed);
                try {
                    map.remove(searched);
                } catch (InexistentEntryException ex) {
                    //If enter here, it means that another Vertex doesn't have the Vertex passed by Parameter.
                }
            }
        }
        vertex.remove(searched);
    }


    /**
     * Method that add an Edge between two vertices.
     * @param vertexA The first Vertex.
     * @param vertexB The second Vertex.
     * @param data The weight of the Edge.
     * @throws DuplicateEntryException 
     * @throws AlreadyHasAdjacency If there's already an Edge between the vertices informed.
     * @throws InexistentVertexException If some of two vertices doesn't exist in the Graph. 
     * @throws LoopIsNotAllowedException If the two vertices are the same.
     */
    @Override
    public void addEdge(Object vertexA, Object vertexB, Object data) throws DuplicateEntryException, AlreadyHasAdjacency, InexistentVertexException, LoopIsNotAllowedException{
        
        Vertex Avertex = new Vertex(vertexA);
        Vertex Bvertex = new Vertex(vertexB);
        
        Vertex verify = null;
        Vertex verifyOther = null;
        
        try{
            verify = (Vertex)vertex.get(Avertex);
        
            verifyOther = (Vertex) vertex.get(Bvertex);
        }
        catch(InexistentEntryException iex){
            throw new InexistentVertexException();
        }
        
        Edge edge = new Edge(verify, verifyOther, data);
        
        if(Avertex.equals(Bvertex)){
            throw new LoopIsNotAllowedException();
        }
        try{
            vertex.contains(Avertex);
            vertex.contains(Bvertex);
        }
        catch(InexistentEntryException iex){
            throw new InexistentVertexException();
        }
        
        try{
            verify.getAdjacency().contains(verifyOther);
            throw new AlreadyHasAdjacency();
        }
        catch(InexistentEntryException iex){
            verify.getAdjacency().put(verifyOther, edge);
        }
        
        try{
            verifyOther.getAdjacency().contains(verify);
            throw new AlreadyHasAdjacency();
        }
        catch(InexistentEntryException iex){
            verifyOther.getAdjacency().put(verify, edge);   
        }
        try{
            edges.contains(edge);
            throw new DuplicateEntryException();
        }
        catch(InexistentEntryException iex){
            edges.put(edge);
        }
    }


    /**
     * Method that search an Edge.
     * @param vertexA One of the two vertices.
     * @param vertexB Another of the two vertices.
     * @return The Edge found.
     * @throws InexistentEntryException If there's no Edge between the two vertices informed.
     */
    @Override
    public Edge getEdge(Object vertexA, Object vertexB) throws InexistentEntryException{
        Vertex a = new Vertex(vertexA);
        Vertex b = new Vertex(vertexB);
        Edge edge = new Edge(a, b, null);
        
        if(edges.contains(edge)){
            Edge ret = (Edge)edges.get(edge);
            return ret;
        }
        throw new InexistentEntryException();
    }
    

    /**
     * Method that returns a Key of a Entry of the HashSet with the vertices.
     * 
     * @param key The Key of the Vertex searched.
     * @return The Key found.
     * @throws InexistentEntryException If there's no Vertex in the HashSet of Vertices with the Key informed.
     */
    public Object getKey(Object key) throws InexistentEntryException{
       return ((Vertex) this.getVertex(key)).getVertex();  
    }
  
    /**
     * Method that returns a Vertex of the HashSet with the vertices.
     * 
     * @param key The key of the Vertex searched.
     * @return The Vertex found.
     * @throws InexistentEntryException If there's no Vertex in the HashSet with the key informed.
     */
    public Object getVertex(Object key) throws InexistentEntryException{
        Vertex searchVertex = new Vertex(key);
        
        Vertex ret = (Vertex) vertex.get(searchVertex);
        
       return ret;  
    }

     /**
     * Method that returns an Iterator for the HashSet that contains all edges registered.
     * @return An Iterator.
     */
    @Override
    public Iterator edges() {
        Iterator it;
        
        try{
            it = edges.iterator();
        } catch (ThereNoKeysException ex) {
            return null;
        }
        return it;
    }

      /**
     * The number of edges that are registered in the Graph.
     * @return The number of edges the Graph.
     */
    @Override
    public int numEdges() {
        return numEdges;
    }

      /**
     * Method that removes an Edge.
     * @param removed The edge to be removed.
     * @throws EmptyHashException If the HashSet with the Edges is empty.
     * @throws InexistentEntryException If the Edge informed doesn't exist.
     */
    @Override
    public void removeEdge(Edge removed) throws EmptyHashException, InexistentEntryException{
        Vertex oneVertex = removed.getA();
        Vertex otherVertex = removed.getB();
        
        oneVertex.getAdjacency().remove(otherVertex);
        
        otherVertex.getAdjacency().remove(oneVertex);
        
        edges.remove(removed);
    }
    

    /**
     * Method that verifies the shortest paths between the vertices of the Graph.
     * @param keyStart The Key of the first vertex to be analyzed.
     * @return A HashMap with the shortest paths.
     * @throws InexistentEntryException If there's no Vertex with the Key informed.
     * @throws DuplicateEntryException
     */
    public HashMap shortestPath(Object keyStart) throws InexistentEntryException, DuplicateEntryException, TheresNoEntryException{
        
        HashMap shortestPath = new HashMap();
        FilaPrioridade queue = new FilaPrioridade(10);
        
        Vertex v = (Vertex) this.getVertex(keyStart);
        queue.add(new EntryDjikstra(v, null, 0));
        
        while(!queue.isEmpty()){
            
            EntryDjikstra e = (EntryDjikstra) queue.remove();
            v = (Vertex) e.getCur();
            
            try{
                if(!shortestPath.contains(v)){
                    shortestPath.put(v, e);
                }
            }catch(InexistentEntryException ex){
                shortestPath.put(v, e);
            }
            
            Iterator adjacencies = v.getAdjacency().iterator();
            while(adjacencies.hasNext()){
                
                Entry vertEdge = (Entry) adjacencies.next();
                if(vertEdge != null){
                    Vertex u = (Vertex) vertEdge.getKey();

                    double dist = (double) ((Edge) vertEdge.getValue()).getWeight();
                    dist = dist + e.getDistance();
                   
                    try{
                        EntryDjikstra t = (EntryDjikstra) ((Entry) shortestPath.get(u)).getValue();
                        if(dist < t.getDistance()){
                            queue.add(new EntryDjikstra(u, v, dist));
                        }
                    }catch(InexistentEntryException ex){
                        queue.add(new EntryDjikstra(u, v, dist));
                    }
                }
            } 
        }
        return shortestPath;  
    }
    
    /**
     * Method that analyze the shortest route between two points. 
     * @param start The initial point.
     * @param end The ending point.
     * @param paths A HashMap with the shortest paths of the Graph.
     * @return A Stack with the points of the shortest route.
     * @throws InexistentEntryException
     * @throws Exceptions.NoWaysException
     */
    public Stack <EntryDjikstra> path (Object start, Object end, HashMap paths) throws InexistentEntryException, NoWaysException{
        
        Stack <EntryDjikstra> stack = new Stack();
        
        Vertex s = (Vertex) this.getVertex(start);
        Vertex e = (Vertex) this.getVertex(end);
        
        while(!s.equals(e)){
            try{
                stack.push((EntryDjikstra) ((Entry) paths.get(e)).getValue());
            }catch(InexistentEntryException ex){
               throw new NoWaysException();
            }
            
            e = stack.peek().getPrev();
        }
        stack.push((EntryDjikstra) ((Entry) paths.get(e)).getValue());
        return stack;
    } 

    @Override
    public Iterator outGoingEdges(Object vertex) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    @Override
    public int outDegree(Object vertex) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

 
    @Override
    public Iterator incomingEdges(Object vertex) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

   
    @Override
    public int inDegree(Object vertex) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public LinkedList getAllVertex() throws ThereNoKeysException{
        return vertex.toList();
    }
    
    public LinkedList getAllEdges() throws ThereNoKeysException{
        return edges.toList();
    }
}
