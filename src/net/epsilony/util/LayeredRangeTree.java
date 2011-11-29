/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.util;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import static org.apache.commons.math.util.MathUtils.log;
import static java.lang.Math.ceil;
import static java.lang.Math.pow;

/**
 * <p>
 * A Layered Range Tree is a fractional cascading Range Tree. </br>
 * the build time is O(log<sup>d</sup>(n)n) </br>
 * the search time is O(log<sup>(d-1)</sup>(n)) </br>
 * holdind a Layered Rang Tree need O(log<sup>d</sup>(n)n) memory</br>
 * in fact it's about 72*log<sup>d</sup><sub>2</sub>(n)*n+32*log<sup>d-1</sup><sub>2</sub>*n+72*log<sub>2</sub>(n)*n+dataMemory   (bytes)
 * where d means dimension;</br>
 * </p>
 * <p>
 * The whole algorithm is described minutely in Mark de Berg et. al. <i>Computational
 * Geometry Algorithms and Applications(Third Edition)</i> Ch5 </br>
 * It should be pointed out that the input datas should not contain 
 * duplicate objects, test is needed for this situation. In order to get a Collection without
 * duplications, firstly sort the Collection with a compartor from:</br>
 * {@code tree.}{@link #getDictComparator getDictComparator}{@code (tree.}{@link #getDimension getDimension()}{@code -1)}</br>
 * and then filter the duplicated objects.  
 * Input a sorted Collection by dictionary comparator with priority from last dimension to the first will not
 * suffer too much overhead.
 * </p>
 * <p>
 * Some facility tools are supplied for building 2D({@link LayeredRangeTree.Double2D}) or 3D({@link LayeredRangeTree.Double3D}) trees from double array.
 * <ul>
 * <li>{@link #buildDouble2DSearchTree}
 * <li>{@link #buildDouble3DSearchTree}
 * </ul>
 * And some facilities do up operations' backforward:
 * <ul>
 * <li>{@link #fillDatas2D}
 * <li>{@link #fillDatas3D}
 * </p>
 * <p> for common objects need to combine with indes use {@link IndexWrapper}
 * @see IndexWrapper#wrapWithIndex(java.util.Collection, java.util.ArrayList, int[], java.util.List, java.util.ArrayList) 
 * </p>
 * @author epsilonyuan@gmail.com
 */
public class LayeredRangeTree<Data> {

    /**
     * The dimension is equaals to the number of Data's comparators;
     * @return the dimension of this tree 
     */
    public int getDimension() {
        return comparators.size() - 1;
    }

    /**
     * Construct a Layered Range Tree in order to {@line #search} it soon<br>
     * The input Collections will be copied inside, so they are safe.
     * @param datas the datas going to be search
     * @param comps the comparators of dimension 1, 2, ... n, the whole tree's dimension is depend on this
     */
    public LayeredRangeTree(Collection<Data> datas, List<Comparator<Data>> comps) {
        buildTree(datas, comps);
    }

    /**
     * Get a dictionary Comparator with dimension priority sequence(from higher to lower) <b>priDimIndex</b>,n,n-1,<b>priDimIndex+1</b>,<b>priDimIndex-1</b>,...,1</br>
     * @param priDimIndex the first priority compare dimension
     * @return  In fact, build a Comparator wrapping {@link #dictCompare dictCompare(priDimIndex)}
     */
    public Comparator<Data> getDictComparator(final int priDimIndex) {
        return new Comparator<Data>() {

            @Override
            public int compare(Data o1, Data o2) {
                return dictCompare(o1, o2, priDimIndex);
            }
        };
    }



    /**
     * a utility class for wrapping 3D double datas, see also:
     * <ul>
     * <li>{@link #buildDouble3DSearchTree(double[], int, boolean, int[]) }
     * <li>{@link #wrapDouble3D(double[], int, boolean, int[]) }
     * </ul>
     */
    public static class Double3D {

        public double x, y, z;

