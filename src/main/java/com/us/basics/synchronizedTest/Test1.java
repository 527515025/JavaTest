package com.us.basics.synchronizedTest;

/**
 * Created by yangyibo on 17/12/28.
 */
public class Test1 {
    private int count = 0;

    public void add() {
        this.count += 3;
        System.out.println(count);
    }

    public synchronized void subtract() {
        this.count = count - 1;
        System.out.println(Thread.currentThread().getName() + "- subtract1 :" + count);
        try {
            Thread.sleep(4000);
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println(Thread.currentThread().getName() + "- subtract2 :" + count);
    }

    /**
     * 类锁
     */
    public synchronized static void subtract2() {
        int sum = 0;
        sum++;
        System.out.println(Thread.currentThread().getName() + "- subtract2 1 :" + sum);
        try {
            Thread.sleep(4000);
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println(Thread.currentThread().getName() + "- subtract2 2 :" + sum);
    }


    /**
     * 测试可重入
     */
    public synchronized void reentrantTest() {
        System.out.println(Thread.currentThread().getName() + "- begin subtract1 :" + count);
        subtract2();
        System.out.println(Thread.currentThread().getName() + "- end subtract1 :" + count);


    }

}
