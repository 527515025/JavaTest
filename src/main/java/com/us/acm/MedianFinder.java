package com.us.acm;

import java.util.Collections;
import java.util.PriorityQueue;

/**
 * 数据流中的中位数
 *  大小顶堆，排序。使用 优先队列
 *
 *
 * 如何得到一个数据流中的中位数？如果从数据流中读出奇数个数值，那么中位数就是所有数值排序之后位于中间的数值。如果从数据流中读出偶数个数值，那么中位数就是所有数值排序之后中间两个数的平均值。
 * <p>
 * 例如，
 * <p>
 * [2,3,4] 的中位数是 3
 * <p>
 * [2,3] 的中位数是 (2 + 3) / 2 = 2.5
 *
 * @author yyb
 * @time 2020/5/20
 * <p>
 * 思路，获取流数据后进行排序，size%2 ==0 则取中间两个数相加除以 2 ，如果 size%2=1 则取中间数
 * 由于 stream.sorted 是自然排序所以比较慢，时间不符合要求，顾采用堆排序
 */
public class MedianFinder {
    /**
     * 优先队列，PriorityQueue 线程不安全，多线程使用 PriorityBlockingQueue
     * 该队列的头部是相对于指定顺序的最小元素
     */
    private PriorityQueue<Integer> maxHeap, minHeap;


    public MedianFinder() {
        //大顶堆 优先队列实现的大顶堆
        maxHeap = new PriorityQueue<>(Collections.reverseOrder());
        //小顶堆 优先队列实现的小顶堆
        minHeap = new PriorityQueue<>();
    }

    public void addNum(int num) {
        //将指定的元素插入到此优先级队列中。
        maxHeap.offer(num);
        //将大顶堆中最小的元素放到小顶堆
        minHeap.offer(maxHeap.poll());
        //如果小顶堆中数大于
        if (minHeap.size() > maxHeap.size()) {
            maxHeap.offer(minHeap.poll());
        }
    }

    public double findMedian() {
        if (maxHeap.size() == minHeap.size()) {
            //peek检索但不删除此队列的头，如果此队列为空，则返回 null 。
            return (maxHeap.peek() + minHeap.peek()) * 0.5;
        }
        return maxHeap.peek();
    }


    public static void main(String[] args) {
        MedianFinder finder = new MedianFinder();
        finder.addNum(11);
        finder.addNum(7);
        finder.addNum(15);
        finder.addNum(116);
        finder.addNum(13);
        finder.addNum(24);
        System.out.println(finder.findMedian());

    }


}
