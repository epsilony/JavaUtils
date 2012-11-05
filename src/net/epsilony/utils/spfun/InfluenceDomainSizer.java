/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.utils.spfun;

import net.epsilony.utils.geom.Node;

/**
 *
 * @author epsilon
 */
public interface InfluenceDomainSizer {
    double getSize(Node nd);
    
    double getMaxSize();
}
