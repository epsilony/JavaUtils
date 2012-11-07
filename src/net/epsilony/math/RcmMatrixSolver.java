/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.math;

import gnu.trove.iterator.TIntIterator;
import gnu.trove.list.array.TIntArrayList;
import java.util.LinkedList;
import no.uib.cipr.matrix.BandMatrix;
import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.Matrix;
import no.uib.cipr.matrix.MatrixEntry;
import no.uib.cipr.matrix.Vector;
import no.uib.cipr.matrix.VectorEntry;
import no.uib.cipr.matrix.sparse.FlexCompRowMatrix;
import no.uib.cipr.matrix.sparse.SparseVector;

/**
 * A matrix solver based on </br>
 * 1. reverse Cuthill–McKee algorithm (RCM)
 * {@link http://en.wikipedia.org/wiki/Cuthill%E2%80%93McKee_algorithm} & 2. mtj package
 * {@link http://code.google.com/p/matrix-toolkits-java/}
 *
 * <p>
 * The all-in-one operation is {@link #bandSolve(no.uib.cipr.matrix.Matrix, int, no.uib.cipr.matrix.Vector) }, which is based on {@link BandMatrix} of MTJ.
 * Only symmetrical or unsymmetrical matrix are supported, no special optimization for half-triangle symmetric nor spd matrix. The character of matrix is set by 
 * parameter {@code flag} and the values should be {@link #SYMMETRIC} or {@link #UNSYMMETRIC}
 * </p>
 * 
 * @version 0.1
 * @author epsilonyuan@gmail.com
 */
public class RcmMatrixSolver {

    public static final int SYMMETRIC = 1, UNSYMMETRIC = 2;//, SYMMETRIC_BUT_ONLY_UP_HALF = 3, SYMMETRIC_BUT_ONLY_DOWN_HALF = 4;

    /**
     * Treat {@code mat} as a graph adjacency matrix and returns an iterator which iterates the neighbours of {@code vertex}. 
     * When the {@code mat} is not symmetrical ({@code flag} is {@link #UNSYMMETRIC})that the corresponding graph is directed, the graph will be complete as undirected and the consequenting
     * iterator will iterates the neibours of the undirected neighbours. 
     * @param mat
     * @param flag 
     * @param vertex 
     * @return 
     */
    public static TIntIterator neiboursIterator(Matrix mat, int flag, int vertex) {
        switch (flag) {
            case SYMMETRIC:
                return neiboursIterator4Symmetric(mat, vertex);
            case UNSYMMETRIC:
//            case SYMMETRIC_BUT_ONLY_UP_HALF:
//           case SYMMETRIC_BUT_ONLY_DOWN_HALF:
                return neiboursIterator4Unsymmetric(mat, vertex);
            default:
                throw new UnsupportedOperationException("The flag value should be the enum like final static int given by this class, such as SYMMETRIC, UNSYMMETRIC ...");
        }
    }

