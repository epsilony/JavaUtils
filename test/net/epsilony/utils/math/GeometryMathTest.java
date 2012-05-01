/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.utils.math;

import java.awt.geom.Point2D;
import java.util.LinkedList;
import net.epsilony.utils.java2d.Java2DGeometryMath;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author epsilonyuan@gmail.com
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
        boolean result = Java2DGeometryMath.isCounterClockwise(points);
        assertEquals(expResult, result);
        expResult=false;
        points.clear();
        points.add(new Point2D.Double(0, 0));
        points.add(new Point2D.Double(0,1));
        points.add(new Point2D.Double(1,0));
        result = Java2DGeometryMath.isCounterClockwise(points);
        assertEquals(expResult, result);
    }
}
