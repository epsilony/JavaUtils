/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.epsilony.util.ui.geom.demo;

import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import net.epsilony.util.ui.geom.ShapeUtils;

/**
 *
 * @author epsilon
 */
public class ConicDemo {

    public static void main(String[] args){
        Shape[] shapes={new Ellipse2D.Double(0, 0, 50, 50)};
        for(Shape s:shapes){
            System.out.println(s);
            System.out.println(ShapeUtils.toString(s));
            System.out.println("");
            System.out.println(ShapeUtils.toString(new Area(s)));
        }
    }
}
