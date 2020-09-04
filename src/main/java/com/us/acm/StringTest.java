package com.us.acm;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * String 类型的 算法集合
 *
 * @author yyb
 * @time 2020/9/4
 */
public class StringTest {

    public static void main(String[] args) {
        System.out.println("findRepeat: " + findRepeat(init()));
    }


    private static Integer[] init() {
        return new Integer[]{2, 3, 5, 4, 6, 19, 3, 1};
    }

    private static List<Integer> initList() {
        List<Integer> list = new ArrayList<>();
        list.add(2);
        list.add(19);
        list.add(3);
        list.add(5);
        list.add(3);
        list.add(3);
        return list;
    }


    /**
     * 在一个长度为 n 的数组 nums 里的所有数字都在 0～n-1 的范围内。数组中某些数字是重复的，但不知道有几个数字重复了，也不知道每个数字重复了几次。请找出数组中任意一个重复的数字。
     * 输入：
     * [2, 3, 1, 0, 2, 5, 3]
     * 输出：2 或 3
     * 2 <= n <= 100000
     */
    private static int findRepeat(Integer[] init) {
        Set<Integer> set = new HashSet<>();
        int repeat = -1;
        for (int num : init) {
            if (!set.add(num)) {
                repeat = num;
                break;
            }
        }
        return repeat;
    }

}

