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

    public static final int RCM_FORTRAN_INDICES = 1;
    public static final int RCM_C_INDICES = 2;
    public static final int RCM_NO_SORT = 4;
    public static final int RCM_INSERTION_SORT = 8;
    public static final int RCM_NO_REVERSE =  16;
    public static final int RCM_USE_MASK = 32;

    public interface Librcm extends Library {

        Librcm INSTANCE = (Librcm) Native.loadLibrary("rcm",
                Librcm.class);

        void _Z6genrcmiiPiS_S_(int node_num, int adj_num, int[] adj_row, int[] adj, int[] perm);
    }

    public interface Librcm2 extends Library {

        Librcm2 INSTANCE = (Librcm2) Native.loadLibrary("rcm2", Librcm2.class);

        void genrcmi(int n, int flags, int[] xadj, int[] adj, int[] perm, byte[] mask, int[] deg);
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

    public static class RcmResult {
        int[] perm;
        byte[] mask;
        int[] deg;

        public RcmResult(int[] perm, byte[] mask, int[] deg) {
            this.perm = perm;
            this.mask = mask;
            this.deg = deg;
        }
    }

    public static RcmResult genrcm2(FlexCompRowMatrix inMat, boolean symmetric, int base) {
        Adjacency adj = MatrixUtils.getAdjacencyVectors(inMat, symmetric, 0);
        int[] perm = new int[inMat.numRows()];
        byte[] mask=new byte[inMat.numRows()];
        int[] deg=new int[inMat.numRows()];
        Librcm2.INSTANCE.genrcmi(inMat.numRows(),0, adj.adjRow, adj.adjVec, perm,mask,deg);
        if (base != 0) {
            int delta = base - 0;
            for (int i = 0; i < perm.length; i++) {
                perm[i] += delta;
            }
        }
        return new RcmResult(perm, mask, deg);
    }
}
