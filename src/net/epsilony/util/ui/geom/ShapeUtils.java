/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.util.ui.geom;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import net.epsilony.math.util.BivariateMapper;
import org.apache.commons.math.FunctionEvaluationException;

/**
 * 专门用于处理Shape有关的工具类
 * @author epsilon
 */
public class ShapeUtils {
    private static final PathInfoCalculator pathInfoCalculator=new PathInfoCalculator();
    public static PathInfoCalculator getPathInfoCalculator(){
        pathInfoCalculator.setPathInfoNode(null);
        return pathInfoCalculator;
    }
    public static final AffineTransform identityTransform = new AffineTransform();

    /**
     * 将一个pathIterator的情况打印出来。
     * @param pathIterator
     * @return
     */
    public static String toString(PathIterator pathIterator) {

        int type;
        String out;
        String sum = new String();
        double[] datas = new double[6];
        while (!pathIterator.isDone()) {

            Arrays.fill(datas, 0);
            type = pathIterator.currentSegment(datas);
            switch (type) {
                case PathIterator.SEG_CLOSE:
                    out = "SEG_CLOSE ";
                    break;
                case PathIterator.SEG_CUBICTO:
                    out = "SEG_CUBICTO ";
                    break;
                case PathIterator.SEG_LINETO:
                    out = "SEG_LINETO ";
                    break;
                case PathIterator.SEG_MOVETO:
                    out = "SEG_MOVETO ";
                    break;
                case PathIterator.SEG_QUADTO:
                    out = "SEG_QUADTO ";
                    break;
                default:
                    out = "BAD! UNKNOWN TYPE";
            }
            pathIterator.next();
            if (pathIterator.isDone()) {
                out = String.format("%1$-11s%2$s", out, Arrays.toString(datas));
                sum += out;
                break;
            } else {
                out = String.format("%1$-11s%2$s%n", out, Arrays.toString(datas));
                sum += out;
            }
        }
        return sum;
    }

    public static String toString(Shape shape) {
        return toString(shape.getPathIterator(identityTransform));
    }

    public static String toString(Shape shape, AffineTransform trans) {
        return toString(shape.getPathIterator(trans));
    }

    /**
     * @deprecated 
     * 计算两条首尾相接的Bezier曲线是否实际上可以由一条表示
     * @param input ｛{x00,y00},{x01,y01},{x02,y02},{x03=x10,y03=y10},{x11,y11},｛x12,y12},{x13,y13}}
     * @param output if return == false: {{x00,y00},{x1,y1},{x2,y2},{x13,y13}}
     * @return
     */
    public static boolean canBeOneBezier(double[][] input, double[][] output) {
        double t1 = (input[4][0] - input[2][0]) / (input[3][0] - input[2][0]);
        double t2 = (input[4][1] - input[2][1]) / (input[3][1] - input[2][1]);
        if (!(t1 == t2 && t1 > 0)) {
            return false;
        }
        double xt1 = input[1][0] + (input[2][0] - input[1][0]) / t1;
        double xt2 = input[5][0] + (input[4][0] - input[5][0]) / (1 - t1);
        if (xt1 != xt2) {
            return false;
        }
        double yt1 = input[1][1] + (input[2][1] - input[1][1]) / t1;
        double yt2 = input[5][1] + (input[4][1] - input[5][1]) / (1 - t1);
        if (yt1 != yt2) {
            return false;
        }
        output[0][0] = input[0][0];
        output[3][0] = input[6][0];
        output[0][1] = input[0][1];
        output[3][1] = input[6][1];
        output[1][0] = input[0][0] + (input[1][0] - input[0][0]) / t1;
        output[1][1] = input[0][1] + (input[1][1] - input[0][1]) / t1;
        output[2][0] = input[6][0] + (input[5][0] - input[6][0]) / (1 - t1);
        output[2][1] = input[6][1] + (input[5][1] - input[6][1]) / (1 - t1);
        return true;
    }

