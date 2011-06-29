/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.Model.Geometry2D;

/**
 *
 * @author epsilon
 */
public interface Transformable2D <T>{
    T mirror(Vector2D point);
    T mirror(Axis2D axis);
    T mirror(T t,Vector2D point);
    T mirror(T t,Axis2D axis);
    
    T Rotate(Vector2D point,double angular);
    T Rotate(T t, Vector2D point,double angular);
    
    T scale(Vector2D point,double scale);
    T scale(T t ,Vector2D point,double scale);
    
    T Translate(Vector2D vec);
    T Translate(Vector2D from,Vector2D to);
    T Translate(T t,Vector2D vec);
    T Translate(T t,Vector2D from,Vector2D to);
    
    T Transform(Transform2D trans);
    T Transform(T t,Transform2D trans);
}
