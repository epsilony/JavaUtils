/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.utils.math;

import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.Matrix;
import no.uib.cipr.matrix.sparse.FlexCompRowMatrix;

/**
 *
 * @author epsilonyuan@gmail.com
 */
public class EquationSolvers {
    public static class FlexCompRowMatrixSolver implements EquationSolver{
        int flag;

        public FlexCompRowMatrixSolver(int flag) {
            this.flag = flag;
        }

        @Override
        public DenseVector solve(Matrix matrix, DenseVector vector) {
            FlexCompRowMatrix mat=(FlexCompRowMatrix)matrix;
            return MatrixUtils.solveFlexCompRowMatrixByBandMethod(mat, vector, flag);
        }
        
    }
}
