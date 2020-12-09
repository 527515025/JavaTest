package com.us.basics.threadTest;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 主线程不阻塞等待
 * 测试  CyclicBarrier 的可重用
 * <p>
 * 结果
 * A在上厕所
 * B在上厕所
 * C在上厕所
 * 人都走完了，物业关灯。
 * B上完了
 * C上完了
 * A上完了
 * -------------------
 * 会议结束，A退出，开始撸代码
 * 会议结束，B退出，开始摸鱼
 * 会议结束，C退出，开始摸鱼
 * -------------------
 * C摸鱼结束，下班回家
 * A工作结束，下班回家
 * B摸鱼结束，下班回家
 *
 * @author yyb
 * @time 2020/12/8
 */
public class CyclicBarrierPartiesTest {
    private static int num = 3;
    private static CyclicBarrier cyclicBarrier = new CyclicBarrier(num, () -> {
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
                System.out.println("会议结束，A退出，开始撸代码");
                cyclicBarrier.await();
                System.out.println("A工作结束，下班回家");
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
                System.out.println("会议结束，B退出，开始摸鱼");
                cyclicBarrier.await();
                System.out.println("B摸鱼结束，下班回家");
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
                System.out.println("会议结束，C退出，开始摸鱼");
                cyclicBarrier.await();
                System.out.println("C摸鱼结束，下班回家");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {

            }
        });

        threadPoolExecutor.shutdown();
        System.out.println("人都走完了，物业关灯。");
    }
}
