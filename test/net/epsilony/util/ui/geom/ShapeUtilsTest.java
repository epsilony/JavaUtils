/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.epsilony.util.ui.geom;

import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author epsilon
 */
public class ShapeUtilsTest {

    public ShapeUtilsTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

 
    /**
     * Test of getPathInfo method, of class ShapeUtils.
     */
    @Test
    public void testGetPathInfo_3args_2() {
        System.out.println("getPathInfo");
        List<PathInfoNode> infos = new LinkedList<PathInfoNode>();
        List<int[]> closedIndes = new LinkedList<int []>();
        Area area=new Area(new Rectangle2D.Double(0,0,400,400));
        area.subtract(new Area(new Rectangle2D.Double(10, 10, 20, 20)));
        area.subtract(new Area(new Ellipse2D.Double(50,10,100,100)));
        System.out.println("Area:");
        System.out.println(ShapeUtils.toString(area));
        System.out.println("");
        
        ShapeUtils.getPathInfo(area, infos, closedIndes);
        for(PathInfoNode pn:infos){
            System.out.println(pn);
        }
        for(int[] cls:closedIndes){
            System.out.println(Arrays.toString(cls));
        }
        assertTrue(true);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

}