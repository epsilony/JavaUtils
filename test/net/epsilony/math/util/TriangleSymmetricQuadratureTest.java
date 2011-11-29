/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.math.util;

import java.util.Arrays;
import java.util.Random;
import org.apache.commons.math.analysis.BivariateRealFunction;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static net.epsilony.math.util.TriangleSymmetricQuadrature.*;

/**
 *
 * @author epsilonyuan@gmail.com
 */
public class TriangleSymmetricQuadratureTest {

    public TriangleSymmetricQuadratureTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testQuadrate_8args() throws Exception {
        System.out.println("quadrate");
        double x1 = 0.0;
        double y1 = 0.0;
        double x2 = 10;
        double y2 = 0.0;
        double x3 = 5;
        double y3 = 10;
        double dis = 5;
        int repeat = 1;
        SimpsonIntegratorOnTriangleDomain integrator=new SimpsonIntegratorOnTriangleDomain();
        integrator.integrator.setRelativeAccuracy(1e-12);  //the best that can pass the test! The bottle neck is the SimpsonIntegrator
        for (int i = 0; i < repeat; i++) {
            for (int power = MIN_POWER; power <= MAX_POWER; power++) {   
                BivariateRealFunction fun = new RandomBivariatePolynomial(power);
                System.out.println("Power: "+ power +" Repeat"+i);
                System.out.println("function:"+fun);
                double x1t=x1+new Random().nextDouble()*dis;
                double x2t=x2+new Random().nextDouble()*dis;
                double x3t=x3+new Random().nextDouble()*dis;
                double y1t=y1+new Random().nextDouble()*dis;
                double y2t=y2+new Random().nextDouble()*dis;
                double y3t=y3+new Random().nextDouble()*dis;
                System.out.println(String.format("(%f,%f)-(%f,%f)-(%f,%f)", x1t,y1t,x2t,y2t,x3t,y3t));
                System.out.println("面积 = "+EYMath.triangleArea(x1t, y1t, x2t, y2t, x3t, y3t));
                
                double simpsonResult = integrator.integrate(x1t, y1t, x2t, y2t, x3t, y3t, fun);
                double result = TriangleSymmetricQuadrature.quadrate(x1t, y1t, x2t, y2t, x3t, y3t, power, fun);
                System.out.println("Simpson Result: "+simpsonResult+" result: "+result);
                assertEquals(simpsonResult, result, Math.abs(simpsonResult*integrator.integrator.getRelativeAccuracy()));
                System.out.println("");
            }
        }
    }
    
    @Test
    public void testRandomBivariatePolynomial(){
        System.out.println("Test the sample use function");
        RandomBivariatePolynomial fun=new RandomBivariatePolynomial(3);
        System.out.println(fun);
        int repeat=100;
        for(int i=0;i<repeat;i++){
            double x=new Random().nextDouble();
            double y=new Random().nextDouble();
            double[] coefs=fun.coefs;
            double expResult=coefs[0]+coefs[1]*x+coefs[2]*y+coefs[3]*x*x+coefs[4]*x*y+coefs[5]*y*y+coefs[6]*x*x*x+coefs[7]*x*x*y+coefs[8]*x*y*y+coefs[9]*y*y*y;
            double result=fun.value(x, y);
            assertEquals(expResult, result,1e-10);
        }
    }
    
    public static class RandomBivariatePolynomial implements BivariateRealFunction{
        double[] coefs;
        int power;
        @Override
        public double value(double x,double y){
            double[] t=new double[power+1];
            double result=coefs[0];
            t[0]=1;
            for(int i=1,index=1;i<=power;i++){
                for(int j=i;j>0;j--){
                    t[j]=t[j-1]*y;
                }
                t[0]*=x;
                for(int j=0;j<=i;j++){
                    result+=t[j]*coefs[index++];
                }
            }
            return result;
        }

        public RandomBivariatePolynomial(int power) {
            this.power = power;
            coefs=new double[((power+1)*(power+2))/2];
            for(int i=0;i<coefs.length;i++){
                coefs[i]=new Random().nextDouble();
            }
        }

        @Override
        public String toString() {
            return Arrays.toString(coefs);
        }
                  
    }
}


