/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.utils.math;

import java.util.Arrays;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.BivariateRealFunction;

/**
 * <p> 对称三角形Gauss积分的工具类 </p> <p> 采用sum(面积×权值×积分点被积函数值)的方法进行积分，积分点采用重心坐标为背景数据，积分点全在三角形的内部（边，与外部均无积分点），最高代数精度为{@link #MAX_POWER}
 * </p> <p>the data is copy from: </ br> Table 10.5 Dunavant quadrature for area
 * coordinate triangle , p275,Chapter 10, Finite Element Analysis with Error
 * Estimators, Ed Akin, Elsevere.</p>
 *
 * @author epsilonyuan@gmail.com
 */
public class TriangleSymmetricQuadrature {

    public final static int MAX_POWER = 8;
    public final static int MIN_POWER = 1;
    public final static int[] numPts = new int[]{1, 3, 4, 6, 7, 12, 13, 16};
    public final static double[][] weights = new double[][]{
        {1},
        {1 / 3d, 1 / 3d, 1 / 3d},
        {-27 / 48d, 25 / 48d, 25 / 48d, 25 / 48d},
        {0.109951743655322, 0.109951743655322, 0.109951743655322, 0.223381589678011, 0.223381589678011, 0.223381589678011},
        {0.225000000000000, 0.125939180544827, 0.125939180544827, 0.125939180544827, 0.132394152788506, 0.132394152788506, 0.132394152788506},
        {0.050844906370207, 0.050844906370207, 0.050844906370207, 0.116786275726379, 0.116786275726379, 0.116786275726379, 0.082851075618374, 0.082851075618374, 0.082851075618374, 0.082851075618374, 0.082851075618374, 0.082851075618374},
        {-0.149570044467682, 0.175615257433208, 0.175615257433208, 0.175615257433208, 0.053347235608838, 0.053347235608838, 0.053347235608838, 0.077113760890257, 0.077113760890257, 0.077113760890257, 0.077113760890257, 0.077113760890257, 0.077113760890257},
        {0.144315607677787, 0.095091634267285, 0.095091634267285, 0.095091634267285, 0.103217370534718, 0.103217370534718, 0.103217370534718, 0.032458497623198, 0.032458497623198, 0.032458497623198, 0.027230314174435, 0.027230314174435, 0.027230314174435, 0.027230314174435, 0.027230314174435, 0.027230314174435}, //        {0.097135796282799, 0.031334700227139, 0.031334700227139, 0.031334700227139, 0.077827541004774, 0.077827541004774, 0.077827541004774, 0.079647738927210, 0.079647738927210, 0.079647738927210, 0.025577675658698, 0.025577675658698, 0.025577675658698, 0.043283539377289, 0.043283539377289, 0.043283539377289, 0.043283539377289, 0.043283539377289, 0.043283539377289}
    };
    public final static double[][] barycentricCoordinates = new double[][]{
        {1 / 3d, 1 / 3d, 1 / 3d},
        {2 / 3d, 1 / 6d, 1 / 6d,
            1 / 6d, 2 / 3d, 1 / 6d,
            1 / 6d, 1 / 6d, 2 / 3d},
        {1 / 3d, 1 / 3d, 1 / 3d,
            0.6, 0.2, 0.2,
            0.2, 0.6, 0.2,
            0.2, 0.2, 0.6},
        {0.816847572980459, 0.091576213509771, 0.091576213509771,
            0.091576213509771, 0.816847572980459, 0.091576213509771,
            0.091576213509771, 0.091576213509771, 0.816847572980459,
            0.108103018168070, 0.445948490915965, 0.445948490915965,
            0.445948490915965, 0.108103018168070, 0.445948490915965,
            0.445948490915965, 0.445948490915965, 0.108103018168070},
        {1 / 3d, 1 / 3d, 1 / 3d, 0.797426985353087,
            0.101286507323456, 0.101286507323456, 0.101286507323456,
            0.797426985353087, 0.101286507323456, 0.101286507323456,
            0.101286507323456, 0.797426985353087, 0.059715871789770,
            0.470142064105115, 0.470142064105115, 0.470142064105115,
            0.059715871789770, 0.470142064105115, 0.470142064105115,
            0.470142064105115, 0.059715871789770},
        {0.873821971016996, 0.063089014491502, 0.063089014491502,
            0.063089014491502, 0.873821971016996, 0.063089014491502,
            0.063089014491502, 0.063089014491502, 0.873821971016996,
            0.501426509658179, 0.249286745170910, 0.249286745170910,
            0.249286745170910, 0.501426509658179, 0.249286745170910,
            0.249286745170910, 0.249286745170910, 0.501426509658179,
            0.636502499121399, 0.310352451033784, 0.053145049844817,
            0.636502499121399, 0.053145049844817, 0.310352451033784,
            0.310352451033784, 0.636502499121399, 0.053145049844817,
            0.310352451033784, 0.053145049844817, 0.636502499121399,
            0.053145049844817, 0.636502499121399, 0.310352451033784,
            0.053145049844817, 0.310352451033784, 0.636502499121399},
        {1 / 3d, 1 / 3d, 1 / 3d,
            0.479308067841920, 0.260345966079040, 0.260345966079040,
            0.260345966079040, 0.479308067841920, 0.260345966079040,
            0.260345966079040, 0.260345966079040, 0.479308067841920,
            0.869739794195568, 0.065130102902216, 0.065130102902216,
            0.065130102902216, 0.869739794195568, 0.065130102902216,
            0.065130102902216, 0.065130102902216, 0.869739794195568,
            0.638444188569810, 0.312865496004874, 0.048690315425316,
            0.638444188569810, 0.048690315425316, 0.312865496004874,
            0.312865496004874, 0.638444188569810, 0.048690315425316,
            0.312865496004874, 0.048690315425316, 0.638444188569810,
            0.048690315425316, 0.638444188569810, 0.312865496004874,
            0.048690315425316, 0.312865496004874, 0.638444188569810},
        {1 / 3d, 1 / 3d, 1 / 3d,
            0.081414823414554, 0.459292588292723, 0.459292588292723,
            0.459292588292723, 0.081414823414554, 0.459292588292723,
            0.459292588292723, 0.459292588292723, 0.081414823414554,
            0.658861384496480, 0.170569307751760, 0.170569307751760,
            0.170569307751760, 0.658861384496480, 0.170569307751760,
            0.170569307751760, 0.170569307751760, 0.658861384496480,
            0.898905543365938, 0.050547228317031, 0.050547228317031,
            0.050547228317031, 0.898905543365938, 0.050547228317031,
            0.050547228317031, 0.050547228317031, 0.898905543365938,
            0.008394777409958, 0.263112829634638, 0.728492392955404,
            0.008394777409958, 0.728492392955404, 0.263112829634638,
            0.263112829634638, 0.008394777409958, 0.728492392955404,
            0.263112829634638, 0.728492392955404, 0.008394777409958,
            0.728492392955404, 0.008394777409958, 0.263112829634638,
            0.728492392955404, 0.263112829634638, 0.008394777409958}
    };

