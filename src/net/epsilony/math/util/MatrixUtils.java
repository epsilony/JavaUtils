/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.math.util;

import no.uib.cipr.matrix.BandMatrix;
import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.Matrix;
import no.uib.cipr.matrix.MatrixEntry;
import no.uib.cipr.matrix.UpperSPDBandMatrix;
import no.uib.cipr.matrix.UpperSymmBandMatrix;
import no.uib.cipr.matrix.Vector;
import no.uib.cipr.matrix.sparse.FlexCompRowMatrix;
import no.uib.cipr.matrix.sparse.SparseVector;

/**
 *
 * @author epsilon
 */
public class MatrixUtils {

    public static final byte UNSYMMETRICAL = 0x02;
    public static final byte SYMMETRICAL = 0x01;
//    public static final byte UNSYMMETRICAL_AND_COMPLETE = 0x02;
    public static final byte SYMMETRICAL_BUT_RECORD_ONLY_UP_HALF = 0x06;
//   public static final byte SYMMETRIAL_BUT_RECORD_ONLY_LOW_HALF=0x0B;
    public static final byte SPD = 0x10;

    /**
     * 关连信息
     */
    public static class Adjacency {

        /**
         * <p> 起始编号base为0时结点i的关联关系由：adjVec[adjRow[i]]至adjVec[adjRow[i+1]-1]表示</p>
         * <p> adjVec[adjRow[i]]至adjVec[adjRow[i+1]-1]不包含i,且可为空集 </p>
         * <p> adjVec[adjRow[i]]至adjVec[adjRow[i+1]-1]为严格的升序跋 </p>
         * <p> base 默认为0 </p>
         */
        public int[] adjRow;
        public int[] adjVec;
        public int base = 0;

        public Adjacency(int[] adjRow, int[] adjVec, int base) {
            this.adjRow = adjRow;
            this.adjVec = adjVec;
            this.base = base;
        }

        public Adjacency(int[] adjRow, int[] adjVec) {
            this.adjRow = adjRow;
            this.adjVec = adjVec;
        }
    }

    /**
     * 计数inMat阵中的有用元素个数，计数前将调用inMat.compact()
     */
    public static int getUsed(FlexCompRowMatrix inMat) {
        return getUsed(inMat, true);
    }

    /**
     * 计数inMat阵中的有用元素个数
     * @param compactBefore 是否在计数之前调用inMat.compact()
     * @return 
     */
    public static int getUsed(FlexCompRowMatrix inMat, boolean compactBefore) {
        if (compactBefore) {
            inMat.compact();
        }
        int sumUsed = 0;
        for (int row = 0; row < inMat.numRows(); row++) {
            sumUsed += inMat.getRow(row).getUsed();
        }
        return sumUsed;
    }

