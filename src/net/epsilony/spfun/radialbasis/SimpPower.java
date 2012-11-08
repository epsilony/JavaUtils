/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.spfun.radialbasis;

import java.util.Arrays;
import net.epsilony.utils.SomeFactory;
import net.epsilony.spfun.radialbasis.RadialFunctionCore;

/**
 *
 * @author epsilon
 */
public class SimpPower extends RadialFunctionCoreAdapter {
    int power;

    public SimpPower(int power) {
        this.power = power;
    }

    @Override
    public double[] valuesByNormalisedDistSq(double disSq, double[] results) {
        results = initResultOutput(results);
        if (disSq >= 1) {
            Arrays.fill(results, 0);
            return results;
        }
        double t = disSq - 1;
        results[0] = Math.pow(t, power);
        //(r*r-1)^p = (q-1)^p
        if (diffOrder >= 1) {
            results[1] = Math.pow(t, power - 1) * power;
        }
        return results;
    }


    public static SomeFactory<RadialFunctionCore> genFactory(final int power) {
        return new SomeFactory<RadialFunctionCore>() {
            @Override
            public RadialFunctionCore produce() {
                return new SimpPower(power);
            }
        };
    }
}
