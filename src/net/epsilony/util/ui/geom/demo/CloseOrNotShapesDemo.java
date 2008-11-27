/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.util.ui.geom.demo;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import javax.swing.JFrame;
import javax.swing.JPanel;
import net.epsilony.util.ui.geom.ShapeUtils;

/**
 *
 * @author epsilon
 */
public class CloseOrNotShapesDemo extends JFrame {

    Path2D close = new Path2D.Double();
    Path2D open = new Path2D.Double();
    double xdis = 100;
    double ydis = 100;
    

    {
        close.moveTo(xdis, ydis);
        open.moveTo(xdis, ydis);
        close.curveTo(10 + xdis, ydis + 0,100+xdis,10+ydis,50+xdis,30+ydis);
        open.lineTo(10 + xdis, ydis + 0);
        close.lineTo(0 + xdis, ydis + 10);
        open.lineTo(0 + xdis, ydis + 10);
        close.closePath();
        close.moveTo(xdis * 1.5, ydis * 1.5);
        open.moveTo(xdis * 1.5, ydis * 1.5);
        close.lineTo(10 + xdis * 1.5, ydis * 1.5 + 0);
        open.lineTo(10 + xdis * 1.5, ydis * 1.5 + 0);
        close.lineTo(0 + xdis * 1.5, ydis * 1.5 + 10);
        open.lineTo(0 + xdis * 1.5, ydis * 1.5 + 10);
        close.closePath();

        open.moveTo(xdis * 1.5, ydis);
        open.lineTo(xdis * 1.5 + 10, ydis);
        open.moveTo(xdis * 1.5, ydis + 10);
        open.lineTo(xdis * 1.5 + 10, ydis);

        close.moveTo(xdis * 1.5, ydis);
        close.lineTo(xdis * 1.5 + 10, ydis);
        close.closePath();
        close.moveTo(xdis * 1.5, ydis + 10);
        close.lineTo(xdis * 1.5 + 10, ydis);
        close.closePath();
    }
    Shape closeShape = close.createTransformedShape(AffineTransform.getTranslateInstance(xdis, 0));
    Shape openShape = open.createTransformedShape(new AffineTransform());
    JPanel panel = new JPanel() {

        @Override
        protected void paintComponent(Graphics arg0) {
            Graphics2D gr = (Graphics2D) arg0;
            gr.setColor(Color.BLACK);
            gr.draw(openShape);
            gr.setColor(Color.RED);
            gr.draw(closeShape);
            Area openArea = new Area(openShape);
            Area closeArea = new Area(closeShape);
            openArea.transform(AffineTransform.getTranslateInstance(0, ydis));
            closeArea.transform(AffineTransform.getTranslateInstance(0, ydis));
            gr.setColor(Color.BLACK);
            gr.draw(openArea);
            gr.setColor(Color.RED);
            gr.draw(closeArea);
            System.out.println("OpenArea: isSinglular" + openArea.isSingular() + " isEmpty:" + openArea.isEmpty());
            System.out.println("closeArea: isSinglular" + closeArea.isSingular() + " isEmpty:" + closeArea.isEmpty());
            System.out.println(openArea);
            System.out.println(ShapeUtils.toString(openArea));
            System.out.println("closeShape:");
            System.out.println(ShapeUtils.toString(closeShape));
        }
    };

    public CloseOrNotShapesDemo() {
        panel.setSize(480, 480);
        add(panel);
        setSize(500, 500);
        setVisible(true);
    }

    public static void main(String[] args) {
        JFrame frame = new CloseOrNotShapesDemo();
    }
}
