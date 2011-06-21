/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.math.util;

import java.awt.geom.Point2D;
import java.util.LinkedList;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author epsilon
 */
public class GeometryMathTest {
    
    public GeometryMathTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * Test of isCounterClockwise method, of class Geometry.
     */
    @Test
    public void testIsCounterClockwise() {
        System.out.println("isCounterClockwise");
        LinkedList<Point2D.Double> points=new LinkedList<Point2D.Double>();
        points.add(new Point2D.Double(0, 0));
        points.add(new Point2D.Double(1,0));
        points.add(new Point2D.Double(0,1));
        boolean expResult = true;
        boolean result = GeometryMath.isCounterClockwise(points);
        assertEquals(expResult, result);
        expResult=false;
        points.clear();
        points.add(new Point2D.Double(0, 0));
        points.add(new Point2D.Double(0,1));
        points.add(new Point2D.Double(1,0));
        result = GeometryMath.isCounterClockwise(points);
        assertEquals(expResult, result);
    }
}
