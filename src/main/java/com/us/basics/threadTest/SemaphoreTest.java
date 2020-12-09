package com.us.basics.threadTest;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 会阻塞主线程
 * Semaphore叫做信号量，他的计数器是递增的
 * **构造函数传入的初始值为0**，当子线程**调用release()方法时，计数器递增**，主线程**acquire()传参为3则说明主线程一直阻塞，直到计数器为3才会返回**。
 * <p>
 * <p>
 * <p>
 * A在上厕所
 * B在上厕所
 * C在上厕所
 * 等待所有人从厕所回来开会...
 * B上完了
 * C上完了
 * A上完了
 * 所有人都好了，开始开会...
 *
 * @author yyb
 * @time 2020/12/8
 */
public class SemaphoreTest {
    private static int num = 3;
    private static int initNum = 0;
    /**
     * 初始化为0个信号量
     */
    private static Semaphore semaphore = new Semaphore(initNum);
    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(num, num,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>());

    public static void main(String[] args) throws Exception {
        threadPoolExecutor.submit(() -> {
            System.out.println("A在上厕所");
            try {
                Thread.sleep(4000);
                //释放一个许可 给信号量
                semaphore.release();
                System.out.println("A上完了");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {

            }
        });
        threadPoolExecutor.submit(() -> {
            System.out.println("B在上厕所");
            try {
                Thread.sleep(2000);
                //释放一个许可 给信号量
                semaphore.release();
                System.out.println("B上完了");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {

            }
        });
        threadPoolExecutor.submit(() -> {
            System.out.println("C在上厕所");
            try {
                Thread.sleep(3000);
                //释放一个许可 给信号量
                semaphore.release();
                System.out.println("C上完了");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {

            }
        });

        System.out.println("等待所有人从厕所回来开会...");
        //判断信号量的许可是不是等于设置的3，如果是则继续运行，否则阻塞，
        semaphore.acquire(num);
        System.out.println("所有人都好了，开始开会...");

        threadPoolExecutor.shutdown();
        System.out.println("下班都回家了...");

    }
}
