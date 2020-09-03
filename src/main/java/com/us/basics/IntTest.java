package com.us.basics;

/**
 * Created by yangyibo on 8/17/17.
 */
public class IntTest {
    public static void main(String[] args) {
        compareInteger();
        compareInt();
//        valueString();
    }

    /**
     * Integer 型值 == 比较最大＋－127 Integer.high是128
     * Integer 在-128<=i<=127的时候是直接用的int原始数据类型，而超出了这个范围则是new了一个Integer对象。
     * 我们知道"=="符号在比较对象的时候是比较的内存地址，而对于原始数据类型是直接比对的数据值。
     * equals 为对象比较
     */
    public static void compareInteger() {
        Integer a = 128;
        Integer b = 128;
        System.out.println("compareInteger: a == b: " + (a == b));
        System.out.println("compareInteger: a.equals(b) : " + (a.equals(b)));
    }

    public static void compareInt() {
        int a = 2145456789;
        int b = 2145456789;
        System.out.println("compareInt int : a == b:" + (a == b));
    }

    public static void valueString() {
        String str = "1";
        Integer i = 0;
        try {
            i = Integer.valueOf(str);
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println(i);

    }
}
