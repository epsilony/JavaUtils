/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.util;

import java.awt.geom.PathIterator;
import java.nio.DoubleBuffer;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.nio.Buffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author epsilon
 */
public class TriangleJnaTest {

    public TriangleJnaTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testSomeMethod() {
        TriangleJna.LibTriangleJna libTriangleJna = TriangleJna.LibTriangleJna.INSTANCE;

        double[] pointlist = new double[]{80, 0, 100, 50, 0, 100, -100, 50, -80, 0, -100, -50, 0, -100, 100, -50, 0, -90, 80, -50, 0, -10, -80, -50, -70, 50, -60, 30, -10, 55, -40, 55, 70, 50, 60, 30, 10, 55, 40, 55, -10, 25, -20, -10, 10, 25, 20, -10, -50, 0, 50, 0};
        int numberofpoints = 26;
        int[] pointmarkerlist = new int[]{2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4, 5, 5, 5, 5, 6, 6, 7, 7, 0, 0};

        int[] segmentlist = new int[]{1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8, 1, 9, 10, 10, 11, 11, 12, 12, 9, 13, 14, 14, 15, 15, 16, 16, 13, 17, 18, 18, 19, 19, 20, 20, 17, 21, 22, 23, 24};

        int numberofsegments = 22;
        int[] segmentmarkerlist = new int[]{2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4, 5, 5, 5, 5, 6, 6};

        int numberofholes = 3;
        double[] holelist = new double[]{0, -50, -50, 50, 50, 50};

        String s = "pq0nV";
        TriangleJna.triangulateio in = TriangleJna.triangulateio.instanceWithoutAttributes(numberofpoints, pointlist, pointmarkerlist, numberofsegments, segmentlist, segmentmarkerlist,numberofholes, holelist);
        TriangleJna.triangulateio out = new TriangleJna.triangulateio();

        libTriangleJna.triangulate(s, in, out, new TriangleJna.triangulateio());
        System.out.println("pointlist:");
        System.out.println(Arrays.toString(out.getArrayField("pointlist", true)));
//        System.out.println("pointmarkerlist");
//        System.out.println(Arrays.toString(out.pointmarkerlist.getIntArray(0, out.numberofpoints)));
//        System.out.println("triangles");
//        System.out.println(Arrays.toString(out.trianglelist.getIntArray(0, out.numberoftriangles*3)));

    }
    
    @Test
    public void testCallTrianglute(){
        TriangleJna jna=new TriangleJna();
        jna.setNoNeighborOutput(true);
        jna.setNoElementOutput(true);
        jna.setNoBoundaryMarkersOutput(true);
        jna.setQualityAngle();
        Area area=new Area(new Rectangle2D.Double(0, 0, 20, 20));
        PathIterator pathIterator = area.getPathIterator(new AffineTransform());
        while(pathIterator.isDone()==false){
           
            double[] ts=new double[6];
            int flag = pathIterator.currentSegment(ts);
            pathIterator.next();
            String str = Arrays.toString(ts);
            System.out.println(str);
        }
    }
}
