/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.epsilony.util.ui.Model2D;

import java.awt.Shape;

/**
 * 一个Element2D的接口
 * Element2D包含了几何信息、名称、点选时所算的区域以及子的Element
 * @author epsilon
 */
public interface Element2D {
    public Shape getShape();
    public Shape getSelectableArea();
    public String getType();
    public Element2D getParent();
    public Element2D[] getChilds();
    public boolean isSelectable(double x, double y);
    public void setSelectableSize(double size);
    public boolean isChanged();
    public void setChanged();
}
