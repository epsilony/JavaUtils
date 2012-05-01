/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.utils.java2d;

import java.awt.geom.PathIterator;
import java.util.Arrays;
import java.util.List;
import static java.lang.Math.sqrt;

/**
 * 用于存放PathIterator信息的结点
 * 一个结点对应一条曲线
 * datas={x0,y0,x1,y1,..。｝相当于pathIterator.currentSegment()获得的double[]在头部加上起点
 * segType为PathIterator规定的SEG_LINETO等三种曲线。
 * @author epsilonyuan@gmail.com
 */
public class PathInfoNode {

    List<PathInfoNode> infos = null;
    public double[] datas = new double[8];
    public int segType=-1;
    double[] g1SegDomains = null;
    final static int SEG_G1_PATHS = 10;
    
    public List<PathInfoNode> getInfos(){
        return infos;
    }

    public int getInfosIndex(double input) {
        int i = 0;
        if (input > g1SegDomains[0]) {
            do {
                i++;
            } while (g1SegDomains[i] < input);
        }


        return i;
    }
    
    public double getInfosParm(int i, double input) {
        double t;

        if (i != 0) {
            t = (input - g1SegDomains[i - 1]) / (g1SegDomains[i] - g1SegDomains[i - 1]);
        } else {
            t = input / g1SegDomains[0];
        }
        return t;
    }

    public void setG1ContinusPaths(List<PathInfoNode> infos) {
        this.infos = infos;
        segType = SEG_G1_PATHS;
        g1SegDomains = new double[infos.size()];
        double tLen = 1;
        PathInfoNode tp = null;
        int i = 1;
        double sumLen = 1;
        for (PathInfoNode p : infos) {
            if (tp == null) {
                tp = p;
                g1SegDomains[0] = 1;
                continue;
            }
            tLen *= p.getStartArrow() / tp.getEndArrow();
            g1SegDomains[i] = tLen;
            sumLen += tLen;
            i++;
        }
        g1SegDomains[0] /= sumLen;
        int length = g1SegDomains.length;
        double tLen2;
        tLen = g1SegDomains[length - 1];
        g1SegDomains[length - 1] = 1;

        for (i = 1; i < length / 2; i++) {

            g1SegDomains[i] = g1SegDomains[i - 1] + g1SegDomains[i] / sumLen;
            tLen2 = g1SegDomains[length - i - 1];
            g1SegDomains[length - 1 - i] = g1SegDomains[length - i] - tLen / sumLen;
            tLen = tLen2;
        }

        if (length % 2 != 0 && length > 1) {
            g1SegDomains[length / 2 + 1] = g1SegDomains[length / 2] + g1SegDomains[length / 2 + 1] / sumLen;
        }
    }

    public static int getEndIndex(int segType) {
        switch (segType) {
            case PathIterator.SEG_LINETO:
                return 2;
            case PathIterator.SEG_CUBICTO:
                return 6;
            case PathIterator.SEG_QUADTO:
                return 4;
            default:
                return -1;
        }
    }

    public static int getEndIndex(PathInfoNode node) {
        return getEndIndex(node.segType);
    }

    public double getEndX() {
        return datas[getEndIndex(segType)];
    }

    public double getEndY() {
        return datas[getEndIndex(segType) + 1];
    }

    public double getStartX() {
        return datas[0];
    }

    public double getStartY() {
        return datas[1];
    }

    @Override
    public String toString() {
        String s = null;
        switch (segType) {
            case PathIterator.SEG_LINETO:
                s = "LINE  ";
                break;
            case PathIterator.SEG_CUBICTO:
                s = "CUBIC ";
                break;
            case PathIterator.SEG_QUADTO:
                s = "QUAD  ";
                break;
            default:
                s = "BAD OTHER!";
        }
        return String.format("%s %s", s, Arrays.toString(datas));
    }

    int getType() {
        return segType;
    }

    double[] getDatas() {
        return datas;
    }

    public double getEndArrow() {
        switch (segType) {
            case PathIterator.SEG_CUBICTO:
                return 3 * sqrt((datas[6] - datas[4]) * (datas[6] - datas[4]) + (datas[7] - datas[5]) * (datas[7] - datas[5]));
            case PathIterator.SEG_QUADTO:
                return 2 * sqrt((datas[4] - datas[2]) * (datas[4] - datas[2]) + (datas[5] - datas[3]) * (datas[5] - datas[3]));
            case PathIterator.SEG_LINETO:
                return sqrt((datas[2] - datas[0]) * (datas[2] - datas[0]) + (datas[2] - datas[0]) * (datas[2] - datas[0]));
            default:
                throw new IllegalStateException("bad type of PathInfoNode");
        }
    }

    public double getStartArrow() {
        double len = sqrt((datas[2] - datas[0]) * (datas[2] - datas[0]) + (datas[2] - datas[0]) * (datas[2] - datas[0]));
        switch (segType) {
            case PathIterator.SEG_CUBICTO:
                return 3 * len;
            case PathIterator.SEG_QUADTO:
                return 2 * len;
            case PathIterator.SEG_LINETO:
                return len;
            default:
                throw new IllegalStateException("bad type of PathInfoNode");
        }
    }

}