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
    double coef;
    Type type;

    @Override
    public RadialBasisFunction CopyOf(boolean deep) {

        return new CompactSupportRadialBasisFunction(type);

    }

    /**
     *
     */
    public enum Type {

        Wu_C2(0), Wu_C4(1), Wendland_C2(2), Wendland_C4(3), Wendland_C6(4);

        Type(int index) {
            this.index = index;

        }
        int index;
    }
    private static double[][] coefs = new double[][]{//系数，一阶导数/r,二阶导数/2
        {8, 0, -72, 105, 0, -63, 0, 27, 0, -5}, {-144, 315, 0, -315, 0, 189, 0, -45}, {0, -945, 0, 945, 0, -315},//wuc2
        {6, 0, -44, 0, 198, -231, 0, 99, 0, -33, 0, 5}, {-88, 0, 792, -1155, 0, 693, 0, -297, 0, 55}, {1584, -3465, 0, 3465, 0, -2079, 0, 495},//wuc4
        {1, 0, -10, 20, -15, 4}, {-20, 60, -60, 20}, {-120, 60},//Wendland_C2
        {3, 0, -28, 0, 210, -448, 420, -192, 35}, {-56, 0, 840, -2240, 2520, -1344, 280}, {1680, -6720, 10080, -6720, 1680},//Wendland_C4
        {1, 0, -11, 0, 66, 0, -462, 1056, -1155, 704, -231, 32}, {-22, 0, 264, 0, -2772, 7392, -9240, 6336, -2310, 352}, {528, 0, -11088, 36960, -55440, 44352, -18480, 3168},//Wendland_C6
    };
    private static double[] coefs2 = new double[]{315, 0, 60, 0, 0};

    public CompactSupportRadialBasisFunction(Type type) {
        this.type = type;
        p = new PolynomialFunction(coefs[type.index * 3]);
        p1 = new PolynomialFunction(coefs[type.index * 3 + 1]);
        p2 = new PolynomialFunction(coefs[type.index * 3 + 2]);
        coef = coefs2[type.index];
    }

    @Override
    public double[] partialDifferential(double x, double y, double[] results) {
        double r, g11, t;
        r = sqrt((x - centerX) * (x - centerX) + (y - centerY) * (y - centerY));
        t = r / delta;
        if (t >= 1) {
            results[0] = 0;
            results[1] = 0;
            return results;
        }
        g11 = p1.value(t) / delta / delta;
        results[0] = (x - centerX) * g11;
        results[1] = (y - centerY) * g11;
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
        double g1, g2;
        double t = r / delta;
        g1 = p1.value(t);
        g2 = p2.value(t);
        if (0 != t) {
            results[0] = (g1 + (x - centerX) * (x - centerX) / delta / delta * (g2 + coef / t)) / delta / delta;
            results[1] = (x - centerX) * (y - centerY) * (g2 + coef / t) / pow(delta, 4);
            results[2] = (g1 + (y - centerY) * (y - centerY) / delta / delta * (g2 + coef / t)) / delta / delta;
        } else {
            double gt = g1 / delta / delta;
            results[0] = gt;
            results[1] = 0;
            results[2] = gt;
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

    public static void main(String[] args) throws IOException {


        //测试并通过
        CompactSupportRadialBasisFunction wuc2 = new CompactSupportRadialBasisFunction(Type.Wu_C2);
        CompactSupportRadialBasisFunction wuc4 = new CompactSupportRadialBasisFunction(Type.Wu_C4);
        CompactSupportRadialBasisFunction wendlandc2 = new CompactSupportRadialBasisFunction(Type.Wendland_C2);
        CompactSupportRadialBasisFunction wendlandc4 = new CompactSupportRadialBasisFunction(Type.Wendland_C4);
        CompactSupportRadialBasisFunction wendlandc6 = new CompactSupportRadialBasisFunction(Type.Wendland_C6);

        BufferedWriter wuc2f = new BufferedWriter(new FileWriter("./testdata/wuc2da"));
        BufferedWriter wuc4f = new BufferedWriter(new FileWriter("./testdata/wuc4da"));
        BufferedWriter wendlandc2f = new BufferedWriter(new FileWriter("./testdata/wendlandc2da"));
        BufferedWriter wendlandc4f = new BufferedWriter(new FileWriter("./testdata/wendlandc4da"));
        BufferedWriter wendlandc6f = new BufferedWriter(new FileWriter("./testdata/wendlandc6da"));
        int sum = 100;
        double xc = 1.8, yc = 3.1, delta = 3, domain = 3.1;
        CompactSupportRadialBasisFunction[] csrbfs = new CompactSupportRadialBasisFunction[]{wuc2, wuc4, wendlandc2, wendlandc4, wendlandc6};
        BufferedWriter[] bws = new BufferedWriter[]{wuc2f, wuc4f, wendlandc2f, wendlandc4f, wendlandc6f};
        double[] tds = new double[3];
        double r1, r2, r3, r4;
        for (int k = 0; k < 5; k++) {
            csrbfs[k].setCenter(xc, yc);
            csrbfs[k].setSupportDomainRadius(delta);

            for (int l = 0; l < 6; l++) {
               
                for (int i = 0; i <= sum; i++) {
                   
                    double y = yc - domain + i * 2 * domain / sum;
                    StringBuilder sb = new StringBuilder();
                    for (int j = 0; j <= sum; j++) {
                         double x = xc - domain +j* 2 * domain / sum;

                        switch (l) {
                            case 0:
                                sb.append(x + " ");
                                break;
                            case 1:
                                sb.append(y + " ");
                                break;
                            case 2:
                                r1 = csrbfs[k].value(x, y);
                                sb.append(r1 + " ");
                                break;
                            case 3:
                                csrbfs[k].partialDifferential(x, y, tds);
                                r2 = tds[0];
                                sb.append(r2 + " ");
                                break;
                            case 4:
                                csrbfs[k].quadPartialDifferential(x, y, tds);
                                r3 = tds[0];
                                sb.append(r3 + " ");
                                break;
                            case 5:
                                csrbfs[k].quadPartialDifferential(x, y, tds);
                                r4 = tds[1];
                                sb.append(r4 + " ");
                                break;

                        }
                    } sb.append(String.format("%n"));
                bws[k].write(sb.toString());
                }
               
            }
            bws[k].close();
        }
    }
}
