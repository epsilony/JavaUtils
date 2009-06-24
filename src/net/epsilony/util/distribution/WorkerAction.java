/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.epsilony.util.distribution;

/**
 *
 * @author epsilon
 */
public interface WorkerAction {
    public void action();
    public void setRange(Range range);
}
