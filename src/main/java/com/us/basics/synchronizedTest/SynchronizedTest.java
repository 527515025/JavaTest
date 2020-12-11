package com.us.basics.synchronizedTest;

/**
 * Java的锁分为对象锁和类锁。
 * Created by yangyibo on 17/12/28.
 * <p>
 * 1. 当两个并发线程访问同一个对象object中的这个synchronized(this)同步代码块时，一个时间内针对该对象的操作只能有一个线程得到执行。另一个线程必须等待当前线程执行完这个代码块以后才能执行该代码块。
 * 　  2. 然而，另一个线程仍然可以访问该object中的非synchronized(this)同步代码块。
 * 　　3. 尤其关键的是，当一个线程访问object的一个synchronized(this)同步代码块时，其他线程对该object中所有其它synchronized(this)同步代码块的访问将被阻塞。 例子：objectLock2()
 *
 * 　　4. 同步加锁的是对象，而不是代码。因此，如果你的类中有一个同步方法，这个方法可以被两个不同的线程同时执行，只要每个线程自己创建一个的该类的实例即可。 例子：objectLock2()
 *
 * 　　5. 不同的对象实例的synchronized方法是不相干扰的。也就是说，其它线程照样可以同时访问相同类的另一个对象实例中的synchronized方法；例子：objectLock2()
 * 　　6. synchronized关键字是不能继承的，也就是说，基类的方法synchronized f(){} 在继承类中并不自动是synchronized f(){}，而是变成了f(){}。继承类需要你显式的指定它的某个方法为synchronized方法。
 * 　　7.对一个全局对象或者类加锁时，对该类的所有对象都起作用。所有的对象都要阻塞的顺序访问类锁代码。但是其他 对象锁的方法仍可访问， 例子：classTest3()
 * <p>
 * <p>
 * 类锁：不同的线程，访问使用类锁的方法的时候，他们获取到的“锁”，其实是Class对象。因为同一个类中有且只有一个Class对象，但同一个类中可以有很多个其他对象。
 * 此时，就出现了同一个类中多个对象对Class对象使用的竞争，类锁则保证了在同一时间，只允许一个线程访问被类锁锁住的方法。 例子：classTest3()
 */
public class SynchronizedTest {
    public static void main(String[] args) {
//        objectLock();
//        objectLock2();
//        objectLock3();
//        classTest();
//        classTest2();
//        reentrantTest();
        reentrantTest2();
    }


    /**
     * 测试对象锁
     * 用于测试对象锁时，可不可以调用实例对象的其他方法。
     * 结果：thread1 获取锁后，thread2 扔可以执行 add 方法
     */
    private static void objectLock() {
        Test1 test1 = new Test1();

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                test1.objectLock();
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                test1.add();
            }
        });
        thread1.start();
        thread2.start();
    }

    /**
     * 测试对象锁
     * 用于测试锁住同一方法是否可以两个线程执行
     * 结果：可以两个线程同时执行同一代码块，但是不同的实例可以执行
     */
    private static void objectLock2() {
        Test1 test1 = new Test1();

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                test1.objectLock();
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                test1.objectLock();
            }
        });
        thread1.start();
        thread2.start();
    }

    /**
     * 测试对象锁
     * 用于测试锁住同一方法是否可以两个线程用两个实例同时执行
     * 结果：被锁住的方法，线程顺序执行，thread1 获取锁， thread2 等待
     */
    private static void objectLock3() {
        Test1 test1 = new Test1();

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                test1.objectLock();
            }
        });
        Test1 test2 = new Test1();
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                test2.objectLock();
            }
        });
        thread1.start();
        thread2.start();
    }


    /**
     * 测试类锁
     * 用于测试类锁时，可不可以调用其他方法。
     * 结果：thread1 获取类锁后，thread2 扔可以执行 add 方法
     */
    public static void classTest() {
        Test1 test1 = new Test1();

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                Test1.classLock();
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                test1.add();
            }
        });
        thread1.start();
        thread2.start();
    }


    /**
     * 测试类锁
     * 用于测试类锁时，创建两个对象实例调用其锁方法。
     * 结果：thread1 获取类锁后，thread2 等到thread 释放锁后才可以执行锁方法
     */
    public static void classTest2() {

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                Test1.classLock();
            }
        });
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                Test1.classLock();
            }
        });
        thread1.start();
        thread2.start();
    }


    /**
     * 测试类锁
     * 用于测试类锁时，锁到类对象后，其他对象仍可访问非类锁的锁对象
     * 结果：thread1 获取类锁后，thread2 等到thread 释放锁后才可以执行锁方法
     */
    public static void classTest3() {

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                Test1.classLock();
            }
        });

        Test1 test1 = new Test1();
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                test1.objectLock();
            }
        });
        thread1.start();
        thread2.start();
    }


    /**
     * 测试可重入，锁着对象，然后再锁类锁，仍然可以重入
     */
    private static void reentrantTest() {
        Test1 test1 = new Test1();
        test1.reentrantTest();
    }

    /**
     * 两个线程，两个对象，可以进入各自的对象锁，但是类锁两个对象是公用的，所以顺序执行类锁代码
     * 测试可重入，锁着对象，然后再锁类锁，仍然可以重入
     */
    private static void reentrantTest2() {
        Test1 test1 = new Test1();
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                test1.reentrantTest();
            }
        });

        Test1 test2 = new Test1();
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                test2.reentrantTest();
            }
        });
        thread1.start();
        thread2.start();
    }

}
