package com.us.acm;

import java.util.Scanner;

/**
 * Created by yangyibo on 2018/11/29.
 */
public class DrinkBeer {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int t = sc.nextInt();
        int count = 0, o = 0, k = 0;
        count = t / 2;//喝的瓶数
        k = o = count;//k瓶盖数 ，o空瓶数,count 买的瓶数
        while (k / 4 >= 1 || o / 2 >= 1) {
            if (o / 2 >= 1)//计算瓶子
            {
                count += o / 2;
                k = k + o / 2;
                o = o % 2 + o / 2;
            }
            if (k / 4 >= 1) { //计算瓶盖
                count += k / 4;
                o += k / 4;
                k = k % 4 + k / 4;
            }
        }
        System.out.println("啤酒数：" + count + "空瓶数： " + o + "瓶盖数： " + k);
    }
}

