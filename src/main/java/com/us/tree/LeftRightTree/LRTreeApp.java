package com.us.tree.LeftRightTree;

/**
 * Created by yangyibo on 17/7/18.
 */
public class LRTreeApp {
    public static void main(String[] args) {
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

        System.out.print("PreOrder : ");
        tree.traverse(1);
        System.out.println("");

        System.out.print("InOrder : ");
        tree.traverse(2);
        System.out.println();

        System.out.print("PostOrder : ");
        tree.traverse(3);
        System.out.println();

        System.out.println(tree.delete(7));

        System.out.print("PreOrder : ");
        tree.traverse(1);
        System.out.println();

        System.out.print("InOrder : ");
        tree.traverse(2);
        System.out.println();

        System.out.print("PostOrder : ");
        tree.traverse(3);
        System.out.println();

    }
}
