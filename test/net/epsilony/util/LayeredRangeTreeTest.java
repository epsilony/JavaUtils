/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Arrays;
import java.util.Random;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author epsilon
 */
public class LayeredRangeTreeTest {

    public LayeredRangeTreeTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @After
    public void tearDown() {
    }

    static class Data2D implements Serializable {

        int x;
        int y;

        @Override
        public String toString() {
            return ("[" + x + "," + y + "]");
        }
    }

    @Test
    public void testSearch2D() throws FileNotFoundException, IOException {

        Random random = new Random();

        Comparator<Data2D> compX = new Comparator<Data2D>() {

            @Override
            public int compare(Data2D o1, Data2D o2) {
                return o1.x - o2.x;
            }
        };
        Comparator<Data2D> compY = new Comparator<Data2D>() {

            @Override
            public int compare(Data2D o1, Data2D o2) {
                return o1.y - o2.y;
            }
        };

        final List<Comparator<Data2D>> comps = Arrays.asList(compX, compY);

        int testSize = 10000;
        int randomRange = 100;
        int testNum = 100000;
        int testSearchRange = 10;
        long start, end, time1, time2, time3, time4;
        time1 = 0;
        time2 = 0;
        time3 = time4 = 0;
        Data2D[] datas = new Data2D[testSize];
        for (int i = 0; i < testSize; i++) {
            datas[i] = new Data2D();
            datas[i].x = random.nextInt(randomRange);
            datas[i].y = random.nextInt(randomRange);
        }
        Comparator<Data2D> dictComparator = new Comparator<Data2D>() {

            @Override
            public int compare(Data2D o1, Data2D o2) {
                int c = (comps.get(1)).compare(o1, o2);
                if (c == 0) {
                    return ((Comparator<Data2D>) comps.get(0)).compare(o1, o2);
                }
                return c;
            }
        };
        Arrays.sort(datas, dictComparator);

        ArrayList<Data2D> arrayList = new ArrayList<>(datas.length);
        for (int i = 0; i < datas.length - 1; i++) {
            if (0 == dictComparator.compare(datas[i], datas[i + 1])) {
                continue;
            }
            arrayList.add(datas[i]);
        }
        arrayList.add(datas[datas.length - 1]);
        datas = arrayList.toArray(new Data2D[0]);

        for (int i = 0; i < datas.length; i++) {
            int change = random.nextInt(datas.length);
            Data2D t = datas[i];
            datas[i] = datas[change];
            datas[change] = t;
        }

        LayeredRangeTree<Data2D> layeredRangeTree = new LayeredRangeTree<>();
        start = System.nanoTime();
        layeredRangeTree.buildTree(Arrays.asList(datas), comps);
        end = System.nanoTime();
        time1 += end - start;

        for (int i = 0; i < testNum; i++) {
            Data2D from = new Data2D();
            Data2D to = new Data2D();
            int centerX = random.nextInt(randomRange);
            int centerY = random.nextInt(randomRange);
            from.x = centerX - random.nextInt(testSearchRange);
            from.y = centerY - random.nextInt(testSearchRange);
            to.x = centerX + random.nextInt(testSearchRange);
            to.y = centerY + random.nextInt(testSearchRange);

//            from.x = datas[datas.length / 2].x;
//            if (to.x < from.x) {
//                int t = from.x;
//                from.x = from.y;
//                from.y = t;
//            }


            ArrayList<Data2D> results = new ArrayList<>();
            start = System.nanoTime();
            layeredRangeTree.search(results, from, to);
            end = System.nanoTime();
            time2 += end - start;

            Data2D[] resultsArray = results.toArray(new Data2D[results.size()]);
            Arrays.sort(resultsArray, dictComparator);

            ArrayList<Data2D> resultsCompare = new ArrayList<>(resultsArray.length);

            start = System.nanoTime();
            Arrays.sort(datas, dictComparator);
            end = System.nanoTime();
            time3 += end - start;

            start = System.nanoTime();
            for (int j = 0; j < datas.length; j++) {
                int k;
                for (k = 0; k < comps.size(); k++) {
                    int compFrom = comps.get(k).compare(from, datas[j]);
                    int compTo = comps.get(k).compare(to, datas[j]);
                    if (compFrom <= 0 && compTo >= 0) {
                        continue;
                    }
                    break;
                }
                if (k >= comps.size()) {
                    resultsCompare.add(datas[j]);
                }
            }
            end = System.nanoTime();
            time4 += end - start;
            Data2D[] resultsCompareArray = resultsCompare.toArray(new Data2D[resultsCompare.size()]);
            Arrays.sort(resultsCompareArray, dictComparator);
            Arrays.sort(resultsArray, dictComparator);
//            System.out.println("from:" + from);
//            System.out.println("to:" + to);
//
//            System.out.println("results.size()=" + results.size());
//            System.out.println(Arrays.toString(resultsArray));
//            System.out.println("exp size=" + resultsCompareArray.length);
//            System.out.println(Arrays.toString(resultsCompareArray));
            assertArrayEquals(resultsCompareArray, resultsArray);

            //for debugs:
//            if (!Arrays.equals(resultsArray, resultsCompareArray)) {
//                System.out.println("error datas:");
//                System.out.println(Arrays.toString(datas));
//                //layeredRangeTree.search(results, from, to);
//                String outStr = new SimpleDateFormat().format(new Date());
//                ObjectOutputStream out = null;
//                try {
//                    out = new ObjectOutputStream(new FileOutputStream("/home/epsilon/desktop/output" + outStr));
//                    out.writeObject(datas);
//                } finally {
//                    //out.writeObject(layeredRangeTree);
//                    if (null != out) {
//                        out.close();
//                    }
//                }
//            }
//layeredRangeTree.search(results, from, to);

            if(i%(testNum/100)==0){
                System.out.println(i);
            }

        }
        System.out.println("2D time");
        System.out.println("layer range tree build time:" + time1 / testNum);
        System.out.println("layer range tree search time" + time2 / testNum);
        System.out.println("array sort time" + time3 / testNum);
        System.out.println("array search time" + time4 / testNum);
        System.out.println("");
    }