        public Double3D(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public static int compareX(Double3D d1, Double3D d2) {
            return (int) Math.signum(d1.x - d2.x);
        }

        public static int compareY(Double3D d1, Double3D d2) {
            return (int) Math.signum(d1.y - d2.y);
        }

        public static int compareZ(Double3D d1, Double3D d2) {
            return (int) Math.signum(d1.z - d2.z);
        }

        public static List<Comparator<Double3D>> getComparators() {
            Comparator<Double3D> compX, compY, compZ;
            compX = new Comparator<Double3D>() {

                @Override
                public int compare(Double3D d1, Double3D d2) {
                    return (int) Math.signum(d1.x - d2.x);
                }
            };
            compY = new Comparator<Double3D>() {

                @Override
                public int compare(Double3D d1, Double3D d2) {
                    return (int) Math.signum(d1.y - d2.y);
                }
            };
            compZ = new Comparator<Double3D>() {

                @Override
                public int compare(Double3D d1, Double3D d2) {
                    return (int) Math.signum(d1.z - d2.z);
                }
            };
            List<Comparator<Double3D>> result = Arrays.asList(compX, compY, compZ);
            return result;
        }

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }

        public double getZ() {
            return z;
        }

        public void setZ(double z) {
            this.z = z;
        }

        public void fill(double[] datas, int start) {
            datas[start] = x;
            datas[start + 1] = y;
            datas[start + 2] = z;
        }
    }

    /**
     * a utility class for wrapping 3D double datas with indes, see also:
     * <ul>
     * <li>{@link #buildDouble3DSearchTree(double[], int, boolean, int[]) }
     * <li>{@link #wrapDouble3D(double[], int, boolean, int[]) }
     * </ul>
     */
    public static class DoubleIndexed3D extends Double3D {

        public int index;

        public DoubleIndexed3D(int index, double x, double y, double z) {
            super(x, y, z);
            this.index = index;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }
    }

    /**
     * a utility class for wrapping 2D double datas, see also:
     * <ul>
     * <li>{@link #buildDouble2DSearchTree(double[], int, boolean, int[]) }
     * <li>{@link #wrapDouble2D(double[], int, boolean, int[]) }
     * </ul>
     */
    public static class Double2D {

        public double x, y;

        public static int compareX(Double3D d1, Double3D d2) {
            return (int) Math.signum(d1.x - d2.x);
        }

        public static int compareY(Double3D d1, Double3D d2) {
            return (int) Math.signum(d1.y - d2.y);
        }

        public static List<Comparator<Double2D>> getComparators() {
            Comparator<Double2D> compX, compY;
            compX = new Comparator<Double2D>() {

                @Override
                public int compare(Double2D d1, Double2D d2) {
                    return (int) Math.signum(d1.x - d2.x);
                }
            };
            compY = new Comparator<Double2D>() {

                @Override
                public int compare(Double2D d1, Double2D d2) {
                    return (int) Math.signum(d1.y - d2.y);
                }
            };
            List<Comparator<Double2D>> result = Arrays.asList(compX, compY);
            return result;
        }

        public Double2D(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }

        public void fill(double[] datas, int start) {
            datas[start] = x;
            datas[start + 1] = y;
        }
    }

    /**
     * a utility class for wrapping 2D double datas with indes, see also:
     * <ul>
     * <li>{@link #buildDouble2DSearchTree(double[], int, boolean, int[]) }
     * <li>{@link #wrapDouble2D(double[], int, boolean, int[]) }
     * </ul>
     */
    public static class DoubleIndexed2D extends Double2D {

        public int index;

        public DoubleIndexed2D(int index, double x, double y) {
            super(x, y);
            this.index = index;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }
    }

