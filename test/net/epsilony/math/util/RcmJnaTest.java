/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.math.util;

import no.uib.cipr.matrix.sparse.FlexCompRowMatrix;
import org.junit.AfterClass;
import org.junit.BeforeClass;

/**
 *
 * @author epsilon
 */
public class RcmJnaTest {

    public RcmJnaTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    int[] getPerm_01() {
        return new int[]{9, 1, 8, 6, 4, 7, 5, 3, 2, 10};
    }

    int[] getPermInv_01() {
        return new int[]{2, 9, 8, 5, 7, 4, 6, 3, 1, 10};
    }

    MatrixUtils.Adjacency getGraph_01() {
        int[] adjVec = new int[]{
            4, 6,
            3, 5, 7, 10,
            2, 4, 5,
            1, 3, 6, 9,
            2, 3, 7,
            1, 4, 7, 8,
            2, 5, 6, 8,
            6, 7,
            4,
            2};

        int[] adjRow = new int[]{1, 3, 7, 10, 14, 17, 21, 25, 27, 28, 29};
        return new MatrixUtils.Adjacency(adjRow, adjVec, 1);
    }

    FlexCompRowMatrix getMatrix_01() {
        FlexCompRowMatrix result;
        int[][] origin = new int[][]{
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
        result = new FlexCompRowMatrix(origin.length, origin.length);
        for (int i = 0; i < origin.length; i++) {
            for (int j = 0; j < origin[i].length; j++) {
                if (origin[i][j] != 0) {
                    result.set(i, j, origin[i][j]);
                }
            }
        }
        return result;
    }
}
