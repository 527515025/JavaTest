package com.us.basics.threadTest;

import com.us.Person;

/**
 * 本例子用于测试两个线程共用变量情况
 * 值传递和引用传递
 *
 * @author yyb
 * @time 2019/6/20
 */
public class ThreadTest {
    public static void main(String[] args) {
        main1();
//        main2();
    }


    /**
     * 用于测试多线程访问同一对象时，对象修改变化
     * 引用传递
     */
    private static void main1() {
        Person p = new Person();
        p.setName("abel");
        p.setAge(0);
        System.out.println("-0-----main1----"+p.getAge());
        createThread1(p);
        p.setAge(p.getAge() + 1);
        System.out.println("--1----main1----"+p.getAge());
        createThread2(p);
    }


    /**
     * 线程1
     *
     * @param para
     */
    private static void createThread1(Person para) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                pringln(para);
            }
        });
        thread.start();

    }

    /**
     * 线程2
     *
     * @param para
     */
    private static void createThread2(Person para) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                pringln(para);
            }
        });
        thread.start();
    }

    private static void pringln(Person para) {
        Thread t = Thread.currentThread();
        String name = t.getName();
        System.out.println(name + "----begin-----" + para.getAge());
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println(name + "-----end------" + para.getAge());
    }


/*****************************************************************************************************************************/


    /**
     * 用于测试多线程访问同一对象时，对象修改变化
     * 值传递
     */
    private static void main2() {
        String p = "asc";
        int i = 0;
        createThread3(p, i);
        p = "zxc";
        i++;
        createThread4(p, i);
    }


    /**
     * 线程1
     *
     * @param para
     */
    private static void createThread3(String para, Integer i) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                pringln2(para, i);
            }
        });
        thread.start();

    }

    /**
     * 线程2
     *
     * @param para
     */
    private static void createThread4(String para, Integer i) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                pringln2(para, i);
            }
        });
        thread.start();
    }

    private static void pringln2(String para, Integer i) {
        Thread t = Thread.currentThread();
        String name = t.getName();
        System.out.println(name + "----begin-----" + para + "----" + i);
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println(name + "-----end------" + para + "----" + i);
    }
}
