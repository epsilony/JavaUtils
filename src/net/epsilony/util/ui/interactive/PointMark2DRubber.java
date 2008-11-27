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
import net.epsilony.util.ui.geom.PointMark2D;

/**
 *
 * @author epsilon
 */
public class PointMark2DRubber implements Rubber2D {

    Color color;
    double size;
    Shape former = null;
    String type;
    MouseAdapter mouseAdapter = new MouseAdapter() {

        @Override
        public void mouseExited(MouseEvent arg0) {
            resetRubber(arg0.getComponent().getGraphics());
        }

        @Override
        public void mouseMoved(MouseEvent arg0) {
            showRubber(arg0);
        }
    };

    public PointMark2DRubber(Color color, double size,String type) {
        this.color = color;
        this.size = size;
        this.type=type;
    }

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
        Graphics2D gr=(Graphics2D) evt.getComponent().getGraphics();
        resetRubber(gr);
        former = new PointMark2D(evt.getX(), evt.getY(), size, type);
        gr.draw(former);
    }

    public void clearFormers() {
        former=null;
    }

}
