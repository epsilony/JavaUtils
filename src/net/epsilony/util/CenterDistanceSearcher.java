/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.util;

import java.util.List;

/**
 *
 * @author epsilonyuan@gmail.com
 */
public interface CenterDistanceSearcher<T,V> {
    List<V> search(T center,double dist,List<V> results);
}
