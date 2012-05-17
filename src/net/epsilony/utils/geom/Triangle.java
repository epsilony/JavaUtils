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

    public Coordinate c1, c2, c3;

    public Triangle(double x1, double y1, double x2, double y2, double x3, double y3) {

        c1 = new Coordinate();
        c2 = new Coordinate();
        c3 = new Coordinate();

        c1.x = x1;
        c2.x = x2;
        c3.x = x3;
        c1.y = y1;
        c2.y = y2;
        c3.y = y3;
    }

    public Triangle() {
        c1 = new Coordinate();
        c2 = new Coordinate();
        c3 = new Coordinate();
    }

    public Triangle(boolean init) {
        if (init) {
            c1 = new Coordinate();
            c2 = new Coordinate();
            c3 = new Coordinate();
        }
    }
}
