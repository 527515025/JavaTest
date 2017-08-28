package com.us.acm;

/**
 * 排序算法
 * Created by yangyibo on 8/22/17.
 * <p>
 * <p>
 * 排序算法大体可分为两种：
 * 一种是比较排序，时间复杂度O(nlogn) ~ O(n^2)，主要有：冒泡排序，选择排序，插入排序，归并排序，堆排序，快速排序等。
 * 另一种是非比较排序，时间复杂度可以达到O(n)，主要有：计数排序，基数排序，桶排序等。
 * <p>
 * <p>
 * 有两种简单排序算法分别是插入排序和选择排序，
 * 两个都是数据量小时效率高。实际中插入排序一般快于选择排序，由于更少的比较和在有差不多有序的集合表现更好的性能。但是选择排序用到更少的写操作，所以当写操作是一个限制因素时它被使用到。
 */
public class SortTest {

    public static void main(String[] args) {
        init();
//        print(insertionSort(init()));
//        print(shellSort(init()));
        print(simpleSelectSort(init()));
    }

    private static void print(int[] arrays) {
        for (int i : arrays) {
            System.out.print(i + " ");
        }
    }

    private static int[] init() {
        int[] arraytoSort = {2, 68, 34, 98, 7, 37, 5, 8, 3, 10, 1, 33, 76, 23, 94, 31, 67, 97, 35};
//        int[] arraytoSort = {2, 68, 34, 98, 7, 37, 5, 8, 3, 10};
        return arraytoSort;
    }

    /**
     * 直接插入排序
     * <p>
     * <p>
     * 需求；
     * 将一个记录插入到已排序好的有序表中，从而得到一个新，记录数增1的有序表。
     * 即：先将序列的第1个记录看成是一个有序的子序列，
     * 然后从第2个记录逐个进行插入，直至整个序列有序为止。
     * <p>
     * <p>
     * 做法：
     * 获取有序队列的最后一个数 j ，和当前需要插入的数 insertNum
     * 从后遍历有序列表的数据 ，与insertNum 比较，如果大于 insertNum ；就将有序序列 遍历到的当前位 j 往后数据统一 往后移动一位
     * 如果 小于 insertNum 则将 insertNum 插入到 j+1 位
     * <p>
     * <p>
     * <p>
     * 复杂度：
     * 最好：已经排好顺序的集合，这样只需要线性时间即遍历一次集合，每次只需要比较当前元素与前一个元素的大小问题，时间复杂度O(n)
     * 最坏：即刚好与所要的顺序相反，时间复杂度为O(n^2)
     * 平均：时间复杂度也是O(n^2)
     *
     * @param arraytoSort
     * @return
     */
    private static int[] insertionSort(int[] arraytoSort) {
        int length = arraytoSort.length;
        int insertNum; //要插入的数
        for (int i = 1; i < length; i++) { // 排序多少次，第一个数不用排序
            insertNum = arraytoSort[i];
            int j = i - 1; //已经排序好的序列元素个数
            while (j >= 0 && arraytoSort[j] > insertNum) {
                arraytoSort[j + 1] = arraytoSort[j]; //j 位元素大于insertNum, j 以后元素都往后移动一格
                j--;
            }
            arraytoSort[j + 1] = insertNum;//比较到第j 位时 小于 insertNum ，所以insertNum 应该放在 j+1 位
        }
        return arraytoSort;
    }

    /**
     * 插入排序—希尔排序
     * 希尔排序是1959 年由D.L.Shell 提出来的，相对直接排序有较大的改进。希尔排序又叫缩小增量排序
     * <p>
     * 思想
     * 先将整个待排序的记录序列分割成为若干子序列分别进行直接插入排序，待整个序列中的记录“基本有序”时，再对全体记录进行依次直接插入排序。
     * <p>
     * 做法：
     * 首先确定分的组数。一般是取数组长度除以2
     * 然后对每组 组中元素进行 插入排序。
     * 然后将length/2，重复1,2步，直到length=0为止。
     * <p>
     * 复杂度：
     * 最好：O(n log n)
     * 最坏：即刚好与所要的顺序相反，时间复杂度为O(n^2)
     * 分组的依据（n/2）对复杂度的影响比较大。
     */
    private static int[] shellSort(int[] arraytoSort) {
        int length = arraytoSort.length;
        while (length != 0) {
            length = length / 2;
            for (int j = 0; j < length; j++) { //分的组数 ，length 为组的步长
                for (int i = j + length; i < arraytoSort.length; i += length) {  //遍历每组中的元素，从第二个数开始 第一个元素是 j
                    //里面实际上是嵌套了一个 插入排序

                    int x = i - length;//j为当前组有序序列最后一位的位数
                    int temp = arraytoSort[i];//当前要插入的元素
                    while (x >= 0 && arraytoSort[x] > temp) { //从后往前遍历。
                        arraytoSort[x + length] = arraytoSort[x];//向后移动length位
                        x -= length;
                    }
                    arraytoSort[x + length] = temp;

                }
            }
        }
        return arraytoSort;
    }


    /**
     * 简单选择排序
     * <p>
     * 思想：
     * 在要排序的一组数中，选出最小（或者最大）的一个数与第1个位置的数交换；
     * 然后在剩下的数当中再找最小（或者最大）的与第2个位置的数交换，
     * 依次类推，直到第n-1个元素（倒数第二个数）和第n个元素（最后一个数）比较为止。
     * <p>
     * 做法：
     *
     * @param arraytoSort
     * @return
     */
    private static int[] simpleSelectSort(int[] arraytoSort) {
        int length = arraytoSort.length;
        for (int i = 0; i < length; i++) {
            int key = arraytoSort[i];
            int position = i; // 最小数据的位置
            for (int j = i + 1; j < length; j++) { //遍历后面的数据比较最小
                if (arraytoSort[j] < key){ //如果当前数据不是最小的，则交换
                    //记录最小的
                    key = arraytoSort[j];
                    position = j;
                }
            }
            //交换
            arraytoSort[position] = arraytoSort[i];
            arraytoSort[i] = key;
        }
        return arraytoSort;
    }
}
