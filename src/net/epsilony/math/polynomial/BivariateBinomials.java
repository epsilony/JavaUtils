/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.epsilony.math.polynomial;
import static java.util.Arrays.fill;

/**
 * 生成二维的完备单项式列<br>
 * 0阶的单项式列：1<br>
 * 1阶的单项式列：1  x  y<br>
 * 2阶的单项式列：1  x<sup>2</sup>  xy  y<sup>2</sup><br>
 * 3阶的单项式列：1  x<sup>3</sup>  x<sup>2</sup>y  xy<sup>2</sup>  y<sup>3</sup><br>
 * .....
 * 主要的函数为getBinomials
 * @author epsilon
 * @version 0.11 最新修改后通过测试
 */
public class BivariateBinomials {

    int power;
    public BivariateBinomials(int power) {
        this.power=power;
    }
    /**
     * 返回一个power阶的完备单项式列组成的数组<br>
     * @param power 单项的最高阶数 如阶数小于0则不做任何运算直接返回results
     * @param x 
     * @param y
     * @param results 存放结果的数组，要求其长度不小于(power+1)*power/2,即如power=3刚result.length==10
     * @return 其元素的值为{1, x<sup>3</sup>, x<sup>2</sup>y, xy<sup>2</sup>, y<sup>3</sup>...}
     */
    public static double[] getBinomials(int power,double x,double y,double [] results){
        if (power<0){
            return results;
        }
        fill(results,0,(power*power+3*power)/2+1,1);
        int i,j,k;
        for (i=1;i<=power;i++){
            for(j=i;j<=power;j++){
                for(k=0;k<=j-i;k++){
                    results[(j*j+j)/2+k]*=x;
                    results[(j*j+j)/2+j-k]*=y;
                }
            }
        }
        return results;
    }
    public double [] getBinomials(double x,double y,double[] results){
        return getBinomials(power, x, y, results);
    }
}
