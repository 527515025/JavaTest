package com.us.basics.threadTest;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author yyb
 * @time 2020/12/17
 */
public class ABCCAS {

    private static AtomicInteger flag = new AtomicInteger(0);

    static class ThreadA extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                if (flag.get() % 3 != 0) {
                    Thread.yield();
                }
                System.out.print("A");
                flag.incrementAndGet();
            }
        }
    }

    static class ThreadB extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                if (flag.get() % 3 != 1) {
                    Thread.yield();
                }
                System.out.print("B");
                flag.incrementAndGet();
            }
        }
    }

    static class ThreadC extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                if (flag.get() % 3 != 2) {
                    Thread.yield();
                }
                System.out.print("C");
                flag.incrementAndGet();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new ABCCondition.ThreadA().start();
        new ABCCondition.ThreadB().start();
        new ABCCondition.ThreadC().start();
    }
}
