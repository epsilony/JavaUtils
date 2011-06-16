/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.math.analysis;

import java.util.Arrays;
import java.util.Random;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.integration.SimpsonIntegrator;
import org.apache.commons.math.analysis.polynomials.PolynomialFunction;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static net.epsilony.math.analysis.GaussLegendreQuadratureUtils.*;

/**
 *
 * @author epsilon
 */
public class GaussLegendreQuadratureUtilsTest {

    public GaussLegendreQuadratureUtilsTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * Test of quadrate method, of class GaussLegendreQuadratureUtils.
     */
    @Test
    public void testQuadrate() throws Exception {
        for (int i = 0; i < positionsArrays.length; i++) {
            System.out.println("Points positions:");
            System.out.println(i + ": " + Arrays.toString(positionsArrays[i]));
            System.out.println("Points Weights:");
            System.out.println(i + ": " + Arrays.toString(weightsArrays[i]));
        }
        System.out.println("quadrate");
        SimpsonIntegrator simpsonIntegrator = new SimpsonIntegrator();
        System.out.println("Simpson Integrator Relative Accuracy: " + simpsonIntegrator.getRelativeAccuracy());
        System.out.println("Simpson Integrator Absolute Accuracy: " + simpsonIntegrator.getAbsoluteAccuracy());
        double randGap = 100;
        int repeat = 10;
        for (int i = 0; i < repeat; i++) {
            for (int nPt = MINPOINTS; nPt <= MAXPOINTS; nPt++) {
                double[] c = new double[nPt * 2 - 1];
                for (int j = 0; j < nPt * 2 - 1; j++) {
                    c[j] = new Random().nextDouble();
                }
                UnivariateRealFunction fun = new PolynomialFunction(c);
                double low = new Random().nextDouble() * randGap;
                double up = new Random().nextDouble() * randGap;
                double max, min;
                double scale;
                if (low > up) {
                    min = up;
                    max = low;
                    scale=-1;
                } else {
                    min = low;
                    max = up;
                    scale=1;
                }

                double expResult = scale*simpsonIntegrator.integrate(fun, min, max);
                double result = GaussLegendreQuadratureUtils.quadrate(fun, nPt, low, up);
                System.out.println("min: " + min + ", " + max);
                System.out.println(fun);
                System.out.println("expResult: " + expResult + ", result: " + result);
                assertEquals(expResult, result, simpsonIntegrator.getRelativeAccuracy() * Math.abs(expResult));
            }
        }
    }
}
