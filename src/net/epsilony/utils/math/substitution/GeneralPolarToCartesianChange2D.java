/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.epsilony.utils.math.substitution;

import net.epsilony.utils.java2d.PathInfoCalculator;
import net.epsilony.utils.math.analysis.UnivariateVectorialFunctionEx;
import net.epsilony.utils.java2d.PathInfoNode;
import org.apache.commons.math.FunctionEvaluationException;
import static java.lang.Math.abs;

/**
 * 一个变换从最初的u[-1,1],v[-1,1],到ro[0,1],th[0,1]到x,y的变换。
 * 
 * @author epsilonyuan@gmail.com
 */
public class GeneralPolarToCartesianChange2D implements BivariateMapper{
    PathInfoCalculator pathInfoCal;
    UnivariateVectorialFunctionEx diffFun;
    double xOri,yOri;

    public GeneralPolarToCartesianChange2D() {
        xOri=0;
        yOri=0;
    }

    public GeneralPolarToCartesianChange2D(PathInfoNode infoNode,double xOri,double yOri) {
        setXOri(xOri);
        setYOri(yOri);
        setPathInfoNode(infoNode);
    }

    public void setXOri(double xOri) {
        this.xOri = xOri;
    }

    public void setYOri(double yOri) {
        this.yOri = yOri;
    }
    
    public void setOri(double x,double y)
    {
        xOri=x;
        yOri=y;
    }
    
    

    
    
    /**
     * 广义极坐标变换，ro=(u+1)/2,th=(u+1)/2,圆心为xOri,yOri,圆弧为PathInfoNode所指定的参数曲线x(th),v(th)
     * @param u [-1,1]
     * @param v [-1,1]
     * @param results 最小长度为3的数组，运算返的值为：{ x(u,v),y(u,v),diff({x,y},{u,v})};
     * @return results
     * @throws org.apache.commons.math.FunctionEvaluationException
     */
    @Override
    public double[] getResults(double u, double v, double[] results){
        try {
            double[] tds=new double[2];
            double[] tds2=new double[2];
            double ro,th,tdxro,tdxth,tdyro,tdyth;
            ro=(u+1)/2;
            th=(v+1)/2;
            pathInfoCal.value(th, tds);
            results[0]=xOri*(1-ro)+tds[0]*ro;
            results[1]=yOri*(1-ro)+tds[1]*ro;
            diffFun.value(th, tds2);
            tdxro=-xOri+tds[0];
            tdxth=tds2[0]*ro;
            tdyro=-yOri+tds[1];
            tdyth=tds2[1]*ro;
            results[2]=abs((tdxro*tdyth-tdxth*tdyro)/4);
            return results;
        } catch (FunctionEvaluationException ex) {
        }finally{
            return null;
        }
    }

    public void setPathInfoNode(PathInfoNode infoNode) {
        if(null==pathInfoCal){
            pathInfoCal=new PathInfoCalculator(infoNode);
            diffFun=pathInfoCal.vectorDerivative();
        }else
        {
            pathInfoCal.setPathInfoNode(infoNode);
        }
    }
    
}
