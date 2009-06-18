/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.epsilony.util.ui;

import no.uib.cipr.matrix.Matrix;

/**
 *
 * @author epsilon
 */
public class MatrixUtils {
    public static String stringPlot(Matrix matrix){
        StringBuilder sb=new StringBuilder();
        String x="X";
        String iS="I";
        String zS="0";
        String ln=String.format("%n");
        for(int i=0;i<matrix.numRows();i++){
            for(int j=0;j<matrix.numColumns();j++){
                if(i==j){
                    sb.append(iS);
                    continue;
                }
                if(matrix.get(i, j)!=0){
                    sb.append(x);
                }else
                {
                    sb.append(zS);
                }
            }
            sb.append(ln);
        }
        return sb.toString();
    }
}
