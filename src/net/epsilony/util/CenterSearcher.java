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
public interface CenterSearcher<T,V> {
    List<V> search(T center,List<V> results);
}
