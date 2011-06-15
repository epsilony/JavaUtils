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
 * <p> if mat is banded to mat2 then:</p>
 * <p> mat{permInv[i],permInv[j]}=mat2{i,j}</p>
 * <p> mat2{perm[i],perm[j]}=mat{i,j}</p>
 * @see MatrixUtils
 * @author epsilon
 */
public class RcmJna {

    public static final int RCM_FORTRAN_INDICES = 1;
    public static final int RCM_C_INDICES = 2;
    public static final int RCM_NO_SORT = 4;
    public static final int RCM_INSERTION_SORT = 8;
    public static final int RCM_NO_REVERSE =  16;
    public static final int RCM_USE_MASK = 32;

    /**
     * may have bug in librcm.so
     */
    public interface Librcm extends Library {

        //librcm.so is complied by jni/rcm/RCMNetBeans/...
        Librcm INSTANCE = (Librcm) Native.loadLibrary("rcm",
                Librcm.class);

        void _Z6genrcmiiPiS_S_(int node_num, int adj_num, int[] adj_row, int[] adj, int[] perm);
    }

    public interface Librcm2 extends Library {

        //librcm.so is complied by jni/rcm/RcmNetBeans2/...
        Librcm2 INSTANCE = (Librcm2) Native.loadLibrary("rcm2", Librcm2.class);

        void genrcmi(final int n, final int flags, final int[] xadj, final int[] adj, int[] perm, byte[] mask, int[] deg);
    }


    public static int[] genrcm(FlexCompRowMatrix inMat, int flag, int base) {
        Adjacency adj = MatrixUtils.getAdjacency(inMat, flag, 1);
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
    
    public static int[] getPermInv(int[] perm){
        return getPermInv(perm,0);
    }
    
    public static class RcmResult {
        public int[] perm;
        public int[] permInv;
        public byte[] mask;
        public int[] deg;
        public int base=0;

        /**
         * 
         * @param perm 排列
         * @param mask 与perm一样长度的数组，用以记录结点是否被重排过，非0为重排过。
         * @param deg 结点的度，与perm一样长度
         */
        public RcmResult(int[] perm, byte[] mask, int[] deg) {
            this.perm = perm;
            this.mask = mask;
            this.deg = deg;
            permInv=getPermInv(perm, 0);
        }

        public RcmResult(int[] perm, byte[] mask, int[] deg,int base) {
            this.perm = perm;
            this.mask = mask;
            this.deg = deg;
            this.base=base;
            permInv=getPermInv(perm, base);
        }
        
        
    }
    
    /**
     * 利用RCM - Reverse Cuthill McKee Ordering获取带宽缩减信息
     * @param inMat 须是方阵
     * @param symmetric inMat是否是对称的
     * @param base 输出的perm中元素的起始编号
     * @return 
     */
    public static RcmResult genrcm2(FlexCompRowMatrix inMat, int flag, int base) {
        Adjacency adj = MatrixUtils.getAdjacency(inMat, flag, 0);
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
        return new RcmResult(perm, mask, deg,base);
    }
}
