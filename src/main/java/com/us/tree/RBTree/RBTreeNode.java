package com.us.tree.RBTree;

/**
 * 红黑树数据对象
 *
 * @author yyb
 * @time 2019/10/28
 */
public class RBTreeNode<T extends Comparable<T>> {
    /**
     * 值
     */
    private T value;
    /**
     * 左节点
     */
    private RBTreeNode<T> left;
    /**
     * 右节点
     */
    private RBTreeNode<T> right;
    /**
     * 父节点
     */
    private RBTreeNode<T> parent;
    /**
     * 颜色，红色
     */
    private boolean red;

    public RBTreeNode() {
    }

    public RBTreeNode(T value) {
        this.value = value;
    }

    public RBTreeNode(T value, boolean isRed) {
        this.value = value;
        this.red = isRed;
    }

    public T getValue() {
        return value;
    }

    void setValue(T value) {
        this.value = value;
    }

    RBTreeNode<T> getLeft() {
        return left;
    }

    void setLeft(RBTreeNode<T> left) {
        this.left = left;
    }

    RBTreeNode<T> getRight() {
        return right;
    }

    void setRight(RBTreeNode<T> right) {
        this.right = right;
    }

    RBTreeNode<T> getParent() {
        return parent;
    }

    void setParent(RBTreeNode<T> parent) {
        this.parent = parent;
    }

    boolean isRed() {
        return red;
    }

    boolean isBlack() {
        return !red;
    }

    /**
     * 是叶子节点
     **/
    boolean isLeaf() {
        return left == null && right == null;
    }

    void setRed(boolean red) {
        this.red = red;
    }

    void makeRed() {
        red = true;
    }

    void makeBlack() {
        red = false;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}