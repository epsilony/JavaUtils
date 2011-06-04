/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.math.analysis;

import org.apache.commons.math.FunctionEvaluationException;

/**
 * 计划用commons Math中的接口代替
 * @deprecated 
 * @author epsilon
 */
@Deprecated
public interface MultivariateRealFunction {

    public int getDimension();

    public double value(double[] x) throws FunctionEvaluationException;
}
