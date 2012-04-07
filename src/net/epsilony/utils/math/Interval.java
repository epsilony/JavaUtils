/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.epsilony.math.util;

/**
 *
 * @author epsilonyuan@gmail.com
 */
public class Interval {
    double left;
    double right;
    public Interval(double input1,double input2){
        if (input1 > input2) {
            right = input1;
            left = input2;
        } else {
            left = input1;
            right = input2;
        }
    }

    public double getLeft() {
        return left;
    }

    public double getRight() {
        return right;
    }

    public void setLeft(double left) {
        this.left = left;
    }

    public void setRight(double right) {
        this.right = right;
    }
    
    public double getSize(){
        return right-left;
    }
    
    @Override
    public String toString(){
        return "["+left + ", " + right+"]";
    }
    

}
