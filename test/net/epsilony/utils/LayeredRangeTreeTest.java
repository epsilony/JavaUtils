/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Arrays;
import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author epsilonyuan@gmail.com
 */
public class LayeredRangeTreeTest {
    boolean isHotspot=false;
    boolean isTest=true;

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

    List<Comparator<Data2D>> getComparators2D() {
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

        return Arrays.asList(compX, compY);
    }

    Comparator<Data2D> getDictComparator2D() {
        return new Comparator<Data2D>() {

            List<Comparator<Data2D>> comps = getComparators2D();

            @Override
            public int compare(Data2D o1, Data2D o2) {
                int c = (comps.get(1)).compare(o1, o2);
                if (c == 0) {
                    return ((Comparator<Data2D>) comps.get(0)).compare(o1, o2);
                }
                return c;
            }
        };
    }

    /**
     * get sorted no 
     * @param testSize
     * @param randomRange
     * @return 
     */
    ArrayList<Data2D> getRandomSample2D(int testSize, int randomRange) {
        Random random = new Random();

        ArrayList<Data2D> arrayList = new ArrayList<>(testSize);
        for (int i = 0; i < testSize; i++) {
            Data2D t = new Data2D();
            t.x = random.nextInt(randomRange);
            t.y = random.nextInt(randomRange);
            arrayList.add(t);
        }
        Comparator<Data2D> dictComparator = getDictComparator2D();
        Collections.sort(arrayList, dictComparator);

        ArrayList<Data2D> results = new ArrayList<>(arrayList.size());
        results.add(arrayList.get(0));
        for (int i = 1; i < arrayList.size(); i++) {
            if (0 == dictComparator.compare(arrayList.get(i), arrayList.get(i - 1))) {
                continue;
            }
            results.add(arrayList.get(i));
        }

        return results;
    }

    static <T> void wash(ArrayList<T> sorted) {
        Random rand = new Random();
        for (int i = 0, stop = (int) (sorted.size() * 1.1); i < stop; i++) {
            int index1 = rand.nextInt(sorted.size());
            int index2 = rand.nextInt(sorted.size());
            T t = sorted.get(index1);
            sorted.set(index1, sorted.get(index2));
            sorted.set(index2, t);
        }
    }

    @Test
    public void testHotSpot2D() throws FileNotFoundException, IOException {

        if(!isHotspot){
            return;
        }

        final List<Comparator<Data2D>> comps = getComparators2D();

        int testSize = 1000000;
        int randomRange = 1000;
        int testNum = 1000;
        int testSearchRange = 10;
        long start, end, time1, time2, time3, time4;
        time1 = 0;
        time2 = 0;
        time3 = time4 = 0;

        ArrayList<Data2D> sampleList = getRandomSample2D(testSize, randomRange);
        
        wash(sampleList);


        start = System.nanoTime();
        LayeredRangeTree<Data2D> layeredRangeTree = new LayeredRangeTree<>(sampleList, comps);
        end = System.nanoTime();
        time1 += end - start;

        Comparator<Data2D> dictComparator = getDictComparator2D();



        for (int i = 0; i < testNum; i++) {
            Data2D from = new Data2D();
            Data2D to = new Data2D();
            Random random = new Random();
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

            if (i % (testNum / 100) == 0) {
                System.out.println(i);
            }

        }
        System.out.println("2D time");
        System.out.println("layer range tree build time:" + time1);
        System.out.println("layer range tree search time" + time2 / testNum);
        System.out.println("array sort time" + time3 / testNum);
        System.out.println("array search time" + time4 / testNum);
        System.out.println("");
    }

