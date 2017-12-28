package com.us.basics.synchronizedTest;

/**
 * Created by yangyibo on 17/12/28.
 */
public class Test1 {
    private int count = 0;

    public void add () {
        this.count +=3;
        System.out.println(count);
    }

    synchronized public void subtract () {
        this.count = count - 1;
        System.out.println("subtract1 :"+count);
        try {
            Thread.sleep(4000);
        }catch (Exception e) {
            System.out.println(e);
        }
        System.out.println("subtract2 :"+count);
    }

    /**
     * 类锁
     */
    synchronized public static void subtract2() {
        int sum = 0;
        sum++;
        System.out.println("subtract2 1 :"+sum);
        try {
            Thread.sleep(4000);
        }catch (Exception e) {
            System.out.println(e);
        }
        System.out.println("subtract2 2 :"+sum);
    }
}
