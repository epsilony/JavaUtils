/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.util.ui.Model2D;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import net.epsilony.util.ui.geom.PointMark2D;
import net.epsilony.util.ui.interactive.PointMark2DRubber;
import net.epsilony.util.ui.interactive.Rubber2D;
import net.epsilony.util.ui.interactive.Rubber2DAbstract;

/**
 *
 * @author epsilon
 */
public class CubicCurve2DOperator implements Element2DOperator {

    private double pointMarkSize = 10;
    Object locker = new Object();
    private Color rubberColor = Color.GRAY;
    double[] points = new double[8];
    int step = 0;
    /**
     * 用于操作的鼠标动作处理
     * 
     */
    MouseAdapter mouseAdapter = new MouseAdapter() {

        @Override
        public void mouseClicked(MouseEvent arg0) {
            switch (arg0.getButton()) {
                case MouseEvent.BUTTON1:
                    if (step == 4) {
                        setElem(new CubicCurve2DElement(points[0], points[1], points[4], points[5], points[6], points[7], points[2], points[3]));
                        synchronized (future) {
                            future.notifyAll();
                        }
                        step = 0;
                        rubber.resetRubber(arg0.getComponent().getGraphics());
                        rubber = rubbers[0];
                        return;
                    }
                    points[step * 2] = arg0.getX();
                    points[step * 2 + 1] = arg0.getY();
                    step++;
                    break;
                case MouseEvent.BUTTON3:
                    if (step == 0 || arg0.getClickCount() > 1) {
                        future.cancel(true);
                        step = 0;
                        rubber.resetRubber(arg0.getComponent().getGraphics());
                        rubber = rubbers[0];
                        return;
                    } else {
                        step--;
                    }
                    break;
            }
            rubber.resetRubber(arg0.getComponent().getGraphics());
            rubber = rubbers[step];
            rubber.showRubber(arg0);
        }

        @Override
        synchronized public void mouseExited(MouseEvent arg0) {
            rubber.getRubberMouseAdapter().mouseExited(arg0);
        }

        @Override
        synchronized public void mouseMoved(MouseEvent arg0) {
            rubber.getRubberMouseAdapter().mouseMoved(arg0);
        }
    };    //当前状态下的橡皮效果生成器
    Rubber2D rubber = rubbers[0];
    static Path2D path = new Path2D.Double();//在rubbers中应用，用于快速获取rubber shape
    static AffineTransform trans = new AffineTransform();//在rubbers中应用
/*rubbers here:*/
    public static final Rubber2D[] rubbers = new Rubber2D[5];
    