    /**
     * @deprecated 
     * 计算两条首尾相接的Bezier曲线是否实际上可以由一条表示
     * @param input ｛{x00,y00},{x01,y01},{x02,y02},{x03=x10,y03=y10},{x11,y11},｛x12,y12},{x13,y13}}
     * @param output if return == false: {{x00,y00},{x1,y1},{x2,y2},{x13,y13}}
     * @return
     */
    public static boolean canBeOneBezier(Shape input, Shape output) {
        boolean resB=false;
        double[] data = new double[6];

        PathIterator inputPI = input.getPathIterator(new AffineTransform());
        Path2D outputPath = new Path2D.Double();

        LinkedList<double[]> dataArrays = new LinkedList<double[]>();
        LinkedList<Integer> segs = new LinkedList<Integer>();
        LinkedList<double[]> segEnds = new LinkedList<double[]>();
        double[] tds, tds2, tds3;
        double[][] tinputs = new double[7][2];
        double[][] toutputs = new double[4][2];
        while (inputPI.isDone()) {
            int seg = inputPI.currentSegment(data);
            switch (seg) {
                case PathIterator.SEG_MOVETO:
                    tds = new double[2];
                    tds[0] = data[0];
                    tds[1] = data[1];
                    segEnds.add(tds);
                    break;
                case PathIterator.SEG_CLOSE:
                    if (segs.peekLast() != PathIterator.SEG_CUBICTO || segs.get(1) != PathIterator.SEG_CUBICTO) {
                        break;
                    }
                    tds = dataArrays.get(1);
                    tds2 = dataArrays.peekLast();
                    tds3 = segEnds.peekFirst();
                    if (tds3[0] != tds2[4] || tds3[1] != tds2[5]) {
                        break;
                    }
                    tds3 = segEnds.get(segEnds.size() - 2);
                    tinputs[0][0] = tds3[0];
                    tinputs[0][1] = tds3[1];

                    tinputs[1][0] = tds2[0];
                    tinputs[1][1] = tds2[1];
                    tinputs[2][0] = tds2[2];
                    tinputs[2][1] = tds2[3];
                    tinputs[3][0] = tds2[4];
                    tinputs[3][1] = tds2[5];
                    tinputs[4][0] = tds[0];
                    tinputs[4][1] = tds[1];
                    tinputs[5][0] = tds[2];
                    tinputs[5][1] = tds[3];
                    tinputs[6][0] = tds[4];
                    tinputs[6][1] = tds[5];

                    if (!canBeOneBezier(tinputs, toutputs)) {
                        break;
                    }

                    dataArrays.remove(1);
                    tds3 = dataArrays.peekFirst();
                    tds3[0] = toutputs[3][0];
                    tds3[1] = toutputs[3][1];

                    tds2[0] = toutputs[1][0];
                    tds2[1] = toutputs[1][1];
                    tds2[2] = toutputs[2][0];
                    tds2[3] = toutputs[2][1];
                    tds2[4] = toutputs[3][0];
                    tds2[5] = toutputs[3][1];
                    resB=true;
                    break;

                case PathIterator.SEG_QUADTO:
                    tds=segEnds.peekLast();
                    data=upBezierDegree(tds,data);
                    seg=PathIterator.SEG_CUBICTO;
                case PathIterator.SEG_CUBICTO:                   
                    tds = new double[2];
                    tds[0] = data[4];
                    tds[1] = data[5];
                    segEnds.add(tds);
                    if(segs.peekLast()!=PathIterator.SEG_CUBICTO){
                        break;
                    }
                    tds3=segEnds.get(segEnds.size()-3);
                    tds=dataArrays.peekLast();
                    tinputs[0][0]=tds3[0];
                    tinputs[0][1]=tds3[1];
                    
                    tinputs[1][0] = tds[0];
                    tinputs[1][1] = tds[1];
                    tinputs[2][0] = tds[2];
                    tinputs[2][1] = tds[3];
                    tinputs[3][0] = tds[4];
                    tinputs[3][1] = tds[5];
                    tinputs[4][0] = data[0];
                    tinputs[4][1] = data[1];
                    tinputs[5][0] = data[2];
                    tinputs[5][1] = data[3];
                    tinputs[6][0] = data[4];
                    tinputs[6][1] = data[5];
                    if (!canBeOneBezier(tinputs, toutputs)) {
                        break;
                    }
                    dataArrays.pollLast();
                    segs.pollLast();
                    segEnds.remove(segEnds.size()-2);
                    data[0]=toutputs[1][0];
                    data[1]=toutputs[1][1];
                    data[2]=toutputs[2][0];
                    data[3]=toutputs[2][1];
                    data[4]=toutputs[3][0];
                    data[5]=toutputs[3][1];
                    resB=true;
                    break;
            }
            dataArrays.add(Arrays.copyOf(data, 6));
            segs.add(seg);
            if (seg == PathIterator.SEG_CLOSE) {
                segEnds.clear();
                fillPath(dataArrays, segs, outputPath);
            }
        }
        return resB;
    }

