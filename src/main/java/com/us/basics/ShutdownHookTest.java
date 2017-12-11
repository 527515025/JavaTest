package com.us.basics;

/**
 * Created by yangyibo on 17/10/24.
 */
public class ShutdownHookTest {
    public static void main(String[] args) {


    }

    /**
     * 钩子函数
     */
    private static void shutdownHookMethod() {
        // 定义关闭线程
        Thread shutdownThread = new Thread() {
            public void run() {
                System.out.println("shutdownThread...");
            }
        };
        // jvm关闭的时候先执行该线程钩子
        Runtime.getRuntime().addShutdownHook(shutdownThread);
    }

    /**
     * 线程测试
     */
    private static void threadTest() {
        // 定义线程1
        Thread thread1 = new Thread() {
            public void run() {
                try {
                    sleep(3000);
                    System.out.println("thread1...");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        // 定义线程2
        Thread thread2 = new Thread() {
            public void run() {
                try {
                    sleep(4000);
                    System.out.println("thread2...");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread1.start();
        thread2.start();
    }
}
