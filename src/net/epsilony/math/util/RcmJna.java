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
 * <p>RCM - Reverse Cuthill McKee Ordering获取带宽缩减信息</p>
 * <p> if mat is banded to mat2 then:</p>
 * <p> mat{permInv[i],permInv[j]}=mat2{i,j}</p>
 * <p> mat2{perm[i],perm[j]}=mat{i,j}</p>
 * @see MatrixUtils
 * @author epsilon


 */
public class RcmJna {

    /**
     * <p>调用John Burkardt 所作的RCM c++实现 so 库的接口程序</p>
     * <p> 该RCM C++实现下载自
     * <a href=”http://people.sc.fsu.edu/~jburkardt/cpp_src/rcm/rcm.html">
     * John Burkardt 的RCM c++实现
     * </a> 并由Man YUAN 做了小小的patch。patch后的源文件与编译包将生成<strong> librcm.so </strong>可于
     * <a href="http://epsilony.net/mywiki/Academic/RcmResources#John">Man Yuan的wiki</a>
     * 下载。</p>
     * <p> librcm.so 也可由 svn://epsilony.net/epsilonRepos/jni/rcm/RCMNetBeans 项目编译获得 </p>
     * <p> 在同样有的库时，建议使用{@link RcmJna.Librcm2 Librcm2}以及{@link RcmJna#genrcm2 gnercm2}</p>
     */
    public interface Librcm extends Library {

        //librcm.so is complied by jni/rcm/RCMNetBeans/...
        Librcm INSTANCE = (Librcm) Native.loadLibrary("rcm",
                Librcm.class);

        /**
         * 该函数对应原C++函数genrcm，
         * @param node_num 结点数，即矩阵的行数
         * @param adj_num adj数组的长度
         * @param adj_row 见<a href="http://epsilony.net/mywiki/Academic/RcmResouces#Adjacency">
         *        MyWiki 上的解释。</a> <strong> 注意这里Base Index 不为0，为1</strong>
         * @param adj 见<a href="http://epsilony.net/mywiki/Academic/RcmResouces#Adjacency">
         *        MyWiki 上的解释。</a> <strong> 注意这里Base Index 不为0，为1</strong>
         * @param perm 输出见<a href="http://epsilony.net/mywiki/Academic/RcmResources#John">MyWiki 上的解释</a> <strong> 注意这里Base Index 不为0，为1</strong>
         */
        void _Z6genrcmiiPiS_S_(int node_num, int adj_num, int[] adj_row, int[] adj, int[] perm);
    }

    /**
     * <p> 该接口的C语言后台下载自
     * <a href=”http://www.math.temple.edu/~daffi/software/rcm/">
     * David Fritzsche 的RCM C实现</a>
     * 一个便于编译中librcm2.so的Netbeans C项目可以从<a href="http://epsilony.net/mywiki/Academic/RcmResources#John">Man Yuan的wiki</a>
     * 下载。</p>
     * <p> librcm2.so 也可由 svn://epsilony.net/epsilonRepos/jni/rcm/Rcm2NetBeans 项目编译获得 </p>     */
    public interface Librcm2 extends Library {

        public static final int RCM_FORTRAN_INDICES = 1;
        public static final int RCM_C_INDICES = 2;
        public static final int RCM_NO_SORT = 4;
        public static final int RCM_INSERTION_SORT = 8;
        public static final int RCM_NO_REVERSE = 16;
        public static final int RCM_USE_MASK = 32;
        //librcm2.so is complied by jni/rcm/Rcm2NetBeans/
        Librcm2 INSTANCE = (Librcm2) Native.loadLibrary("rcm2", Librcm2.class);

        /**
         * 该函数对应原C函数<a href="http://www.math.temple.edu/~daffi/software/rcm/doxy/html/rcm_8h.html#a7">genrcmi </a>
         * @param n 矩阵的行数
         * @param flags {@link #RCM_FORTRAN_INDICES} 一般采用默认值0及可
         * @param adjRow 见<a href="http://epsilony.net/mywiki/Academic/RcmResouces#Adjacency">
         *        MyWiki 上的解释。</a>
         * @param adjVec 见<a href="http://epsilony.net/mywiki/Academic/RcmResouces#Adjacency">
         *        MyWiki 上的解释。</a>
         * @param perm 见<a href="http://epsilony.net/mywiki/Academic/RcmResouces#Adjacency">
         *        MyWiki 上的解释。</a>
         * @param mask 输入时用于标记哪个结点不重排，输出时表明哪个结点被重排过了。
         * @param deg 结点的度
         */
        void genrcmi(final int n, final int flags, final int[] adjRow, final int[] adjVec, int[] perm, byte[] mask, int[] deg);
    }

