package com.us.tree.LeftRightTree;

/**
 * Created by yangyibo on 17/7/18.
 * http://www.cnblogs.com/licheng/archive/2010/04/06/1705547.html
 * <p>
 * 树是一种比较重要的数据结构，尤其是二叉树。二叉树是一种特殊的树，在二叉树中每个节点最多有两个子节点，
 * 一般称为左子节点和右子节点（或左孩子和右孩子），并且二叉树的子树有左右之分，其次序不能任意颠倒。二叉树是递归定义的，
 * <p>
 * 因此，与二叉树有关的题目基本都可以用递归思想解决，
 */
public class LRTree {

    //节点对象
    class Node {
        public long value;

        public Node leftChild;

        public Node rightChild;

        public Node(long value) {
            this.value = value;
            leftChild = null;
            rightChild = null;
        }
    }

    public Node root;

    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }

    public LRTree() {
        root = null;
    }

    // 向树中插入一个节点
    public void insert(long value) {
        Node newNode = new Node(value);
        // 树是空的
        if (root == null)
            root = newNode;
        else {
            Node current = root;
            Node parentNode;
            while (true) {
                parentNode = current;
                if (value < current.value) {
                    current = current.leftChild;
                    // 要插入的节点为左孩子节点
                    if (current == null) {
                        parentNode.leftChild = newNode;
                        return;
                    }
                } else {
                    // 要插入的节点为右孩子节点
                    current = current.rightChild;
                    if (current == null) {
                        parentNode.rightChild = newNode;
                        return;
                    }
                }
            }
        }
    }


    /**
     * 依据树节点的值删除树中的一个节点
     */
    public boolean delete(int value) {
        // 遍历树过程中的当前节点
        Node current = root;
        // 要删除节点的父节点
        Node parent = root;
        // 记录树的节点为左孩子节点或右孩子节点
        boolean isLeftChild = true;
        while (current.value != value) {
            parent = current;
            // 要删除的节点在当前节点的左子树里
            if (value < current.value) {
                isLeftChild = true;
                current = current.leftChild;
            }
            // 要删除的节点在当前节点的右子树里
            else {
                isLeftChild = false;
                current = current.rightChild;
            }
            // 在树中没有找到要删除的节点
            if (current == null)
                return false;
        }
        // 要删除的节点为叶子节点
        if (current.leftChild == null && current.rightChild == null) {
            // 要删除的节点为根节点
            if (current == root)
                root = null;
                // 要删除的节点为左孩子节点
            else if (isLeftChild)
                parent.leftChild = null;
                // 要删除的节点为右孩子节点
            else
                parent.rightChild = null;
        }
        // 要删除的节点有左孩子节点，没有右孩子节点
        else if (current.rightChild == null) {
            // 要删除的节点为根节点
            if (current == null)
                root = current.leftChild;
                // 要删除的节点为左孩子节点
            else if (isLeftChild)
                parent.leftChild = current.leftChild;
                // 要删除的节点为右孩子节点
            else
                parent.rightChild = current.leftChild;
        }
        // 要删除的节点没有左孩子节点，有右孩子节点
        else if (current.leftChild == null) {
            // 要删除的节点为根节点
            if (current == root)
                root = root.rightChild;
                // 要删除的节点为左孩子节点
            else if (isLeftChild)
                parent.leftChild = current.rightChild;
                // 要删除的节点为右孩子节点
            else
                parent.rightChild = current.rightChild;
        }
        // 要删除的接节点既有左孩子节点又有右孩子节点
        else {
            Node successor = getSuccessor(current);
            // 要删除的节点为根节点
            if (current == root)
                root = successor;
                // 要删除的节点为左孩子节点
            else if (isLeftChild)
                parent.leftChild = successor;
                // 要删除的节点为右孩子节点
            else
                parent.rightChild = successor;
        }
        return true;
    }

    // 找到要删除节点的替补节点
    private Node getSuccessor(Node delNode) {
        // 替补节点的父节点
        Node successorParent = delNode;
        // 删除节点的替补节点
        Node successor = delNode;
        Node current = delNode.rightChild;
        while (current != null) {
            // successorParent指向当前节点的上一个节点
            successorParent = successor;
            // successor变为当前节点
            successor = current;
            current = current.leftChild;
        }
        // 替补节点的右孩子节点不为空
        if (successor != delNode.rightChild) {
            successorParent.leftChild = successor.rightChild;
            successor.rightChild = delNode.rightChild;
        }
        return successor;
    }


    /**
     * 调度方法
     *
     * @param traverseType
     */
    public void traverse(int traverseType) {
        switch (traverseType) {
            case 1:
                preOrder(root);
                break;
            case 2:
                inOrder(root);
                break;
            case 3:
                postOrder(root);
                break;
            default:
                break;
        }
    }

    /**
     * 先续遍历树中的所有节点
     */
    public void preOrder(Node currentRoot) {
        if (currentRoot != null) {
            //先输出当前节点
            System.out.print(currentRoot.value + " ");
            preOrder(currentRoot.leftChild);
            preOrder(currentRoot.rightChild);
        }
    }

    /**
     * 中续遍历树中的所有节点
     *
     * @param currentNode
     */
    public void inOrder(Node currentNode) {
        if (currentNode != null) {
            inOrder(currentNode.leftChild);
            //左节点输出完 输出当前节点
            System.out.print(currentNode.value + " ");
            inOrder(currentNode.rightChild);
        }
    }

    /**
     * 分层遍历二叉树
     */
    public void LevelTraverse(Node currentNode) {

    }

    /**
     * 后续遍历树中的所有节点
     *
     * @param currentNode
     */
    public void postOrder(Node currentNode) {
        if (currentNode != null) {
            postOrder(currentNode.leftChild);
            postOrder(currentNode.rightChild);
            //最后输出当前节点
            System.out.print(currentNode.value + " ");
        }
    }

    /**
     * 得到二叉树的节点个数
     *
     * @param root
     * @return
     */
    public Integer GetNodeNum(Node root) {
        if (root == null) {
            //递归出口
            return 0;
        }
        return GetNodeNum(root.leftChild) + GetNodeNum(root.rightChild) + 1;
    }


    /**
     * 求二叉树的深度
     * 思考：如果二叉树为空，二叉树的深度为0
     * 如果二叉树不为空，二叉树的深度 = max(左子树深度， 右子树深度) + 1
     */
    public Integer GetDepth(Node root) {
        if (root == null) {
            return 0;
        }
        Integer depthLeft = GetDepth(root.leftChild);
        Integer depthRight = GetDepth(root.rightChild);
        return depthLeft > depthRight ? (depthLeft + 1) : (depthRight + 1);
    }

    /**
     * 求二叉树第K层的节点个数
     * 思考：如果二叉树为空或者k<1返回0
     * 如果二叉树不为空并且k==1，返回1
     * 如果二叉树不为空且k>1，返回左子树中k-1层的节点个数与右子树k-1层节点个数之和
     */
    public Integer GetNodeNumKthLevel(Node root, Integer k) {
        if (root == null || k < 1)
            return 0;
        if (k == 1)
            return 1;
        int numLeft = GetNodeNumKthLevel(root.leftChild, k - 1); // 左子树中k-1层的节点个数 递归左节点，k 为递归层数
        int numRight = GetNodeNumKthLevel(root.rightChild, k - 1); // 右子树中k-1层的节点个数 递归右节点，k 为递归层数
        return (numLeft + numRight);
    }

    /**
     * 求叶子节点个数
     * 思考：叶子节点，左右节点都是null，为叶子节点
     */
    public Integer GetLeafNodeNum(Node root) {
        if (root == null)
            return 0;
        if (root.leftChild == null && root.rightChild == null)
            return 1;
        int numLeft = GetLeafNodeNum(root.leftChild);
        int numRight = GetLeafNodeNum(root.rightChild);
        return (numLeft + numRight);
    }



}
