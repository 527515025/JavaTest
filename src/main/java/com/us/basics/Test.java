package com.us.basics;

/**
 * Created by yangyibo on 17/1/4.
 */
public class Test {
    public static void main(String[] args) {
//        string();
//        m(9);
//        replace();
        new Zi();
    }


    static void replace() {
        String str = "yang.abel";
        str = str.replace("c", "b");
        System.out.println(str + "---------");
    }

    static void string() {
        String name = "ROLE_EVENT_ALL";
        while (name.endsWith("ALL")) {
            System.out.println("true");
        }
    }

    static void m(Integer i) {
        if (i > 0) {
            System.out.println("---------------" + i);
            m(i - 1); //只执行到递归调用处。
        }
        System.out.println("++++++++++++++++" + i);
    }

    /**
     * 当调用子类的时候先走的是父类的构造方法，
     * 因此先走的是父类的构造方法，调用show方法，但是这里隐含了一个关键字，
     * 其实在Fu类的构造方法中调用show()的时候省略了关键字this,实际上是this.show();
     * 通过上述的介绍我们已经知道this代表的是子类的对象,因此这里调用子类的show方法，而不是父类的show方法！
     */
    static class Fu {//父类

        Fu() {
            show();
        }

        void show() {
            System.out.println("Fu");
        }
    }

    static class Zi extends Fu//子类继承父类
    {
        void show() {
            System.out.println("Zi");
        }
    }

}
