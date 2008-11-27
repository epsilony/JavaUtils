/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.math.analysis.multigaussquadrature;

import java.awt.geom.Ellipse2D;
import java.util.Arrays;
import java.util.LinkedList;
import net.epsilony.math.analysis.GaussLegendreQuadrature;
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
public class BivariateGaussLegendreQuadratureTest {

    public BivariateGaussLegendreQuadratureTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * Test of generalSectorQuadrate method, of class BivariateGaussLegendreQuadratures.
     */
    @Test
    public void testGeneralSectorQuadrate() throws Exception {
        System.out.println("generalSectorQuadrate");
        BivariateRealFunction fun = new BivariateRealFunction() {

            public double value(double x, double y) throws FunctionEvaluationException {
                return 1;
            }
        };
        double x0 = 0.0;
        double y0 = 0.0;
        boolean isGood=true;
        Ellipse2D el = new Ellipse2D.Double(-20, -10,40, 20);
        LinkedList<PathInfoNode> infos=new LinkedList();
        LinkedList<int[]> closeds=new LinkedList<int[]>();
        ShapeUtils.getPathInfo(el, infos, closeds);
        
        PathInfoNode arcInfo = infos.getFirst();
        int nRad =2;
        int nCir = 2;
        double expResult = 1.570796326794897e+02/2;
        double result = BivariateGaussLegendreQuadratures.GeneralSectorQuadrature.generalSectorQuadrate(fun, x0, y0, arcInfo, nRad, nCir);
        double err=abs((expResult-result)/expResult);
        System.out.println("Result:"+result);
        System.out.println("err:"+err);
        System.out.println("Coefs:"+Arrays.toString(GaussLegendreQuadrature.getGaussLegendreQuadratureCoefficients(2)));
        System.out.println("points"+Arrays.toString(GaussLegendreQuadrature.getGaussLegendreQuadraturePoints(2)));
        isGood=isGood&&err<2e-3;
        assertTrue(isGood);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
}