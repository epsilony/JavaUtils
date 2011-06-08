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

    public static class Adjacency {

        public int[] adjRow;
        public int[] adjVec;
        public int base;

        public Adjacency(int[] adjRow, int[] adjVec, int base) {
            this.adjRow = adjRow;
            this.adjVec = adjVec;
            this.base = base;
        }
    }

    public static int getUsed(FlexCompRowMatrix inMat) {
        int sumUsed = 0;
        for (int row = 0; row < inMat.numRows(); row++) {
            sumUsed += inMat.getRow(row).getUsed();
        }
        return sumUsed;
    }

    public static Adjacency getAdjacencyVectors(FlexCompRowMatrix inMat, boolean symmetric, int base) {
        int[] adjVec, adjRow;
        int nodeNum = inMat.numRows();
        adjRow = new int[nodeNum + 1];
        if (symmetric) {
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

    public static class Bandwidth {

        public int upBandwidth, lowBandwidth;

        public Bandwidth(int upBandwidth, int lowBandwidth) {
            this.upBandwidth = upBandwidth;
            this.lowBandwidth = lowBandwidth;
        }
    }

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

    public static BandedResult getBandedMatrix(FlexCompRowMatrix mat, boolean symmetric) {
        int[] perm = RcmJna.genrcm2(mat, symmetric, 0).perm;
        int[] permInv = RcmJna.getPermInv(perm, 0);
        Bandwidth bandwidth = getBandwidthByPerm(mat, perm);
        Matrix matrix;
        if (symmetric) {
            matrix = new UpperSymmBandMatrix(mat.numRows(), bandwidth.upBandwidth);

        } else {
            matrix = new BandMatrix(mat.numRows(), bandwidth.lowBandwidth, bandwidth.upBandwidth);
        }

        for (MatrixEntry me : mat) {
            matrix.set(perm[me.row()], perm[me.column()], me.get());
        }
        return new BandedResult(matrix, permInv,bandwidth);
    }

    public static Bandwidth getBandwidthByPerm(FlexCompRowMatrix mat, int[] perm) {
        int upBandwidth = 0, lowBandwidth = 0;
        for (int rowI = 0; rowI < mat.numRows(); rowI++) {
            int[] rowIndes = mat.getRow(rowI).getIndex();
            int nodePermed = perm[rowI];
            for (int colI = 0; colI < rowIndes.length; colI++) {
                int dis = perm[rowIndes[colI]]-nodePermed;
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

    public static DenseVector solveFlexCompRowMatrixByBandMethod(FlexCompRowMatrix mat, Vector b, boolean symmetric, boolean spd) {
        BandedResult bandedResult = getBandedMatrix(mat, symmetric);
        DenseVector bx;
        if (!spd) {
            bx = (DenseVector) bandedResult.bandedMatrix.solve(b, new DenseVector(mat.numRows()));
        } else {
            bx = (DenseVector) new UpperSPDBandMatrix(bandedResult.bandedMatrix, bandedResult.bandwith.upBandwidth,false).solve(b, new DenseVector(mat.numRows()));  
        }
        DenseVector x = new DenseVector(bx.size());
        for (int i = 0; i < x.size(); i++) {
            x.set(bandedResult.permInv[i], bx.get(i));
        }
        return x;
    }
}
