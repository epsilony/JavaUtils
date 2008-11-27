/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.epsilony.math.polynomial;

/**
 *
 * @author li
 */
public interface Gaussquadrature {
    public double Integrate();
     public  double[] getGaussLegendreQuadraturePoint();
     public  double[] getGaussLegendreQuadratureCoefficient();
}
