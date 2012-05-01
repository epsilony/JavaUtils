/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.utils;

import java.util.List;

/**
 *
 * @param <T> 
 * @param <V> 
 * @author epsilonyuan@gmail.com
 */
public interface CenterDistanceSearcher<T,V> {
    List<V> search(T center,double radius,List<V> results);
}
