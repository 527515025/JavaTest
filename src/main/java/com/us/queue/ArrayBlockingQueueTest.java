package com.us.queue;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by yangyibo on 17/2/23.
 */
public class ArrayBlockingQueueTest {
    private static ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<>(5);

    public static void main(String[] args) {
        Thread readThread = new Thread(new ReadThread());
        readThread.start();
        for (int i = 0; i < 3; i++) {
            try {
                Thread writeThread = new Thread(new WriteThread());
                writeThread.start();
                Thread.sleep(20000);
            } catch (Exception e) {

            }
        }
    }

    private static List<String> init() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add("abel--" + i);
        }
        return list;
    }

    private static void println(String str) {
        System.out.println(str);
    }


    static class WriteThread extends Thread {

        @Override
        public void run() {
            init().stream().forEach(x -> {
                try {
                    println(x + "-------放入队列");
                    //存数据到队列当队列满了就阻塞,当数据被取走后,继续往队列中方数据
                    queue.put(x);
                    //queue.offer(x); 当队列满了以后,存不进去的元素就丢失了
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }


    static class ReadThread extends Thread {
        @Override
        public void run() {
            try {
                while (true) {
                    //不停的取出数据,当队列为空的时候阻塞,等待数据放入队列
                    Thread.sleep(1000);
                    println(queue.take() + "-----------取出队列");
                    //队列为空的时候阻塞
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}