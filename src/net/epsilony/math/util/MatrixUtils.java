/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.math.util;

import no.uib.cipr.matrix.sparse.FlexCompRowMatrix;
import no.uib.cipr.matrix.sparse.SparseVector;

/**
 *
 * @author epsilon
 */
public class MatrixUtils {

    public static int getUsed(FlexCompRowMatrix inMat) {
        int sumUsed = 0;
        for (int row = 0; row < inMat.numRows(); row++) {
            sumUsed += inMat.getRow(row).getUsed();
        }
        return sumUsed;
    }

    public static int[][] getAdjacencyVectors(FlexCompRowMatrix inMat, boolean symmetric, int base) {
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
        return new int[][]{adjRow,adjVec};
    }
}
