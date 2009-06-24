/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.epsilony.util.distribution;

/**
 *
 * @author epsilon
 */
public interface Range {
    Range[] divide(int division);
    Range cut(int unit);
    int getUnit();
}
