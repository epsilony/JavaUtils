/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.epsilony.math.analysis.multigaussquadrature;

import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.util.List;
import net.epsilony.util.ui.geom.PathInfoNode;
import static java.lang.Math.*;
/**
 *
 * @author epsilon
 */
public class BivariateAreaQuadrateGrid {
    Area area;
    double cellSizeX;
    double cellSizeY;
    int nCellX;
    int nCellY;
    double gridX0,gridY0;
    double gridW,gridH;
    Area cellRectArea=new Area(new Rectangle2D.Double(0,0,cellSizeX,cellSizeY));
    AffineTransform trans=new AffineTransform();
     LinkedList<int[]> intersectsCellOris=new LinkedList<int[]>();
    
    public void setSize(double size){
        Rectangle2D rect=area.getBounds2D();
        gridX0=rect.getMinX();
        gridY0=rect.getMinY();
        gridW=rect.getWidth();
        gridH=rect.getHeight();
        nCellX=(int) ceil(gridW/size);
        nCellY=(int) ceil(gridH/size);
        cellSizeX=gridW/nCellX;
        cellSizeY=gridH/nCellY;
    }
    
    public double getCellOriX(int col){
        return gridX0+cellSizeX*col;
    }
    public double getCellOriY(int row){
        return gridY0+cellSizeY*row;
    }
    
    public boolean cellInsides(int row,int col){
        return area.contains(gridX0+cellSizeX*col, gridY0+cellSizeY*row, gridW, gridH);
    }
    
    public boolean cellIntersects(int row,int col){
        return area.intersects(gridX0+cellSizeX*col, gridY0+cellSizeY*row, gridW, gridH);
    }
    
    private  void quadrateInsidesAndGetItersectsCells(){
        intersectsCellOris.clear();
        for(int i=0;i<nCellY;i++){
            for(int j=0;j<nCellX;j++){
                if(cellInsides(i,j)){
                    quadrateRectCell(i,j);                    
                }else if(cellIntersects(i,j)){
                    intersectsCellOris.add(new int[]{i,j});
                }
            }
        }
    }

    private void quadrateRectCell(int i, int j) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    
    private Area getCellArea(int i,int j){
        trans.setToTranslation(gridX0+j*cellSizeX, gridY0+i*cellSizeY);
        Area a=cellRectArea.createTransformedArea(trans);
        a.intersect(area);
        return a;
    }
    
    private void classifyCellAreas(List<Area> areas){
//        for(Area a:areas){
//            if(a.is)
//        }
    }
    
    
   
}
