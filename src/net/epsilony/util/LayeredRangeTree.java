/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.util;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import static org.apache.commons.math.util.MathUtils.log;
import static java.lang.Math.ceil;
import static java.lang.Math.pow;

/**
 *
 * @author epsilon
 */
public class LayeredRangeTree<Data> {

    ArrayList<Comparator<Data>> comparators;
    Tree rootTree;

    class Node {

        Node(Data key) {
            this.key = key;
        }

        boolean isLeaf() {
            return left == null;
        }
        Data key;
        Object associate;
        Node left, right;
    }

    class Tree {

        int dataBinarySearch(ArrayList<Data> datas, Data key) {
            int left = 0;
            int right = datas.size();
            int index;
            Comparator<Data> comp = comparators.get(0);
            if (comp.compare(datas.get(0), key) >= 0) {
                return 0;
            } else if (comp.compare(datas.get(datas.size() - 1), key) < 0) {
                return -1;
            }

            while (right > left) {
                index = (right + left) / 2;
                int compKeyData = comp.compare(key, datas.get(index));
                if (compKeyData <= 0) {
                    right = index;
                } else {
                    left = index + 1;
                }
            }
            return left;
        }

        void layerCascade(Node father) {
            ArrayList<Data> rightDatas, leftDatas;
            Node right, left;
            right = father.right;
            left = father.left;

            if (right.isLeaf()) {
                rightDatas = new ArrayList<>(1);
                rightDatas.add(0, right.key);
            } else {
                rightDatas = (ArrayList<Data>) right.associate;
                layerCascade(right);
            }

            if (left.isLeaf()) {
                leftDatas = new ArrayList<>(1);
                leftDatas.add(0, left.key);
            } else {
                leftDatas = (ArrayList<Data>) left.associate;
                layerCascade(left);
            }

            int dimIndex = 0;
            CasData fatherCas = new CasData((ArrayList<Data>) father.associate);
            father.associate = fatherCas;
            ArrayList<Data> fatherDatas = fatherCas.datas;
            int i, iLeft, iRight;
            for (i = 0, iLeft = 0, iRight = 0; i < fatherDatas.size(); i++) {
                if (iLeft >= leftDatas.size()) {
                    fatherCas.leftCas[i] = -1;
                } else if (0 == compare(leftDatas.get(iLeft), fatherDatas.get(i), dimIndex)) {
                    fatherCas.leftCas[i] = iLeft++;
                } else {
                    fatherCas.leftCas[i] = iLeft;
                }

                if (iRight >= rightDatas.size()) {
                    fatherCas.rightCas[i] = -1;
                } else if (0 == compare(rightDatas.get(iRight), fatherDatas.get(i), dimIndex)) {
                    fatherCas.rightCas[i] = iRight++;
                } else {
                    fatherCas.rightCas[i] = iRight;
                }
            }
        }

        void build(int dimIndex, ArrayList<Data> datas) {
            //initiate the tree: 
            if (datas.isEmpty()) {
                return;
            }
            if (datas.size() == 1) {
                rootNode = new Node(datas.get(0));
                return;
            }

            int treeDeep = (int) ceil(log(2, datas.size())) + 1;
            int fullTreeLeafNum = (int) pow(2, treeDeep - 1);
            int higherLeafNum = fullTreeLeafNum - datas.size();
            LinkedList<Node> dequeue = new LinkedList<>();
            for (int i = 0; i < higherLeafNum; i++) {
                Node tNode = new Node(datas.get(datas.size() - 1 - i));
                dequeue.addFirst(tNode);
            }
            for (int i = 0; i < datas.size() - higherLeafNum; i++) {
                Node tNode = new Node(datas.get(i));
                dequeue.addLast(tNode);
            }

            ArrayList<Data> tLefts = new ArrayList<>(1);
            ArrayList<Data> tRights = new ArrayList<>(1);
            tLefts.add(null);
            tRights.add(null);
            while (dequeue.size() > 1) {
                Node right = dequeue.pollLast();
                Node left = dequeue.pollLast();
                Node keyNode = left;
                while (!keyNode.isLeaf()) {
                    keyNode = keyNode.right;
                }
                Node father = new Node(keyNode.key);
                father.left = left;
                father.right = right;
                ArrayList<Data> rightDatas, leftDatas;

                if (right.isLeaf()) {
                    tRights.set(0, right.key);
                    rightDatas = tRights;
                } else {
                    rightDatas = (ArrayList<Data>) right.associate;
                    if (dimIndex >= 2) {
                        right.associate = new Tree();
                        ((Tree) right.associate).build(dimIndex - 1, rightDatas);
                    }
                }


                if (left.isLeaf()) {
                    tLefts.set(0, left.key);
                    leftDatas = tLefts;
                } else {
                    leftDatas = (ArrayList<Data>) left.associate;
                    if (dimIndex >= 2) {
                        left.associate = new Tree();
                        ((Tree) left.associate).build(dimIndex - 1, leftDatas);
                    }
                }
                father.associate = merge(leftDatas, rightDatas, dimIndex - 1);
                dequeue.addFirst(father);

            }
            rootNode = dequeue.pop();
            if (dimIndex >= 2) {
                ArrayList<Data> rootNodeDatas = (ArrayList<Data>) rootNode.associate;
                rootNode.associate = new Tree();
                ((Tree) rootNode.associate).build(dimIndex - 1, rootNodeDatas);
            } else {
                layerCascade(rootNode);
            }
        }

