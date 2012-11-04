/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.epsilony.utils.math.substitution;

import org.apache.commons.math.FunctionEvaluationException;
import static java.lang.Math.abs;

/**
 * 将一个u,v区域映射到x,y区域，其中u,v属于[-1,1]
 * x,y区域为三解形，其三个顶点p1,p2,p3的坐标分别为(x1,y1),(x2,y2)，（x3,y3)
 * 映射的方法是：取p1到p2的u分点p1+(p2-p1)*u 为点pu,再取p3到pu的v分点，则有u,v到x,y的映射g (这里u,v属于[0,1])
 * u,v到((u+1)/2,(v+1)/2)的映射k
 * 则gk映射即为本类的映射
 * @author epsilonyuan@gmail.com
 */
public class TriMapper implements BivariateMapper{

    double [] vertex = new double[6];  //三角形的三个顶点坐标
    
    public void setVertex(double x1,double y1,double x2,double y2,double x3,double y3){
        vertex[0]=x1;
        vertex[1]=y1;
        vertex[2]=x2;
        vertex[3]=y2;
        vertex[4]=x3;
        vertex[5]=y3;
    }
    
    @Override
    public double[] getResults(double u, double v, double[] results){
        u=(u+1)/2;
        v=(v+1)/2;
        results[0]=(vertex[0]*(1-u)+vertex[2]*u)*v+vertex[4]*(1-v);
        results[1]=(vertex[1]*(1-u)+vertex[3]*u)*v+vertex[5]*(1-v);
        results[3]=1/4*abs(v*(vertex[2]-vertex[0])*((vertex[3]-vertex[1])*u+vertex[1]-vertex[5])-(vertex[3]-vertex[1])*v*((vertex[0]-vertex[4])+(vertex[2]-vertex[0])*u));
        return results;
    }

}
