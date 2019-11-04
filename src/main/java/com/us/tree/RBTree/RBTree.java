package com.us.tree.RBTree;

import java.util.concurrent.atomic.AtomicLong;

/**
 * https://mp.weixin.qq.com/s?__biz=MjM5NjQ5MTI5OA==&mid=2651745738&idx=2&sn=dc7570dfb3b652ec26a9b90d5e965149&chksm=bd12b4878a653d91346b06f2870242708e667725dbbaf273a3eb7cea9c8193115f80463db65f&scene=21#wechat_redirect
 * <p>
 * 平衡二叉查找树，红黑树 Red-Black Tree RBTree在理论上还是一棵BST树，但是它在对BST的插入和删除操作时会维持树的平衡，即保证树的高度在[logN,logN+1]
 * <p>
 * 任何一个节点都有颜色，黑色或者红色
 * 根节点是黑色的
 * 父子节点之间不能出现两个连续的红节点
 * 任何一个节点向下遍历到其子孙的叶子节点，所经过的黑节点个数必须相等
 * 空节点被认为是黑色的
 *
 * @author yyb
 * @time 2019/10/28
 */
public class RBTree<T extends Comparable<T>> {
    private final RBTreeNode<T> root;
    /**
     * 记录节点数， AtomicLong 原子操作线程安全，csa 算法实现乐观锁
     */
    private AtomicLong size = new AtomicLong(0);

    /**
     * 在非覆盖模式下，节点可以有相同的值，但建议不要使用非覆盖模式。
     * true 为覆盖模式，所有节点的值相同则覆盖
     */
    private volatile boolean overrideMode = true;

    public RBTree() {
        this.root = new RBTreeNode<>();
    }

    public RBTree(boolean overrideMode) {
        this();
        this.overrideMode = overrideMode;
    }


    public boolean isOverrideMode() {
        return overrideMode;
    }

    public void setOverrideMode(boolean overrideMode) {
        this.overrideMode = overrideMode;
    }

    /**
     * 获取节点数
     *
     * @return
     */
    public long getSize() {
        return size.get();
    }

    /**
     * 获取根节点，根节点为root 的左子节点
     *
     * @return
     */
    private RBTreeNode<T> getRoot() {
        return root.getLeft();
    }

    /**
     * 添加节点，如果树中存在该值
     *
     * @param value
     * @return
     */
    public T addNode(T value) {
        RBTreeNode<T> t = new RBTreeNode<>(value);
        return addNode(t);
    }

    /**
     * find the value by give value(include key,key used for search,
     * other field is not used,@see compare method).if this value not exist return null
     *
     * @param value
     * @return
     */
    public T find(T value) {
        RBTreeNode<T> dataRoot = getRoot();
        while (dataRoot != null) {
            int cmp = dataRoot.getValue().compareTo(value);
            if (cmp < 0) {
                dataRoot = dataRoot.getRight();
            } else if (cmp > 0) {
                dataRoot = dataRoot.getLeft();
            } else {
                return dataRoot.getValue();
            }
        }
        return null;
    }

