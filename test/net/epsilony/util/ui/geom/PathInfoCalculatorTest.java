/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.epsilony.util.ui.geom;

import java.awt.geom.Ellipse2D;
import java.awt.geom.PathIterator;
import java.util.LinkedList;
import net.epsilony.math.analysis.UnivariateVectorFunction;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.integration.RombergIntegrator;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author epsilon
 */
public class PathInfoCalculatorTest {

    public PathInfoCalculatorTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

   
   

   
  


    /**
     * Test of getLength method, of class PathInfoCalculator.
     */
    @Test
    public void testGetLength() throws Exception {
        System.out.println("getLength");
        Ellipse2D el=new Ellipse2D.Double(-20, -10, 40, 20);
        LinkedList<PathInfoNode> infos=new LinkedList<PathInfoNode>();
        LinkedList<int[]> closedIndes=new LinkedList<int[]>();
        ShapeUtils.getPathInfo(el, infos, closedIndes);
        PathInfoCalculator instance = new PathInfoCalculator(infos.getFirst());
        final UnivariateVectorFunction deriFun=instance.vectorDerivative();
        final double [] tds=new double[2];
        UnivariateRealFunction lengthFun=new UnivariateRealFunction() {

            @Override
            public double value(double x) throws FunctionEvaluationException {
                deriFun.values(x, tds);
                return Math.sqrt(tds[0]*tds[0]+tds[1]*tds[1]);
                       
            }
        };
        RombergIntegrator rI=new RombergIntegrator(lengthFun);
        double expResult = rI.integrate(0, 1);
        double result = instance.getLength();
        double err=Math.abs((expResult-result)/result);
        System.out.println("Result:"+result);
        System.out.println("expResult"+expResult);
        System.out.println("err"+err);
        System.out.println("SEG_CLOSE" + PathIterator.SEG_CLOSE);
        System.out.println("SEG_CUBICTO" + PathIterator.SEG_CUBICTO);
        System.out.println("SEG_LINETO" + PathIterator.SEG_LINETO);
        System.out.println("SEG_MOVETO" + PathIterator.SEG_MOVETO);
        System.out.println("SEG_QUADTO" + PathIterator.SEG_QUADTO);
        System.out.println("WIND_EVEN_ODD" + PathIterator.WIND_EVEN_ODD);
        System.out.println("WIND_NON_ZERO" + PathIterator.WIND_NON_ZERO);
  
        assertTrue(err<0.01);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    
}