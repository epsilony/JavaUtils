/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.utils.math;

import java.util.Arrays;
import static java.lang.Math.*;
import org.apache.commons.math.ArgumentOutsideDomainException;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateRealFunction;

/**
 * Gauss－Legendre积分，计划用commons-math的LegendreGaussIntegrator代替
 * @author epsilonyuan@gmail.com
 */
public class GaussLegendreQuadratureUtils {

    public static final int MAXPOINTS = 5;
    public static final int MINPOINTS = 1;
    public static final double[][] positionsArrays = new double[][]{
        {0},
        {-sqrt(3) / 3, sqrt(3) / 3},
        {-sqrt(15) / 5, 0, sqrt(15) / 5},
        {-sqrt(525 + 70 * sqrt(30)) / 35, -sqrt(525 - 70 * sqrt(30)) / 35, sqrt(525 - 70 * sqrt(30)) / 35, sqrt(525 + 70 * sqrt(30)) / 35},
        {-sqrt(245 + 14 * sqrt(70)) / 21, -sqrt(245 - 14 * sqrt(70)) / 21, 0, sqrt(245 - 14 * sqrt(70)) / 21, sqrt(245 + 14 * sqrt(70)) / 21}};
    public static final double[][] weightsArrays = new double[][]{
        {2},
        {1, 1},
        {5 / 9d, 8 / 9d, 5 / 9d},
        {(18 - sqrt(30)) / 36, (18 + sqrt(30)) / 36, (18 + sqrt(30)) / 36, (18 - sqrt(30)) / 36},
        {(322 - 13 * sqrt(70)) / 900, (322 + 13 * sqrt(70)) / 900, 128 / 225d, (322 + 13 * sqrt(70)) / 900, (322 - 13 * sqrt(70)) / 900}};

    public static boolean isNumInDomain(int n) {
        if (n < MINPOINTS || n > MAXPOINTS) {
            throw new UnsupportedOperationException("The quadrature points number:"+n+" is not supported yet");
        }

        return true;
    }

    /**
     * 获取[-1,1]区间上的n点Gause-Legendre积分点坐标拷贝
     * @param nPt Gauss点数，取值在闭区间 [{@link #MINPOINTS},{@link #MAXPOINTS}]中
     * @return a Copy of n Point position of Gauss-Legendre Quadrature over interval [1,1]
     * @throws ArgumentOutsideDomainException 
     */
    public static double[] getPositions(int nPt){
        isNumInDomain(nPt);
        return Arrays.copyOf(positionsArrays[nPt - 1], nPt);
    }
    
    /**
     * 获取[-1,1]区间上的n点Gause-Legendre积分点坐标
     * @param nPt Gauss点数，取值在闭区间 [{@link #MINPOINTS},{@link #MAXPOINTS}]中
     * @param copy 是否拷备输出
     * @return [-1,1]区间上的nPt点Gause-Legendre积分点坐标
     * @throws ArgumentOutsideDomainException 
     */
    public static double[] getPositions(int nPt,boolean copy) throws ArgumentOutsideDomainException{
        if(copy){
            return getPositions(nPt);
        }else{
            isNumInDomain(nPt);
            return positionsArrays[nPt-1];
        }
    }

    /**
     * 获取积分下限low至积分上线up之间nPt个Gauss点坐标
     * @param nPt Gauss点数，取值在闭区间 [{@link #MINPOINTS},{@link #MAXPOINTS}]中
     * @param low 积分下限
     * @param up 积分上限
     * @param results 不可以为null或长度小于nPt
     * @return
     * @throws ArgumentOutsideDomainException 
     */
    public static double[] getPositions(int nPt, double low, double up, double[] results) throws ArgumentOutsideDomainException {
        isNumInDomain(nPt);
        double[] positions = positionsArrays[nPt - 1];
        double t1 = up - low;
        double t2 = up + low;
        for (int i = 0; i < nPt; i++) {
            results[i] =(t1 * positions[i] + t2) / 2;
        }
        return results;
    }
    
    public static double[] getPositions(int nPt,double low,double up) throws ArgumentOutsideDomainException{
        return getPositions(nPt, low, up,new double[nPt]);
    }

