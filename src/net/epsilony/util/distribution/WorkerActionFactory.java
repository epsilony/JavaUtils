/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.epsilony.util.distribution;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author epsilon
 */
public interface WorkerActionFactory {
    public WorkerAction newAction(AtomicInteger processedUnit);
}
