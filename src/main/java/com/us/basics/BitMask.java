package com.us.basics;

/**
 * Created by yangyibo on 17/12/11.
 * 在Java中，位运算符有很多，例如与(&)、非(~)、或(|)、异或(^)、移位(<<和>>)等
 * <p>
 * <<      :     左移运算符，num << 1,相当于num乘以2
 * >>      :     右移运算符，num >> 1,相当于num除以2
 * >>>    :     无符号右移，忽略符号位，空位都以0补齐
 * 异或运算符 ^ 一句话，相异为真 返回 boolean
 * <p>
 * 0001 | 0100，也就是0101 或只要两位有一位 为真则为真 ，便拥有了Select和Update两项权限。
 * 0001 ~ 0100  也就是 1010 只有两位都为 0 则为真。
 * 0001 & 0101  也就是 0001 只有两位都为 1 则为真
 * <p>
 * <p>
 * <p>
 * 使用位掩码的方式，只需要用一个大于或等于0且小于16的整数即可表示所有的16种权限的状态。
 */
public class BitMask {
    /**
     * 1*2的0次方 的二进制 0001
     */
    public static int ADD = 1 << 0;
    /**
     * 1*2的1次方 的二进制 0010
     */
    public static int DELETE = 1 << 1;
    /**
     * 1*2的2次方 的二进制 0100
     */
    public static int UPDATE = 1 << 2;
    /**
     * 1*2的3次方 的二进制 1000
     */
    public static int SELECT = 1 << 3;

    // 当前状态
    private int currentStatus;

    BitMask(int currentStatus) {
        this.currentStatus = currentStatus;
    }


    /**
     * 添加某个操作权限
     *
     * @param more
     * @return
     */
    private BitMask append(int more) {
        currentStatus = currentStatus | more;
        return this;
    }

    /**
     * 除去某个操作权限
     *
     * @param more
     * @return
     */
    private BitMask delete(int more) {
//        currentStatus = currentStatus - more;
        currentStatus &= ~more;
        return this;
    }

    /**
     * 是否拥有某个权限
     *
     * @param more
     * @return
     */
    private boolean isPermission(int more) {
        return (currentStatus & more) > 0;
    }


    public static void main(String[] args) {
        BitMask bk = new BitMask(BitMask.DELETE);
        //添加权限
        bk.append(BitMask.ADD).append(BitMask.UPDATE);
        bk.delete(BitMask.ADD);
        // 判断是否有 ADD 操作权限
        System.out.println(bk.isPermission(BitMask.ADD));
        test1();
        test2();
        test3();
    }

    /**
     * 测试与
     */
    public static void test1() {
        //0101
        int a = 5;
        //0110
        int b = 6;
        //输出为 0100 为 4
        System.out.println(a & b);

    }

    /**
     * 测试或
     */
    public static void test2() {
        //0101
        int a = 5;
        //0110
        int b = 6;
        //输出为 0111 为 7
        System.out.println(a | b);

    }

    /**
     * 测试非 可以理解为从 a 中减去 b
     * ~5 取 5的补码，正数的补码 0101 ，为绝对值，其余位补零。然后取反 1010 ，最高位为 1 则为负数，然后取其补码，负数的补码是其绝对值的原码取反 0101 ，然后末尾+1 0110 所以为－6
     * 非操作比较绕，可以理解为绝对值＋1 并取负数
     */
    public static void test3() {
        //0101
        int a = 5;
        //输出为 －6
        System.out.println(~a);
        System.out.println(" ~a == -6 is " + (~a == -6));
    }

}