    /**
     * build a 3D ArrayList, the <b>{@code datas}</b> should be like:</br>
     * {x<sub>1</sub>,y<sub>1</sub>,z<sub>1</sub>,x<sub>2</sub>,y<sub>2</sub>,z<sub>2</sub>,...,x<sub>n</sub>,y<sub>n</sub>,z<sub>n</sub>}<br>
     * Normally, used by {@link #buildDouble3DSearchTree(double[], int, boolean, int[]) } to build a layered range tree
     * @param datas {x<sub>1</sub>,y<sub>1</sub>,z<sub>1</sub>,x<sub>2</sub>,y<sub>2</sub>,z<sub>2</sub>,...,x<sub>n</sub>,y<sub>n</sub>,z<sub>n</sub>}
     * @param num the total number of 3D points, {@code (num*3<=datas.length)} should always be <strong> true </strong> 
     * @param needIndex if the indes of points are important, turn this <strong> true </strong> and the tree will hold the indes imformation
     * @param indes <ul><li>if needIndex is <strong>true</strong>, and indes is <strong>null</strong>, the point with dimension <strong>(datas[i*3], datas[i*3+1], datas[i*3+2])</strong> will mark the index <strong>i</strong> 
     * <li> if needIndex is <strong>true</strong> and indes is not <strong>null</strong>, the point with dimension <strong>(datas[i*3], datas[i*3+1], datas[i*3+2])</strong> with mark the index <strong>indes[i]</strong>.
     *      <ul>
     *          <li> in this situation the {@code (indes.length>=num)} should always be <strong> true</strong>
     *      </ul>
     * </ul>
     * @return 
     */
    public static ArrayList<Double3D> wrapDouble3D(double[] datas, int num, boolean needIndex, int[] indes) {
        ArrayList<Double3D> results = new ArrayList<>(num);
        if (needIndex) {
            if (null == indes) {
                for (int i = 0; i < num; i++) {
                    int base = i * 3;
                    results.add(new DoubleIndexed3D(i, datas[base], datas[base + 1], datas[base + 2]));
                }
            } else {
                for (int i = 0; i < num; i++) {
                    int base = i * 3;
                    results.add(new DoubleIndexed3D(indes[i], datas[base], datas[base + 1], datas[base + 2]));
                }
            }
        } else {
            for (int i = 0; i < num; i++) {
                int base = i * 3;
                results.add(new Double3D(datas[base], datas[base + 1], datas[base + 2]));
            }
        }
        return results;
    }

    /**
     * build a 2D ArrayList from double array, the <b>{@code datas}</b> should be like:</br>
     * {x<sub>1</sub>,y<sub>1</sub>,x<sub>2</sub>,y<sub>2</sub>,...,x<sub>n</sub>,y<sub>n</sub>}</br>
     * Normally, used by {@link #buildDouble2DSearchTree(double[], int, boolean, int[]) } to build a layered range tree
     * @param datas {x<sub>1</sub>,y<sub>1</sub>,x<sub>2</sub>,y<sub>2</sub>,...,x<sub>n</sub>,y<sub>n</sub>}
     * @param num the total number of 2D points, {@code (num*2<=datas.length)} should always be <strong> true </strong> 
     * @param needIndex if the indes of points are important, turn this <strong> true </strong> and the tree will hold the indes imformation
     * @param indes <ul><li>if needIndex is <strong>true</strong>, and indes is <strong>null</strong>, the point with dimension <strong>(datas[i*2], datas[i*2+1])</strong> will mark the index <strong>i</strong> 
     * <li> if needIndex is <strong>true</strong> and indes is not <strong>null</strong>, the point with dimension <strong>(datas[i*2], datas[i*2+1])</strong> with mark the index <strong>indes[i]</strong>.
     *      <ul>
     *          <li> in this situation the {@code (indes.length>=num)} should always be <strong> true</strong>
     *      </ul>
     * </ul>
     * @return 
     */
    public static ArrayList<Double2D> wrapDouble2D(double[] datas, int num, boolean needIndex, int[] indes) {
        ArrayList<Double2D> results = new ArrayList<>(num);
        if (needIndex) {
            if (null == indes) {
                for (int i = 0; i < num; i++) {
                    int base = i * 2;
                    results.add(new DoubleIndexed2D(i, datas[base], datas[base + 1]));
                }
            } else {
                for (int i = 0; i < num; i++) {
                    int base = i * 2;
                    results.add(new DoubleIndexed2D(indes[i], datas[base], datas[base + 1]));
                }
            }
        } else {
            for (int i = 0; i < num; i++) {
                int base = i * 2;
                results.add(new Double2D(datas[base], datas[base + 1]));
            }
        }
        return results;
    }

    /**
     * build a 2D layered domain tree from double array, the <b>{@code datas}</b> should be like:</br>
     * {x<sub>1</sub>,y<sub>1</sub>,x<sub>2</sub>,y<sub>2</sub>,...,x<sub>n</sub>,y<sub>n</sub>}
     * @param datas {x<sub>1</sub>,y<sub>1</sub>,x<sub>2</sub>,y<sub>2</sub>,...,x<sub>n</sub>,y<sub>n</sub>}
     * @param num the total number of 2D points, {@code (num*2<=datas.length)} should always be <strong> true </strong> 
     * @param needIndex if the indes of searched out points are important, turn this <strong> true </strong> and the tree will hold the indes imformation
     * @param indes <ul><li>if needIndex is <strong>true</strong>, and indes is <strong>null</strong>, the point with dimension <strong>(datas[i*2], datas[i*2+1])</strong> will mark the index <strong>i</strong> 
     * <li> if needIndex is <strong>true</strong> and indes is not <strong>null</strong>, the point with dimension <strong>(datas[i*2], datas[i*2+1])</strong> with mark the index <strong>indes[i]</strong>.
     *      <ul>
     *          <li> in this situation the {@code (indes.length>=num)} should always be <strong> true</strong>
     *      </ul>
     * </ul>
     * @return 
     */
    public static LayeredRangeTree<Double2D> buildDouble2DSearchTree(double[] datas, int num, boolean needIndex, int[] indes) {
        return new LayeredRangeTree<>(wrapDouble2D(datas, num, needIndex, indes), Double2D.getComparators());
    }

