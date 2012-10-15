/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.utils.math;

import java.util.Arrays;
import static net.epsilony.utils.math.RcmMatrixSolver.*;
import no.uib.cipr.matrix.BandMatrix;
import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.sparse.FlexCompRowMatrix;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author epsilon
 */
public class RcmMatrixSolverTest {
    @Test
    public void testBandSolver() {
        
        FlexCompRowMatrix mat01 = getSymmetricTestMatrix_01();
        int[] bw = getBandWidth(mat01, SYMMETRIC);
        System.out.println("bw=" + Arrays.toString(bw));
        int[][] rcmRes = rcm(mat01, SYMMETRIC);
        int[] bww = getBandWidthByRcmResults(mat01, rcmRes);
        System.out.println("bww = " + Arrays.toString(bww));

        DenseMatrix mat02 = new DenseMatrix(mat01);
        bw = getBandWidth(mat02, SYMMETRIC);
        System.out.println("bw_mat02 = " + Arrays.toString(bw));
        int[][] rcmRes2 = rcm(mat02, SYMMETRIC);
        bww = getBandWidthByRcmResults(mat02, rcmRes2);
        System.out.println("bww = " + Arrays.toString(bww));

        BandMatrix bMat = getBandedMatrixByRcmResult(mat01, rcmRes);
        DenseVector f = new DenseVector(bMat.numRows());
        Arrays.fill(f.getData(), 1);
        DenseVector bandResult = (DenseVector) bMat.solve(f, new DenseVector(bMat.numColumns()));
        DenseVector bandCoverResult=recoveryBandedResultByRcmResult(bandResult, rcmRes);
        
        DenseVector expResult=(DenseVector) mat02.solve(f, new DenseVector(f.size()));
        
        System.out.println(Arrays.toString(bandCoverResult.getData()));
        System.out.println(Arrays.toString(expResult.getData()));
        
        assertArrayEquals(expResult.getData(), bandCoverResult.getData(),1e-10);
    }

        public static FlexCompRowMatrix getSymmetricTestMatrix_01() {
        int[][] orimatrix = new int[][]{
            {1, 0, 0, 4, 0, 6, 0, 0, 0, 0},
            {0, 2, 3, 0, 5, 0, 7, 0, 0, 10},
            {0, 2, 3, 4, 5, 0, 0, 0, 0, 0},
            {1, 0, 3, 4, 0, 6, 0, 0, 9, 0},
            {0, 2, 3, 0, 5, 0, 7, 0, 0, 0},
            {1, 0, 0, 4, 0, 6, 7, 8, 0, 0},
            {0, 2, 0, 0, 5, 6, 7, 8, 0, 0},
            {0, 0, 0, 0, 0, 6, 7, 8, 0, 0},
            {0, 0, 0, 4, 0, 0, 0, 0, 9, 0},
            {0, 2, 0, 0, 0, 0, 0, 0, 0, 10}
        };
        FlexCompRowMatrix result = new FlexCompRowMatrix(orimatrix.length, orimatrix.length);
        for (int rowI = 0; rowI < orimatrix.length; rowI++) {
            int[] row = orimatrix[rowI];
            for (int colI = 0; colI < row.length; colI++) {
                if (row[colI] != 0) {
                    result.set(rowI, colI, row[colI]);
                }
            }
        }

        return result;
    }
}
