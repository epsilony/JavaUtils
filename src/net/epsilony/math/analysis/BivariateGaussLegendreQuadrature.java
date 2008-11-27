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
 *
 * @author epsilon
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
