/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.epsilony.utils.math.polynomial;

import net.epsilony.math.polynomial.BivariateBinomials;
import java.util.Arrays;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author epsilonyuan@gmail.com
 */
public class BivariateBinomialsTest {

    public BivariateBinomialsTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * Test of getBinomials method, of class BivariateBinomials.
     */
    @Test
    public void testGetBinomials_4args() {
        System.out.println("getBinomials static");
        int power = 3;
        double x = 2;
        double y = 1;
        double[] results = new double[10];
        double[] expResult1 = {1,2,1,4,2,1,8,4,2,1};
        double[] expResult2 = {1,1,2,1,2,4,1,2,4,8};
        BivariateBinomials.getBinomials(power, x, y, results);
        System.out.println("results1="+Arrays.toString(results));
        Boolean b=Arrays.equals(results, expResult1);
        BivariateBinomials.getBinomials(3, 1, 2, results);
        System.out.println("results1="+Arrays.toString(results));
        b=b&&Arrays.equals(results, expResult2);
        assertTrue(b);
    }

    /**
     * Test of getBinomials method, of class BivariateBinomials.
     */
    @Test
    public void testGetBinomials_3args() {
        System.out.println("getBinomials");
        double x = 2;
        double y = 1;
        double[] results = new double[10];
        BivariateBinomials instance = new BivariateBinomials(3);
        double[] expResult1 = {1,2,1,4,2,1,8,4,2,1};
        double[] expResult2 = {1,1,2,1,2,4,1,2,4,8};
        instance.getBinomials(x, y, results);
        System.out.println("results1="+Arrays.toString(results));
        Boolean b=Arrays.equals(results, expResult1);
        instance.getBinomials(1, 2, results);
        System.out.println("results1="+Arrays.toString(results));
        BivariateBinomials.getBinomials(3, 1, 2, results);
        b=b&&Arrays.equals(results, expResult2);
        assertTrue(b);
    }

}