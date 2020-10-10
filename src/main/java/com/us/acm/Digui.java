package com.us.acm;

/**
 * Created by yangyibo on 2018/9/13.
 * 斐波那契数列
 *
 *
 * 又称黄金分割数列、费波那西数列、费波拿契数、费氏数列，指的是这样一个数列：0、1、1、2、3、5、8、13、21、
 * ……在数学上，斐波纳契数列以如下被以递归的方法定义：F0=0，F1=1，Fn=F(n-1)+F(n-2)（n>=2，n∈N*），
 * 用文字来说，就是斐波那契数列列由 0 和 1 开始，之后的斐波那契数列系数就由之前的两数相加。
 * 特别指出：0不是第一项，而是第零项。在现代物理、准晶体结构、化学等领域，斐波纳契数列都有直接的应用，
 * 为此，美国数学会从1960年代起出版了《斐波纳契数列》季刊，专门刊载这方面的研究成果。
 */
public class Digui {
    public static void main(String[] args) {
        System.out.println(fib(3));
    }


    /**
     * 当输入大于 47 以后 返回值已经超出int 的范围了。
     * @param i
     * @return
     */
    public static Long fib(int i) {
        if (i != 1 && i != 2) {
            return (fib(i - 1) + fib(i - 2));

        } else {
            return 1L;
        }
    }
}
