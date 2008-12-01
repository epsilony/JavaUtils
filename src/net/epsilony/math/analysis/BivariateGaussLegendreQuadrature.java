/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.math.analysis;

import java.util.logging.Level;
import java.util.logging.Logger;
import net.epsilony.math.util.BivariateMapper;
import org.apache.commons.math.ArgumentOutsideDomainException;
import org.apache.commons.math.FunctionEvaluationException;

/**
 * <P>本类是个工厂类的雏形，用于将一个二维的一一映射和Gauss-Legendre积分器结合</P>
 * <P>设计的使用流程：<br>
 * <ol type=1>
 * <li>构造一个BivariateMapper 该一一映射器实际用来指定被积区域XY，<br>
 *  因为一个[-1,1]<sup>2</sup>的区域上的所有点通过Mapper就得到XY区域上的所有点 
 * <li>构造本类，其中需要指定本类结合的mapper以及高斯积分点数的平方根 <br
 * <li>调用quadrate完成计算 
 * <li>修改mapper的属性 
 * <li>重复3－4步直到完成计算。
 * </ol>
 * @see net.epsilony.math.util.BivariateMapper
 * @version 0.10-haven't been tested 
 * @author M.Yuan
 */
public class BivariateGaussLegendreQuadrature {

    double[] coefs = null;
    double[] pts = null;
    int rowcol;
    MultivariateRealFunction fun;
    BivariateMapper mapper;

    /**
     * 
     * @param rowcol 高斯采样点数等于rowcol值的平方
     * @param fun 被积函数
     * @param mapper 从u,v域(u,v属于[-1,1])到被积的x,y域的映射
     */
    public BivariateGaussLegendreQuadrature(int rowcol, MultivariateRealFunction fun, BivariateMapper mapper) {
        try {
            this.rowcol = rowcol;
            this.fun = fun;
            this.mapper = mapper;
            coefs = GaussLegendreQuadrature.getGaussLegendreQuadratureCoefficients(rowcol);
            pts = GaussLegendreQuadrature.getGaussLegendreQuadraturePoints(rowcol);
        } catch (ArgumentOutsideDomainException ex) {
            Logger.getLogger(BivariateGaussLegendreQuadrature.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public double quadrate() {
        double result = 0;
        double[] mapResults = new double[3];
        int i, j;
        for (i = 0; i < rowcol; i++) {
            for (j = 0; j < rowcol; j++) {
                try {
                    mapper.getResults(pts[i], pts[j], mapResults);
                    result += coefs[i] * coefs[j] * fun.value(mapResults) * mapResults[2];
                } catch (FunctionEvaluationException ex) {
                    Logger.getLogger(BivariateGaussLegendreQuadrature.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return 0;
    }
}
