/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.util.collection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import static java.lang.Math.*;

/**
 *
 * @author epsilon
 */
public class LayeredDomainTree<T> implements Serializable{

    ArrayList<T> points = new ArrayList<T>();
    Comparator<? super T> outerComp, innerComp;
    ArrayList<T> leafNodeValues;
    ArrayList<ArrayList<T>> tree;
    int higherNodesBase;
    int higherNodesCount;

    public Collection<T> getPoints(){
        return points;
    }
    public static int father(int child) {
        return (child - 1) / 2;
    }

    public static int leftChild(int father) {
        return father * 2 + 1;
    }

    public static int rightChild(int father) {
        return father * 2 + 2;
    }

    public LayeredDomainTree(Collection<T> inputPoints, Comparator<? super T> compW, Comparator<? super T> compH, boolean wider) {
        this.points.addAll(inputPoints);
        if (wider) {
            outerComp = compW;
            innerComp = compH;
        } else {
            outerComp = compH;
            innerComp = compW;
        }
        Collections.sort(this.points, outerComp);
        LinkedList<ArrayList<T>> leafNodes = new LinkedList<ArrayList<T>>();
        T tp = points.get(0), tp2;
        int start = 0;
        for (int i = 1; i < points.size(); i++) {
            tp2 = points.get(i);
            if (outerComp.compare(tp, tp2) < 0) {
                leafNodes.add(new ArrayList(points.subList(start, i)));
                tp = tp2;
                start = i;
            }
        }
        leafNodes.add(new ArrayList(points.subList(start, points.size())));
        leafNodeValues = new ArrayList<T>(leafNodes.size());
        for (ArrayList<T> ln : leafNodes) {
            Collections.sort(ln, innerComp);
            leafNodeValues.add(ln.get(0));
        }
        int k = (int) floor(log(leafNodes.size()) / log(2));
        int lowerNodesCount = (int) (pow(2, k + 1) - leafNodes.size());
        higherNodesCount = leafNodes.size() - lowerNodesCount;
        higherNodesBase = (int) pow(2, k + 1) - 1;
        int size = higherNodesBase + higherNodesCount;

        tree = new ArrayList<ArrayList<T>>(size);
        for (int i = 0; i < size; i++) {
            tree.add(null);
        }
        int base = (int) pow(2, k + 1) - 1;
        ArrayList<T> node;
        for (int i = 0; i < higherNodesCount; i += 2) {
            tree.set(base + i, leafNodes.get(i));
            tree.set(base + i + 1, leafNodes.get(i + 1));
            tree.set(father(base + i), node = new ArrayList<T>(leafNodes.get(i).size() + leafNodes.get(i + 1).size()));
            node.addAll(leafNodes.get(i));
            node.addAll(leafNodes.get(i + 1));
            Collections.sort(node, innerComp);
        }

        for (int i = 0; i < lowerNodesCount; i++) {
            tree.set(base - lowerNodesCount + i, leafNodes.get(i + higherNodesCount));
        }
        ArrayList<T> lc, rc;
        for (int i = k - 1; i >= 0; i--) {
            base = (int) pow(2, i) - 1;
            size = (int) pow(2, i) + base;
            for (int j = base; j < size; j++) {
                lc = tree.get(leftChild(j));
                rc = tree.get(rightChild(j));
                node = new ArrayList<T>(lc.size() + rc.size());
                node.addAll(lc);
                node.addAll(rc);
                Collections.sort(node, innerComp);
                tree.set(j, node);
            }
        }
    }

    public List<T> domainSearch(List<T> list, T from, T to) {
        list.clear();
        ArrayList<T> node;
        int t1, t2;
        int fromIndex = CollectionUtils.ceil(leafNodeValues, from, outerComp);
        int toIndex = CollectionUtils.floor(leafNodeValues, to, outerComp);
//        System.out.println("toIndex = " + toIndex);
//        System.out.println("fromIndex = " + fromIndex);
        if (fromIndex == -1 || toIndex == -1) {
            return list;
        }
        if (fromIndex < higherNodesCount && toIndex >= higherNodesCount) {
            fromIndex += higherNodesBase;
            toIndex += higherNodesBase - leafNodeValues.size();
            if (fromIndex % 2 == 0) {
                node = tree.get(fromIndex);
                t1 = CollectionUtils.ceil(node, from, innerComp);
                t2 = CollectionUtils.floor(node, to, innerComp);
                if (t1 != -1 && t2 != -1) {
                    list.addAll(node.subList(t1, t2 + 1));
                }
                fromIndex = father(fromIndex) + 1;
            } else {
                fromIndex = father(fromIndex);
            }
        } else {
            if (fromIndex >= higherNodesCount) {
                fromIndex += higherNodesBase - leafNodeValues.size();
                toIndex += higherNodesBase - leafNodeValues.size();
            } else {
                fromIndex += higherNodesBase;
                toIndex += higherNodesBase;
            }
        }
//        System.out.println("fromIndex2 = " + fromIndex);
//        System.out.println("toIndex2 = " + toIndex);

        while (fromIndex < toIndex) {
            if (fromIndex % 2 == 0) {
                node = tree.get(fromIndex);
                t1 = CollectionUtils.ceil(node, from, innerComp);
                t2 = CollectionUtils.floor(node, to, innerComp);
                if (t1 != -1 && t2 != -1) {
                    list.addAll(node.subList(t1, t2 + 1));
                }
                fromIndex = father(fromIndex) + 1;
            } else {
                fromIndex = father(fromIndex);
            }
            if (toIndex % 2 != 0) {
                node = tree.get(toIndex);
                t1 = CollectionUtils.ceil(node, from, innerComp);
                t2 = CollectionUtils.floor(node, to, innerComp);
                if (t1 != -1 && t2 != -1) {
                    list.addAll(node.subList(t1, t2 + 1));
                }
                toIndex = father(toIndex) - 1;
            } else {
                toIndex = father(toIndex);
            }
        }

        if (toIndex == fromIndex) {
            node = tree.get(fromIndex);
            t1 = CollectionUtils.ceil(node, from, innerComp);
            t2 = CollectionUtils.floor(node, to, innerComp);
            if (t1 != -1 && t2 != -1) {
                list.addAll(node.subList(t1, t2 + 1));
            }
        }


        return list;
    }
}
