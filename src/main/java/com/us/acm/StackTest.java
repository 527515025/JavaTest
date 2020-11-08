package com.us.acm;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.LinkedBlockingQueue;

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

    public static void main(String[] args) {
        MyStack2 myStack = new MyStack2();
        myStack.push(1);
        myStack.push(3);
        myStack.push(4);
        myStack.push(5);
        System.out.println("000");
    }


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
     * 6 ms 39.8 MB
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

    /**
     * 用队列实现栈
     * <p>
     * 使用队列实现栈的下列操作：
     * <p>
     * push(x) -- 元素 x 入栈
     * pop() -- 移除栈顶元素
     * top() -- 获取栈顶元素
     * empty() -- 返回栈是否为空
     * 注意:
     * <p>
     * 你只能使用队列的基本操作-- 也就是 push to back, peek/pop from front, size, 和 is empty 这些操作是合法的。
     * 你所使用的语言也许不支持队列。 你可以使用 list 或者 deque（双端队列）来模拟一个队列 , 只要是标准的队列操作即可。
     * 你可以假设所有操作都是有效的（例如, 对一个空的栈不会调用 pop 或者 top 操作）。
     * <p>
     * 来源：力扣（LeetCode）
     * 链接：https://leetcode-cn.com/problems/implement-stack-using-queues
     * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
     */
    static class MyStack {
        Queue<Integer> queue1;

        public MyStack() {
            queue1 = new LinkedBlockingQueue();
        }

        /**
         * Push element x onto stack.
         * 入栈
         */
        public void push(int x) {
            int count = queue1.size();
            queue1.offer(x);
            for (int i = 0; i < count; i++) {
                queue1.offer(queue1.poll());
            }
        }

        /**
         * Removes the element on top of the stack and returns that element.
         */
        public int pop() {
            return queue1.poll();
        }

        /**
         * Get the top element.
         */
        public int top() {
            return queue1.peek();
        }

        /**
         * Returns whether the stack is empty.
         */
        public boolean empty() {
            return queue1.isEmpty();
        }

    }


    /**
     * 使用双端队列实现栈
     */
    static class MyStack2 {
        ArrayDeque<Integer> deque;

        /**
         * Initialize your data structure here.
         */
        public MyStack2() {
            deque = new ArrayDeque<>();

        }

        /**
         * Push element x onto stack.
         */
        public void push(int x) {
            deque.addFirst(x);
        }

        /**
         * Removes the element on top of the stack and returns that element.
         */
        public int pop() {
            return deque.pop();
        }

        /**
         * Get the top element.
         */
        public int top() {
            return deque.peek();
        }

        /**
         * Returns whether the stack is empty.
         */
        public boolean empty() {
            return deque.isEmpty();
        }
    }

}
