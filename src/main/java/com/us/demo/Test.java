package com.us.demo;

/**
 * Created by yangyibo on 17/1/4.
 */
public class Test {
    public static void main(String[] args) {
//        string();
//        m(9);
        replace();
    }


    static void replace() {
        String str = "yang.abel";
        str= str.replace("c","b");
        System.out.println(str+"---------");
    }

    static void string() {
        String name = "ROLE_EVENT_ALL";
        while (name.endsWith("ALL")) {
            System.out.println("true");
        }
    }

    static void m(Integer i) {
        if (i > 0) {
            System.out.println("---------------" + i);
            m(i - 1); //只执行到递归调用处。
        }
        System.out.println("++++++++++++++++" + i);
    }


}
