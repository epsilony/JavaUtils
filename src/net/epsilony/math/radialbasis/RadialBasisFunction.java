/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.epsilony.math.radialbasis;

/**
 *
 * @author epsilon
 */
public interface RadialBasisFunction {

    /**
     * 求径向基函数的一阶偏导数
     * @param x
     * @param y
     * @param results 最小长度为2
     * @return ｛x的偏导数值, y的偏导数值}
     */
    double[] partialDifferential(double x, double y, double[] results);

    /**
     * 设置径向基的中心
     * @param centerX 径向中心X坐标x<sub>i</sub>
     * @param centerY 径向中心Y坐标y<sub>i</sub>
     */
    void setCenter(double centerX, double centerY);

    /**
     * 求径向基函数的值
     * @param x
     * @param y
     * @return
     */
    double value(double x, double y);

}
