/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.util;

import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import static java.lang.Math.*;
import net.epsilony.util.ui.geom.ShapeUtils;

import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.ListIterator;

/*
 * Conrec.java
 *
 * Created on 5 August 2001, 15:03
 *
 * Copyright (c) 1996-1997 Nicholas Yue
 *
 * This software is copyrighted by Nicholas Yue. This code is base on the work of
 * Paul D. Bourke CONREC.F routine
 *
 * The authors hereby grant permission to use, copy, and distribute this
 * software and its documentation for any purpose, provided that existing
 * copyright notices are retained in all copies and that this notice is included
 * verbatim in any distributions. Additionally, the authors grant permission to
 * modify this software and its documentation for any purpose, provided that
 * such modifications are not distributed without the explicit consent of the
 * authors and that existing copyright notices are retained in all copies. Some
 * of the algorithms implemented by this software are patented, observe all
 * applicable patent law.
 *
 * IN NO EVENT SHALL THE AUTHORS OR DISTRIBUTORS BE LIABLE TO ANY PARTY FOR
 * DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES ARISING OUT
 * OF THE USE OF THIS SOFTWARE, ITS DOCUMENTATION, OR ANY DERIVATIVES THEREOF,
 * EVEN IF THE AUTHORS HAVE BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * THE AUTHORS AND DISTRIBUTORS SPECIFICALLY DISCLAIM ANY WARRANTIES, INCLUDING,
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, AND NON-INFRINGEMENT.  THIS SOFTWARE IS PROVIDED ON AN
 * "AS IS" BASIS, AND THE AUTHORS AND DISTRIBUTORS HAVE NO OBLIGATION TO PROVIDE
 * MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR MODIFICATIONS.
 */
/**
 * Conrec a straightforward method of contouring some surface represented a regular
 * triangular mesh.
 *
 * Ported from the C++ code by Nicholas Yue (see above copyright notice).
 * @see http://astronomy.swin.edu.au/pbourke/projection/conrec/ for full description
 * of code and original C++ source.
 *
 * @author  Bradley White
 * @version 1.0
 */
public class Conrec {

    private double[] h = new double[5];
    private int[] sh = new int[5];
    private double[] xh = new double[5];
    private double[] yh = new double[5];
    double[] levels;

