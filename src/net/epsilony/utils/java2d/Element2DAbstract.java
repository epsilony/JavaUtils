/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.utils.java2d;

import java.awt.BasicStroke;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.util.List;

/**
 *
 * @author epsilonyuan@gmail.com
 */
public abstract class Element2DAbstract implements Element2D {
    static double size = 7;
    public static Stroke stroke = new BasicStroke((float) size, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    String type;
    Shape shape;
    Element2D parent;
    List<Element2D> childList;
    
    boolean isChanged = false;
    private Shape selShape = null;
    private Rectangle2D selRect = null;

    synchronized public void setChanged() {
        isChanged = true;
    }

    synchronized public boolean isChanged() {
        return isChanged;
    }
    
    abstract public Shape getShape();

    public String getType() {
//        return type;
        return this.getClass().getSimpleName();
    }

    public Element2D getParent() {
        return parent;
    }

    public Element2D[] getChilds() {
        return (Element2D[]) childList.toArray();
    }

    public boolean isSelectable(double x, double y) {
        if (null == selRect||isChanged()) {
            selRect = getShape().getBounds2D();
        }
        if (selRect.contains(x, y) == false) {
            return false;
        }
        if (null == selShape||isChanged) {
            selShape = stroke.createStrokedShape(getShape());
        }
        if (selShape.contains(x, y)) {
            return true;
        } else {
            return false;
        }
    }

    public Element2DAbstract(String type) {
        this.type = type;
    }

    public void setSelectableSize(double size) {
        if (this.size == size && isChanged() == false) {
            return;
        }
        this.size = size;
        selRect = null;
        selShape = null;
    }

    public Shape getSelectableArea() {
        if (null == selShape||isChanged) {
            
            selShape = stroke.createStrokedShape(getShape());
        }
        return selShape;
    }
    
    
}
