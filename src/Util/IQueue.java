/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

public interface IQueue {
  
    public boolean isEmpty();

    public int size();

    public void put(Object o);

    public Object poll();

    public Object peek();        

}