    @Test
    public void testSearch2D() throws FileNotFoundException, IOException {
        if(!isTest){
            return;
        }

        int testSize = 10000;
        int randomRange = 100;
        int testNum = 1000;
        int testSearchRange = 10;

        final List<Comparator<Data2D>> comps = getComparators2D();


        long start, end, time1, time2, time3, time4;
        time1 = 0;
        time2 = 0;
        time3 = time4 = 0;

        ArrayList<Data2D> sampleList = getRandomSample2D(testSize, randomRange);
        System.out.println("sampleList" + sampleList);
        wash(sampleList);


        start = System.nanoTime();
        LayeredRangeTree<Data2D> layeredRangeTree = new LayeredRangeTree<>(sampleList, comps);
        end = System.nanoTime();
        time1 += end - start;

        Comparator<Data2D> dictComparator = getDictComparator2D();
        Data2D[] datas = sampleList.toArray(new Data2D[sampleList.size()]);
        start = System.nanoTime();
        Arrays.sort(datas, dictComparator);
        end = System.nanoTime();
        time3 += end - start;


        for (int i = 0; i < testNum; i++) {
            Data2D from = new Data2D();
            Data2D to = new Data2D();
            Random random = new Random();
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
            Collections.sort(results, dictComparator);
            assertArrayEquals(results.toArray(), resultsCompare.toArray());
//            System.out.println("from:" + from);
//            System.out.println("to:" + to);
//
//            System.out.println("results.size()=" + results.size());
//            System.out.println(Arrays.toString(resultsArray));
//            System.out.println("exp size=" + resultsCompareArray.length);
//            System.out.println(Arrays.toString(resultsCompareArray));



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

            if (i % (testNum / 100) == 0) {
                System.out.println(i);
            }

        }
        System.out.println("2D time");
        System.out.println("layer range tree build time:" + time1);
        System.out.println("layer range tree search time" + time2 / testNum);
        System.out.println("array sort time" + time3);
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
    public void testHotspot3D() throws FileNotFoundException, IOException {

        if(!isHotspot){
            return;
        }

        int testSize = 10000;
        int randomRange = 100;
        int testNum = 100000;
        int testSearchRange = 10;

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


        start = System.nanoTime();
        LayeredRangeTree<Data3D> layeredRangeTree = new LayeredRangeTree<>(Arrays.asList(datas), comps);
        end = System.nanoTime();
        time1 += end - start;
        Data3D from = new Data3D();
        Data3D to = new Data3D();
        for (int i = 0; i < testNum; i++) {

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

            ArrayList<Data3D> results = new ArrayList<>();
            start = System.nanoTime();
            layeredRangeTree.search(results, from, to);
            end = System.nanoTime();
            time2 += end - start;

        }
        System.out.println("3D time");
        System.out.println("layer range tree build time:" + time1);
        System.out.println("layer range tree search time" + time2 / testNum);
        System.out.println("");
    }

    @Test
    public void testSearch3D() throws FileNotFoundException, IOException {
        if(!isTest){
            return;
        }
        int testSize = 10000;
        int randomRange = 100;
        int testNum = 100000;
        int testSearchRange = 10;
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


        start = System.nanoTime();
        LayeredRangeTree<Data3D> layeredRangeTree = new LayeredRangeTree<>(Arrays.asList(datas), comps);
        end = System.nanoTime();
        time1 += end - start;

        start = System.nanoTime();
        Arrays.sort(datas, dictComparator);
        end = System.nanoTime();
        time3 += end - start;


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

            ArrayList<Data3D> results = new ArrayList<>();
            start = System.nanoTime();
            layeredRangeTree.search(results, from, to);
            end = System.nanoTime();
            time2 += end - start;


            Data3D[] resultsArray = results.toArray(new Data3D[results.size()]);
            Arrays.sort(resultsArray, dictComparator);


            ArrayList<Data3D> resultsCompare = new ArrayList<>(resultsArray.length);

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



            if (i % (testNum / 100) == 0) {
                System.out.println(i);
            }
        }
        System.out.println("3D time");
        System.out.println("layer range tree build time:" + time1);
        System.out.println("layer range tree search time" + time2 / testNum);
        System.out.println("array sort time" + time3);
        System.out.println("array search time" + time4 / testNum);
        System.out.println("");
    }
}
