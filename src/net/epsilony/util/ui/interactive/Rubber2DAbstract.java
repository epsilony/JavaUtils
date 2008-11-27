/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.util.ui.interactive;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 一个Rubber2D的最基本实现,可实现一个单色的rubber效果。
 * @author epsilon
 */
abstract public class Rubber2DAbstract implements Rubber2D {

    double[] parms;
    protected Color color=Color.GRAY;
    protected MouseAdapter mouseAdapter = new MouseAdapter() {

        @Override
        public void mouseExited(MouseEvent arg0) {
            resetRubber(arg0.getComponent().getGraphics());
        }

        @Override
        public void mouseMoved(MouseEvent arg0) {
            showRubber(arg0);
        }
    };

    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * 提供一个公用的数据接口，可能会在实throw new UnsupportedOperationException("Not supported yet.");现{@link #getShape(double, double) }中得以应用
     * @param parms
     */
    public void setParms(double[] parms) {
        this.parms = parms;
    }
    
    /**
     * 提供一个公用的数据接口，可能会在实现{@link #getShape(double, double) }中得以应用
     */
    public double[] getParms(){
        return parms;
    }
    
    /**
     * 记录刚画的一个Shape
     */
    protected Shape former = null;

    /**
     * 用于重载实现
     * @param x 通过鼠标得到的值
     * @param y 通过鼠标得到的值
     * @return 即将要在{@link #showRubber(java.awt.event.MouseEvent) }中画出
     */
    public abstract Shape getShape(double x, double y);

    public MouseAdapter getRubberMouseAdapter() {
        return mouseAdapter;
    }

    public void resetRubber(Graphics graphics) {
        Graphics2D gr = (Graphics2D) graphics;
        gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_DEFAULT);
        gr.setXORMode(Color.white);
        gr.setColor(color);
        if (former != null) {
            gr.draw(former);
            former = null;
        }
    }

    public void showRubber(MouseEvent evt) {
        Graphics2D gr = (Graphics2D) evt.getComponent().getGraphics();
        resetRubber(gr);
        former = getShape(evt.getX(), evt.getY());
        gr.draw(former);
    }

    public void clearFormers() {
        former=null;
    }
    
    
}
