/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.Model.Geometry2D;

/**
 *
 * @author epsilon
 */
public interface Transformer2D <T>{
    void mirror(Vector2D point);
    void mirror(Axis2D axis);
}
