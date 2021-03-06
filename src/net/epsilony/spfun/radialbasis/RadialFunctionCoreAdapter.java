/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.spfun.radialbasis;

import net.epsilony.spfun.radialbasis.RadialFunctionCore;

/**
 *
 * @author epsilon
 */
public abstract class RadialFunctionCoreAdapter implements RadialFunctionCore {
    protected int diffOrder;

    @Override
    public void setDiffOrder(int order) {
        this.diffOrder = order;
    }

    @Override
    public int getDiffOrder() {
        return diffOrder;
    }

    protected double[] initResultOutput(double[] ori) {
        if (null == ori) {
            return new double[diffOrder + 1];
        } else {
            return ori;
        }
    }
    
}
