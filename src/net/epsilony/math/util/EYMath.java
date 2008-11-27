/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.epsilony.math.util;
/**
 * 自建的工具类，用以补充java.math中缺的静态函数
 * @author epsilon
 */
public class EYMath {
    
    /**
     * 求排列
     * @param n
     * @param m
     * @return P^m_n = n!/(n-m)!
     */
    public static long permutation(int n,int m){
        long p=1;
        for (int i=n-m+1;i<=n;i++){
            p*=i;
        }
        return p;
    }
}
