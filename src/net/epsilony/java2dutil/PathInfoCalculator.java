/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.java2dutil;

import java.awt.geom.PathIterator;
import net.epsilony.math.analysis.DifferentiableUnivariateVectorialFunctionEx;
import net.epsilony.math.util.GaussLegendreQuadratureUtils;
import net.epsilony.math.analysis.UnivariateVectorialFunctionEx;
import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import static java.lang.Math.*;

/**
 *
 * @author epsilonyuan@gmail.com
 */
final public class PathInfoCalculator implements DifferentiableUnivariateVectorialFunctionEx {

    PathInfoNode pathInfoNode;

    public PathInfoCalculator() {
    }

    public int getPathType() {
        return pathInfoNode.getType();
    }

    public void setPathInfoNode(PathInfoNode pathInfoNode) {
        this.pathInfoNode = pathInfoNode;
    }
    
    public PathInfoCalculator(PathInfoNode pathInfoNode) {
        setPathInfoNode(pathInfoNode);
    }

    @Override
    public int getDimension() {
        return 2;
    }

    public static double[] values(double input, double[] results, PathInfoNode node) throws FunctionEvaluationException {
        double td, tx, ty;
        double[] datas = node.getDatas();
        switch (node.getType()) {
            case PathIterator.SEG_LINETO:
                results[0] = datas[0] * (1 - input) + datas[2] * input;
                results[1] = datas[1] * (1 - input) + datas[3] * input;
                return results;
            case PathIterator.SEG_QUADTO:
                td = 1;
                tx = 0;
                ty = 0;

                tx += td * datas[0];
                ty += td * datas[1];
                td *= input;
                tx += td * (-2 * datas[0] + 2 * datas[2]);
                ty += td * (-2 * datas[1] + 2 * datas[3]);
                td *= input;
                tx += td * (datas[0] - 2 * datas[2] + datas[4]);
                ty += td * (datas[1] - 2 * datas[3] + datas[5]);
                results[0] = tx;
                results[1] = ty;
                return results;
            case PathIterator.SEG_CUBICTO:
                td = 1;
                tx = 0;
                ty = 0;

                tx += td * datas[0];
                ty += td * datas[1];
                td *= input;
                tx += td * (-3 * datas[0] + 3 * datas[2]);
                ty += td * (-3 * datas[1] + 3 * datas[3]);
                td *= input;
                tx += td * (3 * datas[0] - 6 * datas[2] + 3 * datas[4]);
                ty += td * (3 * datas[1] - 6 * datas[3] + 3 * datas[5]);
                td *= input;
                tx += td * (-datas[0] + 3 * datas[2] - 3 * datas[4] + datas[6]);
                ty += td * (-datas[1] + 3 * datas[3] - 3 * datas[5] + datas[7]);
                results[0] = tx;
                results[1] = ty;
                return results;
            default:
                throw new FunctionEvaluationException(input, "not write seg_type:%s",node.getType());
        }
    }

    public static double[] diffedValues(double input, double[] results, PathInfoNode node) throws FunctionEvaluationException {
        double td;
        double tx;
        double ty;
        double[] datas = node.getDatas();
        switch (node.getType()) {
            case PathIterator.SEG_LINETO:
                results[0] = -datas[0] + datas[2];
                results[1] = -datas[1] + datas[3];
                return results;
            case PathIterator.SEG_QUADTO:

                tx = 0;
                ty = 0;

                td = 1;
                tx += td * (-2 * datas[0] + 2 * datas[2]);
                ty += td * (-2 * datas[1] + 2 * datas[3]);
                td *= input;
                tx += 2 * td * (datas[0] - 2 * datas[2] + datas[4]);
                ty += 2 * td * (datas[1] - 2 * datas[3] + datas[5]);
                results[0] = tx;
                results[1] = ty;
                return results;
            case PathIterator.SEG_CUBICTO:

                tx = 0;
                ty = 0;

                td = 1;
                tx += td * (-3 * datas[0] + 3 * datas[2]);
                ty += td * (-3 * datas[1] + 3 * datas[3]);
                td *= input;
                tx += 2 * td * (3 * datas[0] - 6 * datas[2] + 3 * datas[4]);
                ty += 2 * td * (3 * datas[1] - 6 * datas[3] + 3 * datas[5]);
                td *= input;
                tx += 3 * td * (-datas[0] + 3 * datas[2] - 3 * datas[4] + datas[6]);
                ty += 3 * td * (-datas[1] + 3 * datas[3] - 3 * datas[5] + datas[7]);
                results[0] = tx;
                results[1] = ty;
                return results;
            default:
                throw new FunctionEvaluationException(input,"not write seg_type:%s",node.getType());
        }
    }

