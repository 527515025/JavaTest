package com.us.acm;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 两数之和
 *
 * 给定一个整数数组 nums 和一个目标值 target，请你在该数组中找出和为目标值的那 两个 整数，并返回他们的数组下标。
 * <p>
 * 你可以假设每种输入只会对应一个答案。但是，数组中同一个元素不能使用两遍。
 * <p>
 *
 * @author yyb
 * @time 2020/6/17
 */
public class TwoSum {
    public static void main(String[] args) {
        int[] result = twoSum(new int[]{3, 2, 4}, 6);
        Arrays.stream(result).parallel().forEach(x -> System.out.println(x));
    }

    /**
     * 两次遍历
     *
     * @param nums
     * @param target
     * @return
     */
    public static int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            map.put(nums[i], i);
        }

        for (int i = 0; i < nums.length; i++) {
            Integer value = map.get(target - nums[i]);
            //因为自身只能使用一次，所以 value != i 不取自身
            if (null != value && value != i) {
                return new int[]{i, value};

            }
        }
        throw new IllegalArgumentException("No two sum solution");
    }

    /**
     * 一次遍历
     * 在进行迭代并将元素插入到表中的同时，会回过头来检查表中是否已经存在当前元素所对应的目标元素。
     * 如果它存在，那我们已经找到了对应解，并立即将其返回
     *
     * @param nums
     * @param target
     * @return
     */
    public static int[] twoSum2(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            Integer value = map.get(target - nums[i]);
            if (null != value) {
                return new int[]{i, value};
            }
            //因为自身只能使用一次，所以应该先查寻再放入map
            map.put(nums[i], i);
        }
        throw new IllegalArgumentException("No two sum solution");
    }
}
