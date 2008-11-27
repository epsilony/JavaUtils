/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.math.analysis;

import java.util.Arrays;
import static java.lang.Math.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.epsilony.math.polynomial.LegendrePolynomial;
import org.apache.commons.math.ArgumentOutsideDomainException;
import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MathException;
import org.apache.commons.math.analysis.LaguerreSolver;
import org.apache.commons.math.analysis.PolynomialFunction;
import org.apache.commons.math.analysis.RombergIntegrator;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.complex.Complex;

/**
 * Gauss－Legendre积分
 * @author epsilon
 */
public class GaussLegendreQuadrature {

    /**
     * 获取高斯－勒让德公式的（－1，1）积分点
     * @param n 设定积分公式公有n点
     * @return double [] the Gauss-Legendre Quadrature Points from -1 to 1 
     */
    int nPoints;
    UnivariateRealFunction function = null;
    public static final int MAXPOINTS = 14;
    public static final int MINPOINTS = 1;
    private static double[][] pointsArray = new double[MAXPOINTS][];
    private static double[][] coefsArray = new double[MAXPOINTS][];
    double[] points;
    double[] coefs;

    public GaussLegendreQuadrature(int nPoints, UnivariateRealFunction fun)  {
        try {
            isNumInDomain(nPoints);
            this.nPoints = nPoints;
            points = getGaussLegendreQuadraturePoints(nPoints);
            coefs = getGaussLegendreQuadratureCoefficients(nPoints);
            function = fun;
        } catch (ArgumentOutsideDomainException ex) {
            Logger.getLogger(GaussLegendreQuadrature.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

//    public static void quadrate(double min,double max,int nPoints,UnivariateRealFunction fun){
//        double t1 = (max - min) * 0.5;
//        double t2 = (max + min) * 0.5;
//        double result = 0;
//        if(coefs[i])
//        for (int i = 0; i < nPoints; i++) {
//            result += coefs[i] * t1 * fun.value(t2 + t1 * points[i]);
//        }
//        return result;
//    }
    
    public double quadrate(double min, double max) throws FunctionEvaluationException {
        double t1 = (max - min) * 0.5;
        double t2 = (max + min) * 0.5;
        double result = 0;
        for (int i = 0; i < nPoints; i++) {
            result += coefs[i] * t1 * function.value(t2 + t1 * points[i]);
        }
        return result;
    }

    public UnivariateRealFunction getFunction() {
        return function;
    }

    public void setFunction(UnivariateRealFunction function) {
        this.function = function;
    }

     public static double[] getGaussLegendreQuadraturePoints(int n,double[] results) throws ArgumentOutsideDomainException{
         
        isNumInDomain(n);

        if (null == pointsArray[n - 1]) {

            double[] ps = new double[n];
            LegendrePolynomial pl = new LegendrePolynomial(n);
            int i;
            LaguerreSolver laguerreSolver = new LaguerreSolver(pl);
            Complex[] complex;
            try {

                complex = laguerreSolver.solveAll(pl.getCoefficients(), 0);
            } catch (ConvergenceException ex) {
                Logger.getLogger(GaussLegendreQuadrature.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            } catch (FunctionEvaluationException ex) {
                Logger.getLogger(GaussLegendreQuadrature.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }

            for (i = 0; i < complex.length; i++) {
                if (complex[i].getImaginary() != 0) {
                    try {
                        throw new Exception("Bad GaussLegendrePoint get, unknown reasons!");
                    } catch (Exception ex) {
                        Logger.getLogger(GaussLegendreQuadrature.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                ps[i] = complex[i].getReal();
            }
            Arrays.sort(ps);
            pointsArray[n - 1] = ps;
            for(i=0;i<n;i++){
                results[i]=ps[i];
            }
            return results;

        } else {
            for(int i=0;i<n;i++){
                results[i]=pointsArray[n-1][i];
            }
            return results;
        }

     }
    
    public static double[] getGaussLegendreQuadraturePoints(int n) throws ArgumentOutsideDomainException {
        double [] results=new double[n];
        return getGaussLegendreQuadraturePoints(n, results);
    }

    public static boolean isNumInDomain(int n) throws ArgumentOutsideDomainException {
        if (n < MINPOINTS || n > MAXPOINTS) {
            throw new ArgumentOutsideDomainException(n, MINPOINTS, MAXPOINTS);
        }
        return true;
    }

    public static double[] getGaussLegendreQuadratureCoefficients(int n, double[] results) throws ArgumentOutsideDomainException {
        isNumInDomain(n);
        if (null == coefsArray[n - 1]) {
            double[] ps;
            double[] cs = new double[n];
            int i;
            int half = (int) ceil(n / 2.0);
            PolynomialFunction legenderPolynomial = new LegendrePolynomial(n);
            PolynomialFunction diffLegenderPolynomial = legenderPolynomial.polynomialDerivative();
            double t;

            ps = getGaussLegendreQuadraturePoints(n);

            for (i = 0; i < half; i++) {
                t = ps[i];
                cs[i] = pow(diffLegenderPolynomial.value(t), -2) * 2 / (1 - t * t);
            }
            for (i = 0; i < n / 2; i++) {
                cs[n - 1 - i] = cs[i];
            }
            coefsArray[n - 1] = cs;
            for (i = 0; i < n; i++) {
                results[i] = cs[i];
            }
            return results;

        } else {
            for(int i=0;i<n;i++){
                results[i]=coefsArray[n-1][i];
            }
            return results;
        }
    }

    public static double[] getGaussLegendreQuadratureCoefficients(int n) throws ArgumentOutsideDomainException {
        double[] results=new double[n];
        return getGaussLegendreQuadratureCoefficients(n, results);
    }

    //测试得知 小于等于13个高斯点一般是正常的，多了就超出了[-1,1]的范围了。
    public static void main(String args[]) throws FunctionEvaluationException, ConvergenceException, MathException, Exception {
        for (int i = 1; i < 15; i++) {
            System.out.println("i=" + i);
            System.out.println(Arrays.toString(getGaussLegendreQuadraturePoints(i)));
            System.out.println(Arrays.toString(getGaussLegendreQuadratureCoefficients(i)));
        }
        System.out.println("");
        double[] coefs = {1, 3, 7, 4, 5};
        PolynomialFunction testFun = new PolynomialFunction(coefs);
        RombergIntegrator rombergIntergrator = new RombergIntegrator(testFun);
        double max = 301;
        double min = 197;
        System.out.println("r: " + rombergIntergrator.integrate(min, max));
        for (int i = 1; i < 14; i++) {
            GaussLegendreQuadrature gaussLegendreQuadrature = new GaussLegendreQuadrature(i, testFun);
            System.out.println(i + ": " + gaussLegendreQuadrature.quadrate(min, max));
        }
    }
}
