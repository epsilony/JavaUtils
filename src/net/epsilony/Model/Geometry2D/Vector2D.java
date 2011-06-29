/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.Model.Geometry2D;

import java.awt.geom.Point2D;

/**
 *
 * @author epsilon
 */
public class Vector2D extends Point2D.Double{

    public Vector2D() {
    }

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }


    @Override
    public String toString() {
        return "Vector2D[" + x + ", " + y + "]";
    }

    public boolean equals(Vector2D vec, double linearTol) {
        double val;
        val = x - vec.x;
        if (val < 0) {
            val = -val;
        }
        if (val > linearTol) {
            return false;
        }
        val = y - vec.y;
        if (val < 0) {
            val = -val;
        }
        if (val > linearTol) {
            return false;
        }
        return true;
    }
}
