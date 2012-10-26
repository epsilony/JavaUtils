/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.utils.geom;

/**
 *
 * @author epsilonyuan@gmail.com
 */
public class Quadrangle {

    public Coordinate[] vertes = new Coordinate[4];

    {
        for (int i = 0; i < vertes.length; i++) {
            vertes[i] = new Coordinate();
        }
    }

    public Quadrangle() {
    }

    public Quadrangle(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
        vertes[0].x = x1;
        vertes[0].y = y1;
        vertes[1].x = x2;
        vertes[1].y = y2;
        vertes[2].x = x3;
        vertes[2].y = y3;
        vertes[3].x = x4;
        vertes[3].y = y4;
    }

    public Quadrangle(double[] points) {
        for(int i=0;i<vertes.length;i++){
            vertes[i].x=points[i*2];
            vertes[i].y=points[i*2+1];
        }
    }
    
    public void setVertes(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4){
        vertes[0].x = x1;
        vertes[0].y = y1;
        vertes[1].x = x2;
        vertes[1].y = y2;
        vertes[2].x = x3;
        vertes[2].y = y3;
        vertes[3].x = x4;
        vertes[3].y = y4;
    }
    
    public void setVertes(Quadrangle quad){
        for(int i=0;i<vertes.length;i++){
            vertes[i].set(quad.vertes[i]);
        }
    }
    
    public double getXY(int index){
        Coordinate vertex=vertes[index/2];
        if(index % 2 ==0){
            return vertex.x;
        }else{
            return vertex.y;
        }
    }
}
