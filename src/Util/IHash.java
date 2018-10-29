
package Util;

import java.io.Serializable;

public interface IHash extends Serializable{
    
    public void put(Object key, Object value);
    
    public Object get(Object key);
    
    public void removeKey(Object key);
    
    public void removeValue(Object value);
    
    public boolean isEmpty();
    
    public int size(); 
}
