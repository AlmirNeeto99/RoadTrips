
package Util;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author AlmirNeto
 */
public class Queue implements IQueue, Serializable{
    
    private Node head; 
    private Node tail; 
    private int size; 

    /**
     * Method that verifies if the queue is empty.
     * @return True if the queue is empty.
     */
    @Override
    public boolean isEmpty() {
        return head == null;
    }

    
    /**
     * Method that verifies the size of the queue.
     * @return The size of the queue.
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Method the insert a Object in the end of queue.
     * @param o The Object to be insert.
    */

    @Override
    public void put(Object o) {
        Node node = new Node(o);
        if(isEmpty()){
            head = node;
            tail = node;
            size++;
        }
        else{
            tail.setNext(node);
            tail = node;
        }
    }

    /**
     * Method that removes a Object of the queue.
     * @return The Object removed.
    */
    @Override
    public Object poll() {
        Object ret = null;
        if(isEmpty()){
            return null;
        }
        else{
            ret = head.getData();
            head = head.getNext();
        }
        return ret;
    }

     /**
     * Method that returns the first element of the queue, but no removes.
     * @return The first element of the queue.
     */
    @Override
    public Object peek() {
        if(!isEmpty()){
            return head;
        }
        return null;
    }

     /**
     * Method that verifies if an Object is in the queue.
     * @param obj The Object to be analyzed.
     * @return True if the Object is in the Queue.
     */
    public boolean contains(Object obj){
        if(!isEmpty()){
            Node search = head;
            while(search != null){
                if(search.getData().equals(obj)){
                    return true;
                }
                search = search.getNext();
            }
        }
        return false; 
    }
    
     /**
     * Method that returns an Iterator of the queue.
     * @return An Iterator of the queue.
     */
    public Iterator iterator(){
        return new MyIt();
    }
    
    private class MyIt implements Iterator{
        
        private Node actual = head;
        
        @Override
        public boolean hasNext() {
            return actual != null;
        }

        @Override
        public Object next() {
            Object ret = actual.getData();
            actual = actual.getNext();
            return ret;
        }   
    }
    
    public LinkedList toList(){
        LinkedList list = new LinkedList();
        for(Node aux = head; aux != null; aux = aux.getNext()){
            list.add(aux.getData());
        }
        return list;
    }

    
    
    private class Node implements Serializable{

        private Object data; 
        private Node next; 

    Node(Object novo){
        this.data = novo;
        this.next = null;
    }
        public Object getData() {
            return data; 
        }

        public void setData(Object data) {
            this.data = data; 
        }

        public Node getNext() {
            return next; 
        }

        public void setNext(Node next) {
            this.next = next; 
        }
    
    }
}
