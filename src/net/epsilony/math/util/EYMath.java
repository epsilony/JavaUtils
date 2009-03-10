/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.math.util;

import org.apache.commons.math.complex.Complex;
import static java.lang.Math.*;

/**
 * 自建的工具类，用以补充java.math中缺的静态函数
 * @author epsilon
 */
public class EYMath {

    /**
     * 求排列
     * @param n
     * @param m
     * @return P^m_n = n!/(n-m)!
     */
    public static long permutation(int n, int m) {
        long p = 1;
        for (int i = n - m + 1; i <= n; i++) {
            p *= i;
        }
        return p;
    }

    /**
     * 设两正放矩形域对角线为(x1,y1)-(x2,y2),(x3,y3)-(x4,y4)求它们是否有交集
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param x3
     * @param y3
     * @param x4
     * @param y4
     * @return true 有交集 false 无交集
     */
    public static boolean isRectangleIntersect(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
        if (((x3 - x1) * (x3 - x2) <= 0 || (x4 - x1) * (x4 - x2) <= 0 || (x1 - x3) * (x1 - x4) <= 0 || (x2 - x3) * (x2 - x4) <= 0) && ((y3 - y1) * (y3 - y2) <= 0 || (y4 - y1) * (y4 - y2) <= 0 || (y1 - y3) * (y1 - y4) <= 0 || (y2 - y3) * (y2 - y4) <= 0)) {
            return true;
        } else {
            return false;
        }
    }

    public static double vectorProduct(double x1, double y1, double x2, double y2) {
        return x1 * y2 - x2 * y1;
    }

    public static double lineSegmentJump(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
        return vectorProduct(x1 - x2, y1 - y2, x3 - x4, y3 - y4);
    }

    public static boolean isLineSegmentIntersect(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
        if (isRectangleIntersect(x1, y1, x2, y2, x3, y3, x4, y4)) {
            if (lineSegmentJump(x1, y1, x2, y2, x3, y3, x4, y4) <= 0) {
                return true;
            }
        }
        return false;
    }

    public static Complex[] solveCubicEquation(double[] effs, Complex[] result) {

        return solveCubicEquation(effs[0], effs[1], effs[2], effs[3], result);

    }

    public static Complex[] solveCubicEquation(double d, double c, double b, double a, Complex[] result) {
        double t1 = 36 * c * b * a - 108 * d * a * a - 8 * b * b * b + 12 * a * sqrt(12 * c * c * c * a - 3 * c * c * b * b - 54 * c * b * a * d + 81 * d * d * a * a + 12 * d * b * b * b);
        double t2 = -pow(t1, 2 / 3) + 12 * c * a - 4 * b * b;
        double t3 = -t2 + 4 * b * pow(t1, 1 / 3);
        double t4 = -1 / 12 / a / pow(t1, 1 / 3);
        result[0] = new Complex(-1 / 6 * (t2 / pow(t1, 1 / 3) + 2 * b) / a, 0);
        result[1] = new Complex(t4 * t3, -t4 * sqrt(3) * t2);
        result[2] = new Complex(t4 * t3, t4 * sqrt(3) * t2);
        return result;
    }

    /**
     * A classic Bezier Curve Agorithm Implementation
     * @param degree
     * @param t
     * @param ctrlPts
     * @param result
     * @return
     */
    public static double[] bezierPoint(int degree, double t, double ctrlPts[], double results[]) {
        throw new UnsupportedOperationException();
    }

    public static double[] cubicBezierPoint(double t, double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4, double[] results) {
        double tx1 = x1 * (1 - t) + x2 * t;
        double ty1 = y1 * (1 - t) + y2 * t;
        double tx2 = x2 * (1 - t) + x3 * t;
        double ty2 = y2 * (1 - t) + y3 * t;
        double tx3 = x3 * (1 - t) + x4 * t;
        double ty3 = y3 * (1 - t) + y4 * t;
        tx1 = tx1 * (1 - t) + tx2 * t;
        ty1 = ty1 * (1 - t) + ty2 * t;
        tx2 = tx2 * (1 - t) + tx3 * t;
        ty2 = ty2 * (1 - t) + ty3 * t;
        tx1 = tx1 * (1 - t) + tx2 * t;
        ty1 = ty1 * (1 - t) + ty2 * t;
        results[0] = tx1;
        results[1] = ty1;
        System.out.println(tx1 + " " + ty1);
        return results;
    }

