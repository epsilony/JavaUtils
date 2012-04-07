/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.math.util;

import gnu.trove.iterator.TIntIterator;
import gnu.trove.list.linked.TIntLinkedList;
import java.util.LinkedList;
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
 * @author epsilonyuan@gmail.com
 */
public class MatrixUtils {

    public static String stringPlot(Matrix matrix) {
        StringBuilder sb = new StringBuilder();
        String x = "X";
        String iS = "I";
        String zS = "0";
        String uS = "U";
        String ln = String.format("%n");
        for (int i = 0; i < matrix.numRows(); i++) {
            for (int j = 0; j < matrix.numColumns(); j++) {
                if (i == j) {
                    if(matrix.get(i,j)!=0){
                    sb.append(iS);}else{
                        sb.append(uS);
                    }
                    continue;
                }
                if (matrix.get(i, j) != 0) {
                    sb.append(x);
                } else {
                    sb.append(zS);
                }
            }
            sb.append(ln);
        }
        return sb.toString();
    }
    public static final byte UNSYMMETRICAL = 0x04;
    public static final byte SYMMETRICAL = 0x01;
    public static final byte UNSYMMETRICAL_BUT_MIRROR_FROM_UP_HALF = 0x02;
    public static final byte SPD = 0x10;

    /**
     * <p> 另请见<a href="http://epsilony.net/mywiki/Academic/RcmResources#Adjacency">MyWiki 上的释例程</a></p>
     * <p> 起始编号base为0时结点i的关联关系由：adjVec[adjRow[i]]至adjVec[adjRow[i+1]-1]表示</p>
     * <p> adjVec[adjRow[i]]至adjVec[adjRow[i+1]-1]不包含i,且可为空集 </p>
     * <p> adjVec[adjRow[i]]至adjVec[adjRow[i+1]-1]为严格的升序跋 </p>
     * <p> {@link #base} 默认为0 </p>
     */
    public static class Adjacency {

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
     * @param flag inMat本身是对称的:{@link #SYMMETRICAL} inMat本身不是对称的，但是其是一个上三角阵:{@link #UNSYMMETRICAL_BUT_MIRROR_FROM_UP_HALF} 若是SPD阵: &{@link #SPD}
     * @param base 返回的Adjacency有关的起始编号
     * @return 
     */
    public static Adjacency getAdjacency(FlexCompRowMatrix inMat, int flag, int base) {
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
        } else if ((flag & MatrixUtils.UNSYMMETRICAL_BUT_MIRROR_FROM_UP_HALF) != 0) {
            TIntLinkedList[] adjVecLists = new TIntLinkedList[nodeNum];
            for (int i = 0; i < nodeNum; i++) {
                adjVecLists[i] = new TIntLinkedList();
            }
            for (int i = 0; i < nodeNum; i++) {
                SparseVector vec = inMat.getRow(i);
                vec.compact();
                int[] indes = vec.getIndex();
                TIntLinkedList rowAdjList = adjVecLists[i];
                for (int j = 0; j < indes.length; j++) {
                    int index = indes[j];
                    if (index == i) {
                        continue;
                    }
                    rowAdjList.add(index);
                    adjVecLists[index].add(i);
                }
            }
            int adjVecSize = 0;
            for (int i = 0; i < nodeNum; i++) {
                adjVecSize += adjVecLists[i].size();
            }

            adjVec = new int[adjVecSize];
            adjRow = new int[nodeNum + 1];
            int startIndex = 0;
            for (int i = 0; i < nodeNum; i++) {
                int size = adjVecLists[i].size();
                adjVecLists[i].toArray(adjVec, 0, startIndex, size);
                adjRow[i] = startIndex;
                startIndex += size;
            }
            adjRow[nodeNum] = startIndex;
        } else {
            TIntLinkedList[] adjVecLists1 = new TIntLinkedList[nodeNum], adjVecLists2 = new TIntLinkedList[nodeNum];
            for (int i = 0; i < nodeNum; i++) {
                adjVecLists1[i] = new TIntLinkedList();
                adjVecLists2[i] = new TIntLinkedList();
            }
            for (int i = 0; i < nodeNum; i++) {
                SparseVector vec = inMat.getRow(i);
                vec.compact();
                int[] indes = vec.getIndex();
                TIntLinkedList rowList = adjVecLists1[i];
                for (int j = 0; j < indes.length; j++) {
                    int index = indes[j];
                    if (index == i) {
                        continue;
                    }
                    adjVecLists2[index].add(i);
                    rowList.add(index);
                }
            }

            int adjVecLength = 0;
            for (int i = 0; i < nodeNum; i++) {
                TIntLinkedList list = new TIntLinkedList();
                TIntLinkedList list1 = adjVecLists1[i];
                TIntLinkedList list2 = adjVecLists2[i];
                TIntIterator iter1 = list1.iterator();
                TIntIterator iter2 = list2.iterator();
                int index1 = 0, index2 = 0;
                boolean b1, b2;
                if (iter1.hasNext()) {
                    index1 = iter1.next();
                    b1 = true;
                } else {
                    b1 = false;
                }

                if (iter2.hasNext()) {
                    index2 = iter2.next();
                    b2 = true;
                } else {
                    b2 = false;
                }

                //merge two list 
                while (b1 || b2) {
                    if (b1 && b2) {
                        if (index1 < index2) {
                            list.add(index1);
                            if (iter1.hasNext()) {
                                index1 = iter1.next();
                            } else {
                                b1 = false;
                            }
                        } else if (index1 == index2) {
                            list.add(index1);
                            if (iter1.hasNext()) {
                                index1 = iter1.next();
                            } else {
                                b1 = false;
                            }
                            if (iter2.hasNext()) {
                                index2 = iter2.next();
                            } else {
                                b2 = false;
                            }
                        } else {
                            list.add(index2);
                            if (iter2.hasNext()) {
                                index2 = iter2.next();
                            } else {
                                b2 = false;
                            }
                        }
                    } else if (b1) {
                        list.add(index1);
                        if (iter1.hasNext()) {
                            index1 = iter1.next();
                        } else {
                            break;
                        }
                    } else {
                        list.add(index2);
                        if (iter2.hasNext()) {
                            index2 = iter2.next();
                        } else {
                            break;
                        }
                    }
                }

                adjVecLists1[i] = list;
                adjVecLength += list.size();
            }

            adjVec = new int[adjVecLength];
            adjRow = new int[nodeNum + 1];
            int startIndex = 0;
            for (int i = 0; i < adjVecLists1.length; i++) {
                int listSize = adjVecLists1[i].size();
                adjVecLists1[i].toArray(adjVec, 0, startIndex, listSize);
                adjRow[i] = startIndex;
                startIndex += listSize;
            }
            adjRow[nodeNum] = startIndex;
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

        @Override
        public String toString() {
            return "Bandwidth{" + "upBandwidth=" + upBandwidth + ", lowBandwidth=" + lowBandwidth + ", bandwidth" + (1 + upBandwidth + lowBandwidth) + '}';
        }
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
     * 带状化后的矩阵结果，bandedMatrix中(rcmResult.perm[i]，rcmResult.perm[j])即为原矩阵的(i,j)
     */
    public static class BandedResult {

        public BandedResult(Matrix bandedMatrix, RcmJna.RcmResult rcmResult, Bandwidth bandwith) {
            this.bandedMatrix = bandedMatrix;
            this.rcmResult = rcmResult;
            this.bandwith = bandwith;
        }
        public Matrix bandedMatrix;
        public RcmJna.RcmResult rcmResult;
        public MatrixUtils.Bandwidth bandwith;
    }

    /**
     * <p> 获得mat带状化后的矩阵与相关复排信息(BandedResult.permInv) </p>
     * <p> 采用RCM - Reverse Cuthill McKee Ordering方法 </p>
     * <p> 只依参数symmetric判定mat是否为对称阵
     * @param mat  要求方阵，否则结果可能不正确
     * @param flag inMat本身是对称的:{@link #SYMMETRICAL} inMat本身不是对称的，但是其是一个上三角阵:{@link #UNSYMMETRICAL_BUT_MIRROR_FROM_UP_HALF} 若是SPD阵: &{@link #SPD}
     * @return 
     */
    public static BandedResult getBandedMatrix(FlexCompRowMatrix mat, int flag) {
//        int[] perm = RcmJna.genrcm(mat, symmetric, 0);
        RcmJna.RcmResult rcmResult = RcmJna.genrcm2(mat, flag, 0);
        int[] perm = rcmResult.perm;
        int[] permInv = rcmResult.permInv;
        Bandwidth bandwidth = getBandwidthByInvPerm(mat, permInv);
        Matrix matrix;
        if ((flag & SYMMETRICAL) == SYMMETRICAL) {
            if ((flag & SPD) == SPD) {
                matrix = new UpperSPDBandMatrix(mat.numRows(), bandwidth.upBandwidth);
            } else {
                matrix = new UpperSymmBandMatrix(mat.numRows(), bandwidth.upBandwidth);
            }
        } else if ((flag & UNSYMMETRICAL_BUT_MIRROR_FROM_UP_HALF) == UNSYMMETRICAL_BUT_MIRROR_FROM_UP_HALF) {
            if ((flag & SPD) == SPD) {
                matrix = new UpperSPDBandMatrix(mat.numRows(), Math.max(bandwidth.lowBandwidth, bandwidth.upBandwidth));
            } else {
                matrix = new UpperSymmBandMatrix(mat.numRows(), Math.max(bandwidth.lowBandwidth, bandwidth.upBandwidth));
            }
        } else {
            matrix = new BandMatrix(mat.numRows(), bandwidth.lowBandwidth, bandwidth.upBandwidth);
        }

        if ((flag & UNSYMMETRICAL_BUT_MIRROR_FROM_UP_HALF) == UNSYMMETRICAL_BUT_MIRROR_FROM_UP_HALF) {
            for (MatrixEntry me : mat) {
                int row = permInv[me.row()];
                int col = permInv[me.column()];
                if (row <= col) {
                    matrix.set(row, col, me.get());
                } else {
                    matrix.set(col, row, me.get());
                }
            }
        } else if ((flag & SYMMETRICAL) == SYMMETRICAL) {
            for (MatrixEntry me : mat) {
                int row = permInv[me.row()];
                int col = permInv[me.column()];
                if (row <= col) {
                    matrix.set(row, col, me.get());
                }
            }
        } else {
            for (MatrixEntry me : mat) {
                int row = permInv[me.row()];
                int col = permInv[me.column()];
                matrix.set(row, col, me.get());
            }
        }
        return new BandedResult(matrix, rcmResult, bandwidth);
    }

    /**
     * 获取重排列permInv下的矩阵带宽。permInv使得mat中的元素(i,j)对应重排列后的带状矩阵元素(permInv[i],permInv[j])
     * @param mat 要求方阵，否则结果可能不正确
     * @param permInv 重排列数组，要求permInv中元素值在[0,mat.numRows()-1]内，如permInv需用于解方程组，则permInv是0,1,...,mat.nunRows()-1的一个排列。
     * @return 
     */
    public static Bandwidth getBandwidthByInvPerm(FlexCompRowMatrix mat, int[] permInv) {
        int upBandwidth = 0, lowBandwidth = 0;
        for (int rowI = 0; rowI < mat.numRows(); rowI++) {
            int[] rowIndes = mat.getRow(rowI).getIndex();
            int nodePermed = permInv[rowI];
            for (int colI = 0; colI < rowIndes.length; colI++) {
                int dis = permInv[rowIndes[colI]] - nodePermed;
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

    /**
     * 用RCM方法缩减矩阵带宽求解方程Ax=b
     * @param matA 满矩方阵 A
     * @param vecB b 向量
     * @param flag matA本身是对称的:{@link #SYMMETRICAL} matA本身不是对称的，但是其是一个上三角阵:{@link #UNSYMMETRICAL_BUT_MIRROR_FROM_UP_HALF} 若是SPD阵: &{@link #SPD}
     * @return Ax=b 的 结果 x
     */
    public static DenseVector solveFlexCompRowMatrixByBandMethod(FlexCompRowMatrix matA, Vector vecB, int flag) {
        return solveFlexCompRowMatrixByBandMethod(matA, vecB, flag, null);
    }

    /**
     * 用RCM方法缩减矩阵带宽求解方程Ax=b
     * @param matA 满矩方阵 A
     * @param vecB b 向量
     * @param flag matA本身是对称的:{@link #SYMMETRICAL} inMat本身不是对称的，但是其是一个上三角阵:{@link #UNSYMMETRICAL_BUT_MIRROR_FROM_UP_HALF} 若是SPD阵: &{@link #SPD}
     * @param bandedResultOutput 用以提取中间结果钩子，如为null则不矛理采。
     * @return Ax=b 的 结果 x
     */
    public static DenseVector solveFlexCompRowMatrixByBandMethod(FlexCompRowMatrix matA, Vector vecB, int flag, LinkedList<BandedResult> bandedResultOutput) {
        BandedResult bandedResult = getBandedMatrix(matA, flag);
        if (bandedResultOutput != null) {
            bandedResultOutput.add(bandedResult);
        }
        DenseVector vec = new DenseVector(vecB.size());
        for (int i = 0; i < vecB.size(); i++) {
            vec.set(bandedResult.rcmResult.permInv[i], vecB.get(i));
        }
        DenseVector resultUnPermed = (DenseVector) bandedResult.bandedMatrix.solve(vec, new DenseVector(matA.numRows()));
        for (int i = 0; i < vec.size(); i++) {
            vec.set(bandedResult.rcmResult.perm[i], resultUnPermed.get(i));
        }
        return vec;
    }

    public static boolean isSymmetric(FlexCompRowMatrix mat, double eps) {
        for (MatrixEntry me : mat) {
            if (me.get() - mat.get(me.column(), me.row()) > eps) {
                return false;
            }
        }
        return true;
    }

    /**
     * 
     * @param mat
     * @return {@link Bandwidth} 
     * @see Bandwidth
     */
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
