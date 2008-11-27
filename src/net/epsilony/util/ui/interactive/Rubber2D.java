/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.epsilony.util.ui.interactive;

import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 用于捕获鼠票并生成橡皮效果的接口
 * @author epsilon
 */
public interface Rubber2D {
    /**
     * 获取处理鼠标事件的MouseAdapter
     * @return 用于JPanel的AddActionListener()
     */
    public MouseAdapter getRubberMouseAdapter();
    
    /**
     * 清除前一次在JPanel上所画的东东，一般在{@link Rubber2D#getRubberMouseAdapter() }中的mouseMoved与mouseExited中应用
     * @param gr 用于JPanel draw 的Graphics2D
     */
    public void resetRubber(Graphics gr);
    
    /**
     * 跟据evt生成一个新的Shape，一般在mouseMoved中紧根{@link Rubber2D#resetRubber(java.awt.Graphics) }
     * @param evt 鼠标事件
     */
    public void showRubber(MouseEvent evt);
    
    /**
     * 清除对上一次已经画出的图形的Shape缓存，用以在paintComponent中重绘图形时使用。
     */
    public void clearFormers();
}
