package com.us.basics.threadTest;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 主线程不阻塞等待
 * CountDownLatch非常相似，初始化传入3个线程和一个任务，线程调用await()之后进入阻塞，计数器-1，当计数器为0时，
 * 就去执行CyclicBarrier中构造函数的任务，当任务执行完毕后，唤醒所有阻塞中的线程。
 * 这验证了CyclicBarrier让一组线程全部达到一个状态之后再全部同时执行的效果。
 *
 * CyclicBarrier 是通过 ReentrantLock加锁，控制count属性的加减计数的。
 *
 *
 * 结果：
 * A在上厕所
 * B在上厕所
 * C在上厕所
 * B上完了
 * C上完了
 * Disconnected from the target VM, address: '127.0.0.1:57153', transport: 'socket'
 * A上完了
 * 所有人都好了，开始开会...
 * -------------------
 * 会议结束，A退出
 * 会议结束，B退出
 * 会议结束，C退出
 * <p>
 * Process finished with exit code 0
 * @author yyb
 * @time 2020/12/8
 */
public class CyclicBarrierTest {
    private static int num = 3;
    private static CyclicBarrier cyclicBarrier = new CyclicBarrier(num, () -> {
        System.out.println("所有人都好了，开始开会...");
        System.out.println("-------------------");
    });
    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(num, num,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>());


    public static void main(String[] args) throws Exception {
        threadPoolExecutor.submit(() -> {
            System.out.println("A在上厕所");
            try {
                Thread.sleep(4000);
                System.out.println("A上完了");
                cyclicBarrier.await();
                System.out.println("会议结束，A退出");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {

            }
        });
        threadPoolExecutor.submit(() -> {
            System.out.println("B在上厕所");
            try {
                Thread.sleep(2000);
                System.out.println("B上完了");
                cyclicBarrier.await();
                System.out.println("会议结束，B退出");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {

            }
        });
        threadPoolExecutor.submit(() -> {
            System.out.println("C在上厕所");
            try {
                Thread.sleep(3000);
                System.out.println("C上完了");
                cyclicBarrier.await();
                System.out.println("会议结束，C退出");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {

            }
        });
        //退出线程池
        threadPoolExecutor.shutdown();
        System.out.println("人都走完了，物业关灯。");
    }
}
