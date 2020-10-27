package com.us.acm;

import java.util.Stack;

/**
 * 用两个栈实现队列
 * <p>
 * 用两个栈实现一个队列。队列的声明如下，请实现它的两个函数 appendTail 和 deleteHead ，分别完成在队列尾部插入整数和在队列头部删除整数的功能。(若队列中没有元素，deleteHead 操作返回 -1 )
 * <p>
 * <p>
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/yong-liang-ge-zhan-shi-xian-dui-lie-lcof
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 * <p>
 * 思路：两个栈的反转为 队列
 *
 * @author yyb
 * @time 2020/10/27
 */
public class StackTest {

    Stack<Integer> input;
    Stack<Integer> output;

    public StackTest() {
        input = new Stack<>();
        output = new Stack<>();
    }

    public void appendTail(int value) {
        input.push(value);
    }

    public int deleteHead() {
        if (!output.isEmpty()) {
            return output.pop();
        } else if (!input.isEmpty()) {
            while (!input.isEmpty()) {
                output.push(input.pop());
            }
        }
        return output.isEmpty() ? -1 : output.pop();
    }


    /**
     * 最小栈
     * <p>
     * 设计一个支持 push ，pop ，top 操作，并能在常数时间内检索到最小元素的栈。时间复杂度 o1
     * <p>
     * push(x) —— 将元素 x 推入栈中。
     * pop() —— 删除栈顶的元素。
     * top() —— 获取栈顶元素。
     * getMin() —— 检索栈中的最小元素。
     * <p>
     * 来源：力扣（LeetCode）
     * 链接：https://leetcode-cn.com/problems/min-stack
     * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
     * <p>
     * <p>
     * 	6 ms 39.8 MB
     */
    class MinStack {
        Stack<Integer> stack;
        private Integer min;

        /**
         * initialize your data structure here.
         */
        public MinStack() {
            stack = new Stack<>();
            min = Integer.MAX_VALUE;
        }

        public void push(int x) {
            if (min >= x) {
                //先把当前最小值入栈记录 成为第二小的值
                stack.push(min);
                min = x;

            }
            stack.push(x);
        }

        public void pop() {
            Integer x = stack.pop();
            if (x.equals(min)) {
                min = stack.pop();
            }
        }

        public int top() {
            //获取栈顶元素，但是不删除
            return stack.peek();
        }

        public int getMin() {
            return min;
        }
    }
}
