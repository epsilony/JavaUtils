/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.utils.math;

import static java.lang.Math.abs;
import net.epsilony.utils.geom.Coordinate;
import net.epsilony.utils.geom.Quadrangle;

/**
 * {@link http://epsilony/mywiki/Math/GaussQuadrature}
 *
 * @author epsilonyuan@gmail.com
 */
public class QuadrangleMapper extends Quadrangle implements BivariateMapper {

    /**
     * 边与x,y轴平形的矩形映射顶点，这个矩形区域为{(x,y)|x&isin;[x<sub>min</sub>,x<sub>max</sub>],x&isin;[y<sub>min</sub>,y<sub>max</sub>]}
     *
     * @param xmin x<sub>min</sub>
     * @param ymin y<sub>min</sub>
     * @param xmax x<sub>max</sub>
     * @param ymin y<sub>max</sub>
     */
    public void setVertes(double xmin, double ymin, double xmax, double ymax) {
        setVertes(xmin,ymin,ymin,xmax,xmax,ymax,xmin,ymax);
    }

    @Override
    public double[] getResults(double iu, double iv, double[] results) {
        return iuv2xyA(getXY(0),getXY(1),getXY(2),getXY(3),getXY(4),getXY(5),getXY(6),getXY(7), iu, iv, results);
    }

    public static double[] iuv2xyA(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4, double iu, double iv, double[] results) {
        if (null == results) {
            results = new double[3];
        }
        double u = (iu + 1) / 2;
        double v = (iv + 1) / 2;

        double uv = u * v;
        double tx = x1 - x2 + x3 - x4;
        double ty = y1 - y2 + y3 - y4;
        results[0] = tx * uv + (x2 - x1) * u + (x4 - x1) * v + x1;     //x
        results[1] = ty * uv + (y2 - y1) * u + (y4 - y1) * v + y1;    //y
        if (results.length > 2) {
            double dxdu = (tx * v + (x2 - x1)) / 2;
            double dxdv = (tx * u + (x4 - x1)) / 2;
            double dydu = (ty * v + (y2 - y1)) / 2;
            double dydv = (ty * u + (y4 - y1)) / 2;
            results[2] = abs(dxdu * dydv - dydu * dxdv);     //|Jacobi|
        }
        return results;
    }

    public static double iuv2xyC(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4, double iu, double iv, Coordinate coord) {

        double[] results=iuv2xyA(x1, y1, x2, y2, x3, y3, x4, y4, iu, iv, null);
        double jacobi=results[2];
        coord.x=results[0];
        coord.y=results[1];
        return jacobi;
    }

    public static double iuv2xyC(Quadrangle quad, double iu, double iv, Coordinate result) {
        double x1 = quad.getXY(0), y1 = quad.getXY(1), x2 = quad.getXY(2), y2 = quad.getXY(3), x3 = quad.getXY(4), y3 = quad.getXY(5), x4 = quad.getXY(6), y4 = quad.getXY(7);
        return iuv2xyC(x1, y1, x2, y2, x3, y3, x4, y4, iu, iv, result);
    }

    public static double[] iuv2xyA(Quadrangle quad, double iu, double iv, double[] results) {
        double x1 = quad.getXY(0), y1 = quad.getXY(1), x2 = quad.getXY(2), y2 = quad.getXY(3), x3 = quad.getXY(4), y3 = quad.getXY(5), x4 = quad.getXY(6), y4 = quad.getXY(7);
        return iuv2xyA(x1, y1, x2, y2, x3, y3, x4, y4, iu, iv, results);
    }

    public static double jacobi(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4, double iu, double iv) {
        double u = (iu + 1) / 2;
        double v = (iv + 1) / 2;
        double uv = u * v;
        double tx = x1 - x2 + x3 - x4;
        double ty = y1 - y2 + y3 - y4;
        double dxdu = (tx * v + (x2 - x1)) / 2;
        double dxdv = (tx * u + (x4 - x1)) / 2;
        double dydu = (ty * v + (y2 - y1)) / 2;
        double dydv = (ty * u + (y4 - y1)) / 2;
        return abs(dxdu * dydv - dydu * dxdv);     //|Jacobi|
    }

    public static double jacobi(Quadrangle quad, double iu, double iv) {
        double x1 = quad.getXY(0), y1 = quad.getXY(1), x2 = quad.getXY(2), y2 = quad.getXY(3), x3 = quad.getXY(4), y3 = quad.getXY(5), x4 = quad.getXY(6), y4 = quad.getXY(7);
        return jacobi(x1, y1, x2, y2, x3, y3, x4, y4, iu, iv);
    }
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
}
