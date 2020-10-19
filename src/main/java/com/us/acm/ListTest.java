package com.us.acm;


/**
 * @author yyb
 * @time 2020/10/19
 */
public class ListTest {


    public static void main(String[] args) {
        ListNode pre = init("1,2,3,4,5");
        ListNode listNode = removeNthFromEnd(pre, 3);
        System.out.println();
    }

    /**
     * 初始化链表
     *
     * @param str
     * @return
     */
    public static ListNode init(String str) {
        ListNode pre = null;
        ListNode next = null;
        String[] strArrays = str.split(",");
        for (int x = 0; x < strArrays.length; x++) {
            if (x == 0) {
                pre = new ListNode();
                pre.setVal(Integer.valueOf(strArrays[x]));
                next = pre;
            } else {
                next = addListNode(next, Integer.valueOf(strArrays[x]));
            }
        }
        return pre;
    }

    public static ListNode addListNode(ListNode pre, int val) {
        ListNode next = new ListNode(val, null);
        pre.setNext(next);
        return next;
    }


    /**
     * 给定一个链表，删除链表的倒数第 n 个节点，并且返回链表的头结点。
     * 进阶：
     * <p>
     * 你能尝试使用一趟扫描实现吗？
     * https://leetcode-cn.com/problems/remove-nth-node-from-end-of-list/
     * <p>
     * <p>
     * 思想双指针法，
     * 每次遍历，保存尾指针和 输入的倒数n指针。
     * 刚开始保存的指针为 0 和 n ,然后循环 n++ 0++； 当 n 节点的下一个节点为空时即 n指向了链表尾部，则0 指针 指的为 倒数第n个节点，删除即可。
     * <p>
     * 注意：考虑 一个节点情况，和 删除倒数第n 个节点，n=链表长度
     * <p>
     * <p>
     * <p>
     * 0 ms	36.2 MB
     *
     * @param head
     * @param n
     * @return
     */
    public static ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode result = head;
        ListNode next = head;
        //当只有一个元素时，删除倒数第一个节点
        if (head.getNext() == null && n == 1) {
            return null;
        }
        for (int i = 0; i < n; i++) {
            //当要删除的节点刚好是头节点的情况，比如输入 [1,2,3] 删除倒数 第3个节点 的情况
            if (next != null && next.getNext() == null && i == (n - 1)) {
                return head.getNext();
            }
            next = next.getNext();
        }
        while (next.getNext() != null) {
            next = next.getNext();
            result = result.getNext();
        }
        result.setNext(result.getNext().getNext());
        return head;
    }


    public static class ListNode {
        int val;
        ListNode next;

        ListNode() {
        }

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }

        public int getVal() {
            return val;
        }

        public void setVal(int val) {
            this.val = val;
        }

        public ListNode getNext() {
            return next;
        }

        public void setNext(ListNode next) {
            this.next = next;
        }
    }


}
