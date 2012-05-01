/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.utils.java2d;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import static java.lang.Math.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author epsilonyuan@gmail.com
 */
public class PointMark2D implements Shape {

    private static Map<String, String> funNameMap = new HashMap<String, String>();
    

    static {
        funNameMap.put("+", "Plus");
        funNameMap.put("*", "Star");
        funNameMap.put("x", "X");
        funNameMap.put("o", "O");
        funNameMap.put(".", "Dot");
        funNameMap.put("r", "Rect");
        funNameMap.put("rect", "Rect");
    }
    double x;
    double y;
    double size;
    String type;
    private Path2D path = new Path2D.Double();
    private Shape shape = null;

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
    
    public double [] getCenter()
    {
        double [] t=new double[2];
        t[0]=x;
        t[1]=y;
        return t;
    }

    public PointMark2D(double x, double y, double size, String type) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.type = type.toLowerCase();
        getPointMark2D();
    }

    public boolean intersects(Rectangle2D arg0) {
        return shape.intersects(arg0);
    }

    public boolean intersects(double arg0, double arg1, double arg2, double arg3) {
        return shape.intersects(arg0, arg1, arg2, arg3);
    }

    public PathIterator getPathIterator(AffineTransform arg0, double arg1) {
        return shape.getPathIterator(arg0, arg1);
    }

    public PathIterator getPathIterator(AffineTransform arg0) {
        return shape.getPathIterator(arg0);
    }

    public Rectangle2D getBounds2D() {
        return shape.getBounds2D();
    }

    public Rectangle getBounds() {
        return shape.getBounds();
    }

    public boolean contains(Rectangle2D arg0) {
        return shape.contains(arg0);
    }

    public boolean contains(double arg0, double arg1, double arg2, double arg3) {
        return shape.contains(arg0, arg1, arg2, arg3);
    }

    public boolean contains(Point2D arg0) {
        return shape.contains(arg0);
    }

    public boolean contains(double arg0, double arg1) {
        return shape.contains(arg0, arg1);
    }

    /**
     * PointMark2D是各种点标记的Shape生成器
     * @param size 标记的大小
     * @param type
     */
    private void getPointMark2D() throws IllegalArgumentException {
        String t = funNameMap.get(type);
        if (t == null) {
            throw new IllegalArgumentException();
        }
        try {
            PointMark2D.class.getMethod("make" + t).invoke(this);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(PointMark2D.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(PointMark2D.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(PointMark2D.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(PointMark2D.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(PointMark2D.class.getName()).log(Level.SEVERE, null, ex);
        }

        shape = path.createTransformedShape(new AffineTransform(1, 0, 0, 1, x, y));
    }

    public void makeX() {
        double t = size / 4.0 * sqrt(2);
        path.moveTo(t * -1, t * -1);
        path.lineTo(t, t);
        path.moveTo(t, t * -1);
        path.lineTo(t * -1, t);
    }

    public void makePlus() {
        path.moveTo(size / 2.0 * -1, 0.0);
        path.lineTo(size / 2.0, 0.0);
        path.moveTo(0, size / 2.0 * -1);
        path.lineTo(0, size / 2.0);
    }

    public void makeO() {
        path.append(new Ellipse2D.Double(-size / 2, -size / 2, size, size), false);
        makeDot();
    }

    public void makeRect() {
        path.append(new Rectangle2D.Double(-size / 2, -size / 2, size, size), false);
    }

    public void makeDot() {
        path.moveTo(0, 0);
        path.lineTo(0, 0);
    }

    public void makeStar() {
        path.moveTo(0, -size / 2.0);
        path.lineTo(0, size / 2.0);
        double t1 = size / 4.0;
        double t2 = size / 4.0 * sqrt(3);
        path.moveTo(t2, t1);
        path.lineTo(-t2, -t1);
        path.moveTo(t2, -t1);
        path.lineTo(-t2, t1);
    }
}
