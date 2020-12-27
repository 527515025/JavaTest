package com.us.acm;


/**
 * @author yyb
 * @time 2020/12/14
 */
public class ArrayTest {
    public static void main(String[] args) {

//        int i = removeElement(init(), 5);
//        int z = search(init(), 4);
        int max = searchMax(new int[]{1, 4, 5, 7, 8, 11, 7, 6, 5, 3, 0});
        System.out.println();
    }


    private static int[] init() {
        return new int[]{1, 2, 3, 4, 4, 4, 5, 6};
    }


    /**
     * 给你一个数组 nums 和一个值 val，你需要 原地 移除所有数值等于 val 的元素，并返回移除后数组的新长度。
     * <p>
     * 不要使用额外的数组空间，你必须仅使用 O(1) 额外空间并 原地 修改输入数组。
     * <p>
     * 元素的顺序可以改变。你不需要考虑数组中超出新长度后面的元素。
     * <p>
     * 来源：力扣（LeetCode）
     * 链接：https://leetcode-cn.com/problems/remove-element
     * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
     * <p>
     * <p>
     * 给定 nums = [0,1,2,2,3,0,4,2], val = 2,
     * <p>
     * 函数应该返回新的长度 5, 并且 nums 中的前五个元素为 0, 1, 3, 0, 4。
     * <p>
     * 注意这五个元素可为任意顺序。
     * <p>
     * 你不需要考虑数组中超出新长度后面的元素。
     * <p>
     * <p>
     * 思想：双指针
     * i 记录有效长度，j遍历数组，并移动数组位置
     *
     * @param nums
     * @param val
     * @return
     */
    public static int removeElement(int[] nums, int val) {
        int i = 0;
        for (int j = 0; j < nums.length; j++) {
            if (nums[j] != val) {
                nums[i] = nums[j];
                i++;
            }
        }
        return i;
    }


    /**
     * 统计一个数字在排序数组中出现的次数。
     * 剑指 Offer 53 - I. 在排序数组中查找数字 I
     * https://leetcode-cn.com/problems/zai-pai-xu-shu-zu-zhong-cha-zhao-shu-zi-lcof/
     * <p>
     * 输入: nums = [5,7,7,8,8,10], target = 8
     * 输出: 2
     * <p>
     * 二分法
     *
     * @param nums
     * @param target
     * @return
     */
    public static int search(int[] nums, int target) {
        return helper(nums, target) - helper(nums, target - 1);
    }

    /**
     * 寻找边界
     *
     * @param nums
     * @param tar
     * @return
     */
    static int helper(int[] nums, int tar) {
        int i = 0, j = nums.length - 1;
        while (i <= j) {
            int m = (i + j) / 2;
            if (nums[m] <= tar) {
                i = m + 1;
            } else {
                j = m - 1;
            }
        }
        return i;
    }

    /**
     * 查找 先升后降数组中最大的值
     * 例如 14578976530  返回9
     *
     * @param nums
     * @return
     */
    public static int searchMax(int[] nums) {
        int i = 0, j = nums.length - 1;
        while (i <= j) {
            int m = (i + j) / 2;
            if (nums[m] < nums[m + 1]) {
                i = m + 1;
            } else {
                j = m - 1;
            }
        }
        return nums[i];
    }


}
