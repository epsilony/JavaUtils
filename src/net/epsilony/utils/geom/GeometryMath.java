/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.utils.geom;

import static java.lang.Math.sqrt;

/**
 * @version 20120529-1
 * @author epsilonyuan@gmail.com
 */
public class GeometryMath {

    public static double triangleArea2D(double x1, double y1, double x2, double y2, double x3, double y3) {
        return 0.5 * Math.abs(pt3Cross2D(x1, y1, x2, y2, x3, y3));
    }

    public static double tetrahedronVolume(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3, double x4, double y4, double z4) {
        return Math.abs(tripleProduct(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4)) / 6;
    }

    /**
     * [(x2-x1,y2-y1,z2-z1)x(x3-x1,y3-y1,z3-z1)]dot(x4-x1,y4-x1,z4-z1)</br>
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
        double xc1 = x2 - x1;
        double yc1 = y2 - y1;
        double zc1 = z2 - z1;
        double xc2 = x3 - x1;
        double yc2 = y3 - y1;
        double zc2 = z3 - z1;
        double x = yc1 * zc2 - zc1 * yc2;
        double y = zc1 * xc2 - xc1 * zc2;
        double z = xc1 * yc2 - yc1 * xc2;
        return x * (x4 - x1) + y * (y4 - y1) + z * (z4 - z1);
    }

    /**
     * [(c2-c1)x(c3-c1)]dot(c4-c1)]
     *
     * @param c1
     * @param c2
     * @param c3
     * @param c4
     * @return
     */
    public static double tripleProduct(Coordinate c1, Coordinate c2, Coordinate c3, Coordinate c4) {
        return tripleProduct(c1.x, c1.y, c1.z, c2.x, c2.y, c2.z, c3.x, c3.y, c3.z, c4.x, c4.y, c4.z);
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

    public static Coordinate cross(Coordinate c1, Coordinate c2) {
        return cross(c1, c2, null);
    }

    public static Coordinate cross(Coordinate c1, Coordinate c2, Coordinate result) {


        return cross(c1.x, c1.y, c1.z, c2.x, c2.y, c2.z, result);
    }

    public static Coordinate cross(double x1, double y1, double z1, double x2, double y2, double z2) {
        return cross(x1, y1, z1, x2, y2, z2, null);
    }

    public static Coordinate cross(double x1, double y1, double z1, double x2, double y2, double z2, Coordinate result) {
        if (null == result) {
            result = new Coordinate();
        }
        result.x = y1 * z2 - z1 * y2;
        result.y = z1 * x2 - x1 * z2;
        result.z = x1 * y2 - y1 * x2;

        return result;
    }

    public static Coordinate normalize(Coordinate coord) {
        double norm = dot(coord, coord);
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

        return sqrt(distanceSquare(c1, c2));
    }

    public static Coordinate minus(Coordinate c1, Coordinate c2) {
        return minus(c1, c2, null);
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

    public static Coordinate add(Coordinate c1, Coordinate c2) {
        return add(c1, c2, null);
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

    public static Coordinate linearCombine(Coordinate c1, double s1, Coordinate c2, double s2) {
        return linearCombine(c1, s1, c2, s2, null);
    }

    public static Coordinate linearCombine(Coordinate c1, double s1, Coordinate c2, double s2, Coordinate result) {
        if (null == result) {
            result = new Coordinate();
        }
        result.x = c1.x * s1 + c2.x * s2;
        result.y = c1.y * s1 + c2.y * s2;
        result.z = c1.z * s1 + c2.z * s2;
        return result;
    }

    public static double dot(Coordinate c1, Coordinate c2) {
        return dot(c1.x, c1.y, c1.z, c2.x, c2.y, c2.z);
    }

    public static double dot(double x, double y, double z, Coordinate c) {
        return dot(x, y, z, c.x, c.y, c.z);
    }

    public static double dot(double x1, double y1, double z1, double x2, double y2, double z2) {
        return x1 * x2 + y1 * y2 + z1 * z2;
    }

    public static double cross2D(double x1, double y1, double x2, double y2) {
        return x1 * y2 - y1 * x2;
    }

    public static double cross2D(Coordinate c1, Coordinate c2) {
        return cross2D(c1.x, c1.y, c2.x, c2.y);
    }

    public static Coordinate scale(Coordinate c, double s, Coordinate result) {
        if (null == result) {
            result = new Coordinate(c);
        } else {
            result.set(c);
        }
        result.scale(s);
        return result;
    }

    public static Coordinate scale(Coordinate c, double s) {
        return scale(c, s, null);
    }

    public static boolean isLineSegment2DIntersect(Coordinate start1, Coordinate end1, Coordinate start2, Coordinate end2) {
        return isLineSegment2DIntersect(start1.x, start1.y, end1.x, end1.y, start2.x, start2.y, end2.x, end2.y);
    }

    /**
     * http://www.softsurfer.com/Archive/algorithm_0104/algorithm_0104B.htm#Line
     * Intersections
     */
    public static boolean isLineSegment2DIntersect(double xs1, double ys1, double xe1, double ye1, double xs2, double ys2, double xe2, double ye2) {
        double u1 = xe1 - xs1;
        double u2 = ye1 - ys1;
        double v1 = xe2 - xs2;
        double v2 = ye2 - ys2;
        double w1 = xs2 - xs1;
        double w2 = ys2 - ys1;
        double denorm = v1 * u2 - v2 * u1;
        if (0 == denorm) {// coincident or just parrel  
            if (w1 * u2 - w2 * u1 != 0) {
                return false;
            }
            double d1 = u1;
            double d2 = w1;
            double d3 = xe2 - xs1;
            if (d1 == 0) {
                d1 = u2;
                d2 = w2;
                d3 = ye2 - ys1;
            }
            double t = d2 / d1;
            if (t <= 1 && t >= 0) {
                return true;
            }
            t = d3 / d1;
            if (t <= 1 && t >= 0) {
                return true;
            }
            if (d2 * d3 < 0) {
                return true;
            }
            return false;

        }
        double t1 = -(v2 * w1 - v1 * w2) / denorm;
        double t2 = (u1 * w2 - u2 * w1) / denorm;
        if (t1 < 0 || t1 > 1 || t2 < 0 || t2 > 1) {
            return false;
        }
        return true;
    }
    public static final int DISDROINT = 0;
    public static final int INTERSECT = 1;
    public static final int COINCIDENT = 2;
    public static int lineSegmentTriangleIntersection(Coordinate start,Coordinate end,Triangle tri,Coordinate normal){
        return lineSegmentTriangleIntersection(start, end, tri.c1, tri.c2, tri.c3, normal);
    }
    
    public static int lineSegmentTriangleIntersection(Coordinate start, Coordinate end, Coordinate c1,Coordinate c2,Coordinate c3, Coordinate normal) {
        Coordinate interPt = new Coordinate();
        Coordinate u = null, v = null;
        if (null == normal) {
            u = minus(c2, c1);
            v = minus(c3, c1);
            normal = cross(u, v);
        }
        int intersec = lineSegmentPlaneIntersection(start, end, normal, c1, interPt);
        if (intersec == DISDROINT) {
            return DISDROINT;
        }
        if (null == u) {
            u = minus(c2, c1);
            v = minus(c3, c1);
        }
        if (intersec == COINCIDENT) {
            if (isPtInTriangle(start, c1,c2,c3, u, v) || isPtInTriangle(end, c1,c2,c3, u, v)) {
                return INTERSECT;
            }
            Coordinate project1=u;
            Coordinate project2=cross(normal,u);
            boolean b1=isLineProjectionIntersect(start, end, c1, c2, project1, project2);
            boolean b2=isLineProjectionIntersect(start, end, c2, c3, project1, project2);
            boolean b3=isLineProjectionIntersect(start, end, c3, c1, project1, project2);
            if(b1||b2||b3){
                return INTERSECT;
            }
            return DISDROINT;
        }
        if (isPtInTriangle(interPt, c1,c2,c3, u, v)) {
            return INTERSECT;
        } else {
            return DISDROINT;
        }
    }

    public static boolean isLineProjectionIntersect(Coordinate start1, Coordinate end1, Coordinate start2, Coordinate end2, Coordinate project1, Coordinate project2) {
        double xs1 = dot(start1, project1);
        double ys1 = dot(start1, project2);
        double xe1 = dot(end1, project1);
        double ye1 = dot(end1, project2);
        double xs2 = dot(start2, project1);
        double ys2 = dot(start2, project2);
        double xe2 = dot(end2, project1);
        double ye2 = dot(end2, project2);
        return isLineSegment2DIntersect(xs1, ys1, xe1, ye1, xs2, ys2, xe2, ye2);
    }
    
    public static boolean isPtInTriangle(Coordinate pt,Triangle tri,Coordinate u, Coordinate v){
        return isPtInTriangle(pt, tri.c1, tri.c2, tri.c3, u, v);
    }
    
    public static boolean isPtInTriangle(Coordinate pt, Coordinate c1,Coordinate c2,Coordinate c3, Coordinate u, Coordinate v) {
        if (u == null || v == null) {
            u = minus(c2, c1);
            v = minus(c3, c1);
        }
        Coordinate w = minus(pt, c1);
        double uv = dot(u, v);
        double wv = dot(w, v);
        double vv = dot(v, v);
        double wu = dot(w, u);
        double uu = dot(u, u);
        double denorm = uv * uv - uu * vv;
        double s = (uv * wv - vv * wu) / denorm;
        double t = (uv * wu - uu * wv) / denorm;
        if (s >= 0 && s <= 1 && t >= 0 && t <= 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * (c1xc2).c3
     *
     * @param c1
     * @param c2
     * @param c3
     * @return
     */
    public static double crossDot(Coordinate c1, Coordinate c2, Coordinate c3) {
        double cx = c1.y * c2.z - c1.z * c2.y;
        double cy = c1.z * c2.x - c1.x * c2.z;
        double cz = c1.x * c2.y - c1.y * c2.x;
        return cx * c3.x + cy * c3.y + cz * c3.z;
    }

    /**
     * http://www.softsurfer.com/Archive/algorithm_0104/algorithm_0104B.htm#Line-Plane
     * Intersection
     *
     * @param start
     * @param end
     * @param normal
     * @param pt
     * @param outInterPt
     * @return
     */
    public static int lineSegmentPlaneIntersection(Coordinate start, Coordinate end, Coordinate normal, Coordinate pt, Coordinate outInterPt) {
        double d1 = dot(end.x - start.x, end.y - start.y, end.z - start.z, normal);
        double d2 = dot(pt.x - start.x, pt.y - start.y, pt.z - start.z, normal);
        if (d1 == 0) {
            if (d2 == 0) {
                return COINCIDENT;
            } else {
                return DISDROINT;
            }
        }
        double t = d2 / d1;
        if (t < 0 || t > 1) {
            return DISDROINT;
        } else {
            if (null != outInterPt) {
                outInterPt.x = (end.x - start.x) * t + start.x;
                outInterPt.y = (end.y - start.y) * t + start.y;
                outInterPt.z = (end.z - start.z) * t + start.z;
            }
            return INTERSECT;
        }
    }

    public static double triangleArea2D(Triangle tri) {
        return triangleArea2D(tri.c1.x,tri.c1.y,tri.c2.x,tri.c2.y,tri.c3.x,tri.c3.y);
    }
}
