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
public class Vector2D extends Point2D.Double implements Transformable2D<Vector2D> {

    public Vector2D() {
    }

    public Vector2D(double x, double y) {
        super(x, y);
    }

    @Override
    public String toString() {
        return "Vector2D[" + x + ", " + y + "]";
    }

    public void reverse() {
        x = -x;
        y = -y;
    }

    public void normalize() {
        double mod = getModulus();
        x/=mod;
        y/=mod;
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

    @Override
    public Vector2D mirror(Vector2D point) {
        reverse();
        multiply(2);
        x+=point.x;
        y+=point.y;
        return this;
    }

    @Override
    public Vector2D mirror(Axis2D axis) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Vector2D mirror(Vector2D t, Vector2D point) {
        t.x=x;
        t.y=y;
        t.reverse();
        t.multiply(2);
        t.x+=point.x;
        t.y+=point.y;
        return t;
    }

    @Override
    public Vector2D mirror(Vector2D t, Axis2D axis) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Vector2D Rotate(Vector2D point, double angular) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Vector2D Rotate(Vector2D t, Vector2D point, double angular) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Vector2D scale(Vector2D point, double scale) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Vector2D scale(Vector2D t, Vector2D point, double scale) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Vector2D Translate(Vector2D vec) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Vector2D Translate(Vector2D from, Vector2D to) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Vector2D Translate(Vector2D t, Vector2D from, Vector2D to) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Vector2D Transform(Transform2D trans) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Vector2D Transform(Vector2D t, Transform2D trans) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public double getModulus() {
        return Math.sqrt(x * x + y * y);
    }

    public double getSquareModulus() {
        return x * x + y * y;
    }

    public void Add(Vector2D vec) {
        x += vec.x;
        y += vec.y;
    }

    /**
     * return the magnitude of this x vec
     * @param vec
     * @return always &gt=0;
     */
    public double getCrossMagnitude(Vector2D vec) {
        double result = x * vec.y - y * vec.x;
        if (result < 0) {
            return -result;
        }
        return result;
    }

    public double getCross(Vector2D vec) {
        return x * vec.y - y * vec.x;
    }
    
    public void multiply(double scale){
        x*=scale;
        y*=scale;
    }
    
    
}
