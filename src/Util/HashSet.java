
package Util;

import Model.Entry;
import Exceptions.*;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author almir
 */
public class HashSet implements Serializable{
    
    private int size;
    private Entry[] keys;
    private final double LOAD_FACTOR = 0.5;
    private final Entry EMPTY = new Entry(null, null);
    
    public HashSet(){
        keys = new Entry[10];
    }
    
    public HashSet(int size){
        keys = new Entry[size];
    }
    /**
     * 
     * Method that add an Entry in the HashSet.
     * @param key Object that is the key of the Hash.
     * @throws DuplicateEntryException When the Key is already in the Hash.
     */
    public void put(Object key) throws DuplicateEntryException{
        Entry entry = new Entry(key, null);
        int hashCode = Math.abs(key.hashCode() % keys.length);
        int pos = this.searchPosition(hashCode, entry);
        
        if(keys[pos] == null){
            keys[pos] = entry;
            size++;
        }
        else if(keys[pos].equals(EMPTY)){
            keys[pos] = entry;
            size++;
        }
        else if(keys[pos].getKey().equals(key)){
            throw new DuplicateEntryException();
        }
        
        if((size / (double)keys.length) > LOAD_FACTOR){
            resize();
        }
    }
    /**
     * Method that resize the Array.
     * 
     * @throws DuplicateEntryException 
     */
    private void resize() throws DuplicateEntryException{
        Entry[] temp = keys;
        keys = new Entry[keys.length*2];
        for(Entry transf : temp){
            if(transf != null && !transf.equals(EMPTY)){
                put(transf.getKey());
            }
        }  
    }
    /**
     * Method that verifies where a new Entry will be if there's an Entry in its position.
     * @param position The hashCode of the current Entry.
     * @param search The Entry that will be added in the Hash.
     * @throws DuplicateEntryException When the Key is already in the Hash.
     */
    private int searchPosition(int position, Entry search){
        int firstEmpty = -1;
        while (keys [position] != null && !keys[position].equals(search)){
            if (firstEmpty == -1 && keys[position].equals(EMPTY)){
                firstEmpty = position;
            }
            position = (position + 1) % keys.length;
        }
        
        if (keys[position] == null && firstEmpty != -1){
            return firstEmpty;
        }
        else{
            return position;
        } 
    }
    /**
     * Method that removes a Key from the Hash.
     * @param key Key that will be removed from Hash.
     * @throws EmptyHashException
     * @throws InexistentEntryException 
     */
    public void remove(Object key) throws EmptyHashException, InexistentEntryException{
        if(size == 0){
            throw new EmptyHashException();
        }
        int hashCode = key.hashCode() % keys.length;
        hashCode = Math.abs(hashCode);
        if(keys[hashCode] == null){
            throw new InexistentEntryException();
        }
        else if(keys[hashCode].getKey().equals(key)){
            keys[hashCode] = EMPTY;
            size--;
        }
        else{
            searchAndRemove(hashCode, key);
        }
    }
    /**
     * Method that Searches and removes the key from the Hash.
     * @param key Key that will be removed from the Hash.
     */
    private void searchAndRemove(int hashCode, Object key){
        for(int i = hashCode; keys[i] != null; i = (i + 1) % keys.length){
            if(keys[i].equals(EMPTY)){}
            else if(keys[i].getKey().equals(key)){
                keys[i] = EMPTY;
                size--;
            }
        }
    }
    /**
     * Method that verifies if a key it's in the Hash.
     * @param key The Key that will be verified.
     * @return TRUE if the Key is inside the Hash. FALSE, otherwise.
     * @throws InexistentEntryException When the key doesn't exist in Hash.
     */
    public boolean contains(Object key) throws InexistentEntryException{
        int hashCode = Math.abs(key.hashCode() % keys.length);
        System.out.println(hashCode);
        for(int i = hashCode; keys[i] != null; i = (i + 1) % keys.length){
            System.out.println(keys[i]);
            if(keys[i] == null){
                throw new InexistentEntryException();
            }
            if(keys[i].equals(EMPTY)){
            }
            else if(keys[i].getKey().equals(key)){
                return true;
            }
        }
        throw new InexistentEntryException();
    }
    /**
     * Method that gets and return a Key.
     * @param key Key that will be searched in the Hash.
     * @return The key, if exists.
     * @throws InexistentEntryException When the key doesn't exist in Hash.
     */
    public Object get(Object key) throws InexistentEntryException{
        return ((Entry) this.getElement(key)).getKey();
    }
    
     /**
     * Method that gets and return a Entry.
     * @param key Key that will be searched in the Hash.
     * @return The Entry, if exists.
     * @throws InexistentEntryException When the key doesn't exist in Hash.
     */
    private Object getElement(Object key) throws InexistentEntryException{
        int hashCode = Math.abs(key.hashCode() % keys.length);
        for(int i = hashCode; keys[i] != null; i = (i + 1) % keys.length){
            if(keys[i] == null){
                throw new InexistentEntryException();
            }
            else if(keys[i].equals(EMPTY)){
            }
            else if(keys[i].getKey().equals(key)){
                Object ret = keys[i];
                return ret;
            }
        }
        throw new InexistentEntryException(); 
    }
    /**
     * Method that return the size of the Hash.
     * @return The size of the Hash.
     */
    public int size(){
        return size;
    }
    
    public Iterator iterator() throws ThereNoKeysException {
        return toList().iterator();
    }

    /**
     * Method that returns a list with all Keys.
     * @return A List with the Keys.
     * @throws ThereNoKeysException When there's no keys In HashSet.
     */
    public LinkedList toList() throws ThereNoKeysException{
        if(size == 0){
            throw new ThereNoKeysException();
        }
        LinkedList list = new LinkedList();
        for(int i = 0; i < keys.length; i++){
            if(keys[i] != null && !keys[i].equals(EMPTY)){
                list.add(keys[i].getKey());
            }
        }
        return list;
    }
}