/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.utils.geom;

import java.util.Comparator;

/**
 *
 * @param <T> 
 * @author epsilon
 */
public interface CenterDistanceComparator <T extends Coordinate> extends Comparator<T>{
    void setCenter(Coordinate center);
}