    /**
     * 获取inMat的Adjacency 
     * @param inMat  要求方阵，否则结果可能不正确
     * @param isInMatSymmetrical inMat 本身是否是对称的
     * @param base 返回的Adjacency有关的起始编号
     * @param fakeSymmetrilize 对于非对称的inMat改为输出inMat.^2+(inMat.^2)'的Adjacency
     * @return 
     */
    public static Adjacency getAdjacency(FlexCompRowMatrix inMat, byte flag, int base) {
        int[] adjVec, adjRow;
        int nodeNum = inMat.numRows();
        adjRow = new int[nodeNum + 1];
//        if (isInMatSymmetrical||!fakeSymmetrilize) {
        if ((flag & MatrixUtils.SYMMETRICAL) != 0) {
            int adjNum = getUsed(inMat) - nodeNum;
            adjVec = new int[adjNum];
            for (int rowI = 0, adjVecIndex = 0; rowI < inMat.numRows(); rowI++) {
                adjRow[rowI] = adjVecIndex;
                SparseVector rowVec = inMat.getRow(rowI);
                int[] rowDataIndex = rowVec.getIndex();

                for (int i = 0; i < rowDataIndex.length; i++) {
                    if (rowDataIndex[i] == rowI) {
                        continue;
                    }
                    adjVec[adjVecIndex++] = rowDataIndex[i];
                }
            }
            adjRow[nodeNum] = adjNum;
        } else {
            FlexCompRowMatrix inMatTrans = (FlexCompRowMatrix) inMat.transpose(new FlexCompRowMatrix(nodeNum, nodeNum));
            // Count the adjacency:
            int adjNum = 0;
            for (int rowI = 0; rowI < inMat.numRows(); rowI++) {
                SparseVector rowVec = inMat.getRow(rowI);
                SparseVector colVec = inMatTrans.getRow(rowI);
                int rowIndesI = 0;
                int colIndesI = 0;
                int[] rowIndes = rowVec.getIndex();
                int[] colIndes = colVec.getIndex();
                int rowIndex, colIndex;
                boolean rowHasNext, colHasNext;
                rowHasNext = rowIndesI < rowIndes.length;
                colHasNext = colIndesI < colIndes.length;
                while (rowHasNext && colHasNext) {
                    rowIndex = rowIndes[rowIndesI];
                    colIndex = colIndes[colIndesI];
                    if (rowIndex < colIndex) {
                        rowIndesI++;
                    } else if (rowIndex > colIndex) {
                        colIndesI++;
                    } else {
                        colIndesI++;
                        rowIndesI++;
                    }
                    adjNum++;
                    rowHasNext = rowIndesI < rowIndes.length;
                    colHasNext = colIndesI < colIndes.length;
                }
                if (rowHasNext) {
                    adjNum += rowIndes.length - rowIndesI;
                }
                if (colHasNext) {
                    adjNum += colIndes.length - colIndesI;
                }
            }
            adjNum -= nodeNum;

            adjVec = new int[adjNum];

            //fill the adjRow and adjVec
            for (int rowI = 0, adjVecIndex = 0; rowI < inMat.numRows(); rowI++) {
                SparseVector rowVec = inMat.getRow(rowI);
                SparseVector colVec = inMatTrans.getRow(rowI);
                int rowIndesI = 0;
                int colIndesI = 0;
                adjRow[rowI] = adjVecIndex;
                int[] rowIndes = rowVec.getIndex();
                int[] colIndes = colVec.getIndex();
                int rowIndex, colIndex;
                boolean rowHasNext, colHasNext;
                rowHasNext = rowIndesI < rowIndes.length;
                colHasNext = colIndesI < colIndes.length;
                while (rowHasNext && colHasNext) {
                    rowIndex = rowIndes[rowIndesI];
                    colIndex = colIndes[colIndesI];
                    if (rowIndex < colIndex) {
                        rowIndesI++;
                        adjVec[adjVecIndex++] = rowIndex;
                    } else if (rowIndex > colIndex) {
                        adjVec[adjVecIndex++] = colIndex;
                        colIndesI++;
                    } else {
                        if (rowI != rowIndex) {
                            adjVec[adjVecIndex++] = rowIndex;
                        }
                        colIndesI++;
                        rowIndesI++;
                    }
                    rowHasNext = rowIndesI < rowIndes.length;
                    colHasNext = colIndesI < colIndes.length;
                }
                if (rowHasNext) {
                    for (int i = rowIndesI; i < rowIndes.length; i++) {
                        rowIndex = rowIndes[i];
                        adjVec[adjVecIndex++] = rowIndex;
                    }
                }
                if (colHasNext) {
                    for (int i = colIndesI; i < colIndes.length; i++) {
                        colIndex = colIndes[i];
                        adjVec[adjVecIndex++] = colIndex;
                    }
                }
            }
            adjRow[nodeNum] = adjNum;
        }

        if (0 != base) {
            for (int i = 0; i < adjVec.length; i++) {
                adjVec[i] += base;
            }
            for (int i = 0; i < adjRow.length; i++) {
                adjRow[i] += base;
            }
        }
        return new Adjacency(adjRow, adjVec, base);
    }

    /**
     * 带宽信息，upBandwidth: 矩阵上半部分不包括对角元素的带宽，lowBandwidth: 矩阵下半部分不包括对角元素的带宽。
     */
    public static class Bandwidth {

        public int upBandwidth, lowBandwidth;

        public Bandwidth(int upBandwidth, int lowBandwidth) {
            this.upBandwidth = upBandwidth;
            this.lowBandwidth = lowBandwidth;
        }

        public int getBandwidth() {
            return upBandwidth + lowBandwidth + 1;
        }
    }

    /**
     * 带状化后的矩阵结果，bandedMatrix中(permInv[i]，perInv[j])即为原矩阵的(i,j)
     */
    public static class BandedResult {

        public BandedResult(Matrix bandedMatrix, int[] permInv, Bandwidth bandwith) {
            this.bandedMatrix = bandedMatrix;
            this.permInv = permInv;
            this.bandwith = bandwith;
        }
        public Matrix bandedMatrix;
        public int[] permInv;
        public MatrixUtils.Bandwidth bandwith;
    }

