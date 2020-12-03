package com.us.acm;

import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * String 类型的 算法集合
 *
 * @author yyb
 * @time 2020/9/4
 */
public class StringTest {

    public static void main(String[] args) {

        System.out.println(patternMatching("({[(0809809)]})"));
//        System.out.println(maxFreq("abbabbabbceabbede", 3, 2, 4));
//        System.out.println("findRepeat: " + findRepeat(init()));
        reverseString(new char[]{});

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


    /**
     * 力扣 1297. 子串的最大出现次数
     * <p>
     * 给你一个字符串 s ，请你返回满足以下条件且出现次数最大的 任意 子串的出现次数：
     * <p>
     * 子串中不同字母的数目必须小于等于 maxLetters 。
     * 子串的长度必须大于等于 minSize 且小于等于 maxSize 。
     * <p>
     * 来源：力扣（LeetCode）
     * 链接：https://leetcode-cn.com/problems/maximum-number-of-occurrences-of-a-substring
     * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
     * <p>
     * <p>
     * <p>
     * <p>
     * <p>
     * 思路：暴力遍历。
     * 只关注minSize即可，因为如果长度为maxSize的字串出现了N次那么长度为minSize的字串出现次数也会是N
     * 而题目要求返回出现次数最大的任意子串。
     * <p>
     * 2、确定了长度以后就可以维护一个固定大小的滑动窗口 (i + minsize)去统计满足条件（不同字母的数目小于等于maxLetters）的
     * 子串的出现次数了，统计次数可以借助map来做。
     *
     * @param s
     * @param maxLetters 字串中不重复的字母个数
     * @param minSize    字串最小长度 <=26
     * @param maxSize    字串最大长度 <=26
     * @return 字串出现的次数
     */
    public static int maxFreq(String s, int maxLetters, int minSize, int maxSize) {
        int len = s.length();
        //统计出现次数
        Map<String, Integer> count = new HashMap<>();

        for (int i = 0; i < len; i++) {
            if (i + minSize > len) {
                break;
            }
            String sub = s.substring(i, i + minSize);
            if (isMatch(sub, maxLetters)) {
                count.put(sub, count.getOrDefault(sub, 0) + 1);
            }
        }
        //最大次数
        int ansMax = 0;
        for (Map.Entry<String, Integer> entry : count.entrySet()) {
            if (entry.getValue() > ansMax) {
                ansMax = entry.getValue();
            }
        }
        return ansMax;
    }


    /**
     * 判断不重复的字符有多少个
     *
     * @param s
     * @param maxLetters
     * @return
     */
    private static boolean isMatch(String s, int maxLetters) {
        Set<Character> chars = new HashSet<>();
        for (Character c : s.toCharArray()) {
            chars.add(c);
        }
        if (chars.size() > maxLetters) {
            return false;
        }

        return chars.size() <= maxLetters;
    }


    /**
     * 模式匹配
     * 给定一个只包括 '('，')'，'{'，'}'，'['，']' 的字符串，判断字符串是否有效。
     * <p>
     * 有效字符串需满足：
     * <p>
     * 左括号必须用相同类型的右括号闭合。
     * 左括号必须以正确的顺序闭合。
     * 注意空字符串可被认为是有效字符串。
     * <p>
     * 来源：力扣（LeetCode）
     * 链接：https://leetcode-cn.com/problems/valid-parentheses
     *
     * @param s
     * @return
     */
    private static boolean patternMatching(String s) {
        int len = s.length();
        Map<Character, Character> map = new HashMap<>(4);
        map.put('}', '{');
        map.put(')', '(');
        map.put(']', '[');
        Stack<Character> stack = new Stack<>();
        for (int i = 0; i < len; i++) {
            char c = s.charAt(i);
            if (map.containsKey(c)) {
                // 栈为空或括号不匹配, peek() 返回栈顶元素，但不在堆栈中删除它。
                if (stack.isEmpty() || !stack.peek().equals(map.get(c))) {
                    return false;
                }
                //元素匹配，则返回栈顶元素，并在进程中删除它。
                stack.pop();
            } else if (map.containsValue(c)) {
                stack.push(c);
            }
        }
        //栈不为空则不匹配
        return stack.isEmpty();
    }


    /**
     * 反转字符串
     * 编写一个函数，其作用是将输入的字符串反转过来。输入字符串以字符数组 char[] 的形式给出。
     * <p>
     * 不要给另外的数组分配额外的空间，你必须原地修改输入数组、使用 O(1) 的额外空间解决这一问题。
     * <p>
     * 你可以假设数组中的所有字符都是 ASCII 码表中的可打印字符。
     * <p>
     * 来源：力扣（LeetCode）
     * 链接：https://leetcode-cn.com/problems/reverse-string
     * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
     * <p>
     * 思想指针交换
     *
     * @param s
     */
    public static void reverseString(char[] s) {
        if (s.length == 0) {
            return;
        }
        int l = s.length - 1;
        for (int i = 0; i <= l / 2; i++) {
            char tmp = s[i];
            s[i] = s[l - i];
            s[l - i] = tmp;
        }
    }

}

