/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.math.util;

import java.util.Arrays;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.BivariateRealFunction;

/**
 * the data is copy from: </ br>
 * D.A. Dunavant. High degree efficient symmetrical Gaussian quadrature rules for thetriangle. International Journal of Numerical Methods in Engineering, 22:1129-1148
 * @author epsilon
 */
public class TriangleSymmetricQuadrature {

    final static int[] numPts = new int[]{1, 3, 4, 6, 7, 12, 13};
    final static double[][] weights = new double[][]{
        {1},
        {1 / 3d, 1 / 3d, 1 / 3d},
        {-27 / 48d, 25 / 48d, 25 / 48d, 25 / 48d},
        {0.109951743655322, 0.109951743655322, 0.109951743655322, 0.223381589678011, 0.223381589678011, 0.223381589678011},
        {0.225000000000000, 0.125939180544827, 0.125939180544827, 0.125939180544827, 0.132394152788506, 0.132394152788506, 0.132394152788506},
        {0.050844906370207, 0.050844906370207, 0.050844906370207, 0.116786275726379, 0.116786275726379, 0.116786275726379, 0.082851075618374, 0.082851075618374, 0.082851075618374, 0.082851075618374, 0.082851075618374, 0.082851075618374},
        {-0.149570044467670, 0.175615257433204, 0.175615257433204, 0.175615257433204, 0.053347235608839, 0.053347235608839, 0.053347235608839, 0.077113760890257, 0.077113760890257, 0.077113760890257, 0.077113760890257, 0.077113760890257, 0.077113760890257}};
    final static double[][] barycentricCoordinates = new double[][]{
        {1 / 3d, 1 / 3d, 1 / 3d},
        {2 / 3d, 1 / 6d, 1 / 6d, 1 / 6d, 2 / 3d, 1 / 6d, 1 / 6d, 1 / 6d, 2 / 3d},
        {1 / 3d, 1 / 3d, 1 / 3d, 0.6, 0.2, 0.2, 0.2, 0.6, 0.2, 0.2, 0.2, 0.6},
        {0.816847572980459, 0.091576213509771, 0.091576213509771, 0.091576213509771, 0.816847572980459, 0.091576213509771, 0.091576213509771, 0.091576213509771, 0.816847572980459, 0.108103018168070, 0.445948490915965, 0.445948490915965, 0.445948490915965, 0.108103018168070, 0.445948490915965, 0.445948490915965, 0.445948490915965, 0.108103018168070},
        {1 / 3d, 1 / 3d, 1 / 3d, 0.797426985353087, 0.101286507323456, 0.101286507323456, 0.101286507323456, 0.797426985353087, 0.101286507323456, 0.101286507323456, 0.101286507323456, 0.797426985353087, 0.059715871789770, 0.470142064105115, 0.470142064105115, 0.470142064105115, 0.059715871789770, 0.470142064105115, 0.470142064105115, 0.470142064105115, 0.059715871789770},
        {0.873821971016996, 0.063089014491502, 0.063089014491502, 0.063089014491502, 0.873821971016996, 0.063089014491502, 0.063089014491502, 0.063089014491502, 0.873821971016996, 0.501426509658179, 0.249286745170910, 0.249286745170910, 0.249286745170910, 0.501426509658179, 0.249286745170910, 0.249286745170910, 0.249286745170910, 0.501426509658179, 0.626502499121399, 0.310352451033785, 0.053145049844816, 0.626502499121399, 0.053145049844816, 0.310352451033785, 0.310352451033785, 0.626502499121399, 0.053145049844816, 0.310352451033785, 0.053145049844816, 0.626502499121399, 0.053145049844816, 0.626502499121399, 0.310352451033785, 0.053145049844816, 0.310352451033785, 0.626502499121399},
        {1 / 3d, 1 / 3d, 1 / 3d, 0.479308067841923, 0.260345966079038, 0.260345966079038, 0.260345966079038, 0.479308067841923, 0.260345966079038, 0.260345966079038, 0.260345966079038, 0.479308067841923, 0.869739794195568, 0.065130102902216, 0.065130102902216, 0.065130102902216, 0.869739794195568, 0.065130102902216, 0.065130102902216, 0.065130102902216, 0.869739794195568, 0.638444188569809, 0.312865496004875, 0.048690315425316, 0.638444188569809, 0.048690315425316, 0.312865496004875, 0.312865496004875, 0.638444188569809, 0.048690315425316, 0.312865496004875, 0.048690315425316, 0.638444188569809, 0.048690315425316, 0.638444188569809, 0.312865496004875, 0.048690315425316, 0.312865496004875, 0.638444188569809}
    };
//    static {
//        for(int i=0;i<weights.length;i++){
//            System.out.println(Arrays.toString(weights[i]));
//        }
//    }

    public static double[] getBarycentricCoordinates(int power, boolean copy) {
        if (copy) {
            return Arrays.copyOf(barycentricCoordinates[power - 1], barycentricCoordinates[power - 1].length);
        } else {
            return barycentricCoordinates[power - 1];
        }
    }

    public static double[] getBarycentricCoordinates(int power) {
        return getBarycentricCoordinates(power, false);
    }

    public static double[] getWeights(int power, boolean copy) {
        if (copy) {
            return Arrays.copyOf(weights[power - 1], weights[power - 1].length);
        } else {
            return weights[power - 1];
        }
    }
    
    public static double[] getWeights(int power){
        return getWeights(power,false);
    }

    public static double quadrate(double x1, double y1, double x2, double y2, double x3, double y3, int power, BivariateRealFunction fun, double area) throws FunctionEvaluationException {
        int numPt = numPts[power - 1];
        double[] coords = barycentricCoordinates[power -1];
        double[] ws= weights[power-1];
        double result=0;
        for (int i = 0; i < numPt; i++) {
            double x = x1 *coords[i*3]+x2*coords[i*3+1]+x3*coords[i*3+2];
            double y = y1 *coords[i*3]+y2*coords[i*3+1]+y3*coords[i*3+2];
            result+=ws[i]*fun.value(x, y);
        }
        result*=area;
        return result;
    }
    
    public static double quadrate(double x1, double y1,double x2,double y2,double x3,double y3,int power,BivariateRealFunction fun) throws FunctionEvaluationException{
        double area = Math.abs((x1 - x2) * (y3 - y2) - (x3 - x2) * (y1 - y2)) / 2;
        return quadrate(x1, y1, x2, y2, x3, y3, power, fun, area);
    }
}
