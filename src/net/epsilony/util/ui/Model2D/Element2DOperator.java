/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.epsilony.util.ui.Model2D;

import java.awt.event.MouseAdapter;
import java.util.concurrent.Future;

/**
 * 应用交互的生成或修改图元的一个接口一般的使用模式是：
 * <br>1. 调用createOperator()/modifyOperator()获取一个MouseAdapter，将这个MouseAdapter挂进一个JPanel的ActionListener</br>
 * <br>2. 调用getFuture(),获取一个Future<Element2D>,并且新建一个进程,调用future.get()具体形式见{@link Element2DManager#newElement(java.lang.String) }</br>
 * 注意，1，2部顺序不能倒置，且用完一次createOperator()/modifyOperator()后，类内部自动生成一个与最近一次create/modify动作有关的future
 * <br>切记切记！！</br>
 * @author epsilon
 */
public interface Element2DOperator {
    MouseAdapter createOperator();
    /**
     * @deprecated 末完成设计
     * 
     * @param elem
     * @return
     */
    MouseAdapter modifyOperator(Element2D elem);
    Future<Element2D> getFuture();
}
