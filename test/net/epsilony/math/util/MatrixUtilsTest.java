/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.math.util;

import no.uib.cipr.matrix.Matrix;
import no.uib.cipr.matrix.VectorEntry;
import java.util.Random;
import net.epsilony.math.util.MatrixUtils.BandedResult;
import net.epsilony.math.util.MatrixUtils.Bandwidth;
import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.MatrixEntry;
import no.uib.cipr.matrix.Vector;
import no.uib.cipr.matrix.sparse.FlexCompRowMatrix;
import org.apache.commons.math.random.JDKRandomGenerator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author epsilon
 */
public class MatrixUtilsTest {

    public MatrixUtilsTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getAdjacencyVectors method, of class MatrixUtils.
     */
    @Test
    public void testGetAdjacencyVectors() {
        System.out.println("getAdjacencyVectors");
        System.out.println("start testing symmetric base 1");
        FlexCompRowMatrix inMat = getSymmetricTestMatrix_01();
        boolean symmetric = true;
        int base = 1;
        MatrixUtils.Adjacency expResult = getSymmetricTestResult_01();
        MatrixUtils.Adjacency result = MatrixUtils.getAdjacencyVectors(inMat, symmetric, base);
        assertArrayEquals(result.adjRow, expResult.adjRow);
        assertArrayEquals(result.adjVec, expResult.adjVec);
        System.out.println("end of testing symmetric base 1");
        System.out.println("start testing unsymmetric base 1");
        inMat = getUnsymmetricTestMatrix_01();
        result = MatrixUtils.getAdjacencyVectors(inMat, false, base);
        assertArrayEquals(result.adjRow, expResult.adjRow);
        assertArrayEquals(result.adjVec, expResult.adjVec);
        System.out.println("end of testing unsymmetric base 1");
    }

