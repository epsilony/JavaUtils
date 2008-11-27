/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.math.analysis;

import org.apache.commons.math.MathException;

/**
 *
 * @author epsilon
 */
public interface GaussQuadrature {

    public double[] GetGaussQuadraturePoints(int nPoints) throws MathException;

    public double[] GetGaussQuadratureCoefs(int nPoints) throws MathException;
}
