package com.us.acm;

/**
 * 排序算法
 * Created by yangyibo on 8/22/17.
 * <p>
 * 排序算法大体可分为两种：
 * 一种是比较排序，时间复杂度O(nlogn) ~ O(n^2)，主要有：冒泡排序，选择排序，插入排序，归并排序，堆排序，快速排序等。
 * 另一种是非比较排序，时间复杂度可以达到O(n)，主要有：计数排序，基数排序，桶排序等。
 */
public class SortTest {

    public static void main(String[] args) {
        init();
        print(insertionSort(init()));
    }

    private static void print(int[] arrays) {
        for (int i : arrays) {
            System.out.print(i + " ");
        }
    }

    private static int[] init() {
        int[] arraytoSort = {2, 68, 34, 98, 7, 37, 5, 8, 3, 10, 1, 33, 76, 23, 94, 31, 67, 97, 35};
        return arraytoSort;
    }

    private static int[] insertionSort(int[] arraytoSort) {
        int length = arraytoSort.length;
        int insertNum; //要插入的数
        for (int i =1 ; i<length;i++){ // 排序多少次，第一个数不用排序
            insertNum = arraytoSort[i];
            int j = i-1;
            while (j >= 0 && arraytoSort[j] < insertNum){
                arraytoSort[j+1] = insertNum;
                j--;
            }
        }
        return null;
    }

    /**
     * 直接插入排序
     * <p>
     * 需求； 将第一个数和第二个数排序，然后构成一个有序序列
     * 将第三个数插入进去，构成一个新的有序序列。
     * 对第四个数、第五个数……直到最后一个数，重复第二步。
     * <p>
     * 做法： 将一个记录插入到已排序好的有序表中，从而得到一个新，记录数增1的有序表。
     * 即：先将序列的第1个记录看成是一个有序的子序列，然后从第2个记录逐个进行插入，直至整个序列有序为止。
     * 要点：设立哨兵，作为临时存储和判断数组边界之用
     *
     * @param arraytoSort
     */
    private static int[] insertionSort2(int[] arraytoSort) {
        int length = arraytoSort.length;//数组长度，将这个提取出来是为了提高速度。
        int insertNum;//要插入的数
        for (int i = 1; i < length; i++) { //插入的次数
            insertNum = arraytoSort[i]; //要插入的数
            int j = i - 1; //已经排序好的序列元素个数
            while (j >= 0 &&
                    arraytoSort[j] > insertNum) {
                arraytoSort[j + 1] = arraytoSort[j];//元素移动一格
                j--;
            }
            arraytoSort[j + 1] = insertNum;//将需要插入的数放在要插入的位置。
        }
        return arraytoSort;
    }
}
