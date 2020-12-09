package com.us.basics.threadTest;

import java.util.concurrent.*;

/**
 * 设置 countDownLatch 为 3 ，通过 AQS 的state 设置3 ，countDown() 方法减1 ，await()不为0 时阻塞，为0 唤醒
 *
 * 结果：
 * A在上厕所
 * B在上厕所
 * 等待所有人从厕所回来开会...
 * C在上厕所
 * B上完了
 * C上完了
 * A上完了
 * 所有人都好了，开始开会...
 *
 * @author yyb
 * @time 2020/12/8
 */
public class CountDownLatchTest {
    private static int num = 3;
    private static CountDownLatch countDownLatch = new CountDownLatch(num);
    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(num, num,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>());

    public static void main(String[] args) throws Exception {
        threadPoolExecutor.submit(() -> {
            System.out.println("A在上厕所");
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                countDownLatch.countDown();
                System.out.println("A上完了");
            }
        });
        threadPoolExecutor.submit(() -> {
            System.out.println("B在上厕所");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                countDownLatch.countDown();
                System.out.println("B上完了");
            }
        });
        threadPoolExecutor.submit(() -> {
            System.out.println("C在上厕所");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                countDownLatch.countDown();
                System.out.println("C上完了");
            }
        });

        System.out.println("等待所有人从厕所回来开会...");
        countDownLatch.await();
        System.out.println("所有人都好了，开始开会...");
        threadPoolExecutor.shutdown();
        System.out.println("所有人都走了...");

    }
}
