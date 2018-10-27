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
//        print(simpleSelectSort(init()));
//        print(heapSort(init()));
//        print(bubbleSort(init()));
//        print(bubbleSort2(init()));
        print(quickSort(init(), 0, init().length - 1));
//        print(mergeSort(init(), 0, init().length - 1));
    }

    private static void print(int[] arrays) {
        for (int i : arrays) {
            System.out.print(i + " ");
        }
        System.out.println();
    }

    private static int[] init() {
//        int[] arrayToSort = {2, 68, 34, 98, 7, 37, 5, 8, 3, 10, 1, 33, 76, 23, 94, 31, 67, 97, 35, 38};
        int[] arrayToSort = {35, 68, 34, 98, 7, 37, 5, 100};
        return arrayToSort;
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
     * <p>
     * 稳定，不会改变相等数原有的顺序
     *
     * @param arrayToSort
     * @return
     */
    private static int[] insertionSort(int[] arrayToSort) {
        int length = arrayToSort.length;
        int insertNum; //要插入的数
        for (int i = 1; i < length; i++) { // 排序多少次，第一个数不用排序
            insertNum = arrayToSort[i];
            int j = i - 1; //已经排序好的序列元素个数
            while (j >= 0 && arrayToSort[j] > insertNum) {
                arrayToSort[j + 1] = arrayToSort[j]; //j 位元素大于insertNum, j 以后元素都往后移动一格
                j--;
            }
            arrayToSort[j + 1] = insertNum;//比较到第j 位时 小于 insertNum ，所以insertNum 应该放在 j+1 位
        }
        return arrayToSort;
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
    private static int[] shellSort(int[] arrayToSort) {
        int length = arrayToSort.length;
        while (length != 0) {
            length = length / 2;
            for (int j = 0; j < length; j++) { //分的组数 ，length 为组的步长
                for (int i = j + length; i < arrayToSort.length; i += length) {  //遍历每组中的元素，从第二个数开始 第一个元素是 j
                    //里面实际上是嵌套了一个 插入排序

                    int x = i - length;//j为当前组有序序列最后一位的位数
                    int temp = arrayToSort[i];//当前要插入的元素
                    while (x >= 0 && arrayToSort[x] > temp) { //从后往前遍历。
                        arrayToSort[x + length] = arrayToSort[x];//向后移动length位
                        x -= length;
                    }
                    arrayToSort[x + length] = temp;

                }
            }
        }
        return arrayToSort;
    }


    /**
     * 简单选择排序
     * <p>
     * 选择排序类似于插入排序，只是是有选择的插入
     * <p>
     * <p>
     * <p>
     * 思想：
     * 在要排序的一组数中，选出最小（或者最大）的一个数与第1个位置的数交换；
     * 然后在剩下的数当中再找最小（或者最大）的与第2个位置的数交换，
     * 依次类推，直到第n-1个元素（倒数第二个数）和第n个元素（最后一个数）比较为止。
     * <p>
     * 做法：
     * 按照数组顺序，记录当前数的位置 和大小，
     * 找寻数组中当前数以后的（也就是未排序的） 最小的数和 位置，
     * 将最小数的位置 和数值与当前数 交换
     * <p>
     * 时间复杂度：n(n − 1) / 2 ∈ Θ(n2)
     *
     * @param arrayToSort
     * @return
     */
    private static int[] simpleSelectSort(int[] arrayToSort) {
        int length = arrayToSort.length;
        for (int i = 0; i < length; i++) {
            int key = arrayToSort[i];
            int position = i; // 最小数据的位置
            for (int j = i + 1; j < length; j++) { //遍历后面的数据比较最小
                if (arrayToSort[j] < key) { //如果当前数据不是最小的，则交换
                    //记录最小的
                    key = arrayToSort[j];
                    position = j;
                }
            }
            //交换
            arrayToSort[position] = arrayToSort[i]; //将 最小的 位置放如 i 的值
            arrayToSort[i] = key; //将最小的值放入 i
        }
        return arrayToSort;
    }


    /**
     * 堆排序
     * 堆排序是选择排序种类的一部分 不是稳定的排序。
     * 优点就是最坏情况下时间复杂度是O(nlogn)
     * <p>
     * 将序列构建成大顶堆。建堆方法 buildMaxHeap
     * 将根节点与最后一个节点交换，然后断开最后一个节点。  交换方法 swap
     * 重复第一、二步，直到所有节点断开。
     *
     * @param arrayToSort
     * @return
     */
    private static int[] heapSort(int[] arrayToSort) {
        int arrayLength = arrayToSort.length;
        //循环建堆
        for (int i = 0; i < arrayLength - 1; i++) {
            //建大顶堆
            buildMaxHeap(arrayToSort, arrayLength - 1 - i);
            print(arrayToSort);
            //交换堆顶和最后一个元素
            swap(arrayToSort, 0, arrayLength - 1 - i);
        }

        return arrayToSort;
    }

    private static void swap(int[] data, int i, int j) {
        int tmp = data[i];
        data[i] = data[j];
        data[j] = tmp;
    }

    /**
     * 对data数组从0到lastIndex建大顶堆
     *
     * @param data
     * @param lastIndex
     */
    private static void buildMaxHeap(int[] data, int lastIndex) {
        // 从lastIndex处节点（最后一个节点）的父节点开始
        // (lastIndex - 1) / 2 为最后的一个根节点的索引
        for (int i = (lastIndex - 1) / 2; i >= 0; i--) {
            //k保存正在判断的节点
            int k = i;
            //如果当前k节点的子节点存在
            while (k * 2 + 1 <= lastIndex) {
                //k节点的左子节点的索引
                int biggerIndex = 2 * k + 1;
                //如果biggerIndex小于lastIndex，即biggerIndex+1代表的k节点的右子节点存在
                if (biggerIndex < lastIndex) {
                    //若果右子节点的值较大
                    if (data[biggerIndex] < data[biggerIndex + 1]) {
                        //若左节点小于右节点，则biggerIndex+1 此时 则biggerIndex 实际为右节点的索引，所以biggerIndex总是记录较大子节点的索引
                        biggerIndex++;
                    }
                }
                //如果k节点（k为根节点）的值小于其较大的子节点的值
                if (data[k] < data[biggerIndex]) {
                    //交换他们交换他们
                    swap(data, k, biggerIndex);
                    //将biggerIndex赋予k，开始while循环的下一次循环，重新保证k节点的值大于其左右子节点的值
//                    k = biggerIndex;
                } else {
                    break;
                }
            }
        }
    }

    /**
     * 交换排序： 冒泡排序
     * 思想：
     * 将序列中所有元素两两比较，将最大的放在最后面。让较大的数往下沉，较小的往上冒
     * 将剩余序列中所有元素两两比较，将最大的放在最后面。
     * <p>
     * 冒泡排序效率非常低，效率还不如插入排序。
     *
     * @param arrayToSort
     * @return
     */
    private static int[] bubbleSort(int[] arrayToSort) {
        int arrayLength = arrayToSort.length;
        for (int i = 0; i < arrayLength; i++) {//i为拍好序的元素个数
            for (int j = 0; j < arrayLength - i - 1; j++) { //j 为未排序的元素个数
                if (arrayToSort[j + 1] < arrayToSort[j]) {
                    int tmp = arrayToSort[j + 1];
                    arrayToSort[j + 1] = arrayToSort[j];
                    arrayToSort[j] = tmp;
                }
            }
            print(arrayToSort);
        }
        return arrayToSort;
    }


    /**
     * 冒泡排序优化：
     * 设置一标志性变量pos,用于记录每趟排序中最后一次进行交换的位置。由于pos位置之后的记录均已交换到位,
     * 故在进行下一趟排序时只要扫描到pos位置即可。
     *
     * @param arrayToSort
     * @return
     */
    private static int[] bubbleSort2(int[] arrayToSort) {
        int arrayLength = arrayToSort.length;

        for (int i = 0; i < arrayLength; i++) {//i为拍好序的元素个数
            int pos = 0;
            for (int j = 0; j < arrayLength - i - 1; j++) { //j 为未排序的元素个数
                if (arrayToSort[j + 1] < arrayToSort[j]) {
                    int tmp = arrayToSort[j + 1];
                    arrayToSort[j + 1] = arrayToSort[j];
                    arrayToSort[j] = tmp;
                    pos = 1;
                }
            }
            if (pos == 0) {// pos 等于 0 时，说明已经排序好了，就不需要再做比较了
                break;
            }
        }
        return arrayToSort;
    }


    /**
     * 交换排序： 快速排序
     * 要求时间最快时。
     * 快速排序是一个不稳定的排序方法。
     * <p>
     * 思想：
     * 1）选择一个基准元素,通常选择第一个元素或者最后一个元素,
     * 2）通过一趟排序讲待排序的记录分割成独立的两部分，其中一部分记录的元素值均比基准元素值小。另一部分记录的 元素值比基准值大。
     * 3）此时基准元素在其排好序后的正确位置
     * 4）然后分别对这两部分记录用同样的方法继续进行排序，直到整个序列有序。
     * <p>
     * 解决：递归
     *
     * @param arrayToSort
     * @param start       0
     * @param end         数组最后一位下标
     * @return
     */
    private static int[] quickSort(int[] arrayToSort, int start, int end) {
        if (start < end) {
            int base = arrayToSort[start]; // 选定的基准值（第一个数值作为基准值）
            int temp; // 记录临时中间值
            int i = start, j = end;
            do {
                while (arrayToSort[i] < base && i < end)// 左边 i < end 数组不能越界
                    i++;
                while (arrayToSort[j] > base && j > start)// 右边 j > start 数组不能越界
                    j--;
                if (i <= j) {//得到上边两个while的不满足条件，比如 下标 i 的值大于 base 和 下标 j 的值小于 base 交换位置
                    temp = arrayToSort[i];
                    arrayToSort[i] = arrayToSort[j];
                    arrayToSort[j] = temp;
                    i++;
                    j--;
                }
            } while (i <= j);// i <= j 说明第一趟还没有比较完。
            // 由于第一趟的两个 while  i++和 j-- 操作，i 和j之间的元素都是排序好的，但是i和j 之间相差的元素个数不确定。
            if (start < j) {
                quickSort(arrayToSort, start, j); //递归比较第一趟的左边部分,第一趟循环完毕，下标 j 是小于 base 的 所以 j 之前的就是 左边部分
            }
            if (end > i) {
                quickSort(arrayToSort, i, end);//递归比较第一趟的右边边部分，下标 i 是大于 base 的 所以 j 之前的就是 右边部分
            }
        }
        return arrayToSort;
    }


    /**
     * 归并排序
     *
     * @param numbers
     * @param left    0
     * @param right   数组最后一位下标
     */
    public static int[] mergeSort(int[] numbers, int left, int right) {
        int t = 1;// 每组元素个数
        int size = right - left + 1;
        while (t < size) {
            int s = t;// 本次循环每组元素个数
            t = 2 * s; // 合并后组的元素个数
            int i = left; //本组排序的起始下标
            while (i + (t - 1) < size) {
                merge(numbers, i, i + (s - 1), i + (t - 1));
                i += t;
            }
            if (i + (s - 1) < right)
                merge(numbers, i, i + (s - 1), right);
        }
        return numbers;
    }

    private static void merge(int[] data, int p, int q, int r) {
        int[] B = new int[data.length];
        int s = p;
        int t = q + 1;
        int k = p;
        while (s <= q && t <= r) {
            if (data[s] <= data[t]) {
                B[k] = data[s];
                s++;
            } else {
                B[k] = data[t];
                t++;
            }
            k++;
        }
        if (s == q + 1)
            B[k++] = data[t++];
        else
            B[k++] = data[s++];
        for (int i = p; i <= r; i++)
            data[i] = B[i];
        print(data);
    }


    /**
     * 梳排序
     * 它是冒泡排序的一种变体，就像希尔排序一样，也是利用一个间隔值来堆其进行分组，只不过希尔排序内部嵌套的是插入排序，而梳排序嵌套的是冒泡排序。
     */

}
