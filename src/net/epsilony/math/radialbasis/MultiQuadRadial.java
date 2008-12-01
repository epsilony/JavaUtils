/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.math.radialbasis;

import static java.lang.Math.pow;

/**
 * 复合二次径向基函数
 * (r<sub>i</sub><sup>2</sup>+(&alpha<sub>c</sub>d<sub>c</sub>)<sup>2</sup>)<sup>q</sup>
 * 其中 r<sub>i</sub>=((x-x<sub>i</sub>)<sup>2</sup>+(y-y<sub>i</sub>)<sup>2</sup>)<sup>1/2<sup>
 * @author epsilon
 * @version 0.10 未曾测试过。
 */
public class MultiQuadRadial implements RadialBasisFunction {

    double centerX, centerY;
    double alphac, dc, q;
    /**
     *
     * @param centerX 径向中心X坐标x<sub>i</sub>
     * @param centerY 径向中心Y坐标y<sub>i</sub>
     * @param alphac 无量纲参数&alpha<sub>c</sub>，用以标明影响的结点重数
     * @param dc 节点的平均距离d<sub>c</sub>
     * @param q 幂参数
     */
    public MultiQuadRadial(double centerX, double centerY, double alphac, double dc, double q) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.alphac = alphac;
        this.dc = dc;
        this.q = q;
    }
    /**
     * 求径向基函数的值
     * @param x 
     * @param y
     * @return
     */
    @Override
    public double value(double x, double y) {
        return pow(((x - centerX) * (x - centerX) + (y - centerY) * (y - centerY) + (alphac * dc) * (alphac * dc)), q);
    }

    /**
     * 求径向基函数的一阶偏导数
     * @param x
     * @param y
     * @param results 最小长度为2
     * @return ｛x的偏导数值, y的偏导数值}
     */
    @Override
    public double[] partialDifferential(double x, double y, double[] results) {
        double t = q * pow(((x - centerX) * (x - centerX) + (y - centerY) * (y - centerY) + (alphac * dc) * (alphac * dc)), q - 1) * 2;
        results[0] = (x - centerX) * t;
        results[1] = (y - centerY) * t;
        return results;
    }
    
    /**
     * 设置径向基的中心
     * @param centerX 径向中心X坐标x<sub>i</sub>
     * @param centerY 径向中心Y坐标y<sub>i</sub>
     */
    @Override
    public void setCenter(double centerX,double centerY){
        this.centerX=centerX;
        this.centerY=centerY;
    }
}