    static class Data3D {

        int x, y, z;

        @Override
        public String toString() {
            return "[" + x + "," + y + "," + z + "]";
        }
    }

    @Test
    public void testSearch3D() throws FileNotFoundException, IOException {

        Random random = new Random();

        Comparator<Data3D> compX = new Comparator<Data3D>() {

            @Override
            public int compare(Data3D o1, Data3D o2) {
                return o1.x - o2.x;
            }
        };
        Comparator<Data3D> compY = new Comparator<Data3D>() {

            @Override
            public int compare(Data3D o1, Data3D o2) {
                return o1.y - o2.y;
            }
        };

        Comparator<Data3D> compZ = new Comparator<Data3D>() {

            @Override
            public int compare(Data3D o1, Data3D o2) {
                return o1.z - o2.z;
            }
        };

        final List<Comparator<Data3D>> comps = Arrays.asList(compX, compY, compZ);

        int testSize = 10000;
        int randomRange = 100;
        int testNum = 100000;
        int testSearchRange = 10;
        Data3D[] datas = new Data3D[testSize];
        for (int i = 0; i < testSize; i++) {
            datas[i] = new Data3D();
            datas[i].x = random.nextInt(randomRange);
            datas[i].y = random.nextInt(randomRange);
            datas[i].z = random.nextInt(randomRange);
        }
        Comparator<Data3D> dictComparator = new Comparator<Data3D>() {

            @Override
            public int compare(Data3D o1, Data3D o2) {
                int c = (comps.get(1)).compare(o1, o2);
                if (c == 0) {
                    return ((Comparator<Data3D>) comps.get(0)).compare(o1, o2);
                }
                return c;
            }
        };
        Arrays.sort(datas, dictComparator);

        ArrayList<Data3D> arrayList = new ArrayList<>(datas.length);
        for (int i = 0; i < datas.length - 1; i++) {
            if (0 == dictComparator.compare(datas[i], datas[i + 1])) {
                continue;
            }
            arrayList.add(datas[i]);
        }
        arrayList.add(datas[datas.length - 1]);
        datas = arrayList.toArray(new Data3D[0]);

        for (int i = 0; i < datas.length; i++) {
            int change = random.nextInt(datas.length);
            Data3D t = datas[i];
            datas[i] = datas[change];
            datas[change] = t;
        }

        long time1, time2, time3, time4, start, end;
        time1 = time2 = time3 = time4 = 0;

        LayeredRangeTree<Data3D> layeredRangeTree = new LayeredRangeTree<>();

        start = System.nanoTime();
        layeredRangeTree.buildTree(Arrays.asList(datas), comps);
        end = System.nanoTime();
        time1 += end - start;

        for (int i = 0; i < testNum; i++) {
            Data3D from = new Data3D();
            Data3D to = new Data3D();
            int centerX = random.nextInt(randomRange);
            int centerY = random.nextInt(randomRange);
            int centerZ = random.nextInt(randomRange);
            from.x = centerX - random.nextInt(testSearchRange);
            from.y = centerY - random.nextInt(testSearchRange);
            from.z = centerZ - random.nextInt(testSearchRange);
            to.z = centerZ + random.nextInt(testSearchRange);
            to.x = centerX + random.nextInt(testSearchRange);
            to.y = centerY + random.nextInt(testSearchRange);

            from.y = to.y;
            from.z = to.z;


//            from.x = datas[datas.length / 2].x;
//            if (to.x < from.x) {
//                int t = from.x;
//                from.x = from.y;
//                from.y = t;
//            }


            ArrayList<Data3D> results = new ArrayList<>();
            start = System.nanoTime();
            layeredRangeTree.search(results, from, to);
            end = System.nanoTime();
            time2 += end - start;


            Data3D[] resultsArray = results.toArray(new Data3D[results.size()]);
            Arrays.sort(resultsArray, dictComparator);


            ArrayList<Data3D> resultsCompare = new ArrayList<>(resultsArray.length);
            start = System.nanoTime();    
            Arrays.sort(datas, dictComparator);
            end = System.nanoTime();
            time3 += end - start;

            start = System.nanoTime();
            for (int j = 0; j < datas.length; j++) {
                int k;
                for (k = 0; k < comps.size(); k++) {
                    int compFrom = comps.get(k).compare(from, datas[j]);
                    int compTo = comps.get(k).compare(to, datas[j]);
                    if (compFrom <= 0 && compTo >= 0) {
                        continue;
                    }
                    break;
                }
                if (k >= comps.size()) {
                    resultsCompare.add(datas[j]);
                }
            }
            end = System.nanoTime();
            time4 += end - start;

            Data3D[] resultsCompareArray = resultsCompare.toArray(new Data3D[resultsCompare.size()]);
            Arrays.sort(resultsCompareArray, dictComparator);
//            Arrays.sort(resultsArray, dictComparator);
//            System.out.println("from:" + from);
//            System.out.println("to:" + to);
//            System.out.println("results.size()=" + results.size());
//            System.out.println(Arrays.toString(resultsArray));
//            System.out.println("exp size=" + resultsCompareArray.length);
//            System.out.println(Arrays.toString(resultsCompareArray));
            assertArrayEquals(resultsCompareArray, resultsArray);

            //for debugs:
//            if (!Arrays.equals(resultsArray, resultsCompareArray)) {
//                System.out.println("error datas:");
//                System.out.println(Arrays.toString(datas));
//                layeredRangeTree.search(results, from, to);
//                String outStr = new SimpleDateFormat().format(new Date());
//                ObjectOutputStream out = null;
//                try {
//                    out = new ObjectOutputStream(new FileOutputStream("/home/epsilon/desktop/output" + outStr));
//                    out.writeObject(datas);
//                } finally {
//                    //out.writeObject(layeredRangeTree);
//                    if (null != out) {
//                        out.close();
//                    }
//                }
//            }



            if(i%(testNum/100)==0){
                System.out.println(i);
            }
        }
        System.out.println("2D time");
        System.out.println("layer range tree build time:" + time1 / testNum);
        System.out.println("layer range tree search time" + time2 / testNum);
        System.out.println("array sort time" + time3 / testNum);
        System.out.println("array search time" + time4 / testNum);
        System.out.println("");

    }
}