    /**
     * 调用John Burkardt的C++实现
     * @param inMat
     * @param flag {@link MatrixUtils#SPD MatrixlUtils}中的flag <strong> 非本类的调用librcm2.genrcmi的flag参数</strong>
     * @param base 反回的数列的起始编号 index base
     * @return 起始编号为base的perm 见<a href="http://epsilony.net/mywiki/Academic/RcmResources#John">MyWiki 上的解释</a>
     */
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

    /**
     * 由perm获取permInv这两者的关系请见<a href="http://epsilony.net/mywiki/Academic/RcmResources#John">MyWiki 上的解释</a>
     * @param perm
     * @param base perm的index base
     * @return permInv 其base index 等同于perm，即base
     */
    public static int[] getPermInv(int[] perm, int base) {
        int[] permInv = new int[perm.length];
        for (int i = 0; i < perm.length; i++) {
            permInv[perm[i] - base] = i + base;
        }
        return permInv;
    }

    /**
     * <p>由perm获取permInv这两者的关系请见<a href="http://epsilony.net/mywiki/Academic/RcmResources#John">MyWiki 上的解释</a></p>
     * <p>输入与返回的数列其<strong> Index Base 必须为 0</strong> </p>
     * @param perm
     * @return permInv
     */
    public static int[] getPermInv(int[] perm) {
        return getPermInv(perm, 0);
    }

    /**
     * 专用于genrcm2返回结果用
     */
    public static class RcmResult {

        /**
         * 见<a href="http://epsilony.net/mywiki/Academic/RcmResources#John">MyWiki 上的解释</a>
         */
        public int[] perm;
        /**
         * 见<a href="http://epsilony.net/mywiki/Academic/RcmResources#John">MyWiki 上的解释</a>
         */
        public int[] permInv;
        public byte[] mask;
        public int[] deg;
        public int base = 0;

        /**
         * 
         * @param perm 见<a href="http://epsilony.net/mywiki/Academic/RcmResources#John">MyWiki 上的解释</a>
         * @param mask 见<a href="http://www.math.temple.edu/~daffi/software/rcm/doxy/html/rcm_8h.html#a7">genrcmi原生文档</a>
         * @param deg 结点的度，与perm一样长度
         */
        public RcmResult(int[] perm, byte[] mask, int[] deg) {
            this.perm = perm;
            this.mask = mask;
            this.deg = deg;
            permInv = getPermInv(perm, 0);
        }

        /**
         * @param perm 见<a href="http://epsilony.net/mywiki/Academic/RcmResources#John">MyWiki 上的解释</a>
         * @param mask 见<a href="http://www.math.temple.edu/~daffi/software/rcm/doxy/html/rcm_8h.html#a7">genrcmi原生文档</a>
         * @param deg 结点的度，与perm一样长度
         * @param base deg perm 与 permInv的index base
         */
        public RcmResult(int[] perm, byte[] mask, int[] deg, int base) {
            this.perm = perm;
            this.mask = mask;
            this.deg = deg;
            this.base = base;
            permInv = getPermInv(perm, base);
        }
    }

    /**
     * 利用RCM - Reverse Cuthill McKee Ordering获取带宽缩减信息
     * @param inMat 须是方阵
     * @param {@link MatrixUtils#SPD MatrixlUtils}中的flag <strong> 非调用Jna genrcmi所用的flag </strong>
     * @param base 输出的结果中permInv与perm中元素的起始编号
     * @return 
     */
    public static RcmResult genrcm2(FlexCompRowMatrix inMat, int flag, int base) {
        Adjacency adj = MatrixUtils.getAdjacency(inMat, flag, 0);
        int[] perm = new int[inMat.numRows()];
        byte[] mask = new byte[inMat.numRows()];
        int[] deg = new int[inMat.numRows()];
        Librcm2.INSTANCE.genrcmi(inMat.numRows(), 0, adj.adjRow, adj.adjVec, perm, mask, deg);
        if (base != 0) {
            int delta = base - 0;
            for (int i = 0; i < perm.length; i++) {
                perm[i] += delta;
            }
        }
        return new RcmResult(perm, mask, deg, base);
    }
}
