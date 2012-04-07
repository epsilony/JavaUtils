/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.epsilony.math.radialbasis;

/**
 * 径向基函数接口
  * <p> <br><bold> Changelist </bold></br>
 * <br> 0.11 增加二阶偏导数函数接口 </br>
 * <br> 0.10 建立 </br>
 * @version 0.11
 * @author M.Yuan

 */
public interface RadialBasisFunction {

    /**
     * 求径向基函数的一阶偏导数
     * @param x
     * @param y
     * @param results 最小长度为2
     * @return ｛&part;f/&part;x, &part;f/&part;y}
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
     * @return 基函数的值
     */
    double value(double x, double y);
    
    /**
     * 求径向基函数的一阶偏导数
     * @param x
     * @param y
     * @param results 最小长度为3
     * @return ｛&part;&sup2;f/&part;x&sup2;,&part;&sup2;f/&part;x&part;y, &part;&sup2;f/&part;y&sup2;}
     */
    double [] quadPartialDifferential(double x,double y,double [] results);
    
    public void setNodesAverageDistance(double dc);

    public void setSupportDomainRadius(double delta);

    public RadialBasisFunction CopyOf(boolean deep);
}
