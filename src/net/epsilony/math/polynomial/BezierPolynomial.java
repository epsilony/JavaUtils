/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.math.polynomial;

import java.util.logging.Level;
import java.util.logging.Logger;
import net.epsilony.math.analysis.DifferentiableUnivariateVectorFunction;
import net.epsilony.math.analysis.UnivariateVectorFunction;
import net.epsilony.util.ArrayUtils;
import org.apache.commons.math.ArgumentOutsideDomainException;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math.analysis.UnivariateRealFunction;

/**
 * Bezier曲线的多项式，可以构造任意1～3维空间的Bezier曲线多项式
 * @author epsilon
 */
public class BezierPolynomial extends PolynomialFunction implements DifferentiableUnivariateVectorFunction {

    private PolynomialFunction[] polynomials;
    private int dimension = 0;
    private int nCtrlPts=0;

    /**
     * 获取一个一维Bezier多项式的系数
     * @param ctrlFactors 一维Bezier的控制点坐标如{x0,ctrlx1,ctrlx2,x3}
     * @return 一维Bezier多项式系数，阶数与数组下标对应。
     */
    public static double[] getCoefficients(double[] ctrlFactors) {
        return getPolynomialFunctionInstance(ctrlFactors).getCoefficients();
    }

    /**
     * 获取一个一维Bezier多项式
     * @param ctrlFactors 一维Bezier的控制点坐标
     * @return 一维Bezier多项式
     */
    public static PolynomialFunction getPolynomialFunctionInstance(double[] ctrlFactors) {
        int n = ctrlFactors.length - 1;
        PolynomialFunction sum = new BernsteinPolynomial(0, n);
        double[] coefs=sum.getCoefficients();

        PolynomialFunction p;


        for (int i = 0; i <= n; i++) {
            p = new BernsteinPolynomial(i, n);
            for(int j=0;j<coefs.length;j++){
            coefs[j]*=ctrlFactors[i];

        }
            p=new PolynomialFunction(coefs);
            sum = sum.add(p);
        }
        return sum;
    }

    /**
     * 构造一维Bezier多项式
     * @param ctrlFactors 一维Bezier的控制点坐标
     */
    public BezierPolynomial(double[] ctrlFactors) {
        super(getCoefficients(ctrlFactors));
        polynomials[0] = this;
        dimension = 1;
    }

    /**
     * 构造Bezier多项式
     * @param ctrlPoints 控制点数组
     */
    public BezierPolynomial(double[][] ctrlPoints) {
        super(multiVariateInit(ctrlPoints));
        nCtrlPts=ctrlPoints.length;
        dimension = ctrlPoints[0].length;
        polynomials = new PolynomialFunction[dimension];
        polynomials[0] = this;
        for (int j = 1; j < dimension; j++) {
            polynomials[j] = getPolynomialFunctionInstance(ArrayUtils.getMatrixColumn(ctrlPoints, j));
        }
    }

    private static double[] multiVariateInit(double[][] ctrlPoints) {
        return getCoefficients(ArrayUtils.getMatrixColumn(ctrlPoints, 0));
    }

    @Override
    public int getDimension() {
        return dimension;
    }

    @Override
    public double[] values(double input, double[] results) throws FunctionEvaluationException {
        if (input > 1 || input < 0) {
            try {
                throw new ArgumentOutsideDomainException(input, 0, 1);
            } catch (ArgumentOutsideDomainException ex) {
                Logger.getLogger(BezierPolynomial.class.getName()).log(Level.SEVERE, null, ex);
                throw new FunctionEvaluationException(ex,input);
            }
        }
        if (null == results) {
            throw new NullPointerException();
        }
        for (int i = 0; i < dimension; i++) {
            results[i] = polynomials[i].value(input);
        }
        return results;
    }

    @Override
    public double[] values(double input) throws FunctionEvaluationException {
        if (input > 1 || input < 0) {
            try {
                throw new ArgumentOutsideDomainException(input, 0, 1);
            } catch (ArgumentOutsideDomainException ex) {
                Logger.getLogger(BezierPolynomial.class.getName()).log(Level.SEVERE, null, ex);
                throw new FunctionEvaluationException(ex,input);
            }
        }
        double[] results = new double[dimension];
        return values(input, results);
    }

    /**
     * @return 本Bezier曲线有几个控制点
     */
    public int getCtrlPtsSize() {
        return nCtrlPts;
    }

    @Override
    public UnivariateVectorFunction vectorDerivative() {
        UnivariateVectorFunction result=new UnivariateVectorFunction() {
            UnivariateRealFunction[] funs=new UnivariateRealFunction[dimension];
            int dim;
            {
                dim=dimension;
                for(int i=0;i<dimension;i++){
                    funs[i]=polynomials[i].derivative();
                }
            }
            
            @Override
            public int getDimension() {
                return dim;
            }

            @Override
            public double[] values(double input, double[] results) throws FunctionEvaluationException {
                for(int i=0;i<dim;i++){
                    results[i]=funs[i].value(input);
                }
                return results;
            }

            @Override
            public double[] values(double input) throws FunctionEvaluationException {
                double[] results=new double[dim];
                return values(input,results);
            }
        };
        return result;
    }
    
}

