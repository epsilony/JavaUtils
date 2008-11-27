/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.math.polynomial;
import org.apache.commons.math.FunctionEvaluationException;
/**
 *
 * @author li
 */
public interface BivariateRealFunction {

    public double value(double x, double y)throws FunctionEvaluationException;
}
