/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.math.analysis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.epsilony.math.polynomial.PolynomialUtils;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import net.epsilony.math.Interval;
import net.epsilony.utils.OrderedLinkedList;
import org.apache.commons.math.analysis.polynomials.PolynomialFunction;

/**
 *
 * @deprecated  没有通过测试
 * @author epsilonyuan@gmail.com
 */
@Deprecated
public class RootIntervals extends ArrayList<Interval> {

    UnivariateRealFunction uniFun;
    int nRoot;
    Interval bigIntv;
    List<Double> rootList = new LinkedList<Double>();
    Comparator<Interval> comparator = new RootIntervalComparator();

    class RootIntervalComparator implements Comparator<Interval> {

        @Override
        public int compare(Interval arg0, Interval arg1) {
            try {
                double size0 = arg0.getSize();
                double size1 = arg1.getSize();
                if (isOddInterval(arg0) == IntervalRoot.ODD) {
                    size0 *= 1.5;
                }
                if (isOddInterval(arg1) == IntervalRoot.ODD) {
                    size1 *= 1.5;
                }
                if (size0 < size1) {
                    return -1;
                } else {
                    if (size0 > size1) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            } catch (FunctionEvaluationException ex) {

                System.out.println("error!");
                Logger.getLogger(RootIntervals.class.getName()).log(Level.SEVERE, null, ex);
                return 0;
            }
        }
    }
    List<Interval> intvList = null;

    /**
     * 构造一个链表，这个链表的每一个元素是一个区间，这样的区间中有一个单根
     * @param bigIntv
     * @param fun
     */
    public RootIntervals(int nRoot, Interval bigIntv, UnivariateRealFunction fun) throws FunctionEvaluationException {
        uniFun = fun;
        this.nRoot = nRoot;
        this.bigIntv = bigIntv;
        intvList = new OrderedLinkedList<Interval>(comparator);
        findIntervals();
    }

    private void findIntervals() throws FunctionEvaluationException {
        splitInterval(nRoot, bigIntv);
        //end of 将bigIntv区间平均划分成nRoot等分，存到tIntvs中

        Interval tIntv;

        int m = 1;
        int rd;
        Random rand = new Random(47);
        while (sumOddIntervals() + rootList.size() < nRoot) {
            if(m%100==0){
                System.out.println("100");
            }
            if (m % 4 != 0) {
                tIntv = intvList.get(0);
                intvList.remove(0);
            } else {
                rd = intvList.size() - 1 - rand.nextInt(intvList.size() / 3);
                tIntv = intvList.get(rd);
                intvList.remove(rd);
            }
            m++;

            switch (isOddInterval(tIntv)) {
                case ODD:
                    splitInterval(3, tIntv);
                    break;
                case EVEN:
                    splitInterval(2, tIntv);
                    break;
                case BOTHROOT:
                    if (!rootList.contains(new Double(tIntv.getLeft()))) {
                        rootList.add(new Double(tIntv.getLeft()));
                    }
                    if (!rootList.contains(new Double(tIntv.getRight()))) {
                        rootList.add(new Double(tIntv.getRight()));
                    }
                    splitInterval(2, tIntv);
                    break;
                case LEFTROOT:
                    if (!rootList.contains(new Double(tIntv.getLeft()))) {
                        rootList.add(new Double(tIntv.getLeft()));
                    }
                    splitInterval(2, tIntv);
                    break;
                case RIGHTROOT:
                    if (!rootList.contains(new Double(tIntv.getRight()))) {
                        rootList.add(new Double(tIntv.getRight()));
                    }
                    splitInterval(2, tIntv);
                    break;
            }
        }


        for (Interval intv : intvList) {
            if (isOddInterval(intv) == IntervalRoot.ODD) {
                add(intv);
            }
        }
    }

    enum IntervalRoot {

        EVEN, ODD, LEFTROOT, RIGHTROOT, BOTHROOT;
    }

    /**
     * 确定一个区间中的实根数是否是奇数个
     * @param intv
     * @return 
     *          EVEN:  区间中有偶根
     *          ODD： 区间中有奇根
     *          LEFTROOT： 区间左边是根
     *          RIGHTROOT： 区间右边是根
     *          BOTHROOT： 区间左边都是根（可以买彩票去了）
     * @throws org.apache.commons.math.FunctionEvaluationException
     */
    IntervalRoot isOddInterval(Interval intv)
            throws FunctionEvaluationException {
        double lVal = value(intv.getLeft());
        double rVal = value(intv.getRight());

        if (lVal < 0 && rVal < 0 || lVal > 0 && rVal > 0) {
            return IntervalRoot.EVEN;
        } else {
            if (lVal != 0 && rVal != 0) {
                return IntervalRoot.ODD;
            } else {
                if (lVal == rVal) {
                    return IntervalRoot.BOTHROOT;
                } else {
                    if (lVal == 0) {
                        return IntervalRoot.LEFTROOT;
                    } else {
                        return IntervalRoot.RIGHTROOT;
                    }
                }
            }
        }
    }

    private void splitInterval(int n, Interval intv) {
        int i;
        double t = intv.getSize() / n;
        double left = intv.getLeft();
        double right = left + t;

//将bigIntv区间平均划分成n等分，存到intvList中
        for (i = 1; i < n; i++) {
            intvList.add(new Interval(left, right));
            left = right;
            right += t;
        }
        intvList.add(new Interval(left, intv.getRight()));
    //end of 将bigIntv区间平均划分成nRoot等分，存到tIntvs中
    }

    int sumOddIntervals() throws FunctionEvaluationException {
        int s = 0;
        for (Interval i : intvList) {
            if (isOddInterval(i) == IntervalRoot.ODD) {
                s++;
            }
        }
        return s;
    }

    public List<Double> getRootList() {
        return rootList;
    }

    double value(double x) throws FunctionEvaluationException {
        return uniFun.value(x);
    }

    public static void main(String args[]) throws FunctionEvaluationException {
        double[] roots = {-1, -3, -5, -7, -11, -13, -15};
        double[] efos = {0, 1};
        PolynomialFunction p = new PolynomialFunction(efos);
        for (int i = 0; i < roots.length; i++) {
            efos[0] = roots[i];
            p = PolynomialUtils.multiply(p, new PolynomialFunction(efos));
        }

        RootIntervals rootIntvs = new RootIntervals(roots.length + 1, new Interval(0, 20), p);
        rootIntvs.findIntervals();
        System.out.println(Arrays.toString(rootIntvs.toArray()));
        System.out.println(Arrays.toString(rootIntvs.getRootList().toArray()));
    }
}
