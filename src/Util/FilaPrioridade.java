
package Util;

import java.io.Serializable;
import java.util.*;

/**
 *
 * @author Jaevillen
 */
public class FilaPrioridade implements IPriorityQueue, Serializable{
    private Comparable[] data;
    private int size;

    /**
     * Constructor of the class.
     * 
     * @param tamanhoInicial The Initial lenght of the queue.
     */
    public FilaPrioridade(int tamanhoInicial){
        data = new Comparable[tamanhoInicial];
    }

    
    /**
     * Method that verifies if the queue is empty.
     * @return Tru if the queue is empty.
     */
    @Override
    public boolean isEmpty() {
       return this.size == 0;
    }

    /**
     * Method that verifies the size of the queue.
     * @return
     */
    @Override
    public int size() {
        return size;
    }
        
    private void swap(int parent, int child){
        Comparable temp = data[parent];
        data [parent] = data[child];
        data[child] = temp;
    }
    
    /**
     *Method that add a Comparable in the queue. 
     * 
     * @param obj The Comparable.
     */
    @Override
    public void add(Comparable obj) {
         if(size == data.length){
           Comparable[] temp = new Comparable[this.size*2];
           System.arraycopy(data, 0, temp, 0, this.size);
           data = temp; 
        }
        data[size] = obj;
        int parent = (this.size - 1)/2;
        int child = this.size;
        while(child > 0 && data[parent].compareTo(data[child]) > 0){
            swap(parent, child);
            child = parent;
            parent = (parent - 1)/2;
        }
        size++;
    }
    
    /**
     * Method that takes the first element of the queue.
     * @return The smaller element of the queue.
     */
    @Override
    public Comparable peek() {
        if(this.isEmpty()){
            return null;
        }
        return data[0];
    }

    private int min(int i, int j){
        if(i < size && j < size){
            return data[i].compareTo(data[j]) < 0 ? i : j;
        }
        else if(i < size){
            return i;
        }
        else if(j < size){
            return j;
        }
        return data.length;
    }
    
     /**
     * Method that removes the element that is in the beginning of the queue.
     * @return The element removed.
     */
    @Override
    public Comparable remove() {
        Comparable ret = data[0];
        data[0] = data[size - 1];
        size--;
        
        
        int parent = 0;
        int child = min(parent * 2 + 1, parent * 2 + 2);
        while(child < size && data[child].compareTo(data[parent]) < 0){
            swap(parent, child);
            parent = child;
            child = min(parent * 2 + 1, parent * 2 + 2);
        }
        return ret;
    }
    
    /**
     * Method that returns an Iterator of the class.
     * @return One Iterator of the class.
    */

    public Iterator iterator(){
        return new ArrayIterator();
    }
    private class ArrayIterator implements Iterator{
        private int cur;
        
        @Override
        public boolean hasNext() {
            return data[cur] != null;
        }

        @Override
        public Comparable next() {
            Comparable ret = data[cur];
            cur++;
            return ret;
        }
    }
}
