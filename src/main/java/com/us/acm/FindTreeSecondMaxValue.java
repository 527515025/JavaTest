package com.us.acm;

import com.us.tree.LeftRightTree.LRTree;
import com.us.tree.LeftRightTree.LRTreeApp;

/**
 * 阿里云
 * 给定一个二叉搜索树，找出其第二大的数。
 * 对于二叉搜索树，若它的左子树不空，则左子树上所有结点的值均小于它的根结点的值； 若它的右子树不空，则右子树上所有结点的值均大于它的根结点的值
 *
 * 思路：遍历整棵树
 *
 * @author yyb
 * @time 2020/7/10
 */
public class FindTreeSecondMaxValue {
    static long max = 0;
    static long second = 0;

    public static void main(String[] args) {
        LRTree tree = LRTreeApp.init();
        preOrder(tree.getRoot());
        System.out.println(max + "---------" + second);

    }

    private static void preOrder(LRTree.Node root) {
        if (root == null) {
            return;
        }
        if (root.value > max) {
            second = max;
            max = root.value;
        }
        if (root.value < max && root.value >= second) {
            second = root.value;
        }
        preOrder(root.leftChild);
        preOrder(root.rightChild);
    }

}




