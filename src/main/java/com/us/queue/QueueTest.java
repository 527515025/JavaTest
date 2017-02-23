package com.us.queue;


import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by yangyibo on 17/2/23.
 * <p>
 * add        增加一个元索                     如果队列已满，则抛出一个IIIegaISlabEepeplian异常
 * addall      增加一个 String 集合中的所有元素
 * remove     移除并返回队列头部的元素    如果队列为空，则抛出一个NoSuchElementException异常
 * removeAll;  删除队列中参数包含的元素
 * element    返回队列头部的元素             如果队列为空，则抛出一个NoSuchElementException异常
 * offer       添加一个元素并返回true       如果队列已满，则返回false
 * poll         移除并返问队列头部的元素    如果队列为空，则返回null
 * peek       返回队列头部的元素             如果队列为空，则返回null
 * put         添加一个元素                      如果队列满，则阻塞
 * take        移除并返回队列头部的元素     如果队列为空，则阻塞
 * <p>
 * <p>
 * remove、element、offer 、poll、peek 其实是属于Queue接口。
 */
public class QueueTest {
    public static void main(String[] args) {
        List<String> list=init();
//        queueTest(list);
//        arrayBlockingQueue(list);
        linkedBlockingQueue(list);
    }

    private static List<String> init() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add("abel--" + i);
        }
        return list;
    }

    private static void println(String str) {
        System.out.println("\n" + str);
    }


    private static void queueTest(List<String> list) {
        Queue<String> queue = new LinkedList<>();
        list.stream().forEach(x -> queue.offer(x));

        println("---------------------  1  --------------------------\n");
        println("队列中的元素是:" + queue);
        //移除首元素
        println(queue.poll());
        println(queue.poll());
        println("队列中的元素是:" + queue);

        println("---------------------  2  --------------------------\n");

        queue.offer("abel--" + 100);
        //删除队列中包含的list中的元素
        queue.removeAll(list);
        println("队列中的元素是:" + queue);

        println("---------------------  3  --------------------------\n");

        //增加一个string 集合
        queue.addAll(list);
        println(queue.peek());
        println("队列中的元素是:" + queue);

    }

    /**
     * 阻塞 queue
     * 阻塞操作put和take。put方法在队列满时阻塞，take方法在队列空时阻塞。
     * ArrayBlockingQueue在构造时需要指定容量
     *
     * @param list
     */
    private static void arrayBlockingQueue(List<String> list) {
        ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<String>(10);
        queue.addAll(list);
        println("---------------------  1  --------------------------\n");
        println("队列中的元素是:" + queue);

        println("---------------------  2  --------------------------\n");
        queue.remove("abel--0");
        println("队列中的元素是:" + queue);

        println("---------------------  3  --------------------------\n");


    }

    /**
     * 阻塞 queue
     * 阻塞操作put和take。put方法在队列满时阻塞，take方法在队列空时阻塞。
     * LinkedBlockingQueue在构造时需要指定容量 ,不指定容量时默认 Integer.MAX_VALUE
     */
    private static void linkedBlockingQueue(List<String> list) {
        LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>(9);
        list.stream().forEach(x -> {
            try {
                queue.put(x);
                //存到第10 个元素时阻塞
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        println("---------------------  1  --------------------------\n");
        println("队列中的元素是:" + queue);

    }

}