    @Override
    public double[] value(double input, double[] results) throws FunctionEvaluationException {
        if (input > 1 || input < 0) {
            throw new IllegalArgumentException("the input can only be in [0,1]");
        }
        if (getPathType() != PathInfoNode.SEG_G1_PATHS) {
            return values(input, results, pathInfoNode);
        }
        int i = pathInfoNode.getInfosIndex(input);
        double t = pathInfoNode.getInfosParm(i, input);
        return values(t, results, pathInfoNode.infos.get(i));
    }

   

    @Override
    public double[] value(double input) throws FunctionEvaluationException {
        double[] results = new double[getDimension()];
        return value(input, results);
    }
    UnivariateVectorFunctionImpl derivative = new UnivariateVectorFunctionImpl();

    @Override
    public UnivariateVectorialFunctionEx vectorDerivative() {
        return derivative;
    }

    

    final private class UnivariateVectorFunctionImpl implements UnivariateVectorialFunctionEx {

        public UnivariateVectorFunctionImpl() {
        }

        @Override
        public int getDimension() {
            return 2;
        }

        @Override
        public double[] value(double input, double[] results) throws FunctionEvaluationException {
            if (input > 1 || input < 0) {
                throw new IllegalArgumentException("the input can only be in [0,1]");
            }
            if (pathInfoNode.getType() != PathInfoNode.SEG_G1_PATHS) {
                return diffedValues(input, results, pathInfoNode);
            }
            int i = pathInfoNode.getInfosIndex(input);
            double t = pathInfoNode.getInfosParm(i, input);
            return diffedValues(t, results, pathInfoNode.getInfos().get(i));
        }

        @Override
        public double[] value(double input) throws FunctionEvaluationException {
            double[] results = new double[getDimension()];
            return value(input, results);
        }
    }

    final private class ForGetLengthUse implements UnivariateRealFunction {

        double[] tds = new double[2];

        @Override
        public double value(double x) throws FunctionEvaluationException {
            derivative.value(x, tds);
            return sqrt(tds[0] * tds[0] + tds[1] * tds[1]);
        }
    }
    ForGetLengthUse fgluFun = new ForGetLengthUse();
    public double getLength() throws FunctionEvaluationException, ConvergenceException {
        double[] datas = pathInfoNode.getDatas();
        switch (getPathType()) {
            case PathIterator.SEG_CUBICTO:
                return GaussLegendreQuadratureUtils.quadrate(fgluFun,3,0, 1);
            case PathIterator.SEG_LINETO:
                return sqrt((datas[0] - datas[2]) * (datas[0] - datas[2]) + (datas[1] - datas[3]) * (datas[1] - datas[3]));
            case PathIterator.SEG_QUADTO:
                return GaussLegendreQuadratureUtils.quadrate(fgluFun,3,0, 1);
            default:
                return -1;
        }
    }
    public static double epsG1Continus = 1e-10;

    public static boolean isG1Continus(PathInfoNode pi0, PathInfoNode pi1) {
        if (pi0.getEndX() != pi1.getEndX() || pi0.getEndY() != pi1.getEndY()) {
            return false;
        }
        int endi0 = PathInfoNode.getEndIndex(pi0);
        if ((pi0.datas[endi0] - pi0.datas[endi0 - 2]) * (pi1.datas[3] - pi1.datas[1]) - (pi0.datas[endi0 - 1] - pi0.datas[endi0 + 1]) * (pi1.datas[2] - pi1.datas[0]) > epsG1Continus) {
            return false;
        }
        return true;
    }
}
