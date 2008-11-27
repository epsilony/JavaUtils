/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.epsilony.math.temptest;
import static java.lang.Math.*;
import java.math.BigDecimal;

/**
 * 测试java double 所能达到的精度。
 * @author epsilon
 */
public class DoublePrecisionTest {
    /**
     * 从这个数值实验可以看出
     * 根据IEEE754规范的双精度整型
     * 其计算的相对精度应为2^-53*2,即2^-52约2.220446049e-16
     * @param args
     */
    public static void main(String args[]){
        BigDecimal b=new BigDecimal(2.0);
        BigDecimal c=new BigDecimal(1.0);
        double t=1;
        for (int i=0;i<55;i++)
        {
            c=c.divide(b);
            t/=2.0;
            System.out.println(i+"a:"+1.0+" c:"+c+" a-c "+(new BigDecimal(1.0)).subtract(c));
            System.out.println(1.0-t);
            System.out.println(i+"a:"+2.0+" c:"+c+" a-c "+(new BigDecimal(2.0)).subtract(c));
            System.out.println(2.0-t);
            System.out.println(i+"a:"+3.0+" c:"+c+" a-c "+(new BigDecimal(3.0)).subtract(c));
            System.out.println(16-t);
            System.out.println("________");
        }
    }

}
