/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.math.analysis;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateVectorialFunction;

/**
 *
 * @author li
 */
public interface UnivariateVectorialFunctionEx extends UnivariateVectorialFunction{

    int getDimension();
    double[] value(double input,double[] results) throws FunctionEvaluationException;
}
