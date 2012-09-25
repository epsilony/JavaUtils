/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.utils.geom;

import java.util.Random;
import org.apache.commons.math.geometry.Rotation;
import org.apache.commons.math.geometry.Vector3D;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * All the methods in {@link GeometryMath} has been directly or in directly
 * tested.
 *
 * @version 20120529-1
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
     * Test of triangleArea2D method, of class GeometryMath.
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
        double result = GeometryMath.triangleArea2D(x1, y1, x2, y2, x3, y3);
        assertEquals(expResult, result, 1e-6);

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
        double expResult = 4 / 6.0;
        double result = GeometryMath.tetrahedronVolume(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4);
        assertEquals(expResult, result, 1e-6);
        result = GeometryMath.tetrahedronVolume(x1 + 0.1, y1 + 0.1, z1 + 0.1, x2 + 0.1, y2 + 0.1, z2 + 0.1, x3 + 0.1, y3 + 0.1, z3 + 0.1, x4 + 0.1, y4 + 0.1, z4 + 0.1);
        expResult = 4 / 6.0;
        assertEquals(expResult, result, 1e-6);
    }

    /**
     * Test of tripleProduct method, of class GeometryMath.
     */
    @Test
    public void testTripleProduct() {
        double[][] samples = new double[][]{
            {0.32674181, 0.67703555, 0.76472565},
            {0.37781503, 0.3471433, 0.20577671},
            {0.638418, 0.15578956, 0.71358848},
            {0.65665528, 0.27126622, 0.83225679}};
        double exp = -0.015779219940682574;
        Coordinate[] cs = new Coordinate[samples.length];
        for (int i = 0; i < samples.length; i++) {
            cs[i] = new Coordinate(samples[i]);
        }
        double act = GeometryMath.tripleProduct(cs[0], cs[1], cs[2], cs[3]);
        assertEquals(exp, act, 1e-6);


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

    }

    /**
     * Test of cross method, of class GeometryMath.
     */
    @Test
    public void testCross_Coordinate_Coordinate() {
        double[] v1 = new double[]{0.90534023, 0.81030933, 0.91987766};
        double[] v2 = new double[]{0.51075733, 0.70686597, 0.72496539};
        double[] expV = new double[]{-0.06278399, -0.18650608, 0.22608277};
        Coordinate cv1 = new Coordinate(v1);
        Coordinate cv2 = new Coordinate(v2);
        Coordinate resC = GeometryMath.cross(cv1, cv2);
        assertArrayEquals(expV, resC.toArray(null), 1e-6);
        resC = new Coordinate();
        Coordinate resC2 = GeometryMath.cross(cv1, cv2, resC);
        assertArrayEquals(expV, resC.toArray(null), 1e-6);
        assertArrayEquals(expV, resC2.toArray(null), 1e-6);


    }

    /**
     * Test of normalize method, of class GeometryMath.
     */
    @Test
    public void testNormalize() {
        Coordinate v1 = new Coordinate(0.89300395, 0.66563516, 0.08706149);
        Coordinate v2 = new Coordinate(0.12030804, 0.25720585, 0.29711651);
        double[] expRes1 = new double[]{0.79933284, 0.59581376, 0.07792923};
        double[] expRes2 = new double[]{0.29273233, 0.62583074, 0.72294098};
        Coordinate act1 = GeometryMath.normalize(v1);
        Coordinate act2 = GeometryMath.normalize(v2);
        assertArrayEquals(expRes1, act1.toArray(null), 1e-6);
        assertArrayEquals(expRes2, act2.toArray(null), 1e-6);
    }

    /**
     * Test of distanceSquare method, of class GeometryMath.
     */
    @Test
    public void testDistance() {
        Coordinate v1 = new Coordinate(0.46153366, 0.87332827, 0.08244482);
        Coordinate v2 = new Coordinate(0.44941345, 0.1303087, 0.60728621);
        double exp = 0.9097711106612556;
        double act = GeometryMath.distance(v1, v2);
        assertEquals(exp, act, 1e-6);
        v1 = new Coordinate(0.05986069, 0.02553298, 0.24859737);
        v2 = new Coordinate(0.19139411, 0.41113791, 0.97587176);
        exp = 0.83361876067943541;
        act = GeometryMath.distance(v1, v2);
        assertEquals(exp, act, 1e-6);
    }

    /**
     * Test of cross2D method, of class GeometryMath.
     */
    @Test
    public void testCross2D_Coordinate_Coordinate() {
        Coordinate v1 = new Coordinate(0.11621082, 0.76426441);
        Coordinate v2 = new Coordinate(0.60430609, 0.05415815);
        double exp = -0.45555587;
        double act = GeometryMath.cross2D(v1, v2);
        assertEquals(exp, act, 1e-6);
    }

    /**
     * Test of lineSegmentTriangleIntersection method, of class GeometryMath.
     */
    @Test
    public void testLineSegmentTriangleIntersection() {
        Coordinate start = new Coordinate(0, 0, 0);
        Coordinate end = new Coordinate(0, 0, 1);
        double[][] triangleVs = new double[][]{
            {0, 0, 1.5, 0, 1, 2.5, 0, -1, 2.5},
            {0, 0, 0.5, 0, 1, 1.5, 0, -1, 1.5},
            {0, 0, 0.5, 0, 1, 0.75, 0, -1, 0.75},
            {-1, 0, 1.5, 0, -1, 2.5, 1.1, 1, 2.5},
            {-1, 0, 0.5, 1, -0.1, 0.5, 1, -2, 0.75},
            {-1, 0, 0.5, 1, 1, 0.5, 1, -2, 0.75}
        };
        int[] exps = new int[]{GeometryMath.DISDROINT, GeometryMath.INTERSECT, GeometryMath.INTERSECT, GeometryMath.DISDROINT, GeometryMath.DISDROINT, GeometryMath.INTERSECT};
        for (int i = 0; i < exps.length; i++) {
            Triangle tri = new Triangle();
            tri.c1 = new Coordinate(triangleVs[i][0], triangleVs[i][1], triangleVs[i][2]);
            tri.c2 = new Coordinate(triangleVs[i][3], triangleVs[i][4], triangleVs[i][5]);
            tri.c3 = new Coordinate(triangleVs[i][6], triangleVs[i][7], triangleVs[i][8]);
            int act = GeometryMath.lineSegmentTriangleIntersection(start, end, tri, null);
            try {
                assertEquals(exps[i], act);
            } catch (AssertionError e) {
                GeometryMath.lineSegmentTriangleIntersection(start, end, tri, null);
                throw e;
            }
        }

        triangleVs = new double[][]{
            {1, 1, 0.5, 1, -1, 0.5, -1, 0, 0.5},
            {1, 1, 0.5, 1, -1, 0.5, 0.1, 0, 0.5}
        };
        exps = new int[]{GeometryMath.INTERSECT, GeometryMath.DISDROINT};
        Coordinate transform = new Coordinate(new Random().nextDouble(), new Random().nextDouble(), new Random().nextDouble());
        Coordinate rotAxis = new Coordinate(new Random().nextDouble(), new Random().nextDouble(), new Random().nextDouble());
        double rotAngle = new Random().nextDouble() * 2 * Math.PI;
        Rotation rot = new Rotation(new Vector3D(rotAxis.x, rotAxis.y, rotAxis.z), rotAngle);
        Vector3D startVec = rot.applyTo(new Vector3D(start.x, start.y, start.z));
        Vector3D endVec = rot.applyTo(new Vector3D(end.x, end.y, end.z));
        Vector3D transVec = new Vector3D(transform.x, transform.y, transform.z);
        startVec = startVec.add(transVec);
        endVec = endVec.add(transVec);
        start = new Coordinate(startVec.getX(), startVec.getY(), startVec.getZ());
        end = new Coordinate(endVec.getX(), endVec.getY(), endVec.getZ());
        for (int i = 0; i < exps.length; i++) {

            Vector3D v1 = new Vector3D(triangleVs[i][0], triangleVs[i][1], triangleVs[i][2]);
            Vector3D v2 = new Vector3D(triangleVs[i][3], triangleVs[i][4], triangleVs[i][5]);
            Vector3D v3 = new Vector3D(triangleVs[i][6], triangleVs[i][7], triangleVs[i][8]);
            v1 = rot.applyTo(v1).add(transVec);
            v2 = rot.applyTo(v2).add(transVec);
            v3 = rot.applyTo(v3).add(transVec);
            Triangle tri = new Triangle();
            tri.c1 = new Coordinate(v1.getX(), v1.getY(), v1.getZ());
            tri.c2 = new Coordinate(v2.getX(), v2.getY(), v2.getZ());
            tri.c3 = new Coordinate(v3.getX(), v3.getY(), v3.getZ());
            int act = GeometryMath.lineSegmentTriangleIntersection(start, end, tri, null);
            try {
                assertEquals(exps[i], act);
            } catch (AssertionError e) {
                GeometryMath.lineSegmentTriangleIntersection(start, end, tri, null);
                throw e;
            }
        }
    }

    @Test
    public void testIsLineSegment2DIntersect() {
        double[][] xys = new double[][]{
            {0, 0, 1, 0, 2, 0, 3, 0},//d
            {0, 0, 1, 0, 0.5, 0, 3, 0},//i
            {0, 0, 1, 0, 0.5, 0, 0.75, 0},//i
            {0, 0, 1, 0, -0.5, 0, 0.5, 0},//i
            {0, 0, 1, 0, -1, 0, -0.5, 0},//d
            {0, 0, 1, 0, 0, 1, 1, 1},//d
            {0, 0, 1, 0, 2, -1, 2, 1},//d
            {0, 0, 1, 0, 0.5, -1, 0.5, 2},//i
            {0, 0, 1, 0, -1, 1, -1, -1},//d
            {0, 0, 0, 1, 0, 2, 0, 3},//d
            {0, 0, 0, 1, 0, 0.5, 0, 3},//i
            {0, 0, 0, 1, 0, 0.5, 0, 0.75},//i
            {0, 0, 0, 1, 0, -1, 0, 3},//i
            {0, 0, 0, 1, 0, -1, 0, -2},//d
        };
        boolean[] exps = new boolean[]{false, true, true, true, false, false, false, true, false, false, true, true, true, false};
        for (int i = 0; i < exps.length; i++) {
            boolean exp = exps[i];
            double[] xy = xys[i];
            boolean act = GeometryMath.isLineSegment2DIntersect(new Coordinate(xy[0], xy[1]), new Coordinate(xy[2], xy[3]), new Coordinate(xy[4], xy[5]), new Coordinate(xy[6], xy[7]));
            try {
                assertEquals(exp, act);
            } catch (AssertionError e) {
                GeometryMath.isLineSegment2DIntersect(new Coordinate(xy[0], xy[1]), new Coordinate(xy[2], xy[3]), new Coordinate(xy[4], xy[5]), new Coordinate(xy[6], xy[7]));
                throw e;
            }
        }
    }

    /**
     * Test of crossDot method, of class GeometryMath.
     */
    @Test
    public void testCrossDot() {
        double[][] samples = new double[][]{
            {0.638072220325, 0.112534092808, 0.270665132693},
            {0.955586445258, 0.19457147353, 0.710817912233},
            {0.264031621259, 0.848582393298, 0.107967005882},
            {0.165189812079, 0.915228173196, 0.131160707137},
            {0.0992347836469, 0.392229726119, 0.114450742302},
            {0.252464530002, 0.118088640255, 0.176826654841}};
        double[] exps = new double[]{-0.15638738027325175, 0.0081588151877721032};
        Coordinate[] cs = new Coordinate[samples.length];
        for (int i = 0; i < samples.length; i++) {
            cs[i] = new Coordinate(samples[i]);
        }
        for (int i = 0; i < exps.length; i++) {
            double exp = exps[i];
            double act = GeometryMath.crossDot(cs[3 * i], cs[3 * i + 1], cs[3 * i + 2]);
            assertEquals(exp, act, 1e-6);
        }
    }

    /**
     * Test of lineSegmentPlaneIntersection method, of class GeometryMath.
     */
    @Test
    public void testLineSegmentPlaneIntersection() {
        int[] exps = new int[]{GeometryMath.DISDROINT, GeometryMath.INTERSECT};

        double[][] startEnds = new double[][]{
            {0.09923478364688143, 0.39222972611931395, 0.11445074230169294},
            {0.25246453000189528, 0.11808864025467514, 0.17682665484088445}};
        Coordinate start = new Coordinate(startEnds[0]);
        Coordinate end = new Coordinate(startEnds[1]);
        Coordinate pt = new Coordinate(0.63807222032486932, 0.11253409280822768, 0.27066513269261017);
        //Coordinate norm=new Coordinate(-0.07033143, -0.97027065, 0.23157798);
        Coordinate disjointNorm = new Coordinate(-0.0664753509212, -0.970326193135, 0.232516364192);
        //Coordinate coincidentNorm=new Coordinate( 0.0253785122484, -0.00967378540646, -0.104859789047);
        Coordinate intersecNorm = new Coordinate(-0.0741875, -0.9702151, 0.23063959);
        Coordinate[] pts = new Coordinate[]{pt, pt};
        Coordinate[] norms = new Coordinate[]{disjointNorm, intersecNorm};
        for (int i = 0; i < exps.length; i++) {
            Coordinate out = new Coordinate();
            int act = GeometryMath.lineSegmentPlaneIntersection(start, end, norms[i], pts[i], out);
            assertEquals(exps[i], act);
            if (act == GeometryMath.INTERSECT) {
                double act2 = GeometryMath.dot(GeometryMath.minus(out, pt), norms[i]);
                assertEquals(0, act2, 1e-6);
            }
        }
    }

    @Test
    public void testCrossPointOfLineSegments() {
        Random rand = new Random();
        Coordinate[] sample = new Coordinate[4];
        double[] sampleXYs = new double[]{-1.0, -2.0, 3.0, 6.0, 1.0, -1.0, -0.25, 4.0};
        double expX = 0.5, expY = 1;
        for (int i = 0; i < sample.length; i++) {
            sample[i] = new Coordinate(sampleXYs[i * 2], sampleXYs[i * 2 + 1]);
        }
        Coordinate act = GeometryMath.crossPointOfLineSegments(sample[0], sample[1], sample[2], sample[3], null);
        assertEquals(expX, act.x, 1e-10);
        assertEquals(expY, act.y, 1e-10);
    }

    @Test
    public void testIsInsideQuadrangle() {
        double[] vertesXys = new double[]{1, -1, 3, 1, 2, 3, 0, 2};
        Coordinate[] vertes = new Coordinate[4];
        for (int i = 0; i < 4; i++) {
            vertes[i] = new Coordinate(vertesXys[i * 2], vertesXys[i * 2 + 1]);
        }

        double[] pts = new double[]{1, 0,
            2, -0.00001,
            2, 0.001,
            2.1, 2.9,
            2.1, 2.5,
            -0.1, 2,
            1, 2.50001,
            1, 2.498};
        boolean[] exps = new boolean[]{true, false, true, false,true, false, false, true};
        for(int i=0;i<exps.length;i++){
            boolean act=GeometryMath.isInsideQuadrangle(pts[i*2], pts[i*2+1], vertes);
            assertEquals(exps[i], act);
        }
    }
}
