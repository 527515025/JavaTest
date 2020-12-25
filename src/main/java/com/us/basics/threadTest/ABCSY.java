package com.us.basics.threadTest;


/**
 * @author yyb
 * @time 2020/12/17
 */
public class ABCSY {
    private static Object A = new Object();
    private static Object B = new Object();
    private static Object C = new Object();

    private static int count = 0;

    static class ThreadA extends Thread {
        @Override
        public void run() {
            try {
                for (int i = 0; i < 10; i++) {
//                    synchronized (A) {
                        while (count % 3 != 0) {
                            A.wait();
                        }
                        System.out.print("A");
                        count++;
                        B.notify();
//                    }

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class ThreadB extends Thread {
        @Override
        public void run() {
            try {
                for (int i = 0; i < 10; i++) {
//                    synchronized (B) {
                        while (count % 3 != 0) {
                            B.wait();
                        }
                        System.out.print("B");
                        count++;
                        C.notify();
//                    }

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
            }
        }
    }

    static class ThreadC extends Thread {
        @Override
        public void run() {
            try {
                for (int i = 0; i < 10; i++) {
//                    synchronized (C) {
                        while (count % 3 != 0) {
                            C.wait();
                        }
                        System.out.print("C");
                        count++;
                        A.notify();
//                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new ABCCondition.ThreadA().start();
        new ABCCondition.ThreadB().start();
        new ABCCondition.ThreadC().start();
    }
}
