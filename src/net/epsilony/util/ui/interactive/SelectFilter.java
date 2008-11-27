/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.epsilony.util.ui.interactive;

import net.epsilony.util.ui.Model2D.Element2D;

/**
 *
 * @author epsilon
 */
public interface SelectFilter {
    public boolean isSelectable(Element2D elem);
}
