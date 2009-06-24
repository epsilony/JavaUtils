/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.epsilony.util.distribution;

import java.util.ArrayList;

/**
 *
 * @author epsilon
 */
public interface WorkpieceFactory<PRODUCT> {
    PRODUCT newWorkpiece();
    PRODUCT assemblyWorkpieces(ArrayList<PRODUCT> workpieces);
}
