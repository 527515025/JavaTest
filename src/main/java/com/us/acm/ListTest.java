package com.us.acm;


import java.util.List;

/**
 * @author yyb
 * @time 2020/10/19
 */
public class ListTest {


    public static void main(String[] args) {
        ListNode pre = init("1,2,3,4,5");
//        ListNode listNode = removeNthFromEnd(pre, 3);
//        ListNode listNode2 = reverseList(pre);
        ListNode listNode3 = reverseRecurList(pre);
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


    /**
     * 链表结构
     */
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


    /**
     * 反转链表
     *
     * @param head
     * @return
     */
    private static ListNode reverseList(ListNode head) {
        ListNode cur = head;
        ListNode pre = null;
        while (cur != null) {
            // 暂存后继节点 cur.next
            ListNode tmp = cur.next;
            //头节点指向空，当作尾节点
            cur.next = pre;
            //尾节点前移，为当前已经反转的节点
            pre = cur;
            //移动到下一个节点。
            cur = tmp;
        }
        return pre;
    }

    /**
     * 反转单链表递归
     *
     * @param head
     * @return
     */
    private static ListNode reverseRecurList(ListNode head) {
        return recur(head, null);
    }

    private static ListNode recur(ListNode cur, ListNode pre) {
        if (cur == null) {
            return pre;
        }
        ListNode res = recur(cur.next, cur);
        //递归指向自己的上一个节点。
        cur.next = pre;
        //返回尾节点
        return res;
    }

    /**
     * 判断链表成环
     * <p>
     * <p>
     * 方法一，，存储链表节点，存储前判断，看是否重复
     * 方法二，快慢指针
     *
     * @param head
     * @return
     */
    private static boolean ringList(ListNode head) {
        if (head == null || head.next == null) {
            return false;
        }
        ListNode tmpA = head;
        //B 为快指针
        ListNode tmpB = head.next;
        while (tmpA != tmpB) {
            // 无环则 B指针可以遍历完链表，否则B指针会一直周旋,
            if (tmpB == null || tmpB.next == null) {
                return false;
            }
            tmpA = tmpA.next;
            //快慢指针步长不能一致，否则会追不上
            tmpB = tmpB.next.next;
        }
        return true;
    }

    /**
     * 判断链表交叉
     * https://leetcode-cn.com/problems/liang-ge-lian-biao-de-di-yi-ge-gong-gong-jie-dian-lcof/
     * <p>
     * 方法一，存储链表节点，存储前判断，看是否重复
     * 方法二，双指针。
     * <p>
     * <p>
     * 两个指针 node1，node2 分别指向两个链表 headA，headB 的头结点，然后同时分别逐结点遍历，
     * 当 node1 到达链表 headA 的末尾时，重新定位到链表 headB 的头结点；
     * 当 node2 到达链表 headB 的末尾时，重新定位到链表 headA 的头结点。
     * <p>
     * <p>
     * 这样，当它们相遇时，所指向的结点就是第一个公共结点。
     *
     * @param
     * @return
     */
    private static ListNode crossList(ListNode headA, ListNode headB) {
        ListNode tmpA = headA;
        ListNode tmpB = headB;
        while (tmpA != tmpB) {
            //重新定位到另外一个链表的首位则 可以通过两次遍历即可弥补差值，如果是仍遍历本身数组则相交点为最小公倍数
            tmpA = tmpA == null ? headB : tmpA.next;
            tmpB = tmpB == null ? headA : tmpB.next;
        }
        return tmpA;
    }


    /**
     * 判断链表交叉
     * <p>
     * 方法一，存储两链表节点，存储前判断是否存在节点，存在则说明相交。
     * 方法二 前后指针,
     * 方法三 消减链表长度差值，同时起步，找相交
     * <p>
     * 两个链表的第一个公共节点
     * <p>
     * <p>
     * 先统计两个链表的长度，如果两个链表的长度不一样，就让链表长的先走，直到两个链表长度一样，
     * 这个时候两个链表再同时每次往后移一步，看节点是否一样，
     * 如果有相等的，说明这个相等的节点就是两链表的交点，
     * 否则如果走完了还没有找到相等的节点，说明他们没有交点
     *
     * @param
     * @return
     */
    private static ListNode crossList2(ListNode headA, ListNode headB) {
        int lenA = length(headA);
        int lenB = length(headB);
        //移动相差位置
        while (lenA != lenB) {
            if (lenA > lenB) {
                lenA--;
                headA = headA.next;
            } else {
                lenB--;
                headB = headB.next;
            }
        }
        //同时起步判断是否相交
        while (headA != headB) {
            headA = headA.next;
            headB = headB.next;
        }
        return headA;
    }

    private static int length(ListNode head) {
        int len = 0;
        while (head != null) {
            head = head.next;
            len++;
        }
        return len;
    }
}
