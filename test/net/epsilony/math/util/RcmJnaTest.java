/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.math.util;

import java.util.Arrays;
import java.util.Random;
import net.epsilony.math.util.MatrixUtils.Bandwidth;
import net.epsilony.math.util.RcmJna.RcmResult;
import no.uib.cipr.matrix.sparse.FlexCompRowMatrix;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

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

    /**
     * Test of genrcm method, of class RcmJna.
     */
    @Test
    public void testGenrcm() {
        System.out.println("genrcm");
        FlexCompRowMatrix inMat = getMatrix_01();
        boolean symmetric = true;
        int base = 1;
        int[] expResult = getPerm_01();
        int[] result = RcmJna.genrcm(inMat, symmetric, base);
        assertArrayEquals(expResult, result);

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

    @Test
    public void compareRcms() {
        int size = 200;
        int prop = 100;
        int propRange = 100;
        int repeat = 200;
        int sum=0,sum2=0;
        for (int i = 0; i < repeat; i++) {
            int transTime = new Random().nextInt(propRange) + prop;
            FlexCompRowMatrix matrix = MatrixUtilsTest.getRandomFullRankMatrix_02(size, transTime, true, false);
            matrix.compact();

            int[] perm = RcmJna.genrcm(matrix, true, size);
            int[] perm2 = RcmJna.genrcm2(matrix, true, size).perm;
            if (perm[perm.length - 1] == -1) {
                perm[perm.length - 1] = 0;
                System.out.println("bad!----------------------------");
            }
            Bandwidth bandOri = MatrixUtils.getBandwidth(matrix);
            Bandwidth band = MatrixUtils.getBandwidthByPerm(matrix, perm);
            Bandwidth band2 = MatrixUtils.getBandwidthByPerm(matrix, perm2);
            String f = "%s -up:%d -low:%d";
            System.out.println(String.format(f, "ori", bandOri.upBandwidth, bandOri.lowBandwidth));
            System.out.println(String.format(f, "1", band.upBandwidth, band.lowBandwidth));
            System.out.println(String.format(f, "2", band2.upBandwidth, band2.lowBandwidth));
            if(band.lowBandwidth>band2.lowBandwidth){
                sum2++;
            }else if(band.lowBandwidth<band2.lowBandwidth){
                sum++;
            }else{
                sum2++;
                sum++;
            }
        }
    }
}
