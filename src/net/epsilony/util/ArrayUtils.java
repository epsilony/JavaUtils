/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.util;

import java.util.Arrays;

/**
 *
 * @author epsilon
 */
public class ArrayUtils {

    /**
     * 或取一个二维数组的某列
     * @param matrix
     * @param col 列号
     * @return 获得一个二维数组的第col列的Copy 
     */
    public static double[] getMatrixColumn(double[][] matrix, int col) {
        int n = matrix.length;
        double[] results = new double[n];
        for (int i = 0; i < n; i++) {
            results[i] = matrix[i][col];
        }
        return results;
    }

    public static String toString(double[][] matrix) {
        String t = new String();
        for (int i = 0; i < matrix.length; i++) {
            t += Arrays.toString(matrix[i]);
            if (i != matrix.length - 1) {
                t += System.getProperty("line.separator");
            }
        }
        return t;
    }
}