        void checkToAdd(Collection<Data> results, Node node, Data from, Data to, int highestDimIndex) {

            for (int i = highestDimIndex; i >= 0; i--) {
                Comparator<Data> comp = comparators.get(i);
                int fromComp = comp.compare(from, node.key);
                int toComp = comp.compare(to, node.key);
                if (fromComp > 0 || toComp < 0) {
                    return;
                }
            }
            results.add(node.key);
        }

        void query(int dimIndex, Data from, Data to, Collection<Data> results) {
            if (null == rootNode) {
                return;
            }
            if (rootNode.isLeaf()) {
                checkToAdd(results, rootNode, from, to, dimIndex);
            }

//            Comparator comp = comps[dim - 1];

            int compFrom;
            int compTo;
            Node splitNode = rootNode;
            Comparator<Data> comp = comparators.get(dimIndex);

            //determine the splitnode
            while (splitNode != null) {
                compFrom = comp.compare(from, splitNode.key);
                compTo = comp.compare(to, splitNode.key);
                if (compTo < 0) {
                    splitNode = splitNode.left;
                } else if (compFrom > 0) {
                    splitNode = splitNode.right;
                } else {
                    break;
                }
            }

            if (splitNode == null) {
                return;
            }

            if (splitNode.isLeaf()) {
                checkToAdd(results, splitNode, from, to, dimIndex - 1);
                return;
            }

            if (dimIndex >= 2) {
                Node leftSearchNode = splitNode.left;
                while (!leftSearchNode.isLeaf()) {
                    if (comp.compare(from, leftSearchNode.key) <= 0) {
                        if (leftSearchNode.right.isLeaf()) {
                            checkToAdd(results, leftSearchNode.right, from, to, dimIndex - 1);
                        } else {
                            ((Tree) leftSearchNode.right.associate).query(dimIndex - 1, from, to, results);
                        }
                        leftSearchNode = leftSearchNode.left;
                    } else {
                        leftSearchNode = leftSearchNode.right;
                    }
                }
                checkToAdd(results, leftSearchNode, from, to, dimIndex);

                Node rightSearchNode = splitNode.right;
                while (!rightSearchNode.isLeaf()) {
                    if (comp.compare(to, rightSearchNode.key) >= 0) {
                        if (rightSearchNode.left.isLeaf()) {
                            checkToAdd(results, rightSearchNode.left, from, to, dimIndex - 1);
                        } else {
                            ((Tree) rightSearchNode.left.associate).query(dimIndex - 1, from, to, results);
                        }
                        rightSearchNode = rightSearchNode.right;
                    } else {
                        rightSearchNode = rightSearchNode.left;
                    }
                }
                checkToAdd(results, rightSearchNode, from, to, dimIndex);

            } else { //dim==2
                Comparator<Data> compDown = comparators.get(0);

                CasData splitNodeCasData = (CasData) splitNode.associate;
                int splitBSearchIndex = dataBinarySearch(splitNodeCasData.datas, from);
                if (-1 == splitBSearchIndex) {
                    return;
                }

                int leftSearchCas = splitNodeCasData.leftCas[splitBSearchIndex];
                Node leftSearchNode = splitNode.left;
                while (!leftSearchNode.isLeaf() && leftSearchCas >= 0) {
                    if (comp.compare(from, leftSearchNode.key) <= 0) {
                        int tRightCasIndex = ((CasData) leftSearchNode.associate).rightCas[leftSearchCas];
                        if (tRightCasIndex >= 0) {

                            if (leftSearchNode.right.isLeaf()) {
                                checkToAdd(results, leftSearchNode.right, from, to, 0);
                            } else {
                                ArrayList<Data> tDatas = ((CasData) leftSearchNode.right.associate).datas;

                                for (int i = tRightCasIndex; i < tDatas.size(); i++) {
                                    if (compDown.compare(tDatas.get(i), to) <= 0) {
                                        results.add(tDatas.get(i));
                                    } else {
                                        break;
                                    }
                                }
                            }
                        }
                        leftSearchCas = ((CasData) leftSearchNode.associate).leftCas[leftSearchCas];
                        leftSearchNode = leftSearchNode.left;

                    } else {
                        leftSearchCas = ((CasData) leftSearchNode.associate).rightCas[leftSearchCas];
                        leftSearchNode = leftSearchNode.right;
                    }
                }

                checkToAdd(results, leftSearchNode, from, to, dimIndex);  //must from dimIndex


                int rightSearchCas = splitNodeCasData.rightCas[splitBSearchIndex];
                Node rightSearchNode = splitNode.right;
                while (!rightSearchNode.isLeaf() && rightSearchCas >= 0) {
                    if (comp.compare(to, rightSearchNode.key) >= 0) {
                        int tLeftCasIndex = ((CasData) rightSearchNode.associate).leftCas[rightSearchCas];
                        if (tLeftCasIndex >= 0) {
                            if (rightSearchNode.left.isLeaf()) {
                                checkToAdd(results, rightSearchNode.left, from, to, 0);
                            } else {
                                ArrayList<Data> tDatas = ((CasData) rightSearchNode.left.associate).datas;
                                int i;
                                for (i = tLeftCasIndex; i < tDatas.size(); i++) {
                                    if (compDown.compare(tDatas.get(i), to) <= 0) {
                                        results.add(tDatas.get(i));
                                    } else {
                                        break;
                                    }
                                }
                            }
                        }
                        rightSearchCas = ((CasData) rightSearchNode.associate).rightCas[rightSearchCas];
                        rightSearchNode = rightSearchNode.right;

                    } else {
                        rightSearchCas = ((CasData) rightSearchNode.associate).leftCas[rightSearchCas];
                        rightSearchNode = rightSearchNode.left;
                    }
                }


                checkToAdd(results, rightSearchNode, from, to, dimIndex); //must from dimIndex;

            }
        }
        Node rootNode;
    }

