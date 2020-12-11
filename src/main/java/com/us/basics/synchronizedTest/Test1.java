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

    public synchronized void objectLock() {
        this.count = count - 1;
        System.out.println(Thread.currentThread().getName() + "- objectLock 1:");
        try {
            Thread.sleep(4000);
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println(Thread.currentThread().getName() + "- objectLock 2:");
    }

    /**
     * 类锁
     */
    public static synchronized void classLock() {
        System.out.println(Thread.currentThread().getName() + "- classLock  1:");
        try {
            Thread.sleep(4000);
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println(Thread.currentThread().getName() + "- classLock  2:");
    }


    /**
     * 测试可重入
     */
    public synchronized void reentrantTest() {
        System.out.println(Thread.currentThread().getName() + "- begin reentrantTest :" + count);
        classLock();
        System.out.println(Thread.currentThread().getName() + "- end reentrantTest :" + count);


    }

}