    public static FlexCompRowMatrix getSymmetricTestMatrix_01() {
        int[][] orimatrix = new int[][]{
            {1, 0, 3, 4, 0, 0, 7, 8, 9, 0},
            {0, 2, 3, 4, 0, 0, 0, 0, 0, 10},
            {1, 2, 3, 0, 0, 0, 0, 8, 0, 0},
            {1, 2, 0, 4, 0, 6, 0, 0, 9, 10},
            {0, 0, 0, 0, 5, 0, 0, 0, 0, 0},
            {0, 0, 0, 4, 0, 6, 0, 8, 0, 0},
            {1, 0, 0, 0, 0, 0, 7, 0, 9, 10},
            {1, 0, 3, 0, 0, 6, 0, 8, 0, 0},
            {1, 0, 0, 4, 0, 0, 7, 0, 9, 10},
            {0, 2, 0, 4, 0, 0, 7, 0, 9, 10}
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

    public static MatrixUtils.Adjacency getSymmetricTestResult_01() {
        /*int[][] orimatrix = new int[][]{
        {1, 0, 3, 4, 0, 0, 7, 8, 9, 0},
        {0, 2, 3, 4, 0, 0, 0, 0, 0, 10},
        {1, 2, 3, 0, 0, 0, 0, 8, 0, 0},
        {1, 2, 0, 4, 0, 6, 0, 0, 9, 10},
        {0, 0, 0, 0, 5, 0, 0, 0, 0, 0},
        {0, 0, 0, 4, 0, 6, 0, 8, 0, 0},
        {1, 0, 0, 0, 0, 0, 7, 0, 9, 10},
        {1, 0, 3, 0, 0, 6, 0, 8, 0, 0},
        {1, 0, 0, 4, 0, 0, 7, 0, 9, 10},
        {0, 2, 0, 4, 0, 0, 7, 0, 9, 10}
        };*/
        int[] adjVec = new int[]{
            3, 4, 7, 8, 9,
            3, 4, 10,
            1, 2, 8,
            1, 2, 6, 9, 10,
            4, 8,
            1, 9, 10,
            1, 3, 6,
            1, 4, 7, 10,
            2, 4, 7, 9
        };
        int[] adjRow = new int[]{1, 6, 9, 12, 17, 17, 19, 22, 25, 29, 33};
        return new MatrixUtils.Adjacency(adjRow, adjVec, 1);
    }

    public static FlexCompRowMatrix getUnsymmetricTestMatrix_01() {
        int[][] orimatrix = new int[][]{
            {1, 0, 3, 0, 0, 0, 0, 8, 0, 0},
            {0, 2, 3, 4, 0, 0, 0, 0, 0, 10},
            {1, 0, 3, 0, 0, 0, 0, 8, 0, 0},
            {1, 0, 0, 4, 0, 6, 0, 0, 0, 0},
            {0, 0, 0, 0, 5, 0, 0, 0, 0, 0},
            {0, 0, 0, 4, 0, 6, 0, 8, 0, 0},
            {1, 0, 0, 0, 0, 0, 7, 0, 0, 0},
            {1, 0, 3, 0, 0, 6, 0, 8, 0, 0},
            {1, 0, 0, 4, 0, 0, 7, 0, 9, 0},
            {0, 2, 0, 4, 0, 0, 7, 0, 9, 10}
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

    /**
     * Test of getBandedMatrix method, of class MatrixUtils.
     */
    //@Test
    public void testGetBandedMatrix() {
        System.out.println("getBandedMatrix");
        FlexCompRowMatrix mat = null;
        boolean symmetric = false;
        BandedResult expResult = null;
        BandedResult result = MatrixUtils.getBandedMatrix(mat, symmetric);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getBandwidthByPerm method, of class MatrixUtils.
     */
    //@Test
    public void testGetBandwidthByPerm() {
        System.out.println("getBandwidthByPerm");
        FlexCompRowMatrix mat = null;
        int[] perm = null;
        Bandwidth expResult = null;
        Bandwidth result = MatrixUtils.getBandwidthByPerm(mat, perm);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of solveFlexCompRowMatrixByBandMethod method, of class MatrixUtils.
     */
    @Test
    public void testSolveFlexCompRowMatrixByBandMethod() {
        System.out.println("solveFlexCompRowMatrixByBandMethod");
        System.out.println("unsymmetric random case:");
        int repeat=200;
        System.out.println("repeat "+repeat+" times per case:");
        int size = 100;
        int propRange=10;
        int propBase=50;
        int prop;
        
        boolean symmetric = false;
        boolean spd = false;
        for(int i=0;i<repeat;i++){
            prop = new Random().nextInt(propRange) + propBase;
            testCase_02(size, prop, symmetric, spd);
        }
        System.out.println("symmetric random case");
        symmetric = true;
        for(int i=0;i<repeat;i++){
            prop = new Random().nextInt(propRange) + propBase;
            testCase_02(size, prop, symmetric, spd);
        }

        System.out.println("symmetric spd random case");
        spd = true;
        for(int i=0;i<repeat;i++){
            prop = new Random().nextInt(propRange) + propBase;
            testCase_02(size, prop, symmetric, spd);
        }
    }

    private void testCase_02(int size, int prop, boolean symmetric, boolean spd) {
        System.out.println("size:" + size + ", prop:" + prop);

        FlexCompRowMatrix mat = getRandomFullRankMatrix_02(size, prop, symmetric, spd);
        Vector b = new DenseVector(size);
        for (int i = 0; i < b.size(); i++) {
            b.set(i, 1);
        }

        if (symmetric) {
            for (MatrixEntry me : mat) {
                assertEquals(me.get(), mat.get(me.column(), me.row()), 1e-10);
            }
        }
        DenseMatrix denseMat = new DenseMatrix(mat);
        DenseVector expResult = (DenseVector) denseMat.solve(b, new DenseVector(size));
        DenseVector result = MatrixUtils.solveFlexCompRowMatrixByBandMethod(mat, b, symmetric, spd);
        assertArrayEquals(expResult.getData(), result.getData(), 1e-5);
    }

    public FlexCompRowMatrix getRandomFullRankMatrix_02(int numRow, int transTime, boolean symmetric, boolean spd) {
        FlexCompRowMatrix mat = new FlexCompRowMatrix(numRow, numRow);

        Random rand = new JDKRandomGenerator();
        //initiate the diagnal
        for (int i = 0; i < mat.numRows(); i++) {
            mat.set(i, i, rand.nextInt(100) + 1);
        }
        for (int i = 0; i < transTime; i++) {
            int from = rand.nextInt(numRow);
            int to = rand.nextInt(numRow);
            double scale = rand.nextDouble() - 0.5;

            if (symmetric) {
                for (VectorEntry ve : mat.getRow(from)) {
                    mat.add(to, ve.index(), ve.get() * scale);
                }

                for (int rowI = 0; rowI < numRow; rowI++) {
                    mat.add(rowI, to, scale * mat.get(rowI, from));
                }

            } else {

                if (rand.nextBoolean()) {
                    for (VectorEntry ve : mat.getRow(from)) {
                        mat.add(to, ve.index(), ve.get() * scale);
                    }
                } else {
                    for (int rowI = 0; rowI < numRow; rowI++) {
                        mat.add(rowI, to, scale * mat.get(rowI, from));
                    }
                }

            }
        }
        if (spd) {
            Matrix transpose = mat.transpose(new FlexCompRowMatrix(numRow, numRow));
            mat = (FlexCompRowMatrix) transpose.mult(mat, new FlexCompRowMatrix(numRow, numRow));
        }
        return mat;

    }
}