/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.utils.math;

import net.epsilony.utils.PartDiffOrdered;
import net.epsilony.utils.geom.Coordinate;

/**
 *
 * @author epsilon
 */
public interface CoordinatePartDiffFunction extends PartDiffOrdered{
    double[] values(Coordinate coord,double[] results);
}
