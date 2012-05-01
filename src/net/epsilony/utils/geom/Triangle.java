/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.utils.geom;

/**
 *
 * @author epsilonyuan@gmail.com
 */
public class Triangle {
    public double x1, y1, x2, y2, x3, y3;

    public Triangle(double x1, double y1, double x2, double y2, double x3, double y3) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.x3 = x3;
        this.y3 = y3;
    }
    
    public Triangle(){
        
    }

    @Override
    public String toString() {
        return String.format("(%f,%f;%f,%f;%f,%f)", x1,y1,x2,y2,x3,y3);
    }
    
    
}
