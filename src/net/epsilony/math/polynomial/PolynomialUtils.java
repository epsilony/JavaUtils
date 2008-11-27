/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.epsilony.math.polynomial;
import org.apache.commons.math.analysis.PolynomialFunction;


/**
 * 该类用于对apache commons-math 的PoloynomialFunction类进行操作
 * @author epsilon
 */

public class PolynomialUtils {
    /**
     * @param p1
     * @param p2
     * @return p1+p2
     */
    public static PolynomialFunction add(PolynomialFunction p1,PolynomialFunction p2){
        if(p1.degree()>p2.degree())
        {
            PolynomialFunction tp=p1;
            p1=p2;
            p2=tp;
        }
        double [] coefs=new double[p2.degree()+1];
        double [] coefs1=p1.getCoefficients();
        double [] coefs2=p2.getCoefficients();
        int i;
        for (i=0;i<=p1.degree();i++){
            coefs[i]=coefs1[i]+coefs2[i];
        }
        for(;i<=p2.degree();i++){
            coefs[i]=coefs2[i];
        }
        return new PolynomialFunction(coefs);
    }
    /**
     * @param p1
     * @param p2
     * @return p1*p2
     */
    public static PolynomialFunction multiply(PolynomialFunction p1,PolynomialFunction p2){
        return new PolynomialFunction(coeffientsMultply(p1.getCoefficients(), p2.getCoefficients()));
    }
    
    
    public static PolynomialFunction multiply(PolynomialFunction p1,double factor){
        double []coefs=p1.getCoefficients();
        for(int i=0;i<coefs.length;i++)
        {
            coefs[i]*=factor;
        }
        return new PolynomialFunction(coefs);
    }
    
    public static double[] coeffientsMultply(double[] coefs1,double [] coefs2){
        double [] coefs =new double [coefs1.length+coefs2.length-1];

        int i,j;
        double t=0;
        for(i=0;i<coefs1.length;i++)
        {
            t=coefs1[i];
            if(t==0)
            {
                continue;
            }
            for(j=0;j<coefs2.length;j++)
            {
                coefs[j+i]+=t*coefs2[j];
            }
        }
        return coefs;
    }
    
    public static double [] coeffienctsMultiply(double []coefs,double factor){
        double []results=new double[coefs.length];
        for (int i=0;i<coefs.length;i++){
            results[i]=factor*coefs[i];
        }
        return results;
    }

    public static PolynomialFunction integrate(PolynomialFunction p1){
        double [] coefs=p1.getCoefficients();
        double [] iCoefs=new double[coefs.length+1];
        for(int i=0;i<coefs.length;i++){
            iCoefs[i+1]=coefs[i]/(i+1);
        }
        return new PolynomialFunction(iCoefs);
    }

}