    /**
     * 通过value 删除树的节点，如果节点不存在，则返回null
     *
     * @param value 查询的key
     * @return 删除包含值的节点
     */
    public T remove(T value) {
        // 获取根结点
        RBTreeNode<T> dataRoot = getRoot();
        RBTreeNode<T> parent = root;

        while (dataRoot != null) {
            // 从根节点查找 value值
            int cmp = dataRoot.getValue().compareTo(value);
            if (cmp < 0) {
                //如果value 小于根节点值则向左子树查询
                parent = dataRoot;
                dataRoot = dataRoot.getRight();
            } else if (cmp > 0) {
                //如果value 大于根节点值则向左子树查询
                parent = dataRoot;
                dataRoot = dataRoot.getLeft();
            } else {
                //如果value值相等，找到需要删除的节点
                if (dataRoot.getRight() != null) {
                    //查询待删除节点是否有右节点
                    RBTreeNode<T> min = removeMin(dataRoot.getRight());
                    //x used for fix color balance
                    RBTreeNode<T> x = min.getRight() == null ? min.getParent() : min.getRight();
                    boolean isParent = min.getRight() == null;

                    min.setLeft(dataRoot.getLeft());
                    setParent(dataRoot.getLeft(), min);
                    if (parent.getLeft() == dataRoot) {
                        parent.setLeft(min);
                    } else {
                        parent.setRight(min);
                    }
                    setParent(min, parent);

                    boolean curMinIsBlack = min.isBlack();
                    //inherit dataRoot's color
                    min.setRed(dataRoot.isRed());

                    if (min != dataRoot.getRight()) {
                        min.setRight(dataRoot.getRight());
                        setParent(dataRoot.getRight(), min);
                    }
                    //remove a black node,need fix color
                    if (curMinIsBlack) {
                        if (min != dataRoot.getRight()) {
                            fixRemove(x, isParent);
                        } else if (min.getRight() != null) {
                            fixRemove(min.getRight(), false);
                        } else {
                            fixRemove(min, true);
                        }
                    }
                } else {
                    setParent(dataRoot.getLeft(), parent);
                    if (parent.getLeft() == dataRoot) {
                        parent.setLeft(dataRoot.getLeft());
                    } else {
                        parent.setRight(dataRoot.getLeft());
                    }
                    //current node is black and tree is not empty
                    if (dataRoot.isBlack() && !(root.getLeft() == null)) {
                        RBTreeNode<T> x = dataRoot.getLeft() == null
                                ? parent : dataRoot.getLeft();
                        boolean isParent = dataRoot.getLeft() == null;
                        fixRemove(x, isParent);
                    }
                }
                setParent(dataRoot, null);
                dataRoot.setLeft(null);
                dataRoot.setRight(null);
                if (getRoot() != null) {
                    getRoot().setRed(false);
                    getRoot().setParent(null);
                }
                size.decrementAndGet();
                return dataRoot.getValue();
            }
        }
        return null;
    }

    /**
     * fix remove action
     *
     * @param node
     * @param isParent
     */
    private void fixRemove(RBTreeNode<T> node, boolean isParent) {
        RBTreeNode<T> cur = isParent ? null : node;
        boolean isRed = isParent ? false : node.isRed();
        RBTreeNode<T> parent = isParent ? node : node.getParent();

        while (!isRed && !isRoot(cur)) {
            RBTreeNode<T> sibling = getSibling(cur, parent);
            //sibling is not null,due to before remove tree color is balance

            //if cur is a left node
            boolean isLeft = parent.getRight() == sibling;
            if (sibling.isRed() && !isLeft) {
                //case 1
                //cur in right
                parent.makeRed();
                sibling.makeBlack();
                rotateRight(parent);
            } else if (sibling.isRed() && isLeft) {
                //cur in left
                parent.makeRed();
                sibling.makeBlack();
                rotateLeft(parent);
            } else if (isBlack(sibling.getLeft()) && isBlack(sibling.getRight())) {
                //case 2
                sibling.makeRed();
                cur = parent;
                isRed = cur.isRed();
                parent = parent.getParent();
            } else if (isLeft && !isBlack(sibling.getLeft())
                    && isBlack(sibling.getRight())) {
                //case 3
                sibling.makeRed();
                sibling.getLeft().makeBlack();
                rotateRight(sibling);
            } else if (!isLeft && !isBlack(sibling.getRight())
                    && isBlack(sibling.getLeft())) {
                sibling.makeRed();
                sibling.getRight().makeBlack();
                rotateLeft(sibling);
            } else if (isLeft && !isBlack(sibling.getRight())) {
                //case 4
                sibling.setRed(parent.isRed());
                parent.makeBlack();
                sibling.getRight().makeBlack();
                rotateLeft(parent);
                cur = getRoot();
            } else if (!isLeft && !isBlack(sibling.getLeft())) {
                sibling.setRed(parent.isRed());
                parent.makeBlack();
                sibling.getLeft().makeBlack();
                rotateRight(parent);
                cur = getRoot();
            }
        }
        if (isRed) {
            cur.makeBlack();
        }
        if (getRoot() != null) {
            getRoot().setRed(false);
            getRoot().setParent(null);
        }

    }

