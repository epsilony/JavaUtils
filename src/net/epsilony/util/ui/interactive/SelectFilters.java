/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.epsilony.util.ui.interactive;

import java.awt.geom.Rectangle2D;
import net.epsilony.util.ui.Model2D.Element2D;

/**
 *
 * @author epsilon
 */
public class SelectFilters {
    public static SelectFilter getTypeFilter(final String type){
        if (type==null){
            throw new NullPointerException();
        }
        return new SelectFilter(){
            public boolean isSelectable(Element2D elem) {
                if (elem.getType().equalsIgnoreCase(type)){
                    return true;
                }else
                {
                    return false;
                }
            }         
        };
    }
    
    public static SelectFilter getRectFilter(final Rectangle2D rect){
        if (rect==null){
            throw new NullPointerException();
        }
        return new SelectFilter(){
            public boolean isSelectable(Element2D elem){
                return rect.contains(elem.getShape().getBounds2D());
            }
        };
    }

}
