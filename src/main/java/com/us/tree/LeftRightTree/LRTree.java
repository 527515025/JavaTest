package com.us.tree.LeftRightTree;

import java.util.*;

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
    public class Node {
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
     * 先续遍历树中的所有节点, 最先输出跟节点， 先输出当前节点，然后输出左子树，然后输出右子树
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
     * 中续遍历树中的所有节点 ，中间输出跟节点 。先左节点， 中间节点，右节点
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
     * 后续遍历树中的所有节点 最后输出跟节点，先左，右，后中
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

    /**
     * 深度优先遍历
     * 二叉树
     *
     * @param root
     */
    void dfs(Node root) {
        if (root == null) {
            return;
        }
        System.out.println(root.value);
        dfs(root.leftChild);
        dfs(root.rightChild);
    }

    /**
     * 二叉树 广度优先
     *
     * @param root
     */
    public void bfs(Node root) {
        Queue<Node> queue = new ArrayDeque<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            Node node = queue.poll();
            System.out.println(node.value);
            if (node.leftChild != null) {
                queue.add(node.leftChild);
            }
            if (node.rightChild != null) {
                queue.add(node.rightChild);
            }
        }
    }


    /**
     * 可用队列替换
     */
    private List<List<Long>> levels = new ArrayList<>();

    /**
     * 深度优先
     * 递归 分层遍历二叉树
     * 结点的层次：从根结点开始，假设根结点为第1层，根结点的子节点为第2层，依此类推，如果某一个结点位于第L层，则其子节点位于第L+1层 [5]  。
     * <p>
     * 时间复杂度 oN
     * 空间复杂度ON
     */
    public List<List<Long>> levelTraverses(Node currentNode) {
        levelTraversRecursion(currentNode, 0);
        //输出
        levels.forEach(x -> {
            System.out.println("--------------------");
            x.forEach(y -> {
                System.out.print(y + " ");
            });
            System.out.println();
        });
        return levels;
    }

    /**
     * 分层遍历二叉树 递归
     */
    public void levelTraversRecursion(Node currentNode, int level) {
        if (currentNode == null) {
            return;
        }
        if (levels.size() == level) {
            levels.add(new ArrayList<>());
        }
        levels.get(level).add(currentNode.value);
        levelTraversRecursion(currentNode.leftChild, level + 1);
        levelTraversRecursion(currentNode.rightChild, level + 1);
    }

    //出现层次识别可以使用队列 模式识别

    /**
     * 二叉树 广度优先 改造的分层遍历
     * 时间复杂度 oN
     * 空间复杂度ON
     * new Node(111111111) 为层次的分界符号
     * <p>
     * 大致思想， 广度优先优先遍历，将每层的节点放入队列，然后出列，判断是否为分界符。遍历其子节点
     * 如果为分界符，则判断队列中是否还有元素，如果有则放入新的分界符，输出打印时跳过分界符号。
     *
     * @param root
     */
    public void bfsLevelTravers(Node root) {
        Queue<Node> queue = new ArrayDeque<>();
        queue.add(root);
        queue.add(new Node(111111111));
        while (!queue.isEmpty()) {
            for (int i = 0; i < queue.size(); i++) {
                Node node = queue.poll();
                if (node.value == 111111111) {
                    System.out.println("--------------------------");
                    if (!queue.isEmpty()) {
                        queue.add(new Node(111111111));
                    }
                } else {
                    System.out.println(node.value);
                }
                if (node.leftChild != null) {
                    queue.add(node.leftChild);
                }
                if (node.rightChild != null) {
                    queue.add(node.rightChild);
                }
            }
        }
    }

    /**
     * 广度优先 bfs
     * 蛇形遍历
     * 二叉树的锯齿形层次遍历 ，（即先从左往右，再从右往左进行下一层遍历，以此类推，层与层之间交替进行）
     * <p>
     * 双端队列， null 为分界值，和分层遍历思维一致，只是在每层参数放置时，放入双端队列也就是链表
     * <p>
     * 时间复杂度：O(N)
     * 空间复杂度：O(N)
     *
     *
     * ArrayList 是数组 所以 add（0，e） 是在内存中System.arrayCopy 拷贝一个新的数组，并将后面的所有元素位置+1
     * LinkedList 是链表
     * @param root
     */
    public List<List<Long>> snakeTraversal(Node root) {
        if (root == null) {
            return new ArrayList<>();
        }
        List<List<Long>> results = new ArrayList<>();
        List<Long> levelList = new ArrayList<>();
        //遍历方向
        boolean forward = true;
        //双端队列 维护节点信息
        LinkedList<Node> queue = new LinkedList<>();
        queue.add(root);
        //分界
        queue.add(null);
        while (queue.size() > 0) {
            Node currNode = queue.pollFirst();
            if (currNode != null) {
                if (forward) {
                    levelList.add(currNode.value);
                } else {
                    levelList.add(0, currNode.value);
                }
                if (currNode.leftChild != null) {
                    queue.add(currNode.leftChild);
                }
                if (currNode.rightChild != null) {
                    queue.add(currNode.rightChild);
                }
            } else {
                //为分界节点
                results.add(levelList);
                forward = !forward;
                //清空本层
                levelList = new LinkedList<>();
                if (queue.size() > 0) {
                    queue.addLast(null);
                }
            }
        }
        return results;
    }


    /**
     * 深度优先
     * 递归 蛇形遍历二叉树  考虑双端队列
     * <p>
     * <p>
     * 结点的层次：从根结点开始，假设根结点为第1层，根结点的子节点为第2层，依此类推，如果某一个结点位于第L层，则其子节点位于第L+1层 [5]
     *
     * 递归遍历每层节点，然后将节点和节点的层级信息存储起来，获取每层的节点信息。根据奇数层反转，偶数层不反转即可
     * <p>
     * <p>
     * <p>
     * 时间复杂度 oN
     * 空间复杂度 O(H)，其中 HH 是树的高度。例如：包含 NN 个节点的树，高度大约为 log_2{N}
     * 与 BFS 不同，在 DFS 中不需要维护双端队列。方法递归调用会产生额外的内存消耗。
     * 方法 DFS(node, level) 的调用堆栈大小刚好等于节点所在层数。
     * 因此 DFS 的空间复杂度为 O(log2N)，这比 BFS 好很多。
     */


    private List<List<Long>> snakeTravers = new ArrayList<>();

    public List<List<Long>> dfsSnakeTraversal(Node currentNode) {
        dfsLevelTraversRecursion(currentNode, 0);
        return snakeTravers;
    }

    /**
     * 分层蛇形遍历二叉树 递归
     */
    public void dfsLevelTraversRecursion(Node currentNode, int level) {
        if (currentNode == null) {
            return;
        }
        if (snakeTravers.size() == level) {
            snakeTravers.add(new ArrayList<>());
        }
        if (level % 2 > 0) {
            snakeTravers.get(level).add(0, currentNode.value);
        } else {
            snakeTravers.get(level).add(currentNode.value);
        }
        dfsLevelTraversRecursion(currentNode.leftChild, level + 1);
        dfsLevelTraversRecursion(currentNode.rightChild, level + 1);
    }
}