    public static double cbobeps = 1e-32;


    /**
     * 将infos所表示的一个单环Shape中的可合并曲线段合并
     * @param infos
     * @param output 输出用最好是LinkedList
     * @return
     */
    public static boolean canBeOneBezier(List<PathInfoNode> infos, List<PathInfoNode> output) {
        PathInfoNode tNode = null;
        output.clear();
        double td1, td2, td3, td4, u, tdx, tdy;
        double[] datas;
        double[] tdatas;
        boolean canBe = false;
        for (PathInfoNode infoNode : infos) {
            if (tNode == null || tNode.getEndX() != infoNode.getStartX() || tNode.getEndY() != infoNode.getStartY()) {
                tNode = infoNode;
                output.add(infoNode);
                continue;
            }

            datas = infoNode.datas;
            tdatas = tNode.datas;

            if (infoNode.segType == PathIterator.SEG_LINETO && tNode.segType == PathIterator.SEG_LINETO) {
                td1 = datas[2] - datas[0];
                td2 = datas[3] - datas[1];
                td3 = tdatas[2] - tdatas[0];
                td4 = tdatas[3] - tdatas[1];
                if (td3 * td2 == td4 * td1) {
                    tdatas[2] = datas[2];
                    tdatas[3] = datas[3];
                    canBe = true;
                    continue;
                } else {
                    tNode = infoNode;
                    output.add(infoNode);
                    continue;
                }
            }

            if (infoNode.segType == PathIterator.SEG_QUADTO) {
                upBezierDegree(infoNode);
            }

            if (infoNode.segType == PathIterator.SEG_CUBICTO && tNode.segType == PathIterator.SEG_CUBICTO) {
                td1 = datas[2] - datas[0];
                td2 = datas[3] - datas[1];
                td3 = tdatas[6] - tdatas[4];
                td4 = tdatas[7] - tdatas[5];
                if (td3 * td2 == td4 * td1) {
                    u = td1 > td2 ? td3 / td1 : td4 / td2;
                    tdx = tdatas[4] * u + (tdatas[4] - tdatas[2]) * u - datas[2] - (datas[2] - datas[4]) / u;
                    tdy = tdatas[5] * u + (tdatas[5] - tdatas[3]) * u - datas[3] - (datas[3] - datas[5]) / u;
                    if (tdx * tdx - tdy * tdy < cbobeps) {
                        tdatas[2] = tdatas[2] * (1 + u) - tdatas[0] * u;
                        tdatas[3] = tdatas[3] * (1 + u) - tdatas[1] * u;
                        tdatas[4] = (datas[4] - datas[6]) / u + datas[4];
                        tdatas[5] = (datas[5] - datas[7]) / u + datas[5];
                        tdatas[6] = datas[6];
                        tdatas[7] = datas[7];
                        canBe = true;
                        continue;
                    }
                }
            }

            tNode=infoNode;
            output.add(infoNode);

        }

        if (output.isEmpty()) {
            return canBe;
        }

        PathInfoNode infoNode = output.get(0);


        if (tNode == null || tNode.getEndX() != infoNode.getStartX() || tNode.getEndY() != infoNode.getStartY()) {
            return canBe;
        }

        datas = infoNode.datas;
        tdatas = tNode.datas;

        if (infoNode.segType == PathIterator.SEG_LINETO && tNode.segType == PathIterator.SEG_LINETO) {
            td1 = datas[2] - datas[0];
            td2 = datas[3] - datas[1];
            td3 = tdatas[2] - tdatas[0];
            td4 = tdatas[3] - tdatas[1];
            if (td3 * td2 == td4 * td1) {
                tdatas[2] = datas[2];
                tdatas[3] = datas[3];
                canBe = true;
                output.remove(0);
                return canBe;
            } else {
                return canBe;
            }
        }

        if (infoNode.segType == PathIterator.SEG_QUADTO) {
            upBezierDegree(infoNode);
        }

        if (infoNode.segType == PathIterator.SEG_CUBICTO && tNode.segType == PathIterator.SEG_CUBICTO) {
            td1 = datas[2] - datas[0];
            td2 = datas[3] - datas[1];
            td3 = tdatas[6] - tdatas[4];
            td4 = tdatas[7] - tdatas[5];
            if (td3 * td2 == td4 * td1) {
                u = td1 > td2 ? td3 / td1 : td4 / td2;
                tdx = tdatas[4] * u + (tdatas[4] - tdatas[2]) * u - datas[2] - (datas[2] - datas[4]) / u;
                tdy = tdatas[5] * u + (tdatas[5] - tdatas[3]) * u - datas[3] - (datas[3] - datas[5]) / u;
                if (tdx * tdx - tdy * tdy < cbobeps) {
                    tdatas[2] = tdatas[2] * (1 + u) - tdatas[0] * u;
                    tdatas[3] = tdatas[3] * (1 + u) - tdatas[1] * u;
                    tdatas[4] = (datas[4] - datas[6]) / u + datas[4];
                    tdatas[5] = (datas[5] - datas[7]) / u + datas[5];
                    tdatas[6] = datas[6];
                    tdatas[7] = datas[7];
                    canBe = true;
                    output.remove(0);
                    return canBe;
                }
            }
        }



        return canBe;
    }

