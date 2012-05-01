/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author epsilonyuan@gmail.com
 */
    public class IndexWrapper<T> {

        public T data;
        public int index;

        public T getData() {
            return data;
        }

        public int getIndex() {
            return index;
        }

        public IndexWrapper(T data, int index) {
            this.data = data;
            this.index = index;
        }
        
        private static class IndexWrapperComparator<S> implements Comparator<IndexWrapper<S>>{
            Comparator<S> toWrap;

            public IndexWrapperComparator(Comparator<S> toWrap) {
                this.toWrap = toWrap;
            }
            @Override
            public int compare(IndexWrapper<S> o1, IndexWrapper<S> o2) {
                return toWrap.compare(o1.data, o2.data);
            }
            
        }

        /**
         * wrap Type lack of index, it can ease the use of {@link LayeredRangeTree} 
         * @param <S>
         * @param inDatas
         * @param outDatas wrapped objects
         * @param indes if null wrap the objects with 0,1,... else wrap with index indes[0],indes[1],...
         * @param inComps
         * @param outComps wrapped Comparators
         */
        public static <S> void wrapWithIndex(Collection<S> inDatas, ArrayList<IndexWrapper<S>> outDatas, int[] indes, List<Comparator<S>> inComps, ArrayList<Comparator<IndexWrapper<S>>> outComps) {
            outDatas.ensureCapacity(inDatas.size());
            outDatas.clear();
            if (null == indes) {
                int i = 0;
                for (S s : inDatas) {
                    outDatas.add(new IndexWrapper(s, i++));
                }
            } else {
                int i = 0;
                for (S s : inDatas) {
                    outDatas.add(new IndexWrapper(s, indes[i++]));
                }
            }

            outComps.ensureCapacity(inComps.size());
            outComps.clear();
            for (Comparator<S> s : inComps) {
                outComps.add(new IndexWrapperComparator<>(s));
            }
        }
        
    }