    /**
     * build a 3D layered domain tree from double array, the <b>{@code datas}</b> should be like:</br>
     * {x<sub>1</sub>,y<sub>1</sub>,z<sub>1</sub>,x<sub>2</sub>,y<sub>2</sub>,z<sub>2</sub>,...,x<sub>n</sub>,y<sub>n</sub>,z<sub>n</sub>}
     * @param datas {x<sub>1</sub>,y<sub>1</sub>,z<sub>1</sub>,x<sub>2</sub>,y<sub>2</sub>,z<sub>2</sub>,...,x<sub>n</sub>,y<sub>n</sub>,z<sub>n</sub>}
     * @param num the total number of 3D points, {@code (num*3<=datas.length)} should always be <strong> true </strong> 
     * @param needIndex if the indes of searched out points are important, turn this <strong> true </strong> and the tree will hold the indes imformation
     * @param indes <ul><li>if needIndex is <strong>true</strong>, and indes is <strong>null</strong>, the point with dimension <strong>(datas[i*3], datas[i*3+1], datas[i*3+2])</strong> will mark the index <strong>i</strong> 
     * <li> if needIndex is <strong>true</strong> and indes is not <strong>null</strong>, the point with dimension <strong>(datas[i*3], datas[i*3+1], datas[i*3+2])</strong> with mark the index <strong>indes[i]</strong>.
     *      <ul>
     *          <li> in this situation the {@code (indes.length>=num)} should always be <strong> true</strong>
     *      </ul>
     * </ul>
     * @return 
     */
    public static LayeredRangeTree<Double3D> buildDouble3DSearchTree(double[] datas, int num, boolean needIndex, int[] indes) {
        return new LayeredRangeTree<>(wrapDouble3D(datas, num, needIndex, indes), Double3D.getComparators());
    }

    /**
     * fill the imformation of <strong> in </strong> into duble array <strong> output　</strong> start at the index <strong> start </strong></br>
     * wheh the <strong> indes </strong> is not null, also fills the <strong> indes</strong> start at the index <strong> indesStart </strong>
     * @param in Doubld3D collection
     * @param output {@code (output.lenght>=3*in.size()+start)} must always be <strong> true </strong>;
     * @param start the start index to fill the double array <strong> output</strong>
     * @param indes if <strong> null </strong> will be ignored else, {@code (indes.length>=in.size()+start)} must always be <strong> true </strong> and the var <strong> in </strong> must actually constains {@link DoubleIndexed3D}
     * @param indesStart the start index to fill the int array <strong> indes </strong>
     */
    public static void fillDatas3D(Collection<Double3D> in, double[] output, int start, int[] indes, int indesStart) {
        int switcher = 0;
        if (null != output) {
            switcher++;
        }
        if (null != indes) {
            switcher += 2;
        }
        if (0 == switcher) {
            return;
        }

        switch (switcher) {
            case 1:
                for (Double3D d : in) {
                    d.fill(output, start);
                    start += 3;
                }
                break;
            case 2:
                for (Double3D d : in) {
                    indes[indesStart] = ((DoubleIndexed3D) d).index;
                    indesStart++;
                }
                break;
            case 3:
                for (Double3D d : in) {
                    d.fill(output, start);
                    start += 3;
                    indes[indesStart] = ((DoubleIndexed3D) d).index;
                    indesStart++;
                }
                break;
        }

    }