    /**
     * 获取power阶代数精度所对应的三角形对称积分点数
     *
     * @param power 积分的代数精度 应属于闭区间[{@link #MIN_POWER},{@link #MAX_POWER}]
     * @return
     */
    public static int getNumPoints(int power) {
        return numPts[power - 1];
    }

    /**
     * 获取power阶代数精度的三角形对称积分的积分点的重心坐标
     *
     * @param power 积分的代数精度
     * @param copy true: 返回{@link #barycentricCoordinates}的片段拷贝, false: 直接返回{@link #barycentricCoordinates}的片段引用
     * @return 重心坐标数组，长度为3*{@link #getNumPoints(int) getNumPoints(power)},其内容为:</
     * br>
     * {α<sub>1</sub>,β<sub>1</sub>,γ<sub>1</sub>,α<sub>2</sub>,β<sub>2</sub>,γ<sub>2</sub>,...}
     */
    public static double[] getBarycentricCoordinates(int power) {     
            return Arrays.copyOf(barycentricCoordinates[power - 1], barycentricCoordinates[power - 1].length);
    }

    /**
     * 获取power阶代数精度的三角形对称积分的积分点的迪卡尔坐标，三角形的三个顶点是(x1,y1),(x2,y2),(x3,y3),结果录入results返回
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param x3
     * @param y3
     * @param power 积分的代数精度 应属于闭区间[{@link #MIN_POWER},{@link #MAX_POWER}]
     * @param results 用于输入的数组，results.length于不小于2*{@link #getNumPoints(int) getNumPoints(power)}
     * @return 重心坐标数组，有效数据index为左闭右开区间[0,2*{@link #getNumPoints(int) getNumPoints(power)})内的整数，其内容为{x<sub>积分点1</sub>,y<sub>积分点1</sub>,x<sub>积分点2</sub>,y<sub>积分点2</sub>,...,}
     */
    public static int getPositions(double x1, double y1, double x2, double y2, double x3, double y3, int power, double[] results) {
        double[] coords = barycentricCoordinates[power - 1];
        int numPt = numPts[power - 1];
        for (int i = 0; i < numPts[power - 1]; i++) {
            results[i * 2] = coords[i * 3] * x1 + coords[i * 3 + 1] * x2 + coords[i * 3 + 2] * x3;
            results[i * 2 + 1] = coords[i * 3] * y1 + coords[i * 3 + 1] * y2 + coords[i * 3 + 2] * y3;
        }
        return numPt;
    }

