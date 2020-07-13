package com.us.acm;

import com.us.tree.LeftRightTree.LRTree;
import com.us.tree.LeftRightTree.LRTreeApp;

/**
 * 力扣
 * 1。 给定一个非空特殊的二叉树，每个节点都是正数，并且每个节点的子节点数量只能为 2 或 0。
 * 2。如果一个节点有两个子节点的话，那么该节点的值等于两个子节点中较小的一个。
 * <p>
 * 给出这样的一个二叉树，你需要输出所有节点中的第二小的值。如果第二小的值不存在的话，输出 -1 。
 *
 * 因为条件1 所以可以分为两种情况
 * 如果左右子树最小值都大于根节点的值取较小的值，问题可以转化为求左右子树的最小值，
 * 其他情况取左右子树较大的值。
 *
 * @author yyb
 * @time 2020/7/10
 */
public class FindTreeSecondMixValue {

    public static void main(String[] args) {
        LRTree tree = LRTreeApp.init();
        long second = getSecondMin(tree.getRoot(), tree.getRoot().value);
        System.out.println(second);

    }

    private static long getSecondMin(LRTree.Node root, long val) {
        if (root == null) {
            return -1L;
        }
        if (root.value > val) {
            return root.value;
        }
        long l = getSecondMin(root.leftChild, val);
        long r = getSecondMin(root.rightChild, val);
        if (l > val && r > val) {
            return Math.min(l, r);
        }
        return Math.max(l, r);
    }


}
