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
    /**
     * This class needs a native library call rcm2 which is used for reducing matrix band width.
     * If encounted errors like 'cannot load library rcm2 ... librmc2.so' see {@link http://epsilony.net/mywiki/Academic/RcmResources/GetRcm2}
     */
    public static class FlexCompRowMatrixSolver implements EquationSolver{
        int flag;

        /**
         * Arg {@code flag} point out the type of {@code matrix} when invoking {@link #solve(no.uib.cipr.matrix.Matrix, no.uib.cipr.matrix.DenseVector) solve(matrix,vector) }</br>
         * If the {code matrix} is supposed to be symmetrical, flag should be {@link MatrixUtils#SYMMETRICAL} or {@link MatrixUtils#UNSYMMETRICAL_BUT_MIRROR_FROM_UP_HALF}. If it is certaint that the 
         * symmetrical {@code matrix} is spd, just let {@code flag =flag&MatrixUtils.SPD}
         * @param flag the {@code flag} for invoke {@link MatrixUtils#solveFlexCompRowMatrixByBandMethod(no.uib.cipr.matrix.sparse.FlexCompRowMatrix, no.uib.cipr.matrix.Vector, int, java.util.LinkedList) } later.
         */
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
