package com.us.acm.tree;

import java.util.LinkedList;

/**
 * 求一个二叉树的任意两个节点的距离
 * https://blog.csdn.net/qq_38251888/article/details/88831919
 *
 * java
 * https://zhuanlan.zhihu.com/p/82754383
 * <p>
 * Dist(n1,n2)=Dist(root,n1)+Dist(root,n2)-2*Dist(root,lca)
 *
 * @author yyb
 * @time 2020/12/26
 */
public class FindTreeTowNodeShortestDistance {

    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int val) {
            this.val = val;
        }
    }


    /**
     * 返回两个结点之间的距离通过公共父节点
     * 首先需要找到两个结点的公共祖先的位置，设公共祖先的位置是fatherNode.
     * 则 distence(nodeOne,nodeTwo) = distence(root,nodeOne) + distence(root,nodeTwo) - 2 * distence(root,fatherNode);
     * 所以第一步需要找到两个结点的公共父节点
     * 实现distence函数，所谓的distence函数就是在计算结点所在的层数。
     *
     * @param root
     * @param nodeOne
     * @param nodeTwo
     * @return
     */
    public static int getTwoTreeNodeDistence(TreeNode root, TreeNode nodeOne, TreeNode nodeTwo) {
        TreeNode fatherNode = findCommonFatherNode(root, nodeOne, nodeTwo);
        int x = distance(root, nodeOne) + distance(root, nodeTwo) - 2 * distance(root, fatherNode);
        return x;
    }

    private static int distance(TreeNode root, TreeNode node) {
        // 找到node所在的层数
        int level = 0;
        LinkedList<TreeNode> queue = new LinkedList<>();
        queue.addLast(root);
        while (queue.size() > 0) {
            int size = queue.size();
            level++;
            for (int i = 0; i < size; i++) {
                TreeNode tempNode = queue.removeFirst();
                if (tempNode == node) {
                    return level;
                }
                if (tempNode.left != null) {
                    queue.addLast(tempNode.left);
                }
                if (tempNode.right != null) {
                    queue.addLast(tempNode.right);
                }
            }
        }
        return -1;
    }

    private static TreeNode findCommonFatherNode(TreeNode root, TreeNode nodeOne, TreeNode nodeTwo) {
        // 递归寻找公共父节点
        if (root == null || root == nodeOne || root == nodeTwo) {
            return root;
        }
        // 去root的左子树寻找
        TreeNode left = findCommonFatherNode(root.left, nodeOne, nodeTwo);
        // 去root的右子树寻找
        TreeNode right = findCommonFatherNode(root.right, nodeOne, nodeTwo);
        if (left == null && right == null) {
            // 都没有找到，返回null，说明没有公共祖先
            return null;
        }
        if (left == null) {
            // 左边没找到，右边找到了，说明，right就是公共祖先
            return right;
        }
        if (right == null) {
            return left;
        }
        return root;
    }


    public static void main(String[] args) {

        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.left.left = new TreeNode(4);
        root.left.right = new TreeNode(5);
        root.right.left = new TreeNode(6);
        root.right.right = new TreeNode(7);
        root.right.left.right = new TreeNode(8);
        System.out.println("Dist(8,7) = " + getTwoTreeNodeDistence(root, root.right.left.right, root.right.right));

        System.out.println("Dist(8,3) = " + getTwoTreeNodeDistence(root, root.right.left.right, root.right));

        System.out.println("Dist(8,2) = " + getTwoTreeNodeDistence(root, root.right.left.right, root.left));
    }
}
