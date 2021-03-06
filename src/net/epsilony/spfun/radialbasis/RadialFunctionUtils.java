/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.spfun.radialbasis;

import gnu.trove.list.array.TDoubleArrayList;
import net.epsilony.utils.SomeFactory;
import net.epsilony.spfun.CommonUtils;

/**
 *
 * @author epsilonyuan@gmail.com
 */
public class RadialFunctionUtils {

    public static SomeFactory<RadialFunction> factory(final SomeFactory<RadialFunctionCore> coreFunFactory) {
        return factory(coreFunFactory, 2);

    }

    public static SomeFactory<RadialFunction> factory(final SomeFactory<RadialFunctionCore> coreFunFactory, final int dim) {
        return new SomeFactory<RadialFunction>() {
            @Override
            public RadialFunction produce() {
                WeightFunctionImp imp = new WeightFunctionImp(coreFunFactory.produce(), dim);
                return imp;
            }
        };
    }

    public static RadialFunction weightFunction(RadialFunctionCore coreFun, int dim) {
        return new WeightFunctionImp(coreFun, dim);
    }

    public static RadialFunction weightFunction(RadialFunctionCore coreFun) {
        return new WeightFunctionImp(coreFun);
    }

    static class WeightFunctionImp implements RadialFunction {

        private int diffOrder;
        RadialFunctionCore coreFun;
        private final int dim;
        private double[] coreVals;

        private WeightFunctionImp(RadialFunctionCore coreFun, int dim) {
            this.coreFun = coreFun;
            this.dim = dim;
        }

        private WeightFunctionImp(RadialFunctionCore coreFun) {
            this.coreFun = coreFun;
            this.dim = 2;
        }

        private TDoubleArrayList[] initResults(TDoubleArrayList[] ori) {

            int len = 0;
            switch (diffOrder) {
                case 0:
                    len = 1;
                    break;
                case 1:
                    switch (dim) {
                        case 1:
                            len = 2;
                            break;
                        case 2:
                            len = 3;
                            break;
                        case 3:
                            len = 4;
                            break;
                        default:
                            throw new UnsupportedOperationException();
                    }
                    break;
                default:
                    throw new UnsupportedOperationException();
            }

            if (ori == null) {
                ori = new TDoubleArrayList[len];
                for (int i = 0; i < ori.length; i++) {
                    ori[i] = new TDoubleArrayList();
                }
            } else {
                for (int i = 0; i < len; i++) {
                    ori[i].resetQuick();
                }
            }

            return ori;
        }

        @Override
        public TDoubleArrayList[] values(TDoubleArrayList[] distsSqs, TDoubleArrayList rads, TDoubleArrayList[] results) {
            int size = distsSqs[0].size();
            boolean uniqRads = false;
            double radSq = 0;
            if (rads.size() == 1) {
                uniqRads = true;
                radSq = rads.getQuick(0);
                radSq *= radSq;
            }
            results = initResults(results);
            for (int i = 0; i < size; i++) {
                if (!uniqRads) {
                    radSq = rads.getQuick(i);
                    radSq *= radSq;
                }
                double distSq = distsSqs[0].get(i);
                coreFun.valuesByNormalisedDistSq(distSq / radSq, coreVals);
                results[0].add(coreVals[0]);
                
                if (diffOrder < 1) {
                    continue;
                }

                double distSq_x = distsSqs[1].get(i);
                double distSq_y = distsSqs[2].get(i);
                results[1].add(coreVals[1] * distSq_x / radSq);
                results[2].add(coreVals[1] * distSq_y / radSq);
                if (dim == 3) {
                    double distSq_z = distsSqs[3].get(i);
                    results[3].add(coreVals[1] * distSq_z / radSq);
                }
                
            }
            return results;
        }

        @Override
        public void setDiffOrder(int order) {
            if (order < 0 || order >= 2) {
                throw new UnsupportedOperationException();
            }
            this.diffOrder = order;
            coreFun.setDiffOrder(order);
            coreVals = new double[diffOrder + 1];
        }

        @Override
        public int getDiffOrder() {
            return diffOrder;
        }
        
        private double[] initValueResult(double[] ori){
            if(null==ori){
                return new double[CommonUtils.lenBase(dim, diffOrder)];
            }else{
                return ori;
            }
        }
        

        @Override
        public double[] value(double[] distSq, double rad, double[] result) {
            result=initValueResult(result);
            double radSq = rad*rad;
            coreFun.valuesByNormalisedDistSq(distSq[0]/radSq, result);
            if(diffOrder<1){
                return result;
            }
            double dw_dwDistSq=result[1];
            result[1]=dw_dwDistSq*distSq[1]/radSq;
            result[2]=dw_dwDistSq*distSq[2]/radSq;
            return result;       
        }
    }
}
