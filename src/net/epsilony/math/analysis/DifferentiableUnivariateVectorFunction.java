/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.epsilony.math.analysis;

/**
 *
 * @author epsilon
 */
public interface DifferentiableUnivariateVectorFunction extends UnivariateVectorFunction{
    UnivariateVectorFunction vectorDerivative();
}
