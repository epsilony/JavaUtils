/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.util.ui.Model2D;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import net.epsilony.util.ui.interactive.SelectFilter;

/**
 *
 * @author epsilon
 */
public class Element2DManager {

    Object visHighElemLocker = new Object();//visibles highlights elemSet locker
    Object selLocker = new Object();  //isSelecting locker
    Object newElementAdapterLocker = new Object();
    Set<Element2D> elemSet = new HashSet<Element2D>();  //模型元素集
    Set<Element2D> visibles = new HashSet<Element2D>();   //可视元素集
    Set<Element2D> highlights = new HashSet<Element2D>();  //高亮元素集
    JComponent component;
    MouseAdapter fixMouseAdapter = new MouseAdapter() {

        @Override
        public void mouseClicked(MouseEvent arg0) {
            rulesIndex++;
            rulesIndex%=rules.length;
            component.repaint();
        }

        @Override
        public void mouseDragged(MouseEvent arg0) {
        }

        @Override
        public void mouseEntered(MouseEvent arg0) {
        }

        @Override
        public void mouseExited(MouseEvent arg0) {
        }

        @Override
        public void mouseMoved(MouseEvent arg0) {
        }

        @Override
        public void mousePressed(MouseEvent arg0) {
        }

        @Override
        public void mouseReleased(MouseEvent arg0) {
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent arg0) {
        }
    };//在管理级别需要的鼠标响应于此
    private Color visColor = Color.BLACK;
    private int rulesIndex;
   int rules[]={AlphaComposite.CLEAR,AlphaComposite.DST,AlphaComposite.DST_ATOP,AlphaComposite.DST_IN,AlphaComposite.DST_OUT,AlphaComposite.DST_OVER,
   AlphaComposite.SRC,AlphaComposite.SRC_ATOP,AlphaComposite.SRC_IN,AlphaComposite.SRC_OUT,AlphaComposite.SRC_OVER};

    synchronized public List<Element2D> generateSelectables(SelectFilter filter) {
        selectables.clear();
        for (Element2D elem : visibles) {
            if (filter.isSelectable(elem)) {
                selectables.add(elem);
            }
        }
        return selectables;
    }

    synchronized public Color getVisColor() {
        return visColor;
    }

    synchronized public void setVisColor(Color visColor) {
        this.visColor = visColor;
    }

    synchronized public void elementsAdd(Element2D t) {
        elemSet.add(t);
        visibles.add(t);
    }

