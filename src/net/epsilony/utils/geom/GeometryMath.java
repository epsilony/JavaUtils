/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.utils.geom;

import static java.lang.Math.sqrt;

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
     *
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
     * 相当于：(x2-x1,y2-y1)x(x3-x1,y3-y1)</br> 1,2,3点逆时针时为正值，否则为负
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param x3
     * @param y3
     * @return
     */
    public static double pt3Cross2D(double x1, double y1, double x2, double y2, double x3, double y3) {
        return x1 * y2 + x2 * y3 + x3 * y1 - x1 * y3 - x2 * y1 - x3 * y2;
    }

    public static Coordinate cross(Coordinate c1, Coordinate c2){
        return cross(c1,c2,null);
    }
    
    public static Coordinate cross(Coordinate c1, Coordinate c2, Coordinate result) {
        if (null == result) {
            result = new Coordinate();
        }
        result.x = c1.y * c2.z - c1.z * c2.x;
        result.y = c1.z * c2.x - c1.x * c2.z;
        result.z = c1.x * c2.y - c1.y * c2.x;

        return result;
    }

    public static Coordinate cross(double x1, double y1, double z1, double x2, double y2, double z2){
        return cross(x1,y1,z1,x2,y2,z2,null);
    }
    
    public static Coordinate cross(double x1, double y1, double z1, double x2, double y2, double z2, Coordinate result) {
        if (null == result) {
            result = new Coordinate();
        }
        result.x = y1 * z2 - z1 * x2;
        result.y = z1 * x2 - x1 * z2;
        result.z = x1 * y2 - y1 * x2;

        return result;
    }

    public static Coordinate normalize(Coordinate coord) {
        double norm = coord.x * coord.x + coord.y * coord.y + coord.z * coord.z;
        norm = sqrt(norm);
        coord.x /= norm;
        coord.y /= norm;
        coord.z /= norm;
        return coord;
    }

    public static double distanceSquare(Coordinate c1, Coordinate c2) {
        double dx, dy, dz;
        dx = c2.x - c1.x;
        dy = c2.y - c1.y;
        dz = c2.z - c1.z;
        return dx * dx + dy * dy + dz * dz;
    }

    public static double distance(Coordinate c1, Coordinate c2) {
        double dx, dy, dz;
        dx = c1.x - c2.x;
        dy = c1.y - c2.y;
        dz = c1.z - c2.z;
        return sqrt(dx * dx + dy * dy + dz * dz);
    }
    
    public static Coordinate minus(Coordinate c1, Coordinate c2){
        return minus(c1,c2,null);
    }

    public static Coordinate minus(Coordinate c1, Coordinate c2, Coordinate result) {
        if (null == result) {
            result = new Coordinate();
        }
        result.x = c1.x - c2.x;
        result.y = c1.y - c2.y;
        result.z = c1.z - c2.z;
        return result;
    }

    public static Coordinate add(Coordinate c1, Coordinate c2){
        return add(c1,c2,null);
    }
    
    public static Coordinate add(Coordinate c1, Coordinate c2, Coordinate result) {
        if (null == result) {
            result = new Coordinate();
        }
        result.x = c1.x + c2.x;
        result.y = c1.y + c2.y;
        result.z = c1.z + c2.z;
        return result;
    }
    
    public static Coordinate linearCombine(Coordinate c1,double s1,Coordinate c2,double s2){
        return linearCombine(c1,s1,c2,s2,null);
    }
    
    public static Coordinate linearCombine(Coordinate c1,double s1,Coordinate c2,double s2,Coordinate result){
        if(null==result){
            result=new Coordinate();
        }
        result.x=c1.x*s1+c2.x*s2;
        result.y=c1.y*s1+c2.y*s2;
        result.z=c1.z*s1+c2.z*s2;
        return result;
    }

    public static double dot(Coordinate c1, Coordinate c2) {
        return c1.x * c2.x + c1.y * c2.y + c1.z * c2.z;
    }
    
    public static double cross2D(double x1,double y1,double x2,double y2){
        return x1*y2-y1*x2;
    }
    
    public static double cross2D(Coordinate c1,Coordinate c2){
        return cross2D(c1.x,c1.y,c2.x,c2.y);
    }
    
    public static Coordinate scale(Coordinate c,double s,Coordinate result){
        if(null==result){
            result=new Coordinate(c);
        }else{
            result.setDim(result);
        }
        result.scale(s);
        return result;
    }
    
    public static Coordinate scale(Coordinate c,double s){
        return scale(c,s,null);
    }
    
}