    /**
     * get sibling node
     *
     * @param node
     * @param parent
     * @return
     */
    private RBTreeNode<T> getSibling(RBTreeNode<T> node, RBTreeNode<T> parent) {
        parent = node == null ? parent : node.getParent();
        if (node == null) {
            return parent.getLeft() == null ? parent.getRight() : parent.getLeft();
        }
        if (node == parent.getLeft()) {
            return parent.getRight();
        } else {
            return parent.getLeft();
        }
    }

    private boolean isBlack(RBTreeNode<T> node) {
        return node == null || node.isBlack();
    }

    private boolean isRoot(RBTreeNode<T> node) {
        return root.getLeft() == node && node.getParent() == null;
    }

    /**
     * 查找后续节点
     *
     * @param node 当前节点的左节点
     * @return
     */
    private RBTreeNode<T> removeMin(RBTreeNode<T> node) {
        //查询最小的节点
        RBTreeNode<T> parent = node;
        while (node != null && node.getLeft() != null) {
            //有左节点，继续向下查询
            parent = node;
            node = node.getLeft();
        }
        //传入节点没有左子节点，传入节点为最小节点时直接返回
        if (parent == node) {
            return node;
        }
        //此时最小节点为node

        //用最小节点的右子节点替换到最小节点的位置
        parent.setLeft(node.getRight());
        //设置最小节点的父节点。为最小节点右子节点的父节点
        setParent(node.getRight(), parent);
        //返回最小节点，此时没有移除最小节点与其右子节点的关系
        return node;
    }


    private T addNode(RBTreeNode<T> node) {
        node.setLeft(null);
        node.setRight(null);
        node.setRed(true);
        setParent(node, null);
        //如果根节点没有值则，node 作为根节点的左节点（即红黑树的根节点）
        if (root.getLeft() == null) {
            root.setLeft(node);
            //root node is black
            node.setRed(false);
            size.incrementAndGet();
        } else {
            //查询父节点
            RBTreeNode<T> x = findParentNode(node);
            int cmp = x.getValue().compareTo(node.getValue());

            //开启复写且父节点值等于新添加的节点值
            if (this.overrideMode && cmp == 0) {
                T v = x.getValue();
                x.setValue(node.getValue());
                return v;
            } else if (cmp == 0) {
                //value exists,ignore this node
                return x.getValue();
            }
            // 插入节点与父节点不相等
            setParent(node, x);

            if (cmp > 0) {
                //小于父节点 设置为左节点
                x.setLeft(node);
            } else {
                //大于父节点 设置为右节点
                x.setRight(node);
            }
            //进行树修复，修改颜色、左旋或右旋
            // 1。叔叔节点也为红色。 2。叔叔节点为空，且祖父节点、父节点和新节点处于一条斜线上。 3。叔叔节点为空，且祖父节点、父节点和新节点不处于一条斜线上。
            fixInsert(node);
            size.incrementAndGet();
        }
        return null;
    }

    /**
     * 查找节点的父节点，从root 节点开始遍历查找
     *
     * @param x
     * @return
     */
    private RBTreeNode<T> findParentNode(RBTreeNode<T> x) {
        RBTreeNode<T> dataRoot = getRoot();
        RBTreeNode<T> child = dataRoot;

        while (child != null) {
            int cmp = child.getValue().compareTo(x.getValue());
            //查到相同同节点
            if (cmp == 0) {
                return child;
            }
            if (cmp > 0) {
                dataRoot = child;
                //继续向左子树查询
                child = child.getLeft();
            } else if (cmp < 0) {
                dataRoot = child;
                //向右子树查询
                child = child.getRight();
            }
        }
        return dataRoot;
    }

