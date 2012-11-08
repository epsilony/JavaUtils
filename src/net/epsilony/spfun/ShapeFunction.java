/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.spfun;

import gnu.trove.list.array.TDoubleArrayList;
import java.util.Collection;
import java.util.List;
import net.epsilony.utils.PartDiffOrdered;
import net.epsilony.utils.geom.Coordinate;

/**
 * 形函数 Shape Function。</br>
 * 使用时首先通过
 * {@link #setPDTypes(net.epsilony.simpmeshfree.model.PartialDiffType[])}
 * 设定{@link #values(net.epsilony.geom.Coordinate, java.util.List, no.uib.cipr.matrix.DenseVector[]) }
 * 与{@link #values(net.epsilony.geom.Coordinate, java.util.List, java.util.Collection, no.uib.cipr.matrix.DenseVector[], java.util.List) }
 * 的输出样式</br>
 * 典型的实现为{@link ShapeFunctions2D.MLS}</br>
 * @see PartialDiffType
 * @author epsilonyuan@gmail.com
 */
public interface ShapeFunction extends PartDiffOrdered{   
    /**
     * @param pos   Input
     * @param nodes Input
     * @param ndDistSqs Input, Wheather not null, must be fit for differential order and be as long as {@code nodes}. If null, the implementation should take the Euclidean distance as default
     * @param ndInfRads Input, if {@code ndInfRads.size()} is 1, all the nodes share the same influence radiu that is {@ndInfRads.get(0)}
     * @param result Output, can be null.
     * @return
     */
    TDoubleArrayList[] values(Coordinate pos,List<? extends Coordinate> nodes,TDoubleArrayList[] ndDistSqs,TDoubleArrayList ndInfRads, TDoubleArrayList[] result);
}
