/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.util;

import java.util.AbstractSequentialList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 *
 * @author epsilon
 */
public class OrderedLinkedList<E> extends AbstractSequentialList<E> {

    LinkedList<E> list = new LinkedList<E>();
    Comparator<E> comparator;

    public OrderedLinkedList(Comparator<E> comparator) {
        this.comparator = comparator;
    }

    @Override
    public ListIterator<E> listIterator(final int arg0) {
        ListIterator<E> listIterator = new ListIteratorImpl(arg0);
        return listIterator;
    }

    @Override
    public int size() {
        return list.size();
    }

    
    

    private class ListIteratorImpl implements ListIterator<E> {

        private final int arg0;

        public ListIteratorImpl(int arg0) {
            this.arg0 = arg0;
            lIterator = list.listIterator(arg0);
        }
        ListIterator<E> lIterator = null;

        public boolean hasNext() {
            return lIterator.hasNext();
        }

        public boolean hasPrevious() {
            return lIterator.hasPrevious();
        }

        public int nextIndex() {
            return lIterator.nextIndex();
        }

        public int previousIndex() {
            return lIterator.previousIndex();
        }

        public void remove() {
            lIterator.remove();
        }

        public void set(E arg0) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void add(E arg0) {
            E t;
            boolean hasAdded=false;
            ListIterator<E> iterator=list.listIterator();
            while (iterator.hasNext()) {
                t = iterator.next();
                if (comparator.compare(arg0,t) > 0) {
                    iterator.set(arg0);
                    iterator.add(t);
                    hasAdded=true;
                    break;
                }
            }
            if(!hasAdded)
            {
                iterator.add(arg0);
            }
        }

        public E next() {
            return lIterator.next();
        }

        public E previous() {
            return lIterator.previous();
        }
    }
    public static void main(String args[]){
        Comparator <Double> comp=new Comparator<Double>() {

            public int compare(Double arg0, Double arg1) {
                return Double.compare(arg0.doubleValue(), arg1.doubleValue());
            }
        };
        OrderedLinkedList<Double> list=new OrderedLinkedList<Double>(comp);
        double[] ds={6,9,1,2,8,3,7,4,6,5};
        for(int i=0;i<ds.length;i++){
            list.add(new Double(ds[i]));
        }
        Double [] dss=new Double[0];
        System.out.println(Arrays.toString(list.toArray(dss)));
        for(int i=0;i<list.size();i++){
            System.out.print(" "+list.get(i).toString());
        }
    }
}
