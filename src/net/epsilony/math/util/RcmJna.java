/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.math.util;

import com.sun.jna.Library;
import com.sun.jna.Native;
import net.epsilony.math.util.MatrixUtils.Adjacency;
import no.uib.cipr.matrix.sparse.FlexCompRowMatrix;

/**
 *
 * @author epsilon
 */
public class RcmJna {

    public interface Librcm extends Library {

        Librcm INSTANCE = (Librcm) Native.loadLibrary("rcm",
                Librcm.class);

        void _Z6genrcmiiPiS_S_(int node_num, int adj_num, int[] adj_row, int[] adj, int[] perm);
    }

    public static int[] genrcm(FlexCompRowMatrix inMat, boolean symmetric, int base) {
        Adjacency adj = MatrixUtils.getAdjacencyVectors(inMat, symmetric, 1);
        int[] perm = new int[inMat.numRows()];
        Librcm.INSTANCE._Z6genrcmiiPiS_S_(inMat.numRows(), adj.adjVec.length, adj.adjRow, adj.adjVec, perm);
        if (base != 1) {
            int delta = base - 1;
            for (int i = 0; i < perm.length; i++) {
                perm[i] += delta;
            }
        }
        return perm;
    }

    public static int[] getPermInv(int[] perm, int base) {
        int[] permInv = new int[perm.length];
        for (int i = 0; i < perm.length; i++) {
            permInv[perm[i] - base] = i + base;
        }
        return permInv;
    }
}
