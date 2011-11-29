/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.epsilony.math.polynomial;

import java.util.Arrays;
import net.epsilony.math.analysis.DifferentiableUnivariateVectorialFunctionEx;
import net.epsilony.math.analysis.UnivariateVectorialFunctionEx;
import org.apache.commons.math.FunctionEvaluationException;

/**
 *
 * @author epsilonyuan@gmail.com
 */
public class LinePolynomial implements DifferentiableUnivariateVectorialFunctionEx{

    private int dimension;
    double[][] pts;
    @Override
    public int getDimension() {
        return dimension;
    }

    /**
     * 
     * @param pts 直线的起点与终点{{x1,y1,z1,...},{x2,y2,z2,...}}
     */
    public LinePolynomial(double[][] pts) {
        dimension=pts[0].length;
        this.pts = pts;
    }

    @Override
    public double[] value(double input, double[] results) throws FunctionEvaluationException {
        for(int i=0;i<dimension;i++){
            results[i]=pts[0][i]+input*(pts[1][i]-pts[0][i]);
        }
        return results;
    }

    @Override
    public double[] value(double input) throws FunctionEvaluationException {
        double [] results=new double[dimension];
        return value(input,results);
    }

    @Override
    public UnivariateVectorialFunctionEx vectorDerivative() {
        return new UnivariateVectorialFunctionEx() {
            int dim=dimension;
            double[] diffs=new double[dim];
            {
                for(int i=0;i<dim;i++){
                    diffs[i]=pts[1][i]-pts[0][i];
                }
            }
            @Override
            public int getDimension() {
                return dim;
            }

            @Override
            public double[] value(double input, double[] results) throws FunctionEvaluationException {
                System.arraycopy(diffs, 0, results, 0, dim);
                return results;
            }

            @Override
            public double[] value(double input) throws FunctionEvaluationException {
                return Arrays.copyOf(diffs, dim);
            }
        };
    }

}
