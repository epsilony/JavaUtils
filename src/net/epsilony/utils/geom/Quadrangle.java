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
    public double x1,y1,x2,y2,x3,y3,x4,y4;
    public Quadrangle(){
        
    }

    public Quadrangle(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.x3 = x3;
        this.y3 = y3;
        this.x4 = x4;
        this.y4 = y4;
    }
    
    public Quadrangle(double[] points){
        x1=points[0];
        y1=points[1];
        x2=points[2];
        y2=points[3];
        x3=points[4];
        y3=points[5];
        x4=points[6];
        y4=points[7];
    }
}