    private static TIntIterator neiboursIterator4Symmetric(final FlexCompRowMatrix mat, final int vertex) {
        return new TIntIterator() {
            int[] indes = mat.getRow(vertex).getIndex();
            double[] datas = mat.getRow(vertex).getData();
            int curId = -1;

            {
                nextCurId();
            }

            private void nextCurId() {
                curId++;
                while (curId < indes.length) {
                    if (indes[curId] != vertex && datas[curId] != 0) {
                        break;
                    }
                    curId++;
                }
            }

            @Override
            public int next() {
                int result = indes[curId];
                nextCurId();
                return result;
            }

            @Override
            public boolean hasNext() {
                if (curId < indes.length) {
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
    }

    private static TIntIterator neiboursIterator4Symmetric(final Matrix mat, final int vertex) {
        if (mat.getClass() == FlexCompRowMatrix.class) {
            return neiboursIterator4Symmetric((FlexCompRowMatrix) mat, vertex);
        }
        return new TIntIterator() {
            int curV = -1;

            {
                nextV();
            }

            @Override
            public int next() {
                int result = curV;
                nextV();
                return result;
            }

            private void nextV() {
                curV++;
                while (curV < mat.numColumns()) {
                    if (curV != vertex && mat.get(vertex, curV) != 0) {
                        break;
                    }
                    curV++;
                }
            }

            @Override
            public boolean hasNext() {
                if (curV < mat.numColumns()) {
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
    }

    private static TIntIterator neiboursIterator4Unsymmetric(final Matrix mat, final int vertex) {
        if (mat.getClass() == FlexCompRowMatrix.class) {
            return neiboursIterator4Unsymmetric((FlexCompRowMatrix) mat, vertex);
        }

        return new TIntIterator() {
            int i = -1, j = -1;

            {
                nextI();
                nextJ();
            }

            private void nextI() {
                i++;
                while (i < mat.numRows()) {

                    if (i != vertex && mat.get(i, vertex) != 0) {
                        break;
                    }
                    i++;
                }
            }

            private void nextJ() {
                j++;
                while (j < mat.numRows()) {

                    if (j != vertex && mat.get(vertex, j) != 0) {
                        break;
                    }
                    j++;
                }
            }

            @Override
            public int next() {
                if (i < j) {
                    int result = i;
                    nextI();
                    return result;
                } else if (j < i) {
                    int result = j;
                    nextJ();
                    return result;
                } else {
                    int result = i;
                    nextI();
                    nextJ();
                    return result;
                }
            }

            @Override
            public boolean hasNext() {
                if (i >= mat.numRows() && j >= mat.numColumns()) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
    }

    private static TIntIterator neiboursIterator4Unsymmetric(final FlexCompRowMatrix mat, final int vertex) {
        return new TIntIterator() {
            int row = -1, colId = -1;
            int[] indes;
            double[] datas;

            {
                SparseVector rowVector = mat.getRow(vertex);
                indes = rowVector.getIndex();
                datas = rowVector.getData();
                nextRow();
                nextColId();
            }

            private void nextRow() {
                row++;
                while (row < mat.numRows()) {

                    if (row != vertex && mat.get(row, vertex) != 0) {
                        break;
                    }
                    row++;
                }
            }

            private void nextColId() {
                colId++;
                while (colId < indes.length) {

                    if (indes[colId] != vertex && datas[colId] != 0) {
                        break;
                    }
                    colId++;
                }
            }

            @Override
            public int next() {

                int col = indes[colId];
                if (row < col) {
                    int result = row;
                    nextRow();
                    return result;
                } else if (col < row) {
                    int result = col;
                    nextColId();
                    return result;
                } else {
                    int result = row;
                    nextRow();
                    nextColId();
                    return result;
                }
            }

            @Override
            public boolean hasNext() {
                if (row < mat.numRows() || colId < indes.length) {
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
    }

    /**
     * Determines {@vertex}'s eccentricity vertes and the distance betwean eccentricity vertes and the origin one.
     * @param matrix
     * @param flag
     * @param vertex
     * @param eccentricityOut output
     * @return all the eccentricity vertes
     */
    public static TIntArrayList getEccentricityVertes(Matrix matrix, int flag, int vertex, int[] eccentricityOut) {
        return getEccentricityVertes(matrix, flag, vertex, eccentricityOut, null);
    }

     /**
     * Determines {@vertex}'s eccentricity vertes and the distance betwean eccentricity vertes and the origin one.
     * @param matrix
     * @param flag
     * @param vertex
     * @param eccentricityOut output
     * @param caches for acceleration, can be null. If not null it should contain at list two {@link TIntArrayList}
     * @return all the eccentricity vertes
     */
    public static TIntArrayList getEccentricityVertes(Matrix matrix, int flag, int vertex, int[] eccentricityOut, TIntArrayList[] caches) {
        int vertesNum = matrix.numRows();
        boolean[] visited = new boolean[vertesNum];
        visited[vertex] = true;
        TIntArrayList q1, q2;
        if (null != caches && caches.length >= 2 && caches[0] != null && caches[1] != null) {
            q1 = caches[0];
            q2 = caches[1];
            q1.clear();
            q2.clear();
        } else {
            q1 = new TIntArrayList(vertesNum);
            q2 = new TIntArrayList(vertesNum);
        }
        q1.add(vertex);
        int ecc = 0;
        do {

            for (int i = 0; i < q1.size(); i++) {
                int v = q1.getQuick(i);
                TIntIterator nbIter = neiboursIterator(matrix, flag, v);
                while (nbIter.hasNext()) {
                    int nb = nbIter.next();
                    if (visited[nb] || nb == 0) {
                        continue;
                    }
                    visited[nb] = true;
                    q2.add(nb);
                }
            }
            if (q2.isEmpty()) {
                if (null != eccentricityOut) {
                    eccentricityOut[0] = ecc;
                }
                return q1;
            } else {
                TIntArrayList t = q1;
                q1 = q2;
                q2 = t;
                q2.clear();
                ecc++;
            }
        } while (true);
    }

    public static int getDegree(Matrix mat, int flag, int vertex) {
        TIntIterator iter = neiboursIterator(mat, flag, vertex);
        int deg = 0;
        while (iter.hasNext()) {
            deg++;
            iter.next();
        }
        return deg;
    }

    /**
     * Determinates the pseudo peripheral vertex. 
     * @param mat
     * @param flag
     * @param startV
     * @return 
     */
    public static int pseudoPeripheralVertex(Matrix mat, int flag, int startV) {
        TIntArrayList[] caches = new TIntArrayList[]{new TIntArrayList(mat.numRows()), new TIntArrayList(mat.numRows())};
        int[] ecc = new int[]{0};

        TIntArrayList eccVs = getEccentricityVertes(mat, flag, startV, ecc, caches);

        int formEcc = ecc[0];

        do {
            int smallDeg = mat.numRows() + 1;
            int smallV = -1;
            for (int i = 0; i < eccVs.size(); i++) {
                int v = eccVs.getQuick(i);
                int deg = getDegree(mat, flag, v);
                if (deg < smallDeg) {
                    smallV = v;
                    smallDeg = deg;
                }
            }

            eccVs = getEccentricityVertes(mat, flag, smallV, ecc, caches);

            if (formEcc >= ecc[0]) {
                return smallV;
            }

            formEcc = ecc[0];

        } while (true);
    }

    /**
     * Breath first searching the matrix. Returns the searching path start from {@code startV}. Remind that a matrix presented graph may contains two sub graph which are seperated from each other. 
     * @param mat
     * @param flag
     * @param startV 
     * @param result for acceleration, can be null.
     * @param visited for acceleration, can be null. If not null, should be as long as {@code mat.numRows()}
     * @param numVisited Should not be null! The visited vertex start from {@startV} is added to numVisited[0].
     * @return The searching path start from {@code startV}. 
     */
    static TIntArrayList bfsMatrix(Matrix mat, int flag, int startV, TIntArrayList result, boolean[] visited, int[] numVisited) {
        if (null == result) {
            result = new TIntArrayList(mat.numRows());
        }
        LinkedList<Integer> q = new LinkedList<>();
        if (null == visited) {
            visited = new boolean[mat.numRows()];
        }
        q.add(startV);
        visited[startV] = true;
        result.add(startV);

        int numVisitedI = 1;

        while (!q.isEmpty()) {
            int v = q.pollFirst();
            TIntIterator iter = neiboursIterator(mat, flag, v);
            while (iter.hasNext()) {
                int nb = iter.next();
                if (visited[nb]) {
                    continue;
                } else {
                    visited[nb] = true;
                    numVisitedI++;
                    q.addLast(nb);
                    result.add(nb);
                }
            }
        }

        numVisited[0] += numVisitedI;
        return result;
    }

    public static int[] getBandWidth(Matrix mat, int flag) {
        int leftWidth = 0, rightWidth = 0;
        for (int i = 0; i < mat.numRows(); i++) {
            TIntIterator iter = neiboursIterator(mat, flag, i);
            int maxLeft = 0, maxRight = 0;
            while (iter.hasNext()) {
                int v = iter.next();
                if (v < i) {
                    int left = i - v;
                    if (maxLeft < left) {
                        maxLeft = left;
                    }
                } else {
                    int right = v - i;
                    if (maxRight < right) {
                        maxRight = right;
                    }
                }
            }
            if (leftWidth < maxLeft) {
                leftWidth = maxLeft;
            }
            if (rightWidth < maxRight) {
                rightWidth = maxRight;
            }
        }
        return new int[]{leftWidth, rightWidth};
    }

    public static int[] getBandWidthByRcmResults(Matrix mat, int[][] rcmRes) {
        return getBandWidthByPerm(mat, rcmRes[0]);
    }

    static int[] getBandWidthByPerm(Matrix mat, int[] perm) {
        int[][] widths = new int[mat.numRows()][2];
        for (MatrixEntry me : mat) {
            if (me.get() == 0) {
                continue;
            }
            int row = perm[me.row()];
            int col = perm[me.column()];
            if (col > row) {
                int rightW = col - row;
                if (widths[row][1] < rightW) {
                    widths[row][1] = rightW;
                }
            } else {
                int leftW = row - col;
                if (widths[row][0] < leftW) {
                    widths[row][0] = leftW;
                }
            }
        }
        int[] results = new int[2];
        for (int k = 0; k < results.length; k++) {
            int maxW = 0;
            for (int i = 0; i < widths.length; i++) {
                if (maxW < widths[i][k]) {
                    maxW = widths[i][k];
                }
            }
            results[k] = maxW;
        }
        return results;
    }

    public static BandMatrix getBandedMatrixByRcmResult(Matrix mat, int[][] rcmRes) {
        int[] bandWidths = getBandWidthByRcmResults(mat, rcmRes);
        BandMatrix result = new BandMatrix(mat.numRows(), bandWidths[0], bandWidths[1]);
        int[] perm = rcmRes[0];
        permMatrix(mat, result, perm);
        return result;
    }
    
    public static DenseVector recoveryBandedResultByRcmResult(DenseVector bandedResult,int[][]rcmRes){
        DenseVector result=new DenseVector(bandedResult.size());
        permVector(bandedResult,result,rcmRes[1]);
        return result;
    }

    public static void permMatrix(Matrix from, Matrix to, int[] perm) {
        for (MatrixEntry me : from) {
            to.set(perm[me.row()], perm[me.column()], me.get());
        }
    }
    
    public static void permVector(Vector from,Vector to, int[] perm){
        for(VectorEntry ve:from){
            to.set(perm[ve.index()],ve.get());
        }
    }

    /**
     * Get the permutation array for banding and recovering by RCM alogorithm
     * @param mat
     * @param flag
     * @return { perm for banding, perm for recovering}
     */
    public static int[][] rcm(Matrix mat, int flag) {
        boolean[] visited = new boolean[mat.numRows()];
        int[] numVisited = new int[1];
        int startV = 0;

        TIntArrayList result = new TIntArrayList(mat.numRows());
        while (true) {
            startV = pseudoPeripheralVertex(mat, flag, startV);
            bfsMatrix(mat, flag, startV, result, visited, numVisited);
            if (numVisited[0] < visited.length) {
                for (int i = 0; i < visited.length; i++) {
                    if (!visited[i]) {
                        startV = i;
                        break;
                    }
                }
            } else {
                break;
            }
        }
        result.reverse();
        int[] invPerm = result.toArray();
        int[] perm = inversePerm(invPerm);
        return new int[][]{perm, invPerm};
    }

    /**
     * Get the inverse permutation of {@code perm}
     * @param perm
     * @return 
     */
    public static int[] inversePerm(int[] perm) {
        int[] result = new int[perm.length];
        for (int i = 0; i < perm.length; i++) {
            result[perm[i]] = i;
        }
        return result;
    }
    
    /**
     * 
     * @param mat
     * @param flag
     * @param b
     * @return 
     */
    public static DenseVector bandSolve(Matrix mat,int flag,Vector b){
        int[][] rcmRes = rcm(mat, flag);
        BandMatrix bandMat = getBandedMatrixByRcmResult(mat, rcmRes);
        DenseVector bandRes = (DenseVector) bandMat.solve(b, new DenseVector(b.size()));
        return recoveryBandedResultByRcmResult(bandRes, rcmRes);
    }
}
