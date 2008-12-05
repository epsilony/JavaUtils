/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.math.util;

import org.apache.commons.math.FunctionEvaluationException;
import static java.lang.Math.abs;

/**
 *
 * @author epsilon
 */
public class QuadrangleMapper implements BivariateMapper {

    double x1, y1, x2, y2, x3, y3, x4, y4; //x1,y1,x2,y2,x3,y3,x4,y4按顺时针排列

    public QuadrangleMapper(){
        
    }
    /**
     * <br>生成一个一般的四边形映射，四边形顶点按姆指为Z轴的右手法侧依次为：</br>
     * <br>(x<sub>1</sub>,y<sub>1</sub>)，(x<sub>2</sub>,y<sub>2</sub>)，(x<sub>3</sub>,y<sub>3</sub>)，(x<sub>4</sub>,y<sub>4</sub>)</br>
     * @param x1 x<sub>1</sub>
     * @param y1 y<sub>1</sub>
     * @param x2 x<sub>2</sub>
     * @param y2 y<sub>2</sub>
     * @param x3 x<sub>3</sub>
     * @param y3 y<sub>3</sub>
     * @param x4 x<sub>4</sub>
     * @param y4 y<sub>4</sub>
     */
    public QuadrangleMapper(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.x3 = x3;
        this.y3 = y3;
        this.x4 = x4;
        this.y4 = y4;
    }

    /**
     * 边与x,y轴平形的矩形映射，这个矩形区域为{(x,y)|x&isin;[x<sub>min</sub>,x<sub>max</sub>],x&isin;[y<sub>min</sub>,y<sub>max</sub>]}
     * @param xmin x<sub>min</sub>
     * @param ymin y<sub>min</sub>
     * @param xmax x<sub>max</sub>
     * @param ymin y<sub>max</sub>
     */
    public QuadrangleMapper(double xmin, double ymin, double xmax, double ymax) {
        this.x1 = xmin;
        this.y1 = ymin;
        this.y2 = ymin;
        this.x2 = xmax;
        this.x3 = xmax;
        this.y3 = ymax;
        this.x4 = xmin;
        this.y4 = ymax;
    }
    
    /**
     * <br>设置顶点，四边形顶点按姆指为Z轴的右手法侧依次为：</br>
     * <br>(x<sub>1</sub>,y<sub>1</sub>)，(x<sub>2</sub>,y<sub>2</sub>)，(x<sub>3</sub>,y<sub>3</sub>)，(x<sub>4</sub>,y<sub>4</sub>)</br>
     * @param x1 x<sub>1</sub>
     * @param y1 y<sub>1</sub>
     * @param x2 x<sub>2</sub>
     * @param y2 y<sub>2</sub>
     * @param x3 x<sub>3</sub>
     * @param y3 y<sub>3</sub>
     * @param x4 x<sub>4</sub>
     * @param y4 y<sub>4</sub>
     */
    public void setVertices(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.x3 = x3;
        this.y3 = y3;
        this.x4 = x4;
        this.y4 = y4;
    }

    /**
     * 边与x,y轴平形的矩形映射顶点，这个矩形区域为{(x,y)|x&isin;[x<sub>min</sub>,x<sub>max</sub>],x&isin;[y<sub>min</sub>,y<sub>max</sub>]}
     * @param xmin x<sub>min</sub>
     * @param ymin y<sub>min</sub>
     * @param xmax x<sub>max</sub>
     * @param ymin y<sub>max</sub>
     */
    public void setVertices(double xmin, double ymin, double xmax, double ymax) {
        this.x1 = xmin;
        this.y1 = ymin;
        this.y2 = ymin;
        this.x2 = xmax;
        this.x3 = xmax;
        this.y3 = ymax;
        this.x4 = xmin;
        this.y4 = ymax;
    }

    @Override
    public double[] getResults(double u, double v, double[] results) throws FunctionEvaluationException {
        u = (u + 1) / 2;
        v = (v + 1) / 2;
//matlab 相关代码：   
//    syms x1 x2 y1 y2 x3 x4 y3 y4 u v
//xu1=x1+(x2-x1)*u;
//yu1=y1+(y2-y1)*u;
//xu2=x4+(x3-x4)*u;
//yu2=y4+(y3-y4)*u;
//xv1=x1+(x4-x1)*v;
//yv1=y1+(y4-y1)*v;
//xv2=x2+(x3-x2)*v;
//yv2=y2+(y3-y2)*v;
//dxu=xu2-xu1;
//dyu=yu2-yu1;
//dxv=xv2-xv1;
//dyv=yv2-yv1;
//k=((xv1-xu1)*dyv+(yu1-yv1)*dxv)/(dxu*dyv-dxv*dyu);
//x=xu1+k*dxu;
//y=yu1+k*dyu;
//x=simple(x);
//y=simple(y);
//x
//y
//
//dfxu=simple(diff(x,u))
//dfxv=simple(diff(x,v))
//dfyu=simple(diff(y,u))
//dfyv=simple(diff(y,v))
        results[0] = x1 + u * x2 - u * x1 + v * x4 + v * u * x3 - v * u * x4 - v * x1 - v * u * x2 + v * u * x1;
        results[1] = y1 + u * y2 - u * y1 + v * y4 + v * u * y3 - v * u * y4 - v * y1 - v * u * y2 + v * u * y1;
        double dfxu = x2 + v * x3 - v * x2 - x1 - v * x4 + v * x1;
        double dfxv =x4 + u * x3 - u * x4 - x1 - u * x2 + u * x1;
        double dfyu = y2 + v * y3 - v * y2 - y1 - v * y4 + v * y1;
        double dfyv =y4 + u * y3 - u * y4 - y1 - u * y2 + u * y1;
        results[2]=abs(dfxu*dfyv-dfxv*dfyu)/4;
        return results;

    }
}
