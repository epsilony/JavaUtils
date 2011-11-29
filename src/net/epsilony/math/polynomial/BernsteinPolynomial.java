/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.epsilony.math.polynomial;
import org.apache.commons.math.analysis.polynomials.PolynomialFunction;
import static net.epsilony.math.util.EYMath.*;
import static org.apache.commons.math.util.MathUtils.*;

/**
 *
 * @author epsilonyuan@gmail.com
 */
public class BernsteinPolynomial extends PolynomialFunction{

    public static double[] getCoefficients(int i,int n){
        double[] coefs=new double[n+1];
        double t=permutation(n,n-i);
        for(int k=0;k<=n-i;k++){
            coefs[k+i]=t/factorial(n-i-k)/factorial(k);
            t=t*-1;
        }
        return coefs;
    }
    public BernsteinPolynomial(int i,int n) {
        super(getCoefficients(i,n));
    }
}
