/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.math.analysis;

import org.apache.commons.math.FunctionEvaluationException;

/**
 *
 * @author epsilon
 */
public interface MultivariateRealFunction {

    public int getDimension();

    public double value(double[] x) throws FunctionEvaluationException;
}