    /**
     * 获取power阶代数精度的三角形对称积分的积分点的迪卡尔坐标，三角形的三个顶点是(x1,y1),(x2,y2),(x3,y3)
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param x3
     * @param y3
     * @param power 积分的代数精度 应属于闭区间[{@link #MIN_POWER},{@link #MAX_POWER}]
     * @return 重心坐标数组，长度为2*{@link #getNumPoints(int) getNumPoints(power)},
     * ，其内容为{x<sub>积分点1</sub>,y<sub>积分点1</sub>,x<sub>积分点2</sub>,y<sub>积分点2</sub>,...,}
     */
    public static int getPositions(double x1, double y1, double x2, double y2, double x3, double y3, int power) {
        return getPositions(x1, y1, x2, y2, y3, y3, power, new double[2 * numPts[power - 1]]);
    }

    /**
     * 获取power阶代数精度的单位三角形对称积分的积分点的权值，<strong>实际应用时应乘以三角形积分域的面积</strong>
     *
     * @param power 积分的代数精度 应属于闭区间[{@link #MIN_POWER},{@link #MAX_POWER}]
     * @param copy true: 返回{@link #weights}的片段拷贝, false: 直接返回{@link #weights}的片段引用
     * @return 权值数组，长度{@link #getNumPoints(int) getNumPoints(power)}
     */
    public static double[] getWeights(int power, boolean copy) {
        if (copy) {
            return Arrays.copyOf(weights[power - 1], weights[power - 1].length);
        } else {
            return weights[power - 1];
        }
    }

    /**
     * 获取power阶代数精度的单位三角形对称积分的积分点的权值拷贝，<strong>实际应用时应乘以三角形积分域的面积</strong>
     *
     * @param power 积分的代数精度 应属于闭区间[{@link #MIN_POWER},{@link #MAX_POWER}]
     * @return 权值数组，长度{@link #getNumPoints(int) getNumPoints(power)}
     */
    public static double[] getWeights(int power) {
        return getWeights(power, true);
    }

    /**
     * 在三角形上对函数fun进行代数精度为power的对称Gauss积分，三角形的顶点为(x1,y1,x2,y2,x3,y3),三角形的面积需另外计算为area.
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param x3
     * @param y3
     * @param power 积分的代数精度 应属于闭区间[{@link #MIN_POWER},{@link #MAX_POWER}]
     * @param fun 被积函数
     * @param area 三角形的面积
     * @return
     * @throws FunctionEvaluationException
     * @deprecated 用{@link #quadrate(double, double, double, double, double, double, int, org.apache.commons.math.analysis.BivariateRealFunction)
     * }代替
     */
    public static double quadrate(double x1, double y1, double x2, double y2, double x3, double y3, int power, BivariateRealFunction fun, double area) throws FunctionEvaluationException {
        int numPt = numPts[power - 1];
        double[] coords = barycentricCoordinates[power - 1];
        double[] ws = weights[power - 1];
        double result = 0;
        for (int i = 0; i < numPt; i++) {
            double x = x1 * coords[i * 3] + x2 * coords[i * 3 + 1] + x3 * coords[i * 3 + 2];
            double y = y1 * coords[i * 3] + y2 * coords[i * 3 + 1] + y3 * coords[i * 3 + 2];
            result += ws[i] * fun.value(x, y);
        }
        result *= area;
        return result;
    }

    /**
     * 在三角形上对函数fun进行代数精度为power的对称Gauss积分，三角形的顶点为(x1,y1,x2,y2,x3,y3)
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param x3
     * @param y3
     * @param power 积分的代数精度 应属于闭区间[{@link #MIN_POWER},{@link #MAX_POWER}]
     * @param fun 被积函数
     * @return
     * @throws FunctionEvaluationException
     */
    public static double quadrate(double x1, double y1, double x2, double y2, double x3, double y3, int power, BivariateRealFunction fun) throws FunctionEvaluationException {
        double area = net.epsilony.utils.geom.GeometryMath.triangleArea(x1, y1, x2, y2, x3, y3);
        return quadrate(x1, y1, x2, y2, x3, y3, power, fun, area);
    }

    /**
     * 在三角形上对函数fun进行代数精度为power的对称Gauss积分，三角形的顶点为(x1,y1,x2,y2,x3,y3)
     *
     * @param vertes 长度需大于6，内容为{x1,y1,x2,y2,x3,y3}
     * @param power 积分的代数精度 应属于闭区间[{@link #MIN_POWER},{@link #MAX_POWER}]
     * @param fun 被积函数
     * @return
     * @throws FunctionEvaluationException
     */
    public static double quadrate(double[] vertes, int power, BivariateRealFunction fun) throws FunctionEvaluationException {
        return quadrate(vertes[0], vertes[1], vertes[2], vertes[3], vertes[4], vertes[5], power, fun);
    }
}
