/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.math.radialbasis;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import org.apache.commons.math.analysis.PolynomialFunction;
import static java.lang.Math.*;

/**
 *
 * @author epsilon
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

        Wu_C2(0), Wu_C4(1), Wendland_C2(2), Wendland_C4(3), Wendland_C6(4);

        Type(int index) {
            this.index = index;

        }
        int index;
    }
    private static double[][] coefs = new double[][]{//系数，一阶导数/r,二阶导数/2
        {8, -72, 105, -63, 27, -5}, {-144, 315, -315, 189, -45}, {},//wuc2
        {6, -44, 198, -231, 99, -33, 5}, {-88, 792, -1155, 693, -297, 55}, {1584, -3465, 3465, -2079, 495},//wuc4
        {1, -10, 20, -15, 4}, {-20, 60, -60, 20}, {},//Wendland_C2
        {3, -28, 210, -448, 420, -192, 35}, {-56, 840, -2240, 2520, -1344, 280}, {1680, -6720, 10080, -6720, 1680},//Wendland_C4
        {1, -11, 66, -462, 1056, -1155, 704, -231, 32}, {-22, 264, -2772, 7392, -9240, 6336, -2310, 352}, {528, -11088, 36960, -55440, 44352, -18480, 3168},//Wendland_C6
    };

    public CompactSupportRadialBasisFunction(Type type) {
        this.type = type;
        p = new PolynomialFunction(coefs[type.index * 3]);
        p1 = new PolynomialFunction(coefs[type.index * 3 + 1]);
        p2 = new PolynomialFunction(coefs[type.index * 3 + 2]);
    }

    @Override
    public double[] partialDifferential(double x, double y, double[] results) {
        double r, t0, t;
        r = sqrt((x - centerX) * (x - centerX) + (y - centerY) * (y - centerY));
        t = r / delta;
        if (t >= 1) {
            results[0] = 0;
            results[1] = 0;
            return results;
        }
        t0 = p1.value(t);
        results[0] = (x - centerX) * t0 / delta;
        results[1] = (y - centerY) * t0 / delta;
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
        if (r >= delta) {
            results[0] = 0;
            results[1] = 0;
            results[2] = 0;
            return results;
        }
        double t1, t2;
        double t = r / delta;
        t1 = p1.value(t);
        t2 = p2.value(t);
        results[0] = (t1 + (x - centerX) * (x - centerX) * t2 / delta) / delta;
        results[1] = (x - centerX) * (y - centerY) * t2 / delta / delta;
        results[2] = (t1 + (y - centerY) * (y - centerY) * t2 / delta) / delta;
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

    public static void main(String[] args) throws IOException {
        CompactSupportRadialBasisFunction wuc2 = new CompactSupportRadialBasisFunction(Type.Wu_C2);
        CompactSupportRadialBasisFunction wuc4 = new CompactSupportRadialBasisFunction(Type.Wu_C4);
        CompactSupportRadialBasisFunction wendlandc2 = new CompactSupportRadialBasisFunction(Type.Wendland_C2);
        CompactSupportRadialBasisFunction wendlandc4 = new CompactSupportRadialBasisFunction(Type.Wendland_C4);
        CompactSupportRadialBasisFunction wendlandc6 = new CompactSupportRadialBasisFunction(Type.Wendland_C6);
        
        BufferedWriter wuc2f = new BufferedWriter(new FileWriter("wuc2"));
        BufferedWriter wuc4f = new BufferedWriter(new FileWriter("wuc4"));
        BufferedWriter wendlandc2f = new BufferedWriter(new FileWriter("wendlandc2"));
        BufferedWriter wendlandc4f = new BufferedWriter(new FileWriter("wendlandc4"));
        BufferedWriter wendlandc6f = new BufferedWriter(new FileWriter("wendlandc6"));
        int sum = 100;
        double xc = 1.8, yc = 3.1, delta = 3,domain=3.1;
        CompactSupportRadialBasisFunction[] csrbfs = new CompactSupportRadialBasisFunction[]{wuc2, wuc4, wendlandc2, wendlandc4, wendlandc6};
        BufferedWriter[] bws = new BufferedWriter[]{wuc2f, wuc4f, wendlandc2f, wendlandc4f, wendlandc6f};
        double[] tds = new double[3];
        for (int k = 0; k < 5; k++) {
            csrbfs[k].setCenter(xc, yc);
            csrbfs[k].setSupportDomainRadius(delta);
            for (int i = 0; i <= sum; i++) {
                double x=xc-domain+i*2*domain/sum;
                for (int j = 0; j <= sum; j++) {
                    double y=yc-domain+i*2*domain/sum;
                    double r1,r2,r3,r4;
                    r1=csrbfs[k].value(x, y);
                    csrbfs[k].partialDifferential(x, y, tds);
                    r2=tds[0];
                    csrbfs[k].quadPartialDifferential(x, y, tds);
                    r3=tds[0];
                    r4=tds[1];
                    bws[k].write(String.format("%f   %f   %f   %f   %f   %f", x,y,r1,r2,r3,r4));
                }
            }
            bws[k].close();
        }
    }
}
