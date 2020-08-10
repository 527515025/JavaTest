package com.us.acm;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
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
 */
public class MedianFinder {
    private List<Double> init;

    /**
     * initialize your data structure here.
     */
    public MedianFinder() {
        init = new ArrayList<>();
    }

    public void addNum(int num) {
        init.add(Double.valueOf(num));
    }

    public double findMedian() {
        if (init == null) {
            return -1;
        }
        List<Double> sort = init.stream().sorted().collect(Collectors.toList());
        int remainder = sort.size() % 2;
        int merchant = sort.size() / 2;
        double median;
        if (remainder == 1) {
            median = sort.get(merchant);
        } else {
            median = (sort.get(merchant - 1) + sort.get(merchant)) / 2;
        }
        return median;
    }


    public static void main(String[] args) {
        MedianFinder finder = new MedianFinder();
        finder.addNum(1);
        finder.addNum(7);
        finder.addNum(15);
        finder.addNum(116);
        finder.addNum(13);
        finder.addNum(24);
        System.out.println(finder.findMedian());

    }





}