    // Object that knows how to draw the contour
    // private Render render = null;
    /** Creates new Conrec */
//    public  Conrec(Render render) throws Exception {
//        if (render == null){
//            throw new Exception ("Render null");
//        }
//        this.render = render;
//    }
    /**
     *     contour is a contouring subroutine for rectangularily spaced data
     *
     *     It emits calls to a line drawing subroutine supplied by the user
     *     which draws a contour map corresponding to real*4data on a randomly
     *     spaced rectangular grid. The coordinates emitted are in the same
     *     units given in the x() and y() arrays.
     *
     *     Any number of contour levels may be specified but they must be
     *     in order of increasing value.
     *
     *
     * @param d  - matrix of data to contour
     * @param ilb,iub,jlb,jub - index bounds of data matrix
     *
     *             The following two, one dimensional arrays (x and y) contain the horizontal and
     *             vertical coordinates of each sample points.
     * @param x  - data matrix column coordinates
     * @param y  - data matrix row coordinates
     * @param nc - number of contour levels
     * @param z  - contour levels in increasing order.
     *
     */
    public void contour(double[][] d, int ilb, int iub, int jlb, int jub, double[] x, double[] y, int nc, double[] z) {
        levelLines = new ContourLevelLines[nc];
        for (int i = 0; i < nc; i++) {
            levelLines[i] = new ContourLevelLines();
            levelLines[i].level = z[i];
        }
        levels = Arrays.copyOfRange(z, 0, nc);

        int m1;
        int m2;
        int m3;
        int case_value;
        double dmin;
        double dmax;
        double x1 = 0.0;
        double x2 = 0.0;
        double y1 = 0.0;
        double y2 = 0.0;
        int i, j, k, m;

        // The indexing of im and jm should be noted as it has to start from zero
        // unlike the fortran counter part
        int[] im = {0, 1, 1, 0};
        int[] jm = {0, 0, 1, 1};

        // Note that castab is arranged differently from the FORTRAN code because
        // Fortran and C/C++ arrays are transposed of each other, in this case
        // it is more tricky as castab is in 3 dimension
        int[][][] castab = {
            {
                {0, 0, 8}, {0, 2, 5}, {7, 6, 9}
            },
            {
                {0, 3, 4}, {1, 3, 1}, {4, 3, 0}
            },
            {
                {9, 6, 7}, {5, 2, 0}, {8, 0, 0}
            }
        };

        for (j = (jub - 1); j >= jlb; j--) {
            for (i = ilb; i <= iub - 1; i++) {
                double temp1, temp2;
                temp1 = Math.min(d[i][j], d[i][j + 1]);
                temp2 = Math.min(d[i + 1][j], d[i + 1][j + 1]);
                dmin = Math.min(temp1, temp2);
                temp1 = Math.max(d[i][j], d[i][j + 1]);
                temp2 = Math.max(d[i + 1][j], d[i + 1][j + 1]);
                dmax = Math.max(temp1, temp2);

                if (dmax >= z[0] && dmin <= z[nc - 1]) {
                    for (k = 0; k < nc; k++) {
                        if (z[k] >= dmin && z[k] <= dmax) {
                            for (m = 4; m >= 0; m--) {
                                if (m > 0) {
                                    // The indexing of im and jm should be noted as it has to
                                    // start from zero
                                    h[m] = d[i + im[m - 1]][j + jm[m - 1]] - z[k];
                                    xh[m] = x[i + im[m - 1]];
                                    yh[m] = y[j + jm[m - 1]];
                                } else {
                                    h[0] = 0.25 * (h[1] + h[2] + h[3] + h[4]);
                                    xh[0] = 0.5 * (x[i] + x[i + 1]);
                                    yh[0] = 0.5 * (y[j] + y[j + 1]);
                                }
                                if (h[m] > 0.0) {
                                    sh[m] = 1;
                                } else if (h[m] < 0.0) {
                                    sh[m] = -1;
                                } else {
                                    sh[m] = 0;
                                }
                            }
                            //
                            // Note: at this stage the relative heights of the corners and the
                            // centre are in the h array, and the corresponding coordinates are
                            // in the xh and yh arrays. The centre of the box is indexed by 0
                            // and the 4 corners by 1 to 4 as shown below.
                            // Each triangle is then indexed by the parameter m, and the 3
                            // vertices of each triangle are indexed by parameters m1,m2,and
                            // m3.
                            // It is assumed that the centre of the box is always vertex 2
                            // though this isimportant only when all 3 vertices lie exactly on
                            // the same contour level, in which case only the side of the box
                            // is drawn.
                            //
                            //
                            //      vertex 4 +-------------------+ vertex 3
                            //               | \               / |
                            //               |   \    m-3    /   |
                            //               |     \       /     |
                            //               |       \   /       |
                            //               |  m=2    X   m=2   |       the centre is vertex 0
                            //               |       /   \       |
                            //               |     /       \     |
                            //               |   /    m=1    \   |
                            //               | /               \ |
                            //      vertex 1 +-------------------+ vertex 2
                            //
                            //
                            //
                            //               Scan each triangle in the box
                            //
                            for (m = 1; m <= 4; m++) {
                                m1 = m;
                                m2 = 0;
                                if (m != 4) {
                                    m3 = m + 1;
                                } else {
                                    m3 = 1;
                                }
                                case_value = castab[sh[m1] + 1][sh[m2] + 1][sh[m3] + 1];
                                if (case_value != 0) {
                                    switch (case_value) {
                                        case 1: // Line between vertices 1 and 2
                                            x1 = xh[m1];
                                            y1 = yh[m1];
                                            x2 = xh[m2];
                                            y2 = yh[m2];
                                            break;
                                        case 2: // Line between vertices 2 and 3
                                            x1 = xh[m2];
                                            y1 = yh[m2];
                                            x2 = xh[m3];
                                            y2 = yh[m3];
                                            break;
                                        case 3: // Line between vertices 3 and 1
                                            x1 = xh[m3];
                                            y1 = yh[m3];
                                            x2 = xh[m1];
                                            y2 = yh[m1];
                                            break;
                                        case 4: // Line between vertex 1 and side 2-3
                                            x1 = xh[m1];
                                            y1 = yh[m1];
                                            x2 = xsect(m2, m3);
                                            y2 = ysect(m2, m3);
                                            break;
                                        case 5: // Line between vertex 2 and side 3-1
                                            x1 = xh[m2];
                                            y1 = yh[m2];
                                            x2 = xsect(m3, m1);
                                            y2 = ysect(m3, m1);
                                            break;
                                        case 6: //  Line between vertex 3 and side 1-2
                                            x1 = xh[m3];
                                            y1 = yh[m3];
                                            x2 = xsect(m1, m2);
                                            y2 = ysect(m1, m2);
                                            break;
                                        case 7: // Line between sides 1-2 and 2-3
                                            x1 = xsect(m1, m2);
                                            y1 = ysect(m1, m2);
                                            x2 = xsect(m2, m3);
                                            y2 = ysect(m2, m3);
                                            break;
                                        case 8: // Line between sides 2-3 and 3-1
                                            x1 = xsect(m2, m3);
                                            y1 = ysect(m2, m3);
                                            x2 = xsect(m3, m1);
                                            y2 = ysect(m3, m1);
                                            break;
                                        case 9: // Line between sides 3-1 and 1-2
                                            x1 = xsect(m3, m1);
                                            y1 = ysect(m3, m1);
                                            x2 = xsect(m1, m2);
                                            y2 = ysect(m1, m2);
                                            break;
                                        default:
                                            break;
                                    }
                                    // Put your processing code here and comment out the printf
                                    System.out.println(String.format("%f %f %f %f %f", x1, y1, x2, y2, z[k]));
                                    addToContourLines(x1, y1, x2, y2, z[k]);
                                    //  render.drawContour(x1,y1,x2,y2,z[k]);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private double xsect(int p1, int p2) {
        return (h[p2] * xh[p1] - h[p1] * xh[p2]) / (h[p2] - h[p1]);
    }

    private double ysect(int p1, int p2) {
        return (h[p2] * yh[p1] - h[p1] * yh[p2]) / (h[p2] - h[p1]);
    }
    public double eps = 1e-5;

    /**
     * Get the value of esp
     *
     * @return the value of esp
     */
    public double getEsp() {
        return eps;
    }

    /**
     * Set the value of esp
     *
     * @param esp new value of esp
     */
    public void setEsp(double esp) {
        this.eps = esp;
    }

    public static class ContourLine extends LinkedList<Point2D.Double> {

        boolean closed = false;

        ;
    }

    public static class ContourLevelLines extends LinkedList<ContourLine> {
        double level;
    }
    ContourLevelLines[] levelLines = null;

    void addToContourLines(double x1, double y1, double x2, double y2, double level) {
        int key = Arrays.binarySearch(levels, level);
        double epss = eps * eps;
        ContourLevelLines clines = levelLines[key];
        int s = 0;
        ContourLine tcline = null;
        boolean headHot = false;

        ListIterator<ContourLine> clinesIterator = clines.listIterator();
        while (clinesIterator.hasNext()) {
            ContourLine cline = clinesIterator.next();
            if (cline.closed) {
                continue;
            }
            Point2D.Double ptfst = cline.peekFirst();
            Point2D.Double ptlst = cline.peekLast();
            if (s == 0) {
                double dfst1 = pow(ptfst.getX() - x1, 2) + pow(ptfst.getY() - y1, 2);
                double dfst2 = pow(ptfst.getX() - x2, 2) + pow(ptfst.getY() - y2, 2);
                double dlst1 = pow(ptlst.getX() - x1, 2) + pow(ptlst.getY() - y1, 2);
                double dlst2 = pow(ptlst.getX() - x2, 2) + pow(ptlst.getY() - y2, 2);
                if (dfst1 <= epss && dlst2 <= epss || dfst2 <= epss && dlst1 <= epss) {
                    cline.closed = true;
                    s = 2;
                    break;
                }
                if (dfst1 <= epss) {
                    cline.addFirst(new Point2D.Double(x2, y2));
                    headHot = true;
                    tcline = cline;
                    s = 1;
                    continue;
                }
                if (dfst2 <= epss) {
                    cline.addFirst(new Point2D.Double(x1, y1));
                    headHot = true;
                    tcline = cline;
                    s = 1;
                    continue;
                }
                if (dlst1 <= epss) {
                    cline.addLast(new Point2D.Double(x2, y2));
                    headHot = false;
                    tcline = cline;
                    s = 1;
                    continue;
                }
                if (dlst2 <= epss) {
                    cline.addLast(new Point2D.Double(x1, y1));
                    headHot = false;
                    tcline = cline;
                    s = 1;
                    continue;
                }
            }

            if (s == 1) {
                Point2D.Double pt;
                if (headHot) {
                    pt = tcline.getFirst();
                } else {
                    pt = tcline.getLast();
                }
                double dfstp = pow(ptfst.getX() - pt.getX(), 2) + pow(ptfst.getY() - pt.getY(), 2);
                double dlstp = pow(ptlst.getX() - pt.getX(), 2) + pow(ptlst.getY() - ptlst.getY(), 2);
                if (dfstp > epss && dlstp > epss) {
                    continue;
                }
                if (dfstp <= epss) {
                    if (headHot) {
                        for (Point2D.Double tpt : cline) {
                            tcline.addFirst(tpt);
                        }
                    } else {
                        tcline.addAll(cline);
                    }
                    s = 3;
                    clinesIterator.remove();
                    break;
                }
                if (dlstp <= epss) {
                    if (headHot) {
                        tcline.addAll(0, cline);
                    } else {
                        ListIterator<Point2D.Double> listIterator = cline.listIterator(cline.size() - 1);
                        while (listIterator.hasPrevious()) {
                            tcline.add(listIterator.previous());
                        }
                    }
                    s = 3;
                    clinesIterator.remove();
                    break;
                }
            }
        }

        if (0 == s) {
            ContourLine newLine = new ContourLine();
            newLine.add(new Point2D.Double(x1, y1));
            newLine.add(new Point2D.Double(x2, y2));
            clines.add(newLine);
        }
    }

    boolean mergeDuelLines(ContourLine cl) {
        if (cl.size() == 2 && cl.closed || cl.size() <= 1) {
            return true;
        } else {
            if (cl.size() == 2) {
                return false;
            }
        }
        Point2D.Double pt1, pt2, pt3;
        double epss = eps * eps;
        if (cl.closed) {
            for (int i = 0; i < cl.size(); i++) {
                pt2 = cl.pollFirst();
                pt1 = cl.peekLast();
                pt3 = cl.peekFirst();
                if (Point2D.Double.distanceSq(pt1.x, pt1.y, pt3.x, pt3.y) <= epss) {
                    cl.pollLast();
                } else {
                    cl.addLast(pt2);
                }
            }
            if(cl.size()<=2){
                return true;
            }
        } else {
            if (cl.size() < 2) {
                return true;
            }
            ListIterator<Point2D.Double> lit = cl.listIterator();

            pt1 = lit.next();
            pt2 = lit.next();
            pt3 = lit.next();

            while (true) {
                if (Point2D.distanceSq(pt1.x, pt1.y, pt3.x, pt3.y) <= epss) {
                    lit.previous();
                    lit.previous();
                    lit.previous();
                    lit.remove();
                    lit.next();
                    lit.remove();
                    if (lit.hasPrevious()) {
                        pt1 = lit.previous();
                        lit.next();
                        pt2 = lit.next();
                        if(lit.hasNext()){
                        pt3 = lit.next();}else{
                            return false;
                        }
                    } else {
                        pt1 = lit.next();
                        if (lit.hasNext()) {
                            pt2 = lit.next();
                        } else {
                            return true;
                        }
                        if (lit.hasNext()) {
                            pt3 = lit.next();
                        } else {
                            return false;
                        }
                    }
                } else {
                    pt1=pt2;
                    pt2=pt3;
                    if(lit.hasNext()){
                        pt3=lit.next();
                    }else{
                        return false;
                    }
                }
            }
        }
        return false;

    }

    public static void main(String[] args) {
        ContourLine test = new ContourLine();
        test.add(new Point2D.Double(0,1));
        test.add(new Point2D.Double(0, 0));
        test.add(new Point2D.Double(0, 1));
        test.add(new Point2D.Double(0, 2));
        test.add(new Point2D.Double(0, 3));
        test.add(new Point2D.Double(0, 2));
        test.add(new Point2D.Double(0, 1));
        //test.closed = true;
        Conrec cc = new Conrec();
        boolean result=cc.mergeDuelLines(test);
        System.out.println(test);
        System.out.println(result);




    }
}
