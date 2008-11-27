/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.epsilony.math.polynomial;

import java.util.Arrays;
import net.epsilony.math.analysis.DifferentiableUnivariateVectorFunction;
import net.epsilony.math.analysis.UnivariateVectorFunction;
import org.apache.commons.math.FunctionEvaluationException;

/**
 *
 * @author epsilon
 */
public class LinePolynomial implements DifferentiableUnivariateVectorFunction{

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
    public double[] values(double input, double[] results) throws FunctionEvaluationException {
        for(int i=0;i<dimension;i++){
            results[i]=pts[0][i]+input*(pts[1][i]-pts[0][i]);
        }
        return results;
    }

    @Override
    public double[] values(double input) throws FunctionEvaluationException {
        double [] results=new double[dimension];
        return values(input,results);
    }

    @Override
    public UnivariateVectorFunction vectorDerivative() {
        return new UnivariateVectorFunction() {
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
            public double[] values(double input, double[] results) throws FunctionEvaluationException {
                for(int i=0;i<dim;i++){
                    results[i]=diffs[i];
                }
                return results;
            }

            @Override
            public double[] values(double input) throws FunctionEvaluationException {
                return Arrays.copyOf(diffs, dim);
            }
        };
    }

}