    /**
     * fill the imformation of <strong> in </strong> into duble array <strong> output　</strong> start at the index <strong> start </strong></br>
     * wheh the <strong> indes </strong> is not null, also fills the <strong> indes</strong> start at the index <strong> indesStart </strong>
     * @param in Doubld2D collection
     * @param output {@code (output.lenght>=2*in.size()+start)} must always be <strong> true </strong>;
     * @param start the start index to fill the double array <strong> output</strong>
     * @param indes if <strong> null </strong> will be ignored else, {@code (indes.length>=in.size()+start)} must always be <strong> true </strong> and the var <strong> in </strong> must actually constains {@link DoubleIndexed2D}
     * @param indesStart the start index to fill the int array <strong> indes </strong>
     */
    public static void fillDatas2D(Collection<Double2D> in, double[] output, int start, int[] indes, int indesStart) {
        int switcher = 0;
        if (null != output) {
            switcher++;
        }
        if (null != indes) {
            switcher += 2;
        }
        if (0 == switcher) {
            return;
        }

        switch (switcher) {
            case 1:
                for (Double2D d : in) {
                    d.fill(output, start);
                    start += 2;
                }
                break;
            case 2:
                for (Double2D d : in) {
                    indes[indesStart] = ((DoubleIndexed2D) d).index;
                    indesStart++;
                }
                break;
            case 3:
                for (Double2D d : in) {
                    d.fill(output, start);
                    start += 2;
                    indes[indesStart] = ((DoubleIndexed2D) d).index;
                    indesStart++;
                }
                break;
        }
    }
    private ArrayList<Comparator<Data>> comparators;
    private Tree rootTree;

    private static final class Node<T> {

        Node(T key) {
            this.key = key;
        }

        private boolean isLeaf() {
            return left == null;
        }
        T key;
        Object associate;
        Node left, right;
    }

    private final class Tree {

