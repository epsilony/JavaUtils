/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.utils.math.polynomial;

import static java.lang.Math.ceil;
import static java.lang.Math.floor;

/**
 * <br>生成二维的完备单项式列</br>
 * <br>0阶的单项式列：1</br>
 * <br>1阶的单项式列：1  x  y</br>
 * <br>2阶的单项式列：1  x<sup>2</sup>  xy  y<sup>2</sup></br>
 * <br>3阶的单项式列：1  x<sup>3</sup>  x<sup>2</sup>y  xy<sup>2</sup>  y<sup>3</sup></br>
 * <br>.....</br>
 * <p> <br><bold> Changelist<bold</br>
 * <br> 0.11 新建一阶，二阶偏微分支持 </br>
 * <br> 0.101 修改生成算法，小加速一下</br>
 * <br> 0.10 建立 </br>
 * </p>
 * @author M.Yuan
 * @version 0.101 
 */
public class BivariateBinomials {

    int power;

    public BivariateBinomials(int power) {
        this.power = power;
    }

    /**
     * <br>返回一个power阶的完备单项式列组成的数组</br>
     * <br> v0.101 测试通过 </br>
     * @param power 单项的最高阶数 如阶数小于0则不做任何运算直接返回results
     * @param x 
     * @param y
     * @param results 存放结果的数组，要求其长度不小于(power+1)*power/2,即如power=3刚result.length==10
     * @return 其元素的值为{1, x<sup>3</sup>, x<sup>2</sup>y, xy<sup>2</sup>, y<sup>3</sup>...}
     */
    public static double[] getBinomials(int power, double x, double y, double[] results) {
        if (power < 0) {
            return results;
        }
        results[0] = 1;
        int i, j, s1, s2;
        s1 = 0;
        for (i = 1; i <= power; i++) {
            s2 = (i * i + i) / 2;
            for (j = 0; j < ceil((i + 1) / 2.0); j++) {
                results[s2 + j] = results[s1 + j] * x;
            }
            for (j = 0; j < floor((i + 1) / 2.0); j++) {
                results[s2 + i - j] = results[s1 + i - 1 - j] * y;
            }
            s1 = s2;
        }
        return results;
    }

    /**
     * <br>返回一个power阶的完备单项式列组成的数组</br>
     * <br> v0.101 测试通过 </br>
     * @param power 单项的最高阶数 如阶数小于0则不做任何运算直接返回results
     * @param x 
     * @param y
     * @param results 存放结果的数组，要求其长度不小于(power+1)*power/2,即如power=3刚result.length==10
     * @return 其元素的值为{1, x<sup>3</sup>, x<sup>2</sup>y, xy<sup>2</sup>, y<sup>3</sup>...}
     */
    public double[] getBinomials(double x, double y, double[] results) {
        return getBinomials(power, x, y, results);
    }

    public static double[] getPxBinomials(int power, double x, double y, double[] results) {
        if (power < 0) {
            return results;
        }
        results[0] = 0;
        if (power == 0) {
            return results;
        }
        results[1] = 1;
        results[2] = 0;
        if (power == 1) {
            return results;
        }
        results[3] = 2 * x;
        results[4] = y;
        results[5] = 0;
        if (power == 2) {
            return results;
        }
        results[6] = 3 * x * x;
        results[7] = 2 * x * y;
        results[8] = y * y;
        results[9] = 0;

        if (power == 3) {
            return results;
        }
        if (power > 3) {
            throw new UnsupportedOperationException("大于3阶的还不支持");
        }
        return results;
    }

    public static double[] getPyBinomials(int power, double x, double y, double[] results) {
        if (power < 0) {
            return results;
        }
        results[0] = 0;
        if (power == 0) {
            return results;
        }
        results[1] = 0;
        results[2] = 1;
        if (power == 1) {
            return results;
        }
        results[3] = 0;
        results[4] = x;
        results[5] = 2 * y;
        if (power == 2) {
            return results;
        }
        results[6] = 0;
        results[7] = x * x;
        results[8] = 2 * y * x;
        results[9] = 3 * y * y;
        if (power == 3) {
            return results;
        }
        if (power > 3) {
            throw new UnsupportedOperationException("大于3阶的还不支持");
        }
        return results;
    }

    public static double[] getPxxBinomials(int power, double x, double y, double[] results) {
        if (power < 0) {
            return results;
        }
        results[0] = 0;
        if (power == 0) {
            return results;
        }
        results[1] = 0;
        results[2] = 0;
        if (power == 1) {
            return results;
        }
        results[3] = 2;
        results[4] = 0;
        results[5] = 0;
        if (power == 2) {
            return results;
        }
        results[6] = 6 * x;
        results[7] = 2 * y;
        results[8] = 0;
        results[9] = 0;
        if (power == 3) {
            return results;
        }
        if (power > 3) {
            throw new UnsupportedOperationException("大于3阶的还不支持");
        }
        return results;
    }

    public static double[] getPxyBinomials(int power, double x, double y, double[] results) {
        if (power < 0) {
            return results;
        }
        results[0] = 0;
        if (power == 0) {
            return results;
        }
        results[1] = 0;
        results[2] = 0;
        if (power == 1) {
            return results;
        }
        results[3] = 0;
        results[4] = 1;
        results[5] = 0;
        if (power == 2) {
            return results;
        }
        results[6] = 0;
        results[7] = 2 * x;
        results[8] = 2 * y;
        results[9] = 0;
        if (power == 3) {
            return results;
        }
        if (power > 3) {
            throw new UnsupportedOperationException("大于3阶的还不支持");
        }
        return results;
    }

    public static double[] getPyyBinomials(int power, double x, double y, double[] results) {
        if (power < 0) {
            return results;
        }
        results[0] = 0;
        if (power == 0) {
            return results;
        }
        results[1] = 0;
        results[2] = 0;
        if (power == 1) {
            return results;
        }
        results[3] = 0;
        results[4] = 0;
        results[5] = 2;
        if (power == 2) {
            return results;
        }
        results[6] = 0;
        results[7] = 0;
        results[8] = 2 * x;
        results[9] = 6 * y;
        if (power == 3) {
            return results;
        }
        if (power > 3) {
            throw new UnsupportedOperationException("大于3阶的还不支持");
        }
        return results;
    }
}
