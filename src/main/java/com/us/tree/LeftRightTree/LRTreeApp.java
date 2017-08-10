package com.us.tree.LeftRightTree;

/**
 * Created by yangyibo on 17/7/18.
 */
public class LRTreeApp {
    public static void main(String[] args) {
        LRTree tree = init();
        System.out.println(tree.GetNodeNumKthLevel(tree.getRoot(),7));
    }

    private static LRTree init() {
        LRTree tree = new LRTree();
        tree.insert(8);
        tree.insert(50);
        tree.insert(45);
        tree.insert(21);
        tree.insert(32);
        tree.insert(18);
        tree.insert(37);
        tree.insert(64);
        tree.insert(88);
        tree.insert(5);
        tree.insert(4);
        tree.insert(7);
        return tree;

    }

    private static void selectTree(LRTree tree) {
        System.out.print("－－－－－－－－先续遍历 －－－－－－－－");
        tree.traverse(1);
        System.out.println("");

        System.out.print("－－－－－－－－中续遍历 －－－－－－－－");
        tree.traverse(2);
        System.out.println();

        System.out.print("－－－－－－－－后续遍历－－－－－－－－ ");
        tree.traverse(3);
        System.out.println();

        System.out.print("－－－－－－－－二叉树的节点个数 －－－－－－－－");
        System.out.println(tree.GetNodeNum(tree.getRoot()));

        System.out.println("－－－－－－－－删除节点－－－－－－－－－－");
        System.out.println(tree.delete(7));

        System.out.print("－－－－－－－－先续遍历 －－－－－－－－ ");
        tree.traverse(1);
        System.out.println();

        System.out.print("－－－－－－－－中续遍历－－－－－－－－ ");
        tree.traverse(2);
        System.out.println();

        System.out.print("－－－－－－－－后续遍历 －－－－－－－－ ");
        tree.traverse(3);
        System.out.println();

        System.out.print("－－－－－－－－二叉树的节点个数－－－－－－－－ ");
        System.out.println(tree.GetNodeNum(tree.getRoot()));

        System.out.println("－－－－－－－二叉树的深度－－－－－－－－－");
        System.out.println(tree.GetDepth(tree.getRoot()));

        System.out.println("－－－－－－二叉树第K层的节点个数－－－－－－－－");
        System.out.println(tree.GetNodeNumKthLevel(tree.getRoot(),5));

    }
}
