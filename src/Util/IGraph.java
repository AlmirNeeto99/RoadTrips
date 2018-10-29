
package Util;

import Model.Edge;
import Exceptions.*;
import java.util.Iterator;

public interface IGraph{
    
    public void addVertex(Object key)throws DuplicateEntryException;
    public Iterator vertex();
    public int numVertex();
    public void removeVertex(Object key) throws EmptyHashException, InexistentEntryException, DontHaveAdjacenciesException;
    
    public void addEdge(Object vertexA, Object vertexB, Object data) throws InexistentEntryException, DuplicateEntryException, AlreadyHasAdjacency, InexistentVertexException, LoopIsNotAllowedException;
    public Edge getEdge(Object vertexA, Object vertexB) throws InexistentEntryException;
    public Iterator edges();
    public int numEdges();
    public void removeEdge(Edge removed) throws EmptyHashException, InexistentEntryException;
    
    public Iterator outGoingEdges(Object vertex);
    public int outDegree(Object vertex);
    
    public Iterator incomingEdges(Object vertex);
    public int inDegree(Object vertex);
}