        /**
         * 
         * @param datas
         * @param key design as "from"
         * @return the smallest i that datas.get(i)>=0 key at dimension 1(dimension index 0)
         */
        private int dataBinarySearch(ArrayList<Data> datas, Data key) {
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

        private void fractionalCascading(Node father) {
            ArrayList<Data> rightDatas, leftDatas;
            Node<Data> right, left;
            right = father.right;
            left = father.left;
            boolean isLeftLeaf = left.isLeaf();
            boolean isRightLeaf = right.isLeaf();
            int rightDataSize;
            int leftDataSize;

            if (isRightLeaf) {
                rightDataSize = 1;
                rightDatas = null;
            } else {
                rightDatas = (ArrayList<Data>) right.associate;
                rightDataSize = rightDatas.size();
                fractionalCascading(right);
            }

            if (isLeftLeaf) {
                leftDatas = null;
                leftDataSize = 1;
            } else {
                leftDatas = (ArrayList<Data>) left.associate;
                leftDataSize = leftDatas.size();
                fractionalCascading(left);
            }

            int dimIndex = 0;
            FracCasData<Data> fatherCas = new FracCasData((ArrayList<Data>) father.associate);
            father.associate = fatherCas;
            ArrayList<Data> fatherDatas = fatherCas.datas;
            int i, iLeft, iRight;
            for (i = 0, iLeft = 0, iRight = 0; i < fatherDatas.size(); i++) {
                if (iLeft >= leftDataSize) {
                    fatherCas.leftCas[i] = -1;
                } else if (isLeftLeaf) {
                    if (0 == dictCompare(left.key, fatherDatas.get(i), dimIndex)) {
                        fatherCas.leftCas[i] = iLeft++;
                    }
                } else if (0 == dictCompare(leftDatas.get(iLeft), fatherDatas.get(i), dimIndex)) {
                    fatherCas.leftCas[i] = iLeft++;
                } else {
                    fatherCas.leftCas[i] = iLeft;
                }


                if (iRight >= rightDataSize) {
                    fatherCas.rightCas[i] = -1;
                } else if (isRightLeaf) {
                    if (0 == dictCompare(right.key, fatherDatas.get(i), dimIndex)) {
                        fatherCas.rightCas[i] = iRight++;
                    }
                } else if (0 == dictCompare(rightDatas.get(iRight), fatherDatas.get(i), dimIndex)) {
                    fatherCas.rightCas[i] = iRight++;
                } else {
                    fatherCas.rightCas[i] = iRight;
                }
            }
        }

        /**
         * merge two sorted ArrayList at dimension Index dimIndex
         * @param from1
         * @param from2
         * @param dimIndex
         * @return 
         */
        private ArrayList<Data> merge(ArrayList<Data> from1, ArrayList<Data> from2, int dimIndex) {
            ArrayList<Data> merged = new ArrayList<>(from1.size() + from2.size());
            int i, i1, i2, stop;
            for (i = 0, i1 = 0, i2 = 0, stop = from1.size() + from2.size(); i < stop; i++) {
                Data result;
                if (i1 >= from1.size()) {
                    result = from2.get(i2++);
                } else if (i2 >= from2.size()) {
                    result = from1.get(i1++);
                } else if (dictCompare(from1.get(i1), from2.get(i2), dimIndex) < 0) {
                    result = from1.get(i1++);
                } else {
                    result = from2.get(i2++);
                }
                merged.add(result);
            }
            return merged;
        }

        private void build(int dimIndex, ArrayList<Data> datas) {
            //initiate the tree: 
            if (datas.isEmpty()) {
                return;
            }
            if (datas.size() == 1) {
                rootNode = new Node<>(datas.get(0));
                return;
            }

            int treeDeep = (int) ceil(log(2, datas.size())) + 1;
            int fullTreeLeafNum = (int) pow(2, treeDeep - 1);
            int higherLeafNum = fullTreeLeafNum - datas.size();
            LinkedList<Node<Data>> dequeue = new LinkedList<>();
            for (int i = 0; i < higherLeafNum; i++) {
                Node<Data> tNode = new Node<>(datas.get(datas.size() - 1 - i));
                dequeue.addFirst(tNode);
            }
            for (int i = 0; i < datas.size() - higherLeafNum; i++) {
                Node<Data> tNode = new Node<>(datas.get(i));
                dequeue.addLast(tNode);
            }

            ArrayList<Data> tLefts = new ArrayList<>(1);
            ArrayList<Data> tRights = new ArrayList<>(1);
            tLefts.add(null);
            tRights.add(null);
            while (dequeue.size() > 1) {
                Node<Data> right = dequeue.pollLast();
                Node<Data> left = dequeue.pollLast();
                Node<Data> keyNode = left;
                while (!keyNode.isLeaf()) {
                    keyNode = keyNode.right;
                }
                Node<Data> father = new Node<>(keyNode.key);
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
                fractionalCascading(rootNode);
            }
        }

        void checkToAdd(Collection<? super Data> results, Node<Data> node, Data from, Data to, int highestDimIndex) {

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

        /**
         * recusive search at dimension with index dimIndex
         * @param dimIndex
         * @param from
         * @param to
         * @param results 
         */
        void query(int dimIndex, Data from, Data to, Collection<? super Data> results) {
            if (null == rootNode) {
                return;
            }
            if (rootNode.isLeaf()) {
                checkToAdd(results, rootNode, from, to, dimIndex);
            }

//            Comparator comp = comps[dim - 1];

            int compFrom;
            int compTo;
            Node<Data> splitNode = rootNode;
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
                Node<Data> leftSearchNode = splitNode.left;
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

                Node<Data> rightSearchNode = splitNode.right;
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

                FracCasData<Data> splitNodeCasData = (FracCasData<Data>) splitNode.associate;
                int splitBSearchIndex = dataBinarySearch(splitNodeCasData.datas, from);
                if (-1 == splitBSearchIndex) {
                    return;
                }

                int leftSearchCas = splitNodeCasData.leftCas[splitBSearchIndex];
                Node<Data> leftSearchNode = splitNode.left;
                while (!leftSearchNode.isLeaf() && leftSearchCas >= 0) {
                    if (comp.compare(from, leftSearchNode.key) <= 0) {
                        int tRightCasIndex = ((FracCasData<Data>) leftSearchNode.associate).rightCas[leftSearchCas];
                        if (tRightCasIndex >= 0) {

                            if (leftSearchNode.right.isLeaf()) {
                                checkToAdd(results, leftSearchNode.right, from, to, 0);
                            } else {
                                ArrayList<Data> tDatas = ((FracCasData<Data>) leftSearchNode.right.associate).datas;

                                for (int i = tRightCasIndex; i < tDatas.size(); i++) {
                                    if (compDown.compare(tDatas.get(i), to) <= 0) {
                                        results.add(tDatas.get(i));
                                    } else {
                                        break;
                                    }
                                }
                            }
                        }
                        leftSearchCas = ((FracCasData<Data>) leftSearchNode.associate).leftCas[leftSearchCas];
                        leftSearchNode = leftSearchNode.left;

                    } else {
                        leftSearchCas = ((FracCasData<Data>) leftSearchNode.associate).rightCas[leftSearchCas];
                        leftSearchNode = leftSearchNode.right;
                    }
                }

                checkToAdd(results, leftSearchNode, from, to, dimIndex);  //must from dimIndex


                int rightSearchCas = splitNodeCasData.rightCas[splitBSearchIndex];
                Node<Data> rightSearchNode = splitNode.right;
                while (!rightSearchNode.isLeaf() && rightSearchCas >= 0) {
                    if (comp.compare(to, rightSearchNode.key) >= 0) {
                        int tLeftCasIndex = ((FracCasData<Data>) rightSearchNode.associate).leftCas[rightSearchCas];
                        if (tLeftCasIndex >= 0) {
                            if (rightSearchNode.left.isLeaf()) {
                                checkToAdd(results, rightSearchNode.left, from, to, 0);
                            } else {
                                ArrayList<Data> tDatas = ((FracCasData<Data>) rightSearchNode.left.associate).datas;
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
                        rightSearchCas = ((FracCasData<Data>) rightSearchNode.associate).rightCas[rightSearchCas];
                        rightSearchNode = rightSearchNode.right;

                    } else {
                        rightSearchCas = ((FracCasData<Data>) rightSearchNode.associate).leftCas[rightSearchCas];
                        rightSearchNode = rightSearchNode.left;
                    }
                }


                checkToAdd(results, rightSearchNode, from, to, dimIndex); //must from dimIndex;

            }
        }
        Node<Data> rootNode;
    }

    /**
     * Fractional Cascading Data （分散层叠数据）<br>
     * must be referenced by the {@link Node#associate} of dimension 1(dimension index 0) in a builded tree.<br>
     * PS: {@link Node#associate} May reference other two type: {@link Tree} and {@link ArrayList<Data>}
     */
     private static final class FracCasData<T> {

        private FracCasData(ArrayList<T> datas) {
            this.datas = datas;
            leftCas = new int[datas.size()];
            rightCas = new int[datas.size()];
        }
        private ArrayList<T> datas;
        //fractional cascading datas:
        private int[] leftCas;    //leftCase[i] is the smallest one that left.associate.datas[leftCas[i]]>=datas[i], if leftCas[i]>left.associate.data it should be -1
        private int[] rightCas;  //like leftCase
    }

    /**
     * Make a dictionary compare between <b>ob1</b> and <b>ob2</b> with dimension priority sequence(from higher to lower) <b>priDimIndex</b>,n,n-1,<b>priDimIndex+1</b>,<b>priDimIndex-1</b>,...,1</br>
     * @see #getDictComparator(int) 
     * @param ob1
     * @param ob2
     * @param dimIndex the first priority compare dimention
     * @return same as common Comparators
     */
    public int dictCompare(Data ob1, Data ob2, int dimIndex) {
        int c = comparators.get(dimIndex).compare(ob1, ob2);
        if (c != 0) {
            return c;
        }
        for (int i = comparators.size() - 1; i > dimIndex; i--) {
            int res = comparators.get(i).compare(ob1, ob2);
            if (res != 0) {
                return res;
            }
        }

        for (int i = dimIndex - 1; i >= 0; i--) {
            int res = comparators.get(i).compare(ob1, ob2);
            if (res != 0) {
                return res;
            }
        }
        return 0;
    }

    private void buildTree(Collection<Data> datas, List<Comparator<Data>> comps) {

        this.comparators = new ArrayList<>(comps);
        ArrayList<Data> treeDatas = new ArrayList<>(datas);
        int dimensionIndex = comps.size() - 1;
        Comparator comp = getDictComparator(dimensionIndex);

        Collections.sort(treeDatas, comp);
        rootTree = new Tree();
        rootTree.build(dimensionIndex, treeDatas);
    }

    /**
     * Adds the Data in window <b>{@code from}</b>*<b>{@code to}</b> into <b>{@code results}</b></br>
     * <ul> Note!<li>  <b>{@code results}</b> will <strong>not</strong> be cleared in this method</li>
     * <li>in every dimension  <b>{@code from}</b> must <strong> not be bigger </strong> than  <b>{@code to}</b>
     * </li>
     * </ul>
     * @param results
     * @param from
     * @param to 
     */
    public void search(Collection<? super Data> results, Data from, Data to) {
        results.clear();
        rootTree.query(comparators.size() - 1, from, to, results);
    }
}
