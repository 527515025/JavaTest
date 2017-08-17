package com.us.demo;

/**
 * Created by yangyibo on 8/17/17.
 */
public class IntTest {
    public static void main(String[] args) {
        compare();
    }

    public static void compare() {
        Integer a = 2015;
        Integer b = 2015;
        if (a == b) {
            System.out.println("a == b: true");
        }
        if (a.equals(b)) {
            System.out.println("a.equals(b) : true");
        }
    }
}
