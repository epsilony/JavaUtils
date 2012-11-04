/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.epsilony.utils.math.substitution;

import org.apache.commons.math.FunctionEvaluationException;

/**
 * 将u,v （u,v属于[-1,1]) 平面上的坐标映射到x,y上，并计算Jaccobi阵行列式J(u,v)的绝对值
 * 该接口设计用于一般区域的GaussLegendre积分用。
 * @author epsilonyuan@gmail.com
 */
public interface Substitution2d {
    
    /**
     * @param u u坐标
     * @param v v坐标
     * @param results 最小长度为3的数组，用以储存运算结果，调用函数后结果为：{x(u,v),y(u,v),abs(J(u,v))}
     * @return results
     * @throws org.apache.commons.math.FunctionEvaluationException
     */
    double [] getResults(double u,double v,double[] results);
}
