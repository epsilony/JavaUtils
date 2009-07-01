/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.epsilony.util;

import java.io.Serializable;
import no.uib.cipr.matrix.sparse.FlexCompRowMatrix;
import no.uib.cipr.matrix.sparse.SparseVector;

/**
 *
 * @author epsilon
 */
public class FlexCompRowMatrixSerializable implements Serializable{
    public int row;
    public int col;
    public int[][] indes;
    public double[][] datas;
    public FlexCompRowMatrix get(){
        FlexCompRowMatrix result=new FlexCompRowMatrix(row, col);

        for(int i=0;i<row;i++){
            for(int j=0;j<indes[i].length;j++){
                result.set(i,indes[i][j],datas[i][j]);
            }
        }
        return result;
    }

    public void addTo(FlexCompRowMatrix matrix){
        for(int i=0;i<row;i++){
            for(int j=0;j<indes[i].length;j++){
                matrix.add(i,indes[i][j],datas[i][j]);
            }
        }
    }
    public FlexCompRowMatrixSerializable(FlexCompRowMatrix matrix){
        row=matrix.numRows();
        col=matrix.numColumns();
        indes=new int[row][];
        datas=new double[row][];
        for(int i=0;i<row;i++){
            SparseVector sv=matrix.getRow(i);
            indes[i]=sv.getIndex();
            datas[i]=sv.getData();
        }
    }
}
