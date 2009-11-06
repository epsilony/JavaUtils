package net.epsilony.math.polynomial;

//import commons-math-1.2.jar from apache
import org.apache.commons.math.analysis.polynomials.PolynomialFunction;
import static java.lang.Math.*;
import static net.epsilony.math.util.EYMath.*;
import static org.apache.commons.math.util.MathUtils.*;
import java.util.Arrays;
/**
 * 
 * @author epsilon
 */
public class LegendrePolynomial extends PolynomialFunction{
    
    public LegendrePolynomial(int order)
    {
        super(getCoefficients(order));
    }
    
    public int getOrder(){
        return degree()+1;
    }    
    
    /** 
     * 4 debug:这个系数数组的长度是order＋1,其中非零的元素个数为ceil (order+1)/2
     * P_0 返回｛1｝;
     * P_1     {0，1};
     * P_2     {-0.5,0,1.5};
     * P_3      {0,-1.5,0,2.5};
     * @param order Legendre 多项目的阶数，定义域为非负整数。
     * @return 任意order阶Legendre多项式P_n的系数数组
     */
    public static double [] getCoefficients(int order){
        double [] ds=new double[order+1];
        int k= (int)(ceil (order/2.0));
        double t=-1;
        if((order-k)%2==0){
            t=1;
        }
        t*=pow(2,order*-1);
        for(int i=k;i<=order;i++){
            ds[2*i-order]=t*permutation(2*i, order)/factorial(i)/factorial(order-i);
            t*=-1;
        }
        return ds;
    } 
    
    public static void main(String args[]){
        for(int i=0;i<=10;i++){           
            double []t= getCoefficients(i);
            System.out.println(Arrays.toString(t));
        }
    }
}
