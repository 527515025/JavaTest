package com.us.basics.threadTest;


import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 方案一 篱栅 、回环屏障 ，感觉最简单的方法
 * <p>
 * 多线程交替打印
 * https://leetcode-cn.com/problems/print-foobar-alternately/
 *
 * @author yyb
 * @time 2020/11/19
 */
public class FooBar {
    private int n;

    public FooBar(int n) {
        this.n = n;
    }

    private static final CyclicBarrier c = new CyclicBarrier(2);

    public void foo(Runnable printFoo) throws InterruptedException {

        for (int i = 0; i < n; i++) {

            // printFoo.run() outputs "foo". Do not change or remove this line.
            printFoo.run();
            try {
                c.await();
            } catch (Exception e) {

            }

        }
    }

    public void bar(Runnable printBar) throws InterruptedException {

        for (int i = 0; i < n; i++) {

            // printBar.run() outputs "bar". Do not change or remove this line.
            printBar.run();
            try {
                c.await();
            } catch (Exception e) {

            }
        }
    }


    /**
     * 方案2 信号灯，相对简单
     */
    class FooBar2 {
        private int n;

        public FooBar2(int n) {
            this.n = n;
        }

        /**
         * semaphore初始化为1个信号量
         */
        private Semaphore semaphore = new Semaphore(1);
        /**
         * semaphore初始化为0个信号量
         */
        private Semaphore semaphore2 = new Semaphore(0);


        public void foo(Runnable printFoo) throws InterruptedException {

            for (int i = 0; i < n; i++) {
                //判断semaphore信号量是否存在
                semaphore.acquire();
                printFoo.run();
                //释放1个semaphore信号量
                semaphore2.release();
            }
        }

        public void bar(Runnable printBar) throws InterruptedException {

            for (int i = 0; i < n; i++) {

                //判断semaphore信号量是否存在
                semaphore2.acquire();
                printBar.run();
                //释放1个semaphore信号量
                semaphore.release();
            }
        }
    }


    /**
     * 方案3 ReentrantLock
     */
    class FooBar3 {
        private int n;

        public FooBar3(int n) {
            this.n = n;
        }

        ReentrantLock lock = new ReentrantLock(true);
        Condition c = lock.newCondition();
        volatile boolean flag = true;

        public void foo(Runnable printFoo) throws InterruptedException {

            for (int i = 0; i < n; i++) {
                lock.lock();
                try {
                    if (!flag) {
                        c.await();
                    }
                    printFoo.run();
                    flag = false;
                    c.signal();
                } finally {
                    lock.unlock();
                }
            }
        }

        public void bar(Runnable printBar) throws InterruptedException {
            for (int i = 0; i < n; i++) {
                lock.lock();
                try {
                    if (flag) {
                        c.await();
                    }
                    printBar.run();
                    flag = true;
                    c.signal();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    /**
     * 方案4 与方案三同理 synchronized
     */
    class FooBar4 {
        private int n;

        public FooBar4(int n) {
            this.n = n;
        }

        volatile boolean flag = true;
        private final Object lock = new Object();

        public void foo(Runnable printFoo) throws InterruptedException {
            for (int i = 0; i < n; i++) {
                synchronized (lock) {
                    //首次执行放行
                    if (!flag) {
                        //当前线程挂起
                        lock.wait();
                    }
                    flag = false;
                    printFoo.run();
                    lock.notifyAll();
                }
            }
        }

        public void bar(Runnable printBar) throws InterruptedException {
            for (int i = 0; i < n; i++) {
                synchronized (lock) {
                    if (flag) {
                        //当前线程挂起
                        lock.wait();
                    }
                    printBar.run();
                    flag = true;
                    lock.notifyAll();
                }
            }
        }
    }

    /**
     * 方案5 无锁方案 cas
     */
    class FooBar5 {
        private int n;

        public FooBar5(int n) {
            this.n = n;
        }
        AtomicBoolean flag = new AtomicBoolean(true);
        public void foo(Runnable printFoo) throws InterruptedException {
            for (int i = 0; i < n; i++) {
                while (!flag.get()) {
                    Thread.yield();
                }
                printFoo.run();
                flag.compareAndSet(true, false);
            }
        }

        public void bar(Runnable printBar) throws InterruptedException {
            for (int i = 0; i < n; i++) {
                while (flag.get()) {
                    Thread.yield();
                }
                printBar.run();
                flag.compareAndSet(false, true);
            }
        }
    }
}
