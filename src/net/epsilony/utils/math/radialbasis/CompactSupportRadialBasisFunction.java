/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.math.radialbasis;

import net.epsilony.math.polynomial.PolynomialUtils;
import org.apache.commons.math.analysis.polynomials.PolynomialFunction;
import static java.lang.Math.*;

/**
 *
 * @author epsilonyuan@gmail.com
 */
public class CompactSupportRadialBasisFunction implements RadialBasisFunction {

    private double delta;
    private double centerX;
    private double centerY;
    PolynomialFunction p;
    PolynomialFunction p1;
    PolynomialFunction p2;
    Type type;

    @Override
    public RadialBasisFunction CopyOf(boolean deep) {

        return new CompactSupportRadialBasisFunction(type);

    }

    public enum Type {

        Wu_C2(1, 0), Wu_C4(2, 1), Wendland_C2(0, 2), Wendland_C4(2, 3), Wendland_C6(3, 4);

        Type(int index1, int index2) {
            this.index1 = index1;
            this.index2 = index2;
        }
        int index1;
        int index2;
    }
    private static double[][] ceofs1 = new double[][]{{1, -4, 6, -4, 1}, {1, -5, 10, -5, 1}, {1, -6, 15, -20, 15, -6, 1}, {1, -8, 28, -56, 70, -56, 28, -8, 1}};
    private static double[][] coefs2 = new double[][]{{8, 40, 48, 25, 5}, {6, 36, 82, 72, 30, 5}, {1, 4}, {3, 18, 35}, {1, 8, 25, 32}};


    static {
    }

    public CompactSupportRadialBasisFunction(Type type) {
        this.type=type;
        p = new PolynomialFunction(PolynomialUtils.coeffientsMultply(ceofs1[type.index1], coefs2[type.index2]));
        p1 = p.polynomialDerivative();
        p2 = p1.polynomialDerivative();
    }

    @Override
    public double[] partialDifferential(double x, double y, double[] results) {
        double r, t0, t;
        r = sqrt((x - centerX) * (x - centerX) + (y - centerY) * (y - centerY));
        if (0 != r && r <= delta) {
            t0 = r / delta;
            t = p1.value(t0) / delta / r;
            results[0] = t * (x - centerX);
            results[1] = t * (y - centerY);

        } else {
            results[0] = 0;
            results[1] = 0;
        }
        return results;
    }

    @Override
    public void setCenter(double centerX, double centerY) {
        this.centerX = centerX;
        this.centerY = centerY;
    }

    @Override
    public double value(double x, double y) {
        double t = sqrt((x - centerX) * (x - centerX) + (y - centerY) * (y - centerY)) / delta;
        if (t >= 1) {
            return 0;
        }

        return p.value(t);

    }

    @Override
    public double[] quadPartialDifferential(double x, double y, double[] results) {
        double r = sqrt((x - centerX) * (x - centerX) + (y - centerY) * (y - centerY));
        if (r == 0) {
            double t = p2.value(0);
            results[0] = t;
            results[1] = t;
            results[2] = t;
        } else if (r > delta) {
            results[0] = 0;
            results[1] = 0;
            results[2] = 0;
        } else {
            double t0 = r / delta;
            double t1 = x - centerX / r;
            double t2 = y - centerY / r;
            double p2d = p2.value(t0);
            double p1d = p1.value(t0);

            results[0] = p2d * (t1 / delta) * (t1 / delta) + p1d / delta / r * (1 - t1 * t1);
            results[1] = p2d * (t1 / delta) * (t2 / delta) + p1d / delta / r * (-t1 * t2);
            results[2] = p2d * (t2 / delta) * (t2 / delta) + p1d / delta / r * (1 - t2 * t2);
        }
        return results;
    }

    @Override
    public void setNodesAverageDistance(double dc) {
////        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setSupportDomainRadius(double delta) {
        this.delta = delta;
    }
}
