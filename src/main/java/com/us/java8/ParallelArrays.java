package com.us.java8;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by yangyibo on 16/12/27.
 *
 * 并行和非并行的耗时统计。
 */
public class ParallelArrays {

    public static void main(String[] args) {

        parallel();
    }

    //并行（parallel）数组
    private static void parallel(){
        Integer[] arrayOfLong = new Integer[50000];

        Arrays.parallelSetAll(arrayOfLong,
                index -> ThreadLocalRandom.current().nextInt(1000000));
        Arrays.stream(arrayOfLong).limit(10).forEach(
                i -> System.out.print(i + " "));

        System.out.println();

        System.out.println("\n" + "-begin----" + LocalDateTime.now() + "\n");

        //0.1 -0.2 S
//        Arrays.parallelSort(arrayOfLong);
//        Arrays.stream(arrayOfLong).limit(10).forEach(
//                i -> System.out.print(i+" "));

        //0.2-0.3 S
        Arrays.stream(arrayOfLong).sorted((l1,l2)->l1.compareTo(l2)).limit(10).forEach(e->System.out.print(e+" "));
        System.out.println("\n\n-End----" + LocalDateTime.now());

    }


}
