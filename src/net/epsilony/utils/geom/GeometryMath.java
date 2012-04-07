/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.geom;

/**
 *
 * @author epsilonyuan@gmail.com
 */
public class GeometryMath {

    public static double triangleArea(double x1, double y1, double x2, double y2, double x3, double y3) {
        return 0.5 * Math.abs(x1 * y2 + x2 * y3 + x3 * y1 - x1 * y3 - x2 * y1 - x3 * y2);
    }

    public static double tetrahedronVolume(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3, double x4, double y4, double z4) {
        double t1 = x2 * y3 * z4 + y2 * z3 * x4 + z2 * x3 * y4 - x2 * z3 * y4 - y2 * x3 * z4 - z2 * y3 * x4;
        double t2 = -x1 * y3 * z4 - y1 * z3 * x4 - z1 * x3 * y4 + x1 * z3 * y4 + y1 * x3 * z4 + z1 * y3 * x4;
        double t3 = x1 * y2 * z4 + y1 * z2 * x4 + z1 * x2 * y4 - x1 * z2 * y4 - y1 * x2 * z4 - z1 * y2 * z4;
        double t4 = -x1 * y2 * z3 - y1 * z2 * x3 - z1 * x2 * y3 + x1 * z2 * y3 + y1 * x2 * z3 + z1 * y2 * z3;
        return Math.abs(t1 + t2 + t3 + t4) / 6;
    }

    /**
     * 相当于[(x2-x1,y2-y1,z2-z1)x(x3-x1,y3-y1,z3-z1)]dot(x4-x1,y4-x1,z4-z1)</br>
     * 当点4在1,2,3点右手系正方向上时为正值，否则为负值
     * @param x1
     * @param y1
     * @param z1
     * @param x2
     * @param y2
     * @param z2
     * @param x3
     * @param y3
     * @param z3
     * @param x4
     * @param y4
     * @param z4
     * @return 
     */
    public static double tripleProduct(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3, double x4, double y4, double z4) {
        double t1 = x2 * y3 * z4 + y2 * z3 * x4 + z2 * x3 * y4 - x2 * z3 * y4 - y2 * x3 * z4 - z2 * y3 * x4;
        double t2 = -x1 * y3 * z4 - y1 * z3 * x4 - z1 * x3 * y4 + x1 * z3 * y4 + y1 * x3 * z4 + z1 * y3 * x4;
        double t3 = x1 * y2 * z4 + y1 * z2 * x4 + z1 * x2 * y4 - x1 * z2 * y4 - y1 * x2 * z4 - z1 * y2 * z4;
        double t4 = -x1 * y2 * z3 - y1 * z2 * x3 - z1 * x2 * y3 + x1 * z2 * y3 + y1 * x2 * z3 + z1 * y2 * z3;
        return t1 + t2 + t3 + t4;
    }

    /**
     * 相当于：(x2-x1,y2-y1)x(x3-x1,y3-y1)</br>
     * 1,2,3点逆时针时为正值，否则为负
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param x3
     * @param y3
     * @return 
     */
    public static double crossProduct(double x1, double y1, double x2, double y2, double x3, double y3) {
        return x1 * y2 + x2 * y3 + x3 * y1 - x1 * y3 - x2 * y1 - x3 * y2;
    }
    
}
