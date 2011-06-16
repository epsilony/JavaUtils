/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.math.analysis.multigaussquadrature;

import java.util.Collection;
import net.epsilony.math.analysis.GaussLegendreQuadratureUtils;
import net.epsilony.math.polynomial.BivariateRealFunction;
import net.epsilony.math.util.CoonsBivariateMapper;
import net.epsilony.math.util.GeneralPolarToCartesianChange2D;
import net.epsilony.util.ui.geom.PathInfoNode;
import org.apache.commons.math.ArgumentOutsideDomainException;
import org.apache.commons.math.FunctionEvaluationException;

/**
 *
 * @author epsilon
 */
final public class BivariateGaussLegendreQuadratures {

    public static class GeneralSectorQuadrature {

        private static GeneralPolarToCartesianChange2D gptcChange = new GeneralPolarToCartesianChange2D();
        private static double[] gptcResults = new double[3];

        /**
         * 利用GeneralPolarToCartesianChange2D做一个扇形区域的积分。
         * @param fun
         * @param x0
         * @param y0
         * @param arcInfo
         * @param nRad
         * @param nCir
         * @return
         * @throws org.apache.commons.math.ArgumentOutsideDomainException
         * @throws org.apache.commons.math.FunctionEvaluationException
         */
        public static double generalSectorQuadrate(BivariateRealFunction fun, double x0, double y0, PathInfoNode arcInfo, int nRad, int nCir) throws ArgumentOutsideDomainException, FunctionEvaluationException {
            double result = 0;
            gptcChange.setOri(x0, y0);
            gptcChange.setPathInfoNode(arcInfo);
            double[] radPts, radCoefs, cirPts, cirCoefs;
            
            radPts = GaussLegendreQuadratureUtils.positionsArrays[nRad - 1];
            radCoefs = GaussLegendreQuadratureUtils.weightsArrays[nRad - 1];

            cirPts = GaussLegendreQuadratureUtils.positionsArrays[nCir - 1];
            cirCoefs = GaussLegendreQuadratureUtils.weightsArrays[nCir - 1];

            int i, j;
            for (i = 0; i < nRad; i++) {
                for (j = 0; j < nCir; j++) {
                    gptcChange.getResults(radPts[i], cirPts[j], gptcResults);
                    result += fun.value(gptcResults[0], gptcResults[1]) * gptcResults[2] * cirCoefs[j] * radCoefs[i];
                }
            }
            return result;
        }
    }

    public static class CoonsQuadrangleQuadrature {

        static CoonsBivariateMapper ccc = new CoonsBivariateMapper();

        public static double coonsQuadrate(BivariateRealFunction fun, Collection<PathInfoNode> infos, int nRows, int nCols) throws ArgumentOutsideDomainException, FunctionEvaluationException {

            ccc.setInfos(infos);
            return quadrate(fun, nRows, nCols);
        }

        public static double coonsQudarate(BivariateRealFunction fun, PathInfoNode[] infos, int nRows, int nCols) throws ArgumentOutsideDomainException, FunctionEvaluationException {
            ccc.setInfos(infos);
            return quadrate(fun, nRows, nCols);
        }

        static double[] cccResults = new double[3];


        private static double quadrate(BivariateRealFunction fun, int nRows, int nCols) throws ArgumentOutsideDomainException, FunctionEvaluationException {

            double[] rPts, rCoefs, cPts, cCoefs;
            rPts = GaussLegendreQuadratureUtils.positionsArrays[nRows - 1];
            rCoefs = GaussLegendreQuadratureUtils.weightsArrays[nRows - 1];

            cPts = GaussLegendreQuadratureUtils.positionsArrays[nCols - 1];
            cCoefs = GaussLegendreQuadratureUtils.weightsArrays[nCols - 1];

            int i, j;
            double result = 0;
            for (i = 0; i < nRows; i++) {
                for (j = 0; j < nCols; j++) {
                    ccc.getResults(rPts[i], cPts[j], cccResults);
                    result += fun.value(cccResults[0], cccResults[1]) * rCoefs[i] * cCoefs[j] * cccResults[2];
                }
            }
            return result;
        }
    }
}