    /**
     * 红黑树插入节点调整
     *
     * @param x
     */
    private void fixInsert(RBTreeNode<T> x) {
        RBTreeNode<T> parent = x.getParent();

        while (parent != null && parent.isRed()) {
            RBTreeNode<T> uncle = getUncle(x);
            if (uncle == null) {
                //叔叔为空需要旋转
                RBTreeNode<T> ancestor = parent.getParent();
                // 祖父是不会为null 的，应为之前之前添加的时候 颜色和树已经平衡调整过了
                if (parent == ancestor.getLeft()) {
                    boolean isRight = x == parent.getRight();
                    if (isRight) {
                        //如果父节点是祖父的左子节点，自己是父亲的右子节点 case3 则需要先将自己进行左旋再进行右旋，两步调整
                        rotateLeft(parent);
                    }
                    //如果父节点是祖父的左子节点，自己是父亲的左子节点，case2 则需要将父节点进行右旋
                    rotateRight(ancestor);

                    if (isRight) {
                        //如果是case3 则自身和父节点进行位置交换，所以自己应该设为黑色
                        x.setRed(false);
                        //因为叔叔为空所以树的上层都已经稳定所以结束循环
                        parent = null;
                    } else {
                        //如果是case2  因为此时父节经过旋转，原祖父节点将成为兄弟节点 需要将父亲颜色设置为黑色
                        parent.setRed(false);
                    }
                    //因为uncle 是空所以 旋转后祖父会成为叶子节点，所以祖父应该设置为红色
                    ancestor.setRed(true);
                } else {
                    //父节点是祖父的右节点
                    boolean isLeft = x == parent.getLeft();
                    if (isLeft) {
                        //如果父节点是祖父的右子节点，自己是父亲的左子节点 case3 则需要先将自己进行右旋再进行左旋，两步调整
                        rotateRight(parent);
                    }
                    //如果父节点是祖父的右子节点，自己是父亲的右子节点，case2 则需要将父节点进行左旋
                    rotateLeft(ancestor);

                    if (isLeft) {
                        //如果是case3 则自身和父节点进行位置交换，所以自己应该设为黑色
                        x.setRed(false);
                        //因为叔叔为空所以树的上层都已经稳定所以结束循环
                        parent = null;
                    } else {
                        //如果是case2 因为此时父节经过旋转，原祖父节点将成为兄弟节点 需要将父亲颜色设置为黑色
                        parent.setRed(false);
                    }
                    //因为uncle 是空所以 旋转后祖父会成为叶子节点，所以祖父应该设置为红色
                     ancestor.setRed(true);
                }
            } else {
                //叔叔节点为叶子节点，设置叔叔和父亲为黑色，设置祖父为红色，循环遍历设置到root节点
                parent.setRed(false);
                uncle.setRed(false);
                parent.getParent().setRed(true);
                x = parent.getParent();
                parent = x.getParent();
            }
        }
        //最终 设置根节点为黑色
        getRoot().makeBlack();
        getRoot().setParent(null);
    }

    /**
     * 获取叔叔节点
     *
     * @param node
     * @return
     */
    private RBTreeNode<T> getUncle(RBTreeNode<T> node) {
        RBTreeNode<T> parent = node.getParent();
        RBTreeNode<T> ancestor = parent.getParent();
        if (ancestor == null) {
            return null;
        }
        if (parent == ancestor.getLeft()) {
            return ancestor.getRight();
        } else {
            return ancestor.getLeft();
        }
    }

