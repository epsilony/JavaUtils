/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.math.util;

import java.awt.geom.Ellipse2D;
import java.util.LinkedList;
import net.epsilony.math.analysis.multigaussquadrature.BivariateGaussLegendreQuadratures;
import net.epsilony.math.polynomial.BivariateRealFunction;
import net.epsilony.util.ui.geom.PathInfoNode;
import net.epsilony.util.ui.geom.ShapeUtils;
import org.apache.commons.math.FunctionEvaluationException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static java.lang.Math.*;

/**
 *
 * @author epsilon
 */
public class CoonsCoordinateChange2DTest {

    public CoonsCoordinateChange2DTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * Test of getResults method, of class CoonsBivariateMapper.
     */
    @Test
    public void testGetResults() throws Exception {
        System.out.println("getResults");
        double u = 0.0;
        double v = 0.0;
        Ellipse2D el = new Ellipse2D.Double(-20, -10, 40, 20);
//        Rectangle2D el=new Rectangle2D.Double(0, 0, 30, 20);
        LinkedList<PathInfoNode> infos = new LinkedList<PathInfoNode>();
        LinkedList<int[]> closeds = new LinkedList<int[]>();
        ShapeUtils.getPathInfo(el, infos, closeds);
        double[] results = new double[3];
        CoonsBivariateMapper instance = new CoonsBivariateMapper();
        instance.setInfos(infos);
        BivariateRealFunction fun = new BivariateRealFunction() {

            @Override
            public double value(double x, double y)  {
                return 1;
            }
        };

        double expResult = 6.283185307179587e+02;
        double result;
        double err = 0;
        for (int i = 1; i < 5; i++) {
            result = BivariateGaussLegendreQuadratures.CoonsQuadrangleQuadrature.coonsQuadrate(fun, infos, i, i);
            err = abs((result - expResult) / expResult);
            System.out.println("i=" + i);
            System.out.println("Result: " + result);
            System.out.println("expResult: " + expResult);
            System.out.println("error: " + err);
            System.out.println("");
        }
        assertTrue(err < 1e-3);

    // TODO review the generated test code and remove the default call to fail.
    //fail("The test case is a prototype.");
    }
}