    /**
     * 获取[-1,1]区间上的n点Gause-Legendre积分点权值
     * @param nPt Gauss点数，取值在闭区间 [{@link #MINPOINTS},{@link #MAXPOINTS}]中
     * @return Copy of n Point weights of Gauss-Legendre Quadrature over interval [1,1]
     * @throws ArgumentOutsideDomainException 
     */
    public static double[] getWeights(int nPt){
//        isNumInDomain(nPt);
        return Arrays.copyOf(weightsArrays[nPt - 1], nPt);
    }
    
    /**
     * 获取从积分下限low到积分上限对函数进行nPt点Gauss积分的权值。积分上下限的大小关系没有限制，low可以大于up。
     * @param nPt Gauss点数，取值在闭区间 [{@link #MINPOINTS},{@link #MAXPOINTS}]中
     * @param copy
     * @return 
     * @throws ArgumentOutsideDomainException 
     */
    public static double[] getWeights(int nPt,boolean copy) throws ArgumentOutsideDomainException{
        if(copy){
            return getWeights(nPt);
        }else{
            isNumInDomain(nPt);
            return weightsArrays[nPt-1];
        }
    }
    
    /**
     * 获取从积分下限low到积分上限对函数进行nPt点Gauss积分的权值。积分上下限的大小关系没有限制，low可以大于up。
     * @param nPt Gauss点数，取值在闭区间 [{@link #MINPOINTS},{@link #MAXPOINTS}]中
     * @param low 积分下限
     * @param up 积分上限
     * @param results 不可以为null或长度小于nPt
     * @return
     * @throws ArgumentOutsideDomainException 
     */
    public static double[] getWeights(int nPt,double low,double up,double[] results) throws ArgumentOutsideDomainException{
        isNumInDomain(nPt);
        double[] weights = weightsArrays[nPt - 1];
        double t1 = up - low;
        for (int i = 0; i < nPt; i++) {
            results[i] = t1 / 2 * weights[i] ;
        }
        return results;
    }
    
    /**
     * 获取从积分下限low到积分上限对函数进行nPt点Gauss积分的权值。积分上下限的大小关系没有限制，low可以大于up。
     * @param nPt Gauss点数，取值在闭区间 [{@link #MINPOINTS},{@link #MAXPOINTS}]中
     * @param low 积分下限
     * @param up 积分上限
     * @return
     * @throws ArgumentOutsideDomainException 
     */
    public static double[] getWeights(int nPt,double low,double up) throws ArgumentOutsideDomainException {
        return getWeights(nPt, low, up, new double[nPt]);
    }
    
    /**
     * 从积分下限low到积分上限对函数fun进行nPt点Gauss积分。积分上下限的大小关系没有限制，low可以大于up。
     * @param fun 被积函数
     * @param nPt Gauss点数，取值在闭区间 [{@link #MINPOINTS},{@link #MAXPOINTS}]中
     * @param low 积分下限
     * @param up 积分上限
     * @return
     * @throws FunctionEvaluationException 
     */
    public static double quadrate(UnivariateRealFunction fun, int nPt, double low, double up) throws FunctionEvaluationException {
        double result = 0;
        double[] positions = positionsArrays[nPt - 1];
        double[] weights = weightsArrays[nPt - 1];
        double t1 = up - low;
        double t2 = up + low;
        for (int i = 0; i < nPt; i++) {
            result += t1 / 2 * weights[i] * fun.value((t1 * positions[i] + t2) / 2);
        }
        return result;
    }

    public static int getNumPoints(int power) {
        return (int) Math.ceil((power+1)/2.0);
    }
    
    public static double getWeight(int numPt,int index,double low,double up){
        return weightsArrays[numPt-1][index]*(up-low)/2.0;
    }
    
    public static double getWeight(int numPt,int index){
        return weightsArrays[numPt-1][index];
    }
    
    public static double getPosition(int numPt,int index, double low,double up){
        double[] positions = positionsArrays[numPt - 1];
        double t1 = up - low;
        double t2 = up + low;
        return (t1 * positions[index] + t2) / 2.0;
    }
    
    public static double getPosition(int numPt,int index){
        return positionsArrays[numPt-1][index];
    }

}