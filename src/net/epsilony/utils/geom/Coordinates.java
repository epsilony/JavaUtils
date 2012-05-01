/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.utils.geom;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author epsilon
 */
public class Coordinates {

    public static class CompX<T extends Coordinate> implements Comparator<T> {

        @Override
        public int compare(T c0, T c1) {
            double x1 = c0.x;
            double x2 = c1.x;
            if (x1 == x2) {
                return 0;
            } else if (x1 > x2) {
                return 1;
            } else {
                return -1;
            }
        }
    }

    public static class CompY<T extends Coordinate> implements Comparator<T> {

        @Override
        public int compare(T c0, T c1) {
            double y1 = c0.y;
            double y2 = c1.y;
            if (y1 == y2) {
                return 0;
            } else if (y1 > y2) {
                return 1;
            } else {
                return -1;
            }
        }
    }

    public static class CompZ <T extends Coordinate> implements Comparator<T> {

        @Override
        public int compare(T c0, T c1) {
            double z1 = c0.z;
            double z2 = c1.z;
            if (z1 == z2) {
                return 0;
            } else if (z1 > z2) {
                return 1;
            } else {
                return -1;
            }
        }
    }

    public static <T extends Coordinate> List<Comparator<T>> genComparators(int[] dims,T t) {
        ArrayList<Comparator<T>> results = new ArrayList(dims.length);
        for (int i = 0; i < dims.length; i++) {
            int dim = dims[i];
            switch (dim) {
                case 0:
                    results.add(new CompX<T>());
                    break;
                case 1:
                    results.add(new CompY<T>());
                    break;
                case 2:
                    results.add(new CompZ<T>());
                    break;
                default:
                    throw new IndexOutOfBoundsException();
            }

        }
        return results;
    }
    
    public static <T extends Coordinate> List<Comparator<T>> genComparators(int dim,T t) {
        int[] dims=new int[dim];
        for (int i = 0; i < dims.length; i++) {
            dims[i]=i;
        }
        return genComparators(dims,t);
    }
    
    public static <T extends Coordinate> CenterDistanceComparator<T> normalCenterDistanceComparator(){
        return new CenterDistanceComparator<T>() {
            Coordinate c;
            @Override
            public void setCenter(Coordinate center) {
                c=center;
            }

            @Override
            public int compare(T c1, T c2) {
                double distSq1=GeometryMath.distanceSquare(c1, c);
                double distSq2=GeometryMath.distanceSquare(c2, c);
                if(distSq1<distSq2){
                    return -1;
                }else if(distSq1==distSq2){
                    return 0;
                }else{
                    return 1;
                }
            }
        };
    }
    
        public static <T extends Coordinate> CenterDistanceComparator<T> inverseCenterDistanceComparator(){
        return new CenterDistanceComparator<T>() {
            Coordinate c;
            @Override
            public void setCenter(Coordinate center) {
                c=center;
            }

            @Override
            public int compare(T c1, T c2) {
                double distSq1=GeometryMath.distanceSquare(c1, c);
                double distSq2=GeometryMath.distanceSquare(c2, c);
                if(distSq1<distSq2){
                    return 1;
                }else if(distSq1==distSq2){
                    return 0;
                }else{
                    return -1;
                }
            }
        };
    }
}
