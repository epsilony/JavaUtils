/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.utils.math.polynomial;
import org.apache.commons.math.FunctionEvaluationException;
/**
 *
 * @author li
 */
public interface BivariateRealFunction {

    /**
     * @param x
     * @param y
     * @return
     * @throws org.apache.commons.math.FunctionEvaluationException
     */
    public double value(double x, double y);
}