    public static double[] cubicBezierPoint(double t, double ctrlPts[], double results[]) {
        double tx1 = ctrlPts[0] * (1 - t) + ctrlPts[2] * t;
        double ty1 = ctrlPts[1] * (1 - t) + ctrlPts[3] * t;
        double tx2 = ctrlPts[2] * (1 - t) + ctrlPts[4] * t;
        double ty2 = ctrlPts[3] * (1 - t) + ctrlPts[5] * t;
        double tx3 = ctrlPts[4] * (1 - t) + ctrlPts[6] * t;
        double ty3 = ctrlPts[5] * (1 - t) + ctrlPts[7] * t;
        tx1 = tx1 * (1 - t) + tx2 * t;
        ty1 = ty1 * (1 - t) + ty2 * t;
        tx2 = tx2 * (1 - t) + tx3 * t;
        ty2 = ty2 * (1 - t) + ty3 * t;
        tx1 = tx1 * (1 - t) + tx2 * t;
        ty1 = ty1 * (1 - t) + ty2 * t;
        results[0] = tx1;
        results[1] = ty1;
        System.out.println(tx1 + " " + ty1);
        return results;
    }

    /**
     * 将一个三阶Bezier曲线分成等效的两半，前半的控制点数据存在ctrlPts中，后半部分的放在ctrlPts2中
     * @param ctrlPts IN：原曲线控制点坐标 Out分成两半后的前半个曲线控制点坐柡
     * @param ctrlPts2 Out:分成两半后的后半个曲线控制坐柡。
     */
    public static double[] halfDivideCubicBezierCurve(double ctrlPts[], double ctrlPts2[]) {
        ctrlPts2[6] = ctrlPts[6];
        ctrlPts2[7] = ctrlPts[7];

        ctrlPts2[4] = (ctrlPts[4] + ctrlPts2[6]) / 2;
        ctrlPts2[5] = (ctrlPts[5] + ctrlPts2[7]) / 2;



        double px = (ctrlPts[2] + ctrlPts[4]) / 2;
        double py = (ctrlPts[3] + ctrlPts[5]) / 2;

        ctrlPts[2] = (ctrlPts[2] + ctrlPts[0]) / 2;
        ctrlPts[3] = (ctrlPts[3] + ctrlPts[1]) / 2;

        ctrlPts[4] = (px + ctrlPts[2]) / 2;
        ctrlPts[5] = (py + ctrlPts[3]) / 2;

        ctrlPts2[2] = (px + ctrlPts2[4]) / 2;
        ctrlPts2[3] = (py + ctrlPts2[5]) / 2;

        ctrlPts[6] = ctrlPts2[0] = (ctrlPts[4] + ctrlPts2[2]) / 2;
        ctrlPts[7] = ctrlPts2[1] = (ctrlPts[5] + ctrlPts2[3]) / 2;

        return ctrlPts2;
    }

    public static double cubicBezierCurveFlatnessSqr(double[] ctrlPts) {
        double dis1 = linePointDistanceSqr(ctrlPts[0], ctrlPts[1], ctrlPts[6], ctrlPts[7], ctrlPts[2], ctrlPts[3]);
        double dis2 = linePointDistanceSqr(ctrlPts[0], ctrlPts[1], ctrlPts[6], ctrlPts[7], ctrlPts[4], ctrlPts[5]);
        return dis1 > dis2 ? dis1 : dis2;
    }

    /**
     * 点（px,py)到直线(x1,y1)-(x2,y2)的距离的平方。
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param px
     * @param py
     * @return
     */
    public static double linePointDistanceSqr(double x1, double y1, double x2, double y2, double px, double py) {
        double x21 = x2 - x1;
        double y21 = y2 - y1;
        double x10 = x1 - px;
        double y10 = y1 - py;
        double a = x21 * x21 + y21 * y21;
        double b = (x21 * x10 + y21 * y10) * 2;
        double c = x10 * x10 + y10 * y10;
        return -b * b / 4 / a + c;
    }
}