    /**
     * 左旋，对传入node 的右子节点进行左旋（右子节点只能进行左旋）
     * @param node
     */
    private void rotateLeft(RBTreeNode<T> node) {
        RBTreeNode<T> right = node.getRight();
        if (right == null) {
            throw new java.lang.IllegalStateException("right node is null");
        }
        RBTreeNode<T> parent = node.getParent();
        node.setRight(right.getLeft());
        setParent(right.getLeft(), node);

        right.setLeft(node);
        setParent(node, right);

        if (parent == null) {
            //node pointer to root
            //right  raise to root node
            root.setLeft(right);
            setParent(right, null);
        } else {
            if (parent.getLeft() == node) {
                parent.setLeft(right);
            } else {
                parent.setRight(right);
            }
            //right.setParent(parent);
            setParent(right, parent);
        }
    }

    /**
     * 右旋 对传入node 的左子节点进行右旋，（左子节点只能进行右旋）
     * @param node
     */
    private void rotateRight(RBTreeNode<T> node) {
        RBTreeNode<T> left = node.getLeft();
        if (left == null) {
            throw new java.lang.IllegalStateException("left node is null");
        }
        RBTreeNode<T> parent = node.getParent();
        node.setLeft(left.getRight());
        setParent(left.getRight(), node);

        left.setRight(node);
        setParent(node, left);

        if (parent == null) {
            root.setLeft(left);
            setParent(left, null);
        } else {
            if (parent.getLeft() == node) {
                parent.setLeft(left);
            } else {
                parent.setRight(left);
            }
            setParent(left, parent);
        }
    }

    /**
     * 设置父节点
     * 如果当前节点不为空，则设置父节点为传入父节点，如果传入父节点为root， 则设置父节点为null。
     * @param node
     * @param parent
     */
    private void setParent(RBTreeNode<T> node, RBTreeNode<T> parent) {
        if (node != null) {
            node.setParent(parent);
            if (parent == root) {
                node.setParent(null);
            }
        }
    }

    /**
     * debug method,it used print the given node and its children nodes,
     * every layer output in one line
     *
     * @param root
     */
    public void printTree(RBTreeNode<T> root) {
        java.util.LinkedList<RBTreeNode<T>> queue = new java.util.LinkedList<RBTreeNode<T>>();
        java.util.LinkedList<RBTreeNode<T>> queue2 = new java.util.LinkedList<RBTreeNode<T>>();
        if (root == null) {
            return;
        }
        queue.add(root);
        boolean firstQueue = true;

        while (!queue.isEmpty() || !queue2.isEmpty()) {
            java.util.LinkedList<RBTreeNode<T>> q = firstQueue ? queue : queue2;
            RBTreeNode<T> n = q.poll();

            if (n != null) {
                String pos = n.getParent() == null ? "" : (n == n.getParent().getLeft()
                        ? " LE" : " RI");
                String pstr = n.getParent() == null ? "" : n.getParent().toString();
                String cstr = n.isRed() ? "R" : "B";
                cstr = n.getParent() == null ? cstr : cstr + " ";
                System.out.print(n + "(" + (cstr) + pstr + (pos) + ")" + "\t");
                if (n.getLeft() != null) {
                    (firstQueue ? queue2 : queue).add(n.getLeft());
                }
                if (n.getRight() != null) {
                    (firstQueue ? queue2 : queue).add(n.getRight());
                }
            } else {
                System.out.println();
                firstQueue = !firstQueue;
            }
        }
    }


    public static void main(String[] args) {
        RBTree<Integer> bst = new RBTree<>();
//        bst.addNode(10);
//        bst.addNode(5);
//        bst.addNode(13);
//        bst.addNode(7);
//        bst.addNode(11);
//        bst.addNode(45);
//        bst.addNode(23);
//        bst.addNode(21);
//        bst.addNode(19);
//        bst.addNode(22);


//        bst.addNode(10);
//        bst.addNode(5);
//        bst.addNode(10);



//        delete case2
        bst.addNode(10);
        bst.addNode(7);
        bst.addNode(15);
        bst.addNode(12);
        bst.addNode(17);


        bst.remove(7);

        bst.printTree(bst.getRoot());
    }
}
