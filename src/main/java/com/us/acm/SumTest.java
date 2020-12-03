package com.us.acm;

import java.util.*;

/**
 * 两数之和
 * <p>
 * 给定一个整数数组 nums 和一个目标值 target，请你在该数组中找出和为目标值的那 两个 整数，并返回他们的数组下标。
 * <p>
 * 你可以假设每种输入只会对应一个答案。但是，数组中同一个元素不能使用两遍。
 * <p>
 *
 * @author yyb
 * @time 2020/6/17
 */
public class SumTest {
    public static void main(String[] args) {
//        int[] result = twoSum(new int[]{3, 2, 4}, 6);
//        Arrays.stream(result).parallel().forEach(x -> System.out.println(x));
        threeSum2(new int[]{-1, 0, 1, 2, -1, -4});
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


    /**
     * 三数之和
     * <p>
     * 给你一个包含 n 个整数的数组 nums，判断 nums 中是否存在三个元素 a，b，c ，使得 a + b + c = 0 ？请你找出 所有 满足条件且不重复的三元组。
     * <p>
     * 来源：力扣（LeetCode）
     * 链接：https://leetcode-cn.com/problems/3sum
     * <p>
     * 思路：
     * 排序+双指针法
     * <p>
     * 1429 ms	45.1 MB
     *
     * @param nums
     * @return
     */
    public static List<List<Integer>> threeSum(int[] nums) {
        //此处用set 会超出查询时间
        Map<String, Integer> map = new HashMap<>();
        List<List<Integer>> result = new ArrayList<>();
        Arrays.sort(nums);
        for (int i = 0; i < nums.length; i++) {
            int sum = 0 - nums[i];
            int k = nums.length - 1;
            int j = i + 1;
            while (k > j) {
                int subSum = nums[j] + nums[k];
                String keys = nums[i] + "|" + nums[j] + "|" + nums[k];
                if (subSum == sum && map.get(keys) == null) {
                    List<Integer> list = new ArrayList<>();
                    list.add(nums[i]);
                    list.add(nums[j]);
                    list.add(nums[k]);
                    result.add(list);
                    map.put(keys, 1);
                    System.out.println(keys);
                    j++;
                } else if (subSum > sum) {
                    k--;
                } else {
                    j++;
                }
            }

        }
        return result;
    }

    /**
     * 三数之和
     * <p>
     * 根据官方方式改进后，进行去重操作。
     * 通过 nums[i] == nums[i - 1] 判断 替换 map 去重。
     * <p>
     * 26 ms	43 MB
     * <p>
     * i 为排列后的第一个数
     * j 为双指针的头指针
     * k 为双指针的尾指针
     * sum 为后两个数的和
     * <p>
     * *核心思想**
     * 三数之和 转化为两数之和。即获取第一个数，然后减去第一个数后的值，为后两个数的和（sum）。然后通过双指针计算后两个数。
     * <p>
     * 使用双指针首先需要排序。让数据按照从小到大的顺序，当然也可以从大到小。
     * <p>
     * *移动指针**
     * 头尾指针相加之和 > sum 则说明两数较大，所以尾指针需要前移（因为数组已经按照从小到达排序了），减小数。
     * <p>
     * 头尾指针相加之和 < sum 则说明两数较小，所以前指针需要后移，增大数。
     * <p>
     * *去重**
     * 则只需要保证和上一次枚举的数不相同即可。需要过滤第一次的数。
     *
     * @param nums
     * @return
     */
    public static List<List<Integer>> threeSum2(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        Arrays.sort(nums);
        for (int i = 0; i < nums.length; i++) {
            // 需要和上一次枚举的数不相同
            if (i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }
            int sum = 0 - nums[i];
            int k = nums.length - 1;
            int j = i + 1;

            while (k > j) {
                int subSum = nums[j] + nums[k];
                //j > i + 1 是为了使用 i i+1 两个位置的数相同连续两个相同的数， nums[j] == nums[j - 1] 是需要和上一次枚举的数不相同, 去除重复的循环
                if (j > i + 1 && nums[j] == nums[j - 1]) {
                    j++;
                    continue;
                }
                if (subSum == sum) {
                    List<Integer> list = new ArrayList<>();
                    list.add(nums[i]);
                    list.add(nums[j]);
                    list.add(nums[k]);
                    result.add(list);
                    j++;
                } else if (subSum > sum) {
                    // 两数之和 > 结果，则说明两数较大，所以后指针需要前移，减小数。
                    k--;
                } else {
                    // 两数之和 < 结果，则说明两数较小，所以前指针需要后移，增大数。
                    j++;
                }
            }
        }
        return result;
    }


    /**
     * 官方题解 双指针
     * <p>
     * 23 ms	43 MB
     *
     * @param nums
     * @return
     */
    public static List<List<Integer>> threeSumL(int[] nums) {
        int n = nums.length;
        Arrays.sort(nums);
        List<List<Integer>> ans = new ArrayList<>();
        // 枚举 a
        for (int first = 0; first < n; ++first) {
            // 需要和上一次枚举的数不相同
            if (first > 0 && nums[first] == nums[first - 1]) {
                continue;
            }
            // c 对应的指针初始指向数组的最右端
            int third = n - 1;
            int target = -nums[first];
            // 枚举 b
            for (int second = first + 1; second < n; ++second) {
                // 需要和上一次枚举的数不相同
                if (second > first + 1 && nums[second] == nums[second - 1]) {
                    continue;
                }
                // 需要保证 b 的指针在 c 的指针的左侧
                while (second < third && nums[second] + nums[third] > target) {
                    --third;
                }
                // 如果指针重合，随着 b 后续的增加
                // 就不会有满足 a+b+c=0 并且 b<c 的 c 了，可以退出循环
                if (second == third) {
                    break;
                }
                if (nums[second] + nums[third] == target) {
                    List<Integer> list = new ArrayList<Integer>();
                    list.add(nums[first]);
                    list.add(nums[second]);
                    list.add(nums[third]);
                    ans.add(list);
                }
            }
        }
        return ans;
    }


}
