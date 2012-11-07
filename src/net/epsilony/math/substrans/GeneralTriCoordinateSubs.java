/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.epsilony.math.substrans;

import net.epsilony.math.analysis.DifferentiableUnivariateVectorialFunctionEx;
import net.epsilony.math.analysis.UnivariateVectorialFunctionEx;
import org.apache.commons.math.FunctionEvaluationException;
import static java.lang.Math.abs;

/**
 * 由u,v区域(u,v属于[-1,1]到一边为Bezier曲线，其余两边为直线的近三角型区域映射
 * @author epsilonyuan@gmail.com
 */
public class GeneralTriCoordinateSubs implements SubsTranslation2d{
    
    /**
     * get Coordinate Change Result
     * @param u
     * @param v
     * @return {x,y,jaccobi value}
     */
    private double x1,x2,y1,y2;
    double[] pt=new double[2];
    double[]diffs;
    DifferentiableUnivariateVectorialFunctionEx fun;
    UnivariateVectorialFunctionEx derivatedFun;
    
    public void setup(double[] oriPoint,DifferentiableUnivariateVectorialFunctionEx fun){
        this.fun=fun;
        derivatedFun=fun.vectorDerivative();
        x1=oriPoint[0];
        y1=oriPoint[1];     
    }

    public GeneralTriCoordinateSubs(double [] oriPoint,DifferentiableUnivariateVectorialFunctionEx fun) {
         setup(oriPoint,fun);
    }
    
    public double[] getResults(double u, double v,double [] results) {
        try {
            u=(u+1)/2;
            v=(v+1)/2;
            generatePoint(u);
            generateDiffs(u);
            results[0]=x1+(x2-x1)*v;
            results[1]=y1+(y2-y1)*v;
            results[2]=abs((v*diffs[0]*(y2-y1)-(x2-x1)*v*diffs[1])/4);
            return results;
        } catch (FunctionEvaluationException ex) {
        }finally{
            return null;
        }
    }

    /**
     * 
     * @param u
     * @param v
     * @return {{x2(u),y2(u)}
     */
    private void generatePoint(double u) throws FunctionEvaluationException{
        fun.value(u,pt);
        x2=pt[0];
        y2=pt[1];
    }
          

    /**
     * 
     * @param u
     * @param v
     * @return{dx2/du,dy2/du}
     */
    private void generateDiffs(double u) throws FunctionEvaluationException{
        derivatedFun.value(u, diffs);
    }
            

          
}
