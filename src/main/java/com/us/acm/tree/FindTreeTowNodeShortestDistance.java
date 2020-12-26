package com.us.acm.tree;

import java.util.LinkedList;

/**
 * 求一个二叉树的任意两个节点的距离
 * 距离是指连接两个节点所需要的最小边的条数。
 * https://blog.csdn.net/qq_38251888/article/details/88831919
 * <p>
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
     * 首先需要找到两个结点的 公共祖先 的位置，设公共祖先的位置是 fatherNode.
     * 则 distance(nodeOne,nodeTwo) = distance(root,nodeOne) + distance(root,nodeTwo) - (2 * distance(root,fatherNode));
     * 即 根节点到两节点距离的和 减去 两个根节点到最近父节点到距离
     * <p>
     * <p>
     * 所以第一步需要找到两个结点的公共父节点
     * 实现distance函数，所谓的distance函数就是在计算结点所在的层数。
     *
     * @param root
     * @param nodeOne
     * @param nodeTwo
     * @return
     */
    public static int getTwoTreeNodeDistance(TreeNode root, TreeNode nodeOne, TreeNode nodeTwo) {
        TreeNode fatherNode = findCommonFatherNode(root, nodeOne, nodeTwo);
        int one = distance(root, nodeOne);
        int two = distance(root, nodeTwo);
        int fa = distance(root, fatherNode);
        int x = one + two - 2 * fa;
        return x;
    }

    /**
     * 寻找距离即，广度优先遍历，找到该节点锁在层数，返回 层深 -1 即为距离 边的个数。
     *
     * @param root
     * @param node
     * @return
     */
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
                    return level - 1;
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

    /**
     * 寻找两个节点的父节点
     * 左右子树中找到该节点后，最终会汇合到一个父节点，返回父节点
     *
     * @param root
     * @param nodeOne
     * @param nodeTwo
     * @return
     */
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
            // 左边没找到，右边找到了，说明，right就是公共祖先，两个节都在left
            return right;
        }
        if (right == null) {
            // 右边没找到，左边找到了，说明，left就是公共祖先，两个节点都在left
            return left;
        }
        // 最终汇合节点，两个节点分布左右子树，则该节点为父节点
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

        System.out.println("3 Dist(8,7) = " + getTwoTreeNodeDistance(root, root.right.left.right, root.right.right));

        System.out.println("2 Dist(8,3) = " + getTwoTreeNodeDistance(root, root.right.left.right, root.right));

        System.out.println("4 Dist(8,2) = " + getTwoTreeNodeDistance(root, root.right.left.right, root.left));

        System.out.println("0 Dist(8,8) = " + getTwoTreeNodeDistance(root, root.right.left.right, root.right.left.right));

    }
}
