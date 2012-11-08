/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.spfun.radialbasis;

import net.epsilony.utils.PartDiffOrdered;

/**
 *
 * @author epsilon
 */
public interface RadialFunctionCore extends PartDiffOrdered{
    double[] valuesByNormalisedDistSq(double distSq,double[] results);
}