    private static void fillPath(LinkedList<double[]> dataArrays, LinkedList<Integer> segs, Path2D path) {
        double[] data;
        int seg;
        while (!dataArrays.isEmpty()) {
            data=dataArrays.pollFirst();
            seg=segs.pollFirst();
            switch(seg){
                case PathIterator.SEG_CLOSE:
                    path.closePath();
                    break;
                case PathIterator.SEG_CUBICTO:
                    path.curveTo(data[0], data[1], data[2], data[3], data[4], data[5]);
                    break;
                case PathIterator.SEG_LINETO:
                    path.lineTo(data[0], data[1]);
                    break;
                case PathIterator.SEG_MOVETO:
                    path.moveTo(data[0], data[1]);
                    break;
                case PathIterator.SEG_QUADTO:
                    path.quadTo(data[0], data[1], data[2], data[3]);
                    break;
            }
        }
    }

    public static double[] upBezierDegree(double[] start,double[] data) {
        double [] res = new double[6];
        res[4]=data[2];
        res[5]=data[3];
        res[0]=(start[0]+data[0]*2)/3.0;
        res[1]=(start[1]+data[1]*2)/3.0;
        res[2]=(data[0]*2+data[2])/3.0;
        res[3]=(data[1]*2+data[3])/3.0;
        return res;
    }
    
    public static double[] upBezierDegree(double[] data){
        double []res=new double[4];
        res[0]=data[0];
        res[3]=data[2];
        res[1]=(data[0]+data[1]*2)/3.0;
        res[2]=(data[1]*2+data[2])/3.0;
        return res;
    }


