package com.us.basics.threadTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

/**
 * 方案一 篱栅 、回环屏障
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
     * 方案2 信号灯
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
        private Semaphore semaphore2 = new Semaphore(0 );


        public void foo(Runnable printFoo) throws InterruptedException {

            for (int i = 0; i < n; i++) {
                // printFoo.run() outputs "foo". Do not change or remove this line.
                //判断semaphore信号量是否存在
                semaphore.acquire();
                printFoo.run();
                //释放1个semaphore信号量
                semaphore2.release();
            }
        }

        public void bar(Runnable printBar) throws InterruptedException {

            for (int i = 0; i < n; i++) {

                // printBar.run() outputs "bar". Do not change or remove this line.
                semaphore2.acquire();
                printBar.run();
                semaphore.release();
            }
        }
    }
}