    public Element2DManager(JComponent comp) {
        this.component = comp;
    }
    /**
     * 获取一个用于响应JPanel鼠标事件的MouseAdapter
     * 该MouseAdapter是实现交互动作的基础
     * @return
     */
    MouseAdapter mouseAdapter = new MouseAdapter() {

        @Override
        public void mouseClicked(MouseEvent arg0) {
            fixMouseAdapter.mouseClicked(arg0);

            synchronized (newElementAdapterLocker) {
                if (null != newElementMouseAdapter) {
                    newElementMouseAdapter.mouseClicked(arg0);
                }
            }

            synchronized (selLocker) {
                if (isSelecting()) {
                    selectMouseAdapter.mouseClicked(arg0);
                }
            }
        }

        @Override
        public void mouseDragged(MouseEvent arg0) {
            fixMouseAdapter.mouseDragged(arg0);
            synchronized (newElementAdapterLocker) {
                if (null != newElementMouseAdapter) {
                    newElementMouseAdapter.mouseDragged(arg0);
                }
            }

            synchronized (selLocker) {
                if (isSelecting()) {
                    selectMouseAdapter.mouseDragged(arg0);
                }
            }
        }

        @Override
        public void mouseEntered(MouseEvent arg0) {
            fixMouseAdapter.mouseEntered(arg0);
            synchronized (newElementAdapterLocker) {
                if (null != newElementMouseAdapter) {
                    newElementMouseAdapter.mouseEntered(arg0);
                }
            }

            synchronized (selLocker) {
                if (isSelecting()) {
                    selectMouseAdapter.mouseEntered(arg0);
                }
            }
        }

        @Override
        public void mouseExited(MouseEvent arg0) {
            fixMouseAdapter.mouseExited(arg0);
            synchronized (newElementAdapterLocker) {
                if (null != newElementMouseAdapter) {
                    newElementMouseAdapter.mouseExited(arg0);
                }
            }

            synchronized (selLocker) {
                if (isSelecting()) {
                    selectMouseAdapter.mouseExited(arg0);
                }
            }
        }

        @Override
        public void mouseMoved(MouseEvent arg0) {
            fixMouseAdapter.mouseMoved(arg0);
            synchronized (newElementAdapterLocker) {
                if (null != newElementMouseAdapter) {
                    newElementMouseAdapter.mouseMoved(arg0);
                }
            }

            synchronized (selLocker) {
                if (isSelecting()) {
                    selectMouseAdapter.mouseMoved(arg0);
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent arg0) {
            fixMouseAdapter.mousePressed(arg0);
            synchronized (newElementAdapterLocker) {
                if (null != newElementMouseAdapter) {
                    newElementMouseAdapter.mousePressed(arg0);
                }
            }

            synchronized (selLocker) {
                if (isSelecting()) {
                    selectMouseAdapter.mousePressed(arg0);
                }
            }
        }

        @Override
        public void mouseReleased(MouseEvent arg0) {
            fixMouseAdapter.mouseReleased(arg0);
            synchronized (newElementAdapterLocker) {
                if (null != newElementMouseAdapter) {
                    newElementMouseAdapter.mouseReleased(arg0);
                }
            }

            synchronized (selLocker) {
                if (isSelecting()) {
                    selectMouseAdapter.mouseReleased(arg0);
                }
            }
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent arg0) {
            fixMouseAdapter.mouseWheelMoved(arg0);
            synchronized (newElementAdapterLocker) {
                if (null != newElementMouseAdapter) {
                    newElementMouseAdapter.mouseWheelMoved(arg0);
                }
            }

            synchronized (selLocker) {
                if (isSelecting()) {
                    selectMouseAdapter.mouseWheelMoved(arg0);
                }
            }
        }
    };

    public MouseAdapter getMouseAdapter() {
        return mouseAdapter;
    }

    /**
     * 用于插入到某个JPanel的子类的paintComponet(Graphics gr)中的显示Element2Ds常态的函数
     * @param graphics
     */
    public void paint(Graphics graphics) {
        Graphics2D gr = (Graphics2D) graphics;
        gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        gr.setColor(visColor);
        if (isHighlight()) {
            for (Element2D elem : visibles) {
                if (highlights.contains(elem)) {
                    continue;
                }
                gr.draw(elem.getShape());
            }
        } else {
            for (Element2D elem : visibles) {
                gr.draw(elem.getShape());
            }
        }


        //不反走样的必须要后绘，奇怪。
        selectMouseAdapter.refresh(graphics);

        highlight(graphics);//将highlights中的所有Element2D全部高亮显示

        BufferedImage bi = new BufferedImage(500, 500, BufferedImage.TYPE_INT_RGB);
        WritableRaster wr = bi.getRaster();
        int[] rgb=new int[bi.getWidth()*bi.getHeight()*3];
        //Arrays.fill(rgb, 255);
        rgb[0]=123;
        rgb[1]=0;
        rgb[2]=0;
        wr.setPixels(0, 0, bi.getWidth(), bi.getHeight(), rgb);

        for (int i = 0; i < 256; i++) {
            rgb[1]=i;
            wr.setPixel(50 + i, 50 + i, rgb);
        }
//        Composite cp=AlphaComposite.getInstance(rules[rulesIndex]);
//        gr.setComposite(cp);
        gr.drawImage(bi, new AffineTransform(), component);}

    /*用于setVisible与setHighlight*/
    public final static int PARM_ADD = 1;
    public final static int PARM_RETAIN = 2;
    public final static int PARM_REMOVE = 3;
    public final static int PARM_REPLACE = 4;
    public final static int PARM_CLEAR = 5;

    public void setVisibles(Collection<? extends Element2D> c, int par) {
        Object locker = this;
        if (!Thread.holdsLock(locker)) {
            System.out.println("Cannot get locker of setVisibles(),now. Pls wait...");
        }
        synchronized (locker) {
            if (c != null) {
                switch (par) {
                    case PARM_ADD:
                        visibles.addAll(c);
                        break;
                    case PARM_REMOVE:
                        visibles.removeAll(c);
                        break;
                    case PARM_RETAIN:
                        visibles.retainAll(c);
                        break;
                    case PARM_REPLACE:
                        visibles.clear();
                        visibles.addAll(c);
                        break;
                    case PARM_CLEAR:
                        visibles.clear();
                        break;
                    default:
                        throw new IllegalArgumentException();
                }
            } else {
                switch (par) {
                    case PARM_ADD:
                        visibles.addAll(elemSet);
                        break;
                    case PARM_REMOVE:
                        visibles.removeAll(elemSet);
                        break;
                    case PARM_RETAIN:
                        visibles.retainAll(elemSet);
                        break;
                    case PARM_REPLACE:
                        visibles.clear();
                        visibles.addAll(elemSet);
                    case PARM_CLEAR:
                        visibles.clear();
                        break;
                    default:
                        throw new IllegalArgumentException();
                }
            }
        }
        repaint();
    }

    public void setHighlights(Collection<? extends Element2D> c, int par) {
        Object locker = this;
        if (!Thread.holdsLock(locker)) {
            System.out.println("Cannot get locker of setHighlights(),now. Pls wait...");
        }

        synchronized (locker) {
            if (c != null) {
                switch (par) {
                    case PARM_ADD:
                        highlights.addAll(c);
                        break;
                    case PARM_REMOVE:
                        highlights.removeAll(c);
                        break;
                    case PARM_RETAIN:
                        highlights.retainAll(c);
                        break;
                    case PARM_REPLACE:
                        highlights.clear();
                        highlights.addAll(c);
                        break;
                    case PARM_CLEAR:
                        highlights.clear();
                        break;
                    default:
                        throw new IllegalArgumentException();
                }
            } else {
                switch (par) {
                    case PARM_ADD:
                        highlights.addAll(elemSet);
                        break;
                    case PARM_REMOVE:
                        highlights.removeAll(elemSet);
                        break;
                    case PARM_RETAIN:
                        highlights.retainAll(elemSet);
                        break;
                    case PARM_REPLACE:
                        highlights.clear();
                        highlights.addAll(elemSet);
                    case PARM_CLEAR:
                        highlights.clear();
                        break;
                    default:
                        throw new IllegalArgumentException();
                }
            }
        }
        if (isHighlight()) {
            repaint();
        }
    }
    private boolean highlight = false;

    synchronized public boolean isHighlight() {
        return highlight;
    }

    synchronized public void setHighlight(boolean highlight) {
        this.highlight = highlight;
        repaint();
    }
    Color highColor = Color.RED;

    synchronized public Color getHighlightColor() {
        return highColor;
    }

    synchronized public void setHighlightColor(Color highColor) {
        this.highColor = highColor;
        repaint();
    }

    public void highlight(Graphics graphics) {
        if (!isHighlight()) {
            return;
        }
        Graphics2D gr = (Graphics2D) graphics;
        gr.setColor(highColor);
        gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for (Element2D elem : highlights) {
            gr.draw(elem.getShape());
        }
    }
    /**
     * createElement－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－    */
    /*用于选择适当的交互式图元生成器*/
    static final Map<String, Element2DOperator> element2DOperatorMap = new HashMap<String, Element2DOperator>();
    

    static {
        element2DOperatorMap.put("CubicCurve", new CubicCurve2DOperator());
    }
    /**
     * 交互新建一个type类的Element2D到模型中
     * @param type 所要新建的Element2D的type
     */
    Future<Element2D> newElementFuture = null;
    MouseAdapter newElementMouseAdapter = null;

    synchronized public MouseAdapter getNewElementMouseAdapter() {
        return newElementMouseAdapter;
    }

    synchronized public void setNewElementMouseAdapter(MouseAdapter newElementMouseAdapter) {
        this.newElementMouseAdapter = newElementMouseAdapter;
    }
    private boolean creating;

    synchronized public boolean isCreating() {
        return creating;
    }

    synchronized public void setCreating(boolean creating) {
        this.creating = creating;
    }

    public void createElement(String type) {
        Object locker = this;
        synchronized (locker) {
            if (isCreating()) {
                System.out.println("Being createElement(), now. Pls try next time");
                return;
            }
            setCreating(true);
        }

        System.out.println("here");
        Element2DOperator t = element2DOperatorMap.get(type);
        synchronized (newElementAdapterLocker) {
            newElementMouseAdapter = t.createOperator();
            newElementFuture = t.getFuture();
            Executors.newSingleThreadExecutor().execute(runNewElement);
        }
    }
    Runnable runNewElement = new Runnable() {

        public void run() {
            Element2D t = null;
            try {
                t = newElementFuture.get();
            } catch (InterruptedException ex) {
                Logger.getLogger(Element2DManager.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ExecutionException ex) {
                Logger.getLogger(Element2DManager.class.getName()).log(Level.SEVERE, null, ex);
            }


            if (t != null) {
                elementsAdd(t);
                visiblesAdd(t);
            }


            setNewElementMouseAdapter(null);
            setCreating(false);
            repaint();

        }
    };

    synchronized public void visiblesAdd(Element2D t) {
        visibles.add(t);
    }
//createElement----------------------------------------------------------------------
    private static List<Element2D> selectables = new LinkedList<Element2D>();//filter 后的选择范围
    private static List<Element2D> selectings = new LinkedList<Element2D>();//当前指针下的候选目标
    private static List<Element2D> selecteds = new LinkedList<Element2D>(); //已经选中的元素
    SelectMouseAdapter selectMouseAdapter = new SelectMouseAdapter();
    Element2D preSelecting = null;

    synchronized public List<Element2D> generateSelectings(double x, double y) {
        selectings.clear();
        for (Element2D e : selectables) {
            if (e.isSelectable(x, y)) {
                selectings.add(e);
            }
        }
        return selectings;
    }
    Color preSelectedsColor = Color.CYAN;
    Color selectedsColor = Color.ORANGE;
    Color selectingsColor = Color.PINK;
    Color preSelectingColor = Color.YELLOW;

    /**
    //     * 清除对上一次已经画出的图形的Shape缓存，用以在paintComponent中重绘图形时使用。
    //     *///
    private class SelectMouseAdapter extends MouseAdapter {

        /**
         *  当鼠标下可选的东东不止一个的时候即转入复选（checking）状态。
         */
        boolean checking;
        List<Element2D> preSelecteds = new LinkedList<Element2D>();

        /**
         * 当复选状态（checking==true）时，对鼠标的响应
         * @param evt
         */
        void checkingClicked(MouseEvent evt) {
            if (evt.getClickCount() > 1) {
                return;
            }

            switch (evt.getButton()) {
                case MouseEvent.BUTTON1:
                    if (!evt.isControlDown()) {
                        preSelecteds.clear();
                    }
                    if (null == preSelecting) {
                        break;
                    }
                    if (!preSelecteds.remove(preSelecting)) {
                        preSelecteds.add(preSelecting);
                    }
                    clickedPaint(evt);
                    break;
                case MouseEvent.BUTTON2:
                    selecteds.removeAll(preSelecteds);
                    selecteds.addAll(preSelecteds);
                    preSelecting = null;
                    preSelecteds.clear();
                    selectings.clear();
                    checking = false;
                    clickedPaint(evt);
                    break;
                case MouseEvent.BUTTON3:
                    if (null != preSelecting) {
                        preSelecting = selectings.get((selectings.indexOf(preSelecting) + 1) % selectings.size());
                    } else {
                        preSelecting = selectings.get(0);
                    }
                    clickedPaint(evt);
                    break;
            }

        }//当鼠标下可选的东东不止一个的时候即转入

        /**
         * 当不在复选状态时对鼠标点击的响应
         * 当按下1时：
         * 如果没有按下Ctrl，则清空已经选中的东东selecteds
         * 更新点击时鼠标下的东东selectings如果selectings不止一个，则转入复选状态并重画退出
         * 如果selectings是空的，则重画并退出
         * 这时selectings只剩一个，加入selecteds中，重画并退出
         * @param evt
         */
        void uncheckingClicked(MouseEvent evt) {
            switch (evt.getButton()) {
                case MouseEvent.BUTTON1:
                    if (!evt.isControlDown()) {
                        selecteds.clear();
                    }
                    if (selectings.isEmpty()) {
                        clickedPaint(evt);
                        return;
                    }
                    if (selectings.size() > 1) {
                        checking = true;
                        clickedPaint(evt);
                        return;
                    }
                    if (!selecteds.remove(selectings.get(0))) {
                        selecteds.add(selectings.get(0));
                        clickedPaint(evt);
                        return;
                    }
                    break;
                case MouseEvent.BUTTON2:
                    selectings.clear();
                    setSelecting(false);
                    clickedPaint(evt);
                    break;
                case MouseEvent.BUTTON3:
                    if (!selecteds.isEmpty()) {
                        selecteds.remove(selecteds.size() - 1);
                    }
                    clickedPaint(evt);
                    break;
            }
        }

        @Override
        public void mouseClicked(MouseEvent evt) {
            if (evt.getClickCount() > 1) {
                return;
            }

            if (checking) {
                checkingClicked(evt);
            } else {
                uncheckingClicked(evt);
            }

        }

        @Override
        public void mouseMoved(MouseEvent evt) {
            double x = evt.getX();
            double y = evt.getY();
            if (checking) {
                for (Element2D e : selectings) {
                    if (e.isSelectable(x, y)) {
                        preSelecting = e;
                        break;
                    }
                }
            } else {
                generateSelectings(x, y);
            }
            movedPaint(evt);
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent evt) {
        }
        List<Element2D> formerSelectings = new LinkedList<Element2D>();
        List<Element2D> formerSelecteds = new LinkedList<Element2D>();
        List<Element2D> formerPreSelecteds = new LinkedList<Element2D>();
        Element2D formerPreSelecting = null;

        /**
         * 当按下鼠标后的paint!与refresh()大部分内容相同
         * @param evt
         */
        private void clickedPaint(MouseEvent evt) {
            Graphics2D gr = (Graphics2D) evt.getComponent().getGraphics();
            gr.setXORMode(Color.WHITE);
            gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            gr.setColor(selectingsColor);
            for (Element2D e : formerSelectings) {
                gr.draw(e.getSelectableArea());
            }
            formerSelectings.clear();
            for (Element2D e : selectings) {
                if (!selecteds.contains(e) && preSelecting != e) {
                    gr.draw(e.getSelectableArea());
                    formerSelectings.add(e);
                }
            }

            gr.setColor(selectedsColor);
            for (Element2D e : formerSelecteds) {
                gr.draw(e.getSelectableArea());
            }
            formerSelecteds.clear();
            for (Element2D e : selecteds) {
                gr.draw(e.getSelectableArea());
                formerSelecteds.add(e);
            }

            gr.setColor(preSelectedsColor);
            for (Element2D e : formerPreSelecteds) {
                gr.draw(e.getSelectableArea());
            }
            formerPreSelecteds.clear();
            for (Element2D e : preSelecteds) {
                gr.draw(e.getSelectableArea());
                formerPreSelecteds.add(e);
            }

            gr.setColor(preSelectingColor);
            if (null != formerPreSelecting) {
                gr.draw(formerPreSelecting.getSelectableArea());
            }
            if (null != preSelecting) {
                gr.draw(preSelecting.getSelectableArea());
            }
            formerPreSelecting = preSelecting;
        }

        public void refresh(Graphics graphics) {
            Graphics2D gr = (Graphics2D) graphics;
            gr.setXORMode(Color.WHITE);
            gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            gr.setColor(selectingsColor);
            formerSelectings.clear();
            for (Element2D e : selectings) {
                if (!selecteds.contains(e) && preSelecting != e) {
                    gr.draw(e.getSelectableArea());
                    formerSelectings.add(e);
                }
            }

            gr.setColor(selectedsColor);
            formerSelecteds.clear();
            for (Element2D e : selecteds) {
                gr.draw(e.getSelectableArea());
                formerSelecteds.add(e);
            }

            formerPreSelecteds.clear();
            for (Element2D e : preSelecteds) {
                gr.draw(e.getSelectableArea());
                formerPreSelecteds.add(e);
            }

            gr.setColor(preSelectingColor);
            if (null != preSelecting) {
                gr.draw(preSelecting.getSelectableArea());
            }
            formerPreSelecting = preSelecting;
        }

        private void movedPaint(MouseEvent evt) {
            Graphics2D gr = (Graphics2D) evt.getComponent().getGraphics();
            gr.setXORMode(Color.WHITE);
            gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            gr.setColor(selectingsColor);
            if (!checking) {
                for (Element2D e : formerSelectings) {
                    gr.draw(e.getSelectableArea());
                }
                formerSelectings.clear();
                for (Element2D e : selectings) {
                    gr.draw(e.getSelectableArea());
                    formerSelectings.add(e);
                }
            }
            if (formerPreSelecting != preSelecting) {
                gr.setColor(preSelectingColor);
                if (null != formerPreSelecting) {
                    gr.draw(formerPreSelecting.getSelectableArea());
                }
                if (null != preSelecting) {
                    gr.draw(preSelecting.getSelectableArea());
                }
                formerPreSelecting = preSelecting;
            }
        }
    };
    private boolean selecting = false;

    synchronized public boolean isSelecting() {
        return selecting;
    }

    synchronized public void setSelecting(boolean selecting) {
        this.selecting = selecting;
    }

    public void selectElements(SelectFilter filter) {
        synchronized (this) {
            if (isSelecting()) {
                System.out.println("Being selecting Elements now. Pls try next time...");
                return;
            }
            setSelecting(true);
        }
        generateSelectables(filter);
    }

    synchronized public Element2D[] getElements() {
        return elemSet.toArray(new Element2D[0]);
    }

    synchronized public boolean visiblesRemove(Object arg0) {
        return visibles.remove(arg0);
    }

    synchronized public boolean highlightsRemove(Object arg0) {
        return highlights.remove(arg0);
    }

    synchronized public boolean highlightsAdd(Element2D arg0) {
        return highlights.add(arg0);
    }

    synchronized public boolean highlightsAddAll(Collection<? extends Element2D> arg0) {
        return highlights.addAll(arg0);
    }

    void repaint() {
        component.getParent().repaint();
    }
}