    /**
     * 获取一个PathIterator的曲线几何信息。
     * @param pathIterator
     * @param infos 
     * @param closedIndes 每一个封闭段的起止坐标
     */
    public static void getPathInfo(PathIterator pathIterator, List<PathInfoNode> infos, List<int[]> closedIndes) {
        infos.clear();
        closedIndes.clear();
        int type;
        int[] cis = null;
        double fx = 0,  fy = 0,  startX = 0,  startY = 0,  fx2 = 0,  fy2 = 0;
        PathInfoNode infoNode;
        double[] datas;
        while (!pathIterator.isDone()) {

            infoNode = new PathInfoNode();
            datas = infoNode.datas;
            type = pathIterator.currentSegment(datas);
            switch (type) {
                case PathIterator.SEG_CLOSE:
                    if (fx != startX || fy != startY) {
                        datas[0] = fx;
                        datas[1] = fy;
                        datas[2] = startX;
                        datas[3] = startY;
                        infoNode.segType = PathIterator.SEG_LINETO;
                        infos.add(infoNode);

                    }
                    cis[1] = infos.size() - 1;
                    closedIndes.add(cis);
                    pathIterator.next();
                    continue;
                // break;
                case PathIterator.SEG_CUBICTO:
                    fx2 = datas[4];
                    fy2 = datas[5];
                    break;
                case PathIterator.SEG_LINETO:
                    fx2 = datas[0];
                    fy2 = datas[1];
                    break;
                case PathIterator.SEG_QUADTO:
                    fx2 = datas[2];
                    fy2 = datas[3];
                    break;
                case PathIterator.SEG_MOVETO:
                    cis = new int[]{-1, -1};
                    cis[0] = infos.size();
                    startX = datas[0];
                    startY = datas[1];
                    fx = datas[0];
                    fy = datas[1];
                    pathIterator.next();
                    continue;
                //break;
            }
            for (int i = 7; i >= 2; i--) {
                datas[i] = datas[i-2];
            }
            datas[0] = fx;
            datas[1] = fy;
            fx = fx2;
            fy = fy2;
            infoNode.segType = type;
            infos.add(infoNode);
            pathIterator.next();
        }
    }

    /**
     * 获取一个Shape的曲线几何信息。
     * @param shape 
     * @param dataArrays 曲线的几何信息，每一个元素为{x0,y0,y1,y2....} 例如：对应的segTypes为PathIterator.SEG_LINTO,则为{x0,y0,x1,y1,0,0,0,0}
     * @param segTypes
     * @param closedIndes 每一个封闭段的起止坐标
     */
    public static void getPathInfo(Shape shape, List<PathInfoNode> infos, List<int[]> closedIndes) {
        getPathInfo(shape.getPathIterator(new AffineTransform()), infos, closedIndes);
    }


    private static double[] upBezierDegree(PathInfoNode infoNode) {
        double[] datas = infoNode.datas;
        double x1,  y1,  x2,  y2;
        datas[6] = datas[4];
        datas[7] = datas[5];
        x1 = (datas[0] + datas[2] * 2) / 3;
        x2 = (datas[2] * 2 + datas[4]) / 3;
        y1 = (datas[1] + datas[3] * 2) / 3;
        y2 = (datas[3] * 2 + datas[5]) / 3;
        datas[2] = x1;
        datas[3] = y1;
        datas[4] = x2;
        datas[5] = y2;
        infoNode.segType = PathIterator.SEG_CUBICTO;
        return datas;
    }
    
    public static Shape getCoordinateChangeGridShape(BivariateMapper bcc,int nRows,int nCols) throws FunctionEvaluationException{
        int i,j;
        double du=2.0/nCols;
        double dv=2.0/nRows;
        Path2D path=new Path2D.Double();
        double[] results=new double[3];
        for(i=0;i<=nRows;i++){
            bcc.getResults(0-1, dv*i-1, results);
            path.moveTo(results[0], results[1]);
            for(j=1;j<=nCols;j++){
                bcc.getResults(du*j-1, dv*i-1, results);
                path.lineTo(results[0], results[1]);
            }
        }
        
        for(j=0;j<=nCols;j++){
            bcc.getResults(du*j-1, 0-1, results);
            path.moveTo(results[0], results[1]);
            for(i=1;i<=nRows;i++){
                bcc.getResults(du*j-1, dv*i-1, results);
                path.lineTo(results[0], results[1]);
            }
        }
        
        return path.createTransformedShape(identityTransform);     
    }
}