    //caution for recusive reson the **datas will be free()ed
    class CasData {

        CasData(ArrayList<Data> datas) {
            this.datas = datas;
            leftCas = new int[datas.size()];
            rightCas = new int[datas.size()];
        }
        ArrayList<Data> datas;
        int[] leftCas;
        int[] rightCas;
    }

    ArrayList<Data> merge(ArrayList<Data> from1, ArrayList<Data> from2, int dimIndex) {
        ArrayList<Data> merged = new ArrayList<>(from1.size() + from2.size());
        int i, i1, i2;
        for (i = 0, i1 = 0, i2 = 0; i < from1.size() + from2.size(); i++) {
            Data result;
            if (i1 >= from1.size()) {
                result = from2.get(i2++);
            } else if (i2 >= from2.size()) {
                result = from1.get(i1++);
            } else if (compare(from1.get(i1), from2.get(i2), dimIndex) < 0) {
                result = from1.get(i1++);
            } else {
                result = from2.get(i2++);
            }
            merged.add(result);
        }
        return merged;
    }

    int compare(Data ob1, Data ob2, int dimIndex) {
        int c = comparators.get(dimIndex).compare(ob1, ob2);
        if (c != 0) {
            return c;
        }
        for (int i = comparators.size() - 1; i >= 0; i--) {
            if (i == dimIndex) {
                continue;
            }
            int res = comparators.get(i).compare(ob1, ob2);
            if (res != 0) {
                return res;
            }
        }
        return 0;
    }

    void buildTree(Collection<Data> datas, List<Comparator<Data>> comps) {

        this.comparators = new ArrayList<>(comps);
        ArrayList<Data> treeDatas = new ArrayList<>(datas);
        Comparator comp = new Comparator<Data>() {

            @Override
            public int compare(Data o1, Data o2) {
                return LayeredRangeTree.this.compare(o1, o2, comparators.size() - 1);
            }
        };

        Collections.sort(treeDatas, comp);
        rootTree = new Tree();
        rootTree.build(comps.size() - 1, treeDatas);
    }

    void search(Collection<Data> results, Data from, Data to) {
        results.clear();
        rootTree.query(comparators.size() - 1, from, to, results);
    }
}
