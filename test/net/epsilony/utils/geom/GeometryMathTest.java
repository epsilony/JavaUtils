/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.utils.geom;

import net.epsilony.utils.geom.GeometryMath;
import org.apache.commons.math.linear.OpenMapRealMatrix;
import org.apache.commons.math.linear.SparseRealMatrix;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

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
    
    @After
    public void tearDown() {
    }

    /**
     * Test of triangleArea method, of class GeometryMath.
     */
    @Test
    public void testTriangleArea() {
        System.out.println("triangleArea");
        double x1 = 3;
        double y1 = 3;
        double x2 = 5;
        double y2 = 5;
        double x3 = 4;
        double y3 = 6;
        double expResult = 2;
        double result = GeometryMath.triangleArea(x1, y1, x2, y2, x3, y3);
        assertEquals(expResult, result, 1e-6);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of tetrahedronVolume method, of class GeometryMath.
     */
    @Test
    public void testTetrahedronVolume() {
        System.out.println("tetrahedronVolume");
        double x1 = 0;
        double y1 = 1.0;
        double z1 = 0.0;
        double x2 = -Math.sqrt(2);
        double y2 = 0.0;
        double z2 = Math.sqrt(2);
        double x3 = Math.sqrt(2);
        double y3 = 0.0;
        double z3 = Math.sqrt(2);
        double x4 = 0.0;
        double y4 = 0.0;
        double z4 = 0.0;
        double expResult = 4/6.0;
        double result = GeometryMath.tetrahedronVolume(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4);
        assertEquals(expResult, result, 1e-6);
        result=GeometryMath.tetrahedronVolume(x1+0.1, y1+0.1, z1+0.1, x2+0.1, y2+0.1, z2+0.1, x3+0.1, y3+0.1, z3+0.1, x4+0.1, y4+0.1, z4+0.1);
        expResult=4/6.0; 
        assertEquals(expResult,result,1e-6);
    }

    /**
     * Test of tripleProduct method, of class GeometryMath.
     */
    @Test
    public void testTripleProduct() {
        System.out.println("tripleProduct");
        double x1 = 0.0;
        double y1 = 0.0;
        double z1 = 0.0;
        double x2 = 1.0;
        double y2 = 0.0;
        double z2 = 0.0;
        double x3 = 0.0;
        double y3 = 2.0;
        double z3 = 0.0;
        double x4 = 0.0;
        double y4 = 0.0;
        double z4 = 3.0;
        double expResult = 6;
        double result = GeometryMath.tripleProduct(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4);
        assertEquals(expResult, result, 1e-6);
        // TODO review the generated test code and remove the default call to fail.
       
    }

    /**
     * Test of pt3Cross2D method, of class GeometryMath.
     */
    @Test
    public void testCrossProduct() {
        System.out.println("crossProduct");
        double x1 = 0.0;
        double y1 = 0.0;
        double x2 = 1.0;
        double y2 = 0.0;
        double x3 = 0.0;
        double y3 = 1.0;
        double expResult = 1;
        double result = GeometryMath.pt3Cross2D(x1, y1, x2, y2, x3, y3);
        assertEquals(expResult, result, 0.0);
        expResult = -1;
        result = GeometryMath.pt3Cross2D(x1, y1, -x2, y2, x3, y3);
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        
    }
    
    @Test
    public void temp(){
        SparseRealMatrix sp=new OpenMapRealMatrix(10, 10);
    }
}
