/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.utils.math;

import java.util.logging.Level;
import java.util.logging.Logger;
import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.Matrix;
import no.uib.cipr.matrix.MatrixEntry;
import no.uib.cipr.matrix.sparse.AMG;
import no.uib.cipr.matrix.sparse.CGS;
import no.uib.cipr.matrix.sparse.CompRowMatrix;
import no.uib.cipr.matrix.sparse.DefaultIterationMonitor;
import no.uib.cipr.matrix.sparse.FlexCompRowMatrix;
import no.uib.cipr.matrix.sparse.GMRES;
import no.uib.cipr.matrix.sparse.ILU;
import no.uib.cipr.matrix.sparse.ILUT;
import no.uib.cipr.matrix.sparse.IterationMonitor;
import no.uib.cipr.matrix.sparse.IterativeSolver;
import no.uib.cipr.matrix.sparse.IterativeSolverNotConvergedException;
import no.uib.cipr.matrix.sparse.Preconditioner;

/**
 *
 * @author epsilonyuan@gmail.com
 */
public class EquationSolvers {

    /**
     * This class needs a native library call rcm2 which is used for reducing
     * matrix band width. If encounted errors like 'cannot load library rcm2 ...
     * librmc2.so' see {@link http://epsilony.net/mywiki/Academic/RcmResources/GetRcm2}
     */
    @Deprecated
    public static class FlexCompRowMatrixSolver extends FlexCompRowBand {

        public FlexCompRowMatrixSolver(int flag) {
            super(flag);
        }
    }

    public static class FlexCompRowBand implements EquationSolver {

        int flag;

        /**
         * Arg {@code flag} point out the type of {@code matrix} when invoking {@link #solve(no.uib.cipr.matrix.Matrix, no.uib.cipr.matrix.DenseVector) solve(matrix,vector)
         * }</br> If the {code matrix} is supposed to be symmetrical, flag
         * should be {@link MatrixUtils#SYMMETRICAL} or {@link MatrixUtils#UNSYMMETRICAL_BUT_MIRROR_FROM_UP_HALF}.
         * If it is certaint that the symmetrical {@code matrix} is spd, just
         * let {@code flag =flag&MatrixUtils.SPD}
         *
         * @param flag the {@code flag} for invoke {@link MatrixUtils#solveFlexCompRowMatrixByBandMethod(no.uib.cipr.matrix.sparse.FlexCompRowMatrix, no.uib.cipr.matrix.Vector, int, java.util.LinkedList)
         * } later.
         */
        public FlexCompRowBand(int flag) {
            this.flag = flag;
        }

        @Override
        public DenseVector solve(Matrix matrix, DenseVector vector) {
            FlexCompRowMatrix mat = (FlexCompRowMatrix) matrix;
            return MatrixUtils.solveFlexCompRowMatrixByBandMethod(mat, vector, flag);
        }
    }

    public static class SparseIterative implements EquationSolver {

        private boolean upperOnly;
        IterativeSolver solver;

        public SparseIterative(boolean upperOnly) {
            this.upperOnly=upperOnly;
        }

        @Override
        public DenseVector solve(Matrix matrix, DenseVector vector) {
            FlexCompRowMatrix actMat = new FlexCompRowMatrix(matrix.numRows(), matrix.numColumns());
            for (MatrixEntry me : matrix) {
                int row = me.row();
                int column = me.column();
                double value = me.get();
                actMat.set(row, column, value);
                if (upperOnly && row < column) {
                    actMat.set(column, row, value);
                }
            }
            DenseVector result = new DenseVector(vector.size());
            IterationMonitor iterMonitor=new DefaultIterationMonitor();
            if(null==solver){
                solver=new CGS(new DenseVector(result.size()));
                Preconditioner pc=new AMG();
                pc.setMatrix(actMat);
                solver.setPreconditioner(pc);
                solver.setIterationMonitor(iterMonitor);               
            }
            try {
                solver.solve(actMat, vector, result);
            } catch (IterativeSolverNotConvergedException ex) {
                Logger.getLogger(EquationSolvers.class.getName()).log(Level.SEVERE, null, ex);
                throw new IllegalStateException();
            }
            return result;
        }

        public SparseIterative(IterativeSolver solver, boolean upperOnly) {
            this.upperOnly = upperOnly;
            this.solver = solver;
        }

        @Override
        public String toString() {
            return "SparseIterative{" + "solver=" + solver + '}';
        }
    }
}
