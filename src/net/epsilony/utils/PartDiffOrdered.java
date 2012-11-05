/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.utils;


/**
 * @author epsilonyuan@gmail.com
 */
public interface PartDiffOrdered {

    /**
     * <p>设定此后相关输出的序列值为某函数的一系列偏微分</br>
     * </ul></p>
     * @param order 
     */
    void setDiffOrder(int order);
    
    int getDiffOrder();
}