    /**
     * <p> 获得mat带状化后的矩阵与相关复排信息(BandedResult.permInv) </p>
     * <p> 采用RCM - Reverse Cuthill McKee Ordering方法 </p>
     * <p> 只依参数symmetric判定mat是否为对称阵
     * @param mat  要求方阵，否则结果可能不正确
     * @param symmetric
     * @return 
     */
    public static BandedResult getBandedMatrix(FlexCompRowMatrix mat, byte flag) {
//        int[] perm = RcmJna.genrcm(mat, symmetric, 0);
        int[] perm = RcmJna.genrcm2(mat, flag, 0).perm;
        int[] permInv = RcmJna.getPermInv(perm, 0);
        Bandwidth bandwidth = getBandwidthByPerm(mat, perm);
        Matrix matrix;
        if ((flag & SYMMETRICAL) != 0||(flag & SYMMETRICAL_BUT_RECORD_ONLY_UP_HALF)==SYMMETRICAL_BUT_RECORD_ONLY_UP_HALF) {
            if ((flag & SPD) != 0) {
                matrix = new UpperSPDBandMatrix(mat.numRows(), bandwidth.upBandwidth);
            } else {
                matrix = new UpperSymmBandMatrix(mat.numRows(), bandwidth.upBandwidth);
            }
        } else {
            matrix = new BandMatrix(mat.numRows(), bandwidth.lowBandwidth, bandwidth.upBandwidth);
        }

        if ((flag & SYMMETRICAL_BUT_RECORD_ONLY_UP_HALF) == SYMMETRICAL_BUT_RECORD_ONLY_UP_HALF) {
            for (MatrixEntry me : mat) {
                int row = perm[me.row()];
                int col = perm[me.column()];
                if (row < col) {
                    matrix.set(row, col, me.get());
                } else {
                    matrix.set(col, row, me.get());
                }
            }
        } else if ((flag & SYMMETRICAL) != 0) {
            for (MatrixEntry me : mat) {
                int row = perm[me.row()];
                int col = perm[me.column()];
                if (row < col) {
                    matrix.set(row, col, me.get());
                }
            }
        } else {
            for (MatrixEntry me : mat) {
                int row = perm[me.row()];
                int col = perm[me.column()];
                matrix.set(row, col, me.get());
            }
        }
        return new BandedResult(matrix, permInv, bandwidth);
    }

    /**
     * 获取重排列perm下的矩阵带宽。perm使得mat中的元素(i,j)对应重排列后的矩阵元素(perm[i],perm[j])
     * @param mat 要求方阵，否则结果可能不正确
     * @param perm 重排列数组，要求perm中元素值在[0,mat.numRows()-1]内，如perm需用于解方程组，则perm是0,1,...,mat.nunRows()-1的一个排列。
     * @return 
     */
    public static Bandwidth getBandwidthByPerm(FlexCompRowMatrix mat, int[] perm) {
        int upBandwidth = 0, lowBandwidth = 0;
        for (int rowI = 0; rowI < mat.numRows(); rowI++) {
            int[] rowIndes = mat.getRow(rowI).getIndex();
            int nodePermed = perm[rowI];
            for (int colI = 0; colI < rowIndes.length; colI++) {
                int dis = perm[rowIndes[colI]] - nodePermed;
                if (dis < lowBandwidth) {
                    lowBandwidth = dis;
                } else if (dis > upBandwidth) {
                    upBandwidth = dis;
                }
            }
        }
        lowBandwidth = -lowBandwidth;
        return new Bandwidth(upBandwidth, lowBandwidth);
    }

    public static DenseVector solveFlexCompRowMatrixByBandMethod(FlexCompRowMatrix mat, Vector b, byte flag) {
        BandedResult bandedResult = getBandedMatrix(mat, flag);
        DenseVector bx;
        bx = (DenseVector) bandedResult.bandedMatrix.solve(b, new DenseVector(mat.numRows()));
        DenseVector x = new DenseVector(bx.size());
        for (int i = 0; i < x.size(); i++) {
            x.set(bandedResult.permInv[i], bx.get(i));
        }
        return x;
    }

    public static boolean isSymmetric(FlexCompRowMatrix mat, double eps) {
        for (MatrixEntry me : mat) {
            if (me.get() - mat.get(me.column(), me.row()) > eps) {
                return false;
            }
        }
        return true;
    }

    public static Bandwidth getBandwidth(FlexCompRowMatrix mat) {
        int low = 0, up = 0;
        for (int rowI = 0; rowI < mat.numRows(); rowI++) {
            int[] rowIndes = mat.getRow(rowI).getIndex();
            int t = rowIndes[0] - rowI;
            if (t < low) {
                low = t;
            }
            t = rowIndes[rowIndes.length - 1] - rowI;
            if (t > up) {
                up = t;
            }
        }
        low = -low;
        return new Bandwidth(up, low);
    }
}
