/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.math.analysis.multigaussquadrature;
import net.epsilony.math.analysis.MultivariateRealFunction;
import net.epsilony.math.util.IntervalGenerator;
import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MathException;
/**
 *
 * @author li
 */
public interface MultiGaussQuadrature {

    public void setGrids(double[] x, double[] y);

    public double[][] getDomain(double[] lowleft, double[] upperright);

    public double multiGaussQuadrature(MultivariateRealFunction fun, IntervalGenerator interval) throws FunctionEvaluationException, ConvergenceException, MathException;

    public int choosePoints(double[][] Interval, double PointsInGrids);
}
