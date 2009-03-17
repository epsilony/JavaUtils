/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.util.collection;

import java.util.ArrayList;
import java.util.Comparator;

/**
 *
 * @author epsilon
 */
public class CollectionUtils {

    public static <T> int ceil(ArrayList<T> list, T key, Comparator<? super T> c) {
        if (c.compare(list.get(0), key) >= 0) {
            return 0;
        }
        if (c.compare(list.get(list.size() - 1), key) < 0) {
            return -1;
        }
        int start = 0, end = list.size() - 1, res;
        T mid;
        while (start < end - 1) {
            mid = list.get((start + end) / 2);
            res = c.compare(mid, key);
//            System.out.println("res = " + res);
//            System.out.println("mid = " + mid);
            if (res >= 0) {
                end = (start + end) / 2;
            } else {
                start = (start + end) / 2;
            }
//            System.out.println("start = " + start);
//            System.out.println("end = "+ end);
        }
        res = c.compare(list.get(start), key);
        if (res < 0) {
            return end;
        } else {
            if (res == 0) {
                return start;
            } else {
                return -1;
            }
        }

    }

    public static <T> int floor(ArrayList<T> list, T key, Comparator<? super T> c) {
        if (c.compare(list.get(0), key) > 0) {
            return -1;
        }
        if (c.compare(list.get(list.size() - 1), key) <= 0) {
            return list.size()-1;
        }
        int start = 0, end = list.size() - 1, res;
        T mid;
        while (start < end - 1) {
            mid = list.get((start + end) / 2);
            res = c.compare(mid, key);
//            System.out.println("res = " + res);
//            System.out.println("mid = " + mid);
            if (res > 0) {
                end = (start + end) / 2;
            } else {
                start = (start + end) / 2;
            }
//            System.out.println("start = " + start);
//            System.out.println("end = "+ end);
        }
        res = c.compare(list.get(end), key);
        if (res > 0) {
            return start;
        } else {
            if (res == 0) {
                return end;
            } else {
                return -1;
            }
        }


    }

    public static void main(String[] args) {
        CollectionUtils u = new CollectionUtils();
        Comparator<Integer> comp = new Comparator<Integer>() {

            @Override
            public int compare(Integer o1, Integer o2) {

                return o1 - o2;
            }
        };
        for (int i = 10; i <
                20; i++) {
            ArrayList<Integer> list = new ArrayList<Integer>(i);
            for (int j = 0; j <
                    i - 9; j++) {
                list.add(1);
            }

            for (int j = i - 9; j <
                    i - 3; j++) {
                list.add(3);
            }

            for (int j = i - 3; j <
                    i; j++) {
                list.add(4);
            }

            System.out.println(list);
           // System.out.println(ceil(list, new Integer(4), comp));
        System.out.println(floor(list, new Integer(3), comp));
        }
    }
}
