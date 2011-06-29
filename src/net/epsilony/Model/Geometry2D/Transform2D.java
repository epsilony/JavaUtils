/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.Model.Geometry2D;

import java.awt.geom.AffineTransform;

/**
 *
 * @author epsilon
 */
public class Transform2D extends AffineTransform{

    public Transform2D(double[] flatmatrix) {
        super(flatmatrix);
    }

    public Transform2D(double m00, double m10, double m01, double m11, double m02, double m12) {
        super(m00, m10, m01, m11, m02, m12);
    }

    public Transform2D(float[] flatmatrix) {
        super(flatmatrix);
    }

    public Transform2D(float m00, float m10, float m01, float m11, float m02, float m12) {
        super(m00, m10, m01, m11, m02, m12);
    }

    public Transform2D(AffineTransform Tx) {
        super(Tx);
    }

    public Transform2D() {
    }
    

    public Transform2D getMirrorInstance(Vector2D point){
        return new Transform2D(-1, 0, 0, -1, point.x*2, point.y*2);
    }
}