    {
        //+为操作点，rect为已定点，X为未定点
        //顺序 x1,y1;x2,y2;ctrlx1,ctrly2;ctrlx2,ctrly2;
        rubbers[0] = new PointMark2DRubber(rubberColor, pointMarkSize, "+");
        rubbers[1] = new Rubber2DAbstract() {

            @Override
            public Shape getShape(double x, double y) {
                path.reset();
                path.moveTo(points[0], points[1]);
                path.lineTo(x, y);
                path.append(new PointMark2D(points[0], points[1], pointMarkSize, "rect"), false);
                path.append(new PointMark2D((x - points[0]) / 3.0 + points[0], (y - points[1]) / 3.0 + points[1], pointMarkSize, "x"), false);
                path.append(new PointMark2D(2 * (x - points[0]) / 3.0 + points[0], 2 * (y - points[1]) / 3.0 + points[1], pointMarkSize, "x"), false);
                path.append(new PointMark2D(x, y, pointMarkSize, "+"), false);
                return path.createTransformedShape(trans);
            }
        };
        rubbers[2] = new Rubber2DAbstract() {

            public Shape getShape(double x, double y) {
                path.reset();
                path.append(new PointMark2D(points[0], points[1], pointMarkSize, "rect"), false);
                double ctrlx1 = x;
                double ctrly1 = y;
                path.append(new PointMark2D(ctrlx1, ctrly1, pointMarkSize, "+"), false);
                double ctrlx2 = (x + points[2]) / 2;
                double ctrly2 = (y + points[3]) / 2;
                path.append(new PointMark2D(ctrlx2, ctrly2, pointMarkSize, "x"), false);
                path.append(new PointMark2D(points[2], points[3], pointMarkSize, "rect"), false);
                path.moveTo(points[0], points[1]);
                path.curveTo(ctrlx1, ctrly1, ctrlx2, ctrly2, points[2], points[3]);
                return path.createTransformedShape(trans);
            }
        };
        rubbers[3] = new Rubber2DAbstract() {

            public Shape getShape(double x, double y) {
                path.reset();
                path.append(new PointMark2D(points[0], points[1], pointMarkSize, "rect"), false);
                double ctrlx1 = points[4];
                double ctrly1 = points[5];
                path.append(new PointMark2D(ctrlx1, ctrly1, pointMarkSize, "rect"), false);
                double ctrlx2 = x;
                double ctrly2 = y;
                path.append(new PointMark2D(ctrlx2, ctrly2, pointMarkSize, "+"), false);
                path.append(new PointMark2D(points[2], points[3], pointMarkSize, "rect"), false);
                path.moveTo(points[0], points[1]);
                path.curveTo(ctrlx1, ctrly1, ctrlx2, ctrly2, points[2], points[3]);
                return path.createTransformedShape(trans);
            }
        };
        rubbers[4] = new Rubber2DAbstract() {

            @Override
            public Shape getShape(double x, double y) {
                path.reset();
                path.append(new PointMark2D(points[0], points[1], pointMarkSize, "rect"), false);
                double ctrlx1 = points[4];
                double ctrly1 = points[5];
                path.append(new PointMark2D(ctrlx1, ctrly1, pointMarkSize, "rect"), false);
                double ctrlx2 = points[6];
                double ctrly2 = points[7];
                path.append(new PointMark2D(ctrlx2, ctrly2, pointMarkSize, "rect"), false);
                path.append(new PointMark2D(points[2], points[3], pointMarkSize, "rect"), false);
                path.moveTo(points[0], points[1]);
                path.curveTo(ctrlx1, ctrly1, ctrlx2, ctrly2, points[2], points[3]);
                return path.createTransformedShape(trans);
            }

            @Override
            public void showRubber(MouseEvent evt) {
                if (null == former) {
                    former = getShape(0, 0);
                    Graphics2D gr = (Graphics2D) evt.getComponent().getGraphics();
                    gr.setXORMode(Color.white);
                    gr.setColor(rubberColor);
                    gr.draw(former);
                }
            }
        };
    }
/*//ruubers here!<---------------------------------------------------*/
    
    Element2D elem = null;
    Future<Element2D> creatorFuture = new Future<Element2D>() {

        boolean isCancelled = false;
        boolean isDone = false;

        synchronized boolean getIsCancelled() {
            return isCancelled;
        }

        synchronized void setIsCancelled(boolean isCancelled) {
            this.isCancelled = isCancelled;
        }

        public boolean cancel(boolean arg0) {
            setIsCancelled(true);
            synchronized (future) {
                future.notifyAll();
                return true;
            }
        }

        public boolean isCancelled() {
            return getIsCancelled();
        }

        synchronized public boolean isDone() {
            return isDone;
        }

        public Element2D get() throws InterruptedException, ExecutionException {
            synchronized (this) {
                setIsCancelled(false);
                this.wait();
                if (isCancelled) {
                    throw new InterruptedException();
                }
                if (elem != null) {
                    isDone = true;
                    return elem;
                } else {
                    setIsCancelled(true);
                    throw new InterruptedException();
                }
            }
        }

        public Element2D get(long arg0, TimeUnit arg1) throws InterruptedException, ExecutionException, TimeoutException {
            synchronized (this) {
                if (isCancelled) {
                    throw new InterruptedException();
                }
                this.wait(arg1.convert(arg0, arg1));
                if (elem != null) {
                    isDone = true;
                    return elem;
                } else {
                    setIsCancelled(true);
                    throw new InterruptedException();
                }
            }
        }
    };
    Future<Element2D> future = null;

    synchronized Element2D getElem() {
        return elem;
    }

    synchronized void setElem(Element2D elem) {
        this.elem = elem;
    }

    public MouseAdapter createOperator() {
        rubber = rubbers[0];
        future = creatorFuture;
        return mouseAdapter;
    }

    public MouseAdapter modifyOperator(Element2D elem) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Future<Element2D> getFuture() {
        return future;
    }
}
