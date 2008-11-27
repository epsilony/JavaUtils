/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.math.analysis;

import org.apache.commons.math.FunctionEvaluationException;

/**
 *
 * @author li
 */
public interface UnivariateVectorFunction {

    public int getDimension();
    public double[] values(double input,double[] results) throws FunctionEvaluationException;
    public double[] values(double input) throws FunctionEvaluationException;
}
