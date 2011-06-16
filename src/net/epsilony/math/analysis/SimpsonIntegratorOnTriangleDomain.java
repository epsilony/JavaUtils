/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.math.analysis;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.BivariateRealFunction;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.integration.SimpsonIntegrator;

/**
 *
 * @author epsilon
 */
public class SimpsonIntegratorOnTriangleDomain {

    public SimpsonIntegrator integrator = new SimpsonIntegrator();

    public double integrate(double x1, double y1, double x2, double y2, double x3, double y3, BivariateRealFunction function) throws MaxIterationsExceededException, FunctionEvaluationException {
        double result1 = integrate(x1, y1, x3, y3, function);
        double result2 = integrate(x3, y3, x2, y2, function);
        double result3 = integrate(x1, y1, x2, y2, function);


        return result1 + result2 - result3;
    }

    double integrate(final double lowx, final double lowy, final double upx, final double upy, final BivariateRealFunction function) throws MaxIterationsExceededException, FunctionEvaluationException {
        double scale, min, max;
        if (lowx < upx) {
            min = lowx;
            max = upx;
            scale = 1;
        } else {
            min = upx;
            max = lowx;
            scale = -1;
        }
        double result = integrator.integrate(new UnivariateRealFunction() {

            @Override
            public double value(final double x) throws FunctionEvaluationException {
                double min = 0, max = (x - lowx) / (upx - lowx) * (upy - lowy) + lowy;
                double scale = 1;
                if (min > max) {
                    min = max;
                    max = 0;
                    scale = -1;
                }
                if(min==max){
                    return 0;
                }
                double result = 0;
                try {
                    result = integrator.integrate(new UnivariateRealFunction() {

                        @Override
                        public double value(double y) throws FunctionEvaluationException {
                            return function.value(x, y);
                        }
                    }, min, max);

                } catch (MaxIterationsExceededException ex) {
                    Logger.getLogger(SimpsonIntegratorOnTriangleDomain.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(SimpsonIntegratorOnTriangleDomain.class.getName()).log(Level.SEVERE, null, ex);
                }
                return result *= scale;
            }
        }, min, max);
        result *= scale;
        return result;
    }
}
