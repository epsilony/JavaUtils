/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.math.util;

import no.uib.cipr.matrix.sparse.FlexCompRowMatrix;
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
     * Test of getUsed method, of class MatrixUtils.
     */
    //@Test
    public void testGetUsed() {
        System.out.println("getUsed");
        FlexCompRowMatrix inMat = null;
        int expResult = 0;
        int result = MatrixUtils.getUsed(inMat);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAdjacencyVectors method, of class MatrixUtils.
     */
    @Test
    public void testGetAdjacencyVectors() {
        System.out.println("getAdjacencyVectors");
        FlexCompRowMatrix inMat = getSymmetricTestMatrix_01();
        boolean symmetric = true;
        int base = 1;
        int[][] expResult = getSymmetricTestResult_01();
        int[][] result = MatrixUtils.getAdjacencyVectors(inMat, symmetric, base);
        assertArrayEquals(result[0], expResult[0]);
        assertArrayEquals(result[1],expResult[1]);
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

    public static int[][] getSymmetricTestResult_01() {
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
        int[] adjRow=new int[]{1,6,9,12,17,17,19,22,25,29,33};
        return new int[][]{adjRow,adjVec};
    }
}

